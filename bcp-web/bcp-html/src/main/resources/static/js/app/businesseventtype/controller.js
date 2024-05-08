/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('businesseventtypeController',function ($scope, businesseventtypeService, $modal, $q){
    $scope.tableTitle = "业务事件类型管理";

    $scope.businessEventTypes = [];//返回列表数据
    $scope.businessEventType = {};

    $scope.currentPage = 1;
    //列表
    $scope.init = function (){
        $scope.changePage();

    }

    /**
     * 分页功能
     */
    $scope.changePage = function (){

        businesseventtypeService.getDataByPage($scope.currentPage-1,function (data){
            $scope.businessEventTypes = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });
    }

    /**
     * 执行初始化
     */
    $scope.init();

     //添加或修改
     $scope.edit = function (businessEventType){

         /*  判断id进行回显 */
         if(businessEventType.id != "" && businessEventType.id != null){
            $scope.modalTitle="编辑事件类型";
            $scope.businessEventType = businessEventType;
        } else {
            $scope.modalTitle="添加事件类型";
            $scope.businessEventType = {dataStatus: 1};
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'eventForm.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    $scope.changePage();
                    modalInstance.close();
                }

                /* 保存事件信息 */
                $scope.save = function (){
                    var businessEventType = $scope.businessEventType;
                    businesseventtypeService.save(businessEventType,function (data){
                        $scope.businessEventType = data;
                        if(businessEventType.id =="" || businessEventType.id == null){
                            $scope.businessEventTypes.push(data);
                        }
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });

                }
            }
        });
    }

    /**
     * 唯一性验证
     * @param propname
     * @param propval
     * @returns {*}
     */
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.businessEventType;
            businesseventtypeService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };


     /* 删除 */
    $scope.delete = function (businessEventType,index) {
        var msg = "确定要作废数据  ?";
        if (businessEventType.dataStatus == 9) {
            msg = "确定要删除数据  ?";
        }
        if (confirm(msg)) {
            var id = businessEventType.id;
            businesseventtypeService.delete(id, function (data) {
                $scope.init();
           });
        }
     };

    //恢复数据
    $scope.renew = function (businessEventType,index) {
        if(confirm("确定要恢复该条数据?")){
            businessEventType.dataStatus = 1;
            businesseventtypeService.save(businessEventType,function (data) {
                if(businessEventType.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

});
