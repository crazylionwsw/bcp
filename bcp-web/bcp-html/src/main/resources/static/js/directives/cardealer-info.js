angular.module('app')
  .directive('cardealerInfo', function(cardealerService,customerTransactionService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{name}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                /*    根据客户交易ID  查询该笔交易的经销商信息 */
                customerTransactionService.getOne(scope.id,function (customerTransaction) {
                    if (customerTransaction){
                        cardealerService.getOne(customerTransaction.carDealerId, function(data){
                            if (data) {
                                scope.name = data.name;
                            }
                        })
                    }
                })
            }
        }
    };
  });