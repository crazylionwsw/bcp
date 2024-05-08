/**
 * Created by admin on 2017-12-06.
 */

'use strict';

app.controller('reportController', function ($scope, $stateParams, reportService,$localStorage,sysparamService) {


    $scope.searchDate;
    $scope.searchMonth;

    $scope.dailyCarDemand = {};
    $scope.dailyOrder = {};
    $scope.dailyPickup = {};
    $scope.dailyCarPayment = {};
    $scope.dailyCarRegiarty = {};
    $scope.dailyDmvpledge = {};
    $scope.dailyBankcard = {};

    $scope.dailyCarDemandReapply = [];
    $scope.dailyOrderReapply = [];
    $scope.dailyCarPaymentReapply = [];

    $scope.dailyCarDemandWarning = [];
    $scope.dailyOrderWarning = [];
    $scope.dailyCarPaymentWarning = [];
    $scope.orgs = {};
    $scope.selectedOrg = {id:"-1"}  // -1 为“全部”
    $scope.sumType = false;//false-按照月统计  true-按照天统计

    $scope.init = function () {
        //系统参配项获取业务部门
        sysparamService.getListByCode("BUSINESS_ORGS",function(data){
            $scope.orgs = data;
        });
        $scope.searchData();
    };

    //更换时间
    $scope.changeDate = function(){
        $scope.searchData();
    };

    $scope.changeOrg = function(){
        $scope.searchData();
    };


    /**
     * 更换时间（月或天）
     */
    $scope.resetDate=function () {
        var now = new Date();
        if ($scope.sumType) {
            $scope.searchDate = now.getFullYear() + "-" + (now.getUTCMonth() + 1) + "-" + now.getUTCDate();
        } else {
            var month = now.getUTCMonth() + 1;
            month = (month < 10 ? '0' : '')  + month;
            $scope.searchMonth =now.getFullYear() + "-" + (month);
            $scope.searchDate =  $scope.searchMonth;
        }
    }

    $scope.searchData = function(){
        if(!$scope.searchDate) {
            $scope.resetDate();
        }
        if($scope.sumType==false){
            $scope.searchDate =  $scope.searchMonth;
        }
        reportService.queryDailyReport('A001',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailyCarDemand = data;
        })
        reportService.queryDailyReport('A002',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailyOrder = data;
        })
        reportService.queryDailyReport('A004',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailyCarPayment = data;
        })
        reportService.queryDailyReport('A005',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailyCarRegiarty = data;
        })
        reportService.queryDailyReport('A008',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailyDmvpledge = data;
        })
        reportService.queryDailyReport('A011',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailyBankcard = data;
        })



        reportService.queryDailyReport('A023',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailyCarTransfer = data;
        })
        reportService.queryDailyReport('A019',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailySwipingCard = data;
        })

        reportService.queryDailyReportReapply('A001',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailyCarDemandReapply = data;
        });
        reportService.queryDailyReportReapply('A002',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailyOrderReapply = data;
        });
        reportService.queryDailyReportReapply('A004',$scope.searchDate,$scope.selectedOrg.id, function (data) {
            $scope.dailyCarPaymentReapply = data;
        })

    }

    $scope.init();
});


