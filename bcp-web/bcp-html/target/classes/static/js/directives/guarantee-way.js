angular.module('app')
  .directive('guaranteeWay', function(guaranteewayService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template:  '<span>{{name}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                guaranteewayService.getOne(scope.id, function (data) {
                    if (data != undefined){
                        scope.name = data.name;
                    }
                })
            }
        }
    };
  });