/**
 * Created by LB on 2016-10-24.
 */

'use strict';
app.controller('creditreportController',['$scope','creditreportService','cardemandService','$state','$stateParams','$modal','FileUploader','$cookieStore','$loading', 
    function ($scope, creditreportService, cardemandService, $state,$stateParams,$modal,FileUploader,$cookieStore,$loading){
        
        $scope.title = "征信查询";
        $scope.tableTitle ="征信查询列表";
        $scope.formTitle = "征信查询";

        //返回的列表数据
        $scope.creditreportquerys = [];
        $scope.creditreportquery = null;

        //返回工作流查询集合
        $scope.taskList = [];
        $scope.istask = {};

        //查询参数
        $scope.customer = {};
        $scope.customerFilter = {};

        $scope.currentPage = 1;


        /**
         * 分页功能
         * @param pageIndex
         */
        $scope.changePage = function (){

            creditreportService.getPageData($scope.currentPage-1,$scope.creditreportquery.approveStatus,function(data){
                $scope.creditreportquerys = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1 ;//当前页
                $scope.totalPages=data.totalPages;//总页数
            });
        }


        $scope.init = function (){
            if($stateParams.id) {
                creditreportService.getOne($stateParams.id,function (data) {
                    $scope.creditreportquery = data;
                    /*creditreportService.getCarDemand($stateParams.id,function (data) {
                        $scope.creditreportquery.carDemand = data;
                    });*/
                });
            } else {
                $scope.creditreportquery = {dataStatus: 1, approveStatus: 1};
                $scope.changePage();
            }
        }

        $scope.init();

        /**
         * 查詢
         */
        $scope.search = function() {
            $scope.changePage();
        }

        /*$scope.openQueryDialog = function (){
            var modalInstance = $modal.open({
                scope:$scope,
                animation:true,
                templateUrl:'tpl/view/creditreport/query.html',
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
                    /!*清楚查询内容*!/
                    $scope.clearQuery = function() {

                        $scope.changePage(1);
                        modalInstance.close();
                    };

                    /!*  关闭模态框 *!/
                    $scope.cancel = function ($modalInstance){
                        modalInstance.close();
                    }

                    /!*查询信息*!/
                    $scope.queryCreditreportquerys = function () {
                        if ($scope.customer.cell) {
                            $scope.customer.cells = [$scope.customer.cell];
                        }
                        var customer = $scope.customer;
                        var currentPage = $scope.currentPage;
                        var approveStatus = $scope.creditreportquery.approveStatus;
                        creditreportService.search(customer,currentPage,approveStatus,function (data){
                            $scope.$parent.creditreportquerys = data.content;
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





        /!**
         * 上传征信报告
         * @param creditreportquery
         *!/
        $scope.save = function (creditreportquery){
            creditreportService.save(creditreportquery, function (data) {
                $scope.creditreportquery = data;

                //
                creditreportService.getCarDemand(data.id,function (data) {
                    $scope.creditreportquery.carDemand = data;
                });
            });
        };

        /!**
         * 提交征信报告
         * @param creditreportquery
         *!/
        $scope.submit = function (){
            var creditreportquery = $scope.creditreportquery;
            creditreportquery.loginUserId = $cookieStore.get("userID");
            creditreportService.submit($scope.creditreportquery.id, function (data) {
                $scope.creditreportquery = data;
                $state.go("app.business.creditreport");
            });
        };

        /!**
         * 审核窗口
         *!/
        $scope.audit = function () {

            $scope.signModal = $modal.open({
                scope:$scope,
                animation:true,
                templateUrl:"audit.html",
                controller:function($scope,$modalInstance){
                    //we do nothing now!
                }
            })

        };

        /!**
         * 审核/审批
         * @param cardemand
         *!/
        $scope.sign = function (signinfo){
            $loading.start('signInfo');

            var creditreportquery = $scope.creditreportquery;
            creditreportService.sign(creditreportquery, signinfo, function (data) {
                //
                $scope.creditreportquery = data;

                creditreportquery.carDemand.signInfos.push(signinfo);
                cardemandService.updateSignInfo(creditreportquery.carDemand, function(data){
                    $scope.creditreportquery.carDemand = data;
                });

                $loading.finish('signInfo');
                $scope.signModal.close();
            });
        };

        /!**
         * 查询窗口
         *!/
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

        /!**
         * 清空查询
         *!/
        $scope.clearQuery = function() {
            $scope.changePage(1);
        };

        /!**
         * 查询单据
         * @param customer
         *!/
        $scope.queryBill = function (customer) {
            $scope.customerFilter = customer;

            if (customer.cell) {
                customer.cells = [customer.cell];
            }

            var currentPage = 1;
            var approveStatus = $scope.creditreportquery.approveStatus;
            creditreportService.search(customer,approveStatus,currentPage,function (data){
                $scope.creditreportquerys = data.content;
                $scope.totalItems = data.totalElements;//数据总条数
                $scope.pageSize = data.size;//分页单位
                $scope.currentPage = data.number + 1;//当前页
                $scope.totalPages=data.totalPages;//总页数
            })
        }
        */
}]);