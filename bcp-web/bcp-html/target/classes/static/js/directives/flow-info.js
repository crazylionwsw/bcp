angular.module('app')
  .directive('flowInfo', function($rootScope,workflowService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {billid: '=', billtypecode: '='},
        template: '<span>{{isTask}}</span>',
        link: function(scope, element, attrs) {

            var userid = $rootScope.user.userID;
            if (scope.billid) {
                workflowService.getMyTasks(userid, scope.billtypecode + '.'+ scope.billid, function(data) {

                    if(data && data != undefined ){
                        scope.usergroup = data.name;
                        scope.isTask = "是";
                    }else {
                        scope.isTask = "否";
                        scope.usergroup = "无";
                    }
                })
            }
        }
    };
  });