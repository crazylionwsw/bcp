/**
 * 渠道刷卡
 */
'use strict';
app.controller('swipingcardController',['$scope','swipingcardService','orderService','bankcardService','customerService','customerTransactionService','$modal','$localStorage','$stateParams','$loading','$rootScope','$q',function ($scope,swipingcardService,orderService,bankcardService,customerService,customerTransactionService,$modal,$localStorage,$stateParams,$loading,$rootScope,$q) {

    $scope.title = "渠道刷卡";

    //返回的列表数据
    $scope.swipingcards = [];
    $scope.swipingcard = null;

    //查询参数
    $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A019'};
    $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A019'};

    $scope.currentPage = 1;

    //客户还款信息
    $scope.customerRepaymentInfo = {};
    //刷卡金额
    $scope.totalSwipingMoney = 0.0;
    //贷款金额合计
    $scope.totalCreditAmount = 0.0;

    $scope.canAudit= true;

    $scope.init = function () {
        if ($localStorage.SwipingCardCustomerFilter){
            $scope.searchFilter = $localStorage.SwipingCardCustomerFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }
        $scope.changePage(1);
        if ($stateParams.tid) { //从交易列表打开
            swipingcardService.getOneByCustomerTransactionId($stateParams.tid, function(data){
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
        $scope.totalSwipingMoney= 0.0;
        $scope.totalCreditAmount = 0.0;

        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        swipingcardService.search($scope.searchFilter,$scope.currentPage-1,function (data){
            $scope.swipingcards = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.swipingcards, function(bills){
                $scope.swipingcards = bills;
                $rootScope.$broadcast("CalTotal", bills);
            })
        });

        $localStorage.SwipingCardCustomerFilter = $scope.searchFilter;
    };

    //发送广播，计算刷卡金额,与贷款金额
    $scope.$on('CalTotal',function (e,bills) {
        if (bills){
            angular.forEach(bills,function (item, index) {
                bankcardService.getOneByCustomerTransactionId(item.customerTransactionId, function (data) {
                    if (data) {
                        $scope.addTotal(data.swipingMoney);
                    }
                });

                customerService.getCustomerLoanByCustomerTransactionId(item.customerTransactionId, function (data) {
                    if (data) {
                        $scope.addCreditAmountTotal(data.creditAmount);
                    }
                })
            })
        }
    });

    //刷卡金额合计
    $scope.addTotal = function (amount) {
        if(amount)
            $scope.totalSwipingMoney = $scope.totalSwipingMoney+ amount;
    };

    //贷款金额合计
    $scope.addCreditAmountTotal = function (amount) {
        if(amount)
            $scope.totalCreditAmount = $scope.totalCreditAmount+ amount;
    };

    /*  编辑页面   */
    $scope.edit = function (swipingcard,index) {
        if (!$scope.checked){
            if ($scope.swipingcard){
                $scope.swipingcard = {};
            }
            swipingcardService.getOne(swipingcard.id,function (asc) {
                $scope.swipingcard = asc;
                $scope.checked = true;

                //获取还款信息
                if($scope.swipingcard.customerTransactionId){

                    var customerTransactionId = $scope.swipingcard.customerTransactionId;
                    customerTransactionService.getOne(customerTransactionId,function (transaction) {
                        $scope.swipingcard.transaction = transaction;
                    });
                    customerService.getRepaymentInfoByCustomerTransactionId(customerTransactionId,function (data) {
                        $scope.swipingcard.customerRepaymentInfo = data;
                    });
                    //      获取  获取客户签约信息
                    orderService.getOneByCustomerTransactionId(customerTransactionId,function (order) {
                        $scope.swipingcard.order = order;
                    });

                    customerService.getCustomerLoanByCustomerTransactionId(customerTransactionId,function (loan) {
                        $scope.swipingcard.loan = loan;
                    })
                }
            })
        }
    };

    /**
     * 关闭页面
     */
    $scope.close = function () {
        //更新列表数据
        swipingcardService.getOne($scope.swipingcard.id,function (data) {
            angular.forEach($scope.swipingcards, function(item, index){
                if (item.id == data.id) {
                    if($scope.searchFilter.statusValue == -1){
                        $scope.swipingcards[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.status){
                        $scope.swipingcards.splice(index,1);
                    }else {
                        $scope.swipingcards[index] = data;
                    }

                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.swipingcards[index].transaction = transaction;
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

    $scope.init();

    /**
     * 查询
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
            size:'lg',
            animation:true,
            templateUrl:"query.html",
            controller:function($scope,$modalInstance){
                //we do nothing now!
            }
        })
    };

    /**
     * 清空查询
     */
    $scope.clearQuery = function() {
        $scope.searchFilter = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'status',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A019'};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param search
     */
    $scope.queryBill = function (search) {
        $scope.searchFilter = search;
        $scope.changePage(1);
    };

    /**
     * 审核/审批
     */
    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        swipingcardService.sign(id, signinfo, function (data) {
            $loading.finish('signInfo');
            if (id && presentTaskGroup  && signinfo.result == 2){
                if (presentTaskGroup == "渠道刷卡确认"){
                    //status = 2;
                    $scope.saveStatus(id,2);
                }
            }
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        });
    });

    $scope.saveStatus = function (id, status) {
        swipingcardService.saveStatus(id,status,function (data) {
            if (data){
                $scope.searchFilter.statusValue = data.status;
            }
        })
    };

}]);

