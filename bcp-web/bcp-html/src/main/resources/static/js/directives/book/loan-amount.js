angular.module('app')
  .directive('loanAmount', function(customerTransactionService) {
    return {
        restrict: 'AE',
        scope: {tid: '='},
        templateUrl: 'tpl/view/customer/loan.html',
        link: function(scope, element, attrs) {
            if (scope.tid && scope.tid != '') {
                customerTransactionService.getLoan(scope.tid, function (loan) {
                    scope.loan = loan;
                })
            }
        }
    };
  });