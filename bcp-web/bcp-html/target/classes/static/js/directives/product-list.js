angular.module('app')
    .directive('productList', function() {
        return {
            restrict: 'E',
            scope: {object:'=', ids:'=', save: '&'},
            templateUrl: 'tpl/view/creditproduct/list.html',
            controller: function($scope, $element, $filter, creditproductService){

                $scope.creditProducts = [];

                $scope.$watch('ids', function(value) {
                    if ($scope.ids !== undefined) {
                        creditproductService.getByIds($scope.ids,function(data){
                            $scope.creditProducts = data;
                        })
                    }
                });

                $scope.delete = function(index){
                    $scope.creditProducts.splice(index, 1);
                    $scope.ids.splice(index, 1);
                    $scope.save({ids: $scope.ids});
                };
            }
        };
    });