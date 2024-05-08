/**
 * Created by LB on 2016-10-24.
 */

'use strict';
app.controller('businessexchangeController', ['$scope', 'businessexchangeService','orderService', 'customerService', 'workflowService','customerTransactionService', '$state', '$modal', 'toaster', '$localStorage', '$stateParams','$loading','$rootScope','$q',
    function ($scope, businessexchangeService,orderService, customerService, workflowService,customerTransactionService, $state, $modal, toaster, $localStorage,$stateParams,$loading,$rootScope,$q) {

    $scope.title = '业务调整';
    /*列表*/
    $scope.businessexchanges = [];//返回的列表数据
    $scope.businessexchange = null;
    $scope.cardemand = null;
    $scope.cardemandSignShow = 0;
    $scope.order = null;

    //查询参数
    $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A030'};
    $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A030'};

    $scope.currentPage = 1;
    $scope.approveStatus = 1;

    //返回工作流查询集合
    $scope.taskList = [];
    $scope.istask = {};

    //    右滑  模态框   的展示选项
    $scope.checked = false;

    //      从系统参配项中 获取系统参数，用于页面下拉框数据展示
    $scope.bankChargePaymentWays = [];
    $scope.compensatoryWays = [];
    $scope.compensatoryMonths = [];
    $scope.sourceIncomeWays = [];
    $scope.receiptDatas = [];

    $scope.presentTask = null;

    //贷款金额合计
    $scope.totalCreditAmount = 0.0;
    //银行手续费合计
    $scope.totalBankFeeAmount = 0.0;
    //贷款服务费合计
    $scope.totalLoanServiceFee = 0.0;

    $scope.customertypes = [];

    /**
     * 初始化加载列表和分页
     */
    $scope.init = function () {

        if ($localStorage.BusinessexchangeSearchFilter){
            $scope.searchFilter = $localStorage.BusinessexchangeSearchFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        $scope.changePage(1);
    };

    /*分页功能*/
    $scope.changePage = function (pageIndex) {

        //合计清零
        $scope.totalCreditAmount = 0.0;
        $scope.totalBankFeeAmount = 0.0;
        $scope.totalLoanServiceFee = 0.0;

        if (pageIndex) {
            $scope.currentPage = pageIndex;
        }
        businessexchangeService.search($scope.searchFilter,$scope.currentPage-1, function (data) {
            $scope.businessexchanges = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.businessexchanges, function(bills){
                $scope.businessexchanges = bills;
                $rootScope.$broadcast("CalTotal", bills);
            })
        });

        $localStorage.BusinessexchangeSearchFilter = $scope.searchFilter;
    };

    //发送广播，计算贷款金额
    $scope.$on('CalTotal',function (e,bills) {
        if (bills){
            angular.forEach(bills,function (item, index) {
                customerService.getCustomerLoanByCustomerTransactionId(item.customerTransactionId, function (data) {
                    if (data) {
                        $scope.addTotal(data.creditAmount);
                    }
                });
                customerService.getCustomerLoanByCustomerTransactionId(item.customerTransactionId,function (data) {
                    if(data){
                        $scope.addBankFeeTotal(data.bankFeeAmount);
                    }
                });
                customerService.getCustomerLoanByCustomerTransactionId(item.customerTransactionId,function (data) {
                    if(data){
                        $scope.addLoanServiceFeeTotal(data.loanServiceFee);
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
    //银行手续费合计
    $scope.addBankFeeTotal = function (amount) {
        if(amount)
            $scope.totalBankFeeAmount = $scope.totalBankFeeAmount+ amount;
    };
    //贷款服务费合计
    $scope.addLoanServiceFeeTotal = function (amount) {
        if(amount)
            $scope.totalLoanServiceFee = $scope.totalLoanServiceFee+ amount;
    };


    //     查看  客户签约详细
    $scope.edit = function (businessexchange) {

        if (!$scope.checked) {
            if ($scope.businessexchange) {
                $scope.businessexchange = {};
            }

            //获取客户签约信息
            orderService.getOneByCustomerTransactionId(businessexchange.customerTransactionId,function (orderData) {
               $scope.order = orderData;
            });

            businessexchangeService.getOne(businessexchange.id, function (data) {
                $scope.businessexchange = data;
                $scope.checked = !$scope.checked;


                /*   查询  客户购车信息   */
                if (data.customerExchangeCarId) {
                    customerService.getBusinessExchangeCar(data.customerExchangeCarId, function (car) {
                        $scope.businessexchange.car = car;
                    })
                }
                /*    查询    客户交易信息  */
                if (data.customerExchangeLoanId) {
                    customerService.getBusinessExchangeLoan(data.customerExchangeLoanId, function (loan) {
                        $scope.businessexchange.loan = loan;
                    })
                }
                customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                    $scope.businessexchange.transaction = transaction;
                });

                if (data.customerId) {
                    customerService.getOne(data.customerId, function (customer) {
                        $scope.businessexchange.customer = customer;
                    })
                }

                workflowService.getTaskCurrentGroup(data.billTypeCode + '.' + data.id, function (data) {
                    $scope.presentTask = data;
                });
            })
        }
    };
    
    $scope.createFileNumber = function (transactionId) {
        customerTransactionService.createFileNumber(transactionId,function (data) {
            $scope.businessexchange.transaction.fileNumber = data.fileNumber;
        })
    };


    /*        关闭  右边框      */
    $scope.close = function () {
        businessexchangeService.getOne($scope.businessexchange.id,function (data) {
            angular.forEach($scope.businessexchanges, function(item, index){
                if (item.id == data.id) {
                    if($scope.searchFilter.statusValue == -1){
                        $scope.businessexchanges[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.approveStatus){
                        $scope.businessexchanges.splice(index,1);
                    }else {
                        $scope.businessexchanges[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.businessexchanges[index].transaction = transaction;
                    });
                }
            });
        });

        $scope.checked = false;
    };

    //详情
    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    /**
     * 执行初始化
     */
    $scope.init();

    /**
     * 审批状态查詢
     */
    $scope.search = function () {
        $scope.changePage(1);
    };

    $scope.changePageSize = function () {
        $scope.searchFilter.pageSize = $scope.pageSize;
        $scope.changePage(1);
    };

    /*          模糊查询        START           */
    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope: $scope,
            size:'lg',
            animation: true,
            templateUrl: "query.html",
            controller: function ($scope, $modalInstance) {
                //we do nothing now!
            }
        })
    };

    /**
     * 清空查询
     */
    $scope.clearQuery = function () {
        $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,creditMonths:0,statusName:'approveStatus',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A030'};
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

    $scope.$on('BillSign',function (e,id,signInfo,presentTaskGroup) {
        $loading.start('signInfo');
        businessexchangeService.sign(id, signInfo, function (data) {
            if(data.m){
                alert(data.m);
            }
            $loading.finish('signInfo');
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);

            //发送更新车辆信息的广播

            //发送更新贷款信息的广播

        });
    });

    //业务调整单审批的广播
    $scope.$on('UpdateSignInfo', function (e, id) {
        businessexchangeService.getOne(id, function (data) {
            $scope.businessexchange.approveStatus = data.approveStatus;
        });
        //更新当前任务
        workflowService.getTaskCurrentGroup(data.billTypeCode + '.' + data.id, function (data) {
            $scope.presentTask = data;
        });
    });

    $scope.showCarDemandSignInfos = function (value) {
        if (value == 0){
            $scope.cardemandSignShow = 1;
        } else {
            $scope.cardemandSignShow = 0;
        }
    };

}]);