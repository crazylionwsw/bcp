/**
 * Created by lily on 2017/4/18.
 */
'use strict';
app.controller('resetorderController',['$scope','resetorderService','orderService','$state','$stateParams','$modal','$loading','$localStorage','$rootScope',function ($scope,resetorderService,orderService,$state,$stateParams,$modal,$loading,$localStorage,$rootScope) {
    
    $scope.title = "重新签约";
    
    //返回的列表数据
    $scope.resetorders = [];
    $scope.resetorder = null;
    $scope.order = null;
    
    //返回工作流查询集合
    $scope.taskList = [];
    $scope.istask = {};

    //查询参数
    $scope.customer = {};
    $scope.customerFilter = {};

    $scope.currentPage = 1;
    $scope.checked = false;
    $scope.signModal = {};
    
    $scope.init = function(){

        if ($localStorage.ResetOrderCustomerFilter){
            $scope.customerFilter = $localStorage.ResetOrderCustomerFilter;
        }
        if ($localStorage.ResetOrderApproveStatus){
            $scope.approveStatus = $localStorage.ResetOrderApproveStatus;
        } else {
            $scope.approveStatus = 1;
        }
        $scope.changePage(1);

        if ($stateParams.tid){
            resetorderService.getOneByCustomerTransactionId($stateParams.tid, function(data){
                if (data) $scope.edit(data);
            })
        }
    };

    /*分页功能*/
    $scope.changePage = function (pageIndex) {
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        if( $scope.customerFilter.name || $scope.customerFilter.identifyNo || $scope.customerFilter.cells){
            resetorderService.search($scope.customerFilter,$scope.approveStatus,$scope.currentPage-1,function (data){
                $scope.resetorders = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages=data.totalPages;//总页数
            })
        } else{
            resetorderService.getPageData($scope.approveStatus,$scope.currentPage-1, function (data){
                $scope.resetorders = data.result;//返回的数据
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages=data.totalPages;//总页数
            });
        }
        $localStorage.ResetOrderCustomerFilter = $scope.customerFilter;
        $localStorage.ResetOrderApproveStatus = $scope.approveStatus;
    };

    $scope.edit = function (resetorder,index) {
        if (!$scope.checked){
            if ($scope.resetorder){
                $scope.resetorder = {};
            }
            if ($scope.order){
                $scope.order = {};
            }
            resetorderService.getOne(resetorder.id,function(data){
                $scope.resetorder = data;
                $scope.checked = true;
                //根据客户换车单id查询客户签约单
                if(data && data.purchaseOrderId){
                    orderService.getOne(data.purchaseOrderId,function(order){
                        $scope.order = order;
                    })
                }
            });
        }
    };

    //关闭右边框
    $scope.close = function () {
        resetorderService.getOne($scope.resetorder.id,function (data) {
            angular.forEach($scope.resetorders,function (item, index) {
                if (item.id == data.id){
                    if($scope.approveStatus == -1){
                        $scope.resetorders[index] = data;
                    } else if ($scope.approveStatus != data.approveStatus){
                        $scope.resetorders.splice(index,1);
                    }else {
                        $scope.resetorders[index] = data;
                    }
                }
            })
        });
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    $scope.init();
    
    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
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
        $scope.customerFilter={};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param customer
     */
    $scope.queryBill = function (customer) {
        if (customer.cell) {
            customer.cells = [customer.cell];
        }
        $scope.customerFilter = customer;
        $scope.changePage(1);
    };

    /**
     * 根据审核状态查询
     */
    $scope.search = function(){
        $scope.customerFilter = {};
        $scope.changePage(1);
    };

    /**
     * 审核/审批
     */
    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        resetorderService.sign(id, signinfo, function (data) {
            $loading.finish('signInfo');
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        });
    });

    $scope.$on('UpdateSignInfo', function (e, id) {
        resetorderService.getOne(id, function (data) {
            $scope.resetorder.approveStatus = data.approveStatus;
        })
    });
}]);