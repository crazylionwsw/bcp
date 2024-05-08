/**
 *      获取经销商的报单名称
 */
angular.module('app')
  .directive('cardealerCashsourceInfo', function(cardealerService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template:  '<span>{{name}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                cardealerService.getCashSource(scope.id, function (data) {
                    if (data == undefined){
                        scope.name = "";
                    } else {
                        scope.name = data.shortName;
                    }
                })
            }
        }
    };
  });