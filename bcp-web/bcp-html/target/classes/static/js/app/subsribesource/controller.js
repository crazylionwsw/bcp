/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('subsribesourceController',function ($scope, subsribesourceService, $modal, $q, $filter){
    
    $scope.tableTitle = "订阅源管理";
    $scope.tableDesc = "订阅源管理";

    $scope.subsribesources = [];//返回列表数据
    $scope.subsribesource = {}; //对象

    $scope.currentPage =  1;

    //列表
    $scope.init = function (){
        $scope.changePage();
    };
    /**
     * 分页功能
     */
    $scope.changePage = function (){
        subsribesourceService.getDataByPage($scope.currentPage-1,function (data){
            $scope.subsribesources = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();

    //添加或修改
    $scope.edit = function (subsribesource,index){
        /*  判断id进行回显 */
        if(subsribesource.id != "" && subsribesource.id != null){
            $scope.modalTitle="编辑订阅源";
            $scope.subsribesource = angular.copy(subsribesource);
        } else {
            $scope.modalTitle="添加订阅源";
            $scope.subsribesource = {dataStatus: 1};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'subsribesourceForm.html',
            controller:function ($scope,$modalInstance,$q){

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 保存订阅源 */
                $scope.save = function (){

                    subsribesourceService.save($scope.subsribesource,function (data){
                        if(!$scope.subsribesource.id){
                            $scope.subsribesources.push(data)
                        }else{
                            var index = $scope.subsribesources.indexOf(subsribesource);
                            $scope.subsribesources[index] = data;
                        }
                        $scope.subsribesource = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }
            }
        });
    };

    /* 删除 */
    $scope.delete = function (subsribesource,index) {
        var msg = "确定要作废此数据  ?";
        if (subsribesource.dataStatus == 9) {
            msg = "确定要删除此数据  ?";
        }
        if (confirm(msg)) {
            var id = subsribesource.id;
            subsribesourceService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (subsribesource,index) {
        if(confirm("确定要恢复该条数据?")){
            subsribesource.dataStatus = 1;
            subsribesourceService.save(subsribesource,function (data) {
                if(subsribesource.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    /**
     * 唯一性验证
     * @param propname
     * @param propval
     * @returns {*}
     */
    $scope.checkUnique = function(proname,propvalue){
        return $q(function (resolve, reject) {
            var entity = $scope.subsribesource;
            subsribesourceService.checkUnique(entity,proname,propvalue,function (data) {
                if (data=="false"){
                    reject();
                }else{
                    resolve();
                }
            })
        })
    }
    
});