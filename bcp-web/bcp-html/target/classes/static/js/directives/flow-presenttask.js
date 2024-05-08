angular.module('app')
  .directive('flowPresenttask', function(workflowService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {businesstypecode: '=', billid: '='},
        template: '<span>{{presentTaskGroup}}</span>',
        link: function(scope, element, attrs) {
            if (scope.billid) {
                workflowService.getTaskCurrentGroup(scope.businesstypecode + '.'+ scope.billid, function(data) {
                    if (data != undefined){
                        scope.presentTaskGroup = data.name;
                    }
                })
            }
        }
    };
  });