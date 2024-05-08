angular.module('app')
  .directive('feeitemInfo', function(feeService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{name}}(元)</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                scope.$watch('id',function (value) {
                    feeService.getOne(value, function(data){
                        if (data) {
                            scope.name = data.name;
                        }
                    })
                })
            }
        }
    };
  });