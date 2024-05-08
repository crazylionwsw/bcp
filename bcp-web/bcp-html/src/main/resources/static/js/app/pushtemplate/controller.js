/**
 * Created by admin on 2017/10/13.
 */

'use strict';

app.controller('pushTemplateController',function ($scope,pushTemplateService,msgTemplatelService,$modal,$q){

    $scope.tableTitle = "推送模版管理";
    $scope.tableDesc = "推送模版管理";

    /*  列表 */
    $scope.pushtemplates  = [];//返回列表数据
    $scope.pushtemplate = {};

    $scope.templateObjects = [];

    $scope.currentPage = 1;

    $scope.init = function (){
        msgTemplatelService.getAll(function (data) {
            $scope.templateObjects = data;
        });

       /* documentService.lookup(function (data) {
            $scope.documents = data;
        });*/
        $scope.changePage();
    };


    /**
     * 分页功能
     */
    $scope.changePage = function (){
        pushTemplateService.getDataByPage($scope.currentPage-1,function (data){
            $scope.pushtemplates = data.result;
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


    /*  添加 / 修改 */
    $scope.edit = function (pushtemplate){
        /*  判断id进行回显 */
        if(pushtemplate.id != "" && pushtemplate.id != null){
            $scope.modalTitle="编辑推送模板";
            $scope.pushtemplate = angular.copy(pushtemplate);
        } else {
            $scope.modalTitle="添加推送模板";
            $scope.pushtemplate = {dataStatus: 1};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'pushTemplateModal.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 保存 */
                $scope.save = function (){
                    pushTemplateService.save($scope.pushtemplate,function (data){
                        if (!$scope.pushtemplate.id) {
                            $scope.pushtemplates.push(data);
                        } else {
                            var index = $scope.pushtemplates.indexOf(pushtemplate);
                            $scope.pushtemplates[index] = data;
                        }
                        $scope.pushtemplate = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });

                }
            }
        });
    };


    /* 删除 */
    $scope.delete = function (pushtemplate,index) {
        var msg = "确定要作废此数据  ?";
        if (pushtemplate.dataStatus == 9) {
            msg = "确定要删除此数据  ?";
        }
        if (confirm(msg)) {
            var id = pushtemplate.id;
            pushTemplateService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (pushtemplate,index) {
        if(confirm("确定要恢复该条数据?")){
            pushtemplate.dataStatus = 1;
            pushTemplateService.save(pushtemplate,function (data) {
                if(pushtemplate.dataStatus != 1){
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
            var entity = $scope.pushtemplate;
            pushTemplateService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

});