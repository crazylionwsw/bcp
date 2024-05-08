
angular.module('app')
    .directive('canAudit', function($rootScope,workflowService) {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            scope.$watch(attr.canAudit, function canAuditWatchAction(bill) {
                if (bill) {
                    var userid = $rootScope.user.userID;
                    workflowService.getMyTasks(userid, bill.billTypeCode + '.'+ bill.id, function(data) {
                        if(data && data != undefined){
                            element.removeClass('hidden');
                        }else {
                            element.addClass('hidden');
                        }
                    })
                }
            });
        }
    };
});