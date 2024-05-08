angular.module('app')
  .directive('transactionStage', function(customerTransactionService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '=', bizcode: '='},
        templateUrl: 'tpl/view/customertransaction/stage.html',
        link: function (scope, element, attrs) {
            if (scope.id && scope.id != '') {
                customerTransactionService.getStage(scope.id, function (data) {
                    if (data) {
                        scope.pastStages = [];
                        if (data.currentStage) {
                            for (var i = 0; i < data.definedStages.length; i++) {
                                var item = data.definedStages[i];
                                scope.pastStages.push(item);
                                if (item.code == data.currentStage) {
                                    scope.currentStage = item.name;
                                    break;
                                }
                            }
                        } else {
                            if (!scope.currentStage) scope.currentStage = '无';
                        }
                    }
                })
            }
        },
        controller: function ($scope, $element, $filter, $location, $state, $modal,toaster,$rootScope,enhancementService) {
            var states = $state.get();
            $scope.goBill = function (billTypeCode, id) {
                angular.forEach(states, function (state) {
                    if (state.billTypeCode) {
                        if (billTypeCode == state.billTypeCode) {
                            $state.go(state.name, {tid: id});
                            return false;
                        }
                    }
                })
            };
            //解押单
            $scope.goDecompressBill = function (billTypeCode, id) {
                customerTransactionService.getOne(id,function (transaction) {
                    if (transaction && transaction.status == 8){
                        angular.forEach(states, function (state) {
                            if (state.billTypeCode) {
                                if (billTypeCode == state.billTypeCode) {
                                    $state.go(state.name, {tid: id});
                                    return false;
                                }
                            }
                        })
                    }else {
                        alert("当笔交易未完成，不可创建解押单！");
                    }
                });
            };

            //逾期记录
            $scope.goOverdueBill = function (billTypeCode, id) {
                customerTransactionService.getOne(id,function (tran) {
                    if (tran){
                        angular.forEach(states, function (state) {
                            if (state.billTypeCode) {
                                if (billTypeCode == state.billTypeCode) {
                                    $state.go(state.name, {tid: id});
                                    return false;
                                }
                            }
                        })
                    }
                });
            };

            $scope.enhancement = function (id) {
                customerTransactionService.getOne(id,function (data) {
                    if (data){
                        $rootScope.$broadcast('Enhancement',data);
                    }
                });
            }
        }
    };
  });