/**
 * Created by LB on 2016-10-18.
 */

'use strict';
app.controller('customerTransactionController',['$scope','customerTransactionService','enhancementService','sysparamService','$state','$modal','$localStorage','$stateParams','$rootScope','toaster',function($scope,customerTransactionService,enhancementService,sysparamService,$state,$modal,$localStorage,$stateParams,$rootScope,toaster){
    $scope.tableTitle = '客户业务管理';

    /*列表*/
    $scope.customertransactions = [];
    $scope.customertransaction = {};

    $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:9,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A000'};
    $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:9,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A000'};

    $scope.exportSearch = {timeName:'ts',compensatory:false,business:false,nc:false,oc:false,billTypeCode:'A011'};
    $scope.exportSearchFilter = {timeName:'ts',compensatory:false,business:false,nc:false,oc:false,billTypeCode:'A011'};
    
    $scope.currentPage = 1;
    $scope.checked = false;
    $scope.checkedDecompress = false;

    //贷款金额合计
    $scope.totalCreditAmount = 0.0;

    $scope.imageTypeCodes = [];
    $scope.thisDate = new Date();

    $scope.canSubmit = false;


    $scope.init = function(){

        if ($localStorage.TransactionSearchFilter){
            $scope.searchFilter = $localStorage.TransactionSearchFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        if ($stateParams.cname){
            $scope.searchFilter.name = $stateParams.cname;
            $scope.searchFilter.statusValue = -1;
            $scope.changePage(1);
        } else {
            $scope.changePage(1);
        }
    };

    $scope.changePage = function (pageIndex){
        //清零
        $scope.totalCreditAmount = 0.0;
        if (pageIndex){
            $scope.currentPage = pageIndex;
        }
        customerTransactionService.search($scope.searchFilter,$scope.currentPage-1,function (data){
            $scope.customertransactions = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });
        
        $localStorage.TransactionSearchFilter = $scope.searchFilter;
    };

    $scope.addTotal = function (amount) {
        if(amount)
            $scope.totalCreditAmount = $scope.totalCreditAmount+ amount;
    };
    
    $rootScope.$on('Enhancement',function (e, transaction) {
        if (!$scope.checked) {
            if (transaction){
                $scope.transaction = transaction;
                $scope.imageTypeCodes = [];
                $scope.comment = "";
                $scope.dueTime = null;
                $scope.checked = true;
                $scope.canSubmit = false;
            }
        }
    });

    $scope.close = function () {
        $scope.checked = false;
    };
    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    $scope.init();

    $scope.search = function () {
        $scope.changePage(1);
    };

    $scope.changePageSize = function () {
        $scope.searchFilter.pageSize = $scope.pageSize;
        $scope.changePage(1);
    };

    /*          模糊查询            START              */
    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
            animation:true,
            size:'lg',
            templateUrl:"query.html",
            controller:function($scope,$modalInstance){
                //we do nothing now!
            }
        })
    };

    /**
     * 导出窗口
     */
    $scope.exportQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
            animation:true,
            size:'lg',
            templateUrl:"export.html",
            controller:function($scope,$modalInstance){
                //we do nothing now!
            }
        })
    };

    $scope.exportBill = function (search) {
        if(search.compensatory == true){
            window.open('/json/compensatorytransactions/export?compensatory='+search.compensatory+'&business='+search.business+'&nc='+search.nc+'&oc='+search.oc+'&swipingCardTime='+(search.swipingCardTime?search.swipingCardTime:""));
        }else if(search.business == true){
            window.open('/json/businesstransactions/export?compensatory='+search.compensatory+'&business='+search.business+'&nc='+search.nc+'&oc='+search.oc+'&swipingCardTime='+(search.swipingCardTime?search.swipingCardTime:""));
        }
    };
        
    //清空导出的查询条件
    $scope.clearQuery = function() {
        $scope.exportSearchFilter = {timeName:'ts',compensatory:false,business:false,nc:false,oc:false,billTypeCode:'A011'};
    };
    
    /**
     * 清空查询
     */
    $scope.clearQuery = function() {
        $scope.searchFilter = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'status',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A000'};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param customer
     */
    $scope.queryBill = function (search) {
        $scope.searchFilter = search;
        $scope.changePage(1);
    };
    /*          模糊查询            END              */

    //  接受广播，更新$scope中的imageTypeIds
    $scope.$on('UpdateImageTypes',function (e,customerImageTypeCodes) {
       $scope.imageTypeCodes = customerImageTypeCodes;
    });

    $scope.submit = function () {

        if($scope.dueTime == null && $scope.imageTypeCodes.length == 0){
            toaster.pop('error', '资料补全', '请选择补全资料截止时间和要补全的档案类型!');
        }else if ($scope.dueTime == null){
            toaster.pop('error', '资料补全', '请选择补全资料截止时间!');
        }else if ($scope.imageTypeCodes.length == 0){
            toaster.pop('error', '资料补全', '请选择要补全的档案类型!');
        }else if ($scope.dueTime != null && $scope.imageTypeCodes.length > 0){
            var modalInstance = $modal.open({
                scope:$scope,
                animation:true,
                size: 'lg',
                templateUrl: 'submit.html',
                controller:function($scope,$modalInstance){
                    //是
                    $scope.ok = function () {
                        $scope.save(1);
                        modalInstance.close();
                    };
                    //否
                    $scope.no = function () {
                        $scope.save(0);
                        modalInstance.close();
                    }
                }
            });
        }
    };

    $scope.save = function (allowDeleteCustomerImage) {
        var imageTypeCodes = $scope.imageTypeCodes;
        var dueTime = $scope.dueTime;
        var comment = $scope.comment;
        if (imageTypeCodes.length > 0) {
            var enhancement = {
                customerTransactionId: $scope.transaction.id,
                customerImageTypeCodes: imageTypeCodes,
                approveStatus: 0,
                dataStatus: 1,
                comment : comment,
                dueTime: dueTime,
                userId:$rootScope.user.userID,
                allowDeleteCustomerImage:allowDeleteCustomerImage
            };
            enhancementService.save(enhancement, function (data) {
                if (data){
                    toaster.pop('success', '资料补全', '发起成功');
                    $scope.canSubmit = true;
                } else {
                    toaster.pop('error', '资料补全', '发起失败');
                    $scope.canSubmit = false;
                }
            });
        }
    }
}]);
