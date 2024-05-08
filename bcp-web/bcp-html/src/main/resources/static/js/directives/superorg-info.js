angular.module('app')
  .directive('superorgInfo', function(orginfoService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{name}}</span>',
        link: function(scope, element, attrs) {
            scope.name = 'N/A';
            scope.$watch('id', function (value) {
                if (scope.id != undefined) {
                    orginfoService.getSuperOrgByOrgId(scope.id, function (data) {
                        if (data) {
                            scope.name = data.name;
                        }
                    })
                }
            });

        }
    };
  });