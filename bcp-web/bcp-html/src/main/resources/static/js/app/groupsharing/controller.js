/**
 * Created by lily on 2017-05-04.
 */
'use strict';
app.controller('groupsharingController', function ($scope, groupsharingService, cardealerService, sysparamService, $state, $stateParams, $modal, $cookieStore, $loading, $filter, toaster, $rootScope, $localStorage) {
    $scope.title = "集团分成";
    $scope.tableTitle = "集团分成汇总表";
    $scope.formTitle = "集团分成";

    //返回的列表数据
    $scope.groupsharings = [];
    $scope.groupsharing = [];
    $scope.dealersharing = null;
    $scope.dealersharingDetails = [];

    $scope.saleMonth = "";
    $scope.dealerId = "";
    $scope.orgId = "";

    $scope.calcLabel = "计 算";
    $scope.calcing = false;

    $scope.groups = [];

    $scope.currentPage = 1;
    $scope.page = 1;

    $scope.groupId = "";

    $scope.groupSharingId = null;

    $scope.groupsharingValues = null;

    $scope.mainPartTypes = [
        {
            id: 1,
            name: '渠道'
        },
        {
            id: 2,
            name: '销售人员'
        },
        {
            id: 3,
            name: '渠道经理'
        }
    ];

    $scope.init = function () {

        cardealerService.lookupDealerGroups(function (data) {
            $scope.groups = data;
            $scope.groups.unshift({id:"",name:"全部"})
        });
        $scope.changePage();
    };

    $scope.clearMonth = function () {
        $scope.saleMonth = "";
    };

    $scope.changePage = function () {
        if ($scope.saleMonth == undefined)  $scope.saleMonth = '';
        groupsharingService.getDataByPage($scope.currentPage - 1,$scope.saleMonth,$scope.groupId, function (data) {
            $scope.groupsharings = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };

    $scope.chooseGroup = function (group, index) {
        $scope.groupId = group.id;
    };

    $scope.calcSharing = function () {
        if (!confirm("确定重新生成数据吗?")) {
            return;
        }
        if ($scope.saleMonth != null && $scope.saleMonth != "") {
            $scope.calcing = true;
            groupsharingService.getSharing($scope.groupId, $scope.saleMonth, function (data) {
                $scope.changePage();
                $scope.calcing = false;
                toaster.pop('success', '计算成功。');
            });
        } else {
            alert("请选择月份!");
        }

    }

    $scope.refreshSharing = function (gs) {
        if (confirm("确定要重新计算?")) {
            groupsharingService.getSharing(gs.dealerGroupId, gs.month, function (data) {
                $scope.changePage();
                toaster.pop('success', '计算成功。');
            });
        }
    }

    //打开弹出层
    $scope.view = function (gs) {
        if (!$scope.checked) {
            if ($scope.groupsharing) {
                $scope.groupsharing = {};
            }
            $scope.groupid = gs.dealerGroupId;
            $scope.salemonth = gs.month;
            $scope.groupSharingId = gs.id;
            $scope.reloadStatus($scope.groupid, $scope.salemonth);
            groupsharingService.getOne(gs.id, function (result) {
                $scope.groupsharingKeys = _.keys(result.data);
                $scope.groupsharingValues = result.data;
                $scope.checked = true;
            });
        }
    }

    $scope.searchDetail = function () {
        if ($scope.saleMonth == undefined)  $scope.saleMonth = '';
        groupsharingService.getDataByPage(0, $scope.saleMonth, $scope.groupId, function (data){
            $scope.groupsharings = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };


    /*        关闭  右边框      */
    $scope.close = function () {
        //更新列表数据
        $scope.checked = false;
        $scope.reloadData($scope.groupSharingId);
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    $scope.reloadData = function (groupSharingId) {
        groupsharingService.getGroupSharing(groupSharingId, function (data) {
            for (var i = 0; i < $scope.groupsharings.length; i++) {
                if (data.id == $scope.groupsharings[i].id) {
                    $scope.groupsharings[i]= data
                }
            }
        });
    }

    $scope.balanceGroupSharing = function (groupSharingId) {
        var r= confirm("确认结算当前集团分成？")
        if (r == true) {
            groupsharingService.balanceGroupSharing(groupSharingId, function (data) {
                for (var i = 0; i < $scope.groupsharings.length; i++) {
                    if (data.id == $scope.groupsharings[i].id) {
                        $scope.groupsharings[i].status= data.status
                    }
                }
            });
        }
    }

    $scope.reloadStatus = function (groupid, salemonth) {
        groupsharingService.getGroupSharingInfo(groupid, salemonth, function (data) {
            if (data) {
                $scope.statusMap = data.statuses;
            }
        })
    };

    $scope.getStatus = function (key) {
        return $scope.statusMap[key];
    };

    //修正保存
    $scope.updateSave = function (cardealerId, sharingDetailId, status, sharingRatio, sharingAmount) {
        if (confirm("对于该经销商的分成比例和分成金额是否已核实确定?")) {
            status = 1;
            $scope.mainPartType = 1;
            groupsharingService.saveInfo($scope.groupSharingId, cardealerId, sharingDetailId, status, sharingRatio, sharingAmount, $scope.mainPartType, function (data) {
                groupsharingService.getOne($scope.groupSharingId, function (result) {
                    $scope.groupsharingKeys = _.keys(result.data);
                    $scope.groupsharingValues = result.data;
                    $scope.checked = true;
                    $rootScope.$broadcast("UpdateGroupStatus", $scope.groupId, $scope.salemonth, cardealerId);
                });
                $scope.reloadStatus($scope.groupid, $scope.salemonth);
            })
        }
    };

    //核对保存
    $scope.checkSave = function (cardealerId, sharingDetailId, status, sharingRatio, sharingAmount) {
        status = 2;
        $scope.mainPartType = 1;
        groupsharingService.saveInfo($scope.groupSharingId, cardealerId, sharingDetailId, status, sharingRatio, sharingAmount, $scope.mainPartType = 1, function (data) {
            groupsharingService.getOne($scope.groupSharingId, function (result) {
                $scope.groupsharingKeys = _.keys(result.data);
                $scope.groupsharingValues = result.data;
                $scope.checked = true;
                $rootScope.$broadcast("UpdateGroupStatus", $scope.groupId, $scope.salemonth, cardealerId);
            });
            $scope.reloadStatus($scope.groupid, $scope.salemonth);
        })
    };

    //复核保存
    $scope.resetSave = function (cardealerId, sharingDetailId, status, sharingRatio, sharingAmount) {
        status = 3;
        $scope.mainPartType = 1;
        groupsharingService.saveInfo($scope.groupSharingId, cardealerId, sharingDetailId, status, sharingRatio, sharingAmount, $scope.mainPartType = 1, function (data) {
            groupsharingService.getOne($scope.groupSharingId, function (result) {
                $scope.groupsharingKeys = _.keys(result.data);
                $scope.groupsharingValues = result.data;
                $scope.checked = true;
                $rootScope.$broadcast("UpdateGroupStatus", $scope.groupId, $scope.salemonth, cardealerId);
            });
            $scope.reloadStatus($scope.groupid, $scope.salemonth);
        })
    };

    //结算保存
    $scope.countSave = function (cardealerId, sharingDetailId, status, sharingRatio, sharingAmount) {
        status = 4;
        $scope.mainPartType = 1;
        groupsharingService.saveInfo($scope.groupSharingId, cardealerId, sharingDetailId, status, sharingRatio, sharingAmount, $scope.mainPartType = 1, function (data) {
            groupsharingService.getOne($scope.groupSharingId, function (result) {
                $scope.groupsharingKeys = _.keys(result.data);
                $scope.groupsharingValues = result.data;
                $scope.checked = true;
                $rootScope.$broadcast("UpdateGroupStatus", $scope.groupId, $scope.salemonth, cardealerId);
            });
            $scope.reloadStatus($scope.groupid, $scope.salemonth);
        })
    };

    //修改分成比例的同时修改分成金额
    $scope.updateSharingRatio = function (groupsharing) {
        var sharingRatio = groupsharing.sharingRatio;
        var creditAmount = groupsharing.creditAmount;
        var sharingAmount = groupsharing.sharingAmount;
        groupsharing.sharingAmount = parseFloat(((sharingRatio * creditAmount)).toFixed(2));
    };

    //修改分成金额的同时修改分成比例
    $scope.updateSharingAmount = function (groupsharing) {
        var creditAmount = groupsharing.creditAmount;
        var sharingAmount = groupsharing.sharingAmount;
        // groupsharing.sharingRatio = sharingAmount/creditAmount;
        groupsharing.sharingRatio = parseFloat(( (sharingAmount / creditAmount)).toFixed(5))
    }


    $scope.init();

})