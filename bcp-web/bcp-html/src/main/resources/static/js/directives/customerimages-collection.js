angular.module('app')
    .directive('customerimagesCollection', function() {
        return {
            restrict: 'E',
            scope: {customerid:'='},
            templateUrl: 'tpl/view/customerimage/collections.html',
            controller: function($scope, $element, $filter, $timeout, FileUploader,enhancementService, customerimageService, customerService, $modal, $cookieStore, Lightbox){

                $scope.formTitle = '资料补全';

                $scope.customerImages = [];

                $scope.customerImageTypes = [];
                
                $scope.init = function(){
                    customerimageService.getAll(function (data) {
                        $scope.customerImageTypes = data;
                        customerService.getImagetypes($scope.customerid,function(data){
                            $scope.customerImages = data;
                            //           数据装配
                            $scope.customerImageTypes.forEach(function (item, index) {
                                $scope.customerImageTypes[index].customerImage = $filter('getByImageTypeId')($scope.customerImages, item.id);
                                if ($scope.customerImageTypes[index].customerImage && $scope.customerImageTypes[index].customerImage.fileIds && $scope.customerImageTypes[index].customerImage.fileIds.length > 0) {
                                }
                            });
                        })
                    })
                };
                $scope.init();


                $scope.openLightboxModal = function (image) {
                    $scope.images = [];
                    angular.forEach(image.fileIds, function(item){
                        $scope.images.push({url: '/json/fileimage/'+ item, caption: image.customerImageType.name});
                    });
                    Lightbox.openModal($scope.images, 0);
                };

                $scope.openViewer = function (ci) {
                    $scope.customerImage = ci;
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

                $scope.datacompletion = function(){
                    var customerImageTypeIds = [];
                    $scope.customerImageTypes.forEach(function(item){
                        if ( item.selected) {
                            customerImageTypeIds.push(item.id)
                        }
                    })

                    if (customerImageIds.length > 0) {
                        var cp = {
                            customerId: $scope.customerid,
                            customerImageTypeIds: customerImageTypeIds,
                            loginUserId: $cookieStore.get("userID")};
                        //调用service进行打包
                        enhancementService.save(cp, function(data) {

                        });
                    } else {
                        alert('请选择要补全的档案！')
                    }
                };
            }
        };
    });