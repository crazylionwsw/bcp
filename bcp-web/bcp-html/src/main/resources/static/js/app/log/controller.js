/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('logController',['$scope','$modal','logService',function ($scope,$modal,logService){
    /*列表*/
    $scope.loginlogs  = [];
    $scope.loginlog={};
    $scope.tableTitle = "系统日志管理";
    $scope.tableDesc = "系统日志列表";
    $scope.currentPage = 1;
    /**
     * 初始化加载列表和分页
     */
    $scope.init = function(){
        $scope.changePage(1);
    };

    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }

        logService.getPageData($scope.currentPage-1,function(data){
            $scope.loginlogs = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1 ;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });
    };
    /**
     * 模态框--添加/修改页面
     */
    $scope.edit = function (loginlog){

        /*模态框*/
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:"modelForm.html",
            controller : function ($scope){
                if(loginlog.id != "" && loginlog.id != null) {
                    $scope.modalTitle = "编辑登录日志";
                }else {
                    $scope.modalTitle = "添加登录日志";
                }
                /*关闭模态框*/
                $scope.cancle = function (){
                    modalInstance.close();
                };

                if(loginlog.id && loginlog.id != null){
                    /*根据id获取设备信息*/
                    $scope.loginlog = loginlog;
                }
            }
        });
    };

    /**
     * 删除日志
     */
    $scope.delete = function(loginlog, index) {
        if (confirm("确定要删除数据?")) {
            logService.delete(loginlog, function (data) {
                var index = $scope.loginlogs.indexOf(loginlog);
                $scope.loginlogs.splice(index, 1);
            })
        }
    };

    /**
     * 执行初始化
     */
    $scope.init();
}]);

