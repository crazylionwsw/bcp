angular.module('app')
  .directive('customerStatus', function(customerService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{status}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                customerService.getOne(scope.id, function (data) {
                    if (data) {
                        scope.status = data.status;
                    }
                })
            }    
        }
    };
  });