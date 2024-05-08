/**
 *      当前交易的经销商的渠道行名称
 */
angular.module('app')
  .directive('transactionCardealerCooperationcashsourceInfo', function(cardealerService,customerTransactionService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {transaction: '='},
        template:  '<span>{{name}}</span>',
        link: function(scope, element, attrs) {
            if (scope.transaction && scope.transaction != undefined) {
                scope.$watch('transaction',function (transaction) {
                    cardealerService.getCooperationCashSource(transaction.carDealerId, function (data) {
                        if (data == undefined){
                            scope.name = "";
                        } else {
                            scope.name = data.shortName;
                        }
                    })
                    /*    根据客户交易ID  查询该笔交易的经销商信息 再根据经销商信息 获取  渠道合作行信息     */
                    // customerTransactionService.getOne(value,function (customerTransaction) {
                    //     if (customerTransaction && customerTransaction.carDealerId){
                    //
                    //     }
                    // })
                })
            }
        }
    };
  });