/**
 * Created by user on 2017/9/4.
 */
angular.module('app')
    .directive('msgsubscribeInfo', function(businesseventtypeService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {businesstype: '='},
            template: '<span>{{comment}}</span>',
            link: function(scope, element, attrs ) {
                if (scope.businesstype && scope.businesstype != '') {
                    scope.$watch('businesstype',function (value) {
                        businesseventtypeService.getOneByBusinessType(value, function (data) {
                            if (data)
                                scope.comment = data.comment;
                            else
                                scope.comment = '';
                        })
                    });
                }
            }
        };
    });