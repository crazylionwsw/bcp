/**
 * Created by admin on 2018/3/22.
 */
'use strict';
app.controller('overdueController', function ($scope,overdueService,workflowService,orderService,customerService,cardealerService,customerTransactionService,$modal,toaster,$localStorage,$stateParams,$loading,$rootScope){
    $scope.title = "逾期记录";

    $scope.overduerecords = [];//返回的列表数据
    $scope.overduerecord = {};

    // //查询参数
    $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:-1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A032',advancedPay:0};
    $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:-1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A032',advancedPay:0};

    $scope.currentPage = 1;
    $scope.checked = false;


    /*
     * 初始化方法*/
    $scope.init = function () {
        if ($localStorage.OverduerecordFilter){
            $scope.searchFilter = $localStorage.OverduerecordFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        $scope.changePage(1);

        if ($stateParams.tid){//从交易列表创建

            //      获取  获取客户签约信息
            orderService.getOneByCustomerTransactionId($stateParams.tid,function (order) {
                $scope.order = order;
                if (order && order.customerLoanId) {
                    customerService.getLoan(order.customerLoanId, function (loan) {
                        $scope.order.loan = loan;
                        overdueService.getOneByCustomerTransactionId($stateParams.tid,function (count) {
                            if(count != null && count >= $scope.order.loan.rateType.months){
                                alert("逾期记录已超过当笔交易的贷款期数，不能再创建逾期记录！")
                            }else {
                                overdueService.create($stateParams.tid,function (data) {
                                    if (data){
                                        $scope.edit(data,data.dataStatus);
                                    } else {
                                        alert("分期经理未提交该阶段数据！");
                                    }
                                })
                            }
                        });
                    })
                }
            });
        };
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){

        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        overdueService.search($scope.searchFilter,$scope.currentPage-1,function (data){
            $scope.overduerecords = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.overduerecords, function(bills){
                $scope.overduerecords = bills;
            });
        });
        $localStorage.OverduerecordFilter = $scope.searchFilter;
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

    /*编辑页面         START */
    $scope.edit = function (overduerecord,index) {
        if (!$scope.checked){
            if ($scope.overduerecord){
                $scope.overduerecord = {};
            }
            overdueService.getOne(overduerecord.id,overduerecord.dataStatus,function (de) {
                $scope.overduerecord = de;
                $scope.checked = true;

                if ($scope.overduerecord.customerTransactionId) {
                    var customerTransactionId = $scope.overduerecord.customerTransactionId;
                    customerTransactionService.getOne(customerTransactionId,function (transaction) {
                        $scope.overduerecord.transaction = transaction;
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
                }
                //取得经销商信息
                if ($scope.overduerecord.carDealerId) {
                    cardealerService.getOne($scope.overduerecord.carDealerId, function (carDealer) {
                        $scope.overduerecord.carDealer = carDealer;
                    });
                };
                if($scope.overduerecord.customerId){
                    customerService.getOne($scope.overduerecord.customerId,function (customer) {
                        $scope.overduerecord.customer = customer;
                    })
                };

            })
        }
    };

    //保存逾期记录
    $scope.save = function (overduerecord) {
        if (overduerecord.overdueTime == "" || overduerecord.overdueTime == null){
            alert("逾期时间为必填！");
        }else if (overduerecord.overdueAmount == "" || overduerecord.overdueAmount == null){
            alert("逾期金额为必填！")
        }else {
            var month = overduerecord.overdueTime.slice(0,7);
            overdueService.getOverdueByMonth(overduerecord.customerTransactionId,month,function (over) {
                if(over != null){
                    alert("当笔交易当月已经创建过逾期记录！");
                }else {
                    overdueService.save(overduerecord,function (decom) {
                            if (decom){
                                toaster.pop('success','逾期记录创建成功！');
                                $scope.close();
                                $scope.changePage(1);
                            }else {
                                toaster.pop('danger','逾期记录创建失败！');
                            }
                        });
                }
            });
        }
    };

    $scope.sendMind = function (overduerecord) {
        var oid = overduerecord.id;
        overdueService.sendMind(oid,function (data) {
            if(data){
                toaster.pop('success','逾期提醒发送完成！');
            }
        })
    };

    $scope.init();

    /*        关闭  右边框      */
    $scope.close = function () {

        //更新列表数据
        overdueService.getOne($scope.overduerecord.id,function (data) {
            angular.forEach($scope.overduerecords, function(item, index){
                if (item.id == $scope.overduerecord.id) {
                    if ($scope.searchFilter.statusValue == -1){
                        $scope.overduerecords[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.status){
                        $scope.overduerecords.splice(index,1)
                    } else {
                        $scope.overduerecords[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.overduerecords[index].transaction = transaction;
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
        $scope.searchFilter = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'approveStatus',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A032',advancedPay:0};
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

});