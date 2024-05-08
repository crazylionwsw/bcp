/**
 * Created by user on 2017/8/8.
 */

angular.module('app')
    .directive('businessInfo', function() {
        return {
            restrict: 'E',
            scope: {codes:'='},
            templateUrl: 'tpl/view/cardealer/list.html',
            controller: function($scope, $element, $filter, businessService){
                if ($scope.codes && $scope.codes != '') {
                    $scope.businessCodes = [];

                    $scope.$watch('codes', function (value) {
                        if ($scope.codes !== undefined) {
                            businessService.getByIds($scope.codes, function (data) {
                                $scope.businessCodes = data;
                            });
                        }
                    });
                }
            }
        };
    });
