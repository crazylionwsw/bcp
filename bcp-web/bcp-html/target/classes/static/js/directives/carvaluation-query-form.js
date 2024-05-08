/**
 * Created by user on 2017/11/27.
 */
angular.module('app')
    .directive('carvaluationQueryForm', function() {
        return {
            restrict: 'E',
            scope: {carvaluation: '=', query: '&', clear: '&'}, //调用传入父级scope中的query方法
            templateUrl: 'tpl/view/carvaluation/query.html',
            controller: function($scope, $element, $cookieStore, $localStorage){
                $scope.modalTitle = "查询";

                $scope.cancel = function() {
                    $scope.$parent.$close();
                };

                $scope.doQuery = function() {
                    $scope.query({carvaluation: $scope.carvaluation});
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