angular.module('app')
  .directive('loginuserQueryForm', function() {
    return {
        restrict: 'E',
        scope: {user: '=', query: '&', clear: '&'}, //调用传入父级scope中的query方法
        templateUrl: 'tpl/view/bind/query.html',
        controller: function($scope, $element, $cookieStore, $localStorage){
            $scope.modalTitle = "查询";

            $scope.close = function() {
                $scope.$parent.$close();
            };

            $scope.doQuery = function() {
                $scope.query({user: $scope.user});
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