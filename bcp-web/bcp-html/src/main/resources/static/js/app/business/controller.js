/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('businessController',function ($scope, businessService, billtypeService, cashsourceService, imagetypeService, feeService,$modal, $q, $filter){
    
    $scope.tableTitle = "业务类型管理";
    $scope.tableDesc = "业务类型管理";

    $scope.businesses = [];//返回列表数据
    $scope.business = {};
    $scope.customerimagetypes = [];//资料类型
    $scope.currentPage =  1;
    $scope.billtypes =[];
    $scope.fees = [];

    //期率类型
    $scope.rateTypes = {};
    $scope.rateType = [];


    //列表
    $scope.init = function (){

        $scope.changePage();
		
		//获取可用单据类型列表
        billtypeService.lookup(function (data) {
            $scope.billtypes = data;
        });
		
		//获取可用档案类型列表
        imagetypeService.lookup(function (data){
            $scope.customerimagetypes = data;
        });
		
		//获取可用收费项列表
        feeService.lookup(function (data) {
            $scope.fees = data;
        });
		
		//获取可用资金利率列表
        cashsourceService.looupSourceRates(function (data){
            $scope.sourcerates = data;
        });
    };
    


    // $scope.checkUnique = function(propname,propval) {
    //     return $q(function(resolve, reject){
    //         var entity = $scope.business;
    //         businessService.checkUnique(entity,propname,propval,function(data){
    //             if(data=="false"){
    //                 reject();
    //             }else{
    //                 resolve();
    //             }
    //         });
    //     });
    // };

    /**
     * 分页功能
     */
    $scope.changePage = function (){
        businessService.getDataByPage($scope.currentPage-1,function (data){
            $scope.businesses = data.result;
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
    $scope.edit = function (business,index){
        /*  判断id进行回显 */
        if(business.id != "" && business.id != null){
            $scope.modalTitle="编辑业务类型";
            $scope.business = angular.copy(business);
        } else {
            $scope.modalTitle="添加业务类型";
            $scope.business = {dataStatus: 1};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'businessForm.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance,$q){
                //如果rateTypes是空的，
				$scope.business.sourcerates = [];
                /*if (!$scope.business.rateTypes) {
                    $scope.business.rateTypes = [];
                } else {
                    angular.forEach($scope.sourcerates, function(item){
                        var i = 0, len = $scope.business.rateTypes.length;
                        var sourceRateType;
                        for(; i < len; i++){
                            if ($scope.business.rateTypes[i].sourceRateId == item.id) {
                                sourceRateType = rates[i];
                                break;
                            }
                        }
                        if (sourceRateType) {
                            $scope.business.sourcerates.push(item);

                            var bizRateTypes = [];
                            angular.forEach(item.rateTypes, function(rateType){
                                bizRateTypes.push($filter('mergeRateTypes')(sourceRateType.rateTypeList, rateType));
                            });
                            $scope.business.rateTypes[i].rateTypeList = bizRateTypes;
                        }
                    })
                }*/
                $scope.business.rates = {};
                if ($scope.business.rateTypes) {
                    //转换为Map
                    angular.forEach($scope.business.rateTypes, function(item){
                        $scope.business.rates[item.sourceRateId] = item.rateTypeList;
                    });

                    angular.forEach($scope.sourcerates, function(item){
                        if ($scope.business.rates[item.id]) {
                            $scope.business.sourcerates.push(item);

                            var bizRateTypes = [];
                            angular.forEach(item.rateTypes, function(rateType){
                                bizRateTypes.push($filter('mergeRateTypes')($scope.business.rates[item.id], rateType));
                            });
                            $scope.business.rates[item.id] = bizRateTypes;
                        }
                    })
                }

                $scope.addRate = function (sourcerateId) {
                    if ($scope.business.rates[sourcerateId]) {
                        $scope.business.rates[sourcerateId].push({
                                id: $scope.rateTypes.length+1,
                                months: 0,
                                ratio: 0.0,
                                isNew: true
                            });
                    }
                };

                $scope.selectSourceRate = function(sourcerate, index) {
                    if (sourcerate) {
                        if (!$scope.business.rates[sourcerate.id])
                            $scope.business.rates[sourcerate.id] = sourcerate.rateTypes
                    }
                };

                $scope.removeRate = function (sourcerateId, index) {
                    $scope.business.rates[sourcerateId].splice(index,1);
                };
                
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 保存业务信息 */
                $scope.save = function (){
                    var saveRateTypes = [];
                    angular.forEach($scope.business.sourcerates, function(item){
                        saveRateTypes.push({sourceRateId: item.id, rateTypeList: $scope.business.rates[item.id]});
                    })
                    $scope.business.rateTypes = saveRateTypes;
                    var business = $scope.business;
                    businessService.save(business,function (data){
                        if(!$scope.business.id){
                            $scope.businesses.push(data)
                        }else{
                            $scope.businesses[index] = data;
                        }
                        $scope.businesse = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }
            }
        });
    };

    /* 删除 */
    $scope.delete = function (business,index) {
        var msg = "确定要作废此数据  ?";
        if (business.dataStatus == 9) {
            msg = "确定要删除此数据  ?";
        }
        if (confirm(msg)) {
            var id = business.id;
            businessService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (business,index) {
        if(confirm("确定要恢复该条数据?")){
            business.dataStatus = 1;
            businessService.save(business,function (data) {
                if(business.dataStatus != 1){
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
    $scope.checkUnique = function(proname,provalue){
        return $q(function (resolve, reject) {
            var entity = $scope.business;
            businessService.checkUnique(entity,proname,provalue,function (data) {
                if (data=="false"){
                    reject();
                }else{
                    resolve();
                }
            })
        })
    }
    
});