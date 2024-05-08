angular.module('app')
  .directive('cardealerBlock', function(cardealerService, customerTransactionService) {
    return {
        restrict: 'AE',
        scope: {id: '=',transactionid: '='},
        templateUrl: 'tpl/view/cardealer/block.html',
        link: function(scope, element, attrs) {
            if (scope.transactionid && scope.transactionid != ''){
                /*    根据客户交易ID  查询该笔交易的经销商信息      */
                customerTransactionService.getOne(scope.transactionid,function (customerTransaction) {
                    if (customerTransaction){
                        scope.customerTransaction = customerTransaction;
                    }
                });
            }
            if (scope.id && scope.id != '') {
                cardealerService.getOne(scope.id, function(data){
                    scope.cardealer = data;
                    scope.cardealer.proname = 'cardealerId';
                });
            }
        }
    };
  });