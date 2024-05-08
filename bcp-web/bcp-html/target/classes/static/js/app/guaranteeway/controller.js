/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('guaranteewayController',function ($scope, guaranteewayService, imagetypeService, $modal, $q){

    $scope.tableTitle = "担保方式管理";
    $scope.tableDesc = "担保方式管理";

    /*  列表 */
    $scope.guaranteeways = [];//返回列表数据
    $scope.guaranteeway = {};
    $scope.imagetypes = []; //抵押资料
    $scope.currentPage = 1;


    $scope.init = function (){
        $scope.changePage();
        imagetypeService.lookup(function (data) {
            $scope.imagetypes = data;
        });
    }
    /**
     * 验证唯一
     * @param propname
     * @param propval
     * @returns {*}
     */
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.guaranteeway;
            guaranteewayService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };


    /**
     * 分页功能
     */
    $scope.changePage = function (){
        guaranteewayService.getDataByPage($scope.currentPage-1,function (data){
            $scope.guaranteeways = data.result;
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


    /* 担保方式 添加 / 修改 */
    $scope.edit = function (guaranteeway, index){
        /*  判断id进行回显 */
        if(guaranteeway.id != "" && guaranteeway.id != null){
            $scope.modalTitle="编辑担保方式";
            $scope.guaranteeway = angular.copy(guaranteeway);
        } else {
            $scope.modalTitle="添加担保方式";
            $scope.guaranteeway={dataStatus: 1};
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'guaranteewayModal.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }

                /* 保存担保方式 */
                $scope.save = function (){
                    guaranteewayService.save($scope.guaranteeway,function (data){
                        $scope.guaranteeway = data;
                        if(guaranteeway.id =="" || guaranteeway.id == null){
                            $scope.guaranteeways.push(data);
                        } else {
                            $scope.guaranteeways[index] = data;
                        }
                    });
                    /* 操作完成自动关闭模态框 */
                    modalInstance.close();
                }

            }
        });
    }

    /* 删除 */
    $scope.delete = function (guaranteeway,index) {
        var msg = "确定要作废数据 "+guaranteeway.name+"  ?";
        if (guaranteeway.dataStatus == 9) {
            msg = "确定要删除数据 "+guaranteeway.name+ " ?";
        }
        if (confirm(msg)) {
            var id = guaranteeway.id;
            guaranteewayService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (guaranteeway,index) {
        if(confirm("确定要恢复该条数据?")){
            guaranteeway.dataStatus = 1;
            guaranteewayService.save(guaranteeway,function (data) {
                if(guaranteeway.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

});