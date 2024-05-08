/**
 * Created by hecaifeng on 2017/2/27.
 */
angular.module('app')
    .directive('loginuserInfo', function($localStorage,loginUserService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {id: '='},
            template: '<span>{{name}}</span>',
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    scope.$watch('id',function (value) {
                        loginUserService.getOne(value, function (data) {
                            if (data) {
                                scope.name = data.username;
                            }
                        })
                    })
                }    
            }
        };
    });