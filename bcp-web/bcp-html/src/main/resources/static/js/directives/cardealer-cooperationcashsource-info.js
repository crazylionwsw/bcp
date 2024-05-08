/**
 *      获取经销商的渠道合作行名称
 */
angular.module('app')
  .directive('cardealerCooperationcashsourceInfo', function(cardealerService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template:  '<span>{{name}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                /*   根据经销商信息 获取  渠道合作行信息     */
                cardealerService.getCooperationCashSource(scope.id, function (data) {
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