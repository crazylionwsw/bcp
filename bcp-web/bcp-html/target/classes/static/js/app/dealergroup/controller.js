'use strict';

app.controller('dealerGroupController',function ($scope, cardealerService, sysparamService, $modal, $q){
    $scope.tableTitle = "经销商集团";
    $scope.tableDesc = "经销商集团";

    $scope.dealerGroups = [];//返回列表数据
    $scope.dealerGroup = {};

    $scope.currentPage = 1;

    $scope.showEdit = false;

    $scope.ratioShow = false;
    
    //列表
    $scope.init = function (){
        $scope.changePage();
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (){

        cardealerService.getDealerGroups($scope.currentPage-1, function (data){
            $scope.dealerGroups = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });

        sysparamService.getListByCode("DEALER_SHARING_RATIO",function(data){
            $scope.sharingRatioList = data;
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();

    $scope.chooseSharingRatio = function(item, index) {
        var keys = _.keys(item.ratio);
        $scope.sharingRatios = [];
        angular.forEach(keys, function(k){
            $scope.sharingRatios.push({'months': k, 'ratio': item.ratio[k]});
        });
        if($scope.sharingRatios.length > 0){
            $scope.ratioShow = true;
        }
    };

    $scope.saveSharingRatio = function() {
        var ratio = {};
        angular.forEach($scope.sharingRatios, function(item) {
            ratio[item.months] = item.ratio;
        });
        $scope.dealerGroup.sharingRatio = ratio;
        if($scope.dealerGroup.sharingRatio != null){
            $scope.ratioShow = false;
        }
    };
    
    
    //添加或修改
    $scope.edit = function (group){
        $scope.sharingRatios = [];
        $scope.ratioShow = false;
        /*  判断id进行回显 */
        if(group.id != "" && group.id != null){
            $scope.modalTitle="编辑";
            $scope.dealerGroup = group;

            if (group.sharingRatio) {
                var keys = _.keys(group.sharingRatio);
                angular.forEach(keys, function(k){
                    $scope.sharingRatios.push({'months': k, 'ratio': group.sharingRatio[k]});
                })
            }
        } else {
            $scope.modalTitle="添加";
            $scope.dealerGroup = {dataStatus: 1};
        }
        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: 'groupForm.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    $scope.changePage();
                    modalInstance.close();
                };
                
                /* 保存业务信息 */
                $scope.save = function (){
                    var keys = _.keys($scope.dealerGroup.sharingRatio);
                    if(keys.length == 0){
                        alert("如果新增经销商集团,请确保分成比例已确认或填写!");
                    }else {
                        var group = $scope.dealerGroup;
                        cardealerService.saveDealerGroup(group,function (data){
                            $scope.dealerGroup = data;
                            if(group.id =="" || group.id == null){
                                $scope.dealerGroups.unshift(data);
                            }
                            /* 操作完成自动关闭模态框 */
                            modalInstance.close();
                        });
                    }
                }
            }
        });
    };

    /* 删除 */
    $scope.delete = function (group,index) {
        var msg = "确定要作废此数据？";
        if (group.dataStatus == 9) {
            msg = "确定要删除此数据？";
        }
        if (confirm(msg)) {
            var id = group.id;
            cardealerService.deleteDealerGroup(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (group,index) {
        if(confirm("确定要恢复该条数据?")){
            group.dataStatus = 1;
            cardealerService.saveDealerGroup(group,function (data) {
                if(data.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    /**
     * 查看分成数据
     * @param group
     * @param index
     */
    $scope.sharing = function (group, index) {
        var msg = "确定要作废此数据？";
        if (group.dataStatus == 9) {
            msg = "确定要删除此数据？";
        }
        if (confirm(msg)) {
            var id = group.id;
            cardealerService.deleteDealerGroup(id, function (data) {
                $scope.init();
            });
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
            var entity = $scope.dealerGroup;
            cardealerService.checkUniqueDealerGroup(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
    
    $scope.addRatio = function () {
        $scope.ratioShow = true;
    }
  
});
