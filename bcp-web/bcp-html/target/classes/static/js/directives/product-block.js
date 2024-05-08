angular.module('app')
  .directive('productBlock', function(creditproductService) {
    return {
        restrict: 'E',
        scope: {id: '='},
        templateUrl: 'tpl/view/creditproduct/block.html',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                creditproductService.getOne(scope.id, function (data) {
                    scope.creditproduct = data;
                })
            }   
        }
    };
  });