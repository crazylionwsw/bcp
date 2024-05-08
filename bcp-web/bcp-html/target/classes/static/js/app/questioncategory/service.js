/**
 * Created by zxp on 2017/8/31.
 */
app.factory('questioncategoryService',function (Restangular){
    return{
        
        lookup:function (callback) {
            var rest = Restangular.all('/questioncategory/lookups');
            rest.get('').then(function (data) {
                callback(data);
            })
        },

        //查询树列表数据
        getQuestioncategorys:function (id, callback){
            if(!id) {
                id = "0";
            }
            var rest = Restangular.one('/questioncategory', id).all("questioncategorys");
            rest.get('').then(function (data){
                callback(data);
            });
        },

        /**
         *      根据  问题分类ID  获取  问题列表
         * @param id
         * @param callback
         * @returns {*|Promise}
         */
        getQuestions: function (questioncategoryId,currentPage,callback){
            var rest = Restangular.one('questioncategory',questioncategoryId).all('questionspage');
            return rest.get("", {currentPage: currentPage}).then(function (data){
                callback(data);
            });
        },

        /**
         *          根据 问题分类ID 获取所有的问题
         * @param questioncategoryId
         * @param callback
         * @returns {*|Promise}
         */
        getQuestionsByQuestionCategoryId: function (questioncategoryId,callback){
            var rest = Restangular.one('questioncategory',questioncategoryId).all('questions');
            return rest.get("").then(function (data){
                callback(data);
            });
        },

        //保存问题分类
        saveQuestionCategory:function (questioncategory,callback) {
            var rest = Restangular.all('/questioncategory');
            rest.post(questioncategory).then(function (data) {
                callback(data);
            });
        },

        /**
         *      根据ID回显问题分类
         * @param id
         * @param callback
         * @returns {*|Promise}
         */
        getQuestionCategory:function(id,callback){
            var rest=Restangular.one("/questioncategory",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        checkUnique:function(questioncategory,propname,propval,callback){
            var rest = Restangular.all('/validate/questioncategory/'+propname+'/'+propval);
            rest.post(questioncategory).then(function(data){
                callback(data);
            })
        },

        /**
         * 根据ID回显
         */
        getQuestion:function(id,callback){
            var rest=Restangular.one("/question",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        getQuestionByCode:function(code,callback){
            var rest=Restangular.one("/question/code",code);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        /**
         *      获取 所有的问题
         * @param callback
         */
        getAllQuestions:function (callback) {
            var rest = Restangular.all('/questions');
            rest.get('').then(function (data) {
                callback(data);
            })
        },

        saveQuestion:function (question,callback){
            var rest = Restangular.all('/question');
            rest.post(question).then(function (data){
                callback(data);
            });
        },
        deleteQuestion:function (id,callback) {
            var rest=Restangular.one("/questioncategory",id);
            rest.remove('').then(function (data) {
                callback(data);
            })
        },
        

        //唯一性验证
        checkUniqueQuestion:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/question/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },
        
        search:function (questionQuery,currentPage,callback) {
            var rest=Restangular.all('/question/search?currentPage='+currentPage);
            rest.post(questionQuery).then(function (data) {
                callback(data);
            })
        }

    }
});
