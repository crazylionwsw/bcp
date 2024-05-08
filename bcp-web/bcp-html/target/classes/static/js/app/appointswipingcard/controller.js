/**
 * Created by gqr on 2017/8/17.
 */
/**
 * 预约刷卡
 */
'use strict';
app.controller('appointswipingcardController',['$scope','appointswipingcardService','orderService','bankcardService','customerService','customerTransactionService','employeeService','orginfoService','$modal','$localStorage','$stateParams','$loading','$rootScope','$q','toaster',function ($scope,appointswipingcardService,orderService,bankcardService,customerService,customerTransactionService,employeeService,orginfoService,$modal,$localStorage,$stateParams,$loading,$rootScope,$q,toaster) {

    $scope.title = "预约刷卡";

    //返回的列表数据
    $scope.appointswipingcards = [];
    $scope.appointswipingcard = null;

    //返回工作流查询集合
    $scope.taskList = [];
    $scope.istask = {};
    //当前页默认
    $scope.currentPage=1;

    //贷款金额合计
    $scope.totalCreditAmount = 0.0;
    //领卡人
    $scope.businessNames = [];
    $scope.customerNames = [];
    //卡信息
    $scope.bankcard = {};
    $scope.cusShow = true;
    $scope.sumType = false;
    $scope.businessName = [];
    $scope.customerName = [];
    $scope.receiveCardName = null;
    //分配领卡人
    $scope.receiveCard = true;


    //查询参数
    $scope.search = {timeName:'appointPayTime',compensatory:true,business:true,statusValue:1,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A026'};
    $scope.searchFilter = {timeName:'appointPayTime',compensatory:true,business:true,statusValue:1,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A026'};

    $scope.canAudit= true;

    $scope.init = function () {

        if($localStorage.AppointSwipingCardSearchFilter){
            $scope.searchFilter = $localStorage.AppointSwipingCardSearchFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }
        $scope.changePage(1);
        
        if ($stateParams.tid) { //从交易列表打开
            appointswipingcardService.getOneByCustomerTransactionId($stateParams.tid, function(data){
                if (data && data.dataStatus == 1){
                    $scope.edit(data);
                } else {
                    alert("分期经理未提交该阶段数据！");
                }
            })
        }
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){

        $scope.totalCreditAmount = 0.0;

        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        appointswipingcardService.search($scope.searchFilter,$scope.currentPage-1,function (data){
            $scope.appointswipingcards = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.appointswipingcards, function(bills){
                $scope.appointswipingcards = bills;
                $rootScope.$broadcast("CalTotal", bills);
            })
        });

        $localStorage.AppointSwipingCardSearchFilter = $scope.searchFilter;
    };

    //发送广播，计算金额合计
    $scope.$on('CalTotal',function (e,bills) {
        if (bills){
            angular.forEach(bills,function (item, index) {
                customerService.getCustomerLoanByCustomerTransactionId(item.customerTransactionId, function (data) {
                    if (data) {
                        $scope.addTotal(data.creditAmount);
                    }
                })
            })
        }
    });

    //贷款金额合计
    $scope.addTotal = function (amount) {
        if(amount)
            $scope.totalCreditAmount = $scope.totalCreditAmount+ amount;
    };


    /**
     * 查詢
     */
    $scope.search = function() {
        $scope.changePage(1);
    };

    $scope.changePageSize = function () {
        $scope.searchFilter.pageSize = $scope.pageSize;
        $scope.changePage(1);
    };

    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
            animation:true,
            size:'lg',
            templateUrl:"query.html",
            controller:function($scope,$modalInstance){
                //we do nothing now!
            }
        })
    };

    $scope.clearQuery = function() {
        $scope.searchFilter = {timeName:'appointPayTime',creditMonths:0,compensatory:true,business:true,statusName:'status',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A026'};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param customer
     */
    $scope.queryBill = function (search) {
        $scope.searchFilter = search;
        $scope.changePage(1);
    };
    
    /*  编辑页面   */
    $scope.edit = function (appointswipingcard,index) {
        if (!$scope.checked){
            if ($scope.appointswipingcard){
                $scope.appointswipingcard = {};
            }
            if ($scope.order){
                $scope.order = {};
            }

            $rootScope.$broadcast("ShowReceiveCard",appointswipingcard);

            appointswipingcardService.getOne(appointswipingcard.id,function (asc) {
                $scope.appointswipingcard = asc;
                $scope.checked = true;

                if ($scope.appointswipingcard.customerTransactionId) {
                    var customerTransactionId = $scope.appointswipingcard.customerTransactionId;
                    customerTransactionService.getOne(customerTransactionId,function (transaction) {
                        $scope.appointswipingcard.transaction = transaction;
                    });
                    //      获取  获取客户签约信息
                    orderService.getOneByCustomerTransactionId(customerTransactionId,function (order) {
                        $scope.order = order;
                    });
                }
            })
        }
    };

    //接收广播，判断是否已经分配了领卡人（按钮显示）
    $scope.$on('ShowReceiveCard',function (e,appointswipingcard) {
        bankcardService.getOneByCustomerTransactionId(appointswipingcard.customerTransactionId,function (receive) {
            $scope.bankcard = receive;
            if ($scope.bankcard && $scope.bankcard.receiveCardName == null && appointswipingcard.status == 2){
                $scope.receiveCard = true;
            }else {
                $scope.receiveCard = false;
            }
        });
    });

    $scope.init();

    /**
     * 关闭页面
     */
    $scope.close = function () {
        //更新列表数据
        appointswipingcardService.getOne($scope.appointswipingcard.id,function (data) {
            angular.forEach($scope.appointswipingcards, function(item, index){
                if (item.id == data.id) {
                    if($scope.searchFilter.statusValue == -1){
                        $scope.appointswipingcards[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.status){
                        $scope.appointswipingcards.splice(index,1);
                    } else {
                        $scope.appointswipingcards[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.appointswipingcards[index].transaction = transaction;
                    });
                }
            });
            
        });
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    /**
     * 审核/审批
     */
    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        appointswipingcardService.sign(id, signinfo, function (data) {
            $loading.finish('signInfo');
            /*if (id && presentTaskGroup && signinfo.result == 2){
                if (presentTaskGroup == "预约刷卡确认"){
                    //status = 2;
                    $scope.saveStatus(id,2);
                }
            }*/
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);

        });
    });

    $scope.saveStatus = function (id, status) {
        //记录领卡人信息
        if (status == 2){
            //判断当前的卡业务流程，判断当前是否已经有了领卡人
            bankcardService.getOneByCustomerTransactionId($scope.appointswipingcard.customerTransactionId,function (data2) {
                $scope.customercard = data2;
                if (data2.receiveCardName == null){
                    if(confirm("是否要分配领卡人？")){
                        $scope.createReceiveCard();
                    }else{
                        $rootScope.$broadcast("ShowReceiveCard",$scope.appointswipingcard);
                    }
                }else {
                    if(confirm("是否要修改领卡人？")){
                        $scope.createReceiveCard();
                    }else {
                        $rootScope.$broadcast("ShowReceiveCard",$scope.appointswipingcard);
                    }
                }
            })
        }
        $scope.appointswipingcard.status = data.status;
    };

    //分配领卡人
    $scope.createReceiveCard = function () {
        $scope.modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            size: 'lg',
            backdrop: 'static',
            templateUrl: 'tpl/view/appointswipingcard/man.html',
            controller: function ($scope, $modalInstance) {
                //员工领卡（所有的分期经理）
                employeeService.getStageManager(function (StageManager) {
                    $scope.businessNames = StageManager;
                });

                if($scope.bankcard.receiveCardName == null){
                    //领卡人默认为客户本人
                    customerService.getOne($scope.appointswipingcard.customerId,function (cus) {
                        if(cus){
                            $scope.bankcard.receiveCardName = cus.name;
                        }
                    });
                }

                //切换
                $scope.changeType = function () {
                    if($scope.cusShow == true){
                        $scope.cusShow = false;
                    }else if($scope.cusShow == false){
                        $scope.cusShow = true;
                    }
                    //领卡人的默认
                    if($scope.cusShow == false){ //如果为员工领卡
                        //领卡人默认为当前的分期经理
                        orginfoService.getEmployee($scope.appointswipingcard.employeeId,function (fenqi) {
                            if(fenqi){
                                $scope.bankcard.receiveCardName = fenqi.username;
                            }
                        });
                    }else {
                        //客户领卡（判断是否已经有了领卡人）
                        bankcardService.getOneByCustomerTransactionId($scope.appointswipingcard.customerTransactionId,function (aa) {
                            //领卡人默认为当前的领卡人
                            if(aa && aa.receiveCardName != null){
                                $scope.bankcard.receiveCardName = aa.receiveCardName;
                            } else {
                                //领卡人默认为客户本人
                                customerService.getOne($scope.appointswipingcard.customerId,function (cus) {
                                    if(cus){
                                        $scope.bankcard.receiveCardName = cus.name;
                                    }
                                });
                            }
                        });
                    }
                };

                /*  取消 */
                $scope.cancel = function ($modalInstance) {
                    $rootScope.$broadcast("ShowReceiveCard",$scope.appointswipingcard);
                    $scope.modalInstance.close();
                };

                //保存领卡人
                $scope.save = function() {
                    bankcardService.saveReceiveCardName($scope.appointswipingcard.customerTransactionId,$scope.bankcard.receiveCardName, function (data) {
                        if (data) {
                            $scope.bankcard = data;
                            toaster.pop('success','领卡人','保存成功！');
                            $rootScope.$broadcast("ShowReceiveCard",$scope.appointswipingcard);
                            $scope.modalInstance.close();
                        }
                    });
                };
            }
        })
    }
}]);

