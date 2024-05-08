/**
 * Created by LB on 2016-10-24.
 */
'use strict';
app.controller('decompressController', function ($scope,decompressService,workflowService,orderService,customerService,cardealerService,sysparamService,customerTransactionService,$modal,toaster,$localStorage,$stateParams,$loading,$rootScope){
    $scope.title = "解押管理";

    $scope.decompresss = [];//返回的列表数据
    $scope.decompress = {};

    // //查询参数
    $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A031',advancedPay:0};
    $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'status',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A031',advancedPay:0};

    $scope.currentPage = 1;
    $scope.checked = false;


    /*
    * 初始化方法*/
    $scope.init = function () {
        if ($localStorage.DecompressFilter){
            $scope.searchFilter = $localStorage.DecompressFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        $scope.changePage(1);

        if ($stateParams.tid){//从交易列表打开
            decompressService.create($stateParams.tid,function (data) {
                if (data){
                    $scope.edit(data,data.dataStatus);
                } else {
                    alert("分期经理未提交该阶段数据！");
                }
            })
        };

        //收款账户
        sysparamService.getListByCode("PAYMENTBILL_ACCOUNT",function (data) {
            $scope.receiptAccounts = data;
        });
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){

        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        decompressService.search($scope.searchFilter,$scope.currentPage-1,function (data){
            $scope.decompresss = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.decompresss, function(bills){
                $scope.decompresss = bills;
            });
        });
        $localStorage.DecompressFilter = $scope.searchFilter;
    };

    //保存解押单
    $scope.save = function (decompress) {

        if (decompress.decompressAmount == "" || decompress.decompressAmount == null){
            alert("解押费用为必填！");
        }else if (decompress.decompressTime == "" || decompress.decompressTime == null){
            alert("解押时间为必填！");
        }else if (decompress.decompressType == "" || decompress.decompressType == null){
            alert("解押类型为必填！");
        }else if (decompress.paymentType == "" || decompress.paymentType == null){
            alert("支付方式为必填！");
        }else if (decompress.receiptAccount == "" || decompress.receiptAccount == null){
            alert("收款账户为必填！")
        }else {
            decompressService.save(decompress,function (decom) {
                if (decom){
                    toaster.pop('success','解押单创建成功！');
                    $scope.close();
                    $scope.changePage(1);
                }else {
                    toaster.pop('danger','解押单创建失败！');
                }
            });
        }
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
    $scope.edit = function (decompress,index) {
        if (!$scope.checked){
            if ($scope.decompress){
                $scope.decompress = {};
            }
            decompressService.getOne(decompress.id,decompress.dataStatus,function (de) {
                $scope.decompress = de;
                $scope.checked = true;

                if ($scope.decompress.customerTransactionId) {
                    var customerTransactionId = $scope.decompress.customerTransactionId;
                    customerTransactionService.getOne(customerTransactionId,function (transaction) {
                        $scope.decompress.transaction = transaction;
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
                if ($scope.decompress.carDealerId) {
                    cardealerService.getOne($scope.decompress.carDealerId, function (carDealer) {
                        $scope.decompress.carDealer = carDealer;
                    });
                };
                if($scope.decompress.customerId){
                    customerService.getOne($scope.decompress.customerId,function (customer) {
                        $scope.decompress.customer = customer;
                    })
                };

            })
        }
    };

    $scope.init();
    
    /*        关闭  右边框      */
    $scope.close = function () {

        //更新列表数据
        decompressService.getOne($scope.decompress.id,$scope.decompress.dataStatus,function (data) {
            angular.forEach($scope.decompresss, function(item, index){
                if (item.id == $scope.decompress.id) {
                    if ($scope.searchFilter.statusValue == -1){
                        $scope.decompresss[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.status){
                        $scope.decompresss.splice(index,1)
                    } else {
                        $scope.decompresss[index] = data;
                    }
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.decompresss[index].transaction = transaction;
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
        $scope.searchFilter = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'status',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A031',advancedPay:0};
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


    //  单据审批
    $scope.$on('BillSign',function (e,id,signInfo,presentTaskGroup) {
        $loading.start('signInfo');
        decompressService.sign(id, signInfo, function (data) {
            if(data.m){
                alert(data.m);
            }
            $loading.finish('signInfo');
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        });
    });

    $scope.$on('UpdateSignInfo', function (e, id) {
        decompressService.getOne(id, $scope.decompress.dataStatus,function (data) {
            $scope.decompress.approveStatus = data.approveStatus;
            //更新当前任务
            workflowService.getTaskCurrentGroup(data.billTypeCode + '.' + data.id, function (data) {
                $scope.presentTask = data;
            });
        });
    });

});