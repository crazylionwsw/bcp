/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('bankpledgeController',function ($scope,bankpledgeService,$modal,$state,$stateParams,$filter){

    $scope.title = "银行抵押";
    $scope.tableTitle ="银行抵押列表";
    $scope.formTitle = "银行抵押";

    $scope.bankpledges = [];
    $scope.bankpledge = {};
    $scope.carregistry ={};

    /**
     * 列表
     */
    $scope.init = function (){
        if($stateParams.id) {
            bankpledgeService.getOne($stateParams.id,function (data) {
                $scope.bankpledge = data;
            });
        } else {
            $scope.changePage(1);
        }
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }

        bankpledgeService.getDataByPage(false,$scope.currentPage,function (data){
            $scope.bankpledges = data.content;//返回的数据
            $scope.totalItems = data.totalElements;//数据总条数
            $scope.pageSize = data.size;//分页单位
            $scope.currentPage = data.number + 1;//当前页
            $scope.totalPages = data.totalPages; //总页数
        });
    };
    $scope.init();

    /**
     * 保存
     */
    $scope.save = function (){
        var bankpledge = $scope.bankpledge;
        bankpledgeService.save(bankpledge, function (data){
            //上传成功，使用Toaster
        });
    }

    /**
     * 提交
     */
    $scope.submitBankPledge = function (){
        var bankpledge = $scope.bankpledge;
        bankpledgeService.save(bankpledge, $stateParams.id, function (data){
            $state.go("app.business.dmvpledge");
        });
    }

    $scope.edit = function(bankpledge){
        $state.go("app.business.bankpledgeedit",{id:bankpledge.id});
    }


    $scope.openQueryDialog = function (){
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'tpl/view/customer/query.html',
            controller:function ($scope,$modalInstance){

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }


                /* 保存业务信息 */
                $scope.queryCustomer = function (){
                    /* 操作完成自动关闭模态框 */
                    modalInstance.close();
                }

            }
        });
    }
});