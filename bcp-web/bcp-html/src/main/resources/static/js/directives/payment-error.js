angular.module('app')
  .directive('paymentError', function(maintenanceService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{comment|strList}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                maintenanceService.getPaymentById(scope.id, function (data) {
                    if (data) {
                        scope.comment = data.comment;
                    }
                })
            }    
        }
    };
  });