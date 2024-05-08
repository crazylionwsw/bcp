/**
 * Created by zxp on 2017/3/9.
 */
angular.module('app')
    .directive('businessContractBlock', function(documentService,orderService, $filter) {
        return {
            restrict: 'E',
            scope: {bill: '='},
            templateUrl: 'tpl/view/document/list.html',
            link: function ($scope, element, attrs) {
                $scope.documents =[];
                $scope.customerContract={};

                $scope.$watch('bill', function (bill) {
                    if ($scope.bill) {

                        //      根据  客户ID 、交易ID以及该单据必须的合同IDS,获取客户的合同信息
                        documentService.getByCustomerIdAndCustomerTransactionIdAndDocumentIds($scope.bill.customerId,$scope.bill.customerTransactionId,$scope.bill.billType.documentIds,function (data) {
                                var customerContracts = data;
                                var custometContractIds = [];
                                if (customerContracts){
                                    angular.forEach(customerContracts,function (item, k) {
                                        custometContractIds[k] = item.id;
                                    });
                                    documentService.getTypesByIds($scope.bill.billType.documentIds, function (data) {
                                        $scope.documents = data;
                                        angular.forEach($scope.documents, function(v, k){
                                            $scope.documents[k].downloads = 0;
                                            angular.forEach(customerContracts, function(d, dk){
                                                if (d.documentId == v.id) {
                                                    $scope.documents[k].customerContract = d;
                                                    $scope.documents[k].downloads = d.downloadRecords ? d.downloadRecords.length : 0;
                                                    return;
                                                }
                                            });
                                        });
                                    });
                                }
                        });
                    }
                });
            },

            controller: function($scope, $element, $filter, $cookieStore, $location, customerService,documentService,sysparamService,orderService,$state,$modal,$rootScope) {
                
                var loginUserId = $rootScope.user.userID;
                
                $scope.view = function(document) {
                    if (document.code == "024"){
                        alert("需要选择客户需要交接的资料！");
                    }
                    $scope.save(document, function(fileId){
                        if (document.fileType == "docx"){
                            window.open( "/json/file/download/" + fileId,"_blank");
                        } else {
                            window.open('/pdf/viewer.html?file=/json/file/download/' + fileId, "_blank");
                        }
                    })
                };

                $scope.download = function(document){
                    $scope.save(document, function(fileId){
                        window.open( "/json/file/download/" + fileId,"_blank");
                    })
                };
                $scope.check = function(document){
                    $scope.savecontract(document, function(fileId){
                        if (document.fileType == "docx"){
                            window.open( "/json/file/download/" + fileId,"_blank");
                        } else {
                            window.open('/pdf/viewer.html?file=/json/file/download/' + fileId, "_blank");
                        }
                    })
                };
                $scope.preview = function(document,callback){

                    var modalInstance = $modal.open({
                        scope:$scope,
                        size: 'lg',
                        animation:true,
                        templateUrl:'tpl/view/document/imageshow.html',
                        controller:function($scope,$modalInstance){
                            $scope.modalTitle = "审批函照片";
                            $scope.customerimage ={};
                            customerService.getShenpihan(document.customerContract.customerId,function (data) {
                                $scope.customerimage = data;
                            })
                        }
                    })
                };

                //客户交接
                $scope.chooseDocument = function (document, callback) {
                    var modalInstance = $modal.open({
                        scope:$scope,
                        animation:true,
                        size: 'lg',
                        templateUrl:'chooseDocument.html',
                        controller:function ($scope,$modalInstance,$q){

                            $scope.modalTitle = "客户资料选择";
                            $scope.order = $scope.bill;
                            $scope.receptFile = {customerTransactionId:$scope.bill.customerTransactionId};

                            orderService.getReceptFileByCutomerTransactionId($scope.bill.customerTransactionId,function (data) {
                                if (data){
                                    $scope.receptFile= data;
                                }
                            });

                            /*  关闭模态框 */
                            $scope.cancel = function ($modalInstance){
                                modalInstance.close();
                            };
                            //  获取 客户资料交接单的材料清单
                            sysparamService.getListByCode("CUSTOMER_INFORMATION_RECEIPT",function (data) {
                                $scope.receiptDatas  = data;
                            });

                            $scope.saveReceiptData = function ($modalInstance) {

                                var receptFile = $scope.receptFile;
                                receptFile.customerTransactionId =$scope.bill.customerTransactionId;
                                orderService.saveRecept(receptFile,function (data) {
                                    $scope.receptFile = data;
                                });
                                modalInstance.close();
                            }
                        }
                    });
                };

                $scope.savecontract = function(document, callback) {
                    var customerContract = document.customerContract;
                    if (!customerContract) {
                        customerContract = {documentId: document.id, dataStatus: 1, customerId: $scope.bill.customerId,customerTransactionId:$scope.bill.customerTransactionId,downloadRecords: [{loginUserId: loginUserId}]};
                    } else {
                        if (!customerContract.downloadRecords)
                            customerContract.downloadRecords = [];
                        customerContract.downloadRecords.push({loginUserId: loginUserId});
                    }

                    //保存客户合同
                    documentService.saveCustomerContract(true,customerContract, function(data) {
                        if (data){
                            document.customerContract = data;
                            document.downloads = document.customerContract.downloadRecords.length;

                            if (document.customerContract.fileId) {
                                callback(document.customerContract.fileId);
                            }
                            $rootScope.$broadcast('UpdateContract',document);
                        } else{
                            alert("生成"+document.name +"失败");
                        }
                    });

                };

                $scope.save = function(document, callback) {
                    var customerContract = document.customerContract;
                    if (!customerContract) {
                        customerContract = {documentId: document.id, dataStatus: 1, customerId: $scope.bill.customerId,customerTransactionId:$scope.bill.customerTransactionId, downloadRecords: [{loginUserId: loginUserId}]};
                    } else {
                        if (!customerContract.downloadRecords)
                            customerContract.downloadRecords = [];
                        customerContract.downloadRecords.push({loginUserId: loginUserId});
                    }

                    //保存客户合同
                    documentService.saveCustomerContract(false,customerContract, function(data) {
                        if (data){
                            document.customerContract = data;
                            document.downloads = document.customerContract.downloadRecords.length;

                            if (document.customerContract.fileId) {
                                callback(document.customerContract.fileId);
                            }
                            $rootScope.$broadcast('UpdateContract',document);
                        } else{
                            alert("生成"+document.name +"失败");
                        }
                    });
                };
            }
        };
    });