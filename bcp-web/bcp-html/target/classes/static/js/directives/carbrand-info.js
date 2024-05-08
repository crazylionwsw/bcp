angular.module('app')
  .directive('carbrandInfo', function(carTypeService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{name}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                carTypeService.getCarBrand(scope.id, function(data){
                    if (data) {
                        scope.name = data.name;
                    }
                })
            }
        }
    };
  });