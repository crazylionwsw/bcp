angular.module('app')
  .directive('swipingcardInfo', function($localStorage,bankcardService,$stateParams) {
    return {
        restrict: 'AE',
        scope: {id: '='},
        template: "<span>{{swipingMoney | currency:'￥'}}【{{status | CardStatusFilter }}】</span>",
        link: function(scope, element, attrs){
            if(scope.id && scope.id!='') {
                bankcardService.getOneByCustomerTransactionId(scope.id, function (data) {
                    if (data) {
                        scope.status = data.status;
                        scope.swipingMoney = data.swipingMoney;
                    }
                })
            }
        },
    };
  });