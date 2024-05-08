angular.module('app')
    .directive('flowNodeproperty', function ($rootScope, workflowService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {billid: '=', billtypecode: '='},
            template: '<span>{{nodeName | buttonShowFilter}}</span>',
            link: function (scope, element, attrs) {

                var userid = $rootScope.user.userID;
                if (scope.billid) {
                    workflowService.getActivitiNodeProperty( scope.billtypecode + '.' + scope.billid, function (data) {
                        scope.nodeName = data;
                    })
                }
            }
        };
    });