/**
 * Created by admin on 2018/3/7.
 */
angular.module('app')
    .directive('exchangeAmount', function(customerService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {id: '='},
            template: "<span>{{loan.creditAmount| currency:'￥'}}【{{loan.rateType.months}}期-{{comment}}】</span>",
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    customerService.getCustomerLoanByCustomerTransactionId(scope.id,function (data) {
                        if(data){
                            scope.loan = data;
                            if(scope.loan.compensatoryAmount > 0){
                                scope.comment ="贴息";
                            }else{
                                scope.comment ="商贷";
                            }
                        }
                    })
                }
            }
        };
    });