/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('logManagementController',['$scope','$modal','logManagementService','toaster','$q',function ($scope,$modal,logManagementService,toaster,$q){
    $scope.tableTitle = '登陆管理';
    $scope.tableDesc = "登陆管理列表";

    /*列表*/
    $scope.logmanagements  = [];//返回的列表数据
    $scope.logmanagement = {};

    /**
     * 初始化加载列表和分页
     */
    $scope.init = function(){
        $scope.changeList();

    };

    $scope.changeList = function (){
        logManagementService.getAllList(function(data){
            $scope.logmanagements = data;
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();
    
    $scope.offline = function (logmanagement,index){
            var msg = "确定要让此账号下线吗?";
            if (confirm(msg)) {
                logManagementService.offline(logmanagement, function (data) {
                        $scope.logmanagements.splice(index, 1);
                })
            }
    };
    
}]);


