angular.module('app')
  .directive('customerCompany', function(customerService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        templateUrl: 'tpl/view/customer/company.html',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {

                scope.customer = {};
                customerService.getOne(scope.id, function (data) {
                    scope.customerJob = data.customerJob;
                    scope.customer = data;
                })

            }
            
        },
        controller:function ($scope) {
            $scope.updateCustomerSalary = function() {
                customerService.updateCustomerSalary($scope.customer,function (data) {
                    $scope.customerJob = data.customerJob;
                })
            };
        }
    };
  });