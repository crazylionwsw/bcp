angular.module('app')
  .directive('ordernewcarError', function(maintenanceService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{comment|strList}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                maintenanceService.getOrderById(scope.id, function (data) {
                    if (data) {
                        scope.comment = data.comment;
                    }
                })
            }    
        }
    };
  });