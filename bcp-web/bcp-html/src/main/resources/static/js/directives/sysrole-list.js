angular.module('app')
    .directive('sysroleList', function() {
        return {
            restrict: 'E',
            scope: {ids:'='},
            templateUrl: 'tpl/view/sysrole/list.html',
            controller: function($scope, $element, $filter, sysroleService){
                if ($scope.ids && $scope.ids != '') {
                    $scope.sysRoles = [];

                    $scope.$watch('ids', function (value) {
                        if ($scope.ids !== undefined) {
                            sysroleService.getByIds($scope.ids, function (data) {
                                $scope.sysRoles = data;
                            });
                        }
                    });
                }    
            }
        };
    });