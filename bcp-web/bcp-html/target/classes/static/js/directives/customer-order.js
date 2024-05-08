/**
 * Created by user on 2017/10/25.
 */
angular.module('app')
    .directive('customerOrder', function() {
        return {
            restrict: 'E',
            scope: {id: '='},
            templateUrl: 'tpl/view/customer/orderblock.html',
            controller: function($scope, $element, $filter, customerService,sysparamService){

                $scope.customer = {};
                $scope.socialsecurityreadlines = [];

                sysparamService.getListByCode('SOCIAL_SECURITY_DEADLINE',function (da) {
                    if (da){
                        $scope.socialsecurityreadlines = da;
                    }
                });

                $scope.$watch('id', function (value) {
                    if (value) {
                        customerService.getOne(value, function (data) {
                            $scope.customer = data;
                            if (data){
                                angular.forEach($scope.socialsecurityreadlines,function (item) {
                                    if (item.code == data.socialInsuranceDate){
                                        $scope.customer.socialInsuranceDateName = item.name;
                                    }
                                })
                            }
                        });
                    }
                });

                $scope.updateCustomerCells = function() {
                    var cells = [];
                    if ($scope.customer.cells.length > 0) {
                        $scope.customer.cells.forEach(function(item){
                            cells.push(item.text);
                        })
                    }
                    $scope.customer.cells = cells;
                    customerService.saveCustomer($scope.customer,function (data) {
                        $scope.customer = data;
                    })
                };
            }

        };
    });