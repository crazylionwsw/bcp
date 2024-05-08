/**
 *      获取交易的报单行名称
 */
angular.module('app')
  .directive('transactionCashsourceInfo', function(cashsourceService,customerTransactionService,localStorageService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {transaction: '='},
        template:  '<span>{{name || ""}}</span>',
        link: function(scope, element, attrs) {
            if (scope.transaction && scope.transaction != undefined) {
                /*    根据客户交易ID  查询该笔交易的报单行信息      */

                if (!localStorageService.get('CASHSOURCES')) {
                    localStorageService.set('CASHSOURCES', []);
                }
                scope.$watch('transaction',function (transaction) {
                    cashsourceService.getCashSource(transaction.cashSourceId, function (data) {
                        if (data != undefined){
                            scope.name = data.shortName;
                        }
                    })
                })
            }
        }
    };
  });