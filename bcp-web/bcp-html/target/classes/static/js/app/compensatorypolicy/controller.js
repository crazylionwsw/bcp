/**
 * Created by zxp on 2017/3/5.
 */
'use strict';
app.controller('compensatorypolicyController', function ($scope, compensatorypolicyService, carTypeService, compensatorypolicyinfoService, $modal, $state, $stateParams, $q, $filter) {

    $scope.tableTitle = "贴息管理";
    $scope.compensatorypolicys = [];//返回列表数据
    $scope.compensatorypolicy = {};
    $scope.carbrands = [];
    $scope.months = [];
    $scope.monthsFlag = false;
    $scope.downPaymentRatioFlag = false;
    $scope.compensatoryRatioFlag = false;
    $scope.formulas = [];
    $scope.currentPage = 1;

    //列表
    $scope.init = function () {

        $scope.months = [
            {"code": 12, "name": 12},
            {"code": 18, "name": 18},
            {"code": 24, "name": 24},
            {"code": 30, "name": 30},
            {"code": 36, "name": 36},
            {"code": 48, "name": 48},
            {"code": 60, "name": 60}
        ];

        //车辆品牌
        carTypeService.lookupCarBrands(function (data) {
            $scope.carbrands = data;
        });

        compensatorypolicyinfoService.getAll(function (data) {
            $scope.formulas = data;
        });

        $scope.changePage(1);
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex) {
        if (pageIndex) {
            $scope.currentPage = pageIndex;
        }
        compensatorypolicyService.getDataByPage($scope.currentPage - 1, function (data) {
            $scope.compensatorypolicys = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };

    //开始日期大于结束日期
    $scope.checkEndDate = function (value) {
        if (value) {
            if (moment(value).isAfter($scope.compensatorypolicy.startDate)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    };

    $scope.checkMinDownParmentRatio = function (value) {
        if ($scope.compensatorypolicy.maxDownParmentRatio < value) {
            return true;
        } else {
            return false;
        }
    };

    $scope.edit = function (compensatorypolicy) {
        /*  判断id进行回显 */
        if (compensatorypolicy.id != "" && compensatorypolicy.id != null) {
            $scope.modalTitle = "编辑贴息政策";
            $scope.compensatorypolicy = angular.copy(compensatorypolicy);
        } else {
            $scope.modalTitle = "添加贴息政策";
            $scope.compensatorypolicy = {dataStatus: 1};
        }
        var modalInstance = $modal.open({
            scope: $scope,
            size: 'lg',
            animation: true,
            templateUrl: 'compensatoryPolicyModal.html',
            backdrop: 'static',
            controller: function ($scope, $modalInstance) {

                $scope.fchat = {};

                $scope.fchat.replies = [];

                $scope.selectFlg = false;

                $scope.selectFormula = function (item) {
                    $scope.selectFlg = true;
                    $scope.fchat.replies = [];
                    var i = 0;
                    for (var key in item.templateMap) {
                        $scope.fchat.replies.push({key: i, value: {key: key, value: item.templateMap[key]}});
                        i = i + 1;
                    }
                };

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance) {
                    $scope.fchat = {};

                    $scope.fchat.replies = [];

                    $scope.selectFlg = false;

                    modalInstance.close();
                };

                //      选中品牌查询出该品牌对应的所有的车系
                $scope.selectCarBrand = function (carBrand) {
                    carTypeService.getCarModels(carBrand.id, function (data) {
                        $scope.carmodels = data;
                    })
                };

                $scope.selectCarModel = function (carModel) {
                    carTypeService.lookupCarTypes(carModel.id, function (data) {
                        $scope.cartypes = data;
                    })
                };

                /* 保存促销信息 */
                $scope.save = function () {
                    $scope.compensatorypolicy.templateMap = {};
                    for (var i = 0; i < $scope.fchat.replies.length; i++) {
                        $scope.compensatorypolicy.templateMap[$scope.fchat.replies[i].value.key] = $scope.fchat.replies[i].value.valueR;
                    }
                    compensatorypolicyService.save($scope.compensatorypolicy, function (data) {
                        if (!$scope.compensatorypolicy.id) {
                            $scope.compensatorypolicys.push(data);
                        } else {
                            var index = $scope.compensatorypolicys.indexOf(compensatorypolicy);
                            $scope.compensatorypolicys[index] = data;
                        }
                        $scope.compensatorypolicy = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                };

                for (var i = 0; i < $scope.formulas.length; i++) {
                    if ($scope.formulas[i].id == $scope.compensatorypolicy.templateId) {
                        for (var key in $scope.formulas[i].templateMap) {
                            $scope.fchat.replies.push({
                                key: i,
                                value: {
                                    key: key,
                                    value: $scope.formulas[i].templateMap[key],
                                    valueR: $scope.compensatorypolicy.templateMap[key]
                                }
                            });
                        }
                        $scope.selectFlg = true;
                    }
                }
            }
        });
    };

    /* 删除 */
    $scope.delete = function (compensatorypolicy, index) {
        var msg = "确定要作废数据  ?";
        if (compensatorypolicy.dataStatus == 9) {
            msg = "确定要删除数据  ?";
        }
        if (confirm(msg)) {
            compensatorypolicyService.delete(compensatorypolicy, function (data) {
                $scope.compensatorypolicys.splice(index, 1);
                $scope.compensatorypolicy = {};
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (compensatorypolicy, index) {
        if (confirm("确定要恢复该条数据?")) {
            compensatorypolicy.dataStatus = 1;
            compensatorypolicyService.save(compensatorypolicy, function (data) {
                if (compensatorypolicy.dataStatus != 1) {
                    alert("恢复数据出错!")
                }
            })
        }
    };

    /**
     * 执行初始化
     */
    $scope.init();

    $scope.checkUnique = function (propname, propval) {
        return $q(function (resolve, reject) {
            var entity = $scope.compensatorypolicy;
            compensatorypolicyService.checkUnique(entity, propname, propval, function (data) {
                if (data == "false") {
                    reject();
                } else {
                    resolve();
                }
            });
        });
    };

});