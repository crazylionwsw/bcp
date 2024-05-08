angular.module('app')
    .directive('syncList', function() {
        return {
            restrict: 'E',
            scope: {ids:'='},
            templateUrl: 'tpl/view/sync/list.html',
            controller: function($scope, $element, $filter, syncService){
                if ($scope.ids && $scope.ids != '') {
                    $scope.syncs = [];

                    $scope.$watch('ids', function (value) {
                        if ($scope.ids !== undefined) {
                            syncService.getByIds($scope.ids, function (data) {
                                $scope.syncs = data;
                            });
                        }
                    });
                }    
            }
        };
    });