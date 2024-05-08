angular.module('app')
    .directive('bankemployeeroleList', function() {
        return {
            restrict: 'E',
            scope: {ids:'='},
            template: '<span>{{roles|strList}}</span>',
            controller: function($scope, $element, $filter, cashsourceService){
                if ($scope.ids && $scope.ids != '') {
                    $scope.roles = [];
                    $scope.$watch('ids', function (value) {
                        if ($scope.ids !== undefined) {
                            cashsourceService.getBankEmployeeRoleNameById($scope.ids, function (data) {
                                $scope.roles = data;
                            });
                        }
                    });
                }
            }
        };
    });