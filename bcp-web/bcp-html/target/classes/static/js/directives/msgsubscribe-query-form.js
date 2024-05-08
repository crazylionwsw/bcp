/**
 * Created by user on 2017/12/6.
 */
angular.module('app')
    .directive('msgsubscribeQueryForm', function() {
        return {
            restrict: 'E',
            scope: {search: '=', query: '&', clear: '&',sendtypes:'=',desctypes:'='}, //调用传入父级scope中的query方法
            templateUrl: 'tpl/view/msgsubscribe/query.html',
            controller: function($scope, $element, $cookieStore, $localStorage){
                $scope.modalTitle = "查询";

                $scope.sendTypes = $scope.sendtypes;
                $scope.descTypes = $scope.desctypes;

                $scope.cancel = function() {
                    $scope.$parent.$close();
                };

                $scope.doQuery = function() {
                    $scope.query({search: $scope.search});
                    $scope.$parent.$close();
                };

                /*清楚查询内容*/
                $scope.clearQuery = function() {
                    $scope.clear();
                };

            }
        };
    });
