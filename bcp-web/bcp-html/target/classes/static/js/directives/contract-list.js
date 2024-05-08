angular.module('app')
    .directive('contractList', function(documentService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {customerid: '='},
            templateUrl: 'tpl/view/document/list.html',
            link: function($scope, element, attrs) {
                //第一步，获取合同列表，DataStatus = Save
                $scope.documents = [];
                documentService.getAll(function(data){
                    $scope.documents = data;

                    //第二步，根据CustomerId和Contract获取合同的下载记录
                    documentService.getContractsByCustomerId($scope.customerid, function(data2) {
                        var customerContracts = data2;
                        angular.forEach($scope.documents, function(v, k){
                            $scope.documents[k].downloads = 0;
                            angular.forEach(customerContracts, function(d, dk){
                                if (d.document.id == v.id) {
                                    $scope.documents[k].customerContract = d;
                                    $scope.documents[k].downloads = d.downloadRecords ? d.downloadRecords.length : 0;

                                    return;
                                }
                            });
                        });
                    })
                });
            },

            controller: function($scope, $element, $filter, $cookieStore, $location, documentService, $state) {
                var loginUserId = $cookieStore.get("userID");
                $scope.view = function(document) {
             /*       //保存CustomerContract
                    var customerContract = contract.customerContract;
                    if (!customerContract) {
                        customerContract = {contract: contract, dataStatus: 1, customerId: $scope.customerid, downloadRecords: [{loginUserId: loginUserId}]};
                    } else {
                        customerContract.downloadRecords.push({loginUserId: loginUserId});
                    }

                    //保存客户合同
                    contractService.saveCustomerContract(customerContract, function(data) {
                        contract.customerContract = data;
                        contract.downloads = contract.customerContract.downloadRecords.length;

                        if (contract.customerContract.fileId) {
                            //下载文件
                            //var url = $state.href("pdf.viewer",{fileid:contract.customerContract.fileId});
                            //window.open(url, '_blank');
                            
                            window.open('/html/pdf/viewer.html?file=/json/fileimage/' + contract.customerContract.fileId, "_blank");
                            //window.open( "/json/fileimage/" + contract.customerContract.fileId,"_blank");
                        }
                    });*/
                    $scope.save(document, function(fileId){
                        window.open('/pdf/viewer.html?file=/json/file/download/' + fileId, "_blank");
                    })
                }

                $scope.download = function(document){
                    $scope.save(document, function(fileId){
                        window.open( "/json/file/download/" + fileId,"_blank");
                    })
                };

                $scope.save = function(document, callback) {
                    var customerContract = document.customerContract;
                    if (!customerContract) {
                        customerContract = {document: document, dataStatus: 1, customerId: $scope.customerid, downloadRecords: [{loginUserId: loginUserId}]};
                    } else {
                        if (!customerContract.downloadRecords)
                            customerContract.downloadRecords = [];
                        customerContract.downloadRecords.push({loginUserId: loginUserId});
                    }

                    //保存客户合同

                    documentService.saveCustomerContract(false,customerContract, function(data) {
                        document.customerContract = data;
                        document.downloads = document.customerContract.downloadRecords.length;

                        if (document.customerContract.fileId) {
                            callback(document.customerContract.fileId);
                        }
                    });
                }
            }


        };
    });