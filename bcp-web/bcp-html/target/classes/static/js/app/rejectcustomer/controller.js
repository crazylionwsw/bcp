/**
 * 客户管理
 */

'use strict';

app.controller('rejectcustomerController',['$scope','rejectcustomerService','$stateParams','$localStorage','$state','$modal',function($scope,rejectcustomerService,$stateParams,$localStorage,$state,$modal){
    $scope.tableTitle = '拒绝客户';

    /*列表*/
    $scope.rejectcustomers  = [];//返回的列表数据
    $scope.rejectcustomer = {};

    $scope.searchFilter = {pageSize:20};
    $scope.currentPage = 1;
    
    $scope.init=function(){
        if ($localStorage.RejectCustomerSearchFilter){
            $scope.searchFilter = $localStorage.RejectCustomerSearchFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }
        $scope.changePage(1);
    };

    /*分页功能*/
    $scope.changePage = function (pageIndex){
        if (pageIndex){
            $scope.currentPage = pageIndex;
        }
        rejectcustomerService.search($scope.searchFilter,$scope.currentPage-1,function(data){
            $scope.rejectcustomers = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1 ;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });
        $localStorage.RejectCustomerSearchFilter = $scope.searchFilter;
    };

    /**
     * 执行初始化
     */
    $scope.init();

    //查询分页单位
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
        $scope.searchFilter={};
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

}]);
