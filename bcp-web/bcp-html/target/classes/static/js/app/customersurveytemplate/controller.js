/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('customersurveytemplateController',function ($scope,customersurveytemplateService,questioncategoryService,$modal,$q){

    $scope.tableTitle = "问卷调查管理";
    $scope.tableDesc = "问卷调查管理";
    
    /*  列表 */
    $scope.customersurveytemplates  = [];//返回列表数据
    $scope.customersurveytemplate = {};

    /**   问题分类列表  */
    $scope.questioncategories = [];
    $scope.questioncategory = {};
    
    /**
     *      模板客户选择的问题
     * @type {Array}
     */
    $scope.questions=[];
    $scope.question={};
    
    $scope.questionIds = [];
    
    //  某问题分类下的所有问题的Id
    $scope.questionCategory = {questionIds: []};

    $scope.currentPage = 1;

    $scope.init = function (){
        questioncategoryService.lookup(function (questioncategories) {
            $scope.questioncategories= questioncategories;
        });
        questioncategoryService.getAllQuestions(function (questions) {
            $scope.questions= questions;
            /*angular.forEach(questions,function (item) {
                $scope.questionIds.push(item.id);
            });*/
        });
        $scope.changePage();
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (){
        customersurveytemplateService.getDataByPage($scope.currentPage-1,function (data){
            $scope.customersurveytemplates = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();


    /*  添加 / 修改 */
    $scope.edit = function (customersurveytemplate,index){
        /*  判断id进行回显 */
        if(customersurveytemplate.id != "" && customersurveytemplate.id != null){
            $scope.modalTitle="编辑问卷调查模板";
            $scope.customersurveytemplate = angular.copy(customersurveytemplate)
        } else {
            $scope.modalTitle="添加问卷调查模板";
            $scope.customersurveytemplate = {dataStatus: 1,questionIds:$scope.questionIds};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'customersurveytemplateModal.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance){
                
                $scope.questionCategoryId = [];
                
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
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
                                if ($scope.customersurveytemplate.questionIds && $scope.customersurveytemplate.questionIds.indexOf(item.id) >= 0){
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
                $scope.selectQuestion = function (question) {
                    if ($scope.customersurveytemplate.questionIds.indexOf(question.id) < 0){
                        $scope.customersurveytemplate.questionIds.push(question.id);
                    }
                };

                /* 保存业务信息 */
                $scope.save = function (){
                    var customersurveytemplate = $scope.customersurveytemplate;
                    customersurveytemplateService.save($scope.customersurveytemplate,function (data){
                        if (!$scope.customersurveytemplate.id) {
                            $scope.customersurveytemplates.push(data);
                        } else {
                            //var index = $scope.customersurveytemplates.indexOf(customersurveytemplate);
                            $scope.customersurveytemplates[index] = data;
                        }
                        //$scope.customersurveytemplate = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }
            }
        });
    };

    /* 删除 */
    $scope.delete = function (customersurveytemplate,index) {
        var msg = "确定要作废此条数据？";
        if(customersurveytemplate.dataStatus == 9){
            msg = "确定要删除此条数据？";
        }
        if (confirm(msg)) {
            var id = customersurveytemplate.id;
            customersurveytemplateService.delete(id, function (data) {
               $scope.init();
            });
        }
    };
    //恢复数据
    $scope.renew = function (customersurveytemplate,index) {
        if(confirm("确定要恢复该条数据?")){
            customersurveytemplate.dataStatus = 1;
            customersurveytemplateService.save(customersurveytemplate,function (data) {
                if(customersurveytemplate.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };
    
    /**
     * 唯一性验证
     * @param propname
     * @param propval
     * @returns {*}
     */
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
    
});