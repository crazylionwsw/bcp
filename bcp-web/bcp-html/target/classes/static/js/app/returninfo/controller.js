/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('returninfoController',function ($scope,returninfoService,$modal,$state,$stateParams,$cookieStore){

    $scope.title = "资料归还";
    $scope.tableTitle ="资料归还列表";
    $scope.formTitle = "资料归还";

    //返回的列表数据
    $scope.returninfos = [];
    $scope.returninfo = {};
   
    //查询参数
    $scope.customer = {};
    $scope.customerFilter = {};
    $scope.currentPage=1;
    
    /**
     * 列表
     */
    $scope.init = function (){
        $scope.returninfo = { status : 20 };
        $scope.changePage(1);
    };

    /**
     * 分页功能 资料返还
     */
    $scope.changePage = function (pageIndex){
        if (pageIndex){
            $scope.currentPage = 1;
        }
        returninfoService.getCarRegistriesByPage($scope.currentPage-1,function (data){
            $scope.returninfos = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages ;
        });
    };
    $scope.init();

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
        $scope.customerFilter={};
        $scope.changePage(1);
    };

    /**
     * 关闭
     */
   
    $scope.close=function () {
        $scope.checked=false;
    };
    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };


    /**
     * 模糊查询
     * 查询单据
     * @param customer
     */
    $scope.queryBill = function (customer) {
        
        if (customer.cell) {
            customer.cells = [customer.cell];
        }
        $scope.customerFilter = customer;
        var currentPage = 1;
        returninfoService.search(customer,currentPage-1,function (data){
            $scope.returninfos = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages ;
        })
    };

    $scope.edit = function(returninfo,index){
        if(!$scope.checked){
            returninfoService.getOne(returninfo.id,function (data) {
                $scope.returninfo=data;
                $scope.checked=true;

            })
        }
    };

    /**
     * 添加/修改
     */
    $scope.saveFrom = function (){
        var returninfo = $scope.returninfo;
        returninfo.loginUserId = $cookieStore.get("userID");
        returninfo.status = 8;
        returninfoService.save(returninfo,function (data){
            $state.go("app.business.returninfo");
        });
    };

    $scope.openQueryDialog = function (){
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'tpl/view/returninfo/query.html',
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
                };
                /*清楚查询内容*/
                $scope.clearQuery = function() {
                    $scope.changePage(1);
                    modalInstance.close();
                };

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };
                
                /* 保存业务信息 */
                $scope.queryReturninfos = function (){
                    if ($scope.customer.cell) {
                        $scope.customer.cells = [$scope.customer.cell];
                    }
                    var customer = $scope.customer;
                    var currentPage = $scope.currentPage;
                    returninfoService.search(customer,currentPage-1,function (data){
                        $scope.returninfos = data.result;//返回的数据
                        $scope.totalItems = data.totalCount;//数据总条数
                        $scope.pageSize = data.pageSize;//分页单位
                        $scope.currentPage = data.currentPage + 1;//当前页
                        $scope.totalPages = data.totalPages ;
                    });
                    /* 操作完成自动关闭模态框 */
                    modalInstance.close();
                }

            }
        });
    };
    
    /**
     * 日期控件
     * @param $event
     */
    $scope.open = function($event){
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };
    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1,
        class: 'datepicker'
    };
});