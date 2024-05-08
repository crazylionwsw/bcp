angular.module('app')
  .directive('customerInfo', function(customerService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        templateUrl: 'tpl/view/customer/name.html',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                customerService.getOne(scope.id, function (data) {
                    if (data) {
                        scope.name = data.name;
                        scope.identifyNo = data.identifyNo;
                    }
                })
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