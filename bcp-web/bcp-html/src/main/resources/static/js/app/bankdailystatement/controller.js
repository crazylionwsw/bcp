/**
 * Created by admin on 2017-11-17.
 */

'use strict';

app.controller('bankdailystatementController',function ($scope,bankdailystatementService,cardealerService,cashsourceService,sysparamService,$filter,$modal,$state,$stateParams,$cookieStore,toaster,$loading){

    $scope.title = "银行日流水";

    //返回的列表数据
    $scope.channelbankdailystatements = [];
    $scope.declarationbankdailystatements = [];

    $scope.wholeTotal = {};
    $scope.stagesTotal = {};

    //合作支行
    $scope.cashSources = [];
    //报单支行
    $scope.declarationCashSources =[];

    $scope.poundagesettlement = {};

    $scope.selectedChannelCashSourceName;
    $scope.selectedDeclarationCashSourceName;

    //判断  表格是否显示
    $scope.showTwoBank = true;//是否显示两个支行，默认是报单行和渠道行都显示，当报单行和渠道行一致时，为false

    //计算 报单行、渠道行手续费分成情况合计
    $scope.declarationTotal = {};
    $scope.channelTotal = {};

    /**
     * 列表
     */
    $scope.init = function (){
        $scope.poundagesettlement.orderTime =  $filter('date')(new Date(), 'yyyy-MM-dd');
        //资金来源列表(所有的支行)
        cashsourceService.getChildBank(function (data) {
            $scope.cashSources = data;
        });

        sysparamService.getListByCode("DECLARATION_BANKS",function (data) {
            $scope.declarationCashSources = data;
        });
        
    };

    $scope.init();

    $scope.query = function () {
        $loading.start('query');
        $scope.showTwoBank = angular.equals($scope.poundagesettlement.channelCashSourceId,$scope.poundagesettlement.declarationCashSourceId)==false;
        if (!$scope.showTwoBank){
            //   渠道行、报单行   为一   展示 查询查询出来的 报单行 信息
            bankdailystatementService.searchdeclaration($scope.poundagesettlement, function (data) {
                $scope.declarationbankdailystatements = data;
                $scope.declarationTotal.nums = "共"+data.length+"笔";
                $scope.declarationTotal.limitAmountTotal =0.0;
                $scope.declarationTotal.chargeTotal =0.0;
                angular.forEach(data,function (item) {
                    $scope.declarationTotal.limitAmountTotal += item.limitAmount;
                    $scope.declarationTotal.chargeTotal += item.poundage;
                });
            });
        } else {
            //   渠道行、报单行   不为一    展示 查询查询出来的 报单行、渠道行信息
            bankdailystatementService.searchdeclaration($scope.poundagesettlement, function (data) {
                $scope.declarationbankdailystatements = data;
                $scope.declarationTotal.nums = "共"+data.length+"笔";
                $scope.declarationTotal.limitAmountTotal = 0.0;
                $scope.declarationTotal.chargeTotal = 0.0;
                angular.forEach(data,function (item) {
                    $scope.declarationTotal.limitAmountTotal += item.limitAmount;
                    $scope.declarationTotal.chargeTotal += item.poundage;
                });
            });
            bankdailystatementService.searchchannel($scope.poundagesettlement, function (data) {
                $scope.channelbankdailystatements = data;
                $scope.channelTotal.nums = "共"+data.length+"笔";
                $scope.channelTotal.limitAmountTotal =0.0;
                $scope.channelTotal.chargeTotal = 0.0;
                angular.forEach(data,function (item) {
                    $scope.channelTotal.limitAmountTotal += item.limitAmount;
                    $scope.channelTotal.chargeTotal += item.poundage;
                });
            });
        }
        //查询合计
        bankdailystatementService.searchtotal($scope.poundagesettlement, "WHOLE",function (data) {
            $scope.wholeTotal = data;
        });
        bankdailystatementService.searchtotal($scope.poundagesettlement, "STAGES",function (data) {
            $scope.stagesTotal = data;
        });
        cashsourceService.getCashSource($scope.poundagesettlement.channelCashSourceId,function(data){
            $scope.selectedChannelCashSourceName = data.shortName;
        });
        cashsourceService.getCashSource($scope.poundagesettlement.declarationCashSourceId,function(data){
            $scope.selectedDeclarationCashSourceName = data.shortName;
        });

    };


});