/**
 * @buttonshow  查询条件框中，控制查询信息显示的参数(客户信息、部门信息、借款信息、时间信息)(1：显示，0：隐藏)(eg. 1111--都显示 0000--都不显示)
 */
angular.module('app')
  .directive('queryForm', function(orginfoService,cardealerService,employeeService,sysparamService) {
    return {
        restrict: 'E',
        scope: {search: '=', query: '&', clear: '&',buttonshow:'='}, //调用传入父级scope中的query方法
        templateUrl: 'tpl/view/query/form.html',
        controller: function($scope, $element, $cookieStore, $localStorage,$filter){
            
            $scope.modalTitle = "查询";
            if (!$scope.search.creditMonths)  $scope.search.creditMonths = 0;

            if($scope.buttonshow) {
                $scope.customershow = 0;
                $scope.orginfoshow = 0;
                $scope.loanshow = 0;
                $scope.timeshow = 0;
                var str= $scope.buttonshow.toString();
                if(str.length>0)
                    $scope.customershow =str.substr(0,1);
                if(str.length>1)
                    $scope.orginfoshow = str.substr(1, 1);
                if(str.length>2)
                    $scope.loanshow =  str.substr(2,1);
                if(str.length>3)
                    $scope.timeshow =  str.substr(3,1);
            }else{
                $scope.customershow = 1;
                $scope.orginfoshow = 1;
                $scope.loanshow = 1;
                $scope.timeshow = 1;
            }
            
            //部门
            $scope.orginfos = [];
            $scope.businessManagers = [];
            $scope.cardealers = [];

            $scope.init = function (){
                sysparamService.getListByCode('BUSINESS_ORGS',function (data) {
                    $scope.orginfos = data;
                });
                //  分期经理
                employeeService.getStageManager(function (data) {
                    $scope.businessManagers = data;
                });
                //  渠道
                cardealerService.lookup(function (cardealerdata) {
                    $scope.cardealers = cardealerdata;
                });
            };

            $scope.init();

            $scope.close = function() {
                $scope.$parent.$close();
            };

            $scope.doQuery = function() {
                $scope.query({search: $scope.search});
                $scope.$parent.$close();
            };

            /*清除查询内容*/
            $scope.clearQuery = function() {
                $scope.clear();
            };
        }
    };
  });