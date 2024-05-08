/**
 * 客户缴费单
 * Created by user on 2017/8/8.
 */
angular.module('app')
    .directive('customerFee', function(customerService) {
        return {
            restrict: 'E',
            scope: {transactionid:'='},
            templateUrl: 'tpl/view/customer/fee.html',
            controller: function($scope, $element, $filter, feeService){
                if ($scope.transactionid && $scope.transactionid != '') {
                    customerService.getCustomerFeesByCustomerTransactionId($scope.transactionid,function (fee) {
                        $scope.fee = fee;
                    })
                }
            }
        };
    });
