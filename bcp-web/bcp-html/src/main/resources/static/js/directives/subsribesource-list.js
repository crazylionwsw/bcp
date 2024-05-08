
angular.module('app')
    .directive('subsribesourceList', function() {
        return {
            restrict: 'E',
            scope: {ids:'='},
            templateUrl: 'tpl/view/subsribesource/list.html',
            controller: function($scope, $element, $filter, customerService,orginfoService,cardealerService,cashsourceService){
                if ($scope.ids && $scope.ids != '') {
                    $scope.subMans = [];

                    $scope.$watch('ids', function (value) {
                        if ($scope.ids !== undefined) {
                            // customerService,orginfoService,cardealerService,cashsourceService
                            // subsribesourceService.getByIds($scope.ids, function (data) {
                            //     $scope.subsribeSources = data;
                            // });

                            angular.forEach($scope.ids,function (item) {
                                var id = item;
                                customerService.getOne(id,function (cusdata) {
                                    if(cusdata){
                                        var customers = [];
                                        var customer = {username:cusdata.name};
                                        customers = customer;
                                        $scope.subMans.push(customers);
                                    }
                                });
                                orginfoService.getEmployeeById(id,function (orgdata) {
                                    if(orgdata){
                                        $scope.subMans.push(orgdata);
                                    }
                                });
                                cardealerService.getDealerEmployeeById(id,function (cardealerdata) {
                                    if(cardealerdata){
                                        $scope.subMans.push(cardealerdata);
                                    }
                                });
                                cashsourceService.getEmployeesByCashSourceById(id,function (casudata) {
                                    if(casudata){
                                        $scope.subMans.push(casudata);
                                    }
                                })
                            })



                        }
                    });
                }
            }
        };
    });