/**
 * Find employee info by loginUserId
 * Created by zqw on 2017/11/15.
 */
angular.module('app')
    .directive('loginuserEmployeeEmpinfo', function($localStorage,orginfoService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {id: '='},
            template: '<span>{{username}}{{cell}}</span>',
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    scope.$watch('id',function (value) {
                        orginfoService.getEmployeeByLoginUserId(value, function (data) {
                            if (data) {
                                scope.username = data.username;
                                scope.cell = "【"+data.cell+"】";
                            } else {
                                if (value == '589ae19d30293a9363c20439'){
                                    scope.username = "admin";
                                    scope.cell = "【admin】";
                                }else {
                                    scope.username = "N/A";
                                    scope.cell = "【N/A】";
                                }
                            }
                        })
                    })
                }    
            }
        };
    });