/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('tasksController',function ($scope, tasksService, subScribeService,businesseventtypeService, $modal, $q){
    var treeCtrl;
    $scope.tableTitle = "任务消息管理";
    $scope.formTitle = "任务消息详细";

    $scope.taskdescribes  = [];//返回列表数据
    $scope.taskdescribe = {};

    $scope.selectTaskDescribe = {};
    
    $scope.currentPage =  1;

    $scope.descTypes = {};

    //列表
    $scope.init = function (){
        $scope.changePage();
        subScribeService.getDescType(function (data) {
            $scope.descTypes = data;
        });
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (){
        tasksService.getDataByPage($scope.currentPage-1,function (data){
            $scope.taskdescribes = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };
    /**
     * 删除一级
     * @param taskdescribe
     * @param index
     */
    $scope.delete = function (taskdescribe,index) {
        if (confirm("确定要删除此条数据?")) {
            var id = taskdescribe.id;
            tasksService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    /**
     * 执行初始化
     */
    $scope.init();

    //添加或修改
    $scope.edit = function (taskdescribe){
        /*  判断id进行回显 */
        if(taskdescribe.id != "" && taskdescribe.id != null){
            $scope.modalTitle="编辑任务消息";
            $scope.taskdescribe = angular.copy(taskdescribe);
        } else {
            $scope.modalTitle="添加任务消息";
            $scope.taskdescribe = {dataStatus: 1};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'tasksForm.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance,$q){
                
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 保存业务信息 */
                $scope.save = function (){
                    tasksService.save($scope.taskdescribe,function (data){
                        if (!$scope.taskdescribe.id) {
                            $scope.taskdescribes.push(data);
                        } else {
                            var index = $scope.taskdescribes.indexOf(taskdescribe);
                            $scope.taskdescribes[index] = data;
                        }
                        $scope.taskdescribe = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }
            }
        });
    };

    /* 删除 */
    $scope.delete = function (taskdescribe,index) {
        if (confirm("确定要删除此条数据?")) {
            var id = taskdescribe.id;
            tasksService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    /**
     * 详情页面
     * @param taskdescribe
     */
    $scope.seeTasks = function (taskdescribe) {

        /*  判断id进行回显 */
        if(taskdescribe.id != "" && taskdescribe.id != null){
            $scope.modalTitle="查看任务消息详细";
            $scope.taskdescribe = taskdescribe;
        } else {
            $scope.modalTitle="添加任务消息详细";
            $scope.taskdescribe = {dataStatus: 1};
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'seeTaskForm.html',
            controller:function ($scope,$modalInstance,$q){
                $scope.taskDescribeTree = treeCtrl = {};
                $scope.taskDescribeChilds = [];//返回列表数据
                $scope.taskDescribeChild = {};

                $scope.onlyread = true;
                $scope.showadd = true;
                $scope.currentPid;
                $scope.selectedPid;
                $scope.selectTaskDescribe = function (branch) {
                    if(branch.data.pid){
                        $scope.selectedPid = branch.data.pid;
                    }else{
                        $scope.selectedPid = undefined;
                    }
                    branch.selected = true;
                    $scope.showadd = true;
                    $scope.onlyread = true;
                    if(branch.children.length==0){
                        $scope.needResource(branch.data);
                    }
                    $scope.selectLabel = branch.label;
                    if($scope.selectedTaskDescribe){ //取消之前选中的
                        $scope.selectedTaskDescribe.selected = false;
                    }
                    if(branch.data){
                        $scope.selectedTaskDescribe = branch.data;
                        $scope.taskDescribeChild = branch.data;
                    } else {
                        $scope.selectedTaskDescribe = null;
                    }
                };

                $scope.createDynamic = function () {
                    $scope.onlyread = false;
                    $scope.currentPid = $scope.selectedTaskDescribe.id;
                        //添加
                    $scope.taskDescribeChild={dataStatus: 1};
                    if ($scope.selectedTaskDescribe)
                        $scope.taskDescribeChild.pid = $scope.selectedTaskDescribe.id;
                };

                $scope.edit = function(){
                    $scope.onlyread = false;
                    $scope.currentPid = $scope.selectedPid;
                    $scope.taskDescribeChild = $scope.selectedTaskDescribe;
                };

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                $scope.needResource = function(data){
                    if(data.neededResource){
                        for(var i=0;i<data.neededResource.length;i++){
                            var node = data.neededResource[i];
                            node.pid = data.id;
                            var treenode = {label: node.beanId, id: node.id,data: node};
                            treeCtrl.add_branch(treeCtrl.get_selected_branch(), treenode);
                        }
                    }
                };

                var seeTasksChild = function(taskdescribe){
                    var id = $scope.taskdescribe.id;
                    tasksService.seeTasksChild(id, function (data) {
                        if(data.id){
                            $scope.showadd = false;
                            treeCtrl.add_branch(treeCtrl.get_selected_branch(), {label: data.beanId, id: data.id,data: data});
                        }else{
                            $scope.showadd = true;
                        }
                    });
                };
                seeTasksChild(taskdescribe);

                /* 保存任务信息 */
                $scope.saveSeeTask = function (){
                    var taskDescribeChild = $scope.taskDescribeChild;
                    var id =  $scope.taskdescribe.id;
                    tasksService.saveDescribe(taskDescribeChild,id,$scope.currentPid,function (data){
                        data.pid = $scope.currentPid;
                        if(taskDescribeChild.id =="" || taskDescribeChild.id == null){
                            treeCtrl.add_branch(treeCtrl.get_selected_branch(), {label: data.beanId, id: data.id,data: data});
                        }else{
                            treeCtrl.get_selected_branch().label = data.beanId;
                            treeCtrl.get_selected_branch().id = data.id;
                        }
                        /* 操作完成自动关闭模态框 */
                        $scope.onlyread = true;
                    });
                };

                $scope.remove = function (taskDescribeChild,index) {
                    if (confirm("确定要删除此条数据?")) {
                        var id = taskDescribeChild.id;
                        tasksService.removeTask(id, function (data) {
                            $scope.taskDescribeChilds.splice(index, 1);
                            $scope.taskDescribeChild = [];
                            $scope.cancel();
                        });
                    }
                }
            }
        });
    };

    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.taskdescribe;
            tasksService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
});