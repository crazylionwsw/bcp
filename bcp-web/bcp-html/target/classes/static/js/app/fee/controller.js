/**
 * Created by Administrator on 2016/12/29.
 */


'use strict';

app.controller('feeController',function ($scope,feeService,$modal,$q){
    $scope.tableTitle = "收费项管理";
    $scope.tableDesc = "收费项列表";
    /*  列表 */
    $scope.feeitems= [];//返回列表数据
    $scope.feeitem = {};

    $scope.currentPage = 1;

    $scope.init = function (){
        $scope.changePage();
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (){
        feeService.getDataByPage($scope.currentPage-1,function (data){
            $scope.feeitems = data.result;
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

    //验证唯一
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.feeitem;
            feeService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /* 收费项添加 / 修改 */
    $scope.edit = function (feeitem){
        /*  判断id进行回显 */
        if(feeitem.id != "" && feeitem.id != null){
            $scope.modalTitle="编辑收费项";
            var feeItemEdit=angular.copy(feeitem);
            $scope.feeitem = feeItemEdit;
        }else{
            $scope.modalTitle="添加收费项";
            $scope.feeitem = {dataStatus : 1};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'feeItemModal.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };


                /* 保存业务信息 */
                $scope.save = function (){
                    feeService.save($scope.feeitem,function (data){
                        if (!$scope.feeitem.id) {
                            $scope.feeitems.push(data);
                        } else {
                            var index = $scope.feeitems.indexOf(feeitem);
                            $scope.feeitems[index] = data;
                        }
                        $scope.feeitem = data;
                        modalInstance.close();
                    });
                }

            }
        });
    };

    /* 删除 */
    $scope.delete = function (feeitem,index) {
        var msg = "确定要作废数据  ?";
        if (feeitem.dataStatus == 9) {
            msg = "确定要删除数据  ?";
        }
        if (confirm(msg)) {
            var id = feeitem.id;
            feeService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (feeitem,index) {
        if(confirm("确定要恢复该条数据?")){
            feeitem.dataStatus = 1;
            feeService.save(feeitem,function (data) {
                if(feeitem.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

});