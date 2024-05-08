/**
 *         当前交易状态
 */
angular.module('app')
  .directive('transactionStatus', function(customerTransactionService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{status | transactionStatusFilter }}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                /*    根据客户交易ID  查询该笔交易的经销商信息      */
                scope.$watch('id',function (value) {
                    customerTransactionService.getOne(value,function (customerTransaction) {
                        if (customerTransaction){
                            scope.status = customerTransaction.status;
                        }
                    })
                });
            }
        }
    };
  });