angular.module('app')
  .directive('pledgeCustomerInfo', function(customerService,cardemandService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {transactionid: '='},
        templateUrl: 'tpl/view/customer/name.html',
        link: function(scope, element, attrs) {
            if (scope.transactionid && scope.transactionid != '') {
                cardemandService.getOneByCustomerTransactionId(scope.transactionid,function (cardemand) {
                   if (cardemand && cardemand.pledgeCustomerId){
                       customerService.getOne(cardemand.pledgeCustomerId, function (data) {
                           if (data) {
                               scope.name = data.name;
                               scope.identifyNo = data.identifyNo;
                           }
                       })
                   } else if (cardemand && cardemand.creditMasterId) {
                       customerService.getOne(cardemand.creditMasterId, function (data) {
                           if (data) {
                               scope.name = data.name;
                               scope.identifyNo = data.identifyNo;
                           }
                       })
                   } else {
                       scope.name = 'N/A';
                       scope.identifyNo = 'N/A';
                   }
                });
            }    
        },
        controller:function ($scope,$q,$filter,$state) {
            $scope.goCustomerTransaction = function () {
                if ($scope.name && $scope.name != ''){
                    $state.go('app.business.customertransaction',{cname:$scope.name});
                }
                    
            }
        }
    };
  });