/**
 * Created by LB on 2016-10-24.
 */

'use strict';  

app.controller('dashboardController',['$scope','$stateParams','dashboardService','loginService','localStorageService', '$rootScope', '$localStorage', function ($scope, $stateParams, dashboardService, loginService, localStorageService, $rootScope, $localStorage){
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

    $scope.init = function () {
        dashboardService.queryDailyReport('A001', function (data) {
            $scope.dailyCarDemand = data;
        });
        dashboardService.queryDailyReport('A002', function (data) {
            $scope.dailyOrder = data;
        });
        dashboardService.queryDailyReport('A004', function (data) {
            $scope.dailyCarPayment = data;
        });
        dashboardService.queryDailyReport('A005', function (data) {
            $scope.dailyCarRegiarty = data;
           
        });
        dashboardService.queryDailyReport('A008', function (data) {
            $scope.dailyDmvpledge = data;
        });
        dashboardService.queryDailyReport('A011', function (data) {
            $scope.dailyBankcard = data;
        });
        dashboardService.queryDailyReport('A023', function (data) {
            $scope.dailyCarTransfer = data;
        });
        dashboardService.queryDailyReport('A019', function (data) {
            $scope.dailySwipingCard = data;
        });
        

        dashboardService.queryDailyReportReapply('A001', function (data) {
            $scope.dailyCarDemandReapply = data;
        });
        dashboardService.queryDailyReportReapply('A002', function (data) {
            $scope.dailyOrderReapply = data;
        });
        dashboardService.queryDailyReportReapply('A004', function (data) {
            $scope.dailyCarPaymentReapply = data;
        })
    };
    $scope.init();
}]);


