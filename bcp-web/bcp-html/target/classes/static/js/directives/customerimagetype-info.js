angular.module('app')
    .directive('customerimagetypeInfo', function (customerimageService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {code: '='},
            template: '<span>{{name}}</span>',
            link: function (scope, element, attrs) {
                if (scope.code && scope.code != '') {
                    customerimageService.getOneByCode(scope.code, function (data) {
                        scope.name = data.name;
                    })
                }
            }
        };
    });