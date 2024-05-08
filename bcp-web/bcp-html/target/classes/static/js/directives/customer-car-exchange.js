/**
 * Created by admin on 2018/3/5.
 */
angular.module('app')
    .directive('customerCarExchange', function(customerService,carTypeService,sysparamService,customerTransactionService) {
        return {
            restrict: 'AE',
            scope: {transactionid:'=',id: '=',billtypecode:'=',businesstypecode:'=',bid:'='},
            templateUrl: 'tpl/view/customer/exchangecar.html',
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    if (scope.transactionid && scope.transactionid != ''){
                        customerTransactionService.getOne(scope.transactionid,function (customerTransaction) {
                            if (customerTransaction) {
                                scope.customerTransaction = customerTransaction;
                            }
                        });
                    }
                    customerService.getBusinessExchangeCar(scope.id, function(data){
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
