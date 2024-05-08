/**
 * Created by user on 2017/10/18.
 */
angular.module('app')
    .directive('customerCarvaluationExchange', function(customerService) {
        return {
            restrict: 'AE',
            scope: {id: '=',businesstypecode:'='},
            templateUrl: 'tpl/view/customer/valuation.html',
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    customerService.getBusinessExchangeCarByCustomerTransactionId(scope.id, function(data){
                        scope.car = data;
                        if (data && data.vin){
                            customerService.getCarValuationByVin(data.vin, function (da) {
                                if (da){
                                    scope.carValuation = da;
                                    scope.carValuation.type = "精真估";
                                }
                            })
                        }
                    })
                }
            }
        };
    });

