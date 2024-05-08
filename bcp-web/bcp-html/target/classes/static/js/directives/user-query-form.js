angular.module('app')
  .directive('userQueryForm', function() {
    return {
        restrict: 'E',
        scope: {loginuser: '=', query: '&', clear: '&',sysroles:'='}, //调用传入父级scope中的query方法
        templateUrl: 'tpl/view/loginuser/query.html',
        controller: function($scope, $element, $cookieStore, $localStorage){
            $scope.modalTitle = "查询";
            $scope.sysRoles = $scope.sysroles;

            $scope.close = function() {
                $scope.$parent.$close();
            };

            $scope.doQuery = function() {
                $scope.query({loginuser: $scope.loginuser});
                $scope.$parent.$close();
            };

            /*清除查询内容*/
            $scope.clearQuery = function() {
                $scope.clear();
                //$scope.$parent.$close();
            };
        }
    };
  });