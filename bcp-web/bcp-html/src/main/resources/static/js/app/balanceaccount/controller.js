/**
 * Created by zxp on 2017/11/9.
 */
'use strict';

app.controller('balanceaccountController',function ($scope,balanceaccountService,sysparamService,$modal,$rootScope,$state,$stateParams,$cookieStore,$localStorage,toaster,customerTransactionService){
    $scope.title = "银行对账";
    $scope.tableTitle ="银行对账列表";
    $scope.formTitle = "银行对账处理";
    //返回的列表数据
    $scope.balanceaccounts = [];
    $scope.balanceaccount = {};
    $scope.balanceaccount ={businessTypeCode:'NC'};
    $scope.balanceaccount.businessTypeCode = 'NC';
    $scope.balanceaccountdetails = [];
    $scope.balanceaccountdetail ={};

    $scope.balanceaccount.status=-1;
    var loginUserId = $rootScope.user.userID;


    /**
     * 列表
     */
    $scope.init = function (){
        if($stateParams.id) {
           
        } else {
            $scope.changePage(1);
        }
    }
    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        balanceaccountService.getDataByPage($scope.currentPage-1,$scope.balanceaccount.status,function (data){
            $scope.balanceaccounts = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages; //总页数
        });
    }
    $scope.init();


    /**
     * 右划框明细
     */
    $scope.checked=false;
    //弹出右划框
    $scope.view = function(balanceaccount,index){
        if(!$scope.checked){
            if ($scope.balanceaccount){
                $scope.balanceaccount = {};
            }
            balanceaccountService.getOne(balanceaccount.id,function (data) {
                if (data){
                    $scope.balanceaccount =data;
                }
                $scope.checked=true;
                balanceaccountService.getOneDetails(balanceaccount.id,function (data) {
                    $scope.balanceaccountdetails = data;
                    customerTransactionService.mergeTransactions($scope.balanceaccountdetails, function(bills){
                     $scope.balanceaccountdetails = bills;
                     })
                });
            });




        }
    }

    //关闭右边框
    $scope.close = function () {
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };


    $scope.change=function () {
        $scope.changePage(1);
    }
    
    $scope.toBankCard=function (balanceaccountdetail,index) {
        $state.go("app.business.bankcard", {tid:balanceaccountdetail.customerTransactionId});
    }
    $scope.toOrder=function (balanceaccountdetail,index) {
        $state.go("app.business.ordernewcar",{tid:balanceaccountdetail.customerTransactionId});
    }
    $scope.toPayment=function (balanceaccountdetail,index) {
        $state.go("app.business.appointpayment", {tid: balanceaccountdetail.customerTransactionId});
    }
    //结算费用
    $scope.account=function () {
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'tpl/view/balanceaccount/account.html',
            controller:function ($scope,$modalInstance,$q){

                $scope.counts = function (balanceaccount,$modalInstance) {
                    if (confirm("您确定要计算"+balanceaccount.year+"年"+balanceaccount.month+"月的银行结费？")) {
                        modalInstance.close();
                        balanceaccountService.countOne(balanceaccount.year, balanceaccount.month, loginUserId, function (data) {
                            if( data == undefined){
                                $scope.changePage(1);
                                toaster.pop('error', '计算本月结费为0.0，您可能还未在收款计划表中核对数据！');
                             }else {

                                if (data.m) {
                                    // $scope.balanceaccounts[index] = data.d;
                                    $scope.changePage(1);
                                    toaster.pop('error', data.m);
                                } else {
                                    $scope.changePage(1);
                                    toaster.pop('success', '计算本月结费完成。');
                                }
                           }
                        })
                    }
                };

                /*  关闭模态框 */
                $scope.close = function ($modalInstance){
                    modalInstance.close();
                };
            }
        });
    }
    //重算某月结费
    $scope.count=function (balanceaccount,index) {
        if(balanceaccount.checkStatus >= 1){
            alert("核对完成以后的数据不能重新计算！");
        }else {
            $scope.recount(balanceaccount,index);
        }
    }


    //重算某月结费
    $scope.recount=function (balanceaccount,index) {
        if(confirm("重算可能会占用您一两分钟的时间，您确定要重算？")){
            balanceaccountService.countOneMonth(balanceaccount.year,balanceaccount.month,loginUserId,function (data) {
                if(data.totalPaymentAmount ==0.0){
                    $scope.balanceaccounts[index] = data;
                    $scope.changePage(1);
                    toaster.pop('error', '重算本月结费为0.0，您可能还未在收款计划表中核对数据！');
                }else {
                    if (data.m) {
                        // $scope.balanceaccounts[index] = data.d;
                        $scope.changePage(1);
                        toaster.pop('error', data.m);
                    } else {
                        $scope.balanceaccounts[index] = data;
                        $scope.changePage(1);
                        toaster.pop('success', '重算本月结费完成。');
                    }
                }
            })
        }
    }



    $scope.export=function (balanceaccount,index) {
        /* alert(balanceaccount.year)*/
      //  alert("数据较多，时间较长，请稍等片刻。。。")
        window.open('/json/balanceaccount/export/?year='+balanceaccount.year+'&month='+balanceaccount.month);
    }

    /**
     *核对
     * @param balanceaccount
     * @param index
     */
    $scope.check=function (balanceaccount,index) {
        if(balanceaccount.checkStatus == 0){
            if(balanceaccount.totalPaymentAmount == 0.0){
                alert("该月数据为空，请重新计算后再操作！");
            }else{
                var modalInstance = $modal.open({
                    scope:$scope,
                    animation:true,
                    size: 'lg',
                    templateUrl:'tpl/view/balanceaccount/check.html',
                    controller:function ($scope,$modalInstance,$q){

                        /!*  关闭模态框 *!/
                        $scope.cancel = function ($modalInstance){
                            modalInstance.close();
                        };

                        $scope.pass = function () {
                            //备注信息
                            if ($scope.signinfo && $scope.signinfo.comment){
                                $scope.signinfo = {comment: $scope.signinfo.comment, result: 1, userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id};
                                $scope.check($scope.signinfo);
                                $scope.choseArr=[];
                                modalInstance.close();
                            }else {
                                toaster.pop('info', "请输入核对信息！");
                            }
                        };

                        $scope.check = function (signinfo) {
                            balanceaccountService.checkOne(balanceaccount.id,loginUserId,signinfo, function (data) {
                                if (data.m) {
                                    $scope.changePage(1);
                                    toaster.pop('success', data.m);
                                }else{
                                    $scope.changePage(1);
                                    toaster.pop('success', "核对数据完成！");
                                }
                            })

                        }
                        $scope.close = function () {
                            modalInstance.close();
                        }
                    }
                });
            }
        }

        if(balanceaccount.checkStatus == 1){
            alert("您已经核对信息，不能进行重复核对。");
        }
        if(balanceaccount.checkStatus == 2){
            alert("正在付款。不能进行此操作。");
        }
        if(balanceaccount.checkStatus == 9){
            alert("付款结束，不能进行此操作");
        }
    }


    /**
     *付款
     * @param balanceaccount
     * @param index
     */
    $scope.pay=function (balanceaccount,index) {
      if(balanceaccount.checkStatus == 1){
          var modalInstance = $modal.open({
              scope:$scope,
              animation:true,
              size: 'lg',
              templateUrl:'tpl/view/chargefeeplan/pay.html',
              controller:function ($scope,$modalInstance,$q){
                  balanceaccountService.getOne(balanceaccount.id,function (data) {
                      if (data){

                          $scope.balanceAccount = data;
                          $scope.balanceAccount.businessTypeCode='NC';
                          $scope.balanceAccount.customerTransactionId = data.id;
                          $scope.balanceAccount.customerId = data.id;
                      }
                  });
                  //关闭模态框
                  $scope.cancel = function ($modalInstance){
                      modalInstance.close();
                  };

                  /*  开始付款 */
                  $scope.startPay = function (balanceaccount,$modalInstance){
                      modalInstance.close();
                      balanceaccountService.pay(balanceaccount.id,loginUserId,function (data) {
                          if(data){
                              toaster.pop('success', "开始付款啦！！");
                          }
                          $scope.changePage(1);
                      })
                  };
              }
          });
      }
        if(balanceaccount.checkStatus == 0){
            alert("您当前没有核对信息，不能进行付款操作。");
        }
        if(balanceaccount.checkStatus == 2){
            alert("正在付款。不能重复付款。");
        }
        if(balanceaccount.checkStatus == 9){
            alert("付款结束，不能重复付款");
        }

    };

    //银行对账凭证
    $scope.statement = function (balanceaccount,index) {

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'tpl/view/chargefeeplan/pay.html',
            controller:function ($scope,$modalInstance,$q){
                balanceaccountService.getOne(balanceaccount.id,function (data) {
                    if (data){
                        $scope.balanceAccount = data;
                        $scope.balanceAccount.businessTypeCode='NC';
                        $scope.balanceAccount.customerTransactionId = data.id;
                        $scope.balanceAccount.customerId = data.id;
                    }
                });
                //关闭模态框
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };
            }
        });
    };


    $scope.save=function (balanceaccount) {
        balanceaccountService.save(balanceaccount, function (data) {
            $scope.balanceaccount = data;
        });
    }


    //付款完成
    $scope.over = function (balanceaccount,$modalInstance){
        if(balanceaccount.checkStatus == 2){
            if(confirm("请确认您的付款是否完成")){
                balanceaccountService.payOver(balanceaccount.id,loginUserId,function (data) {
                    if(data){
                        toaster.pop('success', "恭喜，付款完成咯！！");
                    }
                    $scope.changePage(1);
                })
            }
        }
        if(balanceaccount.checkStatus == 0){
            alert("您当前没有核对信息，不能进行操作。");
        }
        if(balanceaccount.checkStatus == 1){
            alert("未到此阶段。不能进行操作。");
        }
        if(balanceaccount.checkStatus == 9){
            alert("付款结束，不能重复付款");
        }


    };




});