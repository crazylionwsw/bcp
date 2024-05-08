angular.module('app')
  .directive('userInfo', function(loginUserService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{username}}</span>',
        link: function(scope, element, attrs ) {
            if (scope.id && scope.id != '') {
                scope.$watch('id',function (value) {
                    loginUserService.getOne(value, function (data) {
                        if (data)
                            scope.username = data.username;
                        else
                            scope.username = '';
                    })
                });
            }    
        }
    };
  });