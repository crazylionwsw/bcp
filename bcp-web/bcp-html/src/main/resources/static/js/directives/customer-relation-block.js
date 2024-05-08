angular.module('app')
  .directive('customerRelationBlock', function() {
    return {
        restrict: 'E',
        scope: {id: '=', proname: '='},
        templateUrl: 'tpl/view/customer/relation.html',
        controller: function($scope, $element, $filter, customerService){

            $scope.customer = {};

            $scope.$watch('id', function (value) {
                if ($scope.id) {
                    customerService.getOne($scope.id, function (data) {
                        $scope.customer = data;
                    });
                }
            });

            $scope.updateCustomerCells = function() {
                var cells = [];
                if ($scope.customer.cells.length > 0) {
                    $scope.customer.cells.forEach(function(item){
                        cells.push(item.text);
                    })
                }
                $scope.customer.cells = cells;
                customerService.saveCustomer($scope.customer,function (data) {
                    $scope.customer = data;
                })
            };

            }
        
    };
  });