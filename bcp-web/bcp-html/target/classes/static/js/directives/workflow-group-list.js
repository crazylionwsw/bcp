angular.module('app')
    .directive('workflowGroupList', function(workflowService) {
        return {
            restrict: 'AE',
            scope: {activitiuserid: '='},
            templateUrl: 'tpl/view/workflow/groupList.html',
            link: function(scope, element, attrs) {
                if(scope.activitiuserid && scope.activitiuserid != ''){
                    scope.userGroups = [];
                    workflowService.getUserGroupsByActivitiUserId(scope.activitiuserid, function(data) {
                        scope.userGroups = data;
                    })
                }
            }
        };
    });