angular.module('app')
    .directive('cartypeList', function() {
        return {
            restrict: 'E',
            scope: {object:'=', ids:'=', save: '&'},
            templateUrl: 'tpl/view/cartype/list.html',
            controller: function($scope, $element, $filter, carTypeService){

                $scope.selectedCarTypes = [];
                
                $scope.$on('idsChanged', function (event, ids) {
                    $scope.ids = ids;
                    if (ids !== undefined) {
                        $scope.getCarTypes(ids);
                    }
                });

                $scope.getCarTypes = function(ids) {
                    carTypeService.getByIds($scope.ids,function(data){
                        $scope.selectedCarTypes = data;
                    });
                }
                
                $scope.$watch('ids', function(value) {
                    if ($scope.ids !== undefined) {
                        $scope.getCarTypes($scope.ids);
                    }
                });


                $scope.delete = function(index){
                    $scope.selectedCarTypes.splice(index, 1);
                    $scope.ids.splice(index, 1);
                    $scope.save({ids: $scope.ids});
                };
            }
        };
    });