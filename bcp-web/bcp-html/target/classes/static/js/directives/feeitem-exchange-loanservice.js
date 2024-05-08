/**
 * Created by admin on 2018/3/7.
 */
angular.module('app')
    .directive('feeitemExchangeLoanservice', function(customerService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {id: '='},
            template: "<span>{{loan.loanServiceFee| currency:'￥'}}</span>",
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    customerService.getCustomerLoanByCustomerTransactionId(scope.id,function (data) {
                        if(data){
                            scope.loan = data;
                        }
                    })
                }
            }
        };
    });