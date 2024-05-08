/**
 * Created by LB on 2016-10-24.
 * 系统资源控制器
 */

'use strict';

app.controller('sysresourceController',function ($scope,sysresourceService,$modal,toaster,$q){
    var treeData,treeCtrl;
    $scope.title = "系统功能";
    $scope.formTitle = "系统功能定义";
    $scope.sysresourceTree = treeCtrl= {};

    //禁用
    $scope.onlyread = false;
    $scope.saved = false;

    /**
     * 系统全部的资源列表
     * @type {Array}
     */
    $scope.sysresources = treeData =  [];
    $scope.sysresource = {};
    $scope.selectedSysResource = null;

    /**
     * 当前选定的资源
     * @param branch
     */
    $scope.selectSysResource = function (branch) {
        branch.selected = true;
        if(branch.children.length==0){
            $scope.getChildData(branch.id);
        }
        $scope.selectLabel = branch.label;
        if($scope.selectedSysResource){ //取消之前选中的
            $scope.selectedSysResource.selected = false;
        }
        if(branch.data){
            $scope.selectedSysResource = branch.data;
            $scope.edit(branch);
        } else {
            $scope.selectedSysResource = null;
            $scope.edit('');
        }

        /*选中作废数据不可再编辑*/
        if(branch.data.dataStatus == 9){
            $scope.onlyread = true;
        }else{
            $scope.onlyread = false;
        }

    }

    /**
     * 初始化
     */
    $scope.init = function () {
        //从后台获取数据
        treeData.push({id: '0', label: "全部资源"});
    }


    $scope.getChildData = function (parentData) {
        sysresourceService.getChildren(parentData, function (data) {
            var selectBranch = treeCtrl.get_selected_branch();
            data.forEach(function (item) {
                var treenode = {label: item.name, id: item.id, uid: item.id, data: item};
                treeCtrl.add_branch(selectBranch, treenode);
            });
        });
    }

    /**
     * 系统功能树编辑
     * @param sysresource
     */
    $scope.edit = function (sysresource) {
        $scope.saved = false;
        if(sysresource.id == "" || sysresource.id == null){
            //添加
            $scope.sysresource={dataStatus: 1};
            if ($scope.selectedSysResource)
                $scope.sysresource.parentId = $scope.selectedSysResource.id;
            $scope.userForm.$setPristine();
        }else{
            //编辑
            $scope.sysresource = $scope.selectedSysResource;
        }
    }

    /*保存系统树信息*/
    $scope.save = function () {

        /*点击完保存禁用按钮*/
        $scope.saved = true;
        if($scope.sysresource.parentId == "" || $scope.sysresource.parentId == null){
            $scope.sysresource.parentId = "0";
        }

        sysresourceService.save($scope.sysresource, function (result) {

            var treenode ={label:result.name,id:result.id,uid:result.id,data:result};
            var selectBranch =  treeCtrl.get_selected_branch();
            if($scope.sysresource.id == "" || $scope.sysresource.id == null){
                treeCtrl.add_branch(selectBranch,treenode);
            }else{
                selectBranch.label = result.name;
            }
            // $scope.saved = true;
            toaster.pop('success', '系统资源', '保存成功！');
        });
    }

    /**唯一验证
     */
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.sysresource;
            sysresourceService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /**
     * 删除资源信息
     */
    $scope.remove = function (sysresource, index){

        if (sysresource.dataStatus != 9 ){
            if (confirm("确定要作废数据  " + sysresource.name + "  ?")) {
                sysresourceService.delete(sysresource, function (data) {
                    toaster.pop('success','系统资源','作废成功');
                    var selectBranch =  treeCtrl.get_selected_branch();
                    //selectBranch.dataStatus = 9;
                    selectBranch.data.dataStatus = 9;
                });
                $scope.onlyread = true;
            }

        }else if(sysresource.dataStatus == 9){
            if (confirm("确定要删除数据  " + sysresource.name + "  ?")) {
                sysresourceService.delete(sysresource, function (data) {
                    treeData.forEach(function(item){
                        $scope.deleteNode(item, $scope.selectedSysResource);
                    });
                    $scope.selectedSysResource = null;
                    treeCtrl.select_next_branch();
                    toaster.pop('success','系统资源','删除成功');
                })
            }
        }
    }

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

    //恢复数据
    $scope.renew = function (sysresource,index) {
        if(confirm("确定要恢复该条数据?")){
            sysresource.dataStatus = 1;
            sysresourceService.save(sysresource,function (data) {
                if(sysresource.dataStatus != 1){
                    alert("恢复数据出错!")
                }
                $scope.onlyread = false;
            })
        }
    };


    //执行默认
    $scope.init();

});