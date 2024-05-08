/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('packageController',function ($scope,packageService,orderService, $modal,$stateParams,$state){

    $scope.title = "资料打包";
    $scope.tableTitle = "资料打包列表";
    $scope.formTitle = "资料打包";

    /*  列表 */
    $scope.orders  = [];//返回列表数据
    $scope.order = {};

    $scope.order.cashSources ={};

    $scope.customerFilter = {};

    /**
     * 打包记录
     */
    $scope.order.customerPackages = [];
    $scope.customerPackage = {};

    $scope.init = function (){
        if($stateParams.id){
            packageService.getOne($stateParams.id,function (data) {
                $scope.order = data;
                orderService.getCashSources($stateParams.id, function (data) {
                    $scope.order.cashSources = data;
                });
                packageService.getAllCustomerPackages($scope.order,function(data){
                    $scope.order.customerPackages = data;
                });
            })
        }else{
            $scope.changePage(1);
        }

    };
/*
    $scope.$on('receiveCustomerImageIds', function(event, params){
        $scope.packageCustomerImageIds = params;
    });*/

    /*分页功能*/
    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }

        packageService.getDataByPage($scope.currentPage,function(data){
            $scope.orders = data.content;
            $scope.totalItems = data.totalElements;//数据总条数
            $scope.pageSize = data.size;//分页单位
            $scope.currentPage = data.number + 1 ;//当前页
            $scope.totalPages = data.totalPages;//总页数
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();
    //回显
    $scope.edit = function(order){
        $state.go("app.business.packageedit",{id:order.id});
    }
    
    $scope.download = function(id){
        window.open('/json/customerpackages/'+id+'/download');
    }




    $scope.openQueryDialog = function (){
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'tpl/view/package/query.html',
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

                /*查询信息*/
                $scope.queryOrderNewcars = function () {
                    if ($scope.customer.cell) {
                        $scope.customer.cells = [$scope.customer.cell];
                    }
                    var customer = $scope.customer;
                    var currentPage = $scope.currentPage;
                    packageService.search(customer,currentPage,function (data){
                        $scope.$parent.orders = data.content;
                        $scope.$parent.totalItems = data.totalElements;//数据总条数
                        $scope.$parent.pageSize = data.size;//分页单位
                        $scope.$parent.currentPage = data.number + 1;//当前页
                        $scope.$parent.totalPages=data.totalPages;//总页数
                    })
                    modalInstance.close();
                }
            }
        });
    }

    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:"query.html",
            controller:function($scope,$modalInstance){
                //we do nothing now!
            }
        })
    }

    /**
     * 清空查询
     */
    $scope.clearQuery = function() {
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param customer
     */
    $scope.queryBill = function (customer) {
        $scope.customerFilter = customer;

        if (customer.cell) {
            customer.cells = [customer.cell];
        }

        var currentPage = 1;
        packageService.search(customer,currentPage,function (data){
            $scope.orders = data.content;
            $scope.totalItems = data.totalElements;//数据总条数
            $scope.pageSize = data.size;//分页单位
            $scope.currentPage = data.number + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数
        })
    }


});