/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('carTypeController', function ($scope, carTypeService, $modal, $filter, FileUploader, $q, toaster, localStorageService, $rootScope) {
    var treeData,treeCtrl,carModelData,carModelTreeCtrl;
    $scope.title = '车型管理';
    $scope.tableTitle="车型列表";
    /*      车型列表*/
    $scope.cartypes = [];//返回的列表数据
    $scope.cartype = {};

    /**      车辆品牌   */
    $scope.carbrand = {};
    $scope.carbrands = [];
    $scope.carBrands={};

    $scope.selectedCarBrand = null;

    /**     车系*/
    $scope.carmodel = {};
    $scope.carmodels = carModelData = [];
    $scope.carmodelsTree = carModelTreeCtrl ={};
    $scope.carModels = {};
    $scope.selectedCarModel = null;
    $scope.currentPage = 1;
    $scope.syncing = false;

    //品牌LOGO上传
    $scope.fileUploaderBrand = new FileUploader({
        url: '/json/file/upload',
        headers: {'User-Token':localStorageService.get('User-Token'), 'Client-Type':'PC'},
    });
    $scope.fileUploaderBrand.onSuccessItem = function(fileItem, response, status, headers) {
        $scope.carbrand.logoFileId = response.d.id;
    };
    /**
     * 上传品牌文件
     */
    $scope.uploadBrand = function(){
        $scope.fileUploaderBrand.uploadAll();
    };

    /**
     * 上传车型文件
     */
    $scope.fileUploaderCartype = new FileUploader({
        url: '/json/file/upload',
        headers: {'User-Token':localStorageService.get('User-Token'), 'Client-Type':'PC'}
    });
    $scope.fileUploaderCartype.onSuccessItem = function(fileItem, response, status, headers) {
        $scope.cartype.imageFileId = response.d.id;
    };
    $scope.uploadCarType = function(){
        $scope.fileUploaderCartype.uploadAll();
    };
    /*
     * 初始化加载品牌
     * */
    $scope.init = function (){
        carTypeService.getCarBrands(function (data) {
            $scope.carBrands = data;

            $scope.checkSyncing();
        });
    };

    $scope.getAllCarBrand = function () {
        carTypeService.getCarBrands(function (data) {
            $scope.carBrands = data;
        });
    }

    //检查车型同步是否完成
    $scope.checkSyncing = function() {

        $scope.syncing = false;

        var asyncProc = localStorageService.get('ASYNC_PROC');
        if (angular.isArray(asyncProc)) {
            var index = asyncProc.indexOf('CARTYPE');
            if(index > -1){
                $scope.syncing = true;
            }
        }

    };

    //      选中品牌查询出该品牌对应的所有的车系
    $scope.selectCarBrand=function (carBrand) {

        if($scope.selectedCarBrand){
            $scope.selectedCarBrand.selected = false;
            $scope.cartypes = null;
        }

        carBrand.selected = true;
        $scope.selectedCarBrand = carBrand;
        var id = carBrand.id;
        carTypeService.getCarModels(id,function (data) {
            $scope.carModels = data;

        })
    };

    //      选中车系查询出该车系对应的所有的车型
    $scope.selectCarModel=function (carModel) {

        if(carModel == null){
            carModel = $scope.selectedCarModel;
        }

        if($scope.selectedCarModel) {
            $scope.selectedCarModel.selected = false;
        }

        carModel.selected = true;
        $scope.selectedCarModel = carModel;
        carTypeService.getCarTypes(carModel,$scope.currentPage-1,function (data) {
            $scope.cartypes = data.result;
            $scope.totalCartypeItems = data.totalCount;//数据总条数
            $scope.pageCartypeSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;
            $scope.totalCartypePages=data.totalPages;//总页数
        })
    };

    $scope.checkUniqueCarBrand = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.carbrand;
            carTypeService.checkUniqueCarBrand(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    $scope.checkUniqueCarModel = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.carmodel;
            carTypeService.checkUniqueCarModel(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
    
    $scope.checkUniqueCarType = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.cartype;
            carTypeService.checkUniqueCarType(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };


    /**
     * 模态框--- 车型  ---添加/修改页面
     */
    $scope.editCarType = function (cartype){

        if(cartype.id != "" && cartype.id != null){
            $scope.modalTitle="编辑车型";
            $scope.cartype = angular.copy(cartype);
        } else {
            $scope.modalTitle="添加车型";
            $scope.cartype = {dataStatus: 1,carModelId:$scope.selectedCarModel.id, carBrandId: $scope.selectedCarBrand.id};
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'carTypeForm.html',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 保存车型信息 */
                $scope.save = function (){
                    carTypeService.saveCarType($scope.cartype,function (data){
                        if (!$scope.cartype.id) {
                            $scope.cartypes.push(data);
                        } else {
                            var index = $scope.cartypes.indexOf(cartype);
                            $scope.cartypes[index] = data;
                        }
                        $scope.cartype = data;

                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                        toaster.pop('success','车型信息','保存成功！');
                    });
                }
            }
        });
    };

    /**
     * 模态框--  品牌  ---添加/修改页面
     */
    $scope.editCarBrand = function (carbrand) {
        if (carbrand == 0) {
            $scope.carbrand = {dataStatus: 1};
            $scope.modalTitle = "添加品牌";
        } else {
            $scope.carbrand = angular.copy($scope.selectedCarBrand);
            $scope.modalTitle = "编辑品牌";
        }
        // /!*模态框*!/
        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "carBrandForm.html",
            controller: function ($scope) {
                // /!*关闭模态框*!/
                $scope.cancel = function () {
                    modalInstance.close();
                };
                // /!*保存品牌信息*!/
                $scope.save = function () {
                    carTypeService.saveCarBrand($scope.carbrand, function (data) {
                        
                        if ($scope.carbrand.id != "" && $scope.carbrand.id != null){
                            var index = $scope.carBrands.indexOf($scope.selectedCarBrand);
                            $scope.carBrands[index] = data;
                            $scope.selectCarBrand($scope.carBrands[index]);
                        }else {
                            $scope.carBrands.push(data);

                        }

                    });
                    modalInstance.close();
                }
            }
        });
    };

    /**
     * 模态框---  车系  ---添加/修改页面
     */
    $scope.editCarModel = function (carmodel) {
        if (carmodel == 0) {
            $scope.carmodel = {dataStatus: 1,carBrandId:$scope.selectedCarBrand.id};
            $scope.modalTitle = "添加车系";
        } else {
            $scope.modalTitle = "编辑车系";
            //$scope.carmodel = $scope.selectedCarModel;
            $scope.carmodel = angular.copy($scope.selectedCarModel);
        }
        // /!*模态框*!/
        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "carModelForm.html",
            controller: function ($scope) {
                // /!*关闭模态框*!/
                $scope.cancel = function () {
                    modalInstance.close();
                };
                // /!*保存车系信息*!/
                $scope.save = function () {

                    carTypeService.saveCarModel($scope.carmodel, function (result) {

                        var selectedBranch =  $scope.selectedCarModel;
                        //  如果 车型数据中ID为空  ，为 添加
                        if ($scope.carmodel.id == null){
                            $scope.carModels.push(result);
                            var index = $scope.carModels.indexOf($scope.selectedCarModel);
                            $scope.carModels[index].selected = true;
                        }else{
                            var index = $scope.carModels.indexOf($scope.selectedCarModel);
                            $scope.carModels[index] = result;
                            $scope.selectCarModel($scope.carModels[index]);
                            selectedBranch = result;
                        }
                    });
                    modalInstance.close();
                    toaster.pop('success','车系信息','保存成功！');
                }
            }
        });
    };

    

    $scope.editCarBrandName = function (carbrand) {
        if (carbrand && carbrand.selected ){
            carbrand.editing = true;
        }
    };

    $scope.doneBrandNameEditing = function (carbrand){
        //  修改之後的名字
        var newCarbrandName = carbrand.name;
        carTypeService.saveCarBrand(carbrand,function (data) {
            var selectedBranch =  $scope.selectedCarBrand;
            selectedBranch.name = data.name;
        });
        carbrand.editing = false;
    };


    $scope.editCarModelName = function (carmodel) {
        if (carmodel && carmodel.selected ){
            carmodel.editing = true;
        }
    };

    $scope.doneModelNameEditing = function (carmodel){
        //  修改之後的名字
        var newCarModelName = carmodel.name;
        carTypeService.saveCarModel(carmodel,function (data) {
            var selectedBranch =  $scope.selectedCarModel;
            selectedBranch.name = data.name;
        });
        carmodel.editing = false;
    };


    //同步车型数据
    $scope.syncCarBrand = function () {
        if($scope.selectedCarBrand){
            var carBrand = $scope.selectedCarBrand;

            //获取异步进程的数组
            var asyncProc = localStorageService.get('ASYNC_PROC');

            if (!asyncProc) asyncProc = [];
            //添加到异步进程列表
            asyncProc.push('CARTYPE');
            localStorageService.set('ASYNC_PROC', asyncProc);

            carTypeService.syncCarBrand(carBrand.id,function (data) {
                toaster.pop('success', '系统消息', '正在同步车型数据，请等待！');
            })
        } else {
            alert("请选择要同步的车辆品牌！");
        }

    };

    /**
     * 监测ASYNC_PROC，判断是否还在同步车型
     */
    $scope.$on('LocalStorageModule.notification.setitem', function(event, msg){
        //检查用户数据
        if (msg.key == 'ASYNC_PROC' && msg.newvalue != 'null') {

            var asyncProc = localStorageService.get('ASYNC_PROC');
            var index = asyncProc.indexOf('CARTYPE');
            if(index > -1){
                $scope.syncing = true;
            } else {
                $scope.syncing = false;
            }
        }
    });

    //恢复数据
    $scope.renew = function (cartype,index) {
        if(confirm("确定要恢复该条数据?")){
            cartype.dataStatus = 1;
            carTypeService.saveCarType(cartype,function (data) {
                if(cartype.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };
    

    $scope.init();
});