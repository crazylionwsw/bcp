/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('versionController',function ($scope,versionService,localStorageService,$modal,FileUploader, toaster,$q){

    $scope.tableTitle = "版本管理";
    $scope.tableDesc ="版本列表";

    $scope.versions = [];
    $scope.version = {};

    $scope.currentPage = 1;

    //APK的上传
    $scope.fileUploaderVersion = new FileUploader({
        url: '/json/file/upload',
        headers: {'User-Token':localStorageService.get('User-Token'), 'Client-Type':'PC'},
    });

    $scope.fileUploaderVersion.onSuccessItem = function(fileItem, response, status, headers) {
        $scope.uploading = false;
        $scope.version.apkUrl = response.d.id;
        toaster.pop('success', '版本管理', 'APK版本包上传成功！');
    };
    /**
     * 文件上传
     */
    $scope.uploadVersion = function(){
        $scope.uploading = true;
        $scope.fileUploaderVersion.uploadAll();
    };

    /**
     * 初始化加载列表和分页
     */
    $scope.init = function(){
        $scope.changePage();
    };

    $scope.changePage = function (){

        versionService.getPageData($scope.currentPage-1,function(data){
            $scope.versions = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1 ;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });
    };


    /**
     * 模态框--添加/修改页面
     */
    $scope.edit = function (version){
        /*  判断id进行回显 */
        if(version.id != "" && version.id != null){
            $scope.modalTitle="编辑版本";
            //$scope.version = version;
            $scope.version = angular.copy(version);
        } else {
            $scope.modalTitle="添加版本";
            $scope.version={dataStatus: 1};
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'apkForm.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 保存版本信息 */
                $scope.save = function (){
                    versionService.saveVersion($scope.version,function (data){
                        if (!$scope.version.id) {
                            $scope.versions.push(data);
                        } else {
                            var index = $scope.versions.indexOf(version);
                            $scope.versions[index] = data;
                        }
                        $scope.version = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }

            }
        });
    };


    /* 删除 */
    $scope.delete = function (version,index) {
        var msg = "确定要作废此版本？";
        if(version.dataStatus == 9){
            msg = "确定要删除此版本？";
        }
        if (confirm(msg)) {
            var id = version.id;
            versionService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (version,index) {
        if(confirm("确定要恢复该条数据?")){
            version.dataStatus = 1;
            versionService.saveVersion(version,function (data) {
                if(version.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.version;
            versionService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();
});