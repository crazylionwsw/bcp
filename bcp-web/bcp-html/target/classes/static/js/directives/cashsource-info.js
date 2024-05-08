angular.module('app')
  .directive('cashsourceInfo', function(cashsourceService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template:  '<span>{{name}}</span>',
        link: function(scope, element, attrs) {
            scope.$watch('id',function (value) {
                if (value && value != '' && value != undefined) {
                    cashsourceService.getCashSource(value, function (data) {
                        if (data != undefined){
                            scope.name = data.shortName;
                        }
                    })
                }
            });
            
        }
    };
  });