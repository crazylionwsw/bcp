angular.module('app')
  .directive('cartypeSelector', function(carTypeService) {
    return {
        restrict: 'E',
        scope: {carBrandId: '=', carModelId: '=', carTypeId: '='},
        templateUrl: 'tpl/view/cartype/selector.html',
        link: function(scope, element, attrs) {
            //首先获取所有品牌

        },
        controller: function($scope, $element, $modal, $filter, carTypeService){
            $scope.carBrands = [];
            $scope.carModels = [];
            $scope.carTypes = [];
            $scope.choosenCarTypes = [];

            /**
             * 显示车型选择器的模态窗口
             */
            $scope.showSelector = function() {
                carTypeService.lookupCarBrands(function(data){
                    $scope.carBrands = data;
                });

                //如果CarTypeId不为空，根据CarTypeId获取CarBrandId，并查询该CarBrand下所有的车系
                if ($scope.carModelId) {
                    carTypeService.loopupCarModels($scope.carBrandId, function(data){
                        $scope.carModels = data;
                    });
                }
                if ($scope.carTypeId) {
                    carTypeService.loopupCarTypes($scope.carModelId, function(data){
                        $scope.carTypes = data;
                    });
                }

                var modalInstance = $modal.open({
                    scope:$scope,
                    animation:true,
                    size: 'lg',
                    templateUrl:'selector.html',
                    controller:function ($scope,$modalInstance,$q){

                        $scope.ok = function() {
                            alert('test');
                            modalInstance.close();
                        }

                        /*  关闭模态框 */
                        $scope.close = function ($modalInstance){
                            modalInstance.close();
                        }
                    }
                });
            };
            
            //选择品牌
            $scope.selectCarBrand = function(carBrand) {
                if($scope.carBrand){
                    $scope.carBrand.selected = false;
                    $scope.carModels = [];
                    $scope.carTypes = [];
                }

                carBrand.selected = true;
                $scope.carBrand = carBrand;

                carTypeService.lookupCarModels(carBrand.id,function (data) {
                    $scope.carModels = data;
                })
            };
            
            //选择车系
            $scope.selectCarModel = function(carModel) {
                if($scope.carModel) {
                    $scope.carModel.selected = false;
                    $scope.carTypes = [];
                }

                carModel.selected = true;
                $scope.carModel = carModel;

                carTypeService.lookupCarTypes(carModel.id,function (data) {
                    $scope.carTypes = data;
                })
            };
            
            //选择车型
            $scope.selectCarType = function(carType) {
                carType.selected = !carType.selected;

                if (!carType.selected) {
                    // remove from the choosen list
                    if ($scope.choosenCarTypes.indexOf(carType)) {
                        $scope.choosenCarTypes.slice(0);
                    }

                } else {
                    // add into the choosen list
                    $scope.choosenCarTypes.push(carType);
                }
                $scope.carType = carType;
            }
        }
  }});