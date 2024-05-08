angular.module('app')
  .directive('customerCell', function(customerService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{cells|strList}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                customerService.getOne(scope.id, function (data) {
                    if (data) {
                        scope.cells = data.cells;
                    }
                })
            }    
        }
    };
  });