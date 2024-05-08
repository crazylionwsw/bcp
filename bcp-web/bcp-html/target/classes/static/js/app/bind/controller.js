/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('bindController',['$scope','$modal','terminalService',function ($scope,$modal,terminalService){
    /*列表*/
    $scope.terminalbinds  = [];//返回的列表数据
    $scope.terminal={};
    $scope.tableTitle = '设备绑定管理';
    $scope.tableDesc = "设备绑定列表";
    $scope.currentPage = 1;
    $scope.user = {};
    $scope.users = [];

    $scope.userFilter = {};
    /**
     * 初始化加载列表和分页
     */
    $scope.init = function(){
        $scope.changePage();
    };

    $scope.changePage = function (){
        terminalService.getPageData($scope.currentPage-1,function(data){
            $scope.terminalbinds = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1 ;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();

    /**
     * 锁定设备
     */
    $scope.lock = function (terminal,index){
        terminal.dataStatus = 8;
        terminalService.save(terminal,function (data){
            $scope.changePage($scope.currentPage);
        })
    };

    /**
     * 解绑
     */
    $scope.discard = function (terminal,index){
        terminal.dataStatus = 9;
        terminalService.save(terminal,function (data){
            $scope.changePage($scope.currentPage);
        })
    };

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
        $scope.userFilter = {};
        $scope.changePage(1);
    };

    /**
     * 查询绑定记录
     * @param user
     */
    $scope.queryBill = function (user) {
        $scope.userFilter = user;
        terminalService.search(user,$scope.currentPage-1,function (data){
            $scope.terminalbinds = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });
    };
}]);


