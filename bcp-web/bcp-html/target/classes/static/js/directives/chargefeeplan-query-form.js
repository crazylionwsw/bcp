angular.module('app')
    .directive('chargefeeplanQueryForm', function() {
        return {
            restrict: 'E',
            scope: {search: '=', query: '&', clear: '&'},
            templateUrl: 'tpl/view/chargefeeplan/query.html',
            controller: function($scope, $element, $cookieStore, $localStorage){
                $scope.modalTitle = "查询";

                //关闭
                $scope.close = function() {
                    $scope.$parent.$close();
                }
                //查找
                $scope.doQuery = function() {
                    $scope.query({searchFilter: $scope.search});
                    $scope.$parent.$close();
                }
               //清除
                $scope.clearQuery = function() {
                    $scope.clear();

                };
            }
        };
    });