angular.module('app')
  .directive('bankfeeInfo', function(customerService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: "<span>{{loan.bankFeeAmount| currency:'￥'}}【{{loan.chargePaymentWay | chargePaymentWayFilter}}】</span>",
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                customerService.getLoan(scope.id,function (data) {
                    if(data){
                        scope.loan = data;
                    }
                })
            }
        }
    };
  });