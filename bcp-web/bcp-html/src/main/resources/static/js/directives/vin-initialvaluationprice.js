/**
 *          根据  二手车  车架号(vin) 获取 二手车初步评估价
 */
angular.module('app')
  .directive('vinInitialvaluationprice', function(carTypeService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {vin: '='},
        template: '<span>{{initialValuationPrice ||0 |currency:"￥"}}</span>',
        link: function(scope, element, attrs) {
            if (scope.vin && scope.vin != '') {
                carTypeService.getOneByVin(scope.vin, function(data){
                    if (data) {
                        scope.initialValuationPrice = data.initialValuationPrice;
                    }
                })
            }
        }
    };
  });