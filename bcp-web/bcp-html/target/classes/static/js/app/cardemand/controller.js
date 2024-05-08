/**
 * Created by LB on 2016-10-24.
 */

'use strict';
app.controller('cardemandController', function ($scope, cardemandService, customerService, declarationService, sysparamService, customersurveytemplateService, questioncategoryService,customerTransactionService,workflowService, $state, $modal, toaster, $localStorage, $stateParams,$loading,$rootScope,$q,DTOptionsBuilder){
    $scope.title = "资质审查";
    $scope.modalTitle = "特殊情况说明";

    //返回的列表数据
    $scope.cardemands = [];
    $scope.cardemand = null;

    //返回工作流查询集合
    $scope.taskList = [];
    $scope.istask = {};

    $scope.signModal = {};

    //查询参数
    $scope.search = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A001'};
    $scope.searchFilter = {timeName:'ts',compensatory:true,business:true,statusValue:1,statusName:'approveStatus',creditMonths:0,nc:true,oc:true,pageSize:20,billTypeCode:'A001'};

    $scope.currentPage = 1;
    
    $scope.checked = false;

    //客户核对信息
    $scope.customercheck = {};

    $scope.customercheckCount = 0;

    $scope.specialComment = "";
    $scope.natures = [];
    $scope.customerclasses = [];

    //      问卷调查模板
    $scope.customersurveytemplates = [];
    $scope.customersurveytemplate = {};

    //      所有的问题
    $scope.questions=[];
    $scope.question={};

    //  某问题分类下的所有问题
    $scope.categoryquestions = [];
    $scope.customersurveyresult = {};

    //贷款金额合计
    $scope.totalCreditAmount = 0.0;
    //银行手续费合计
    $scope.totalBankFeeAmount = 0.0;

    $scope.customertypes = [];

    /**
     * 初始化加载列表
     */
    $scope.init = function (){

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

        // 获取所有的问卷调查模板
        customersurveytemplateService.lookup(function (customersurveytemplates) {
           $scope.customersurveytemplates = customersurveytemplates;
        });
        questioncategoryService.lookup(function (questioncategories) {
            $scope.questioncategories= questioncategories;
        });
        questioncategoryService.getAllQuestions(function (questions) {
            $scope.questions= questions;
        });
        if ($localStorage.CarDemandSearchFilter) {
            $scope.searchFilter = $localStorage.CarDemandSearchFilter;
            if ($scope.searchFilter.pageSize) $scope.pageSize = $scope.searchFilter.pageSize;
        }
      
        $scope.changePage(1);

        if ($stateParams.tid) { //从交易列表打开
            cardemandService.getOneByCustomerTransactionId($stateParams.tid, function(data){
                if (data && data.dataStatus == 1){
                    $scope.edit(data);
                } else {
                    alert("分期经理未提交该阶段数据！");
                }
            })
        }
        //$scope.intiDatatables();
    };
    //
    // $scope.intiDatatables = function(){
    //     $scope.dtOptions = DTOptionsBuilder.newOptions()
    //         .withOption('deferRender', true)
    //         .withOption('scrollCollapse', true)
    //         .withOption('searching', false)
    //         .withOption('ordering', false)
    //         .withOption('paging', false)
    //         .withOption('scrollY', 900)
    //         .withOption('scrollX', true)
    //         .withFixedColumns({
    //             leftColumns: 3
    //         });
    // };

    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){

        $scope.totalCreditAmount = 0.0;  //贷款金额合计清零
        $scope.totalBankFeeAmount = 0.0;  //银行手续费合计清零

        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        cardemandService.search($scope.searchFilter,$scope.currentPage-1,function (data){
            $scope.cardemands = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数

            customerTransactionService.mergeTransactions($scope.cardemands, function(bills){
                $scope.cardemands = bills;
                $rootScope.$broadcast("CalTotal", bills);
            })
        });
        
        $localStorage.CarDemandSearchFilter = $scope.searchFilter;
    };

    //接收广播，计算金额合计
    $scope.$on('CalTotal',function (e,bills) {
        if (bills){
            angular.forEach(bills,function (item, index) {
                customerService.getLoan(item.customerLoanId, function (data) {
                    if (data) {
                        $scope.addTotal(data.creditAmount);
                    }
                });

                customerService.getLoan(item.customerLoanId,function (data) {
                    if(data){
                        $scope.addBankFeeTotal(data.bankFeeAmount);
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

    $scope.addBankFeeTotal = function (amount) {
        if(amount)
            $scope.totalBankFeeAmount = $scope.totalBankFeeAmount+ amount;
    };

    //打开弹出层
    $scope.edit = function(cardemand, index){
        //检查当前是否有已选中的行
/*        if ($scope.cardemand) {
            $scope.cardemand = null;
            $scope.cardemands[index].selected = false;
        }*/
        //$state.go("app.business.cardemandedit",{id:cardemand.id});
        if (!$scope.checked) {
            //$scope.cardemands[index].selected = true;//高亮显示
            if ($scope.cardemand){
                $scope.cardemand = {};
            }
            if ($scope.declaration){
                $scope.declaration = {};
            }
            cardemandService.getOne(cardemand.id,function (data) {
                $scope.cardemand = data;
                $scope.checked = true;

                workflowService.getTaskCurrentGroup(data.billTypeCode + '.' + data.id, function (data) {
                    $scope.presentTask = data;
                });

                if (data.customerTransactionId){
                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                       $scope.cardemand.transaction = transaction;
                    });
                }
                if (data.customerId){
                    customerService.getOne(data.customerId, function(customer){
                        $scope.cardemand.customer = customer;
                    })
                }
                /**   获取购车信息      */
                if (data.customerCarId){
                    customerService.getCar(data.customerCarId, function(customerCar){
                        $scope.cardemand.customerCar = customerCar;
                    })
                }

                /**   获取借款信息  */
                if (data.customerLoanId){
                    customerService.getLoan(data.customerLoanId, function(customerLoan){
                        $scope.cardemand.customerLoan = customerLoan;
                    })
                }
            });
        }
    };

    //  前往银行报批页面，填写报批数据
    $scope.goDeclaration = function (bill) {
        if (bill && bill.customerTransactionId){
            $state.go('app.business.declaration',{tid:bill.customerTransactionId});
        }
    };


    $scope.init();
    
    /*        关闭  右边框      */
    $scope.close = function () {

        //更新列表数据
        cardemandService.getOne($scope.cardemand.id,function (data) {
            angular.forEach($scope.cardemands, function(item, index){
                if (item.id == data.id) {
                    if($scope.searchFilter.statusValue == -1){
                        $scope.cardemands[index] = data;
                    } else if ($scope.searchFilter.statusValue != data.approveStatus){
                        $scope.cardemands.splice(index,1);
                    } else {
                        $scope.cardemands[index] = data;
                    }

                    customerTransactionService.getOne(data.customerTransactionId,function (transaction) {
                        $scope.cardemands[index].transaction = transaction;
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
     * 查詢
     */
    $scope.search = function() {
        $scope.changePage(1);
    };

    $scope.changePageSize = function() {
        $scope.searchFilter.pageSize = $scope.pageSize;
        $scope.changePage(1);
    };

    /*                     模糊查询                           START                      */
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
        $scope.searchFilter = {timeName:'ts',creditMonths:0,compensatory:true,business:true,statusName:'approveStatus',statusValue:$scope.searchFilter.statusValue,nc:true,oc:true,pageSize:$scope.pageSize,billTypeCode:'A001'};
        $scope.changePage(1);
    };

    /**
     * 查询单据
     * @param search
     */
    $scope.queryBill = function (search) {
        $scope.searchFilter = search;
        $scope.changePage(1);
    };

    /*                     模糊查询                          END                      */
    
    /**
     * 获取客户第三方的核对信息
     * @param cardemand
     */
    $scope.customercheck = function(){
        $scope.modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'tpl/view/cardemand/check.html',
            controller:function($scope,$modalInstance){
                //查询客户核对信息
                if($scope.cardemand != null){
                    cardemandService.getcheckinfo($scope.cardemand.customerId,function (data) {
                        $scope.customercheck = data;
                    })
                }
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    $scope.modalInstance.close();
                };
            }
        })
    };

    $scope.$on('BillSign',function (e,id,signinfo,presentTaskGroup) {
        $loading.start('signInfo');
        cardemandService.sign(id, signinfo, function (data) {
            $loading.finish('signInfo');
            $rootScope.$broadcast("ShowTask");
            $rootScope.$broadcast("UpdateSignInfo", id);
        });
    });

    $scope.$on('BillTemSign',function (e,id,temSignInfo,presentTaskGroup) {
        $loading.start('temSignInfo');
        cardemandService.temSign(id, temSignInfo, function (data) {
            if(data && data.m){
                alert(data.m);
            }
            $loading.finish('temSignInfo');
            $rootScope.$broadcast("ShowTemTask");
            $rootScope.$broadcast("UpdateTemSignInfo", id);
        });
    });

    //      接收广播
    $scope.$on('UpdateTemSignInfo',function (e, id,num) {
        if (num && num ==2){
            return;
        }

        cardemandService.getOne(id,function (data) {
            $scope.cardemand.approveStatus = data.approveStatus;
            if (data && data.approveStatus == 2){
                if (confirm("请为客户分配调查问卷模板")){
                    $scope.createCustomerSurvey();
                }
                $scope.prompteNeedCounterGuarantor();
            }
            workflowService.getTaskCurrentGroup(data.billTypeCode + '.' + data.id, function (data) {
                $scope.presentTask = data;
            });
        })


    });

    //TODO  根据登录人的权限和审批流的状态判断是否显示该按钮
    /**
     * 分配调查模版   当登录人有审批人权限且审批任务已经完成时才可以操作
     */
    $scope.createCustomerSurvey = function() {
        $scope.modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            size: 'lg',
            backdrop: 'static',
            templateUrl: 'tpl/view/cardemand/survey.html',
            controller: function ($scope, $modalInstance) {

                $scope.canAnotherSave = false;

                $scope.canSave = true;

                if ($scope.cardemand.customerTransactionId){
                    customersurveytemplateService.getCustomerSurveyResultByTransactionId($scope.cardemand.customerTransactionId,function (data) {
                        if (data && data.id && data.customerSurveyTemplateId){
                            $scope.customersurveyresult.id = data.id;
                            $scope.customersurveyresult.customerSurveyTemplateId = data.customerSurveyTemplateId;
                            angular.forEach($scope.customersurveytemplates,function (item, index) {
                                if (item.id == data.customerSurveyTemplateId){
                                    $scope.customersurveyresult.questionIds = item.questionIds;
                                }
                            })
                        }else{
                            $scope.customersurveyresult = {customerTransactionId:$scope.cardemand.customerTransactionId};
                        }
                    })
                }

                //  某问题分类下的所有问题的Id
                $scope.questionCategory = {questionIds: []};

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance) {
                    $scope.modalInstance.close();
                };

                /**
                 *      选择  调查模板
                 * @param customersurveytemplate
                 */
                $scope.selectCustomerSurveyTemplate = function (customersurveytemplate) {
                    if ($scope.categoryquestions){
                        $scope.categoryquestions = [];
                    }
                    if ($scope.questionCategoryId){
                        $scope.questionCategoryId = [];
                    }
                    $scope.customersurveyresult.questionIds = customersurveytemplate.questionIds;
                };

                /**
                 *      选择  问题分类
                 * @param questioncategory
                 */
                $scope.selectQuestionCategory = function () {
                    var questioncategory = $scope.questionCategoryId[0];
                    if ($scope.questionCategory.id != questioncategory.id) {
                        $scope.questionCategory = questioncategory;
                        $scope.questionCategory.questionIds = [];
                        questioncategoryService.getQuestionsByQuestionCategoryId(questioncategory.id,function (categoryquestions) {
                            $scope.categoryquestions = categoryquestions;
                            angular.forEach(categoryquestions,function (item) {
                                if ($scope.customersurveyresult.questionIds && $scope.customersurveyresult.questionIds.indexOf(item.id) >= 0){
                                    $scope.questionCategory.questionIds.push(item.id);
                                }
                            });
                        })
                    }
                };

                /**
                 *      选择  问题
                 * @param question
                 */
                $scope.selectQuestion = function (question, model) {
                    if ($scope.customersurveyresult.questionIds.indexOf(question.id) < 0){
                        $scope.customersurveyresult.questionIds.push(question.id);
                        $scope.canAnotherSave = true;
                        $scope.canSave = false;
                    }

                };

                //      另存模板
                $scope.anotherSave = function () {
                    var modalInstance = $modal.open({
                        scope:$scope,
                        animation:true,
                        templateUrl:'name.html',
                        controller:function ($scope,$modalInstance,$q){
                            $scope.modalTitle = "另存模板";
                            /*  关闭模态框 */
                            $scope.cancel = function ($modalInstance){
                                modalInstance.close();
                            };

                            $scope.checkUnique = function(propname,propval) {
                                return $q(function(resolve, reject){
                                    var entity = $scope.customersurveytemplate;
                                    customersurveytemplateService.checkUnique(entity,propname,propval,function(data){
                                        if(data=="false"){
                                            reject();
                                        }else{
                                            resolve();
                                        }
                                    });
                                });
                            };

                            $scope.save = function () {
                                var customersurveytemplate = {name:$scope.templateName,code:$scope.templateCode,questionIds:$scope.customersurveyresult.questionIds};
                                $scope.saveQuestionTemplate(customersurveytemplate);
                               modalInstance.close();
                            }
                        }
                    });
                };
                
                $scope.saveSend = function () {
                    $scope.saveCustomerSurveyResult($scope.customersurveyresult);
                    $scope.modalInstance.close();
                };

                //保存模版
                $scope.saveQuestionTemplate = function (customersurveytemplate) {
                    customersurveytemplateService.save(customersurveytemplate,function (data) {
                        if (data && data.id){
                            toaster.pop('success','用户调查问卷','模板保存成功！');
                            $scope.customersurveytemplate = data;
                            $scope.customersurveytemplates.push(data);

                            $scope.customersurveyresult.customerSurveyTemplateId = data.id;
                            $scope.canAnotherSave = false;
                            $scope.canSave = true;
                        }else{
                            toaster.pop('error','用户调查问卷','模板保存失败！');
                        }
                    });
                };


                $scope.saveCustomerSurveyResult = function (customersurveyresult) {
                    if ($scope.customersurveyresult.id){
                        customersurveyresult.id = $scope.customersurveyresult.id;
                    }
                    customersurveyresult.customerTransactionId = $scope.cardemand.customerTransactionId;
                    customersurveytemplateService.saveCustomerSurveyResult(customersurveyresult,function (data) {
                        if ( data ){
                            $scope.customersurveyresult = data;
                            angular.forEach($scope.customersurveytemplates,function (item, index) {
                                if (item.id == data.customerSurveyTemplateId){
                                    $scope.customersurveyresult.questionIds = item.questionIds;
                                }
                            });
                            toaster.pop('success','用户调查问卷','已发送成功！');
                        }
                    });
                };

                $scope.closeSurvey = function () {
                    $scope.modalInstance.close();
                };
            }
        })
    };
    /*                     审查、审批                           START                      */
    
    /*                     特殊情况说明                           START                      */
    $scope.comment = function (){
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'commentForm.html',
            controller:function ($scope,$modalInstance,$q){

                $scope.save = function() {
                    cardemandService.save($scope.cardemand, function (data) {
                        $scope.cardemand = data;
                    });
                    modalInstance.close();
                };

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };
            }
        });
    };
    /*                     特殊情况说明                           END                      */
    
    /*                     征信拍照                           START                      */
    $scope.customercredit = function (customerId) {
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size:'lg',
            templateUrl:'creditForm.html',
            controller:function ($scope,$modalInstance,$q){
                $scope.cutomerid = customerId;

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };
            }
        });
    };
    /*                     征信拍照                           START                      */

    $scope.prompteNeedCounterGuarantor = function () {
        if ($scope.cardemand.id){
            if (confirm("是否需要反担保人")){
                var cardemandBean = {};
                cardemandService.getOne($scope.cardemand.id,function (cardemand) {
                    cardemandBean = cardemand;
                    cardemandBean.needCounterGuarantor = 1;
                    $scope.saveCustomerDemand(cardemandBean);
                })
            }
        }
    };
    
    //  保存是否需要反担保人信息
    $scope.saveCustomerDemand = function (cardemand) {
        cardemandService.save(cardemand,function (data) {
            $scope.cardemand.needCounterGuarantor = data.needCounterGuarantor;
        })
    };

    $scope.changeCustomerType = function (customer) {
        customerService.updateCustomerType(customer.id,customer.isSelfEmployed,function (data) {
            if ( data ) $scope.cardemand.customer = data;
        })
    }
});