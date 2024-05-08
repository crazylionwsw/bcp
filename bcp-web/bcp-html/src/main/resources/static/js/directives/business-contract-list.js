angular.module('app')
    .directive('businessContractList', function(documentService) {
        return {
            restrict: 'E',
            scope: {ccids:'='},
            templateUrl: 'tpl/view/document/cclist.html',
            link: function($scope, element, attrs) {
                if ($scope.ccids && $scope.ccids.length > 0) {
                    $scope.customerContracts = [];
                    documentService.getByIds($scope.ccids, function (data) {
                        $scope.customerContracts = data;
                    });
                }
            },
            controller: function($scope, $element, $filter, $cookieStore, $location, documentService, $state) {
                var loginUserId = $cookieStore.get("userID");
                $scope.view = function(cc) {
                    $scope.save(cc, function(fileId){
                        window.open('/pdf/viewer.html?file=/json/file/download/' + fileId, "_blank");
                    })
                };

                $scope.download = function(cc){
                    $scope.save(cc, function(fileId){
                        window.open( "/json/file/download/" + fileId,"_blank");
                    })
                };

                $scope.save = function(cc, callback) {
                    var customerContract = cc;
                    if (!customerContract) {
                        customerContract = {contract: contract, dataStatus: 1, customerId: $scope.customerid, downloadRecords: [{loginUserId: loginUserId}]};
                    } else {
                        if (!customerContract.downloadRecords)
                            customerContract.downloadRecords = [];
                        customerContract.downloadRecords.push({loginUserId: loginUserId});
                    }

                    //保存客户合同
                    documentService.saveCustomerContract(false,customerContract, function(data) {
                        customerContract = data;

                        var index = $scope.customerContracts.indexOf(customerContract);
                        $scope.customerContracts[index] = customerContract;
                        if (customerContract.fileId) {
                            callback(customerContract.fileId);
                        }
                    });
                }
            }


        };
    });