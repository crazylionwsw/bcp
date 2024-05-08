/**
 * Created by LB on 2016-10-18.
 */

'use strict';

app.controller('printerController',['$scope','printerService','$stateParams','$state','$modal',function($scope,printerService,$stateParams,$state,$modal){
    $scope.title = '资料打印';
    $scope.tableTitle = '资料打印列表';
    $scope.formTitle = "资料打印";
    
    /*列表*/
    $scope.customers  = [];//返回的列表数据
    $scope.customer = {};


    $scope.init=function(){
        if($stateParams.id) {
            //we do nothing
        }else {
            $scope.changePage(1);
        }
    }


    /*分页功能*/
    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }

        printerService.getPageData($scope.currentPage,function(data){
            $scope.customers = data.content;
            $scope.totalItems = data.totalElements;//数据总条数
            $scope.pageSize = data.size;//分页单位
            $scope.currentPage = data.number + 1 ;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });
    }

    /**
     * 执行初始化
     */
    $scope.init();
    
    $scope.print = function(customer){
        if(customer.id != "" && customer.id != null){
            $scope.customer = customer;
            $state.go("app.business.printeredit",{id:customer.id});
        }else{
            $scope.customer = {};
            $state.go("app.business.printeredit");
        }
    }

    /* 业务类型添加 / 修改 */
    $scope.openQueryDialog = function (){
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'tpl/view/printer/query.html',
            controller:function ($scope,$modalInstance){
                $scope.modalTitle = "查询";

                $scope.checkQuery = function(value) {
                    if ($scope.customer.queryString != null && $scope.customer.queryString.trim() != "") {
                        return true;
                    } else {
                        return false;
                    }
                };

                $scope.updateQueryString = function () {
                    $scope.customer.queryString = '';
                    if ($scope.customer.name) $scope.customer.queryString += $scope.customer.name;
                    if ($scope.customer.identifyNo) $scope.customer.queryString += $scope.customer.identifyNo;
                    if ($scope.customer.cell) $scope.customer.queryString += $scope.customer.cell;
                }
                /*清楚查询内容*/
                $scope.clearQuery = function() {
                    $scope.changePage(1);
                    modalInstance.close();
                };

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }

                /* 保存  修改 客户基本信息 */
                $scope.queryCustomers = function (){
                    if ($scope.customer.cell) {
                        $scope.customer.cells = [$scope.customer.cell];
                    }
                    var customer = $scope.customer;
                    var currentPage = $scope.currentPage;
                    printerService.search(customer,currentPage,function (data){
                        $scope.$parent.customers = data.content;
                        $scope.$parent.totalItems = data.totalElements;//数据总条数
                        $scope.$parent.pageSize = data.size;//分页单位
                        $scope.$parent.currentPage = data.number + 1;//当前页
                        $scope.$parent.totalPages=data.totalPages;//总页数
                    })
                    /* 操作完成自动关闭模态框 */
                    modalInstance.close();
                }

            }
        });
    }
    

}]);
