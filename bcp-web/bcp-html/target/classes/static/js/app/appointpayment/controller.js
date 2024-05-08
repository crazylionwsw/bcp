/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('appointpaymentController', function ($scope,appointpaymentService,appointswipingcardService,cardemandService,orderService,bankcardService,customerService,cardealerService,sysparamService,customerTransactionService,$modal,toaster,$localStorage,$stateParams,$loading,$rootScope){
    $scope.title = "垫资支付";

    $scope.appointpayments = [];//返回的列表数据
    $scope.appointpayment = {};

    // //查询参数
    $scope.search = {timeName:'payTime',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A004',advancedPay:0};
    $scope.searchFilter = {timeName:'payTime',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A004',advancedPay:0};
    //业务状态
    $scope.statusList = [];

    $scope.currentPage = 1;
    $scope.checked = false;

    //垫资项目
    $scope.paymentTypeShow = false;
    //垫资方式
    $scope.chargePartyShow = false;
    //贷款金额合计
    $scope.totalCreditAmount = 0.0;
    $scope.first = false;

    /*
    * 初始化方法*/
    $scope.init = function () {
        if ($localStorage.AppointPaymentCustomerFilter){
            $scope.searchFilter = $localStorage.AppointPaymentCustomerFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        $scope.changePage(1);

        if ($stateParams.tid){//从交易列表打开
            appointpaymentService.getOneByCustomerTransactionId($stateParams.tid,function (data) {
                if (data && data.dataStatus == 1){
                    $scope.edit(data);
                } else {
                    alert("分期经理未提交该阶段数据！");
                }
            })
        }
    };


    //支付金额合计
    $scope.getPayTotal=function(){
        var total = 0;
        for(var i = 0; i < $scope.appointpayments.length; i++){
            total +=  $scope.appointpayments[i].appointPayAmount;
        }
        return total;
    };


    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){
        $scope.totalCreditAmount = 0.0;

        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        appointpaymentService.search($scope.searchFilter,$scope.currentPage-1,function (data){
            $scope.appointpayments = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.appointpayments, function(bills){
                $scope.appointpayments = bills;
                $rootScope.$broadcast("CalTotal", bills);
            });
        });
        $localStorage.AppointPaymentCustomerFilter = $scope.searchFilter;
    };

    //发送广播，计算贷款金额
    $scope.$on('CalTotal',function (e,bills) {
        if (bills){
            angular.forEach(bills,function (item, index) {
                customerService.getCustomerLoanByCustomerTransactionId(item.customerTransactionId, function (data) {
                    if (data) {
                        $scope.addCreditAmountTotal(data.creditAmount);
                    }
                })
            })
        }
    });

    //贷款金额合计
    $scope.addCreditAmountTotal = function (amount) {
        if(amount)
            $scope.totalCreditAmount = $scope.totalCreditAmount+ amount;
    };

    /**
     * 更新状态
     */
    $scope.changeStatus = function(){
        $scope.searchFilter.statusValue = $scope.selectedStatus.id;
        $scope.changePage(1);
    };

    /**
     * 查询
     */
    $scope.search = function() {
        $scope.changePage(1);
    };

    $scope.changePageSize = function () {
        $scope.searchFilter.pageSize = $scope.pageSize;
        $scope.changePage(1);
    };

    /*                                  编辑页面         START */
    $scope.edit = function (appointpayment,index) {
        if (!$scope.checked){
            if ($scope.appointpayment){
                $scope.appointpayment = {};
            }
            if ($scope.order){
                $scope.order = {};
            }
            if ($scope.carDealer){
                $scope.carDealer = {};
            }
            if ($scope.payAccount){
                $scope.payAccount = {};
            }

            appointpaymentService.getOne(appointpayment.id,function (appoint) {
                $scope.appointpayment = appoint;
                $scope.checked = true;

                if ($scope.appointpayment.customerTransactionId) {
                    var customerTransactionId = $scope.appointpayment.customerTransactionId;
                    customerTransactionService.getOne(customerTransactionId,function (transaction) {
                        $scope.appointpayment.transaction = transaction;
                    });
                    //      获取  获取客户签约信息
                    orderService.getOneByCustomerTransactionId(customerTransactionId,function (order) {
                        $scope.order = order;
                        if (order && order.customerLoanId) {
                            customerService.getLoan(order.customerLoanId, function (loan) {
                                $scope.order.loan = loan;
                            })
                        }
                    });

                    //获取客户预约刷卡信息
                    appointswipingcardService.getOneByCustomerTransactionId(customerTransactionId,function (appointswipingcard) {
                        $scope.appointswipingcard = appointswipingcard;
                    })
                }

                //判断当前交易是否为“首次垫资”
                var carDealerId = $scope.appointpayment.carDealerId;
                appointpaymentService.getCustomerTransactionsByCarDealerId(carDealerId,function (all) {
                    if(all[0].customerTransactionId == $scope.appointpayment.customerTransactionId){
                        $scope.first = true;
                    }
                });


                //取得经销商信息
                if ($scope.appointpayment.carDealerId) {
                    cardealerService.getOne($scope.appointpayment.carDealerId, function (carDealer) {
                        $scope.carDealer = carDealer;

                        if (!$scope.appointpayment.payAccount) {
                            angular.forEach($scope.carDealer.payAccounts, function (item) {
                                if (item.defaultAccount == 0) {
                                    $scope.payAccount = item;
                                }
                            });
                        } else {
                            $scope.payAccount = $scope.appointpayment.payAccount;
                        }
                    });
                }
            })
        }
    };

    $scope.init();
    
    /*        关闭  右边框      */
    $scope.close = function () {

        //更新列表数据
        appointpaymentService.getOne($scope.appointpayment.id,function (data) {
            angular.forEach($scope.appointpayments, function(item, index){
                if (item.id == $scope.appointpayment.id) {
                    if ($scope.searchFilter.statusValue == -1){
                        $scope.appointpayments[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.approveStatus){
                        $scope.appointpayments.splice(index,1)
                    } else {
                        $scope.appointpayments[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.appointpayments[index].transaction = transaction;
                    });
                }
            });
        });
        $scope.first = false;
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    /**
     *  保存
     */
    $scope.saveAppointPayment = function () {
        appointpaymentService.savePayTime($scope.appointpayment,function (data) {
            //$scope.appointpayment = data;
            if(data){
                toaster.pop('success', '更新成功!');
            }else{
                toaster.pop('danger', '更新失败!');
            }
        })
    };

    /*                                  编辑页面         END */


    /*                                  模糊查询            START     */
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


    $scope.exportBook = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
            size:'lg',
            animation:true,
            templateUrl:"exportBook.html",
            backdrop:'static',
            controller:function($scope,$modalInstance){
                //we do nothing now!
                $scope.closeExport = function () {
                    $scope.queryModal.close();
                }

                $scope.exportDo = function (selectTime) {
                    if(selectTime != null){
                        window.open('/json/appointpayments/export?selectTime='+selectTime);
                        //关闭导出框
                        $scope.queryModal.close();
                    }else {
                        alert("请先选择时间!");
                    }
                }
            }
        })
    };



    /**
     * 清空查询
     */
    $scope.clearQuery = function() {
        $scope.searchFilter = {timeName:'payTime',creditMonths:0,compensatory:true,business:true,statusName:'approveStatus',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A004',advancedPay:0};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param customer
     */
    $scope.queryBill = function (search) {
        $scope.searchFilter = search;
        $scope.changePage(1);
    };
    /*                                  模糊查询            END       */

    //  单据审批
    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {

        $loading.start('signInfo');
        appointpaymentService.sign(id,signinfo,function(data){
            $loading.finish('signInfo');
            if (id && presentTaskGroup && signinfo.result == 2){

                if (presentTaskGroup == "部门审批"){
                    //status = 2;
                    $scope.saveStatus(id,2);
                } else if (presentTaskGroup == "财务核对") {
                    //status = 3;
                    $scope.saveStatus(id,3);
                } else if (presentTaskGroup == "风控审批") {
                    //status = 4;
                    $scope.saveStatus(id,4);
                } else if (presentTaskGroup == "垫资支付") {
                    //status = 5;
                    $scope.saveStatus(id,5);
                }
            } else if (id && presentTaskGroup && signinfo.result == 8){
                $scope.saveStatus(id,0);
            }
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        })
    });
    
    $scope.saveStatus = function (id, status) {
        appointpaymentService.saveStatus(id,status,function (data) {
            if (data){
                $scope.appointpayment.status = data.status;
                if (data.payTime) {
                    $scope.appointpayment.payTime = data.payTime;
                }
            }
        })
    };

    $scope.openDate = function($event){
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };
    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1,
        class: 'datepicker'
    };

    /**
     * 日期控件
     * @param $event
     */
    $scope.openDateDialogStartDate = function($event){
        $event.preventDefault();
        $event.stopPropagation();
        $scope.startDateOpended = true;
    };
    $scope.dateOptionsStartDate = {
        formatYear: 'yy',
        startingDay: 1,
        class: 'datepicker'
    };
    $scope.openDateDialogEndDate = function($event){
        $event.preventDefault();
        $event.stopPropagation();
        $scope.endDateOpended = true;
    };
    $scope.dateOptionsEndDate = {
        formatYear: 'yy',
        startingDay: 1,
        class: 'datepicker'
    };

});