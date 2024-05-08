/**
 * Created by admin on 2018/3/10.
 */
angular.module('app')
    .directive('customerOrderexchangeCar', function(customerService,carTypeService,sysparamService,customerTransactionService) {
        return {
            restrict: 'AE',
            scope: {transactionid:'=',id: '=',billtypecode:'=',businesstypecode:'='},
            templateUrl: 'tpl/view/customer/orderexchangecar.html',
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    if (scope.transactionid && scope.transactionid != ''){
                        customerTransactionService.getOne(scope.transactionid,function (customerTransaction) {
                            if (customerTransaction) {
                                scope.customerTransaction = customerTransaction;
                            }
                        });
                    }
                    customerService.getCar(scope.id, function(data){
                        scope.car = data;
                        scope.car.proname = 'carTypeId';
                        //      获取车辆颜色 系统参配项
                        sysparamService.getListByCode("CAR_COLORS",function (carColors) {
                            if (carColors){
                                angular.forEach(carColors,function (item) {
                                    if (data.carColor == item.code){
                                        scope.car.carColorName = item.name;
                                    }
                                })
                            }
                        });
                        if (data.carTypeId){
                            carTypeService.getCarType(data.carTypeId, function (da) {
                                if(da){
                                    scope.cartype = da;
                                }
                            })
                        }
                        if (data.vin){
                            carTypeService.getOneByVin(data.vin,function (data) {
                                scope.carvaluation = data;
                            })
                        }

                    })
                }
            }
        };
    });