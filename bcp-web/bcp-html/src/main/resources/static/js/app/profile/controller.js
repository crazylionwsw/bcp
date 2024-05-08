/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('loginUserController',function ($scope,loginUserService,$modal,toaster,$q){
    $scope.title = '登录用户管理';
    $scope.tableTitle = "登录用户列表";
    $scope.user = {};
    $scope.users = [];
    $scope.sysRoles = [];

    /**
     * 初始化加载列表和分页
     */
    $scope.init = function () {
        $scope.changePage(1);

        loginUserService.getAllSysRoles(function (data) {
            $scope.sysRoles = data;
        })
    }

    $scope.changePage = function (pageIndex) {
        if (pageIndex) {
            $scope.currentPage = pageIndex;
        }
        loginUserService.getPageData($scope.currentPage, function (data) {
            $scope.users = data.content;
            $scope.totalItems = data.totalElements;//数据总条数
            $scope.pageSize = data.size;//分页单位
            $scope.currentPage = data.number + 1;//当前页
            $scope.totalPages = data.totalPages;
        });
    }

    /**
     * 删除用户信息
     */
    $scope.delete = function (model,index){
        if (model.isRoot) {
            alert("对不起，系统内置用户不删除！");
        } else {
            if(confirm("确定要删除用户？")) {
                model.dataStatus = 9;
                loginUserService.save(model, function (data) {
                    $scope.changePage($scope.currentPage);
                    toaster.pop('success', '登录用户', '删除成功！');
                })
            }
        }
    }


    /**
     * 模态框--添加/修改页面
     */
    $scope.edit = function (data) {
        /*模态框*/
        if (data) {
            $scope.user = data;
        }else{
            $scope.user = {dataStatus:1};
        }
        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "userForm.html",
            controller: function ($scope) {
                $scope.modalTitle = "登录用户";
                $scope.formTitle = "登录用户信息";

                /*关闭模态框*/
                $scope.cancel = function () {
                    modalInstance.close();
                }
                $scope.updateUsername = function () {
                    $scope.user.activitiUserId = $scope.user.username;
                    //检测Username是否重复
                }

                /*保存车型信息*/
                $scope.save = function () {
                    if (!data) {
                        $scope.user.password = $scope.password;
                    }
                    loginUserService.save($scope.user, function (data) {
                        if (!$scope.user.id) {
                            $scope.users.push(data);
                        }
                        modalInstance.close();
                        toaster.pop('success', '登录用户', '保存成功！');
                    });
                }
            }
        });
    }

    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.user;
            loginUserService.checkUniqueUser(entity,propname,propval,function(data){
                if(data.code=='1'){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };


    /**
     * 执行初始化
     */
    $scope.init();
});