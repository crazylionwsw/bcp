/**
 * Created by LB on 2016-10-24.
 */

'use strict';
app.controller('fileexpressController',['$scope','$rootScope','fileexpressService','$modal','$filter','$state','$stateParams','$cookieStore',function($scope,$rootScope,fileexpressService,$modal,$filter,$state,$stateParams,$cookieStore){
    $scope.title = "资料快递";
    $scope.tableTitle ="资料快递列表";
    $scope.formTitle = "资料快递";
    //返回的列表数据
    $scope.fileexpresses = [];
    $scope.fileexpress={};

    //查询参数
    $scope.customer = {};
    $scope.customerFilter = {};

    $scope.currentPage = 1;

    $scope.checked = false;

    $scope.openDateDialog = function($event){
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    }
    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1,
        class: 'datepicker'
    };

    /**
     * 列表
     */
    $scope.init = function (){

        $scope.changePage();

    }

    /**
     * 分页功能
     */
    $scope.changePage = function (){

        fileexpressService.getDataByPage($scope.currentPage-1,function (data){
            $scope.fileexpresses = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

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
        $scope.customerFilter = {};
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
        fileexpressService.search(customer,$scope.currentPage - 1,function (data){
            $scope.fileexpresses = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数
            
        })
    };

    /**
     * 确认/保存
     */
    $scope.confirm=function(){
        var fileexpress = $scope.fileexpress;
        fileexpress.loginUserId = $rootScope.user.userID;
        fileexpressService.saveFileExpress(fileexpress,function(data){
            $scope.close();
        })
    }

    /**
     * 查看详情
     * @param fileexpress
     * @param index
     */
    $scope.edit = function(fileexpress,index){
        //$state.go("app.business.fileexpressedit",{id:fileexpress.id});
        //检查当前是否有已选中的行
        if(!$scope.checked){
            fileexpressService.getOne(fileexpress.id,function (data) {
                $scope.fileexpress = data;
                $scope.checked = true;
            })
        }
    }

    /**
     * 关闭详情页面
     */
    $scope.close = function () {
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    $scope.init();

}]);