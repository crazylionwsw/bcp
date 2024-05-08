/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('surveyController',function ($scope, surveyService, $stateParams, $modal, $q, $filter, $timeout){

    $scope.formData = {};
    $scope.formSchema = null;
    $scope.submitted = false;
    $scope.questionNums = 0;
    $scope.form = [];

    //列表
    $scope.init = function (){

        if($stateParams.id) {
            surveyService.getSurvey($stateParams.id, function (data) {

                $scope.survey = data;
                $scope.formSchema = {'type': 'object', 'title': '', 'properties': {}, 'required': []};

                if (data.categoryQuestions && data.categoryQuestions.length > 0) {
                    data.categoryQuestions = $filter('orderBy')(data.categoryQuestions, 'displayOrder');

                    angular.forEach(data.categoryQuestions, function(cat){

                        var form = {'type': "fieldset", 'title': cat.name, 'items': []};
                        if (cat.questionsList.length > 0) {
                            cat.questionsList = $filter('orderBy')(cat.questionsList, 'displayOrder');
                            angular.forEach(cat.questionsList, function(question) {
                                $scope.questionNums += 1;

                                $scope.formSchema.properties[question.code] = {
                                    'type': "string",
                                    'title': question.content
                                };

                                var formItem = {"key": question.code};
                                if (question.required == 1) {

                                    $scope.formSchema.required.push(question.code);
                                    formItem.validationMessage = {302: "此问题为必填项！"};
                                }

                                if (question.questionType == 'radio' || question.questionType == 'checklist') {
                                    var values = [];
                                    var options = [];
                                    question.questionOptions = $filter('orderBy')(question.questionOptions, 'displayOrder');
                                    angular.forEach(question.questionOptions, function(option){
                                        values.push(option.content);
                                        options.push({'value':option.content, 'name':option.content});
                                    });


                                    if (question.questionType == 'radio') {
                                        question.questionType = "radios-inline";
                                        $scope.formSchema.properties[question.code]['enum'] = values;
                                    }
                                    if (question.questionType == 'checklist') {
                                        question.questionType = "checkboxes";

                                        $scope.formSchema.properties[question.code].type = 'array';
                                        $scope.formSchema.properties[question.code]['items'] = {'type': 'string', 'enum': values};
                                    }

                                    formItem.type = question.questionType;
                                    formItem.titleMap = options;
                                }

                                form.items.push(formItem);
                            })
                        }

                        $scope.form.push(form);
                    })
                }
                ///console.info($scope.formTemplate);
            });

            surveyService.getSurveyResult($stateParams.id, function (data){
                angular.forEach(data, function(item){
                    $scope.formData[item.questionCode] = item.answerContents;
                })
            })
        }
    };

    /**
     * 执行初始化
     */
    $scope.init();

    //保存调查问卷
    $scope.submit = function (){

        $timeout(function() {
            angular.element('#surveyForm').trigger('submit');
            return;
        }, 0);
    };

    $scope.submitSurvey = function(form, e) {
        // First we broadcast an event so all fields validate themselves
        $scope.$broadcast('schemaFormValidate');

        if (form.$valid) {
            var results = [];
            angular.forEach($scope.formData, function (v, k) {
                results.push({'questionCode': k, 'answerContents': v});
            });

            surveyService.saveSurvey($stateParams.id, results, function (data) {
                $scope.submitted = true;
            });
        }

        e.preventDefault();
    }
});