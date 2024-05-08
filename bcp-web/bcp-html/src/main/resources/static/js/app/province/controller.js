/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('provinceController',function ($scope,provinceService,$modal,$q){
    $scope.button=true;

    var treeData,treeCtrl;
    $scope.title = "地区管理";

    /*  列表 */
    $scope.province = {};
    $scope.provinces = treeData =  [];
    $scope.provinceTree = treeCtrl={};

    $scope.selectedProvince = null;

    /**
     * 选定地区来源
     * @param branch
     */
    $scope.selectProvince = function (branch) {
        if(branch.children.length == 0){
            $scope.getChildData(branch.id);
        }
        if($scope.selectedProvince){
            $scope.selectedProvince.selected = false;
        }
        if(branch){
            $scope.selectedProvince = branch.data;
        } else {
            $scope.selectedProvince = null;
        }
    };


    //验证唯一
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.province;
            provinceService.checkUniqueProvince(entity,propname,propval,function(data){
                if(data.code=='1'){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /**
     * 地区编辑
     * @param province
     */
    $scope.editProvince = function (province){
        if(province == 1){
            $scope.modalTitle="编辑地区管理";
            $scope.province = $scope.selectedProvince;
        }else{
            $scope.modalTitle="添加地区管理";
            $scope.province = {};
            $scope.province.dataStatus = 1;
            if ($scope.selectedProvince) {
                $scope.province.parentId = $scope.selectedProvince.id;
            } else {
                $scope.province.parentId = "0";
            }
        }

        /*模态框*/
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'provinceModal.html',
            controller:function ($scope){

                /*  关闭模态框 */
                $scope.cancel = function (){
                    modalInstance.close();
                };
                if($scope.province.parentId == "" || $scope.province.parentId == null){
                    $scope.province.parentId = "0";
                }
                /* 保存信息 */
                $scope.save = function (){
                    provinceService.save($scope.province, function (result) {
                        var treenode ={label:result.name,id:result.id,uid:result.id,data:result};
                        var selectBranch =  treeCtrl.get_selected_branch();
                        if($scope.province.id == "" || $scope.province.id == null){
                            treeCtrl.add_branch(selectBranch ,treenode);
                        } else {
                            selectBranch.label = result.name;
                        }

                        modalInstance.close();
                    });
                }
            }
        });
    };

    $scope.init = function (){
        treeData.push({id: '0', label: "全国"});
    };

    /**
     * 显示下级对象
     */
    $scope.getChildData = function(parentid){
        provinceService.getProvinces(parentid,function(data){
            var selectBranch =  treeCtrl.get_selected_branch();
            data.forEach(function(item){
                var treenode ={label:item.name,id:item.id,uid:item.id,data:item};
                treeCtrl.add_branch(selectBranch,treenode);
            });
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();

    /**
     * 删除地区
     */
    $scope.removeProvince = function () {
        var province = $scope.selectedProvince;
        if (confirm("确定要删除数据" + province.name + "?")) {
            //province.dataStatus = 9;
            provinceService.deleteProvince(province, function (data) {
                treeData.forEach(function (item) {
                    $scope.deleteNode(item,province);
                });
            });
        }
        treeCtrl.select_next_branch();
    };

    //树
    $scope.deleteNode = function(treenode, delnode){
        for(var i=0; i<treenode.children.length; i++){
            if(treenode.children[i].id== delnode.id){
                treenode.children.splice(i,1);
                return;
            }else{
                $scope.deleteNode(treenode.children[i],delnode);
            }
        }
    };

    /**
     * 唯一性验证
     * @param propname
     * @param propval
     * @returns {*}
     */
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.province;
            provinceService.checkUniqueProvince(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
});