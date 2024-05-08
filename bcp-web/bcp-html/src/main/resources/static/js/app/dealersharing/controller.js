/**
 * Created by lily on 2017-05-04.
 */
'use strict';
app.controller('dealersharingController', function ($scope,dealersharingService,cardealerService, sysparamService, $state,$stateParams,$modal,$cookieStore,$loading,$filter,toaster,$localStorage,$rootScope) {
    $scope.title = "渠道分成";
    $scope.tableTitle = "渠道分成汇总表";
    $scope.formTitle = "渠道分成";

    //返回的列表数据
    $scope.dealersharings = [];
    $scope.dealersharing = null;
    $scope.dealersharingDetails = [];

    $scope.dealerSharingLookUps = [];
    
    $scope.saleMonth = "";
    $scope.dealerId = "";
    $scope.orgId = "-1";
    
    $scope.calcLabel = "计 算";
    $scope.calcing = false;

    $scope.currentPage = 1;

    $scope.searchDate;

    $scope.dealerSharing = null;

    $scope.cardealers = [];

    $scope.orginfos = [];

    $scope.mainPartTypes = [
        {
            id:1,
            name:'渠道'
        },
        {
            id:2,
            name:'销售人员'
        },
        {
            id:3,
            name:'渠道经理'
        }
    ];

    $scope.init = function(){

        sysparamService.getListByCode('BUSINESS_ORGS',function (data) {
            $scope.orginfos = data;
        });
        $scope.changePage();
    }

    //更换时间
    $scope.changeDate = function(){
        $scope.searchData();
    };

    $scope.changeSumType = function () {
        $scope.resetDate();
    };

    $scope.changePage = function (){
        if ($scope.saleMonth == undefined)  $scope.saleMonth = '';
        dealersharingService.getDataByPage($scope.currentPage-1,$scope.saleMonth,$scope.orgId,function (data){
            $scope.dealersharings = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };

    //打开弹出层
    $scope.view = function(ds){
        if (!$scope.checked) {
            if ($scope.dealersharing){
                $scope.dealersharing = {};
            }
            $scope.dealerName = ds.dealerName;
            $scope.salemonth = ds.month;
            $scope.dealerSharing = ds;
            //$scope.reloadStatus($scope.groupid, $scope.salemonth);
            dealersharingService.getOne(ds.id,function (result) {
                $scope.dealerSharingLookUps = result;
                $scope.checked = true;
            });
        }
    }

    /*        关闭  右边框      */
    $scope.close = function () {
        //更新列表数据
        dealersharingService.getOneDealerSharing($scope.dealerSharing.id,function (data) {
            angular.forEach($scope.dealersharings,function (item) {
                if(item.id == $scope.dealerSharing.id){
                    item.status = data.status;
                }
            })
        })
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    $scope.resetDate=function () {
        var now = new Date();
        $scope.saleMonth =now.getFullYear() + "-" + (now.getUTCMonth() + 1);
        $scope.searchDate =  $scope.saleMonth;
        $scope.searchDate = $filter('momentDate')($scope.searchDate,'YYYY-MM');
    };

    $scope.searchData = function(){
        if(!$scope.searchDate) {
            $scope.resetDate();
        }
        $scope.saleMonth = $scope.searchDate;
        dealersharingService.getAll($scope.saleMonth,$scope.orgId,function (data) {
            $scope.dealersharings = data;
        })
    };

    $scope.searchDetail = function () {
        if ($scope.saleMonth == undefined)  $scope.saleMonth = '';
        dealersharingService.getDataByPage(0, $scope.saleMonth, $scope.orgId, function (data){
            $scope.dealersharings = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };

    $scope.chooseOrg = function(org, index) {
        $scope.orgId = org.id;
    };

    $scope.clearMonth = function () {
        $scope.saleMonth = "";
    }

    $scope.getAllData = function (){
        dealersharingService.getAll(function (data){
            $scope.dealersharings = data.result;
        });
    };

    $scope.calcSharing = function() {
        if (!confirm("确定重新生成数据吗?")) {
            return;
        }
        if($scope.saleMonth == null || $scope.saleMonth == "" || $scope.orgId == null){
            alert("请选择月份!");
            return;
        }
        $scope.calcing = true;
        dealersharingService.creatDealerDetail($scope.saleMonth,$scope.orgId,function(data){
            $scope.dealerdetail = data;
            if(!data){
                dealersharingService.getDataByPage($scope.currentPage-1,$scope.saleMonth,$scope.orgId,function (data){
                    $scope.dealersharings = data.result;
                    $scope.totalItems = data.totalCount;//数据总条数
                    $scope.pageSize = data.pageSize;//分页单位
                    $scope.currentPage = data.currentPage+1;//当前页
                    $scope.totalPages = data.totalPages;
                    $scope.calcing = false;
                    toaster.pop('success', '计算成功。');
                });
            }
        });

    }

    //状态确认保存
    $scope.confirmSave = function (dsup) {
        if(dsup.mainPartType != null){
            $scope.confirmStatus = null;
            if(dsup.status == 0){
                $scope.confirmStatus = 1;
            }else if(dsup.status == 1){
                $scope.confirmStatus = 2;
            }else if(dsup.status == 2){
                $scope.confirmStatus = 3
            }

            if(dsup.status == 0){
                if(confirm("对于该经销商的分成比例和分成金额是否已核实确定?")){
                    dealersharingService.saveInfo($scope.dealerSharing.id,$scope.dealerSharing.carDealerId,dsup.id,$scope.confirmStatus,dsup.sharingRatio,dsup.sharingAmount,dsup.mainPartType,function (data) {
                        if(data){
                            dsup.status = data.status;
                        }
                    });
                }
            }else{
                dealersharingService.saveInfo($scope.dealerSharing.id,$scope.dealerSharing.carDealerId,dsup.id,$scope.confirmStatus,dsup.sharingRatio,dsup.sharingAmount,dsup.mainPartType,function (data) {
                    if(data){
                        dsup.status = data.status;
                    }
                });
            }

        }else{
            alert("请选择分成对象!");
        }

    };
    
    //渠道结算
    $scope.balanceSave = function (ds) {
        if(confirm("确认结算当前经销商分成?")){
            dealersharingService.balanceDealerSharing(ds.id,function (data) {
                if(data){
                    angular.forEach($scope.dealersharings,function (item1) {
                        if(item1.id == data.id){
                            item1.status = data.status;
                        }
                    })
                }
            })
        }
    };

    $scope.refreshSharing = function (ds) {
        if (confirm("确定要重新计算?")) {
            dealersharingService.refrshDealerSharing(ds.carDealerId,ds.month,function (data) {
                if(data){
                    angular.forEach($scope.dealersharings,function (item1) {
                        if(item1.id == ds.id){
                            item1.status = data.status;
                            toaster.pop('success', '计算成功。');
                        }
                    })
                }
            })
        }
    }

    //修改分成比例的同时修改分成金额
    $scope.updateSharingRatio = function (ds) {
        var sharingRatio = ds.sharingRatio;
        var creditAmount = ds.creditAmount;
        var sharingAmount = ds.sharingAmount;
        ds.sharingAmount = parseFloat(((sharingRatio * creditAmount)).toFixed(2));
    };

    //修改分成金额的同时修改分成比例
    $scope.updateSharingAmount = function (ds) {
        var creditAmount = ds.creditAmount;
        var sharingAmount = ds.sharingAmount;
        ds.sharingRatio = parseFloat(( (sharingAmount/creditAmount)).toFixed(5))
    };

    //分成金额合计
    $scope.getSharingTotal=function(){
        var total = 0;
        var totalCount = {};
        for(var i = 0; i < $scope.dealerSharingLookUps.length; i++){
            if(totalCount[$scope.dealerSharingLookUps[i].cardealerId] == undefined){
                total +=  $scope.dealerSharingLookUps[i].totalSharing;
                totalCount[$scope.dealerSharingLookUps[i].cardealerId] = $scope.dealerSharingLookUps[i].cardealerId;
            }
        }
        return total;
    };

    $scope.init();

})