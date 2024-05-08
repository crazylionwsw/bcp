/**
 * Created by LB on 2016-10-24.
 */
'use strict';

app.controller('dmvpledgeController',function ($scope,dmvpledgeService,orderService,customerService,sysparamService,workflowService,customerTransactionService,documentService,$modal,$state,$rootScope,$localStorage,$stateParams,toaster){

    $scope.title = "车辆抵押";
    $scope.dmvpledges = [];//返回的列表数据
    $scope.dmvpledge = {};
    $scope.bankpledges = [];
    $scope.bankpledge = {};

    $scope.currentPage=1;

    //查询参数
    $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:0,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A008'};
    $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:0,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A008'};

    $scope.checked = false;

    $scope.statusList =[];
    $scope.selectedStatus = {id:"0"};

    //获取当前任务
    $scope.currentTask = null;
    $scope.currentTaskButton = null;
    $scope.syncing = false;

    /**
     * 列表
     */
    $scope.init = function () {
        sysparamService.getListByCode("DMVP_PLEDGE_STATUS",function(data){
            $scope.statusList = data;
        });
        if($localStorage.DMVPSearchFilter){
            $scope.searchFilter = $localStorage.DMVPSearchFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
            if ($scope.searchFilter.statusValue)  $scope.selectedStatus = {id:$scope.searchFilter.statusValue};
        }
        $scope.changePage(1);

        if ($stateParams.tid) { //从交易列表打开
            dmvpledgeService.getOneByCustomerTransactionId($stateParams.tid, function(data){
                if (data) $scope.edit(data);
            })
        }
    };

    /**
     * 更新本地查询缓存
     */
    $scope.updateCache = function(){
        $scope.searchFilter.statusValue = $scope.selectedStatus.id;
        $localStorage.DMVPSearchFilter = $scope.searchFilter;
    };

    /**
     * 分页功能 车辆抵押
     */
    $scope.changePage = function (pageIndex) {
        if (pageIndex) {
            $scope.currentPage = pageIndex;
        }
        dmvpledgeService.search($scope.searchFilter,$scope.currentPage-1,function (data){
            $scope.dmvpledges = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.dmvpledges, function(bills){
                $scope.dmvpledges = bills;
            })
        });

        $scope.updateCache();
    };

    $scope.edit = function(dmvpledge,index){
        if(!$scope.checked){
            if ($scope.dmvpledge){
                $scope.dmvpledge ={};
            }
            if ($scope.order){
                $scope.order = {};
            }
            dmvpledgeService.getOne(dmvpledge.id,function (data) {
                $scope.dmvpledge=data;
                $scope.checked=true;
                if (data&&data.customerTransactionId){
                    var customerTransactionId = data.customerTransactionId;
                    orderService.getOneByCustomerTransactionId(customerTransactionId,function (order) {
                        $scope.order = order;
                        if (order&&order.customerLoanId){
                            customerService.getLoan(order.customerLoanId, function (loan) {
                                $scope.order.loan = loan;
                            })
                        }
                    })
                }
                //清空完成的任务
                angular.forEach($scope.dmvpledge.actionRecords,function(item, index){
                    if($scope.currentTaskButton == item.action){
                        $scope.currentTaskButton = null;
                    }
                    if($scope.currentTask == item.action){
                        $scope.currentTask = null;
                    }
                });

                //获取当前任务
                workflowService.getTaskCurrentGroups($scope.dmvpledge.billTypeCode + '.'+ $scope.dmvpledge.id, function(data) {
                    if(data && data.length > 0 ){
                        angular.forEach(data, function(item, index){
                            if (item.name == "客户抵押资料签收") {
                                $scope.currentTaskButton = item.name;
                            }else {
                                $scope.currentTask = item.name;
                            }
                        });
                    }else{
                        $scope.currentTaskButton = null;
                        $scope.currentTask = null;
                    }
                })
            })
        }
    };

    //关闭右边框
    $scope.close=function () {
        dmvpledgeService.getOne($scope.dmvpledge.id,function (data) {
            angular.forEach($scope.dmvpledges, function(item, index){
                if (item.id == data.id) {
                    if($scope.selectedStatus.id == -1){
                        $scope.dmvpledges[index] = data;
                    } else if ($scope.selectedStatus.id != data.status){
                        $scope.dmvpledges.splice(index,1);
                    }else {
                        $scope.dmvpledges[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.dmvpledges[index].transaction = transaction;
                    });
                }
            });
        });
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    $scope.init();

    /**
     * 判断任务按钮的显示状态
     * @param data
     */
    /*$scope.cardButton = function() {
        var data = $scope.currentTask;
        $scope.visible = true;
        $scope.visible1 = true;
        if(data == "客户抵押资料签收"){
            $scope.visible = false;
            $scope.visible1 = false;
        }
    };*/

    /**
     * 执行工作流
     * @param val
     */
    $scope.process = function(flag) {
        $scope.syncing = true;
        var dmvpledge = $scope.dmvpledge;
        var userId = $rootScope.user.userID;
        var currentTask = $scope.currentTask;
        var currentTaskButton = $scope.currentTaskButton;
        dmvpledge.loginUserId = userId;
        if(flag == 1 && currentTaskButton == "客户抵押资料签收"){
            dmvpledge.status = 1;
            if(dmvpledge.pledgeDateReceiveTime == null || dmvpledge.pledgeDateReceiveTime == undefined){
                dmvpledge.pledgeDateReceiveTime = moment().format('YYYY-MM-DD HH:mm:ss');
            }
            $scope.currentTask = "客户抵押资料签收";
        } else if(flag == 0 && currentTask == "银行抵押合同打印"){
            dmvpledge.status = 2;
            if(dmvpledge.contractStartTime == null || dmvpledge.contractStartTime == undefined){
                dmvpledge.contractStartTime = moment().format('YYYY-MM-DD HH:mm:ss');
            }
        }else if(flag == 0 && currentTask == "银行抵押合同盖章"){
            dmvpledge.status = 3;
            if(dmvpledge.takeContractTime == null || dmvpledge.takeContractTime == undefined){
                dmvpledge.takeContractTime = moment().format('YYYY-MM-DD HH:mm:ss');
            }
        }else if(flag == 0 && currentTask == "车管所抵押开始"){
            dmvpledge.status = 4;
            if(dmvpledge.pledgeStartTime == null || dmvpledge.pledgeStartTime == undefined){
                dmvpledge.pledgeStartTime = moment().format('YYYY-MM-DD HH:mm:ss');
            }
        }else if(flag == 0 && currentTask == "抵押完成"){
            dmvpledge.status = 5;
            if(dmvpledge.pledgeEndTime == null || dmvpledge.pledgeEndTime == undefined){
                dmvpledge.pledgeEndTime = moment().format('YYYY-MM-DD HH:mm:ss');
            }
        }
        dmvpledgeService.sign(dmvpledge,function (data) {
            toaster.pop('success',   $scope.currentTask + "完成！");
            $scope.dmvpledge = data;
            //清空完成的任务
            angular.forEach($scope.dmvpledge.actionRecords,function(item, index){
                if($scope.currentTaskButton == item.action){
                    $scope.currentTaskButton = null;
                }
                if($scope.currentTask == item.action){
                    $scope.currentTask = null;
                }
            });
            //获取当前任务
            workflowService.getTaskCurrentGroups($scope.dmvpledge.billTypeCode + '.'+ $scope.dmvpledge.id, function(data) {
                if(data && data.length > 0 ){
                    angular.forEach(data, function(item, index){
                        if (item.name == "客户抵押资料签收") {
                            $scope.currentTaskButton = item.name;
                        }else {
                            $scope.currentTask = item.name;
                        }
                    });
                }else{
                    $scope.currentTaskButton = null;
                    $scope.currentTask = null;
                }

            });
            $scope.syncing = false;
        })

    };

    /**
     * 保存数据
     */
    $scope.update = function (){
        dmvpledgeService.saveDmvPledge($scope.dmvpledge,function (data){
            $scope.dmvpledge = data;
            toaster.pop('success',"保存抵押数据成功！");
        });
    };

    /*提交银行抵押信息*/
    $scope.submitBankPledge = function (){
        var bankpledge = $scope.bankpledge;
        bankpledge.loginUserId = $rootScope.user.userID;
        bankpledge.status = 8;
        dmvpledgeService.saveBankPledge(bankpledge,function (data){
            $state.go("app.business.dmvpledge");
        });
    };

    /*提交车管所抵押信息*/
    $scope.submitDmvPledge = function (){
        var dmvpledge = $scope.dmvpledge;
        dmvpledge.loginUserId = $rootScope.user.userID;
        dmvpledge.status = 8;
        dmvpledgeService.saveDmvPledge(dmvpledge,function (data){
            $state.go("app.business.dmvpledge");
        });
    };

    $scope.editDmvPledge = function(dmvpledge){
        $state.go("app.business.dmvpledgeedit",{id:dmvpledge.id});
    };

    $scope.editBankPledge = function(dmvpledge){
        $state.go("app.business.bankpledgeedit",{id:dmvpledge.id});
    };

    $scope.checkEndTime = function(value) {
        if (moment(value).isAfter($scope.bankpledge.pledgeStartTime)) {
            return true;
        } else {
            return false;
        }
    };
    $scope.checkDmvPledgeEndTime = function(value) {
        if (moment(value).isAfter($scope.dmvpledge.pledgeStartTime)) {
            return true;
        } else {
            return false;
        }
    };

    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
            animation:true,
            size:'lg',
            templateUrl:"query.html",
            controller:function($scope,$modalInstance){
                //we do nothing now!
            }
        })
    };

    /**
     * 清空查询
     */
    $scope.clearQuery = function() {
        $scope.searchFilter = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'status',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A008'};
        $scope.selectedStatus = {id:$scope.searchFilter.statusValue};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param customer
     */
    $scope.queryBill = function (search) {
        $scope.searchFilter = search;
        $scope.changePage(1)
    };

    //  根据 车辆抵押状态查询数据
    $scope.changeStatus = function(){
        $scope.searchFilter.statusValue = $scope.selectedStatus.id;
        $scope.changePage(1);
    };

    $scope.changePageSize = function () {
        $scope.searchFilter.pageSize = $scope.pageSize;
        $scope.changePage(1);
    };

});