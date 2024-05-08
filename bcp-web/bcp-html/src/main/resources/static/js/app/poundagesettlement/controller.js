/**
 * 银行报批
 */
'use strict';
app.controller('poundagesettlementController',['$scope','poundagesettlementService','sysparamService','cardealerService','cashsourceService','$modal','$localStorage', function ($scope,poundagesettlementService,sysparamService,cardealerService,cashsourceService,$modal,$localStorage){

    $scope.title = '支行结算';

    $scope.poundagesettlements = [];
    $scope.poundagesettlement ={};
    //  手续费缴纳方式列表
    $scope.bankChargePaymentWays =[];
    //合作支行
    $scope.cashSources = [];
    //  报单支行
    $scope.declarationCashSources =[];

    /*查询参数*/
    $scope.customerQuery={userName:""};
    $scope.poundagesettlementQuery={declarationCashSourceId:"",channelCashSourceId:"",chargePaymentWayCode:"",startTime:"",endTime:""};

    $scope.oneAtATime = true;

    $scope.creditAmountTotal = 0;
    $scope.chargeTotal = 0;

    /**
     * 获取列表数据
     */
    $scope.init = function (){

        sysparamService.getListByCode("DECLARATION_BANKS",function (data) {
            $scope.declarationCashSources = data;
        });
        //      获得  手续费支付方式
        sysparamService.getListByCode("BANK_CHARGE_PAYMENTWAY",function (data) {
            $scope.bankChargePaymentWays = data;
        });
        
        //资金来源列表（所有支行）
        cashsourceService.getChildBank(function (data) {
            $scope.cashSources = data;
        });

        if ($localStorage.PoundagesettlementSearchFilter){
            $scope.poundagesettlementQuery = $localStorage.PoundagesettlementSearchFilter;
            $scope.customerQuery = {userName:$localStorage.PoundagesettlementSearchFilter.name};
        }

        $scope.changePage(1);
    };


    /*分页功能*/
    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        if (($scope.customerQuery.userName != undefined && $scope.customerQuery.userName != "") || $scope.poundagesettlementQuery.declarationCashSourceId || $scope.poundagesettlementQuery.channelCashSourceId || $scope.poundagesettlementQuery.chargePaymentWayCode || $scope.poundagesettlementQuery.startTime || $scope.poundagesettlementQuery.endTime){
            poundagesettlementService.search($scope.customerQuery.userName,$scope.poundagesettlementQuery,$scope.poundagesettlementQuery.startTime,$scope.poundagesettlementQuery.endTime,$scope.currentPage-1,function (data){
                $scope.poundagesettlements = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages=data.totalPages;//总页数
                $scope.getPayTotal(data.result);
            });
        } else {
            poundagesettlementService.getDataByPage($scope.currentPage-1,function(data){
                $scope.poundagesettlements = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1 ;//当前页
                $scope.totalPages = data.totalPages;//总页数
                $scope.getPayTotal(data.result);
            });
        }
        $localStorage.PoundagesettlementSearchFilter = $scope.poundagesettlementQuery;
        $localStorage.PoundagesettlementSearchFilter.name = $scope.customerQuery.userName;
    };

    $scope.getPayTotal=function(poundagesettlements){
        $scope.creditAmountTotal = 0;
        $scope.chargeTotal = 0;
        angular.forEach(poundagesettlements,function (item, index) {
            $scope.creditAmountTotal += item.limitAmount;
            $scope.chargeTotal += item.poundage;
        });
    };

    /**
     * 初始化
     */
    $scope.init();

    //清空模糊查询
    $scope.clearPoundageSettlement = function () {
        $scope.customerQuery = {};
        $scope.poundagesettlementQuery = {};
        $scope.changePage($scope.currentPage);
    };

    $scope.refresh = function (poundagesettlement,index) {
        poundagesettlementService.refreshData(poundagesettlement.customerTransactionId,function (data) {
            if (data && data != undefined){
                $scope.poundagesettlements[index] = data;
            }
        })
    }

}]);

