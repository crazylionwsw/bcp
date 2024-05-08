angular.module('app')
  .directive('transactionCartypeInfo', function(orderService,customerService,carTypeService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {transactionid: '='},
        template: '<span>{{name || ""}}</span>',
        link: function (scope, element, attrs) {
            if (scope.transactionid && scope.transactionid != '') {
                orderService.getOneByCustomerTransactionId(scope.transactionid, function (order) {
                    if (order && order.customerCarId) {
                        customerService.getCar(order.customerCarId,function (customercar) {
                            if (customercar && customercar.carTypeId){
                                carTypeService.getCarType(customercar.carTypeId,function (cartype) {
                                    if (cartype && cartype.fullName){
                                        scope.name = cartype.fullName;
                                    } else {
                                        scope.name = 'N/A';
                                    }
                                })
                            }
                        })
                    }
                })
            }
        }
    };
  });