angular.module('app')
    .directive('enhancementCustomerimageBlock', function() {
        return {
            restrict: 'E',
            scope: {bill:'=', save: '&'},
            templateUrl: 'tpl/view/customerimage/block.html',
            controller: function($scope, $element, $filter, $timeout, FileUploader, customerimageService, enhancementService, $modal,$rootScope,localStorageService,toaster){
                $scope.type = {};
                $scope.fileIds = [];
                $scope.customerImage = {};
                $scope.signModal = {};
                $scope.imagetypes = [];
                $scope.canAudit = true;

                $scope.$watch('bill', function(value) {
                    if (value) {
                        //装配档案类型
                        if ($scope.bill.billType) {
                            //取到此单据已上传的档案，每个档案中可以有多个图片
                            var requiredImageTypeCodes = $scope.bill.customerImageTypeCodes;

                            //  调用service，根据传入的customerImageIds取得customerImage
                            enhancementService.getImages($scope.bill.id,function(data){
                                var images = data;
                                if (images) {
                                    angular.forEach(images, function(item, k){
                                        images[k].canDelete = false;
                                        if (item.loginUserId == $rootScope.user.userID) {
                                            images[k].canDelete = true;
                                        }
                                    });

                                    customerimageService.getTypesByCodes(requiredImageTypeCodes,function(data) {
                                        var imgTypes = data;
                                        var imgTypeResult = [];
                                        requiredImageTypeCodes.forEach(function(item){
                                            imgTypes.forEach(function(it){
                                                if (it.code == item) {
                                                    it.customerImage = $filter('getByImageTypeCode')(images, it.code);
                                                    it.customerImage.coverFileId = $filter('getByImageTypeCode')(images, it.code).fileIds[0];
                                                    imgTypeResult.push(it);
                                                }
                                            });
                                        });
                                        $scope.imagetypes = imgTypeResult;
                                    });
                                }
                            });
                        }
                    }
                });

                $scope.openViewer = function (type) {
                    $scope.type = type;
                    $scope.$apply();
                    

                    if (!$scope.viewer) {
                        $scope.viewer = new Viewer(document.getElementById('customerImages'));
                        $('.img-placeholder').remove();
                    }

                    $scope.viewer.update();
                    $scope.viewer.show();
                };

                $scope.isVideo = function(type) {
                    if (type.suffixes.indexOf('mp4') > -1) {
                        return true;
                    } else {
                        return false;
                    }
                };

                $scope.isPDF = function(type) {
                    if (type.suffixes.indexOf('pdf') > -1) {
                        return true;
                    } else {
                        return false;
                    }
                };

                $scope.viewPDF = function(fileId) {
                    window.open('/pdf/viewer.html?file=/json/file/download/' + fileId, "_blank");
                };

                //初始化FileUploader
                var fileUploader = $scope.fileUploader= new FileUploader({
                    url: '/json/file/upload',
                    headers: {'User-Token':localStorageService.get('User-Token'), 'Client-Type':'PC'},
                    autoUpload: true,
                    removeAfterUpload: true
                });

                fileUploader.filters.push({
                    name: 'fileTypeFilter',
                    fn: function(item , options) {
                        var type = item.type.slice(item.type.lastIndexOf('/') + 1);
                        return $scope.type.suffixes.indexOf(type.toUpperCase()) !== -1 || $scope.type.suffixes.indexOf(type.toLowerCase()) !== -1;
                    }
                });

                //图片上传完成后记录文件ID
                $scope.fileUploader.onSuccessItem = function(fileItem, response, status, headers) {
                    $scope.fileIds.push(response.d.id);
                };

                $scope.fileUploader.onWhenAddingFileFailed = function(item, filter, options) {
                    var types = $scope.type.suffixes.join("/");
                    toaster.pop('error', '文件上传','文件类型不符，请选择'+ types + '文件！');
                };

                $scope.fileUploader.onAfterAddingAll = function(addedFileItems) {
                    $scope.uploading = $scope.type;
                };

                $scope.fileUploader.onCompleteAll = function(){
                    var customerImage = $scope.type.customerImage;
                    if (customerImage) {
                        if (!$scope.type.customerImage.fileIds){
                            $scope.type.customerImage.fileIds = [];
                        }
                        customerImage.fileIds = $scope.type.customerImage.fileIds.concat($scope.fileIds);
                        customerImage.loginUserId = $rootScope.user.userID;
                    } else {
                        customerImage = {customerId: $scope.bill.customerId,
                            customerTransactionId: $scope.bill.customerTransactionId,
                            customerImageTypeCode: $scope.type.code,
                            fileIds: $scope.fileIds,
                            loginUserId: $rootScope.user.userID};
                    }

                    //保存customerImage
                    customerimageService.save(customerImage,function (data){
                        var idx = $scope.imagetypes.indexOf($scope.type);
                        $scope.imagetypes[idx].customerImage = data;
                        data.canDelete = false;

                        if (data.loginUserId == $rootScope.user.userID) {
                            data.canDelete = true;
                        }
                        $scope.fileIds = [];
                        
                        $scope.uploading = null;
                        toaster.pop('success', '档案','【'+$scope.type.name+'】已上传完毕！');
                    });
                };

                /**
                 * 选取文件
                 */
                $scope.chooseFiles = function(type){
                    $scope.type = type;
                    $timeout(function() {
                        angular.element('#fileUploader').trigger('click');
                    }, 0);
                };

                $scope.delete = function(customerImage){
                    if (confirm("确定要删除?")) {
                        //调用JSON接口，删除CustomerImage
                        customerimageService.delete(customerImage.id, function (data) {
                            var idx = $scope.imagetypes.indexOf(type);
                            $scope.imagetypes[idx].customerImage.fileIds = [];
                            $scope.imagetypes[idx].customerImage.coverFileId = null;
                            toaster.pop('success', '档案','【'+type.name+'】已删除成功！');
                        });
                    }
                };

                /**
                 * 合成PDF
                 * @param type
                 */
                $scope.merge = function (imageType) {
                    var customerImage = imageType.customerImage;
                    var loginUserId = $rootScope.user.userID;

                    //检查是否存在合并的文件
                    customerimageService.getMergedCustomerContract(customerImage, function (data) {
                        if(data != null){
                            /**
                             * 模态框--添加/修改页面
                             */
                            var modalInstance = $modal.open({
                                scope:$scope,
                                animation:true,
                                size: 'xs',
                                templateUrl:'customerContract.html',
                                controller:function ($scope,$modalInstance){
                                    /*  关闭模态框 */
                                    $scope.cancel = function ($modalInstance){
                                        modalInstance.close();
                                    };

                                    /* 直接下载 */
                                    $scope.download = function (){
                                        customerimageService.mergeCustomerImages(false,customerImage,loginUserId,function (data){
                                            $scope.viewPDF(data.fileId);
                                            /* 操作完成自动关闭模态框 */
                                            modalInstance.close();
                                        });
                                    };
                                    /* 强制生成 */
                                    $scope.constraint = function (){
                                        customerimageService.mergeCustomerImages(true,customerImage,loginUserId,function (data){
                                            $scope.viewPDF(data.fileId);
                                            /* 操作完成自动关闭模态框 */
                                            modalInstance.close();
                                        });
                                    }
                                }
                            });
                        } else {
                            //文档不存在，需要生成
                            customerimageService.mergeCustomerImages(true, customerImage, loginUserId,function (data){
                                $scope.viewPDF(data.fileId);
                            });
                        }
                    })
                };

                /**
                 * 审核窗口
                 */
                $scope.audit = function (type) {

                    //取得当前审核的CustomerImage
                    $scope.customerImage = type.customerImage;

                    //显示审核窗口
                    var modalInstance = $modal.open({
                        scope:$scope,
                        animation:true,
                        templateUrl:"auditCustomerImage.html",
                        controller:function($scope,$modalInstance){
                            $scope.modalTitle = '审核/审批';
                            $scope.signinfo = {comment: null, result: 0, signUser:{id: $rootScope.user.userID}};

                            /*  关闭模态框 */
                            $scope.close = function ($modalInstance){
                                modalInstance.close();
                            };

                            /* 驳回 */
                            $scope.reapply = function (){
                                //设置审核状态
                                $scope.signinfo.result = 8;
                                customerimageService.sign($scope.customerImage, $scope.signinfo, function (data) {
                                    var idx = $scope.imagetypes.indexOf(type);
                                    $scope.imagetypes[idx].customerImage = data;
                                    /* 操作完成自动关闭模态框 */
                                    modalInstance.close();
                                });
                            }
                        }
                    })
                };
            }
        };
    });