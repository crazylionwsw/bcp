/**
 * Created by admin on 2018/3/6.
 */
angular.module('app')
    .directive('cardealerExchangeBlock', function(cardealerService, customerTransactionService) {
        return {
            restrict: 'AE',
            scope: {id: '=',transactionid: '=',bid:'='},
            templateUrl: 'tpl/view/cardealer/exchangeblock.html',
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