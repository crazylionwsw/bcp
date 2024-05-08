angular.module('app')
    .directive('declarationHistoryList', function() {
        return {
            restrict: 'E',
            scope: {historyrecords:'='},
            templateUrl: 'tpl/view/declaration/history.html',
            controller: function($scope, $element, $filter,tasksService,msgService){
                $scope.$watch('historyrecords',function (values) {
                    $scope.historyRecords = values;
                    angular.forEach(values,function (item, index) {
                        tasksService.getMsgRecord(item.resultId,function (data) {
                            if (data){
                                $scope.historyRecords[index].emailSuccess = true;
                                if (data.messageLogId && data.messageLogId.length >0 && data.status == 2){
                                    msgService.getMessageLogByIds(data.messageLogId,function (messageLogs) {
                                        if (messageLogs){
                                            //  失败的次数
                                            var falseNum = 0;
                                            angular.forEach(messageLogs,function (messageLog, mindex) {
                                                if (messageLog.result == 1){
                                                    $scope.historyRecords[index].emailSuccess = true;
                                                } else {
                                                    falseNum ++;
                                                }
                                            });

                                            if (falseNum == messageLogs.length){
                                                $scope.historyRecords[index].emailSuccess = false;
                                            }
                                        }
                                    })
                                } else if (data.messageLogId && data.messageLogId.length == 0 && data.status == 2){
                                    //  发送失败
                                    $scope.historyRecords[index].emailSuccess = false;
                                    $scope.historyRecords[index].reason = '报单行未配置协作用户或未订阅！';
                                } else if ( data.status == 3){
                                    //  发送失败，邮件服务器异常
                                    $scope.historyRecords[index].emailSuccess = false;
                                    $scope.historyRecords[index].reason = '邮件服务器异常！';
                                }
                            }
                        })
                    })
                })
            }
        };
    });