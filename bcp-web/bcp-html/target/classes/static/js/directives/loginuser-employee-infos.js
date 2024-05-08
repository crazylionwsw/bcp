/**
 * Created by user on 2018/1/2.
 */
/**
 * Find employee info by loginUserId
 * Created by zqw on 2017/11/15.
 */
angular.module('app')
    .directive('loginuserEmployeeInfos', function($localStorage,orginfoService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {id: '='},
            template: '<span>{{namecell}}</span>',
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    scope.$watch('id',function (value) {
                        orginfoService.getEmployeeByLoginUserId(value, function (data) {
                            if (data) {
                                scope.namecell = data.username+ '【' + data.cell+'】';
                            } else {
                                scope.namecell = "N/A";
                            }
                        })
                    })
                }
            }
        };
    });