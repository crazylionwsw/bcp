/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('imagetypeController', function ($scope,imagetypeService,localStorageService,FileUploader,toaster,$q){
    $scope.title = '档案类型管理';
    $scope.tableTitle = "档案类型列表";
    $scope.modalTitle = "档案类型";

    $scope.imagetypes = [];//列表
    $scope.imagetype = {};
    $scope.selectedImageType = {};//选中当前的

    /*禁用*/
    $scope.disForm = false;
    $scope.onlyread = false;

    /*  列表 */
    $scope.contracts  = [];//返回列表数据
    $scope.contract = {};

    /**    系统角色列表     */
    $scope.sysRoles = [];
    $scope.sysRole = {};

    //预览图
    $scope.previewShow = false;
    
    var fileUploader = $scope.fileUploader= new FileUploader({
        url: '/json/file/upload',
        headers: {'User-Token':localStorageService.get('User-Token'), 'Client-Type':'PC'},

    });

    $scope.fileUploader.onSuccessItem = function(fileItem, response, status, headers) {
        if (!$scope.imagetype.exampleFileIds) $scope.imagetype.exampleFileIds = [];
        $scope.imagetype.exampleFileIds.push(response.d.id);
    };

    $scope.deleteFile = function(imagetype,fileid,idx){
        imagetypeService.delImgFile(imagetype,fileid,function (data) {
            $scope.imagetype = data;
        });
        $scope.imagetype.exampleFileIds.splice(idx,1);
    };


    $scope.previewShowButton = function () {
        $scope.previewShow = true;
    };

    /**
     * 初始化加载列表
     */
    $scope.init = function () {
        imagetypeService.getAll(function (data) {
            $scope.imagetypes = data;
        });
        imagetypeService.getAllContract(function (data){
            $scope.contracts = data;//返回的数据
        });
        imagetypeService.getAllSysRoles(function (data) {
            $scope.sysRoles = data;
        })
    };

    /**
     * 执行初始化
     */
    $scope.init();

    /**
     * 选定
     */
    $scope.selectImageType = function(imageType){

        if($scope.selectedImageType){
            $scope.selectedImageType.selected = false;
        }
        imageType.selected = true;
        //$scope.fileSelected = null;
        $scope.selectedImageType = imageType;
        $scope.imagetype = angular.copy(imageType);

        $scope.imagetype.suffixesTags = [];
        angular.forEach($scope.imagetype.suffixes, function(k,index){
            $scope.imagetype.suffixesTags.push({text: $scope.imagetype.suffixes[index]});
        });


        if(imageType.dataStatus == 9){
            $scope.disForm = true;
            $scope.onlyread = true;
        }else{
            $scope.disForm = false;
            $scope.onlyread = false;
        }
    };


    $scope.discard = function (){
        var imagetype = $scope.selectedImageType;
        if (imagetype.dataStatus == 1 ){
            if (confirm("确定要作废数据  " + imagetype.name + "  ?")) {
                imagetypeService.delete(imagetype, function (data) {
                    toaster.pop('success','档案类型','作废成功');
                    var index = $scope.imagetypes.indexOf(imagetype);
                    $scope.imagetypes[index] = data;
                    $scope.imagetype = data;

                    $scope.selectImageType($scope.imagetype);
                })
            }
        }else if(imagetype.dataStatus == 9){
            if (confirm("确定要删除数据  " + imagetype.name + "  ?")) {
                imagetypeService.delete(imagetype, function (data) {
                    //  如果 返回的数据状态为 作废
                    if (imagetype.dataStatus == 9){
                        var index = $scope.imagetypes.indexOf(imagetype);
                        $scope.imagetypes.splice(index, 1);
                        $scope.imagetype = {};
                        $scope.add();
                        toaster.pop('success','档案类型','删除成功');
                    }
                })
            }
        }
    };

    //恢复数据
    $scope.renew = function () {
        var imagetype = $scope.selectedImageType;
        if(confirm("确定要恢复该条数据?")){
            imagetype.dataStatus = 1;
            imagetypeService.save(imagetype,function (data) {
                if(imagetype.dataStatus != 1){
                    alert("恢复数据出错!");
                }
                /*选中当前*/
                $scope.selectImageType(imagetype);
                toaster.pop('success','档案类型','恢复成功');
            })
        }
    };


    /**
     * 上传文件
     */
    $scope.upload = function(){
        fileUploader.uploadAll();
    };

    /**
     * 保存
     */
    $scope.save = function () {
        var imagetype = $scope.imagetype;
        imagetype.suffixes = [];

        angular.forEach(imagetype.suffixesTags, function(item,index){
            imagetype.suffixes[index] = imagetype.suffixesTags[index].text;
        });
        imagetypeService.save(imagetype, function (data) {
            toaster.pop('success', '档案类型', '保存成功！');
            $scope.imagetype = data;

            if(imagetype.id == "" || imagetype.id == null){
                $scope.imagetypes.push($scope.imagetype);
            } else {
                angular.forEach($scope.imagetypes, function(item, index){
                    if (item.id == $scope.imagetype.id) {
                        $scope.imagetypes[index] = $scope.imagetype;
                    }
                });
            }
            $scope.selectImageType($scope.imagetype);
        });
    };

    //预览图
    // $scope.file='';
    // $scope.myCroppedImage='';
    // $scope.cropType="circle";

/*    var handleFileSelect=function(evt) {
        var file=evt.currentTarget.files[0];
        var reader = new FileReader();
        reader.onload = function (evt) {
            $scope.$apply(function($scope){
                $scope.fileSelected=evt.target.result;
            });
        };
        reader.readAsDataURL(file);
    };*/

    $scope.fileUploader.onAfterAddingAll = function(addedFileItems) {
        var file = addedFileItems[0]._file;
        var reader = new FileReader();
        reader.onload = function (evt) {
            $scope.$apply(function($scope){
                $scope.fileSelected=evt.target.result;
            });
        };
        reader.readAsDataURL(file);
    };

    //angular.element(document.querySelector('#file')).on('change',handleFileSelect);

    /**
     * 添加档案类型
     */
    $scope.add = function(){

        $scope.disForm = false;
        $scope.onlyread = false;
        //清除选定的档案类型
        if($scope.selectedImageType) $scope.selectedImageType.selected = false;
        $scope.imagetype = {dataStatus:1};

        $scope.userForm.$setPristine();
    };

    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.imagetype;
            imagetypeService.checkUniqueImagetype(entity,propname,propval,function(data){
                if(data=='false'){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
    
});