/**
 * Created by user on 2017/8/9.
 */

angular.module('app')
    .directive('businessMan', function() {
        return {
            restrict: 'E',
            scope: {ids:'='},
            templateUrl: 'tpl/view/cardealer/businessman.html',
            controller: function($scope, $element, $filter, cardealerService){
                if ($scope.ids && $scope.ids != '') {
                    $scope.businessmans = [];
                    $scope.$watch('ids', function (value) {
                        if ($scope.ids !== undefined) {
                            cardealerService.getBussinessManByIds($scope.ids, function (data) {
                                $scope.businessmans = data;
                            });
                        }
                    });
                }
            }
        };
    });
