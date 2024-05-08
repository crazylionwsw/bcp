/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('msgTemplatelController',function ($scope, msgTemplatelService,localStorageService,sysparamService, imagetypeService, FileUploader, $modal, $q,toaster){
    $scope.tableTitle = "模版类型管理";
    $scope.tableDesc = "模版类型管理";

    $scope.templates = [];//返回列表数据
    $scope.template = {};

    $scope.show = true;
    $scope.showuploader = true;

    $scope.currentPage = 1;

    $scope.metaDatas = [];

    $scope.fileUploaderTemplate = new FileUploader({
        url: '/json/file/upload',
        headers: {'User-Token':localStorageService.get('User-Token'), 'Client-Type':'PC'},
    });

    $scope.fileUploaderTemplate.onSuccessItem = function(fileItem, response, status, headers) {
        $scope.uploading = false;
        $scope.template.fileId = response.d.id;
        toaster.pop('success', '模版管理', '模版文件上传成功！');
    };
    /**
     * 文件上传
     */
    $scope.uploadTemplate = function(){
        var userForm = document.forms["userForm"];
        if(userForm["file"].files.length == 0){
            alert("请选择上传文件!");
        }else{
            $scope.uploading = true;
            $scope.fileUploaderTemplate.uploadAll();
        }
        // if(file == null){
        //    alert("请选择上传文件!");
        // }else {
        //     $scope.uploading = true;
        //     $scope.fileUploaderTemplate.uploadAll();
        // }
    };
    
    //重新上传
    $scope.anew = function () {
        $scope.showuploader = true;
        $scope.show = false;
    };

    //列表
    $scope.init = function (){
        //获取系统参数中的元数据
        sysparamService.getListByCode("METADATAS",function (data) {
            $scope.metaDatas = data;
        });
        imagetypeService.getAll(function (data) {
            $scope.imagetypes = data;
        });
        $scope.changePage();
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (){

        msgTemplatelService.getDataByPage($scope.currentPage-1,function (data){
            $scope.templates = data.result;
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
    $scope.edit = function (template){

        /*  判断id进行回显 */
        if(template.id != "" && template.id != null){
            $scope.modalTitle="编辑模版类型";

            $scope.template = template;
            if (template.fileBean) {
                $scope.template.fileId = template.fileBean.id;
                $scope.template.fileName = template.fileBean.fileName;
                $scope.show = true;
                $scope.showuploader = false;
            }
        } else {
            $scope.modalTitle="添加模版类型 ";
            $scope.template = {dataStatus: 1};
            $scope.show = false;
            $scope.showuploader = true;
        }


        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'msgTemplatelForm.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    $scope.changePage();
                    modalInstance.close();
                };

                
                /* 保存模板类型信息 */
                $scope.save = function (){
                    var template = $scope.template;
                    var temId = $scope.template.fileId;

                    if (temId == null){
                        alert("请确认所上传的模板文件！");
                    }else {
                        msgTemplatelService.save(template,temId,function (data){

                            if(template.id =="" || template.id == null){
                                $scope.templates.push(data);
                            }else {
                                var index = $scope.templates.indexOf(template);
                                $scope.templates[index] = data;
                            };
                            $scope.template = data;

                            /* 操作完成自动关闭模态框 */
                            modalInstance.close();
                            toaster.pop('success','模板类型','保存成功！');
                        });

                    };
                }
            }
        });
    };

    /**
     * 唯一性验证
     * @param propname
     * @param propval
     * @returns {*}
     */
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.template;
            msgTemplatelService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /* 删除 */
    $scope.delete = function (template,index) {
        var msg = "确定要作废此数据  ?";
        if (template.dataStatus == 9) {
            msg = "确定要删除此数据  ?";
        }
        if (confirm(msg)) {
            var id = template.id;
            msgTemplatelService.delete(id, function (data) {
                if (template.dataStatus == 9) {
                    $scope.templates.splice(index, 1);
                } else {
                    $scope.templates[index] = data;
                }
            });
        }
    };

    //恢复数据
    $scope.renew = function (template,index) {
        $scope.template = template;
        if (template.fileBean) {
            $scope.template.fileId = template.fileBean.id;
        }
        var temId = $scope.template.fileId;
        if(confirm("确定要恢复该条数据?")){
            template.dataStatus = 1;
            msgTemplatelService.save(template,temId,function (data) {
                if(template.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    $scope.preview = function (template) {

        //window.open();
/*        /!*  判断id进行回显 *!/
        if(template.id != "" && template.id != null){
            $scope.modalTitle="查看详细模版内容";
            $scope.template = template;
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'seeTemForm.html',

            controller:function ($scope,$modalInstance,$q){

                /!*  关闭模态框 *!/
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }

            }
        });*/
    };
  
});
