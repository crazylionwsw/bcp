angular.module('app')
    .directive('declarationCollection', function(sysparamService,customerimageService,$filter) {
        return {
            restrict: 'E',
            scope: {transaction:'=',imagetypecods:'='},
            templateUrl: 'tpl/view/customerimage/collection.html',
            link: function ($scope, element, attrs) {
                $scope.$watch('transaction', function (transaction) {

                    $scope.imagetypes = [];
                    $scope.customerImage = {};
                    $scope.selectedList = [];

                    if (transaction) {
                        //取得档案类型列表
                        customerimageService.getDeclarationImageTypes(function(data){
                            $scope.imagetypes = data;
                            var imageTypeCodeList = [];
                            angular.forEach(data,function (item) {
                                imageTypeCodeList.push(item.code);
                            });
                            //取得已有的档案
                            customerimageService.getByCustomerIdAndCustomerTransactionIdAndImageTypeCodes($scope.transaction.customerId,$scope.transaction.id,imageTypeCodeList,function(data){
                                var images = data;
                                $scope.imagetypes.forEach(function (item, index) {
                                    $scope.imagetypes[index].customerImage = $filter('getByImageTypeCode')(images, item.code);
                                    $scope.imagetypes[index].selected = $filter('checkByImageTypeCode')($scope.imagetypecods, item.code);
                                    if ($scope.imagetypes[index].selected)  $scope.selectedList.push(item.code);
                                });
                            })
                        })
                    }
                })
            },
            controller: function($scope, $element, $filter, $timeout, customerimageService, $rootScope,$modal,$localStorage){

                $scope.signinfo = {comment: null, result: 0, userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id};

                $scope.selectImageType = function($event, imageType) {
                    var checkbox = $event.target;
                    var index = $scope.selectedList.indexOf(imageType.code);
                    if (checkbox.checked) {
                        if (index == -1) $scope.selectedList.push(imageType.code);
                    } else {
                        if (index > -1) $scope.selectedList.splice(index, 1);
                    }
                    $rootScope.$broadcast('UpdateImageTypes',$scope.selectedList);
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
                }

            }
        };
    });