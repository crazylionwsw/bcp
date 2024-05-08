/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('carregistryController',function ($scope,carregistryService,orderService,customerService,cardealerService,customerTransactionService,documentService,$modal,$localStorage,$stateParams,$loading,$rootScope){

    $scope.title = "提车上牌";

    $scope.carregistries = [];//返回的列表数据
    $scope.carregistry = {};

    //查询参数
    $scope.search = {timeName:'registryDate',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:false,pageSize:20,billTypeCode:'A005'};
    $scope.searchFilter = {timeName:'registryDate',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:false,pageSize:20,billTypeCode:'A005'};
    
    $scope.checked = false;
    
    $scope.currentPage = 1;

    //贷款金额合计
    $scope.totalCreditAmount = 0.0;
    
    /**
     * 列表
     */
    $scope.init = function (){
        if ($localStorage.CarRegistryCustomerFilter){
            $scope.searchFilter = $localStorage.CarRegistryCustomerFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }

        $scope.changePage(1);
        if ($stateParams.tid) { //从交易列表打开
            carregistryService.getOneByCustomerTransactionId($stateParams.tid, function(data){
                if (data && data.dataStatus == 1){
                    $scope.edit(data);
                } else {
                    alert("分期经理未提交该阶段数据！");
                }
            })
        }
    };
    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){
        $scope.totalCreditAmount = 0.0;
        
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        carregistryService.search($scope.searchFilter,$scope.currentPage-1,function (data) {
            $scope.carregistries = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.carregistries, function (bills) {
                $scope.carregistries = bills;
                $rootScope.$broadcast("CalTotal", bills);
            });
        });
        $localStorage.CarRegistryCustomerFilter = $scope.searchFilter;
    };

    //发送广播，计算刷卡金额
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

    //修改
    $scope.edit = function(carregistry, index){
        if (!$scope.checked) {
            if ($scope.carregistry){
                $scope.carregistry = {};
            }
            carregistryService.getOne(carregistry.id,function (data) {
                $scope.carregistry = data;
                $scope.checked = true;
                if (data.customerTransactionId) {
                    var customerTransactionId = data.customerTransactionId;
                    customerTransactionService.getOne(customerTransactionId,function (transaction) {
                        $scope.carregistry.transaction = transaction;
                    });
                    //      获取  获取客户签约信息
                    orderService.getOneByCustomerTransactionId(customerTransactionId,function (order) {
                        $scope.carregistry.order = order;
                        if(order.carDealerId){
                            var carDealerId = order.carDealerId;
                            cardealerService.getOne(carDealerId,function (cardealer) {
                                $scope.carregistry.cardelaer = cardealer;
                            })
                        }
                    })
                }

                //获取提车经销商
                if(data.carDealerId){
                    var carDealerId = data.carDealerId;
                    cardealerService.getOne(carDealerId,function (tCarDealer) {
                        $scope.carregistry.tCarDealer = tCarDealer;
                    })
                }

                if(data.customerId){
                    var customerId = data.customerId;
                    customerService.getCustomerCarByCustomerId(customerId,function (car) {
                        $scope.carregistry.car = car;
                    })
                }
            });
        }
    };

    /*        关闭  右边框      */
    $scope.close = function () {
        //更新列表数据
        carregistryService.getOne($scope.carregistry.id,function (data) {
            angular.forEach($scope.carregistries, function(item, index){
                if (item.id == data.id) {
                    if($scope.searchFilter.statusValue == -1){
                        $scope.carregistries[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.approveStatus){
                        $scope.carregistries.splice(index,1);
                    }else {
                        $scope.carregistries[index] = data;
                    }

                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.carregistries[index].transaction = transaction;
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
     * 查询
     */
    $scope.search = function() {
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
        $scope.searchFilter = {timeName:'registryDate',creditMonths:0,compensatory:true,business:true,statusName:'approveStatus',statusValue:$scope.searchFilter.statusValue,nc:true,oc:false,pageSize:$scope.pageSize,billTypeCode:'A005'};
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

    /**
     *      接收广播，进行审批
     */
    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        carregistryService.sign(id, signinfo, function (data) {
            $loading.finish('signInfo');
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        });
    });

    //更新审批状态
    $scope.$on('UpdateSignInfo',function (e, id) {
        carregistryService.getOne(id,function (data) {
            $scope.carregistry.approveStatus = data.approveStatus;
        })
    });
});