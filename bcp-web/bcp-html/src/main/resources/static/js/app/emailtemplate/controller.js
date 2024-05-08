/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('emailTemplateController',function ($scope,emailTemplateService,msgTemplatelService,documentService,$modal,$q){

    $scope.tableTitle = "邮件模版管理";
    $scope.tableDesc = "邮件模版管理";

    /*  列表 */
    $scope.emailtemplates  = [];//返回列表数据
    $scope.emailtemplate = {};
    
    $scope.templateObjects = [];

    $scope.documents = [];

    $scope.currentPage = 1;

    $scope.init = function (){
        msgTemplatelService.getAll(function (data) {
           $scope.templateObjects = data;
        });

        documentService.lookup(function (data) {
            $scope.documents = data;
        });
        $scope.changePage();
    };


    /**
     * 分页功能
     */
    $scope.changePage = function (){
        emailTemplateService.getDataByPage($scope.currentPage-1,function (data){
            $scope.emailtemplates = data.result;
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
    $scope.edit = function (emailtemplate){
        /*  判断id进行回显 */
        if(emailtemplate.id != "" && emailtemplate.id != null){
            $scope.modalTitle="编辑邮件模板";
            $scope.emailtemplate = angular.copy(emailtemplate);
        } else {
            $scope.modalTitle="添加邮件模板";
            $scope.emailtemplate = {dataStatus: 1};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'emailTemplateModal.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 保存 */
                $scope.save = function (){
                    emailTemplateService.save($scope.emailtemplate,function (data){
                        if (!$scope.emailtemplate.id) {
                            $scope.emailtemplates.push(data);
                            $scope.changePage();
                        } else {
                            var index = $scope.emailtemplates.indexOf(emailtemplate);
                            $scope.emailtemplates[index] = data;
                        }
                        $scope.emailtemplate = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });

                }
            }
        });
    };


    /* 删除 */
    $scope.delete = function (emailtemplate,index) {
        var msg = "确定要作废此数据  ?";
        if (emailtemplate.dataStatus == 9) {
            msg = "确定要删除此数据  ?";
        }
        if (confirm(msg)) {
            var id = emailtemplate.id;
            emailTemplateService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (emailtemplate,index) {
        if(confirm("确定要恢复该条数据?")){
            emailtemplate.dataStatus = 1;
            emailTemplateService.save(emailtemplate,function (data) {
                if(emailtemplate.dataStatus != 1){
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
            var entity = $scope.emailtemplate;
            emailTemplateService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
    
});