angular.module('app')
  .directive('carmodelInfo', function(carTypeService) {
    return {
        restrict: 'AE',
        scope: {id: '='},
        template: '<span>{{name}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                carTypeService.getCarModel(scope.id, function(data){
                    if (data) {
                        scope.name = data.name;
                    }
                })
            }
        }
    };
  });