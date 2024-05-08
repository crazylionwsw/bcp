/**
 * Created by lily on 2017-05-04.
 */
'use strict';
app.controller('statisticController',['$scope','statisticService','$state','$stateParams','$modal','$cookieStore','$loading','$filter','toaster',function ($scope,statisticService,$state,$stateParmas,$modal,$cookieStore,$loading,$filter,toaster) {
    $scope.tableTitle = "销售汇总";
    $scope.tableDesc = "销售提成汇总表";
    $scope.formTitle = "销售汇总";

    //返回的列表数据
    $scope.statistics = [];
    $scope.statistic = null;
    $scope.signModal = {};
    $scope.saleMonth = $filter('date')(new Date(),"yyyy-MM");

    /*$scope.init = function(){
        statisticService.getAll($scope.saleMonth,function(data) {
            $scope.statistics = data;
        })
    }

    $scope.init();*/

    $scope.$watch('saleMonth', function(salemonth) {
        statisticService.getAll(salemonth,function(data) {
            $scope.statistics = data;
            /*$scope.saleMonth = salemonth;*/
        })
    })

    $scope.downLoad = function(){
        /*模态框*/
        var modalInstance;
        modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "statisticDownLoad.html",
            controller: function ($scope,$modalInstance) { /*关闭模态框*/
                $scope.cancel = function () {
                    modalInstance.close();
                }

                /**
                 * 下载销售汇总表
                 */
                $scope.startDownLoad = function (downSaleMonth) {
                    statisticService.downLoad(downSaleMonth, function (data) {
                        toaster.pop('success', '销售人员提成汇总表:'+data.duty);
                    })
                    modalInstance.close();
                }

            }
        });
    }

}])