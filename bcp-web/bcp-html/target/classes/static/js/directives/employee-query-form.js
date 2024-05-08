angular.module('app')
  .directive('employeeQueryForm', function() {
    return {
        restrict: 'E',
        scope: {employee: '=', query: '&', clear: '&'}, //调用传入父级scope中的query方法
        templateUrl: 'tpl/view/orginfo/query.html',
        controller: function($scope, $element, $cookieStore, $localStorage){
            $scope.modalTitle = "查询";

            $scope.cancel = function() {
                $scope.$parent.$close();
            };

            $scope.doQuery = function() {
                $scope.query({employee: $scope.employee});
                $scope.$parent.$close();
            };

            /*清楚查询内容*/
            $scope.clearQuery = function() {
                $scope.clear();
                //$scope.$parent.$close();
            };
        }
    };
  });