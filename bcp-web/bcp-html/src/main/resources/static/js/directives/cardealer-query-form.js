/**
 * Created by zxp on 2017/9/7.
 */
angular.module('app')
    .directive('cardealerQueryForm',function () {
        return {
            restrict:'E',
            scope:{orgs:'=',employees:'=',cashsources:'=',cardealerquery:'=',query:'&',clear:'&'},
            templateUrl:'tpl/view/cardealer/query.html',
            controller:function ($scope, $element, $cookieStore, $localStorage) {
                $scope.modalTitle="查询";
                
                $scope.cashSources = $scope.cashsources;
                
                //关闭
                $scope.close=function () {
                    $scope.$parent.$close();
                }

                //查询
                $scope.queryCardealers = function() {
                    $scope.query({cardealerquery: $scope.cardealerquery});

                    $scope.$parent.$close();
                }
                //清除
                $scope.clearQuery=function () {
                    $scope.clear();
                }
                
            }
        }
    });