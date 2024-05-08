/**
 * Created by zxp on 2017/9/20.
 */
'use strict';
app.controller('chargefeeplanController', function ($scope, $state, $stateParams, $rootScope,chargefeeplanService, $modal, toaster, $q, $localStorage,customerTransactionService,$filter) {

    $scope.title = "收款计划";
    $scope.tableTitle = "收款计划表";

    //列表数据
    $scope.chargefeeplans = [];
    $scope.chargefeeplan = {};
    $scope.customer = {};
    $scope.signinfo={};
    $scope.beforeDate=null;
    $scope.checked=false;
    $scope.searchFilter = {pageSize:20,billTypeCode:'A028',timeName:'swingCardDate',statusName:'status',statusValue:-1,sortName:'swingCardDate'};
    //多选框
    $scope.choseArr=[];//定义数组用于存放前端显示
    $scope.x=false;//全部默认为不选
    var flag='';//是否点击了全选，是为a
    $scope.master=false;


    $scope.init = function () {
        if ($stateParams.id) {
            chargefeeplanService.getOne($stateParams.id, function (data) {
                $scope.chargefeeplan = data;
            });
        }
        $scope.changePage(1);
    };


    $scope.changePage = function (pageIndex) {
        $scope.signinfo={};

        if (pageIndex) {
            $scope.currentPage = pageIndex;
        }
        chargefeeplanService.search($scope.searchFilter, $scope.currentPage-1, function (data) {
            $scope.choseArr.length=0;
            $scope.master=false;
            $scope.chargefeeplans = data.result;//返回的数据
            $scope.detailList = data.detailList;//返回的明细
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages;//总页数EnhancementController
            customerTransactionService.mergeTransactions($scope.chargefeeplans, function(bills){
                $scope.chargefeeplans = bills;
            })
        })
    /* if ($scope.queryFilter) {
            $localStorage.ChargePlanQueryFilter = $scope.queryFilter;

        }*/
    };
    $scope.init();

    $scope.changePageSize = function () {
        $scope.searchFilter.pageSize = $scope.pageSize;
        $scope.changePage(1);
    };

    //弹出右划框
    $scope.view = function(chargefeeplan,businessTypeCode){
        if(!$scope.checked){
            if ($scope.chargefeeplan){
                $scope.chargefeeplan = {};
            }
            chargefeeplanService.getOne(chargefeeplan.id,function (data) {
                $scope.chargefeeplan=data;
                $scope.chargefeeplan.businessTypeCode = businessTypeCode;
                $scope.checked=true;

            });
            chargefeeplanService.getOneDetaiil(chargefeeplan.id,function (data) {
                $scope.list=data;
            })
        }
    };

    //关闭右边框
    $scope.close = function () {
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    $scope.search = function () {
        $scope.changePage(1);
    };

    /**
     * 收款信息计算
     */
    $scope.account = function () {
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'tpl/view/chargefeeplan/account.html',
            controller:function ($scope,$modalInstance,$q){
                $scope.counts = function (chargefeeplan,$modalInstance) {
                    if (confirm("您确定要计算"+chargefeeplan.year+"年"+chargefeeplan.month+"月的银行结费？")) {
                        modalInstance.close();
                        $scope.signinfo = {comment: "计算银行结费信息", userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id};
                        chargefeeplanService.countOne(chargefeeplan.year, chargefeeplan.month,$scope.signinfo, function (data) {
                            $scope.changePage(1);
                            if( data ){
                                 toaster.pop('success',data);
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
    };

    $scope.e =0;
    $scope.status=9;
    //错误导出
    $scope.openExport = function (){
        if(confirm("确定要导出所有错误数据吗？")){
            chargefeeplanService.findError(function (data) {
                if(data.length >0){
                    window.open('/json/chargefeeplan/export');
                }else {
                    alert("暂无错误数据导出！");
                }
            })

            
        }
        /*var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'export.html',
            controller:function ($scope,$modalInstance,$q){

                /!*  关闭模态框 *!/
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                $scope.doExport=function (beforeDate) {
                  //  $scope.exporting = true;
                    modalInstance.close();
                    chargefeeplanService.findError(beforeDate,function (data) {
                        if(data.length >0){
                            window.open('/json/chargefeeplan/export/?beforeDate='+beforeDate);
                        }else {
                            alert("暂无错误数据导出！");
                        }
                    })
                    
                }
            }
        });*/
    };


    /*/!**
     * 查询窗口
     *!/*/
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "query.html",
            controller: function ($scope, $modalInstance) {
                //we do nothing now!
            }
        })
    };
    /*/!**
     * 查询
     * @param query
     *!/*/
    $scope.queryBill = function (searchFilter) {
        $scope.searchFilter = searchFilter;
        $scope.changePage(1);

    };

    $scope.clearQuery = function () {
        $scope.searchFilter = {pageSize:20,billTypeCode:'A028',timeName:'swingCardDate',statusName:'status',statusValue:$scope.searchFilter.statusValue,sortName:'swingCardDate'};
        $scope.changePage(1);
    };

    //重算
    $scope.refresh=function (chargefeeplan,index) {
        if(chargefeeplan.status == 2){
            alert("数据已经核对完成，不能进行重算。");
        }else {
            $scope.count(chargefeeplan,index);
        }
    }
    //重算
    $scope.count = function (chargefeeplan, index) {
        $scope.chargefeeplan = chargefeeplan;
        var id = $scope.chargefeeplan.id;
        $scope.signinfo = {comment: "重新计算银行结费信息", userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id};
        chargefeeplanService.refresh(id,$scope.signinfo, function (data) {
            if (data.comment) {
                $scope.chargefeeplans[index] = data;
                $scope.changePage(1);
                toaster.pop('error', data.comment);
            } else {
                /*$scope.chargefeeplans[index] = data;
                $scope.list=data.detailList;*/
                $scope.changePage(1);
                toaster.pop('success', '重算客户收款计划表成功。');
            }
        })
    }
    var loginUserId = $rootScope.user.userID;

    $scope.toCheck=function (chargefeeplan, index) {

        if(chargefeeplan!= null && chargefeeplan.status == 1){
            $scope.checkdate(chargefeeplan,index);
        }
        if(chargefeeplan!= null && chargefeeplan.status == 2){
            alert("数据已经核对，请取消核对进行重新核对。");
        }
        if(chargefeeplan!= null && chargefeeplan.status == 3){
            alert("数据已经核对，请取消核对进行重新核对。");
        }
        if(chargefeeplan == null && $scope.choseArr.length !=0){
            $scope.checkdate(chargefeeplan,index);
        }
        if(chargefeeplan == null && $scope.choseArr.length ==0){
            alert("请选择需要核对的数据。")
        }
    }
    //核对
    $scope.checkdate = function (chargefeeplan, index) {

                var modalInstance = $modal.open({
                    scope:$scope,
                    animation:true,
                    size: 'lg',
                    templateUrl:'tpl/view/chargefeeplan/check.html',
                    controller:function ($scope,$modalInstance,$q) {

                        /!*  关闭模态框 *!/
                        $scope.cancel = function ($modalInstance) {
                            modalInstance.close();
                        };

                        $scope.pass = function () {
                            $scope.signinfo = {comment: $scope.signinfo.comment, result: 2, userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id};
                            $scope.check({signinfo: $scope.signinfo});
                            modalInstance.close();
                        };

                        $scope.reject = function () {
                            $scope.signinfo = {comment: $scope.signinfo.comment, result: 3, userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id};
                            $scope.check({signinfo: $scope.signinfo});
                            modalInstance.close();
                        };

                        $scope.check = function (signinfo) {
                            if (chargefeeplan != null) {
                                chargefeeplanService.checkOne(chargefeeplan.id, loginUserId, signinfo, function (data) {
                                    $scope.choseArr.length = 0;
                                    $scope.master = false;
                                    if (data.m) {
                                        toaster.pop('success', data.m);
                                    } else {
                                        toaster.pop('success', "核对数据成功！！");
                                    }
                                    $scope.changePage(1);
                                })

                            } else {
                                chargefeeplanService.check($scope.choseArr, loginUserId, signinfo, function (data) {
                                    $scope.choseArr.length = 0;
                                    $scope.master = false;
                                    if (data.m) {
                                        toaster.pop('success', data.m);
                                    } else {
                                        toaster.pop('success', "所选数据全部核对成功");
                                    }
                                    $scope.changePage(1);

                                })
                            }
                        }
                        $scope.close = function () {
                            modalInstance.close();
                            $scope.signinfo={};
                        }
                    }
                });
    };

$scope.unCheck=function (chargefeeplan, index) {
    if(chargefeeplan!= null && chargefeeplan.status == 1){
        alert("数据还未核对，不能进行取消核对操作。");
    }
    if(chargefeeplan!= null && chargefeeplan.status == 3){
        alert("核对错误的数据，不能取消核对，可以重新计算！");
    }
    if(chargefeeplan!= null && chargefeeplan.status == 2){
        $scope.cancelcheck(chargefeeplan,index);
    }
    if(chargefeeplan == null && $scope.choseArr.length !=0){
        $scope.cancelcheck(chargefeeplan,index);
    }
    if(chargefeeplan == null && $scope.choseArr.length ==0){
        alert("请选择需要取消核对的数据。")
    }
}

    //取消核对
    $scope.cancelcheck = function (chargefeeplan, index) {
                var modalInstance = $modal.open({
                    scope: $scope,
                    animation: true,
                    size: 'lg',
                    templateUrl: 'tpl/view/chargefeeplan/uncheck.html',
                    controller: function ($scope, $modalInstance, $q) {

                        /!*  关闭模态框 *!/
                        $scope.cancel = function ($modalInstance) {
                            modalInstance.close();
                        };

                        $scope.pass = function () {
                            $scope.signinfo = {comment: $scope.signinfo.comment, result: 4, userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id};
                            $scope.uncheck($scope.signinfo);
                            modalInstance.close();
                        };

                        $scope.uncheck = function (signinfo) {
                            if (chargefeeplan != null) {
                                chargefeeplanService.uncheckOne(chargefeeplan.id, signinfo, function (data) {
                                    $scope.choseArr.length=0;
                                    $scope.master=false;
                                    if (data.m) {
                                        toaster.pop('error', data.m);
                                    } else {
                                        $scope.chargefeeplans[index] = data;
                                        toaster.pop('success', '取消核对客户收款计划表完成。');
                                    }
                                    $scope.changePage(1);
                                })
                            } else {
                                chargefeeplanService.uncheck($scope.choseArr, signinfo, function (data) {
                                    $scope.choseArr.length=0;
                                    $scope.master=false;
                                    if (data.m) {
                                        toaster.pop('error', data.m);
                                    } else {
                                        toaster.pop('success', '取消核对客户收款计划表完成。');
                                    }
                                    $scope.changePage(1);
                                })
                            }

                        };

                        $scope.close = function () {
                            modalInstance.close();
                            $scope.signinfo = {};
                        }
                    }
                });
    };
    
    $scope.toOrder = function (chargefeeplan, index) {
        $state.go("app.business.ordernewcar", {tid: chargefeeplan.customerTransactionId});
    };
    //查找卡业务处理
    $scope.toCard = function (chargefeeplan, index) {
        $state.go("app.business.bankcard", {tid: chargefeeplan.customerTransactionId});
    };

    $scope.selectedAll=false;
    $scope.selected=false;
    
    //全选
    $scope.all= function (m) {
        $scope.choseArr=[];
        for(var i=0;i<$scope.chargefeeplans.length;i++){
            if(m===true){
                $scope.x=true;
                $scope.choseArr.push($scope.chargefeeplans[i].id);
            }else {
                $scope.x=false;
                $scope.choseArr.splice($scope.choseArr.indexOf($scope.chargefeeplans[i].id),1)
            }
        }
    };

    $scope.chk= function (z,x) {//单选或者多选

       if (x == true) {//选中
           $scope.choseArr.push(z.id);
             flag='c';
           if($scope.choseArr.length==$scope.chargefeeplans.length){
             $scope.master=true;
           }else{
               $scope.master=false;
           }
       } else {
            $scope.choseArr.splice($scope.choseArr.indexOf(z.id),1);//取消选中
       }
        if($scope.choseArr.length==0){
            $scope.master=false;
        }
    };

});