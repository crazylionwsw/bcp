angular.module('app')
  .directive('cartypePrice', function(customerTransactionService) {
    return {
        restrict: 'AE',
        scope: {id: '='},
        template: '<td>{{cartype.fullName}}</td><td>{{cartype.price}}</td>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '')
                customerTransactionService.getCar(scope.id, function (data) {
                    scope.cartype = data;
                })
        }
    };
  });