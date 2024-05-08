angular.module('app')
    .directive('sysresourceList', function() {
        return {
            restrict: 'E',
            scope: {ids:'='},
            templateUrl: 'tpl/view/sysresource/list.html',
            controller: function($scope, $element, $filter, sysresourceService){
                if ($scope.ids && $scope.ids != '') {
                    $scope.sysreRources = [];

                    $scope.$watch('ids', function (value) {
                        if ($scope.ids !== undefined) {
                            sysresourceService.getByIds($scope.ids, function (data) {
                                $scope.sysreRources = data;
                            });
                        }
                    });
                }
            }
        };
    });