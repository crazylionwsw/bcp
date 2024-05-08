/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('sysparamController',function ($scope,sysparamService,$modal, toaster, $q){
    $scope.title = "系统参配项管理";

    $scope.modalTitle = "系统参数";

    $scope.sysparams = [];//返回列表数据
    $scope.sysparam = {};
    $scope.currentPage = 1;

    $scope.selectedSysParam = {};

    //禁用
    $scope.disForm = false;

    //列表
    $scope.init = function (){
        sysparamService.getAll(function (data){
            $scope.sysparams = data;//返回的数据
        });
    };
    /**
     * 分页功能
     */
    $scope.changePage = function (){

        sysparamService.getDataByPage($scope.currentPage-1,function (data){
            $scope.sysparams = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages ;
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
            var entity = $scope.sysparam;
            sysparamService.checkUnique(entity,propname,propval,function(data){
                if(data=='false'){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();

    /**
     * 添加
     */
    $scope.add = function(){
        $scope.disForm = false;
        //清除选定的系统参数
        if($scope.selectedSysParam) $scope.selectedSysParam.selected = false;
        $scope.sysparam = {datastatus:1};
        $scope.userForm.$setPristine();
    };

    /**
     * 选中
     * @param sysparam
     */
    $scope.selectSysParam = function (sysparam) {
        if ($scope.selectedSysParam) $scope.selectedSysParam.selected = false;
        sysparam.selected = true;
        $scope.selectedSysParam = sysparam;
        $scope.sysparam = angular.copy(sysparam);


        if(sysparam.dataStatus ==9){
            $scope.disForm = true;
        }else{
            $scope.disForm = false;
        }

    };

    /**
     * 删除系统参配项信息
     */
    $scope.discard = function (){
        var sysparam = $scope.selectedSysParam;
        if (sysparam.dataStatus == 1 ){
            if (confirm("确定要作废数据  " + sysparam.name + "  ?")) {
                sysparamService.delete(sysparam, function (data) {
                    var index = $scope.sysparams.indexOf(sysparam);
                    $scope.sysparams[index] = data;
                    $scope.sysparam = data;

                    $scope.selectSysParam($scope.sysparam);
                    toaster.pop('success','系统参数','作废成功');
                })
            }
        }else if(sysparam.dataStatus == 9){
            if (confirm("确定要删除数据  " + sysparam.name + "  ?")) {
                sysparamService.delete(sysparam, function (data) {
                    //  如果 返回的数据状态为 作废
                    var index = $scope.sysparams.indexOf(sysparam);
                    $scope.sysparams.splice(index, 1);
                    $scope.sysparam = {};
                    $scope.add();
                    toaster.pop('success','系统参数','删除成功');
                })
            }
        }
    };

    $scope.save = function () {
        var sysparam = $scope.sysparam;
        sysparamService.save(sysparam,function (data) {
            $scope.sysparam = data;

            if (sysparam.id == "" || sysparam.id == null){
                $scope.sysparams.push(data);
            }else {
                angular.forEach($scope.sysparams, function(item, index){
                    if (item.id == $scope.sysparam.id) {
                        $scope.sysparams[index] = $scope.sysparam;
                    }
                });
            }
            $scope.selectSysParam($scope.sysparam);
            toaster.pop('success','系统参数','保存成功');
        })
    };


    $scope.renew = function () {
        var sysparam = $scope.selectedSysParam;
        if(confirm("确定要恢复该条数据?")){
            sysparam.dataStatus = 1;
            sysparamService.save(sysparam, function (data) {
                if(data.dataStatus == 1){
                    var index = $scope.sysparams.indexOf(sysparam);
                    $scope.sysparams[index] = data;
                    $scope.sysparam = data;
                    /*选中当前*/
                    $scope.selectSysParam(sysparam);
                    toaster.pop('success','系统参数','恢复成功');
                }else{
                    alert("数据恢复失败!")
                }

            })
        }

    };


});