angular.module('app')
  .directive('approveStatus', function(customerTransactionService,$filter) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {transaction: '=', status:'=',billtypecode:'='},
        template: '<span>{{statusName}}</span>',
        link: function($scope, element, attrs) {
            if ($scope.transaction && $scope.transaction != '') {
                /*    根据客户交易ID  查询该笔交易的经销商信息      */
                $scope.$watch('status',function (value) {
                    if ($scope.transaction && $scope.transaction.status){
                        if (($scope.transaction.status == 11 || $scope.transaction.status == 16) && $scope.billtypecode != 'A013'){
                            $scope.statusName = $filter('transactionStatusFilter')($scope.transaction.status);
                            element.addClass('text-danger');
                        } else {
                            //  资质审查、客户签约、车辆上牌、转移过户、资料补全、业务调整（A030）、缴费单(A025)、解押管理（A031）
                            if ($scope.billtypecode == 'A001' || $scope.billtypecode == 'A002' || $scope.billtypecode == 'A005' || $scope.billtypecode == 'A023' || $scope.billtypecode == 'A013' || $scope.billtypecode == 'A030' || $scope.billtypecode == 'A025'){
                                $scope.statusName = $filter('approveStatusFilter')($scope.status);
                            } else if ($scope.billtypecode == 'A004'){//预约垫资
                                $scope.statusName = $filter('AppointPaymentApproveStatusFilter')($scope.status);
                            } else if ($scope.billtypecode == 'A026'){//预约刷卡
                                $scope.statusName = $filter('WorkStatusFilter')($scope.status);
                            } else if ($scope.billtypecode == 'A011'){//卡业务处理
                                $scope.statusName = $filter('CardStatusFilter')($scope.status);
                            } else if ($scope.billtypecode == 'A015'){//银行报批
                                $scope.statusName = $filter('DeclarationStatusFilter')($scope.status);
                            } else if ($scope.billtypecode == 'A008'){//车辆抵押
                                $scope.statusName = $filter('DmvpStatusFilter')($scope.status);
                            } else if ($scope.billtypecode == 'A020'){//渠道还款
                                $scope.statusName = $filter('DealerRepaymentStatusFilter')($scope.status);
                            } else if ($scope.billtypecode == 'A019'){//渠道刷卡
                                $scope.statusName = $filter('SwipingCardStatusFilter')($scope.status);
                            }else if($scope.billtypecode == 'A031'){
                                $scope.statusName = $filter('DecompressFilter')($scope.status);
                            }
                        }
                    }
                });
            }
        }
    };
  });