/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('sysroleController',function ($scope, sysroleService, sysresourceService, $modal, toaster, $q){

    $scope.tableTitle = "系统角色管理";
    /*$scope.tableDesc ="角色列表";*/
    $scope.sysroles = [];//返回的数据
    $scope.sysrole = {};//当前的系统角色

    //访问权限
    $scope.sysResources = [];
    $scope.currentPage = 1;

    /**
     * 列表
     */
    $scope.init = function (){
        $scope.changePage();
    }

    /**
     * 分页功能
     */
    $scope.changePage = function (){
        sysroleService.getDataByPage($scope.currentPage-1,function (data){
            $scope.sysroles = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });

        sysresourceService.lookup(function (data) {
            $scope.sysResources = data;
        });
    }

    $scope.init();

    /**
     * 模态框--添加/修改页面
     */
    $scope.edit = function (sysrole) {

        /*  判断是编辑还是添加 */
        if(sysrole.id != "" && sysrole.id != null){
            $scope.modalTitle = "编辑系统角色";
            //$scope.sysrole = sysrole;
            $scope.sysrole = angular.copy(sysrole);
        } else {
            $scope.modalTitle = "添加系统角色";
            $scope.sysrole={dataStatus:1};
        }

        /*模态框*/
        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "sysroleinsert.html",
            backdrop:'static',
            controller: function ($scope,$modalInstance) {

                /*关闭模态框*/
                $scope.cancel = function ($modalInstance) {
                    modalInstance.close();
                };

                $scope.selectSysResource = function (sysResource) {
                    sysresourceService.getChildren(sysResource.id,function (data) {
                        if(data){
                            angular.forEach(data,function (item,index) {
                                if($scope.sysrole.sysResourceIds.indexOf(item.id) < 0 ){
                                    $scope.sysrole.sysResourceIds.push(item.id);
                                }
                            })
                        }
                    });
                    if (sysResource.parentId == 0 && $scope.sysrole.sysResourceIds.indexOf(sysResource.id) >= 0){
                        $scope.sysrole.sysResourceIds.splice($scope.sysrole.sysResourceIds.indexOf(sysResource.id));
                    }
                };

                /*保存 修改 角色信息*/
                $scope.save = function () {
                    //var sysrole = $scope.sysrole;
                    sysroleService.save($scope.sysrole, function (data) {

                        if(!$scope.sysrole.id){
                            $scope.sysroles.push(data)
                        }else{
                            var index = $scope.sysroles.indexOf(sysrole);
                            $scope.sysroles[index] = data;
                        }
                        $scope.sysrole = data;
                        modalInstance.close();
                        toaster.pop('success', '系统角色', '保存成功！');
                    });
                }
            }
        });
    };
    

    //唯一验证
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.sysrole;
            sysroleService.checkUniqueSysRole(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /**
     * 删除角色信息
     */
    $scope.delete = function (sysrole,index){
        if (sysrole.dataStatus != 9 ){
            if (confirm("确定要作废数据  " + sysrole.name + "  ?")) {
                sysroleService.delete(sysrole, function (data) {
                    toaster.pop('success','系统角色','作废成功');
                    var sysrole = data;
                    $scope.init();
                    $scope.sysrole = data;
                })
            }
        }else if(sysrole.dataStatus == 9){
            if (confirm("确定要删除数据  " + sysrole.name + "  ?")) {
                sysroleService.delete(sysrole, function (data) {
                    toaster.pop('success','系统角色','删除成功');
                    var sysrole = data;
                    //  如果 返回的数据状态为 作废
                    if (sysrole.dataStatus == 9) {
                        $scope.sysroles.splice(index, 1);
                    } else {
                        $scope.sysroles[index] = data;
                    }
                })
            }
        }
    };

    //恢复数据
    $scope.renew = function (sysrole,index) {
        if(confirm("确定要恢复该条数据?")){
            sysrole.dataStatus = 1;
            sysroleService.save(sysrole,function (data) {
                if(sysrole.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

});