angular.module('app')
  .directive('customerRelation', function() {
    return {
        restrict: 'E',
        scope: {relations: '='},
        template: "<span>{{relas ||''}}</span>",
        controller: function($scope, $element, $filter, customerService){

            $scope.relas = "";

            $scope.$watch('relations', function (value) {
                if (value) {
                    angular.forEach(value,function (item, index) {
                        customerService.getOne(item.customerId, function (data) {
                            var relationName = $filter('RejectCustomerRelation')(item.relation);
                            $scope.relas += data.name+"【"+relationName+"】  ";
                        });
                    });
                }
            });
        }
    };
  });