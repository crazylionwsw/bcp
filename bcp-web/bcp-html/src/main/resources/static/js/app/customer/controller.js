/**
 * 客户管理
 */

'use strict';

app.controller('customerController',['$scope','customerService','orderService','appointpaymentService','cardealerService','carregistryService','$stateParams','$localStorage','$state','$modal',function($scope,customerService,orderService,appointpaymentService,cardealerService,carregistryService,$stateParams,$localStorage,$state,$modal){
    $scope.tableTitle = '客户管理';
    $scope.formTitle = '资料补全';
    $scope.modalTitle = "特殊情况说明";
    $scope.showReportImgDiv = false;
    /*列表*/
    $scope.customers  = [];//返回的列表数据
    $scope.customer = {};
    $scope.cardemand = {};
    $scope.order ={};
    $scope.appointpayment = {};
    $scope.carregistry = {};
    $scope.order = [];
    $scope.payment=[];
    $scope.pickupCar=[];
    $scope.carregistry=[];

    $scope.customerFilter = {};

    $scope.currentPage = 1;

    $scope.checked=false;

    $scope.init=function(){

        if($stateParams.cid) {
            customerService.getOne($stateParams.cid,function (data) {
                if(data) $scope.edit(data);
            })
        }else {
            $scope.changePage();
        }
    };

    /*分页功能*/
    $scope.changePage = function (pageIndex){
        if (pageIndex){
            $scope.currentPage = pageIndex;
        }
        if( $scope.customerFilter.name || $scope.customerFilter.identifyNo || $scope.customerFilter.cells){
            customerService.search($scope.customerFilter,$scope.currentPage-1,function (data){
                $scope.customers = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages=data.totalPages;//总页数
            })
        } else {
            customerService.getPageData($scope.currentPage-1,function(data){
                $scope.customers = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1 ;//当前页
                $scope.totalPages=data.totalPages;//总页数
            });
        }

        $localStorage.CustomerFilter = $scope.customerFilter;
        $localStorage.Status = $scope.status;
    };


    $scope.edit = function(customer, index){
        
        if (!$scope.checked) {
            //$scope.cardemands[index].selected = true;//高亮显示
            if ($scope.customer){
                $scope.customer = {};
            }

            customerService.getOne(customer.id,function (customerData) {
                $scope.customer = customerData;
                $scope.checked = true;

                if($scope.customer.id){
                    var customerId = $scope.customer.id;
                    customerService.getCardemandByCustomerId(customerId,function (cardemandData) {
                        $scope.cardemand = cardemandData;

                        if (cardemandData.customerLoanId){
                            customerService.getLoan(cardemandData.customerLoanId, function(customerLoan){
                                $scope.cardemand.customerLoan = customerLoan;
                            })
                        };

                        //根据交易Id查询签约
                        if (cardemandData.customerTransactionId){
                            orderService.getOneByCustomerTransactionId(cardemandData.customerTransactionId, function(order){
                                $scope.order = order;
                                $scope.appointpayment.order = order;
                            })
                        };

                        //根据交易Id查询车辆上牌
                        if (cardemandData.customerTransactionId){
                            carregistryService.getOneByCustomerTransactionId(cardemandData.customerTransactionId, function(carregistry){
                                $scope.carregistry = carregistry;
                            })
                        };


                        //根据交易Id查询垫资
                        if (cardemandData.customerTransactionId){
                            appointpaymentService.getOneByCustomerTransactionId(cardemandData.customerTransactionId, function(appointpayment){
                                $scope.appointpayment = appointpayment;

                                orderService.getOneByCustomerTransactionId(cardemandData.customerTransactionId,function (order) {
                                    $scope.appointpayment.order = order;
                                });

                                if ($scope.appointpayment.carDealerId) {
                                    cardealerService.getOne($scope.appointpayment.carDealerId, function (carDealer) {
                                        $scope.appointpayment.carDealer = carDealer;

                                        if (!$scope.appointpayment.payAccount) {
                                            angular.forEach($scope.appointpayment.carDealer.payAccounts, function (item) {
                                                if (item.defaultAccount == 0) {
                                                    $scope.appointpayment.payAccount = item;
                                                }
                                            });
                                        }
                                    });
                                }
                            })
                        };

                    });
                }
            });
        }
    };


    /*        关闭  右边框      */
    $scope.close = function () {

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

    //浏览交易
    $scope.lookInfo = function (customer) {
        var name = customer.name;
        if (name && name != ''){
            $state.go('app.business.customertransaction',{cname:name});
        }
    };

    //查询
    $scope.search = function () {
        $scope.changePage(1);
    };

    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
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
        $scope.customerFilter={};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param customer
     */
    $scope.queryBill = function (customer) {
        if (customer.cell) {
            customer.cells = [customer.cell];
        }
        $scope.customerFilter = customer;
        $scope.changePage(1);
    }
    
    // $scope.edit = function(customer,index){
    //     if(!$scope.checked){
    //         customerService.getOne(customer.id,function (data) {
    //             $scope.customer = data;
    //             $scope.checked = true;
    //         })
    //     }
    // };

    // //关闭右边框
    // $scope.close = function () {
    //     $scope.checked = false;
    // };

    // $scope.onOpenSidePanel = function () {
    // };
    // $scope.onCloseSidePanel = function () {
    // };

    /* 业务类型添加 / 修改 */
    // $scope.openQueryDialog = function (){
    //     var modalInstance = $modal.open({
    //         scope:$scope,
    //         animation:true,
    //         templateUrl:'tpl/view/customer/query.html',
    //         controller:function ($scope,$modalInstance){
    //             $scope.modalTitle = "查询";
    //
    //             $scope.checkQuery = function(value) {
    //                 if ($scope.customer.queryString != null && $scope.customer.queryString.trim() != "") {
    //                     return true;
    //                 } else {
    //                     return false;
    //                 }
    //             };
    //
    //             $scope.updateQueryString = function () {
    //                 $scope.customer.queryString = '';
    //                 if ($scope.customer.name) $scope.customer.queryString += $scope.customer.name;
    //                 if ($scope.customer.identifyNo) $scope.customer.queryString += $scope.customer.identifyNo;
    //                 if ($scope.customer.cell) $scope.customer.queryString += $scope.customer.cell;
    //             }
    //             /*清楚查询内容*/
    //             $scope.clearQuery = function() {
    //                 $scope.changePage(1);
    //                 modalInstance.close();
    //             };
    //
    //             /*  关闭模态框 */
    //             $scope.cancel = function ($modalInstance){
    //                 modalInstance.close();
    //             }
    //
    //             /* 保存  修改 客户基本信息 */
    //             $scope.queryCustomers = function (){
    //                 if ($scope.customer.cell) {
    //                     $scope.customer.cells = [$scope.customer.cell];
    //                 }
    //                 var customer = $scope.customer;
    //                 var currentPage = $scope.currentPage;
    //                 customerService.search(customer,currentPage,function (data){
    //                     $scope.$parent.cardemands = data.content;
    //                     $scope.$parent.totalItems = data.totalElements;//数据总条数
    //                     $scope.$parent.pageSize = data.size;//分页单位
    //                     $scope.$parent.currentPage = data.number + 1;//当前页
    //                     $scope.$parent.totalPages=data.totalPages;//总页数
    //                 })
    //                 /* 操作完成自动关闭模态框 */
    //                 modalInstance.close();
    //             }
    //
    //         }
    //     });
    // }

    /**
     * 查询窗口
     */
    // $scope.showQuery = function () {
    //     $scope.queryModal = $modal.open({
    //         scope:$scope,
    //         animation:true,
    //         templateUrl:"query.html",
    //         controller:function($scope,$modalInstance){
    //             //we do nothing now!
    //         }
    //     })
    // }

    /**
     * 清空查询
     */
    // $scope.clearQuery = function() {
    //     $scope.changePage(1);
    // };

    /**
     * 查询单据
     * @param customer
     */
    // $scope.queryBill = function (customer) {
    //     $scope.customerFilter = customer;
    //
    //     if (customer.cell) {
    //         customer.cells = [customer.cell];
    //     }
    //
    //     $scope.currentPage = 1;
    //     customerService.search(customer,$scope.currentPage-1,function (data){
    //         $scope.customers = data.result;
    //         $scope.totalItems = data.totalCount;//数据总条数
    //         $scope.pageSize = data.pageSize;//分页单位
    //         $scope.currentPage = data.currentPage + 1 ;//当前页
    //         $scope.totalPages=data.totalPages;//总页数
    //     })
    // };

    /**
     * 删除用户
     */
    // $scope.delete = function (customer,index){
    //        customerService.remove(customer, function (data) {
    //            $scope.customers.splice(index);
    //        })
    // };

    /**
     * 模糊查询
     */
    // $scope.searchCustomer = function (){
    //
    //     customerService.search($scope.search,function (data){
    //
    //         $scope.customers = data.result;
    //         $scope.totalItems = data.totalCount;//数据总条数
    //         $scope.pageSize = data.pageSize;//分页单位
    //         $scope.currentPage = data.currentPage + 1 ;//当前页
    //         $scope.totalPages=data.totalPages;//总页数
    //     })
    // }
    // $scope.changeReportShow = function(){
    //     $scope.showReportImgDiv = !$scope.showReportImgDiv;
    // }
    //
    // //      查看  客户的全部资料 以及      资料中的图片数据
    // $scope.query = function (customer) {
    //
    //     //查找该客户的签章信息
    //
    //     $state.go("app.business.customerdatacompletion",{id:customer.id});
    //
    // }
    //
    // //特殊情况说明
    // $scope.comment = function (){
    //     var modalInstance = $modal.open({
    //         scope:$scope,
    //         animation:true,
    //         templateUrl:'commentForm.html',
    //         controller:function ($scope,$modalInstance,$q){
    //
    //             $scope.save = function() {
    //                 customerService.saveCustomer($scope.customer, function (data) {
    //                     $scope.customer = data;
    //                 });
    //                 modalInstance.close();
    //             }
    //
    //             /*  关闭模态框 */
    //             $scope.cancel = function ($modalInstance){
    //                 modalInstance.close();
    //             }
    //
    //         }
    //     });
    // }

}]);
