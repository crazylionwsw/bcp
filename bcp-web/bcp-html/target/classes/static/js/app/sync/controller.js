/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('syncController',function ($scope,syncService,$modal,toaster,$loading, $q){

    $scope.tableTitle="数据同步管理";

    $scope.syncs = [];//返回列表数据
    $scope.sync = {};

    $scope.carBrands =[];

    $scope.carBrand = {};

    /**
     * 初始化加载列表和分页
     */
    $scope.init = function(){
        $scope.changePage(1);
    }

    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }

        syncService.getPageData($scope.currentPage,function(data){
            $scope.syncs = data.content;
            $scope.totalItems = data.totalElements;//数据总条数
            $scope.pageSize = data.size;//分页单位
            $scope.currentPage = data.number + 1 ;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });
    }


    /**
     * 删除日志
     */
    $scope.delete = function(sync, index) {
        if (confirm("确定要删除数据?")) {
            syncService.delete(sync, function (data) {
                var index = $scope.syncs.indexOf(sync);
                $scope.syncs.splice(index, 1);
            })
        }
    }


    /**
     * 获得全部车型品牌
     */
    $scope.syncCarbrands = function (syncs) {
        /*模态框*/
        var modalInstance;
        modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "carbrandsForm.html",
            controller: function ($scope,$modalInstance) { /*关闭模态框*/
                $scope.cancel = function () {
                    modalInstance.close();
                }

                syncService.getAllcarBrands(function (data) {
                    $scope.carBrands = data;
                })  /**
                 * 更新一个品牌Car
                 */
                $scope.syncCarType = function (id) {
                        $loading.start('syncCar');
                    syncService.syncCarType(id, function (data) {
                        $loading.finish('syncCar');
                        toaster.pop('success', '同步车系数据'+data.d.carModels+'条！', '同步车型数据'+data.d.carTypes+'条！');
                    })
                    modalInstance.close();
                }

            }
        });
    }

    /**
     * 执行初始化
     */
    $scope.init();
    
    
});