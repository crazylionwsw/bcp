/**
 * Created by user on 2017/12/8.
 */
angular.module('app')
    .directive('orgList', function() {
        return {
            restrict: 'E',
            scope: {ids:'='},
            templateUrl: 'tpl/view/orginfo/list.html',
            controller: function($scope, $element, $filter, orginfoService){
                if ($scope.ids && $scope.ids != '') {
                    $scope.orgs = [];

                    $scope.$watch('ids', function (value) {
                        if ($scope.ids !== undefined) {
                            orginfoService.getByIds($scope.ids, function (data) {
                                $scope.orgs = data;
                            });
                        }
                    });
                }
            }
        };
    });
