angular.module('app')
  .directive('questionQueryForm', function() {
    return {
        restrict: 'E',
        scope: {question: '=', query: '&', clear: '&'}, //调用传入父级scope中的query方法
        templateUrl: 'tpl/view/questioncategory/query.html',
        controller: function($scope, $element, $cookieStore, $localStorage){
            $scope.modalTitle = "查询";

            //取消
            $scope.cancel = function() {
                $scope.$parent.$close();
            };

            //查询
            $scope.doQuery = function() {
                $scope.query({question: $scope.question});
                $scope.$parent.$close();
            };

            //清除
            $scope.clearQuery = function() {
                $scope.clear();
            };
        }
    };
  });