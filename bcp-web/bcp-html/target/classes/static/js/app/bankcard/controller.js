/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('bankcardController',function ($scope,$rootScope,bankcardService,workflowService,customerService,sysparamService,orderService,customerTransactionService,employeeService,orginfoService,$modal,$state,$stateParams,$cookieStore,$localStorage,toaster){

    $scope.title = "卡业务处理";
  
    //返回的列表数据
    $scope.bankcards = [];
    $scope.bankcard ={};
    //卡信息
    //$scope.customercard = {};
    //分期经理列表
    $scope.businessNames = [];
    $scope.sumType = false;
    $scope.cusShow = true;
    //查询参数
    $scope.search = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'status',statusValue:'0',nc:true,oc:true,pageSize:20,billTypeCode:'A011'};
    $scope.searchFilter = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'status',statusValue:'0',nc:true,oc:true,pageSize:20,billTypeCode:'A011'};
    
    //获取历史任务
    $scope.historyTasks = [];
    $scope.currentTask = null;
    
    $scope.bankcardMsg = "";

    $scope.statusList = [];
    $scope.selectedStatus = {id:"0"};

    $scope.currentPage = 1;
    $scope.checked = false;
    $scope.syncing = false;

    $scope.reasonList = [];


    /**
     * 列表
     */
    $scope.init = function (){
        if($stateParams.bankCardData){
            $scope.edit(bankCardData);
        }

        sysparamService.getStringByCode("BANKCARD_SWIPINGNAME",function (data) {
            $scope.swipingName = data;
        });

        //从缓存中获取数据
        if($localStorage.BankCardSearchFilter){
            $scope.searchFilter = $localStorage.BankCardSearchFilter;
            if ($scope.searchFilter.statusValue) $scope.selectedStatus = {id: $scope.searchFilter.statusValue};
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        sysparamService.getListByCode("BANK_CARD_STATUS",function(data){
            $scope.statusList = data;
        });

        $scope.changePage(1);

        if ($stateParams.tid) { //从交易列表打开
            bankcardService.getOneByCustomerTransactionId($stateParams.tid, function(data){
                if (data) $scope.edit(data);
            })
        }
    };

    /**
     * 分页功能 卡业务处理
     */
    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        bankcardService.search($scope.searchFilter,$scope.currentPage-1,function (data){
            $scope.bankcards = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages; //总页数

            customerTransactionService.mergeTransactions($scope.bankcards, function(bills){
                $scope.bankcards = bills;
            })
        });

        $localStorage.BankCardSearchFilter = $scope.searchFilter;
    };

    /**
     * 查看详情页面(右侧弹出层)
     * @param bankcard
     * @param index
     */
    $scope.edit = function(bankcard, index){
        if (!$scope.checked) {
            if ($scope.bankcard){
                $scope.bankcard = {};
            }

            bankcardService.getOne(bankcard.id,function (data) {
                $scope.bankcard = data;
                $scope.checked = true;
                /*//通过客户交易Id查询客户卡信息
                customerService.getOneCustomerCard(bankcard.customerTransactionId,function (data) {
                    if (data == undefined){
                        $scope.customercard = {};
                    }else{
                        $scope.customercard = data;
                    }
                });*/

                //获取当前任务
                workflowService.getTaskCurrentGroup($scope.bankcard.billTypeCode + '.'+ $scope.bankcard.id, function(data) {
                    $scope.currentTask = data;
                    $scope.cardButton();
                })
                //员工领卡（所有的分期经理）
                employeeService.getAllEmployee(function (allEmployee) {
                    $scope.businessNames = allEmployee;
                });
            });
        }
    };
    
    $scope.init();
    
    /**
     * 关闭 右边框
     */
    $scope.close = function () {
        bankcardService.getOne($scope.bankcard.id,function (data) {
            angular.forEach($scope.bankcards, function(item, index){
                if (item.id == data.id) {
                    if($scope.selectedStatus.id == -1){
                        $scope.bankcards[index] = data;
                    } else if ($scope.selectedStatus.id != data.status){
                        $scope.bankcards.splice(index,1);
                    } else {
                        $scope.bankcards[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.bankcards[index].transaction = transaction;
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

    /**
     * 更新状态
     */
    $scope.changeStatus = function(){
        $scope.searchFilter.statusValue = $scope.selectedStatus.id;
        $scope.changePage(1);
    };

    $scope.changePageSize = function () {
        $scope.searchFilter.pageSize = $scope.pageSize;
        $scope.changePage(1);
    };

    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
            size:'lg',
            animation:true,
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
        $scope.searchFilter = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'status',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A011'};
        $scope.selectedStatus = {id:$scope.searchFilter.statusValue};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param search
     */
    $scope.queryBill = function (search) {
        $scope.searchFilter = search;
        $scope.changePage(1);
    };

    /**
     * 判断任务按钮的显示状态
     */
    $scope.cardButton = function() {
        var data = $scope.currentTask;
        $scope.visible1 = true;
        $scope.visible2 = true;
        $scope.visible3 = true;
        $scope.visible4 = true;
        if(data == "代启卡" || data == "调额"){
            $scope.visible1 = false;
            $scope.visible3 = false;
            $scope.visible4 = false;
        }else if(data == "代刷卡" || data == "领卡" || data == "渠道领卡"|| data == "渠道刷卡"){
            $scope.visible2 = false;
        }else if(data =="" || data == null){
            $scope.visible1 = false;
            $scope.visible2 = false;
        }
        if(data == "代启卡" && $scope.bankcard.replaceActivateName == null){
            //领卡人默认为当前的分期经理
            orginfoService.getEmployee($rootScope.user.employee.id,function (fenqi) {
                if(fenqi){
                    $scope.bankcard.replaceActivateName = fenqi.username;
                }
            });
        }
        if((data == "领卡" || data == "渠道领卡") && $scope.bankcard.receiveCardName == null){
            //领卡人默认为客户本人
            customerService.getOne($scope.bankcard.customerId,function (cus) {
                if(cus){
                    $scope.bankcard.receiveCardName = cus.name;
                }
            });
        }

    };

    /**
     * 执行工作流
     * @param val
     */
    $scope.process = function(val) {
        $scope.syncing = true;
        var start = -1;
        var approveStatus = 0;
        var bankcard = $scope.bankcard;
        //var customercard = $scope.customercard;
        var flag = true;
        var currentTask = $scope.currentTask;
        if(val != 9){
            if(currentTask == "申请制卡"){
                bankcard.applyTime = moment().format('YYYY-MM-DD HH:mm:ss');
                bankcard.status = 1;
                approveStatus = val;
            }else if(currentTask == "取卡"){
                if(bankcard.customerCard != null && bankcard.customerCard.cardNo != null && bankcard.customerCard.expireDate != null && bankcard.customerCard.cvv != null ){
                    bankcard.takeTime = moment().format('YYYY-MM-DD HH:mm:ss');
                    bankcard.status = 2;
                    approveStatus = val;
                    /*//保存卡信息
                    customercard.customerTransactionId = bankcard.customerTransactionId;
                    customercard.customerId = bankcard.customerId;
                    customerService.saveCustomerCard(customercard,function (data) {
                        $scope.customercard = data;
                    })*/
                }else{
                    toaster.pop('error', '操作失败,请将卡信息录入完整');
                    $scope.syncing = false;
                    flag = false;
                }
            }else if(currentTask == "代启卡"){
                //代启卡成功的状态
                if(val == 1){
                    if(bankcard.customerCard != null && bankcard.customerCard.initPassword != null && bankcard.replaceActivateName != null){
                        bankcard.replaceActivateTime = moment().format('YYYY-MM-DD HH:mm:ss');
                        bankcard.status = 3;
                        approveStatus = 2;
                        bankcard.swipingMoney = bankcard.customerLoanBean.swipingAmount;
                        start = val;
                        /*//保存卡信息
                        customerService.saveCustomerCard(customercard,function (data) {
                            $scope.customercard = data;
                        })*/
                    }else{
                        toaster.pop('error', '操作失败,请将卡信息录入完整');
                        $scope.syncing = false;
                        flag = false;
                    }
                }else if(val == 0){//代启卡失败的状态
                    /*bankcard.replaceActivateTime = moment().format('YYYY-MM-DD HH:mm');
                    bankcard.replaceActivateName = null;
                    approveStatus = 2;
                    bankcard.status = 99;
                    start = val;*/
                    $scope.activatecard(bankcard);
                    $scope.syncing = false;
                    flag = false;
                    $scope.checked = true;
                }
            }else if(currentTask == "启卡"){
                if(bankcard.replaceActivateTime != null && bankcard.status == 99){
                    toaster.pop('error', '操作失败,代启卡失败后需要用户在PAD端进行自启卡操作！');
                    $scope.syncing = false;
                    flag = false;
                }else{
                    if(bankcard.customerCard != null && bankcard.customerCard.initPassword != null){
                        bankcard.activateTime = moment().format('YYYY-MM-DD HH:mm:ss');
                        bankcard.status = 3;
                        approveStatus = val;
                        /*//保存卡信息
                        customerService.saveCustomerCard(customercard,function (data) {
                            $scope.customercard = data;
                        })*/
                    }else{
                        toaster.pop('error', '操作失败,请将卡信息录入完整');
                        $scope.syncing = false;
                        flag = false;
                    }
                }

            }else if(currentTask == "调额"){
                if(val == 1){
                    /**
                     * 无论是受托支付还是贴息刷卡，调额都需要录入金额
                     */
                    if(bankcard.swipingMoney != null){
                        approveStatus = 2;
                        bankcard.changeAmountTime = moment().format('YYYY-MM-DD HH:mm:ss');
                        bankcard.status = 4;
                        bankcard.swipingName = $scope.swipingName;
                    }else{
                        toaster.pop('error', '操作失败,请将卡信息录入完整');
                        $scope.syncing = false;
                        flag = false;
                    }
                }else{
                    if(confirm("调额失败确认要重新制卡？取消则重新调额！")){
                        $scope.remakecard(bankcard);
                        $scope.syncing = false;
                        flag = false;
                        $scope.checked = true;
                    }else{
                        $scope.syncing = false;
                        flag = false;
                        $scope.checked = true;
                    }
                }

            }else if(currentTask == "渠道领卡"){
                toaster.pop('error', '操作失败，渠道领卡是需要预约刷卡单确认后自动完成该任务！');
                $scope.syncing = false;
                flag = false;
                $scope.checked = true;
                /*if(bankcard.receiveCardName != null ){
                    bankcard.receiveDiscountTime = moment().format('YYYY-MM-DD HH:mm');
                    bankcard.status = 6;
                    approveStatus = val;
                }else{
                    toaster.pop('error', '操作失败,请将卡信息录入完整');
                    $scope.syncing = false;
                    flag = false;
                }*/
            }else if(currentTask == "渠道刷卡"){
                toaster.pop('error', '操作失败，渠道刷卡是需要渠道刷卡单据确认后自动完成该任务！');
                $scope.syncing = false;
                flag = false;
                /*if(bankcard.swipingMoney != null && bankcard.swipingName != null && bankcard.defaultReimbursement != null && bankcard.firstReimbursement != null){
                    bankcard.swipingShopTime = moment().format('YYYY-MM-DD HH:mm');
                    bankcard.status = 5;
                    approveStatus = val;
                }else{
                    toaster.pop('error', '操作失败,请将卡信息录入完整');
                    flag = false;
                }*/
            }else if(currentTask == "代刷卡"){

                if(bankcard.swipingMoney && bankcard.swipingName != null && bankcard.defaultReimbursement != null && bankcard.firstReimbursement != null){
                    bankcard.swipingTrusteeTime = moment().format('YYYY-MM-DD HH:mm:ss');
                    bankcard.status = 5;
                    approveStatus = val;
                }else{
                    toaster.pop('error', '操作失败,请将卡信息录入完整');
                    $scope.syncing = false;
                    flag = false;
                }
            }else if(currentTask == "领卡"){
                if(bankcard.receiveCardName != null ){
                    bankcard.receiveTrusteeTime = moment().format('YYYY-MM-DD HH:mm:ss');
                    bankcard.status = 6;
                    approveStatus = val;
                }else{
                    toaster.pop('error', '操作失败,请将卡信息录入完整');
                    $scope.syncing = false;
                    flag = false;
                }
            }
        }else{
            $scope.cancelcard(bankcard);
            $scope.syncing = false;
            flag = false;
            $scope.checked = true;
            /*if (confirm("确定要销卡吗?")){
                approveStatus = val;
                bankcard.status = 9;
                bankcard.cancelCardTime = moment().format('YYYY-MM-DD HH:mm');
            }else{
                flag = false;
            }*/
        }
        if(flag){
            bankcardService.sign(bankcard,approveStatus,start,function (data) {
                toaster.pop('success', '操作成功');
                $scope.bankcard = data;
                $scope.checked = true;
                //获取当前任务
                workflowService.getTaskCurrentGroup($scope.bankcard.billTypeCode + '.'+ $scope.bankcard.id, function(data) {
                    $scope.currentTask = data;
                    $scope.cardButton();
                });
                $scope.syncing = false;
            })
        }
    };

    /**
     * 客户自启卡
     * @param bankcard
     */
    $scope.activatecard = function(bankcard){
        if(bankcard != null){
            var modalInstance = $modal.open({
                scope:$scope,
                animation:true,
                templateUrl:'tpl/view/bankcard/activatecard.html',
                controller:function ($scope,$modalInstance,$q){
                    bankcard.comment = null;
                    sysparamService.getListByCode("BANK_CARD_ACTIVATE",function(data){
                        $scope.reasonList = data;
                    });
                    $scope.close = function ($modalInstance){
                        modalInstance.close();
                    };

                    $scope.sure = function (bankcard) {
                        bankcard.replaceActivateTime = moment().format('YYYY-MM-DD HH:mm:ss');
                        bankcard.replaceActivateName = null;
                        bankcard.customerCard.initPassword = null;
                        var approveStatus = 2;
                        bankcard.status = 99;
                        var start = 0;
                        bankcardService.sign(bankcard,approveStatus,start,function (data) {
                            toaster.pop('success', '操作成功');
                            $scope.refesh(data)
                        })
                        modalInstance.close();

                    };
                }
            });
        }
    };

    /**
     * 销卡
     * @param bankcard
     */
    $scope.cancelcard = function(bankcard){
        if(bankcard != null){
            var modalInstance = $modal.open({
                scope:$scope,
                animation:true,
                templateUrl:'tpl/view/bankcard/cancelcard.html',
                controller:function ($scope,$modalInstance,$q){
                    bankcard.comment = null;
                    sysparamService.getListByCode("BANK_CARD_CANCELCARD",function(data){
                        $scope.reasonList = data;
                    });
                    $scope.close = function ($modalInstance){
                        modalInstance.close();
                    };

                    $scope.sure = function (bankcard) {
                        var approveStatus = 9;
                        bankcard.status = 9;
                        var start = -1;
                        bankcard.cancelCardTime = moment().format('YYYY-MM-DD HH:mm:ss');
                        bankcardService.sign(bankcard,approveStatus,start,function (data) {
                            toaster.pop('success', '操作成功');
                            $scope.refesh(data)
                        })
                        modalInstance.close();

                    };
                }
            });
        }
    };
    
    
    /**
     * 重新制卡
     * @param bankcard
     */
    $scope.remakecard = function(bankcard){
        if(bankcard != null){
            var modalInstance = $modal.open({
                scope:$scope,
                animation:true,
                templateUrl:'tpl/view/bankcard/remakecard.html',
                controller:function ($scope,$modalInstance,$q){
                    bankcard.comment = null;
                    sysparamService.getListByCode("BANK_CARD_REMAKECARD",function(data){
                        $scope.reasonList = data;
                    });
                    $scope.close = function ($modalInstance){
                        modalInstance.close();
                    };

                    $scope.sure = function (bankcard) {
                        bankcardService.remakecard(bankcard.id,bankcard.comment,function (data) {
                            $scope.refesh(data);
                        });
                        modalInstance.close();

                    };
                }
            });
        }
    };
        
    $scope.refesh = function (data) {
        $scope.bankcard = data;
        //获取当前任务
        workflowService.getTaskCurrentGroup($scope.bankcard.billTypeCode + '.'+ $scope.bankcard.id, function(data) {
            $scope.currentTask = data;
            $scope.cardButton();
        });
    }

    //客户和员工切换
    $scope.changeType = function () {
        if($scope.cusShow == true){
            $scope.cusShow = false;
        }else if($scope.cusShow == false){
            $scope.cusShow = true;
        }
        //领卡人的默认
        if($scope.cusShow == false){ //如果为员工领卡
            //领卡人默认为当前的分期经理
            orginfoService.getEmployee($scope.bankcard.employeeId,function (fenqi) {
                if(fenqi){
                    $scope.bankcard.receiveCardName = fenqi.username;
                }
            });
        }else {
            //领卡人默认为客户本人
            customerService.getOne($scope.bankcard.customerId,function (cus) {
                if(cus){
                    $scope.bankcard.receiveCardName = cus.name;
                }
            });
        }
    };

    $scope.clearReport = function (bankcard) {
        if(bankcard != null){
            if(confirm("将清除该笔交易中的所有征信报告,包含借款人征信报告,配偶征信报告,指标人征信报告,是否继续?")){
                bankcardService.realDeleteReport(bankcard,function (data) {
                    if(data != null){
                        toaster.pop('success', data);
                    }
                })
            }
        }
    };

});