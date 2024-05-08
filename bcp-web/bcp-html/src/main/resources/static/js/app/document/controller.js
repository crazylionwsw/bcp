/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('documentController',function ($scope,documentService,msgTemplatelService,imagetypeService,$modal,$q){

    $scope.tableTitle = "文档模板管理";
    $scope.tableDesc = "文档模板管理";

    $scope.contractings =[
        {
            id: 0,
            name: '资金提供方'
        },
        {
            id: 1,
            name: '客户'
        },
        {
            id: 2,
            name: '渠道'
        },
        {
            id: 3,
            name: '富择'
        }
    ];
    $scope.types=[
        {
            id:"pdf",
            name:"pdf"
        },
        {
            id:"docx",
            name:"docx"
        },
        {
            id:"doc",
            name:"doc"
        }

    ];

    /*  列表 */
    $scope.documents  = [];//返回列表数据
    $scope.document = {};
    
    $scope.templateObjects = [];

    $scope.currentPage = 1;

    $scope.init = function (){
        msgTemplatelService.getAll(function (data) {
           $scope.templateObjects = data;
        });

        //获取可用档案类型列表
        imagetypeService.lookup(function (data){
            $scope.customerimagetypes = data;
        });

        $scope.changePage();
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (){
        documentService.getDataByPage($scope.currentPage-1,function (data){
            $scope.documents = data.result;
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


    /* 合同管理 添加 / 修改 */
    $scope.edit = function (document){
        /*  判断id进行回显 */
        if(document.id != "" && document.id != null){
            $scope.modalTitle="编辑文档模板";
            $scope.document = angular.copy(document);
        } else {
            $scope.modalTitle="添加文档模板";
            $scope.document = {dataStatus: 1};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'contractModal.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 保存文档模板 */
                $scope.save = function (){
                    documentService.save($scope.document,function (data){
                        if (!$scope.document.id) {
                            $scope.documents.push(data);
                            $scope.changePage();
                        } else {
                            var index = $scope.documents.indexOf(document);
                            $scope.documents[index] = data;
                        }
                        $scope.document = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });

                }
            }
        });
    };


    /* 删除 */
    $scope.delete = function (document,index) {
        var msg = "确定要作废此数据  ?";
        if (document.dataStatus == 9) {
            msg = "确定要删除此数据  ?";
        }
        if (confirm(msg)) {
            var id = document.id;
            documentService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (document,index) {
        if(confirm("确定要恢复该条数据?")){
            document.dataStatus = 1;
            documentService.save(document,function (data) {
                if(document.dataStatus != 1){
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
            var entity = $scope.document;
            entity.comment = propval;
            documentService.checkUnique(entity,propname,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
    
});