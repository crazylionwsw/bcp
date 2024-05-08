angular.module('app')
  .directive('cardealerName', function(cardealerService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{name}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                /*    根据客户交易ID  查询该笔交易的经销商信息      */
                cardealerService.getOne(scope.id, function(data){
                    if (data) {
                        scope.name = data.name;
                    }
                })
            }
        }
    };
  });