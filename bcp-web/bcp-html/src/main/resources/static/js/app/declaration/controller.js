/**
 * 银行报批
 */
'use strict';
app.controller('declarationController', function ($scope,declarationService, orderService,cardemandService,sysparamService,customerService,carTypeService,customerTransactionService,localStorageService,$state,$stateParams,$modal,$loading,toaster){

    $scope.title = '银行报批';

    $scope.submitText = "发送银行报批";
    $scope.stageText = "生成专项分期说明";
    $scope.stageValue = 1;

    /*列表*/
    $scope.declarations  = [];//返回的列表数据
    $scope.declaration  = {};

    $scope.signModal = {};

    /*查询参数*/
    $scope.customer = {};
    $scope.customerFilter = {};

    $scope.submitText = {};
    $scope.declarationRecord = {};
    $scope.currentPage = 1;
    
    $scope.checked = false;
    
    $scope.natures = [];
    $scope.customerclasses = [];
    $scope.sourceIncomeWays = [];

    $scope.oneAtATime = true;

    $scope.declarationQuery = {status :-1};
    $scope.customerQuery = {};

    /**
     * 初始化加载列表和分页
     */
    $scope.init = function(){
        $scope.declaration = {dataStatus: 1};
        //     获得  客户类别列表
        sysparamService.getListByCode("CUSTOMER_CLASS",function (data) {
            $scope.customerclasses = data;
        });
        //     获得  单位属性列表
        sysparamService.getListByCode("NATURE_CLASS",function (data) {
            $scope.natures = data;
        });
        //      获取  收入证明方式
        sysparamService.getListByCode("SOURCE_INCOME_WAY",function (data) {
            $scope.sourceIncomeWays = data;
        });

        $scope.changePage(1);

        if ($stateParams.tid) { //从交易列表打开
            declarationService.getOneByCustomerTransactionId($stateParams.tid, function(data){
                if (data) $scope.edit(data);
            })
        }
    };

    /*分页功能*/
    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        declarationService.getDataByPage($scope.currentPage-1,function(data){
            $scope.declarations = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.declarations, function(bills){
                $scope.declarations = bills;
            })
        });
    };
    //右边框实现
    $scope.edit = function(declaration,index){

        if(!$scope.checked){
            if ($scope.declaration){
                $scope.declaration = {};
            }
            declarationService.getOne(declaration.id,function (data) {

                $scope.declaration = data;
                $scope.checked = true;
                if (data && data.stageApplication != null && data.stageApplication != ''){
                    $scope.stageText = "重新生成专项分期说明";
                    $scope.stageValue = 2;
                }
                if (data && data.resultIds && data.resultIds.length > 0){
                    $scope.submitText = "重新发送报批";
                } else {
                    $scope.submitText = "发送银行报批";
                }

                $scope.findData(data);

            })
        }
    };

    $scope.findData = function (data) {

        /*   查询  客户信息   */
        if (data.customerId){
            customerService.getOne(data.customerId,function (customer) {
                $scope.declaration.customer = customer;
            })
        }

        /*    查询    客户交易信息  */
        if (data.customerTransactionId){
            
            declarationService.getDeclarationHistorys(data.customerTransactionId,function (historys) {
                $scope.declaration.historys = historys;
            });
            
            customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                $scope.transaction = transaction;
            });

            cardemandService.getOneByCustomerTransactionId(data.customerTransactionId, function (cardemand) {
                $scope.declaration.cardemand = cardemand;
                if (cardemand.mateCustomerId){
                    customerService.getOne(cardemand.mateCustomerId,function (mateCustomer) {
                        $scope.declaration.cardemand.mateCustomer = mateCustomer;
                    });
                }
                if (cardemand.pledgeCustomerId){
                    customerService.getOne(cardemand.pledgeCustomerId,function (pledgeCustomer) {
                        $scope.declaration.cardemand.pledgeCustomer = pledgeCustomer;
                    })
                }
            });
            orderService.getOneByCustomerTransactionId(data.customerTransactionId, function (order) {
                $scope.declaration.order = order;
                /*   查询  客户购车信息   */
                if (order && order.customerCarId){
                    customerService.getCar(order.customerCarId,function (car) {
                        $scope.declaration.car = car;
                        if (car && car.carTypeId){
                            carTypeService.getCarType(car.carTypeId,function (carType) {
                                if (carType)  $scope.declaration.car.carType = carType;
                            })
                        }
                    })
                }
                /*    查询    客户交易信息  */
                if (order && order.customerLoanId){
                    customerService.getLoan(order.customerLoanId, function (loan) {
                        $scope.declaration.loan = loan;
                    })
                }
            })
        }
    };

    //关闭右边框
    $scope.close=function () {
        //更新列表数据
        declarationService.getOne($scope.declaration.id,function (data) {
            angular.forEach($scope.declarations, function(item, index){
                if (item.id == data.id) {
                    $scope.declarations[index] = data;
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.declarations[index].transaction = transaction;
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

    //清空模糊查询
    $scope.clearDeclaration = function () {
        $scope.customerQuery = {};
        $scope.declarationQuery = {status :-1};
        $scope.changePage($scope.currentPage);
    };

    //模糊查询
    $scope.searchDeclaration = function (customerQuery,declarationQuery) {
        var currentPage = 1;
        declarationService.searchDeclaration($scope.customerQuery.name,declarationQuery,currentPage-1,function (data) {
            $scope.declarations = data.result;//返回的数据
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.declarations, function(bills){
                $scope.declarations = bills;
            })
        })
    };

    /**
     * 发送报批文件
     * @param declaration
     */
    $scope.submit = function (declaration){

        if (declaration.customerClass == null || declaration.customerClass == ""){
            //alert("请先确定客户类别！再进行银行报批！");
            toaster.pop('info','银行报批','请先确定客户类别！再进行银行报批！');
            return;
        }
        if (confirm("你确定要发送银行报批文件?")){
            toaster.pop('info','银行报批','正在进行银行报批！');
            declarationService.submit($scope.declaration.id,function (data) {
                
                if (data == undefined){
                    toaster.pop('error','银行报批','报批失败！');
                } else {
                    toaster.pop('success','银行报批','申请报批成功！');
                    $scope.declaration.status = data.status;
                    $scope.submitText = "重新报批";
                    declarationService.getDeclarationHistorys(data.customerTransactionId,function (historys) {
                        $scope.declaration.historys = historys;
                    });
                }
            });
        }
    };

    /**
     * 银行反馈窗口
     */
    $scope.feedback = function () {
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:"feedback.html",
            controller:function($scope,$modalInstance){
                $scope.modalTitle = "银行反馈";
                $scope.declarationResult = {comment: null, result: 0, approvedCreditAmount:0, creditAmount:0};

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                $scope.saveDeclarationResult = function ($modalInstance) {

                    var result = $scope.declarationResult.result;
                    if (result == 0){
                        alert("请选择银行审批结果");
                        return;
                    } else if (result == 2 ||  result == 8){

                        if ($scope.declarationResult.approvedCreditAmount < ($scope.declaration.loan.applyAmount+$scope.declaration.loan.bankFeeAmount)){
                            alert("客户【"+$scope.declaration.customer.name+"】的银行批复贷款额度为【"+$scope.declarationResult.approvedCreditAmount+"】元，请尽快重审客户签约,调整客户贷款信息！");
                        }

                        //      同步  银行批付额度
                        $scope.declaration.loan.approvedCreditAmount = $scope.declarationResult.approvedCreditAmount;
                        customerService.updateApprovedAmount($scope.declaration.loan, function (data) {
                            $scope.declaration.loan = data;
                        });
                    }

                    $scope.sign({declarationResult: $scope.declarationResult});
                    modalInstance.close();
                };

                /*  关闭模态框 */
                $scope.close = function ($modalInstance){
                    modalInstance.close();
                }
            }
        })
    };

    /**
     * 记录报批结果
     * @param declaration
     */
    $scope.sign = function (declarationResult){
        $loading.start('declarationResult');
        declarationService.feedback($scope.declaration.id, declarationResult, function (data) {
            $scope.declaration.status = data.status;
            declarationService.getDeclarationHistorys(data.customerTransactionId,function (historys) {
                $scope.declaration.historys = historys;
            });
            $loading.finish('declarationResult');
        });
    };

    /**
     * 查询
     */
    $scope.search = function() {
        $scope.changePage(1);
    };

    //  给   客户  分类
    $scope.customerClass = function(declaration,customerclass){
        if(confirm("您确定将这位客户设置为"+customerclass.name+"客户")){
            $scope.saveDeclaration($scope.declaration);
        }else{
            //  要修改的类别
            var bb = declaration.customerClass;
            var aa = "";//  原本的客户类别
            declarationService.getOneByCustomerTransactionId(declaration.customerTransactionId,function (data) {
                aa = data.customerClass;
                if (aa != bb){
                    $scope.declaration.customerClass = aa;
                }
            })
        }
    };

    $scope.getMonthList = function (nums,curYear,curMonth,lastAccount,accountNum) {
        var months = [];
        var da = new Date();
        if(!curYear){
            curYear =  da.getFullYear();
        }
        if(!curMonth){
            curMonth = da.getMonth()+1;
        }
        if(lastAccount){
            curYear = lastAccount.year;
            curMonth = lastAccount.month -1;
        }
        for (var i = 0; i < nums; i++) {
            var one ={};
            if (accountNum == 1){
                one.id = $scope.declaration.paymentToIncome.accountList.length + i+1;
            } else if (accountNum == 2){
                one.id = $scope.declaration.paymentToIncome.accountList2.length + i+1;
            }
            one.year = curYear;
            one.month = curMonth;
            if(one.month ==0){
                one.year  =  curYear -1;
                one.month  = 12;
                curYear --;
                curMonth = 12;
            }
            curMonth--;
            one.income = '';
            months.push(one);
        }
        return months;
    };

    //      add  account
    $scope.addAccount = function (num) {
        var account = {};
        if (num == 1){
            account = $scope.declaration.paymentToIncome.accountList;
        } else if (num ==2 ){
            account = $scope.declaration.paymentToIncome.accountList2;
        }
        //     如果  银行流水有值  就去最后一个
        if (account.length > 0){
            var lastAccount = account[account.length-1];
        }
        var list = $scope.getMonthList(12,new Date().getFullYear(),new Date().getMonth(),lastAccount,num);
        for(var i=0; i<list.length; i++){
            if (num == 1){
                $scope.declaration.paymentToIncome.accountList.push(list[i]);
            } else if (num ==2 ){
                $scope.declaration.paymentToIncome.accountList2.push(list[i]);
            }
        }
    };
    //      remove  account
    $scope.removeAccount = function (index,num) {
        if (num == 1){
            $scope.declaration.paymentToIncome.accountList.splice(index,1);
        } else if (num == 2){
            $scope.declaration.paymentToIncome.accountList2.splice(index,1);
        }
    };

    $scope.$on('UpdateImageTypes',function (e,customerImageTypeCodes) {
        $scope.declaration.imageTypeCodes = customerImageTypeCodes;
    });

    //  保存配偶 单位信息
    $scope.updateMateCustomer = function (mateCustomer) {
        customerService.updateCustomerJob(mateCustomer.id,mateCustomer.customerJob,function (data) {
            $scope.declaration.cardemand.mateCustomer.customerJob = data.customerJob;
        });
    };

    $scope.save = function () {
        $scope.saveDeclaration($scope.declaration);
    };

    //保存银行报批数据
     $scope.saveDeclaration = function (declaration){
        declarationService.save(declaration, function (data) {
            $scope.declaration = data;
            $scope.findData(data);
        });
    };

    $scope.updateStageApplication = function (stageValue) {
        if (stageValue == 1){
            if (confirm('请先确保报批数据填写完整，再生成专项分期情况说明！是否生成？')){
                declarationService.save($scope.declaration, function (data) {
                    if (data && data.id){
                        $scope.createStageApplication(data.id);
                    }
                });
            }
        } else if (stageValue == 2){
            if (confirm('专项分期情况说明，已经生成！确认要重新生成？')){
                declarationService.save($scope.declaration, function (data) {
                    if (data && data.id){
                        $scope.createStageApplication(data.id);
                    }
                });
            }
        }
    };

    $scope.createStageApplication = function (id) {
        declarationService.updateDeclarationStageApplication(id,function (data) {
            if (data && data.stageApplication && data.stageApplication != ''){
                $scope.declaration.stageApplication = data.stageApplication;
                toaster.pop('success','银行报批专项分期情况','生成成功！');
            } else {
                toaster.pop('error','银行报批专项分期情况','生成失败！');
            }
        })
    }
    
});