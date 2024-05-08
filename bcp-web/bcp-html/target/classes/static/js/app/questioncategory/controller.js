/**
 * Created by zxp on 2017/8/31.
 */

app.controller('questioncategoryController',[ '$scope','questioncategoryService','$modal','toaster','$q',function ($scope,questioncategoryService,$modal,toaster,$q) {

    var treeData,treeCtrl;

    $scope.title = '分类问题管理';
    $scope.tableTitle = "问题列表";

    $scope.questioncategoryTree = treeCtrl={};
    $scope.selectLabel = {};

    $scope.questioncategorys = treeData =  [];
    $scope.questioncategory = {};
    $scope.selectedQuestioncategory = null;
    
    $scope.questions=[];
    $scope.question={};
    $scope.questionFilter={};

    $scope.checked=false;
    $scope.one = false;
    $scope.saved = false;
    $scope.noCode = false;

    $scope.selectBranch = null;

    $scope.selectedQuestion=null;

    $scope.currentPage = 1;
    $scope.data=null;

    $scope.availableQuestionCategorys = [];

    $scope.options =[];
    $scope.option ={};

    /**
     * 初始化
     */
    $scope.init = function (){
        $scope.getRootNode();
        $scope.getAllQuestionCategory();
    };

    /**
     * 选定分类问题
     * @param branch
     */
    $scope.selectQuestioncategory = function(branch) {
        $scope.selectBranch = branch;
        branch.selected = true;
        if (branch.children.length == 0) {
            $scope.getChildData(branch.id);
        }

        $scope.selectLabel = branch.label;
        if ($scope.selectedQuestioncategory)  $scope.selectedQuestioncategory.selected = false;
        if (branch.data) {
            $scope.selectedQuestioncategory = branch.data;
            $scope.getQuestions();
        } else {
            $scope.selectedQuestioncategory = null;
            $scope.getQuestions();
        }
    };
    $scope.getAllQuestionCategory = function () {
        questioncategoryService.lookup(function(data){
            $scope.availableQuestionCategorys = data;
        });
    };


    /**
     * 获取左边选中的问题列表
     */
    $scope.getQuestions = function (){
        $scope.changeQuestionPage();
    };

    /*分页功能*/
    $scope.changeQuestionPage = function (){
        var questioncategoryId = "0";

        if($scope.selectedQuestioncategory)
            questioncategoryId = $scope.selectedQuestioncategory.id;
            questioncategoryService.getQuestions(questioncategoryId, $scope.currentPage - 1,function(data){
                $scope.questions = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1 ;//当前页
                $scope.totalPages = data.totalPages;//总页数
            });
    };

   
    /**
     * 构造根节点
     */
    $scope.getRootNode = function() {
        treeData.push({label:'问题分类',id:'0', uid:null, data:null});
        $scope.questioncategorys = treeData;
    };

    //验证问题编码唯一
    $scope.checkUnique = function(propname,propval) {
        if (propval != "" && propval != undefined){
            return $q(function(resolve, reject){
                var entity = $scope.question;
                questioncategoryService.checkUniqueQuestion(entity,propname,propval,function(data){
                    if(data=="false"){
                        reject();
                    }else{
                        resolve();
                    }
                });
            });
        }
    };

    /**
     * 取得下级节点
     * @param parentData
     */
    $scope.getChildData = function(parentData){
        questioncategoryService.getQuestioncategorys(parentData,function(data){
            var selectBranch =  treeCtrl.get_selected_branch();
            data.forEach(function(item){
                var treenode ={label:item.name,id:item.id,uid:item.id,data:item};
                treeCtrl.add_branch(selectBranch,treenode);
            });
        });
    };
    

    /**
     * 执行初始化
     */
    $scope.init();

    //弹出层控制
    $scope.editOptions=function (question,index) {

        var questionCategory = null;
        if(question.id != null && question.id != ''){
            $scope.modalTitle = "编辑问题明细";
            //修改问题时，编码禁用不允许修改
            $scope.noCode = true;
            if (!$scope.checked) {
                if ($scope.question){
                    $scope.question = {};
                }
                if ($scope.options){
                    $scope.options = [];
                }
                if ($scope.option){
                    $scope.option = {};
                }

                questioncategoryService.getQuestion(question.id, function (data) {
                    $scope.question = data;
                    $scope.options = question.questionOptions;
                    $scope.checked = true;
                });
            }
        }else{
            if($scope.selectedQuestioncategory) {
                $scope.modalTitle = "添加问题明细";
                $scope.noCode = false;
                if (!$scope.checked){
                    if ($scope.question){
                        $scope.question = {};
                    }
                    if ($scope.options){
                        $scope.options = [];
                    }
                    if ($scope.option){
                        $scope.option = {};
                    }
                    $scope.checked = true;
                    $scope.questioncategory = $scope.selectedQuestioncategory.name;
                }
            }else {
                alert("请选择问题分类");
            }
        }

        $scope.addOption = function () {
            $scope.inserted = {
                content: '',
                displayOrder:'',
                isNew: true
            };
            $scope.options.push($scope.inserted);
        };

        $scope.removeOption = function (index) {
            $scope.options.splice(index,1);
        };


        //当问题类型为单选，可选答案数量为1
        $scope.Change = function (questionType) {
            if(questionType == "radio"){
                $scope.question.optionalAnswersQuantity = 1;
                $scope.one = true;
            }else if(questionType == "checklist"){
                $scope.one = false;
            }else if(questionType == "text"){
                $scope.question.optionalAnswersQuantity = 0;
                $scope.one = true;
            }
        };

        //答案详情的答案内容
        $scope.checkContent = function(data) {
            if (data == undefined || data == "" || data == null) {
                return "答案内容未填写！";
            }
        };
        //答案详情的显示顺序
        $scope.checkDisplayOrder = function(data) {
            if (data == undefined || data == "" || data == null) {
                return "显示顺序未填写！";
            }
        };

        /*保存或修改问题信息*/
        $scope.saveform = function () {

            $scope.saved = true;
            var question = $scope.question;
            question.questionOptions = [];
            //答案内容为空的数据不保存
            angular.forEach($scope.options,function (item,index) {
                if(item.content != "" && item.content != null){
                    question.questionOptions.push(item);
                }
            });

            questioncategoryService.saveQuestion(question, function (data) {
                if ($scope.question.id == "" || $scope.question.id == null) {
                    $scope.questions.push(data);
                    toaster.pop('success', '新增问题', '保存成功！');
                } else {
                    //var index = $scope.cardealers.indexOf($scope.cardealer);
                    $scope.questions[index] = data;
                    toaster.pop('success', '修改问题', '保存成功！');
                }
                $scope.one = false;
                $scope.closeTan();
                $scope.selectQuestioncategory($scope.selectBranch);
            });
            $scope.saved = false;
        }
    };

    /*        关闭  右边框      */
    $scope.closeTan = function () {
        //关闭后清空所有已输入的内容
        $scope.question.code = null;
        $scope.question.questionType = null;
        $scope.question.required = null;
        $scope.question.optionalAnswersQuantity = null;
        $scope.question.displayOrder = null;
        $scope.question.content = null;
        $scope.question.questionOptions = [];

        $scope.userForm.$setPristine();
        //答案详情关闭不保存
        $scope.changeQuestionPage();
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    //查询
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:"query.html",
            controller:function($scope,$modalInstance){

            }
        })
    };

    //编辑问题分类
    $scope.editQuestionType = function (questioncategory){

        //1是个修改，0是添加
        if(questioncategory == 1){
            $scope.modalTitle="编辑问题分类";
            $scope.questioncategory = $scope.selectedQuestioncategory;
            //$scope.orginfo = angular.copy($scope.selectedOrginfo);
        }else{
            $scope.modalTitle="添加问题分类";
            $scope.questioncategory = {dataStatus: 1};
            if ($scope.selectedQuestioncategory)
                $scope.questioncategory.parentId = $scope.selectedQuestioncategory.id;
        }

        /*模态框*/
        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "questionCategoryForm.html",
            controller: function ($scope) {

                /*关闭模态框*/
                $scope.cancelQuestionType = function () {
                    modalInstance.close();
                };

                /*保存信息*/
                $scope.saveQuestionType = function () {
                    if($scope.questioncategory.parentId == "" || $scope.questioncategory.parentId == null){
                        $scope.questioncategory.parentId = "0";
                    }

                    questioncategoryService.saveQuestionCategory($scope.questioncategory, function (result) {
                        var treenode ={label:result.name,id:result.id,uid:result.id,data:result};
                        var selectedBranch =  treeCtrl.get_selected_branch();

                        if ($scope.questioncategory.id == "" || $scope.questioncategory.id == null) {
                            treeCtrl.add_branch(selectedBranch ,treenode);
                        } else {
                            selectedBranch.label = result.name;
                        }
                        modalInstance.close();
                        toaster.pop('success', '问题分类', '保存成功！');
                    });
                }
            }
        });
    };


    //问题分类验证唯一
    $scope.checkUniqueQueCategory = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.questioncategory;
            questioncategoryService.checkUnique(entity,propname,propval,function(data){
                if(data=='false'){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };


    //删除问题
    $scope.deleteQuestion=function (question,index) {
        var msg="确定要作废问题"+question.content+"?";
        var msg2 = "作废成功";
        if(confirm(msg)){
            var id = question.id;
            questioncategoryService.deleteQuestion(id,function (data) {
                if (question.dataStatus == 9) {
                    $scope.questions.splice(index, 1);
                } else {
                    $scope.questions[index] = data;
                }
                $scope.changeQuestionPage();
                toaster.pop('success','问题', msg2);
            })
        }
    };


    //启用问题
    $scope.renew = function (question,index) {
        if(confirm("确定要恢复该条数据?")){
            question.dataStatus = 1;
            questioncategoryService.saveQuestion(question,function (data) {
                // if(question.dataStatus == 1){
                //     $scope.questions[index] = data;
                // }else{
                //     alert("恢复数据出错!")
                // }
            })
        }
    };


   

    /*
     * 清空查询
     */
    $scope.clearQuery = function() {
        $scope.questionFilter = {};
        $scope.changeQuestionPage();
    };


    /**
     * 查询
     * @param question
     */
    $scope.queryBill=function (question) {

        if($scope.selectedQuestioncategory != null && $scope.selectedQuestioncategory.id){
            question.questionCategoryId = $scope.selectedQuestioncategory.id;
        }else {
            question.questionCategoryId = null;
        }

        $scope.questionFilter = question;
        $scope.currentPage = 1;
        questioncategoryService.search(question, $scope.currentPage-1,function(data){
            $scope.questions = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });

    }

}]);
