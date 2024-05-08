angular.module('app')
  .directive('flowGetpresenttaskgroup', function(workflowService,customerTransactionService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {billid: '=', billtypecode: '=',transaction:"="},
        template: '<span>{{presentTaskGroup ||""}}</span>',
        link: function(scope, element, attrs) {

            if (scope.billid && scope.billid != '') {
                if (scope.transaction && scope.transaction.status){
                    if ((scope.transaction.status != 11 && scope.transaction.status != 16 )|| scope.billtypecode == 'A012' || scope.billtypecode == 'A013'){
                        scope.presentTaskGroup = "";
                        workflowService.getTaskCurrentGroup(scope.billtypecode + '.'+ scope.billid, function(data) {
                            scope.presentTaskGroup = data;
                        });
                    }
                } else {
                    workflowService.getTaskCurrentGroup(scope.billtypecode + '.'+ scope.billid, function(data) {
                        scope.presentTaskGroup = data;
                    });
                }
                // workflowService.getTaskCurrentGroup(scope.billtypecode + '.'+ scope.billid, function(data) {
                //     scope.presentTaskGroup = data;
                //     //  交易状态为取消或取消中，取消业务单据除外
                //     if (scope.transactionid && scope.transactionid != ''){
                //         customerTransactionService.getOne(scope.transactionid,function (transaction) {
                //             if (transaction && (transaction.status == 11 || transaction.status == 16) && scope.billtypecode != 'A012'){
                //                 scope.presentTaskGroup = "";
                //             }
                //         })
                //     }
                // });

            }
        }
    };
  });