/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('salespolicyController', function ($scope,salespolicyService,provinceService,businessService,localStorageService,toaster,$q, $filter){
    $scope.title = '销售政策管理';
    $scope.tableTitle = "销售政策列表";
    $scope.modalTitle = "销售政策类型";

    $scope.salespolicys = [];//列表
    $scope.salespolicy = {};
    $scope.salespolicy.businessTypes = [];
    $scope.salespolicy.rates = {};

    //禁用
    $scope.disForm = false;

    //选中
    $scope.selectedSalespolicy = {};


    //地区列表
    $scope.proVinces = [];

    //经营业务
    $scope.businessTypes = [];

    $scope.saved = false;

    /**
     * 初始化加载列表
     */
    $scope.init = function () {
        //获取业务类型列表
        businessService.lookupWithRates(function (data) {
            $scope.businessTypes = data;
            //
            angular.forEach($scope.businessTypes, function(item, key){
                var rates = {};
                angular.forEach(item.rateTypes, function(item2){
                    angular.forEach(item2.rateTypeList,function (i,k) {
                        item2.rateTypeList[k].ratio = i.ratio*100;
                    });
                    rates[item2.sourceRateId] = item2.rateTypeList;
                });
                $scope.businessTypes[key].rates = rates;
            });

            salespolicyService.getAll(function (data) {
                $scope.salespolicys = $scope.updateRates(data,2);
            });
        });


        //地区列表
        provinceService.lookup(function (data) {
            $scope.proVinces = data;
        });

    };

    $scope.updateRates= function(salespolicys, percent){

        angular.forEach(salespolicys,function (salespolicy) {
            if(percent == 2){
                //TODO 将利率改为百分位数值

                salespolicy.rates = {};
                salespolicy.businessTypes = [];
                if (salespolicy.salesRates) {
                    //转换为Map
                    angular.forEach(salespolicy.salesRates, function(item){
                        if (!salespolicy.rates[item.businessTypeCode])
                            salespolicy.rates[item.businessTypeCode] = {};

                        angular.forEach(item.rateTypeList, function(item2){
                            angular.forEach(item2.rateTypeList,function (i) {
                                i.ratio100 = i.ratio*100;
                            });
                            salespolicy.rates[item.businessTypeCode][item2.sourceRateId] = item2.rateTypeList;   //利率集合
                        });
                    });
                    angular.forEach($scope.businessTypes, function(bt){
                        if (salespolicy.rates[bt.code]) { //
                            salespolicy.businessTypes.push(bt);

                            var salesRateTypes = {};
                            var sourcerateIds = _.keys(bt.rates);
                            var sourcerateTypes = _.values(bt.rates);

                            var salesBizRateTypes = salespolicy.rates[bt.code];

                            for(var i = 0; i < sourcerateIds.length; i++ ) {
                                if (salesBizRateTypes[sourcerateIds[i]]) {
                                    var mergedRateTypes = [];
                                    angular.forEach(sourcerateTypes[i], function(rateType) {
                                        mergedRateTypes.push($filter('mergeRateTypes')(salesBizRateTypes[sourcerateIds[i]], rateType));
                                    });
                                    salesRateTypes[sourcerateIds[i]] = mergedRateTypes;
                                } else {

                                    salesRateTypes[sourcerateIds[i]] = sourcerateTypes[i];
                                }
                            }
                            salespolicy.rates[bt.code] = salesRateTypes;
                        }
                    })
                } else {
                    salespolicy.businessTypes = [];
                    salespolicy.rates = {};
                }

            }else if (percent == -2){
                //TODO 将百分位改为小数位
                var saveRateTypes = [];
                angular.forEach(salespolicy.rates, function(item,i1){
                    var rateTypes = [];
                    var sourcerateIds = _.keys(item);
                    var sourcerateTypeLists = _.values(item);
                    angular.forEach(sourcerateIds,function(sourceRateId,sindex){
                        var rateTypeList = sourcerateTypeLists[sindex];
                        /*遍历reteTypeList取到利率radio，然后除以100存入数据库*/
                        angular.forEach(rateTypeList,function (rateType,rindex) {
                            rateTypeList[rindex].ratio = rateType.ratio100/100;
                        });
                        rateTypes.push({sourceRateId: sourceRateId, rateTypeList: rateTypeList});
                    });
                    saveRateTypes.push({businessTypeCode: i1, rateTypeList: rateTypes});
                });
                salespolicy.salesRates = saveRateTypes;
            } else {
                //  暂时作废--不适用
                var saveRateTypes = [];
                angular.forEach(salespolicy.salesRates, function(item,i1){
                    var salesRates = [];
                    angular.forEach(item.rateTypeList, function(item2,i2) {
                        /*遍历reteTypeList取到利率radio，然后除以100存入数据库*/
                        angular.forEach(item2.rateTypeList,function (i,index) {
                            item2.rateTypeList[index].ratio = i.ratio100/100;
                        });
                        salesRates.push({sourceRateId: item2.sourceRateId, rateTypeList: item2.rateTypeList});
                    });
                    saveRateTypes.push({businessTypeCode: item.businessTypeCode, rateTypeList: salesRates});
                });
                salespolicy.salesRates = saveRateTypes;
            }
        });
        return salespolicys;
    };

    /**
     * 执行初始化
     */
    $scope.init();


    /**
     * 选定
     */
    $scope.selectSalesPolicy = function(salespolicy){
        if($scope.selectedSalespolicy) $scope.selectedSalespolicy.selected = false;

        salespolicy.selected = true;
        $scope.selectedSalespolicy = salespolicy;
        $scope.salespolicy = angular.copy(salespolicy);
        /*判断数据状态*/
        if(salespolicy.dataStatus==9){
            $scope.disForm = true;
        }else{
            $scope.disForm = false;
        }

    };

    $scope.selectBusinessType = function(businessType, index) {
        if (businessType) {
            if (!$scope.salespolicy.rates[businessType.code]) {
                $scope.salespolicy.rates[businessType.code] = businessType.rates
            }
        }
    };

    
    $scope.selectProvinces = function (province) {
        provinceService.getChildren(province.id,function (data) {
            if(data){
                angular.forEach(data,function (item,index) {
                    if($scope.salespolicy.provinces.indexOf(item.id) < 0 ){
                        $scope.salespolicy.provinces.push(item.id);
                    }
                })
            }
        });
        if (province.parentId == 0 && $scope.salespolicy.provinces.indexOf(province.id) >= 0){
            $scope.salespolicy.provinces.splice($scope.salespolicy.provinces.indexOf(province.id));
        }
    };


    /**
     * 保存
     */
    $scope.saveform = function (){

        var salespolicy = $scope.updateRates([$scope.salespolicy],-2)[0];

        salespolicyService.saveSalesPolicy(salespolicy,function (data){
            $scope.saved = true;
            if(salespolicy.id == "" || salespolicy.id == null){
                $scope.salespolicys.push(data);
                toaster.pop('success', '销售政策', '添加成功！');
            }else {
                angular.forEach($scope.salespolicys, function(item, index){
                    if (item.id == $scope.salespolicy.id) {
                        $scope.salespolicys[index] = $scope.salespolicy;
                    }
                });
                $scope.selectSalesPolicy($scope.salespolicy);
                toaster.pop('success', '销售政策', '修改成功！');
            }
        });
    };


    /**
     * 删除
     */
    $scope.discard = function (){
        var salespolicy = $scope.selectedSalespolicy;
        if (salespolicy.dataStatus != 9 ){
            if (confirm("确定要作废数据  " + salespolicy.name + "  ?")) {
                salespolicyService.delete(salespolicy, function (data) {
                    toaster.pop('success','销售政策','作废成功');

                    var index = $scope.salespolicys.indexOf(salespolicy);
                    $scope.salespolicys[index] = data;
                    $scope.salespolicy = data;

                    $scope.disForm = true;
                    $scope.selectSalesPolicy($scope.salespolicy);
                });
            }
        }else if(salespolicy.dataStatus == 9){
            if (confirm("确定要删除数据  " + salespolicy.name + "  ?")) {
                salespolicyService.delete(salespolicy, function (data) {
                    toaster.pop('success','销售政策','删除成功');
                    //  如果 返回的数据状态为 作废
                    if (salespolicy.dataStatus == 9){
                        var index = $scope.salespolicys.indexOf(salespolicy);
                        $scope.salespolicys.splice(index, 1);
                        $scope.salespolicy = [];
                        $scope.add();
                    }
                })
            }
        }
    };

    //恢复数据
    $scope.renew = function () {
        var salespolicy = $scope.selectedSalespolicy;
        if(confirm("确定要恢复该条数据?")){
            salespolicy.dataStatus = 1;
            salespolicyService.saveSalesPolicy(salespolicy,function (data) {
                if(salespolicy.dataStatus != 1){
                    alert("恢复数据出错!")
                }
                $scope.selectSalesPolicy(salespolicy);
            })
        }
    };

    /**
     * 添加
     */
    $scope.add = function(){
        $scope.disForm = false;
        $scope.saved = false;
        //清除选定
        if($scope.salespolicy) $scope.salespolicy.selected = false;
        $scope.salespolicy = {datastatus:1};
        $scope.salespolicy.rates = {};
        $scope.salespolicy.businessTypes = [];
        $scope.userForm.$setPristine();
    };
    
    /**
     * 唯一性验证
     * @param propname
     * @param propval
     * @returns {*}
     */
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.salespolicy;
            salespolicyService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

});