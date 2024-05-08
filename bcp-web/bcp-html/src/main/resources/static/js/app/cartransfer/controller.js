/**
 * Created by LB on 2016-10-24.
 */

'use strict';
app.controller('cartransferController',['$scope','cartransferService','customerService','orderService','carregistryService','customerTransactionService','$modal','toaster','$localStorage','$stateParams','$loading','$rootScope', function ($scope,cartransferService,customerService,orderService,carregistryService,customerTransactionService,$modal,toaster,$localStorage,$stateParams,$loading,$rootScope){
    $scope.title = "转移过户";

    //返回的列表数据
    $scope.cartransfers = [];
    $scope.cartransfer = null;

    //查询参数
    $scope.search = {timeName:'transferDate',compensatory:false,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:false,oc:true,pageSize:20,billTypeCode:'A023'};
    $scope.searchFilter = {timeName:'transferDate',compensatory:false,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:false,oc:true,pageSize:20,billTypeCode:'A023'};
    $scope.currentPage = 1;

    $scope.checked = false;

    //贷款金额合计
    $scope.totalCreditAmount = 0.0;

    /**
     * 初始化加载列表
     */
    $scope.init = function (){
        if ($localStorage.CarTransferCustomerFilter){
            $scope.searchFilter = $localStorage.CarTransferCustomerFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        $scope.changePage(1);
        if ($stateParams.tid) { //从交易列表打开
            cartransferService.getOneByCustomerTransactionId($stateParams.tid, function(data){
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
    $scope.changePage = function (pageIndex) {
        $scope.totalCreditAmount = 0.0;

        if (pageIndex) {
            $scope.currentPage = pageIndex;
        }
        cartransferService.search($scope.searchFilter, $scope.currentPage-1, function (data) {
            $scope.cartransfers = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.cartransfers, function(bills){
                $scope.cartransfers = bills;
                $rootScope.$broadcast("CalTotal", bills);
            })
        });

        $localStorage.CarTransferCustomerFilter = $scope.searchFilter;
    };

    //发送广播，计算刷卡金额
    $scope.$on('CalTotal',function (e,bills) {
        if (bills){
            angular.forEach(bills,function (item, index) {
                customerService.getCustomerLoanByCustomerTransactionId(item.customerTransactionId, function (data) {
                    if (data) {
                        $scope.addCreditAmountTotal(data.creditAmount);
                    }
                })
            })
        }
    });

    //贷款金额合计
    $scope.addCreditAmountTotal = function (amount) {
        if(amount)
            $scope.totalCreditAmount = $scope.totalCreditAmount+ amount;
    };

    $scope.edit = function(cartransfer, index){
        if (!$scope.checked) {
            if ($scope.cartransfer){
                $scope.cartransfer = {};
            }
            cartransferService.getOne(cartransfer.id,function (data) {
                $scope.cartransfer = data;
                $scope.checked = true;

                if (data && data.customerTransactionId){
                    var customerTransactionId = data.customerTransactionId;
                    customerTransactionService.getOne(customerTransactionId,function (transaction) {
                        $scope.cartransfer.transaction = transaction;
                    });
                    orderService.getOneByCustomerTransactionId(customerTransactionId,function (data) {
                        $scope.cartransfer.order = data;
                    });
                }
            });
        }
    };

    /*        关闭  右边框      */
    $scope.close = function () {

        //更新列表数据
        cartransferService.getOne($scope.cartransfer.id,function (data) {
            angular.forEach($scope.cartransfers, function(item, index){
                if (item.id == data.id) {
                    if ($scope.searchFilter.statusValue == -1){
                        $scope.cartransfers[index] = data;
                    } else if($scope.searchFilter.statusValue != data.approveStatus){
                        $scope.cartransfers.splice(index,1);
                    }else {
                        $scope.cartransfers[index] = data;
                    }

                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        if ($scope.cartransfers[index])
                            $scope.cartransfers[index].transaction = transaction;
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
     *   审批状态查詢
     */
    $scope.search = function() {
        $scope.changePage(1);
    };

    $scope.changePageSize = function () {
        $scope.searchFilter.pageSize = $scope.pageSize;
        $scope.changePage(1);
    };

    /*                     模糊查询                           START                      */
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
        $scope.searchFilter = {timeName:'transferDate',creditMonths:0,compensatory:false,business:true,statusName:'approveStatus',statusValue:$scope.searchFilter.statusValue,nc:false,oc:true,pageSize:$scope.pageSize,billTypeCode:'A023'};
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
    /*                     模糊查询                          END                      */

    /**
     *  审批
     */
    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        cartransferService.sign(id, signinfo, function (data) {
            $loading.finish('signInfo');
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        });
    });

    //更新审批状态
    $scope.$on('UpdateSignInfo',function (e, id) {
        cartransferService.getOne(id,function (data) {
            $scope.cartransfer.approveStatus = data.approveStatus;
        })
    });
}]);