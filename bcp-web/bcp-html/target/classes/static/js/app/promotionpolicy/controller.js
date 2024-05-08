/**
 * Created by zxp on 2017/3/5.
 */
'use strict';
app.controller('promotionpolicyController',function ($scope,promotionpolicyService,$modal, $q) {
    $scope.tableDesc = "促销列表";
    $scope.tableTitle="促销管理"
    $scope.promotionpolicys = [];//返回列表数据
    $scope.promotionpolicy = {};

    $scope.currentPage = 1;

    //列表
    $scope.init = function () {
        $scope.changePage();
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (){
        promotionpolicyService.getDataByPage($scope.currentPage-1,function (data){
            $scope.promotionpolicys = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };

    /* 促销添加 / 修改 */
    $scope.edit = function (promotionpolicy){
        /*  判断id进行回显 */
        if(promotionpolicy.id != "" && promotionpolicy.id != null){
            $scope.modalTitle="编辑促销";
            $scope.promotionpolicy = angular.copy(promotionpolicy);
        } else {
            $scope.modalTitle="添加促销";
            $scope.promotionpolicy = {dataStatus: 1};
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'promotionPolicyModal.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    $scope.changePage();
                    modalInstance.close();
                }

                /* 保存促销信息 */
                $scope.save = function (){
                    promotionpolicyService.save($scope.promotionpolicy,function (data){
                        if (!$scope.promotionpolicy.id) {
                            $scope.promotionpolicys.push(data);
                        } else {
                            var index = $scope.promotionpolicys.indexOf(promotionpolicy);
                            $scope.promotionpolicys[index] = data;
                        }
                        $scope.promotionpolicy = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });

                }
            }
        });
    };

    //开始日期大于结束日期
    $scope.checkEndDate = function(value) {
        if (value){
            if(value){
                if (moment(value).isAfter($scope.promotionpolicy.startDate)) {
                    return true;
                } else {
                    return false;
                }
            }
        }else{
            return true;
        }
    };

    /* 删除 */
    $scope.delete = function (promotionpolicy,index) {
        var msg = "确定要作废此条数据  ?";
        if (promotionpolicy.dataStatus == 9) {
            msg = "确定要删除此条数据  ?";
        }
        if (confirm(msg)) {
            var id = promotionpolicy.id;
            promotionpolicyService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (promotionpolicy,index) {
        if(confirm("确定要恢复此条数据 ？")){
            promotionpolicy.dataStatus = 1;
            promotionpolicyService.save(promotionpolicy,function (data) {
                if(promotionpolicy.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    /**
     * 执行初始化
     */
    $scope.init();


    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.promotionpolicy;
            promotionpolicyService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
})