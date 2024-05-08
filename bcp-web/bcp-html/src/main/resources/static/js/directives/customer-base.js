/**
 * Created by user on 2017/10/11.
 */
angular.module('app')
    .directive('customerBase', function() {
        return {
            restrict: 'E',
            scope: {id: '='},
            templateUrl: 'tpl/view/customer/base.html',
            controller: function($scope, $element, $filter, customerService,sysparamService){

                $scope.customer = {};

                $scope.$watch('id', function (value) {
                    if (value) {
                        customerService.getOne(value, function (data) {
                            $scope.customer = data;
                        });
                    }
                });
            }

        };
    });