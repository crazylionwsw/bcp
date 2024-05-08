/**
 * Created by Lily on 2017-06-22.
 */

'use strict';
app.controller('creditphotographController',['$scope','creditphotographService','cardemandService','$state','$stateParams','$modal','FileUploader','$cookieStore','$loading','workflowService',
    function ($scope, creditphotographService, cardemandService, $state,$stateParams,$modal,FileUploader,$cookieStore,$loading,workflowService){

        $scope.title = "征信拍照";
        $scope.tableTitle ="征信拍照列表";
        $scope.formTitle = "征信拍照";

        //返回的列表数据
        $scope.creditphotographs = [];
        $scope.creditphotograph = null;

        //查询参数
        $scope.customer = {};
        $scope.customerFilter = {};

        $scope.currentPage = 1;

        /**
         * 分页列表
         * @param pageIndex
         */
        $scope.changePage = function (){
            if($scope.customerFilter.name || $scope.customerFilter.identifyNo || $scope.customerFilter.cell){
                creditphotographService.search( $scope.customerFilter, $scope.currentPage-1,$scope.creditphotograph.uploadFinish,function (data){
                    $scope.creditphotographs = data.result;
                    $scope.totalItems = data.totalCount;//数据总条数
                    $scope.pageSize = data.pageSize;//分页单位
                    $scope.currentPage = data.currentPage + 1;//当前页
                    $scope.totalPages=data.totalPages;//总页数
                })
            }else{
                creditphotographService.getPageData($scope.currentPage-1,$scope.creditphotograph.uploadFinish,function(data){
                    $scope.creditphotographs = data.result;
                    $scope.totalItems = data.totalCount;//数据总条数
                    $scope.pageSize = data.pageSize;//分页单位
                    $scope.currentPage = data.currentPage + 1 ;//当前页
                    $scope.totalPages=data.totalPages;//总页数
                });
            }

        }
        /**
         * 获取列表数据
         */
        $scope.init = function (){
            if($stateParams.id) {
                creditphotographService.getOne($stateParams.id,function (data) {
                    $scope.creditphotograph = data;
                });
            } else {
                $scope.creditphotograph = {dataStatus: 1, uploadFinish: "true"};
                $scope.changePage();
            }
        }
        /**
         * 初始化
         */
        $scope.init();
        //编辑
        $scope.edit = function (creditphotograph) {
            $state.go("app.business.creditphotographedit",{id:creditphotograph.id});
        }
        /**
         * 查询
         */
        $scope.search = function() {
            $scope.customerFilter = {};
            $scope.changePage(1);
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
         * 关闭模态框
         * @param $modalInstance
         */
        $scope.cancel = function ($modalInstance){
            modalInstance.close();
        }
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
        }


    }])