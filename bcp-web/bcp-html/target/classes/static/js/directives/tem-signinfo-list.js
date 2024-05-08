angular.module('app')
    .directive('temSigninfoList', function(workflowService) {
        return {
            restrict: 'E',
            scope: {id: '=',billtypecode:'='},
            templateUrl: 'tpl/view/signinfo/temList2.html',
            link: function(scope, element, attrs) {
                
                scope.signInfo = null;
                
                if (scope.id && scope.id != '') {
                    workflowService.getByBillIdAndCode(scope.id, function(data){
                        if (data) {
                            scope.signInfos = data.signInfos;
                        }
                    });

                }
            },
            controller: function($scope, $element, $rootScope, workflowService, $modal) {
                
                $scope.signInfos = [];
                
                $scope.$on('UpdateTemSignInfo', function(e, billId){
                    workflowService.getByBillIdAndCode(billId,function(data){
                        if (data) {
                            $scope.signInfos = data.signInfos;
                        }
                    });
                });
                
                $scope.addComment = function () {
                    
                    var modalInstance = $modal.open({
                        scope: $scope,
                        animation: true,
                        templateUrl: "temComment.html",
                        controller: function ($scope, $modalInstance, toaster) {
                            
                            $scope.signInfo = {comment: null, result: 0, userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id,flag:0,fromSalesman:false};

                            /*  关闭模态框 */
                            $scope.cancel = function ($modalInstance){
                                modalInstance.close();
                            };

                            $scope.save =function () {
                                workflowService.saveSignInfo($scope.id, $scope.signInfo, function (data) {
                                    if (data && data.signInfos){
                                        $scope.signInfos = data.signInfos;
                                        $rootScope.$broadcast("UpdateTemSignInfo", $scope.id,2);
                                        modalInstance.close();
                                    } else {
                                        toaster.pop('error','签批信息','备注失败！');
                                    }
                                })
                            }
                        }
                    });
                };
            }
        };
    });