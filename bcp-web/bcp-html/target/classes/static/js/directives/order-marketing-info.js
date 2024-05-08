angular.module('app')
  .directive('orderMarketingInfo', function() {
    return {
        restrict: 'E',
        scope: {transactionid: '='},
        templateUrl: 'tpl/view/poundagesettlement/block.html',
        controller: function($scope, $element, $filter, poundagesettlementService,cashsourceService) {
            
            $scope.$watch('transactionid',function (transactionId) {
                if (transactionId && transactionId != ''){
                    poundagesettlementService.getMarketingCodeByCustomerTransactionId(transactionId,function (poundageSettlement) {
                        $scope.poundageSettlement = poundageSettlement;
                        if (poundageSettlement){
                            cashsourceService.getCashSource(poundageSettlement.settlementCashSourceId, function (data) {
                                if (data != undefined){
                                    $scope.poundageSettlement.newMarketingCode = data.marketingCode;
                                }
                            })
                        }
                    })    
                }
                
            })
        }
    };
  });