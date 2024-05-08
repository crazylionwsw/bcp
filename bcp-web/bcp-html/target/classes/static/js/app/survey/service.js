/**
 * Created by LB on 2016-10-24.
 */

app.factory('surveyService',function (Restangular){
    return {

        //保存
        saveSurvey:function (tid, results, callback){
            var rest = Restangular.one("/customertransaction",tid).all('survey');
            rest.post(results).then(function (data){
                callback(data);
            });
        },

        getSurveyResult: function(tid,callback){
            var rest = Restangular.one("/customertransaction",tid).all('survey');
            return rest.get('result').then(function (data){
                callback(data);
            });
        },

        /* 获取调查问卷 */
        getSurvey: function(tid,callback){
            var rest = Restangular.one("/customertransaction",tid).all('survey');
            return rest.get('').then(function (data){
                callback(data);
            });
        },
    }
});