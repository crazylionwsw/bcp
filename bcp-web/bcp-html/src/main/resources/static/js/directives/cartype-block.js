angular.module('app')
  .directive('cartypeBlock', function(carTypeService) {
    return {
        restrict: 'AE',
        scope: {id: '='},
        templateUrl: 'tpl/view/cartype/block.html',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '')
                carTypeService.getCarType(scope.id, function (data) {
                    scope.cartype = data;
                })
        }
    };
  });