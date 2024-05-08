angular.module('app')
    .directive('sysroleInfo', function(sysroleService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {id: '='},
            template: '<span>{{name}}</span>',
            link: function(scope, element, attrs ) {
                if (scope.id && scope.id != '') {
                    sysroleService.getOne(scope.id, function (data) {
                        if (data){
                            scope.name = data.name;
                        }
                    })
                }
            }
        };
    });