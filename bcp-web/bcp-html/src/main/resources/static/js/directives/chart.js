
angular.module('app')
    .directive('chart', function($rootScope,workflowService) {
    return {
        restrict: 'E',
        scope: {object:'=', ids:'=', save: '&'},
        templateUrl: 'tpl/view/cartype/list.html',
        link: function(scope, element, attr) {
            scope.$watch(attr.canAudit, function canAuditWatchAction(bill) {
                //
            });
        }
    };
});