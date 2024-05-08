angular.module('app')
  .directive('customerId', function(customerService) {
    return {
        restrict: 'AE',
        scope: {id: '='},
        template: '<td>{{customer.name}}</td><td>{{customer.identifyNo}}</td>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '')
                customerService.getOne(scope.id, function (data) {
                    scope.customer = data;
                })
        }
    };
  });