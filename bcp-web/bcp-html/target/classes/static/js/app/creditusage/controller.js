/**
 * Created by Administrator on 2016/12/29.
 */


'use strict';

app.controller('creditusageController',function ($scope,creditusageService,$modal,$q){
    $scope.title = "车辆用途";
    $scope.tableTitle = "车辆用途列表";
    /*  列表 */
    $scope.creditusages= [];//返回列表数据
    $scope.creditusage = {};

    $scope.init = function (){
        creditusageService.getCreditUsages(function (data){
            $scope.creditusages = data;//返回的数据
        })
    }

    /**
     * 执行初始化
     */
    $scope.init();

    //验证唯一
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.creditusage;
            creditusageService.checkUniqueCreditUsage(entity,propname,propval,function(data){
                if(data.code=='1'){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /* 车辆用途添加 / 修改 */
    $scope.edit = function (creditusage){
        /*  判断id进行回显 */
        if(creditusage.id != "" && creditusage.id != null){
            $scope.modalTitle="编辑车辆用途";
            var creditusageEdit=creditusage;
            $scope.creditusage = creditusageEdit;
        }else{
            $scope.modalTitle="添加车辆用途";
            $scope.creditusage = {};
            $scope.creditusage.dataStatus = 1;
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'creditUsageModal.html',
            controller:function ($scope,$modalInstance){

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }


                /* 保存业务信息 */
                $scope.save = function (){
                    var creditusage = $scope.creditusage;
                    creditusageService.saveCreditUsage($scope.creditusage,function (data){
                        $scope.creditusage = data;
                        if(creditusage.id =="" || creditusage.id == null){
                            $scope.creditusages.push(data);
                        }

                    });
                    /* 操作完成自动关闭模态框 */
                    modalInstance.close();
                }

            }
        });
    }

    /* 删除 */
    $scope.delete = function (creditusage,index) {
        if (confirm("确定要删除数据" + creditusage.code + "?")) {
            creditusage.dataStatus = 9;
            creditusageService.saveCreditUsage(creditusage, function (data) {
                $scope.creditusages.splice(index, 1);
                $scope.creditusage = {};
            });
        }
    }

});