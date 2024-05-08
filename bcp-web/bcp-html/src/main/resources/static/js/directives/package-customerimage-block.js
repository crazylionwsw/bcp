angular.module('app')
    .directive('packageCustomerimageBlock', function(customerService, $filter,$cookieStore) {
        return {
            restrict: 'E',
            scope: {cashsource:'=',order:'=', orderid:'=', customerid:'='},
            templateUrl: 'tpl/view/customerimage/package.html',
            link: function($scope, element, attrs) {

                customerService.getCustomerImages($scope.customerid, function(data){
                    if (data) {
                        var images = data;
                        if(images){
                            angular.forEach(images, function(item, k){
                                images[k].canDelete = false;


                                if (item.loginUserId == $cookieStore.get("userID")) {
                                    images[k].canDelete = true;
                                }
                            })
                            var imagetypes = $scope.cashsource.packageImageTypes;
                            imagetypes.forEach(function (item, index) {
                                imagetypes[index].customerImage = $filter('getByImageTypeId')(images, item.id);
                                if (imagetypes[index].customerImage && imagetypes[index].customerImage.fileIds && imagetypes[index].customerImage.fileIds.length > 0) {
                                    imagetypes[index].selected = true;
                                }
                            });
                            $scope.imagetypes = imagetypes;
                        }

                    }
                });
            },
            controller: function($scope, $element, $filter, $timeout, packageService, $modal, $cookieStore, FileUploader){
                $scope.type = {};
                $scope.fileIds = [];
                $scope.customerImage = {};

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
                }

                $scope.isPDF = function(type) {
                    if (type.suffixes.indexOf('pdf') > -1) {
                        return true;
                    } else {
                        return false;
                    }
                }

                $scope.viewPDF = function(fileId) {
                    window.open('/pdf/viewer.html?file=/json/file/download/' + fileId, "_blank");
                }
                //初始化FileUploader
                var fileUploader = $scope.fileUploader= new FileUploader({
                    url: '/json/fileimage',
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
                    $scope.fileIds.push(response.d);
                };

                $scope.fileUploader.onWhenAddingFileFailed = function(item, filter, options) {
                    var types = $scope.type.suffixes.join("/");
                    alert('文件类型不符，请选择'+ types + '文件！');
                };

                $scope.fileUploader.onAfterAddingAll = function(addedFileItems) {
                    $scope.uploading = $scope.type;
                };
                $scope.fileUploader.onCompleteAll = function(){
                    var customerImage = $scope.type.customerImage;
                    if (customerImage) {
                        customerImage.fileIds = $scope.type.customerImage.fileIds.concat($scope.fileIds);
                        customerImage.loginUserId = $cookieStore.get("userID");
                    } else {
                        customerImage = {customerId: $scope.customerid, customerImageType: $scope.type, fileIds: $scope.fileIds, loginUserId: $cookieStore.get("userID")};
                    }

                    //保存customerImage
                    packageService.save(customerImage,function (data){
                        var idx = $scope.imagetypes.indexOf($scope.type);
                        $scope.imagetypes[idx].customerImage = data;
                        $scope.fileIds = [];
                        $scope.uploading = null;
                        alert('上传完毕！');
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
                        packageService.delete(customerImage.id, function (data) {
                            $scope.customerImage.fileIds.splice(index, 1);
                            $scope.save({customerImage: $scope.customerImage});

                        });
                    }
                };


                $scope.package = function(){
                    var customerImageIds = [];
                    $scope.imagetypes.forEach(function(item){
                        if (item.customerImage && item.selected) {
                            customerImageIds.push(item.customerImage.id)
                        }
                    })
                    
                    if (customerImageIds.length > 0) {
                        var cp = {cashSourceId: $scope.cashsource.id,
                            customerId: $scope.customerid,
                            purchaseCarOrderId: $scope.orderid,
                            imageFileIds: customerImageIds,
                            loginUserId: $cookieStore.get("userID")};
                        //调用service进行打包
                        packageService.package(cp, function(data) {
                            $scope.order.customerPackages.push(data);
                            alert('打包成功！');
                        });
                        //$scope.$emit('receiveCustomerImageIds', customerImageIds);
                    } else {
                        alert('请选择要打包的档案！')
                    }
                };
            }
        };
    });