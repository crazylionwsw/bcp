    /**
 * Created by zxp on 2017/3/13.
 */

'use strict';

app.controller('cancelorderController',function ($scope,cancelorderService,orderService,customerTransactionService,customerService, $modal,$stateParams,$state,$loading,$localStorage,$rootScope){

    $scope.title = "取消业务";

    /*  列表 */
    $scope.cancelorders  = [];//返回列表数据
    $scope.cancelorder = {};

    // //查询参数
    $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A012'};
    $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A012'};
    
    $scope.currentPage=1;
    $scope.checked = false;

    //贷款金额合计
    $scope.totalCreditAmount = 0.0;
 
    $scope.init = function (){
        if ($localStorage.CancelOrderSearchFilter){
            $scope.searchFilter = $localStorage.CancelOrderSearchFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        $scope.changePage(1);
        if ($stateParams.tid){
            cancelorderService.getOneByCustomerTransactionId($stateParams.tid, function(data){
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

        if (pageIndex){
            $scope.currentPage = pageIndex;
        }
        cancelorderService.search($scope.searchFilter, $scope.currentPage-1, function (data) {
            $scope.cancelorders = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages; //总页数

            customerTransactionService.mergeTransactions($scope.cancelorders, function(bills){
                $scope.cancelorders = bills;
                $rootScope.$broadcast("CalTotal", bills);
            })
        });

        $localStorage.CancelOrderSearchFilter = $scope.searchFilter;
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
    
    //右边框回显
    $scope.edit = function(cancelorder,index){
        if(!$scope.checked){
            if ($scope.cancelorder){
                $scope.cancelorder = {};
            }
            if ($scope.order){
                $scope.order = {};
            }
            cancelorderService.getOne(cancelorder.id,function (data) {
                $scope.cancelorder=data;
                $scope.checked=true;
                if (data && data.customerTransactionId){
                    var transactionId = data.customerTransactionId;
                    customerTransactionService.getOne(transactionId,function (transaction) {
                        $scope.cancelorder.transaction = transaction;
                    });
                    //      获取  获取客户签约信息
                    orderService.getOneByCustomerTransactionId(transactionId,function (order) {
                        $scope.order = order;
                    })
                }
            })
        }
    };

    //关闭右边框
    $scope.close = function () {
        cancelorderService.getOne($scope.cancelorder.id,function (data) {
            angular.forEach($scope.cancelorders,function (item, index) {
                if (item.id == data.id){
                    if($scope.searchFilter.statusValue == -1){
                        $scope.cancelorders[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.approveStatus){
                        $scope.cancelorders.splice(index,1);
                    }else {
                        $scope.cancelorders[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.cancelorders[index].transaction = transaction;
                    });
                }
            })
        });
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };
    
    /**
     * 审核/审批
     * @param cardemand
     */
    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        cancelorderService.sign(id, signinfo, function (data) {
            $loading.finish('signInfo');
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        });
    });

    $scope.$on('UpdateSignInfo', function (e, id) {
        cancelorderService.getOne(id, function (data) {
            $scope.cancelorder.approveStatus = data.approveStatus;
        })
    });

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
        $scope.searchFilter = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'approveStatus',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A012'};
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
});