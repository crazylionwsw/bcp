/**
 * Created by LB on 2016-10-24.
 * Update by gqr on 2018-08-18.
 */

'use strict';
app.controller('enhancementController',['$scope','enhancementService','customerTransactionService','$state','$stateParams','$modal','$localStorage','$loading','$rootScope', function ($scope,enhancementService,customerTransactionService,$state,$stateParams,$modal,$localStorage,$loading,$rootScope){

    $scope.title = "资料补全";

    $scope.enhancementTitle = "上传资料";

    //返回的列表数据
    $scope.enhancements = [];
    $scope.enhancement = null;
    
    //查询参数
    $scope.search = {timeName:'dueTime',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A013'};
    $scope.searchFilter = {timeName:'dueTime',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A013'};

    $scope.currentPage = 1;
    
    $scope.checked = false;
    /**
     * 初始化加载列表
     */
    $scope.init = function (){
        if ($localStorage.EnhancementSearchFilter ) {
            $scope.searchFilter = $localStorage.EnhancementSearchFilter;
            if ($scope.searchFilter.pageSize)  $scope.pageSize =  $scope.searchFilter.pageSize;
        }
        $scope.changePage(1);
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){
        if (pageIndex){
            $scope.currentPage = pageIndex;
        }
        enhancementService.search($scope.searchFilter, $scope.currentPage-1, function (data){
            $scope.enhancements = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数
            
            customerTransactionService.mergeTransactions($scope.enhancements, function(bills){
                $scope.enhancements = bills;
            })
        });
        
        $localStorage.EnhancementSearchFilter = $scope.searchFilter;
    };

    //修改新车分期订单
    $scope.edit = function(enhancement,index){
        if(!$scope.checked){
            if ($scope.enhancement){
                $scope.enhancement = {};
            }
            enhancementService.getOne(enhancement.id,function (data) {
                $scope.enhancement = data;
                if(data.customerTransactionId){
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.enhancement.transaction = transaction;
                    });
                }
                $scope.checked = true;
            })
        }
    };

    $scope.init();

    $scope.close = function () {
        enhancementService.getOne($scope.enhancement.id,function (data) {
            angular.forEach($scope.enhancements, function(item, index){
                if (item.id == data.id) {
                    if($scope.searchFilter.statusValue == -1){
                        $scope.enhancements[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.approveStatus){
                        $scope.enhancements.splice(index,1);
                    }else {
                        $scope.enhancements[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.enhancements[index].transaction = transaction;
                    });
                }
            });
        });
      $scope.checked = false;
    };
    $scope.onOpenSidePanel=function () {
    };
    $scope.onCloseSidePanel=function () {
    };

    /**
     * 查询
     */
    $scope.search = function() {
        $scope.changePage(1);
    };

    $scope.changePageSize = function() {
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
        $scope.searchFilter = {timeName:'dueTime',compensatory:true,business:true,statusValue:$scope.searchFilter.statusValue,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A013'};
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

    //  审核/审批
    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        enhancementService.sign(id,signinfo,function(data){
            $loading.finish('signInfo');
            $rootScope.$broadcast('ShowTask');
            $rootScope.$broadcast('UpdateSignInfo',id);
        })
    });

    $scope.$on('UpdateSignInfo', function (e, id) {
        enhancementService.getOne(id, function (data) {
            $scope.enhancement.approveStatus = data.approveStatus;
        })
    });
    
}]);