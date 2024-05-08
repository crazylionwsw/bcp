/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('loginUserController',function ($scope, loginUserService, workflowService, sysroleService, $modal, toaster, $q){
    $scope.tableTitle = '登录用户管理';
    $scope.tableDesc = "登录用户列表";
    $scope.user = {};
    $scope.users = [];

    $scope.sysRoles = [];
    
    $scope.activitiSysRoles = [];
    $scope.currentPage = 1;

    $scope.loginUserQuery = {};
    $scope.loginuserFilter = {};
    //禁用
    $scope.saved = false;

    /**
     * 初始化加载列表和分页
     */
    $scope.init = function () {
        $scope.changePage();

        sysroleService.lookup(function (data) {
            $scope.sysRoles = data;
        });
        
        workflowService.getAllGroups(function (data) {
            $scope.activitiSysRoles = data;
        });
    }

    $scope.changePage = function () {
        loginUserService.getPageData($scope.currentPage-1, function (data) {
            $scope.users = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });
    }

    /**
     * 删除用户信息
     */
    $scope.delete = function (user,index){
        if (user.isRoot) {
            alert("对不起，系统内置用户不删除！");
        } else {
            var msg = "确定要作废数据  " + user.username + "  ?";
            var msg2 = "作废成功";
            if (user.dataStatus == 9) {
                alert("作废的用户不可以再次删除!!!");
            }else{
                if (confirm(msg)) {
                    loginUserService.delete(user, function (data) {
                        toaster.pop('success','登录用户', msg2);
                        if (user.dataStatus == 9) {
                            $scope.users.splice(index, 1);
                        } else {
                            $scope.users[index] = data;
                        }
                    })
                }
            }
        }
    }


    /**
     * 模态框--添加/修改页面
     */
    $scope.edit = function (user) {
        /*  判断是编辑还是添加 */
        if(user.id != "" && user.id != null){
            $scope.modalTitle = "编辑登录用户";
            $scope.user = angular.copy(user);
        } else {
            $scope.modalTitle = "添加登录用户";
            $scope.user={dataStatus:1};
        }
        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "userForm.html",
            backdrop:'static',
            controller: function ($scope) {
                /*关闭模态框*/
                $scope.cancel = function () {
                    modalInstance.close();
                }
                $scope.updateUsername = function () {
                    $scope.user.activitiUserId = $scope.user.username;
                    //检测Username是否重复
                }

                /*保存用户信息*/
                $scope.save = function () {
                    $scope.saved = true;
                    if ($scope.password && $scope.password.trim().length > 0) {
                        $scope.user.password = $scope.password;
                    } else {
                        $scope.user.password = '';
                    }

                    loginUserService.save($scope.user, function (data) {
                        if (!$scope.user.id) {
                            $scope.users.push(data);
                        } else {
                            var index = $scope.users.indexOf(user);
                            $scope.users[index] = data;
                        }
                        $scope.user = data;
                        modalInstance.close();
                        toaster.pop('success', '登录用户', '保存成功！');
                    });
                }
            }
        });
    }

    /*查询*/
    /*$scope.openQueryDialog = function (){
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'tpl/view/loginuser/query.html',
            controller:function ($scope,$modalInstance){
                $scope.modalTitle = "查询";
                // $scope.checkQuery = function(value) {
                //     if ($scope.loginUserQuery.queryString != null && $scope.loginUserQuery.queryString.trim() != "") {
                //         return true;
                //     } else {
                //         return false;
                //     }
                // };

                // $scope.updateQueryString = function () {
                //     $scope.loginUserQuery.queryString = '';
                //     if ($scope.loginUserQuery.username) $scope.loginUserQuery.queryString += $scope.loginUserQuery.username;
                //     if ($scope.loginUserQuery.userRoleIds) $scope.loginUserQuery.queryString += $scope.loginUserQuery.userRoleIds;
                // }

                /!*  关闭模态框 *!/
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }
                /!*清除查询内容*!/
                $scope.clearQuery = function() {
                    $scope.loginUserQuery ={username:null,userRoleIds:null};
                    //$scope.loginUserQuery = {};
                    $scope.changePage();
                };

                // var user = $scope.user;

                /!* 查询客户基本信息 *!/
                $scope.queryloginUser = function (){
                    var loginUser = $scope.loginUserQuery;
                    var currentPage = $scope.currentPage;
                    loginUserService.search(loginUser,currentPage-1, function (data) {
                        $scope.$parent.users = data.result;
                        $scope.$parent.totalItems = data.totalCount;//数据总条数
                        $scope.$parent.pageSize = data.pageSize;//分页单位
                        $scope.$parent.currentPage = data.currentPage + 1;//当前页
                        $scope.$parent.totalPages = data.totalPages;
                    });
                    /!* 操作完成自动关闭模态框 *!/
                    modalInstance.close();
                }
            }
        });
    };*/


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
        $scope.loginuserFilter = {};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param user
     */
    $scope.queryBill = function (loginuser) {
        $scope.loginuserFilter = loginuser;
        $scope.currentPage = 1;
        loginUserService.search(loginuser,$scope.currentPage-1,function (data){
            $scope.users = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };


    //唯一验证
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.user;
            loginUserService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };


    //恢复数据
    $scope.renew = function (user,index) {
        if(confirm("确定要恢复该条数据?")){
            user.dataStatus = 1;
            loginUserService.renewLoginUser(user,function (data) {
                if(user.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    /**
     * 执行初始化
     */
    $scope.init();
});