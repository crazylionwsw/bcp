angular.module('app')
  .directive('paymentInfo', function($localStorage,bankcardService,$stateParams) {
    return {
        restrict: 'AE',
        scope: {id: '='},
        template: '<span>{{status | CardStatusFilter }}</span>',
        link: function(scope, element, attrs){
            if(scope.id && scope.id!='') {
                bankcardService.getOneByCustomerTransactionId(scope.id, function (data) {
                    if (data) {
                        scope.status = data.status;
                    }
                })
            }
        },
    };
  });