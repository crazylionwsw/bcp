'use strict';

app.controller('cardealerController',['$scope','$modal','orginfoService','cardealerService','employeeService','cashsourceService','businessService','carTypeService','provinceService','sysparamService','$state','$stateParams','$q','toaster','$filter','$rootScope','$loading',function($scope,$modal,orginfoService,cardealerService,employeeService,cashsourceService,businessService,carTypeService,provinceService,sysparamService,$state,$stateParams,$q,toaster,$filter,$rootScope,$loading){
    $scope.button = true;
    $scope.title = '渠道管理';
    $scope.tableTitle = "渠道员工";
    //返回的列表数据
    $scope.cardealers = [];
    $scope.cardealer = {};

    $scope.signModal = {};
    /*部门的禁用*/
    $scope.disorg = false;

    $scope.selectedCardealer = null;

    /*可用员工*/
    $scope.employees = [];
    $scope.employee = {};

    /*渠道经理*/
    $scope.employeeManagers = [];

    /*分期经理*/
    $scope.businessManagers = [];

    $scope.currentPage = 1;

    $scope.checked = false;

    $scope.cardealer = {};

    //车型列表
    $scope.cartypes = [];

    //渠道员工列表
    $scope.dealeremployees = [];
    $scope.dealeremployee = {};

    $scope.cardealerQuery = {};
    $scope.cardealerQueryFilter = {};

    //品牌列表
    $scope.carbrands = [];
    $scope.selectedBrand = {};

    $scope.modalTitle = "业务转移";
    $scope.cardealerTransfer = {};

    $scope.cardealerTransfer.tid = [];
    $scope.cardealerTransfer.bid = null;

    $scope.format = 'yyyy-MM-dd';
    $scope.opened = false;
    //账户列表
    $scope.payAccounts = [];
    $scope.payAccount = {};

    //合作支行
    $scope.cashSources = [];
    //  报单支行
    $scope.declarationCashSources =[];

    //贷款产品列表
    $scope.creditproducts = [];

    //经营业务
    $scope.businessTypes = [];

    //      4S员工角色
    $scope.employeeRoles = [];

    //地区列表
    $scope.regions = [];

    //期率类型
    $scope.rateTypes = {};
    $scope.rateType = [];

    $scope.currentPage = 1;
    $scope.currentPageCarDealer = 1;

    $scope.currentPageCarDealerEmployee = 1;

    $scope.carealerid = $stateParams.id;

    $scope.canAudit = false;
    //新车二手车垫资时间展示控制
    $scope.showPaymentOldTime = false;
    $scope.showPaymentNewTime = false;

    //
    $scope.ratioShow = false;


    //渠道账户类型
    $scope.accountTypes = [
        {
            id:0,
            name:'个人账户'
        },
        {
            id:1,
            name:'公司账户'
        }
    ];

    //渠道合作状态
    $scope.cooperateStatus =[
        {
            id:0,
            statusName:'商谈阶段'
        },
        {
            id:1,
            statusName:'正常合作'
        },
        {
            id:9,
            statusName:'终止合作'
        }
    ];

    $scope.accountWays = [
        {
            id:0,
            name:'收付款账户'
        },
        {
            id:1,
            name:'付款账户'
        },
        {
            id:2,
            name:'收款账户'
        }
    ];

    $scope.openDateDialog = function($event){
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };
    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1,
        class: 'datepicker'
    };

    //验证渠道4s店名称
    $scope.checkUniqueCarDealer = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.cardealer;
            var discount = entity.paymentPolicy.discount;
            var business = entity.paymentPolicy.business;
            if(discount){
                discount = 1;
                entity.paymentPolicy.discount = discount;
            }else {
                discount = 0;
                entity.paymentPolicy.discount = discount;
            }
            if(business){
                business = 1;
                entity.paymentPolicy.business = business;
            }else {
                business = 0;
                entity.paymentPolicy.business = business;
            }
            cardealerService.checkUniqueCarDealer(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
                var discount = $scope.cardealer.paymentPolicy.discount;
                var business = $scope.cardealer.paymentPolicy.business;
                if(discount == 1){
                    discount = true;
                    $scope.cardealer.paymentPolicy.discount = discount;
                }else {
                    discount = false;
                    $scope.cardealer.paymentPolicy.discount = discount;
                }
                if(business == 1){
                    business = true;
                    $scope.cardealer.paymentPolicy.business = business;
                }else {
                    business = false;
                    $scope.cardealer.paymentPolicy.business = business;
                }
            });
        });
    };
    

    // $scope.openQueryDialog = function (){
    //
    //     var modalInstance = $modal.open({
    //         scope:$scope,
    //         animation:true,
    //         templateUrl:'tpl/view/cardealer/query.html',
    //         controller:function ($scope,$modalInstance){
    //             $scope.modalTitle = "查询";
    //
    //             $scope.checkQuery = function(value) {
    //                 if ($scope.cardealerQuery.queryString != null && $scope.cardealerQuery.queryString.trim() != "") {
    //                     return true;
    //                 } else {
    //                     return false;
    //                 }
    //             };
    //
    //             $scope.updateQueryString = function () {
    //                 $scope.cardealerQuery.queryString = '';
    //                 if ($scope.cardealerQuery.name) $scope.cardealerQuery.queryString += $scope.cardealerQuery.name;
    //                 if ($scope.cardealerQuery.orginfoId) $scope.cardealerQuery.queryString += $scope.cardealerQuery.orginfoId;
    //                 if ($scope.cardealerQuery.employeeId) $scope.cardealerQuery.queryString += $scope.cardealerQuery.employeeId;
    //                 if ($scope.cardealerQuery.businessManId) $scope.cardealerQuery.queryString += $scope.cardealerQuery.businessManId;
    //                 if ($scope.cardealerQuery.cashSourceId) $scope.cardealerQuery.queryString += $scope.cardealerQuery.cashSourceId;
    //                 if ($scope.cardealerQuery.cooperationCashSourceId) $scope.cardealerQuery.queryString += $scope.cardealerQuery.cooperationCashSourceId;
    //
    //             };
    //
    //             /*  关闭模态框 */
    //             $scope.cancel = function ($modalInstance){
    //                 modalInstance.close();
    //             };
    //             /*清楚查询内容*/
    //             $scope.clearQuery = function() {
    //                 $scope.cardealerQuery = {};
    //                 $scope.changePage();
    //             };
    //
    //             var user = $scope.user;
    //             var currentPage = $scope.currentPage;
    //
    //
    //             /* 查询经销商基本信息 */
    //             $scope.queryloginUser = function (){
    //                 var cardealer = $scope.cardealerQuery;
    //                 var currentPage = $scope.currentPage;
    //                 cardealerService.search(cardealerQuery,currentPage-1, function (data) {
    //                     $scope.$parent.cardealers = data.result;
    //                     $scope.$parent.totalItems = data.totalCount;//数据总条数
    //                     $scope.$parent.pageSize = data.pageSize;//分页单位
    //                     $scope.$parent.currentPage = data.currentPage + 1;//当前页
    //                     $scope.$parent.totalPages = data.totalPages;
    //                 });
    //                 /* 操作完成自动关闭模态框 */
    //                 modalInstance.close();
    //             }
    //
    //         }
    //     });
    // };

    /**
     * 初始化加载列表
     */
    $scope.init = function(){
        //经销商分页
        $scope.changePage();

        //获取渠道经理
        employeeService.getChannelManager(function (data) {
            $scope.employeeManagers = data;
        });

        //获取分期经理
        employeeService.getStageManager(function (data) {
            $scope.businessManagers = data;
        });

        //获取可用员工列表
        employeeService.lookup(function (data) {
            $scope.employees = data;
        });
        //合作支行列表
        cashsourceService.lookup(function (data) {
            $scope.cashSources = data;
        });
        // 报单支行列表
        sysparamService.getListByCode("DECLARATION_BANKS",function (data) {
            $scope.declarationCashSources = data;
        });
        //车辆品牌信息
        carTypeService.lookupCarBrands(function (data) {
            $scope.carbrands = data;
        });

        //地区列表
        provinceService.lookup(function (data) {
            $scope.regions = data;
        });

        //组织部门
        orginfoService.lookup(function (data) {
            $scope.orgs = data;
        });
        //  员工角色
        sysparamService.getListByCode("EMPLOYEE_ROLE",function (data) {
            $scope.employeeRoles = data;
        });

        sysparamService.getListByCode("DEALER_SHARING_RATIO",function(data){
            $scope.sharingRatioList = data;
        });

        cardealerService.lookupDealerGroups(function (data) {
            $scope.groups = data;
        });
    };

    //选特殊和标准
    $scope.chooseSharingRatio = function(item, index) {
        var keys = _.keys(item.ratio);
        $scope.sharingRatios = [];
        angular.forEach(keys, function(k){
            $scope.sharingRatios.push({'months': k, 'ratio': item.ratio[k]});
        });
    };

    $scope.saveSharingRatio = function() {
        if (!$scope.cardealer) {
            alert('请选择经销商！');
            return;
        }
        var ratio = {};
        angular.forEach($scope.sharingRatios, function(item) {
            ratio[item.months] = item.ratio;
        });
        cardealerService.saveSharingRatio({'carDealerId': $scope.cardealer.id, 'sharingRatio': ratio}, function (data) {
            // alert('保存成功！');
        });
    };

    /**
     * 经销商列表分页
     */
    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        if( $scope.cardealerQueryFilter.name || $scope.cardealerQueryFilter.orginfoId || $scope.cardealerQueryFilter.employeeId || $scope.cardealerQueryFilter.businessManIds || $scope.cardealerQueryFilter.cashSourceId || $scope.cardealerQueryFilter.cooperationCashSourceId){
            cardealerService.search($scope.cardealerQueryFilter, $scope.currentPage-1,function(data){
                $scope.cardealers = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages=data.totalPages;//总页数
            });
        } else {
            cardealerService.getDataByPage($scope.currentPage-1,function (data){
                $scope.cardealers = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage+1;//当前页
                $scope.totalPages = data.totalPages;
            });
        }
    };

    /*经销商员工列表分页*/
    $scope.changePageDealerEmployees = function (id){
        cardealerService.getDealerEmployees(id,$scope.currentPageCarDealerEmployee-1,function(data){
            $scope.dealeremployees = data.result;
            $scope.totalItemEs = data.totalCount;//数据总条数
            $scope.pageSizeE = data.pageSize;//分页单位
            $scope.currentPageCarDealerEmployee = data.currentPage+1;//当前页
            $scope.totalPageEs = data.totalPages;
        });
    };


    /**
     * 选定经销商信息
     * @param carDealer
     */
    $scope.selectCarDealer = function(carDealer){
        $scope.button = false;
        carDealer.selected = true;
        if($scope.selectedCardealer) {
            $scope.selectedCardealer.selected = false;
        }
        $scope.selectedCardealer = carDealer;

    };

    /**
     * 执行初始化
     */
    $scope.init();

    /**
     * 操作弹出层
     * @param cardealer
     * @param index
     */
    $scope.edit = function (cardealer,index) {

        //隐藏标准选框
        $scope.ratioShow = false;

        $scope.sharingRatios = [];
        if(cardealer.id != null && cardealer.id != ''){
            $scope.changePageDealerEmployees(cardealer.id);
            $scope.modalTitle = "编辑渠道信息";
            if (!$scope.checked) {
                if ($scope.cardealer){
                    $scope.cardealer = {};
                }
                
                /*判断当前经销商是否在交易，禁用*/
                cardealerService.getCardelearTransaction(cardealer.id,function (data) {
                    if(data > 0){
                        $scope.disorg = true;
                    }else {
                        $scope.disorg = false;
                    }
                });
                
                cardealerService.getOne(cardealer.id, function (data) {
                    $scope.cardealer = data;
                    $scope.checked = true;


                    var businessCode = $scope.cardealer.businessTypeCodes;
                    angular.forEach(businessCode,function (item) {
                        if(item == "OC"){
                            $scope.showPaymentOldTime = true;
                        }
                        if(item == "NC"){
                            $scope.showPaymentNewTime = true;
                        }
                    });

                    var discount = $scope.cardealer.paymentPolicy.discount;
                    var business = $scope.cardealer.paymentPolicy.business;
                    if(discount == 1){
                        discount = true;
                        $scope.cardealer.paymentPolicy.discount = discount;
                    }else {
                        discount = false;
                        $scope.cardealer.paymentPolicy.discount = discount;
                    }
                    if(business == 1){
                        business = true;
                        $scope.cardealer.paymentPolicy.business = business;
                    }else {
                        business = false;
                        $scope.cardealer.paymentPolicy.business = business;
                    }

                    if($scope.cardealer.carBrandIds && $scope.cardealer.carBrandIds.length > 0){
                        $scope.cardealer.brandIsLimit = 1;
                    }




                    //      判断  当前登录用户 是否是该渠道的部门经理
                    //      根据  当前渠道的渠道经理 employeeId    去后台  判断 该渠道经理的最上级部门信息
                    if ($scope.cardealer.orginfoId){
                        var loginUserEmployeeId = $rootScope.user.employee.id;
                        cardealerService.checkAuditPermission($scope.cardealer.orginfoId,loginUserEmployeeId,function (result) {
                            if (result == true){
                                $scope.canAudit = true;
                            }
                        })
                    }

                    //获取业务类型列表
                    businessService.lookupWithRates(function (data) {
                        $scope.businessTypes = data;

                        $scope.cardealer.businessTypes = [];
                        $scope.cardealer.businessTypeFees = [];
                        $scope.cardealer.dealerRates = {};
                        $scope.cardealer.dealerServiceFee = {};

                        if ($scope.cardealer.dealerRateTypes) {
                            //转换为Map
                            angular.forEach($scope.cardealer.dealerRateTypes, function (item) {
                                if (!$scope.cardealer.dealerRates[item.businessTypeCode])
                                    $scope.cardealer.dealerRates[item.businessTypeCode] = {};
                                angular.forEach(item.rateTypeList, function (item2) {
                                    $scope.cardealer.dealerRates[item.businessTypeCode][item2.sourceRateId] = item2.rateTypeList;
                                });
                            });
                            angular.forEach($scope.businessTypes, function (item, key) {
                                var rates = {};
                                angular.forEach(item.rateTypes, function (item2) {
                                    rates[item2.sourceRateId] = item2.rateTypeList;
                                });
                                $scope.businessTypes[key].rates = rates;
                            });

                            angular.forEach($scope.businessTypes, function (bt) {
                                if ($scope.cardealer.dealerRates[bt.code]) { //
                                    $scope.cardealer.businessTypes.push(bt);

                                    var dealerRateTypes = {};
                                    var sourcerateIds = _.keys(bt.rates);
                                    var sourcerateTypes = _.values(bt.rates);

                                    var dealerBizRateTypes = $scope.cardealer.dealerRates[bt.code];

                                    for (var i = 0; i < sourcerateIds.length; i++) {
                                        if (dealerBizRateTypes[sourcerateIds[i]]) {
                                            var mergedRateTypes = []
                                            angular.forEach(sourcerateTypes[i], function (rateType) {
                                                mergedRateTypes.push($filter('mergeRateTypes')(dealerBizRateTypes[sourcerateIds[i]], rateType));
                                            });
                                            dealerRateTypes[sourcerateIds[i]] = mergedRateTypes;
                                        } else {

                                            dealerRateTypes[sourcerateIds[i]] = sourcerateTypes[i];
                                        }
                                    }
                                    $scope.cardealer.dealerRates[bt.code] = dealerRateTypes;
                                }
                            })
                        }

                        if ($scope.cardealer.serviceFeeEntityList) {
                            angular.forEach($scope.cardealer.serviceFeeEntityList, function (item) {
                                if (!$scope.cardealer.dealerServiceFee[item.businessType])
                                    $scope.cardealer.dealerServiceFee[item.businessType] = {};
                                angular.forEach(item.rateTypeList, function (item2) {
                                    $scope.cardealer.dealerServiceFee[item.businessType] = item2.rateTypeList;
                                });
                            });
                        }
                    });


                    // get ratio
                    cardealerService.getSharingRatio(cardealer.id, function (data) {
                        if (data) {
                            var keys = _.keys(data.sharingRatio);
                            angular.forEach(keys, function(k){
                                $scope.sharingRatios.push({'months': k, 'ratio': data.sharingRatio[k]});
                            })
                        }
                    });
                })
            }
        }else{
            $scope.modalTitle = "添加渠道信息";
            $scope.cardealer = {dataStatus: 1};
            if (!$scope.checked){
                $scope.checked = true;
            }
        }

        /*保存或修改渠道(经销商)信息*/
        $scope.saveform = function (){
            var saveRateTypes = [];
            angular.forEach($scope.cardealer.businessTypes, function(item){
                var rateTypes = [];
                angular.forEach(item.rateTypes, function(item2) {
                    var s = $scope.cardealer.dealerRates[item.code][item2.sourceRateId];
                    rateTypes.push({sourceRateId: item2.sourceRateId, rateTypeList: $scope.cardealer.dealerRates[item.code][item2.sourceRateId]});
                });
                saveRateTypes.push({businessTypeCode: item.code, rateTypeList: rateTypes});
            });
            $scope.cardealer.dealerRateTypes = saveRateTypes;
            var cardealer = $scope.cardealer;

            var discount = $scope.cardealer.paymentPolicy.discount;
            var business = $scope.cardealer.paymentPolicy.business;
            if(discount){
                discount = 1;
                cardealer.paymentPolicy.discount = discount;
            }else {
                discount = 0;
                cardealer.paymentPolicy.discount = discount;
            }
            if(business){
                business = 1;
                cardealer.paymentPolicy.business = business;
            }else {
                business = 0;
                cardealer.paymentPolicy.business = business;
            }

            cardealerService.saveCarDealer(cardealer,function (data){
                if ($scope.cardealer.id == "" || $scope.cardealer.id == null) {
                    $scope.cardealers.push(data);
                } else {
                    //var index = $scope.cardealers.indexOf($scope.cardealer);
                    $scope.cardealers[index] = data;
                }
                $scope.close();
                toaster.pop('success', '', '保存成功！');
            });
        };
    };
    /**
     * 关闭弹出层
     */
    $scope.close = function () {
        if ($scope.cardealer && $scope.cardealer.id && $scope.cardealer.id != ''){
            //更新列表数据
            cardealerService.getOne($scope.cardealer.id,function (data) {
                angular.forEach($scope.cardealers, function(item, index){
                    if (item.id == $scope.cardealer.id) {
                        $scope.cardealers[index] = data;
                    }
                });
            });
        }
        $scope.userForm.$setPristine();
        $scope.checked = false;

        //重新获取所有渠道经理
        employeeService.getChannelManager(function (data) {
            $scope.employeeManagers = data;
        });

    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    //部门员工验证唯一
    $scope.checkUniqueDealerEmployee = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.dealeremployee;
            cardealerService.checkUniqueDealerEmployee(entity,propname,propval,function(data){
                if(data.code=='1'){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };


    
    $scope.deleteCardealer = function (cardealer,index){
            var msg = "确定要作废数据  " + cardealer.name + "  ?";
            var msg2 = "作废成功";
            if (cardealer.dataStatus == 9) {
                msg = "确定要删除数据  " + cardealer.name + "  ?";
                msg2 = '删除成功';
            }

            if (confirm(msg)) {
                cardealerService.delete(cardealer, function (data) {
                    toaster.pop('success','渠道经销商', msg2);
                    if (cardealer.dataStatus == 9) {
                        $scope.cardealers.splice(index, 1);
                    } else {
                        $scope.cardealers[index] = data;
                    }
                })
            }
    };

    /**
     * 添加或修改经销商员工
     * @param dealeremployee
     */
    $scope.editDealerEmployee = function (dealeremployee,index) {
        /*模态框*/
        if(dealeremployee.id != "" && dealeremployee.id != null) {
            $scope.modalTitle = "编辑渠道员工信息";
            $scope.dealeremployee = angular.copy(dealeremployee);
            //$scope.dealeremployee = dealeremployee;
        }else {
            $scope.modalTitle = "添加渠道员工信息";
            $scope.dealeremployee = {dataStatus: 1};
        }
        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "dealeremployeeModal.html",
            controller: function ($scope) {

                /*关闭模态框*/
                $scope.cancel = function () {
                    modalInstance.close();
                };

                /*保存渠道员工信息*/
                $scope.save = function () {
                    var dealeremployee = $scope.dealeremployee;
                    dealeremployee.carDealerId = $scope.cardealer.id;
                    cardealerService.saveDealerEmployee(dealeremployee, function (data) {
                        if (!$scope.dealeremployee.id) {
                            $scope.dealeremployees.push(data);
                        }else {
                            //var index = $scope.dealeremployees.indexOf($scope.dealeremployee);
                            $scope.dealeremployees[index] = data;
                        }
                        $scope.dealeremployee = data;
                    });
                    modalInstance.close();
                }
            }
        });
    };

    //更换当前的渠道经理
    // $scope.selectChannel = function(channel, index) {
    //     // 渠道经理与所属部门绑定
    //     // if (channel) {
    //     //     $scope.cardealer.orginfoId = channel.orgInfoId;
    //     // }
    //
    //     //根据当前的渠道经理，获取对应的分期经理
    //     $scope.cardealer.businessManIds = [];
    //     employeeService.getStageManager(channel.id,function (data) {
    //         $scope.businessManagers = data;
    //     });
    // };

    //所属部门与渠道经理绑定
    $scope.selectOrg = function(orgInfo, index) {
        $scope.cardealer.employeeId = null;
        orginfoService.getEmployeesByOrgInfoId(orgInfo.id,function (data) {
            $scope.employeeManagers = data;
        })
    };

    /**
     * 删除渠道(经销商)员工
     */
    $scope.deleteDealerEmployee = function (dealeremployee, index){
        var msg = "确定要作废数据  " + dealeremployee.username + "  ?";
        var msg2 = "作废成功";
        if (dealeremployee.dataStatus == 9) {
            msg = "确定要删除数据  " + dealeremployee.username + "  ?";
            msg2 = '删除成功';
        }
        if (confirm(msg)) {
            cardealerService.deleteDealerEmployee(dealeremployee, function (data) {
                toaster.pop('success','渠道员工', msg2);
                if (dealeremployee.dataStatus == 9) {
                    $scope.dealeremployees.splice(index, 1);
                } else {
                    $scope.dealeremployees[index] = data;
                }
            })
        }
    };

    //添加利率
    $scope.addRate = function (sourcerateId) {
        if ($scope.business.rateTypes[sourcerateId]) {
            $scope.business.rateTypes[sourcerateId].push({
                id: $scope.rateTypes.length+1,
                months: 0,
                ratio: 0.0,
                isNew: true
            });
        }
    };

    /* 删除账户信息*/
    $scope.deletePayAccount = function (payAccount,index) {
        if (confirm("确定要删除银行账户是" + payAccount.accountNumber + "的数据吗?")) {
            $scope.cardealer.payAccounts.splice(index, 1);
        }
    };

    /**
     * 模态框--添加/修改银行账户信息
     */
    $scope.editPayAccount = function (payAccount,index){

        $scope.flag = false; // 新增

        if(payAccount.accountNumber != "" && payAccount.accountNumber != null){
            $scope.modalTitle="编辑银行账户";
            $scope.flag = true;
            $scope.payAccount = payAccount;
        } else {
            $scope.modalTitle="添加银行账户";
            $scope.payAccount = {dataStatus: 1};
        }
        //清空模态框内容
        if(payAccount)
            $scope.payAccount=payAccount;
        else
            $scope.payAccount ={};

        /*模态框*/
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:"payaccountForm.html",
            controller:function ($scope,$modalInstance){
                $scope.title = "银行账户";
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };


                $scope.defaultHave = function () {

                    $scope.payAccount.defaultAccount = 0;

                    angular.forEach($scope.cardealer.payAccounts,function (item,index) {
                        if(item.defaultAccount == 0){
                            if(item.accountNumber !== $scope.payAccount.accountNumber){
                                if(confirm("已有默认账户"+item.accountNumber+"是否替换?")){
                                    item.defaultAccount = 1;
                                }else {
                                    $scope.payAccount.defaultAccount = 1;
                                }
                            }
                        }
                    })
                }

                /* 保存账户信息 */
                $scope.savePayAccount = function (){

                    if(!$scope.cardealer.payAccounts){
                        $scope.cardealer.payAccounts = [];
                    }

                    if(!$scope.flag){
                        $scope.cardealer.payAccounts.push($scope.payAccount);
                    }

                    /* 操作完成自动关闭模态框 */
                    modalInstance.close();
                }
            }
        });
    };

    //选择业务类型
    $scope.selectBusinessType = function(businessType, index) {
        if (businessType) {
            if (!$scope.cardealer.dealerRates[businessType.code]) {
                $scope.cardealer.businessTypes.push(businessType);
                $scope.cardealer.dealerRates[businessType.code] = businessType.rates
            }
        };
        if(businessType.code == "OC"){
            $scope.showPaymentOldTime = true;
        }
        if(businessType.code == "NC"){
            $scope.showPaymentNewTime = true;
        }
    };

    //取消选择的经销商集团
    $scope.cancalGroup = function () {
        $scope.cardealer.dealerGroupId = null;
    }



    //选择车型品牌
    $scope.selectCarBrandCtrl = function (carbrand) {
        if(carbrand){
            $scope.cardealer.brandIsLimit = 1;
        }
    };
    //去除车型品牌
    $scope.removeCarBrandCtrl = function (carbrand,index) {

        if(carbrand){
            angular.forEach($scope.cardealer.carBrandIds,function (item) {
                var index = $scope.cardealer.carBrandIds.indexOf(carbrand);
                if(index == item){
                    $scope.cardealer.carBrandIds.splice(index, 1);
                }
            })
        }
        if($scope.cardealer.carBrandIds.length == 0){
            $scope.cardealer.brandIsLimit = 0;
        }
    };

    //去除业务类型
    $scope.removeBusinessType = function(businessType, index) {
        if (businessType) {
            if (!$scope.cardealer.dealerRates[businessType.code]) {
                var index = $scope.cardealer.businessTypes.indexOf(businessType);
                $scope.cardealer.businessTypes.splice(index, 1);
            }
        }
        if(businessType.code == "OC"){
            $scope.showPaymentOldTime = false;
        }
        if(businessType.code == "NC"){
            $scope.showPaymentNewTime = false;
        }
    };

    $scope.addRatio = function () {
        $scope.ratioShow = true;
    }

    // //查询功能
    // $scope.openQueryDialog = function (){
    //     $scope.cardealerQuery = {};
    //     var modalInstance = $modal.open({
    //         scope:$scope,
    //         animation:true,
    //         templateUrl:'tpl/view/cardealer/query.html',
    //         controller:function ($scope,$modalInstance){
    //             $scope.modalTitle = "查询";
    //
    //             var employees = $scope.emoloyees;
    //
    //
    //             /*清楚查询内容*/
    //             $scope.clearQuery = function() {
    //                 $scope.cardealerQuery = {};
    //                 $scope.changePage();
    //             };
    //
    //             /*  关闭模态框 */
    //             $scope.cancel = function ($modalInstance){
    //                 modalInstance.close();
    //             };
    //
    //             /*查询信息*/
    //             $scope.queryCardealers = function () {
    //                 var cardealerQuery = $scope.cardealerQuery;
    //                 var currentPage = $scope.currentPage;
    //                 cardealerService.search(cardealerQuery, currentPage-1,function(data){
    //                     $scope.$parent.cardealers = data.result;
    //                     $scope.$parent.totalItems = data.totalCount;//数据总条数
    //                     $scope.$parent.pageSize = data.pageSize;//分页单位
    //                     $scope.$parent.currentPage = data.currentPage + 1;//当前页
    //                     $scope.$parent.totalPages=data.totalPages;//总页数
    //                 });
    //                 modalInstance.close();
    //             }
    //         }
    //     });
    // };


    /**
     * 指令查询
     * @param cardealer
     * @param index
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
     * 查询
     * @param 
     */
    $scope.queryBill = function (cardealerQuery) {
        $scope.cardealerQueryFilter = cardealerQuery;
        $scope.changePage(1);
    };

    /**
     * 清空查询
     */
    $scope.clearQuery = function() {
        $scope.cardealerQueryFilter = {};
        $scope.changePage(1);
    };

    $scope.renew = function (cardealer,index) {
        if(confirm("确定要恢复该条数据?")){
            cardealer.dataStatus = 1;
            cardealerService.saveCarDealer(cardealer,function (data) {
                if(cardealer.dataStatus == 1){
                    $scope.cardealers[index] = data;
                }else{
                    alert("恢复数据出错!")
                }
            })
        }
    };


    $scope.transfer = function () {
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size:'lg',
            templateUrl:'transferForm.html',
            controller:function ($scope,$modalInstance){
                //获取所有的经销商
                cardealerService.getAllCarDealers(function (data) {
                    $scope.cardealers = data;
                });

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                $scope.transferSave = function (cardealerTransfer) {
                    if(cardealerTransfer.bid != null && cardealerTransfer.tid !=null){
                        for(var i= 0 ; i < cardealerTransfer.tid.length;i++) {
                            if (cardealerTransfer.tid[i] != cardealerTransfer.bid) {
                                $scope.bDatas = {};
                                $scope.tDatas = [];
                                if(cardealerTransfer){
                                    cardealerService.getOne(cardealerTransfer.bid,function (bData) {
                                        $scope.bDatas = bData;
                                        cardealerService.getCarDealerByIds(cardealerTransfer.tid,function (tData) {
                                            $scope.tDatas = tData;
                                            if($scope.bDatas != null && $scope.tDatas.length > 0){
                                                angular.forEach($scope.tDatas,function (item,index) {
                                                    if(JSON.stringify(item.paymentPolicy) == JSON.stringify($scope.bDatas.paymentPolicy)){
                                                        if((item.businessTypeCodes).toString() == ($scope.bDatas.businessTypeCodes).toString()){
                                                            cardealerService.transferDealer(cardealerTransfer,function (data) {
                                                                if ($scope.cardealer.id == "" || $scope.cardealer.id == null) {
                                                                    $scope.cardealers.push(data);
                                                                } else {
                                                                    $scope.cardealers[index] = data;
                                                                }
                                                                $scope.changePage(1);
                                                                $scope.cancel();
                                                                toaster.pop('success', '', '业务转移成功！');
                                                            })
                                                        }else {
                                                            alert(item.name+"与"+$scope.bDatas.name+"的经营业务不同,不可业务转移!");
                                                        }
                                                    }else{
                                                        alert(item.name+"与"+$scope.bDatas.name+"的垫资策略不同,不可业务转移!");
                                                    }
                                                })
                                            }
                                        });
                                    });
                                }
                            }else {
                                alert("将要转移的渠道和合并渠道不能相同，请重新选择！");
                                break;
                            }
                        }
                    }else{
                        alert("请选择将要转移的渠道或合并渠道!!!");
                    }

                }
            }
        });
    }

    /*                     审查、审批                           START                      */
    /**
     * 审核窗口
     */
    $scope.audit = function () {
        $scope.signModal = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:"audit.html",
            controller:function($scope,$modalInstance){
                //we do nothing now!
            }
        })
    };

    /**
     *          审核/审批
     * @param signinfo
     */
    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        cardealerService.sign(id, signinfo, function (data) {
            $loading.finish('signInfo');
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        });
    });

    $scope.$on('UpdateSignInfo', function (e, id) {
        cardealerService.getOne(id, function (data) {
            $scope.cardealer.approveStatus = data.approveStatus;
            $scope.cardealer.status = data.status;
            $scope.cardealer.startDate = data.startDate;
        })
    });
    /*                     审查、审批                           END                      */
    
}]);