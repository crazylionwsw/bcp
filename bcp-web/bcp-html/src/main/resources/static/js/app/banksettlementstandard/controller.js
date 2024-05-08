/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('banksettlementstandardController',function ($scope, banksettlementstandardService,cashsourceService, sysparamService, $modal, $q){
    $scope.tableTitle = "支行分成比例管理";

    $scope.banksettlementstandards = [];//返回列表数据
    $scope.banksettlementstandard = {};

    $scope.customerimagetypes = [];//资料类型

    //合作支行
    $scope.cashSources = [];
    //  报单支行
    $scope.declarationCashSources =[];

    $scope.canSave = false;
    
    //列表
    $scope.init = function (){
        //资金来源列表
        cashsourceService.lookup(function (data) {
            $scope.cashSources = data;
        });
        sysparamService.getListByCode("DECLARATION_BANKS",function (data) {
            $scope.declarationCashSources = data;
        });
        banksettlementstandardService.getData(function (data){
            $scope.banksettlementstandards = data;
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();

    //添加或修改
    $scope.edit = function (banksettlementstandard){
        /*  判断id进行回显 */
        if(banksettlementstandard.id != "" && banksettlementstandard.id != null){
            $scope.modalTitle="编辑银行结算标准";
            $scope.banksettlementstandard = angular.copy(banksettlementstandard);
        } else {
            $scope.modalTitle="添加银行结算标准";
            $scope.banksettlementstandard = {dataStatus: 1,comment:""};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'banksettlementstandardForm.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance,$q){
                /*  关闭模态框 */
                $scope.cancel = function (){
                    modalInstance.close();
                };

                /* 保存业务信息 */
                $scope.save = function (){
                    banksettlementstandardService.save($scope.banksettlementstandard,function (data){
                        if (data.m){
                            alert(data.m);
                        } else {
                            if(banksettlementstandard.id !="" && banksettlementstandard.id != null){
                                var index = $scope.banksettlementstandards.indexOf(banksettlementstandard);
                                $scope.banksettlementstandards[index] = data;
                            }else{
                                $scope.banksettlementstandards.push(data);
                            }
                        }
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }
            }
        });
    };

    $scope.delete = function (banksettlementstandard, index) {
        var msg = "确定要作废数据?";
        if (banksettlementstandard.dataStatus == 9) {
            msg = "确定要删除数据?";
        }
        if (confirm(msg)){
            banksettlementstandardService.delete(banksettlementstandard,function (data) {
                if (banksettlementstandard.dataStatus == 9){
                    $scope.banksettlementstandards.splice(index,1);
                } else{
                   $scope.banksettlementstandards[index] = data;
                }

            })
        }
    };

    //恢复数据---启用
    $scope.renew = function (banksettlementstandard,index) {
        if(confirm("确定要恢复该条数据?")){
            banksettlementstandard.dataStatus = 1;
            banksettlementstandardService.save(banksettlementstandard,function (data) {
                if(banksettlementstandard.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    //开始日期大于结束日期
    $scope.checkEndDate = function(value) {
        if(value){
            if (moment(value).isAfter($scope.banksettlementstandard.startTime)) {
                return true;
            } else {
                return false;
            }
        }else{
            return true;
        }
    };

    //      选择  分成占比
    $scope.calculateProportion = function (declarationProportion) {
        if (declarationProportion) {
            $scope.banksettlementstandard.channelProportion = parseFloat((1 - declarationProportion).toFixed(2));
        }
    };

    $scope.checkBankSettlementStandard = function (banksettlementstandard) {
        if (banksettlementstandard.declarationId && banksettlementstandard.channelId){
            banksettlementstandardService.check(banksettlementstandard.channelId,banksettlementstandard.declarationId,function (data) {
                if (data == true){
                    alert("对不起！该渠道行与报单行的分成比例已经存在！请修改！");
                } else if (data == false) {
                    $scope.canSave = true;
                }
            })
        }
    }
});
