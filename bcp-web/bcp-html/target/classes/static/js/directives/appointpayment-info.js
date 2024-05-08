angular.module('app')
  .directive('appointpaymentInfo', function(appointpaymentService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: "<span>{{appointPayAmount| currency:'￥'}}</span>",
        link: function(scope, element, attrs) {
            scope.appointPayAmount = 0.0;
            if (scope.id && scope.id != '') {
                appointpaymentService.getOneByCustomerTransactionId(scope.id,function (data) {
                    if(data){
                        scope.appointpayment = data;
                        scope.appointPayAmount = data.appointPayAmount;
                        // scope.changeTotal({amount:scope.appointPayAmount});
                    }else {
                        scope.appointPayAmount = 0.0;
                    }
                })
            }
        }
    };
  });