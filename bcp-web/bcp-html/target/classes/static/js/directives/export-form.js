/**
 * Created by user on 2018/1/19.
 */
/**
 * @buttonshow  查询条件框中，控制查询信息显示的参数(客户信息、部门信息、借款信息、时间信息)(1：显示，0：隐藏)(eg. 1111--都显示 0000--都不显示)
 */
angular.module('app')
    .directive('exportForm', function(orginfoService,cardealerService,employeeService,sysparamService) {
        return {
            restrict: 'E',
            scope: {exportSearch: '=', query: '&', clear: '&'}, //调用传入父级scope中的query方法
            templateUrl: 'tpl/view/export/form.html',
            controller: function($scope, $element, $cookieStore, $localStorage,$filter){

                $scope.modalTitle = "查询";

                $scope.closeExport = function() {
                    $scope.$parent.$close();
                };

                $scope.doQueryExport = function() {
                    $scope.query({exportSearch: $scope.exportSearch});
                    $scope.$parent.$close();
                };

                /*清除查询内容*/
                $scope.clearQueryExport = function() {
                    $scope.clear();
                };
            }
        };
    });
