/**
 *          根据  二手车  车架号(vin) 获取 二手车现场评估价
 */
angular.module('app')
  .directive('vinPrice', function(carTypeService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {vin: '='},
        template: '<span>{{price ||0 |currency:"￥"}}</span>',
        link: function(scope, element, attrs) {
            if (scope.vin && scope.vin != '') {
                scope.$watch('vin',function (value) {
                    carTypeService.getOneByVin(value, function(data){
                        if (data) {
                            scope.price = data.price;
                        }
                    })    
                })
            }
        }
    };
  });