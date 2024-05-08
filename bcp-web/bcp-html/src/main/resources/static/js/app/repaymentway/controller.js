/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('repaymentwayController',function ($scope,repaymentwayService,$modal,$q){

    $scope.tableTitle = "还款方式管理";
    $scope.tableDesc = "还款方式管理";

    /*  列表 */
    $scope.repaymentways  = [];//返回列表数据
    $scope.repaymentway = {};

    $scope.currentPage = 1;

    $scope.init = function (){
        $scope.changePage();
    };


    /**
     * 分页功能
     */
    $scope.changePage = function (){
        repaymentwayService.getDataByPage($scope.currentPage-1,function (data){
            $scope.repaymentways = data.result;
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


    /* 还款方式 添加 / 修改 */
    $scope.edit = function (repaymentway){
        /*  判断id进行回显 */
        if(repaymentway.id != "" && repaymentway.id != null){
            $scope.modalTitle="编辑还款方式";
            //还款比例
            var ratioes = repaymentway.ratioes;
            repaymentway.ratioTags = [];
            for(var i in ratioes){
                repaymentway.ratioTags.push({text: ratioes[i]});
            }
            $scope.repaymentway = angular.copy(repaymentway);
        } else {
            $scope.modalTitle="添加还款方式";
            $scope.repaymentway = {dataStatus: 1};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'repaymentwayModal.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 保存还款方式 */
                $scope.save = function (){
                    var repayMent = $scope.repaymentway;
                    repayMent.ratioes = {};
                    $.each(repayMent.ratioTags, function(j){
                        repayMent.ratioes[j] = repayMent.ratioTags[j].text;
                    });
                    repaymentwayService.save($scope.repaymentway,function (data){
                        if (!$scope.repaymentway.id) {
                            $scope.repaymentways.push(data);
                        } else {
                            var index = $scope.repaymentways.indexOf(repaymentway);
                            $scope.repaymentways[index] = data;
                        }
                        $scope.repaymentway = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }
            }
        });
    };


    /* 删除 */
    $scope.delete = function (repaymentway,index) {
        var msg = "确定要作废此条数据？";
        if(repaymentway.dataStatus == 9){
            msg = "确定要删除此条数据？";
        }
        if (confirm(msg)) {
            var id = repaymentway.id;
            repaymentwayService.delete(id, function (data) {
                $scope.init();
            });
        }
    };
    //恢复数据
    $scope.renew = function (repaymentway,index) {
        if(confirm("确定要恢复该条数据?")){
            repaymentway.dataStatus = 1;
            repaymentwayService.save(repaymentway,function (data) {
                if(repaymentway.dataStatus != 1){
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
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.repaymentway;
            repaymentwayService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
});