angular.module('app')
    .directive('creditphotographimageBlock', function() {
        return {
            restrict: 'E',
            scope: {id:'='},
            templateUrl: 'tpl/view/creditphotograph/block.html',
            controller: function($scope, $rootScope, $element, customerimageService,creditphotographService,toaster){
                $scope.customerImage = {};
                $scope.customerImages = [];
                $scope.creditPhotograph = {};
                $scope.generating = false;

                $scope.$watch('id', function(value) {
                    if (value) {
                        //根据 customerId 去后台查询    征信拍照的数据
                        creditphotographService.getOneByCustomerId(value,function (data) {
                            $scope.creditPhotograph = data;
                            $scope.customerImages = data.imageFileIds;
                            $scope.status = data.status;//      PDF的生成情况
                        });
                    }
                });

                $scope.createsave = function(){
                    var imageFiles = {};
                    angular.forEach($scope.customerImages,function(v,k){
                        imageFiles[k] = v;
                    });
                    $scope.creditPhotograph.imageFiles = imageFiles;

                    if ( confirm("开始生成PDF,是否需要删除照片？") ) {
                        $scope.creditPhotograph.comment = "true";
                    }else{
                        $scope.creditPhotograph.comment = "false";
                    }

                    $scope.generating = true;
                    customerimageService.createSavePdf($scope.creditPhotograph,function(data){
                        $scope.creditPhotograph = data;
                        $scope.generating = false;
                        toaster.pop('success','征信拍照', "征信报告已生成！");

                        $rootScope.$broadcast('CreditReportGenerated',$scope.creditPhotograph);
                    });
                    $scope.$parent.$close();
                };

                $scope.openViewer = function (event) {
                    $scope.$apply();
                    
                    if (!$scope.viewer) {
                        $scope.viewer = new Viewer(document.getElementById('customerImages'));
                        $('.img-placeholder').remove();
                    }

                    $scope.viewer.update();
                    $scope.viewer.show();
                };
                
                $scope.sortableOptions = {
                    cursor: "move",
                    update: function(e, ui) {  },
                    stop: function(e, ui) {
                        //拖拽完成后
                        console.log($scope.customerImages)
                    }
                };

                $scope.close = function() {
                    $scope.$parent.$close();
                }
            }
        };
    });