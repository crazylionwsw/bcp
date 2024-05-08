/**
 * Created by user on 2017/9/14.
 */
angular.module('app')
    .directive('emailobjectTemplateobjectInfo', function(msgTemplatelService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {id: '='},
            template: '<span>{{fileName}}</span>',
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    scope.$watch('id',function (value) {
                        msgTemplatelService.getOne(value,function (data) {
                            if (data){
                                scope.fileName = data.name;
                            }
                        })
                    })
                }
            }
        };
    });
