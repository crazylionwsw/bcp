/**
 * Find employee info by loginUserId
 * Created by zqw on 2017/11/15.
 */
angular.module('app')
    .directive('loginuserEmployeeInfo', function($localStorage,orginfoService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {id: '='},
            template: '<span>{{name}}</span>',
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    scope.$watch('id',function (value) {
                        orginfoService.getNameByLoginUserId(value, function (data) {
                            if (data && data.d) {
                                scope.name = data.d;
                            } else {
                                scope.name = "N/A";
                            }
                        })
                    })
                }    
            }
        };
    });