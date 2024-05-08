/**
 * Created by gqr on 2017/8/17.
 */
/**
 * 渠道还款
 */
'use strict';
app.controller('dealerRepaymentController',['$scope','dealerRepaymentService','orderService','bankcardService','customerService','appointpaymentService','cardealerService','customerTransactionService','$modal','$localStorage','$stateParams','$loading','$rootScope',function ($scope,dealerRepaymentService,orderService,bankcardService,customerService,appointpaymentService,cardealerService,customerTransactionService,$modal,$localStorage,$stateParams,$loading,$rootScope) {

    $scope.title = "渠道还款";

    //返回的列表数据
    $scope.dealerRepayments = [];
    $scope.dealerRepayment = {};

    //返回工作流查询集合
    $scope.taskList = [];
    $scope.istask = {};

    //查询参数
    $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A020'};
    $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A020'};

    //贷款金额合计
    $scope.totalCreditAmount = 0.0;
    //垫资金额合计
    $scope.totalAppointPaymentMoney = 0.0;

    $scope.currentPage=1;

    //客户还款信息
    $scope.customerRepaymentInfo = {};

    $scope.canAudit= true;

    $scope.init = function () {
        if ($localStorage.DealerRepaymentSearchFilter){
            $scope.searchFilter = $localStorage.DealerRepaymentSearchFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        $scope.changePage(1);

        if ($stateParams.tid) { //从交易列表打开
            dealerRepaymentService.getOneByCustomerTransactionId($stateParams.tid, function(data){
                if (data && data.dataStatus == 1){
                    $scope.edit(data);
                } else {
                    alert("分期经理未提交该阶段数据！");
                }
            })
        }
    };

    //还款金额合计
    $scope.getPayTotal=function(){
        var total = 0;
        for(var i = 0; i < $scope.dealerRepayments.length; i++){
            total +=  $scope.dealerRepayments[i].amount;
        }
        return total;
    };

    //贷款金额合计
    $scope.addCreditAmountTotal = function (amount) {
        if(amount)
            $scope.totalCreditAmount = $scope.totalCreditAmount+ amount;
    };
    //垫资金额合计
    $scope.addTotal = function (amount) {
        if(amount)
            $scope.totalAppointPaymentMoney = $scope.totalAppointPaymentMoney + amount;
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){
        $scope.totalCreditAmount = 0.0;
        $scope.totalAppointPaymentMoney = 0.0;

        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        dealerRepaymentService.search($scope.searchFilter,$scope.currentPage-1,function (data){
            $scope.dealerRepayments = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.dealerRepayments, function(bills){
                $scope.dealerRepayments = bills;
                $rootScope.$broadcast("CalTotal", bills);
            })
        });

        $localStorage.DealerRepaymentSearchFilter = $scope.searchFilter;
    };

    //发送广播，计算刷卡金额
    $scope.$on('CalTotal',function (e,bills) {
        if (bills){
            angular.forEach(bills,function (item, index) {
                appointpaymentService.getOneByCustomerTransactionId(item.customerTransactionId, function (data) {
                    if (data) {
                        $scope.addTotal(data.appointPayAmount);
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

    /*  编辑页面   */
    $scope.edit = function (dealerRepayment,index) {
        if (!$scope.checked){
            if ($scope.dealerRepayment){
                $scope.dealerRepayment = {};
            }
            dealerRepaymentService.getOne(dealerRepayment.id,function (asc) {
                $scope.dealerRepayment = asc;
                $scope.checked = true;
                //获取还款信息
                if($scope.dealerRepayment.customerTransactionId){
                    var customerTransactionId = $scope.dealerRepayment.customerTransactionId;
                    customerTransactionService.getOne(customerTransactionId,function (transaction) {
                        $scope.dealerRepayment.transaction = transaction;
                    });
                    customerService.getRepaymentInfoByCustomerTransactionId(customerTransactionId,function (data) {
                        $scope.customerRepaymentInfo = data;
                    });

                    //      获取  获取客户签约信息
                    orderService.getOneByCustomerTransactionId(customerTransactionId,function (order) {
                        $scope.dealerRepayment.order = order;
                    });

                    //获取预约垫资时间
                    appointpaymentService.getOneByCustomerTransactionId(customerTransactionId,function (appointpayment) {
                        $scope.dealerRepayment.appointpayment = appointpayment;
                        //取得经销商信息
                        if (appointpayment && appointpayment.carDealerId) {
                            cardealerService.getOne($scope.dealerRepayment.appointpayment.carDealerId, function (carDealer) {
                                $scope.dealerRepayment.appointpayment.carDealer = carDealer;

                                if (!$scope.dealerRepayment.appointpayment.payAccount) {
                                    angular.forEach($scope.dealerRepayment.appointpayment.carDealer.payAccounts, function (item) {
                                        if (item.defaultAccount == 0) {
                                            $scope.dealerRepayment.appointpayment.payAccount = item;
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    };

    /**
     * 关闭页面
     */
    $scope.close = function () {
        dealerRepaymentService.getOne($scope.dealerRepayment.id,function (data) {
            angular.forEach($scope.dealerRepayments, function(item, index){
                if (item.id == data.id) {
                    if($scope.searchFilter.statusValue == -1){
                        $scope.dealerRepayments[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.status){
                        $scope.dealerRepayments.splice(index,1);
                    } else {
                        $scope.dealerRepayments[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        if ($scope.dealerRepayments[index])
                            $scope.dealerRepayments[index].transaction = transaction;
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
        $scope.searchFilter = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'status',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A020'};
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

    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        dealerRepaymentService.sign(id, signinfo, function (data) {
            $loading.finish('signInfo');
            if (id && presentTaskGroup  && signinfo.result == 2){
                if (presentTaskGroup == "渠道还款确认"){
                    $scope.saveStatus(id,2);
                }
            }
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        });
    });

    $scope.saveStatus = function (id, status) {
        dealerRepaymentService.saveStatus(id,status,function (data) {
            if (data && data.status){
                $scope.dealerRepayment.status = data.status;
            }
        })
    };

    //更新审批状态
    $scope.$on('UpdateSignInfo',function (e, id) {
        dealerRepaymentService.getOne(id,function (data) {
            $scope.dealerRepayment.status = data.status;
        })
    });

}]);

