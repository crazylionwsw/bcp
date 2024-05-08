angular.module('app')
  .directive('reAuditButton', function() {
    return {
        restrict: 'E',
        scope: {bill:'='},
        template: '<button class="btn btn-primary" type="button" ng-click="doReAudit()">重 审</button>',
        link: function(scope, element, attrs) {
            scope.$watch('bill', function(value) {
                if (value) {
                    if(value.approveStatus == 2 || value.approveStatus == 8){
                        element.removeClass('hidden');
                    }else {
                        element.addClass('hidden');
                    }
                }
            });
        },
        controller: function($scope, $element, $cookieStore, workflowService, $localStorage, $rootScope){

            /**
             * 重审
             */
            $scope.doReAudit = function() {
                if (window.confirm("你确定要重新审核吗？")) {
                    workflowService.reset($scope.bill, function(data){
                        //  审批状态为待审核
                        $scope.bill.approveStatus = 0;
                        $element.addClass('hidden');
                        $rootScope.$broadcast('UpdateSignInfo',$scope.bill.id)
                    });
                }
            };
        }
    };
  });