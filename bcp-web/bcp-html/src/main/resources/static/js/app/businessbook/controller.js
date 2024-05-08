/**
 * Created by LB on 2016-10-18.
 */

'use strict';

app.controller('businessbookController',['$scope','businessbookService','$state','$modal','$localStorage','$stateParams','$filter',function($scope,businessbookService,$state,$modal,$localStorage,$stateParams,$filter){
    $scope.tableTitle = '业务台帐';

    $scope.searchDate;
    $scope.searchMonth;

    $scope.sumType = true;

    $scope.init=function(){
        $scope.searchData();
    };

    //更换时间
    $scope.changeDate = function(){
        $scope.searchData();
    };

    $scope.changeSumType = function () {
        $scope.resetDate();
    };

    /**
     * 更换时间（月或天）
     */
    $scope.resetDate=function () {
        var now = new Date();
        if ($scope.sumType) {
            $scope.searchDate = now.getFullYear() + "-" + (now.getUTCMonth() + 1) + "-" + now.getUTCDate();
        } else {
            $scope.searchMonth =now.getFullYear() + "-" + (now.getUTCMonth() + 1);
            $scope.searchDate =  $scope.searchMonth;
        }
        $scope.searchDate = $filter('momentDate')($scope.searchDate,'YYYY-MM-DD');
    };

    $scope.searchData = function(){
        if(!$scope.searchDate) {
            $scope.resetDate();
        }
        if($scope.sumType==false){
            $scope.searchDate =  $scope.searchMonth;
        }
        businessbookService.getAll($scope.searchDate, function (data) {
            $scope.businessbooks = data;
            if(data.length == 0){
                alert("暂无业务台账数据!")
            }
        })

    };

    //导出业务台账
    $scope.exportDetails = function(){
        $scope.searchTime();
    };
    $scope.searchTime = function(){
        if(!$scope.searchDate) {
            $scope.resetTime();
        }
        if($scope.sumType==false){
            $scope.searchDate =  $scope.searchMonth;
        }
        var selectTime = $scope.searchDate;
        businessbookService.getAll(selectTime, function (data) {
            if(data.length > 0){
                window.open('/json/businessbook/export?selectTime='+selectTime);
            }else {
                alert("暂无业务台账数据!");
            }
        })


    };
    $scope.resetTime=function () {
        var now = new Date();
        if ($scope.sumType) {
            $scope.searchTime = now.getFullYear() + "-" + (now.getUTCMonth() + 1) + "-" + now.getUTCDate();
        } else {
            $scope.searchMonth =now.getFullYear() + "-" + (now.getUTCMonth() + 1);
            $scope.searchTime =  $scope.searchMonth;
        }
    };



    $scope.init();
    

}]);
