/**
 * Created by LB on 2016-10-24.
 */


'use strict';
app.controller('orderNewCarController', ['$scope', 'orderService', 'customerService', 'declarationService', 'sysparamService','poundagesettlementService', 'cashsourceService', 'workflowService','cardemandService','customerTransactionService','surveyService','questioncategoryService', '$state', '$modal', 'toaster', '$localStorage', '$stateParams','$loading','$rootScope','$q',
    function ($scope, orderService, customerService, declarationService, sysparamService, poundagesettlementService, cashsourceService, workflowService,cardemandService,customerTransactionService,surveyService,questioncategoryService, $state, $modal, toaster, $localStorage,$stateParams,$loading,$rootScope,$q) {

    $scope.title = '客户签约';
    /*列表*/
    $scope.orders = [];//返回的列表数据
    $scope.order = null;
    $scope.cardemand = null;
    $scope.cardemandSignShow = 0;

    //查询参数
    $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A002'};
    $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A002'};

    $scope.currentPage = 1;
    $scope.approveStatus = 1;

    //返回工作流查询集合
    $scope.taskList = [];
    $scope.istask = {};

    //    右滑  模态框   的展示选项
    $scope.checked = false;

    //      从系统参配项中 获取系统参数，用于页面下拉框数据展示
    $scope.bankChargePaymentWays = [];
    $scope.compensatoryWays = [];
    $scope.compensatoryMonths = [];
    $scope.sourceIncomeWays = [];
    $scope.receiptDatas = [];

    $scope.presentTask = null;

    //贷款金额合计
    $scope.totalCreditAmount = 0.0;
    //银行手续费合计
    $scope.totalBankFeeAmount = 0.0;
    //贷款服务费合计
    $scope.totalLoanServiceFee = 0.0;

    $scope.customertypes = [];

    /**
     * 初始化加载列表和分页
     */
    $scope.init = function () {

        $scope.customertypes = [
            {
                code:0,
                name:'非自雇人士'
            },
            {
                code:1,
                name:'自雇人士'
            }
        ];

        //  获取 客户资料交接单的材料清单
        sysparamService.getListByCode("CUSTOMER_INFORMATION_RECEIPT", function (data) {
            $scope.receiptDatas = data;
        });

        if ($localStorage.OrderSearchFilter){
            $scope.searchFilter = $localStorage.OrderSearchFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        $scope.changePage(1);
        if ($stateParams.tid) { //从交易列表打开
            orderService.getOneByCustomerTransactionId($stateParams.tid, function(data){
                if (data && data.dataStatus == 1){
                    $scope.edit(data);
                } else {
                    alert("分期经理未提交该阶段数据！");
                }
            })
        }
    };

    /*分页功能*/
    $scope.changePage = function (pageIndex) {

        //合计清零
        $scope.totalCreditAmount = 0.0;
        $scope.totalBankFeeAmount = 0.0;
        $scope.totalLoanServiceFee = 0.0;

        if (pageIndex) {
            $scope.currentPage = pageIndex;
        }
        orderService.search($scope.searchFilter,$scope.currentPage-1, function (data) {
            $scope.orders = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.orders, function(bills){
                $scope.orders = bills;
                $rootScope.$broadcast("CalTotal", bills);
            })
        });

        $localStorage.OrderSearchFilter = $scope.searchFilter;
    };

    //发送广播，计算贷款金额
    $scope.$on('CalTotal',function (e,bills) {
        if (bills){
            angular.forEach(bills,function (item, index) {
                customerService.getCustomerLoanByCustomerTransactionId(item.customerTransactionId, function (data) {
                    if (data) {
                        $scope.addTotal(data.creditAmount);
                    }
                });

                customerService.getCustomerLoanByCustomerTransactionId(item.customerTransactionId,function (data) {
                    if(data){
                        $scope.addBankFeeTotal(data.bankFeeAmount);
                    }
                });

                customerService.getCustomerLoanByCustomerTransactionId(item.customerTransactionId,function (data) {
                    if(data){
                        $scope.addLoanServiceFeeTotal(data.loanServiceFee);
                    }
                })
            })
        }
    });


    //贷款金额合计
    $scope.addTotal = function (amount) {
        if(amount)
            $scope.totalCreditAmount = $scope.totalCreditAmount+ amount;
    };
    //银行手续费合计
    $scope.addBankFeeTotal = function (amount) {
        if(amount)
            $scope.totalBankFeeAmount = $scope.totalBankFeeAmount+ amount;
    };
    //贷款服务费合计
    $scope.addLoanServiceFeeTotal = function (amount) {
        if(amount)
            $scope.totalLoanServiceFee = $scope.totalLoanServiceFee+ amount;
    };

    //     查看  客户签约详细
    $scope.edit = function (order) {

        if (!$scope.checked) {

            if ($scope.order) {
                $scope.order = {};
            }
            orderService.getOne(order.id, function (data) {
                $scope.order = data;
                $scope.checked = !$scope.checked;

                workflowService.getTaskCurrentGroup(data.billTypeCode + '.' + data.id, function (data) {
                    $scope.presentTask = data;
                });

                /*   查询  客户购车信息   */
                if (data.customerCarId) {
                    customerService.getCar(data.customerCarId, function (car) {
                        $scope.order.car = car;
                    })
                }
                /*    查询    客户交易信息  */
                if (data.customerLoanId) {
                    customerService.getLoan(data.customerLoanId, function (loan) {
                        $scope.order.loan = loan;
                    })
                }
                //查询资质信息
                if(data.customerTransactionId){
                    cardemandService.getOneByCustomerTransactionId(data.customerTransactionId,function (cardemand) {
                        $scope.order.cardemand = cardemand;
                    });
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.order.transaction = transaction;
                    });

                    poundagesettlementService.getMarketingCodeByCustomerTransactionId(data.customerTransactionId,function (poundageSettlement) {
                        $scope.order.poundageSettlement = poundageSettlement;
                        if (poundageSettlement && poundageSettlement.settlementCashSourceId){
                            cashsourceService.getCashSource(poundageSettlement.settlementCashSourceId, function (data) {
                                if (data != undefined){
                                    $scope.order.poundageSettlement.newMarketingCode = data.marketingCode;
                                }
                            })
                        }
                    })
                }

                //      查询客户信息
                if (data.customerId) {
                    customerService.getOne(data.customerId, function (customer) {
                        $scope.order.customer = customer;
                    })
                }

            })
        }
    };
    
    $scope.refresh = function (id) {
        orderService.refresh(id, function (data) {
            $scope.order.poundageSettlement = data;
        })
    };
    
    $scope.createFileNumber = function (transactionId) {
        customerTransactionService.createFileNumber(transactionId,function (data) {
            $scope.order.transaction.fileNumber = data.fileNumber;
        })
    };
        

    //  前往银行报批页面，填写报批数据
    $scope.goDeclaration = function (bill) {
        if (bill && bill.customerTransactionId){
            $state.go('app.business.declaration',{tid:bill.customerTransactionId});
        }
    };

    $scope.showSurvey = function () {
        if ($scope.order && $scope.order.customerTransactionId){
            surveyService.getSurveyResult($scope.order.customerTransactionId, function (data) {
                if (data){
                    var surveyModal = $modal.open({
                        scope: $scope,
                        animation: true,
                        size : 'lg',
                        templateUrl: "survey.html",
                        controller: function ($scope, $modalInstance,$filter) {
                            $scope.modalTitle = "客户问卷";
                            $scope.customerSurveyResults = data;
                            angular.forEach(data,function (item,index) {
                                questioncategoryService.getQuestionByCode(item.questionCode,function (question) {
                                    $scope.customerSurveyResults[index].question = question;
                                    if (question && question.questionType == 'checklist'){
                                        $scope.customerSurveyResults[index].answerContentList = _.values(item.answerContents);
                                    }
                                })
                            });

                            $scope.cancel = function () {
                                surveyModal.close();
                            }
                        }
                    })
                } else {
                    alert("对不起，该客户未分配调查问卷！如需分配，请前往资质审查页面为客户分配！");
                }
            })
        }
    };

    /*        关闭  右边框      */
    $scope.close = function () {
        orderService.getOne($scope.order.id,function (data) {
            angular.forEach($scope.orders, function(item, index){
                if (item.id == data.id) {
                    if($scope.searchFilter.statusValue == -1){
                        $scope.orders[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.approveStatus){
                        $scope.orders.splice(index,1);
                    }else {
                        $scope.orders[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.orders[index].transaction = transaction;
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
     * 执行初始化
     */
    $scope.init();

    /**
     * 审批状态查詢
     */
    $scope.search = function () {
        $scope.changePage(1);
    };

    $scope.changePageSize = function () {
        $scope.searchFilter.pageSize = $scope.pageSize;
        $scope.changePage(1);
    };

    /*          模糊查询        START           */
    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope: $scope,
            size:'lg',
            animation: true,
            templateUrl: "query.html",
            controller: function ($scope, $modalInstance) {
                //we do nothing now!
            }
        })
    };

    /**
     * 清空查询
     */
    $scope.clearQuery = function () {
        $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,creditMonths:0,statusName:'approveStatus',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A002'};
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

    /*          模糊查询        START           */
    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        orderService.sign(id, signinfo, function (data) {
            $loading.finish('signInfo');
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        });
    });

    $scope.$on('BillTemSign',function (e,id,temSignInfo,presentTaskGroup) {
        $loading.start('temSignInfo');
        orderService.temSign(id, temSignInfo, function (data) {
            if(data.m){
                alert(data.m);
            }
            $loading.finish('temSignInfo');
            $rootScope.$broadcast("ShowTemTask");
            $rootScope.$broadcast("UpdateTemSignInfo", id);
        });
    });
    

    /*   TODO:客户签约通过之后，需要提示风控人员是否需要补充资料 */
    //      接收客户签约审批的广播
    $scope.$on('UpdateTemSignInfo', function (e, id) {
        orderService.getOne(id, function (data) {
            $scope.order.approveStatus = data.approveStatus;
            //      审核通过  提示是否进行资料补全
            if (data && data.approveStatus == 2) {
                /*if (confirm("是否进行资料补全？")) {
                    $state.go("app.business.customeredit",{id:data.customerId});
                }*/
            }
        })
    });

    /*                     特殊情况说明                           START                      */
    $scope.comment = function () {
        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: 'commentForm.html',
            controller: function ($scope, $modalInstance, $q) {

                $scope.save = function () {
                    orderService.save($scope.order, function (data) {
                        $scope.order = data;
                    });
                    modalInstance.close();
                };

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance) {
                    modalInstance.close();
                };
            }
        });
    };
    /*                     特殊情况说明                           END                      */

    /**
     * TODO:    重新启动工作流
     */
    $scope.againFlow = function () {
        if ($scope.order.id) {
            workflowService.againFlow($scope.order.businessTypeCode, $scope.order.billTypeCode, $scope.order.id, function (data) {
                if (data != null) {
                    toaster.pop('success', '流程已启动完成');
                    $scope.order = data;
                    //获取当前任务组
                    workflowService.getTaskCurrentGroup($scope.order.billTypeCode + '.' + $scope.order.id, function (data) {
                        $scope.presentTaskGroup = data;
                    })
                }
            });
        }
    };

    $scope.showCarDemandSignInfos = function (value) {
        if (value == 0){
            $scope.cardemandSignShow = 1;
        } else {
            $scope.cardemandSignShow = 0;
        }
    };

    $scope.updateCustomerCells = function() {
        var cells = [];
        if ($scope.order.customer.cells.length > 0) {
            $scope.order.customer.cells.forEach(function(item){
                cells.push(item.text);
            })
        }
        $scope.order.customer.cells = cells;
        customerService.saveCustomer($scope.order.customer,function (data) {
            $scope.order.customer = data;
        })
    };
        
}]);