/**
 * Created by user on 2017/8/8.
 */

angular.module('app')
    .directive('feeitemList', function() {
        return {
            restrict: 'E',
            scope: {feeitemlist:'='},
            templateUrl: 'tpl/view/fee/list.html',
            controller: function($scope, $element, $filter, feeService){
                $scope.$watch('feeitemlist',function (value) {
                    if (value && value != '') {
                        $scope.feeitems = value;
                    }
                })
            }
        };
    });
