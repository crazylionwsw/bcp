/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('pickupcarController',['$scope','pickupcarService','$modal','$loading','$localStorage',function ($scope,pickupcarService,$modal,$loading,$localStorage){

    $scope.title = "提车记录";

    $scope.pickupcars = [];//返回的列表数据
    $scope.pickupcar = null;

    $scope.customer = {};
    $scope.customerFilter = {};

    $scope.currentPage=1;

    $scope.checked = false;

    /**
     * 列表
     */
    $scope.init = function (){
        if ($localStorage.PickupCarCustomerFilter){
            $scope.customerFilter = $localStorage.PickupCarCustomerFilter;
        }
        $scope.changePage(1);
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){

        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        if( $scope.customerFilter.name || $scope.customerFilter.identifyNo || $scope.customerFilter.cells){
            pickupcarService.search($scope.customerFilter,$scope.currentPage -1,function (data){
                $scope.pickupcars = data.result;//返回的数据
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages = data.totalPages;//总页数
            })
        }else{
            pickupcarService.getDataByPage($scope.currentPage-1,function (data){
                $scope.pickupcars = data.result;//返回的数据
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages = data.totalPages;//总页数
            });
        }
        
        $localStorage.PickupCarCustomerFilter = $scope.customerFilter;
    };

    $scope.init();

    $scope.edit = function (pickupcar) {
        if (!$scope.checked){
            if ($scope.pickupcar){
                $scope.pickupcar = {};
            }
            pickupcarService.getOne(pickupcar.id,function (data) {
                $scope.pickupcar = data;
                $scope.checked = true;
            });
        }
    };
    
    $scope.close = function () {
      $scope.checked = false;  
    };
    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    /**
     * 审核窗口
     */
    $scope.audit = function (){
        $scope.signModal = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:"audit.html",
            controller:function($scope,$modalInstance){
                //we do nothing now!
            }
        })
    };

    /**
     * 审核/审批
     * @param signinfo
     */
    $scope.sign = function(signinfo){
        $loading.start('signInfo');
        pickupcarService.sign($scope.pickupcar,signinfo,function(data){
            $scope.pickupcar = data;
            $loading.finish('signInfo');
            $scope.signModal.close();
        })
    };
    
    $scope.save = function (){
        var pickupcar = $scope.pickupcar;
        pickupcarService.save(pickupcar,function (data){
            $scope.pickupcar = data;
        });
    };
    
    /**
     * 删除预约提车
     */
    $scope.delete = function (pickupcar,index){
        pickupcarService.deletePickupCar(pickupcar,function (data){
            pickupcar.dataStatus = 9;
            if(confirm("确定要删除数据"+pickupcar.id+"?")) {
                $scope.pickupcars.splice(index, 1);
                $scope.pickupcar = {};
            }
        })
    };

    /**
     * 查询
     */
    $scope.search = function() {
        $scope.customerFilter = {};
        $scope.changePage(1);
    };

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
    };

    /**
     * 清空查询
     */
    $scope.clearQuery = function() {
        $scope.customerFilter = {};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param customer
     */
    $scope.queryBill = function (customer) {
        
        if (customer.cell) {
            customer.cells = [customer.cell];
        }
        $scope.customerFilter = customer;
        $scope.changePage(1);
    };
    
}]);