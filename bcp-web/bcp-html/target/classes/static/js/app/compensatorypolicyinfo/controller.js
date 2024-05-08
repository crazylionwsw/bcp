/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('compensatorypolicyinfoController',function ($scope,compensatorypolicyinfoService,$modal,$q){


    $scope.tableTitle = "贴息公式";
    $scope.tableDesc = "贴息公式";

    /*  列表 */
    $scope.formulas  = [];//返回列表数据
    $scope.formula = {};

    $scope.currentPage = 1;

    $scope.init = function (){
        $scope.changePage();
    };


    /**
     * 分页功能
     */
    $scope.changePage = function (){
        compensatorypolicyinfoService.getDataByPage($scope.currentPage-1,function (data){
            $scope.formulas = data.result;
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


    /* 添加 / 修改 */
    $scope.edit = function (formula){
        /*  判断id进行回显 */
        if(formula.id != "" && formula.id != null){
            $scope.modalTitle="编辑贴息公式";
            $scope.formula = angular.copy(formula);
        } else {
            $scope.modalTitle="添加贴息公式";
            $scope.formula = {dataStatus: 1};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'formulaModal.html',
            size:'lg',
            backdrop:'static',
            controller:function ($scope,$modalInstance){

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };
                $scope.fchat.replies = [{key: 0, value: ""}];

                // 初始化时由于只有1条回复，所以不允许删除
                $scope.fchat.canDescReply = false;
                if (_.keys($scope.formula.templateMap).length > 0) {
                    $scope.fchat.replies = [];
                }
                for (var key in $scope.formula.templateMap) {
                    $scope.fchat.replies.push({key: new Date().getTime(), value:{key: key, value:$scope.formula.templateMap[key]}});
                }
                /* 保存还款方式 */
                $scope.save = function (){
                    $scope.formula.templateMap = {};
                    for (var i = 0 ; i < $scope.fchat.replies.length; i++) {
                        $scope.formula.templateMap[$scope.fchat.replies[i].value.key] = $scope.fchat.replies[i].value.value
                    }
                    compensatorypolicyinfoService.save($scope.formula,function (data){
                        if (!$scope.formula.id) {
                            $scope.formulas.push(data);
                        } else {
                            var index = $scope.formulas.indexOf(formula);
                            $scope.formulas[index] = data;
                        }
                        $scope.formula = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }
            }
        });
    };


    /* 删除 */
    $scope.delete = function (formula,index) {
        var msg = "确定要作废此条数据？";
        if(formula.dataStatus == 9){
            msg = "确定要删除此条数据？";
        }
        if (confirm(msg)) {
            var id = formula.id;
            compensatorypolicyinfoService.delete(id, function (data) {
                $scope.init();
            });
        }
    };
    //恢复数据
    $scope.renew = function (formula,index) {
        if(confirm("确定要恢复该条数据?")){
            formula.dataStatus = 1;
            compensatorypolicyinfoService.save(formula,function (data) {
                if(formula.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    /**
     * 唯一性验证
     * @param propname
     * @param propval
     * @returns {*}
     */
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.formula;
            compensatorypolicyinfoService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    $scope.fchat = {};

    $scope.fchat.replies = [{key: 0, value: ""}];

    // 初始化时由于只有1条回复，所以不允许删除
    $scope.fchat.canDescReply = false;

    // 增加回复数
    $scope.fchat.incrReply = function($index) {
        $scope.fchat.replies.splice($scope.fchat.replies.length, 0,
            {key: new Date().getTime(), value: ""});   // 用时间戳作为每个item的key
        // 增加新的回复后允许删除
        $scope.fchat.canDescReply = true;
    };

    // 减少回复数
    $scope.fchat.decrReply = function($index) {
        // 如果回复数大于1，删除被点击回复
        if ($scope.fchat.replies.length > 1) {
            $scope.fchat.replies.splice($index, 1);
        }
        // 如果回复数为1，不允许删除
        if ($scope.fchat.replies.length == 1) {
            $scope.fchat.canDescReply = false;
        }
    };

    $scope.fchat.combineReplies = function() {
        var cr = "";
        for (var i = 0; i < $scope.fchat.replies.length; i++) {
            cr += "#" + $scope.fchat.replies[i].value;
        }
        cr = cr.substring(1);
        return cr;
    };
});