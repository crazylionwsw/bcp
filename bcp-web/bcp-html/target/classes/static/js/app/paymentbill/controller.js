/**
 * Created by admin on 2018/3/8.
 */
'use strict';
app.controller('paymentbillController', ['$scope', 'paymentbillService','orderService', 'customerService', 'declarationService', 'sysparamService', 'cashsourceService', 'workflowService','customerTransactionService', '$state', '$modal', 'toaster', '$localStorage', '$stateParams','$loading','$rootScope','$q',
    function ($scope, paymentbillService,orderService, customerService, declarationService, sysparamService, cashsourceService, workflowService,customerTransactionService, $state, $modal, toaster, $localStorage,$stateParams,$loading,$rootScope,$q) {

        $scope.title = '缴费管理';
        /*列表*/
        $scope.paymentbills = [];//返回的列表数据
        $scope.paymentbill = null;
        $scope.cardemand = null;
        $scope.cardemandSignShow = 0;
        $scope.order = null;

        //查询参数
        $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A025'};
        $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A025'};

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

            if (pageIndex) {
                $scope.currentPage = pageIndex;
            }
            paymentbillService.search($scope.searchFilter,$scope.currentPage-1, function (data) {
                $scope.paymentbills = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages = data.totalPages;//总页数

                customerTransactionService.mergeTransactions($scope.paymentbills, function(bills){
                    $scope.paymentbills = bills;
                })
            });

            $localStorage.BusinessexchangeSearchFilter = $scope.searchFilter;
        };

        //     查看  缴费单详细
        $scope.edit = function (paymentbill) {

            //web端提示“该客户的购车缴费已经提交过几次，共计缴费多少元！”
            paymentbillService.getPaymentsByCustomerTransactionId(paymentbill.customerTransactionId,function (payments) {
                var totalPay = 0;
                var totalLength = 0;
                paymentbillService.getOne(paymentbill.id,function (payment) {
                    var pay = payment.paymentAmount == null ? 0 : payment.paymentAmount;
                    angular.forEach(payments,function (item) {
                        totalPay += item.paymentAmount == null ? 0 : item.paymentAmount;
                    });
                    totalLength = payments.length -1;
                    totalPay = totalPay - pay;
                    toaster.pop('info', '该客户的购车缴费已经提交过'+ totalLength +'次，共计缴费'+ totalPay+ '元！');
                });
            });

            if (!$scope.checked) {
                if ($scope.paymentbill) {
                    $scope.paymentbill = {};
                }
                paymentbillService.getOne(paymentbill.id, function (data) {
                    $scope.paymentbill = data;
                    $scope.checked = !$scope.checked;

                    workflowService.getTaskCurrentGroup(data.billTypeCode + '.' + data.id, function (data) {
                        $scope.presentTask = data;
                    });

                    if (data.customerTransactionId){
                        customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                            $scope.paymentbill.transaction = transaction;
                        });
                    }
                    if (data.customerId){
                        customerService.getOne(data.customerId, function(customer){
                            $scope.paymentbill.customer = customer;
                        })
                    }
                })
            }
        };

        $scope.createFileNumber = function (transactionId) {
            customerTransactionService.createFileNumber(transactionId,function (data) {
                $scope.paymentbill.transaction.fileNumber = data.fileNumber;
            })
        };


        /*        关闭  右边框      */
        $scope.close = function () {
            paymentbillService.getOne($scope.paymentbill.id,function (data) {
                angular.forEach($scope.paymentbills, function(item, index){
                    if (item.id == data.id) {
                        if($scope.searchFilter.statusValue == -1){
                            $scope.paymentbills[index] = data;
                        } else if ($scope.searchFilter.statusValue != data.approveStatus){
                            $scope.paymentbills.splice(index,1);
                        }else {
                            $scope.paymentbills[index] = data;
                        }
                        customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                            $scope.paymentbills[index].transaction = transaction;
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
            $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,creditMonths:0,statusName:'approveStatus',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A025'};
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

        $scope.$on('BillSign',function (e,id,temSignInfo,presentTaskGroup) {
            $loading.start('signInfo');
            paymentbillService.sign(id, temSignInfo, function (data) {
                if(data.m){
                    alert(data.m);
                }
                $loading.finish('signInfo');
                $rootScope.$broadcast("ShowTask");
                $rootScope.$broadcast("UpdateSignInfo", id);
            });
        });

        //业务调整单审批的广播
        $scope.$on('UpdateSignInfo', function (e, id) {
            paymentbillService.getOne(id, function (data) {
                $scope.paymentbill.approveStatus = data.approveStatus;
                //更新当前任务
                workflowService.getTaskCurrentGroup(data.billTypeCode + '.' + data.id, function (data) {
                    $scope.presentTask = data;
                });
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
