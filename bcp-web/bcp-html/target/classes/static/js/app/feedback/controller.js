/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('feedBackController',function ($scope, feedBackService, $modal, $q, $filter){
    
    $scope.tableTitle = "意见反馈";
    $scope.tableDesc = "意见反馈";

    $scope.feedbacks = [];//返回列表数据
    $scope.feedback = {}; //对象

    $scope.currentPage =  1;

    //列表
    $scope.init = function (){
        $scope.changePage();
    };
    /**
     * 分页功能
     */
    $scope.changePage = function (){
        feedBackService.getDataByPage($scope.currentPage-1,function (data){
            $scope.feedbacks = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();

    /* 删除 */
    $scope.delete = function (feedback,index) {
        var msg = "确定要作废此数据  ?";
        if (feedback.dataStatus == 9) {
            msg = "确定要删除此数据  ?";
        }
        if (confirm(msg)) {
            var id = feedback.id;
            feedBackService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (feedback,index) {
        if(confirm("确定要恢复该条数据?")){
            feedback.dataStatus = 1;
            feedBackService.save(feedback,function (data) {
                if(feedback.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };


    $scope.seeFeedBack = function (feedback) {

        /*  判断id进行回显 */
        if(feedback.id != "" && feedback.id != null){
            $scope.modalTitle="查看详细反馈内容";
            $scope.feedback = feedback;
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'seeFeedBackForm.html',

            controller:function ($scope,$modalInstance,$q){

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }

            }
        });
    }

});