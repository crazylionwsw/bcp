/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('billtypeController',function ($scope, billtypeService,imagetypeService, documentService, $modal, $q){
    $scope.tableTitle = "单据类型管理";
    $scope.tableDesc = "单据类型管理";

    $scope.billtypes = [];//返回列表数据
    $scope.billtype = {};

    $scope.customerimagetypes = [];//资料类型
    $scope.documents = []; //合同
    $scope.currentPage = 1;
    //列表
    $scope.init = function (){
        $scope.changePage();

        imagetypeService.lookup(function (data){
            $scope.customerimagetypes = data;
        });

        documentService.lookup(function (data){
            $scope.documents = data;
        });
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (){
        
        billtypeService.getDataByPage($scope.currentPage-1,function (data){
            $scope.billtypes = data.result;
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

    //添加或修改
    $scope.edit = function (billtype){

        /*  判断id进行回显 */
        if(billtype.id != "" && billtype.id != null){
            $scope.modalTitle="编辑单据类型";
            $scope.billtype = billtype;
        } else {
            $scope.modalTitle="添加单据类型";
            $scope.billtype = {dataStatus: 1};
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'billtypeForm.html',
            backdrop:'static',
            size:'lg',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    $scope.changePage();
                    modalInstance.close();
                };
                
                /* 保存业务信息 */
                $scope.save = function (){
                    var billtype = $scope.billtype;
                    billtypeService.save(billtype,function (data){
                        $scope.billtype = data;
                        if(billtype.id =="" || billtype.id == null){
                            $scope.billtypes.push(data);
                        }
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }
            }
        });
    };

    /**
     * 唯一性验证
     * @param propname
     * @param propval
     * @returns {*}
     */
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.billtype;
            billtypeService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /* 删除 */
    $scope.delete = function (billtype,index) {
        var msg = "确定要作废此数据  ?";
        if (billtype.dataStatus == 9) {
            msg = "确定要删除此数据  ?";
        }
        if (confirm(msg)) {
            var id = billtype.id;
            billtypeService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (billtype,index) {
        if(confirm("确定要恢复该条数据?")){
            billtype.dataStatus = 1;
            billtypeService.save(billtype,function (data) {
                if(billtype.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };
  
});
