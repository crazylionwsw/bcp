angular.module('app')
  .directive('transactionCardealerInfo', function(customerTransactionService,cardealerService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {transactionid: '='},
        template: '<span>{{name || ""}}</span>',
        link: function (scope, element, attrs) {
            if (scope.transactionid && scope.transactionid != '') {
                customerTransactionService.getOne(scope.transactionid, function (data) {
                    if (data && data.carDealerId) {
                        cardealerService.getOne(data.carDealerId,function (cardealer) {
                            if (cardealer && cardealer.name){
                                scope.name = cardealer.name;
                            } else {
                                scope.name = 'N/A';
                            }
                        })
                    }
                })
            }
        }
    };
  });