/**
 * Created by LB on 2016-10-24.
 */

app.factory('customersurveytemplateService',function (Restangular){
    return {
        
        save:function (customersurveytemplate,callback){
            var rest = Restangular.all('/customersurveytemplate');
            rest.post(customersurveytemplate).then(function (data){
                callback(data);
            });
        },
        
        delete:function (id,callback) {
            var rest = Restangular.one('/customersurveytemplate',id);
            rest.remove('').then(function (data) {
                callback(data);
            });
        },

        getOne:function (id,callback) {
            var rest = Restangular.one('/customersurveytemplate',id);
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/customersurveytemplates?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        lookup: function(callback){
            var rest = Restangular.all('/customersurveytemplates/lookups');
            return rest.get('').then(function (data){
                callback(data);
            });
        },
      
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/customersurveytemplate/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        /**
         *      保存 交易的调查结果数据
         * @param customersurveyresult
         * @param callback
         */
        saveCustomerSurveyResult:function (customersurveyresult,callback) {
            var rest = Restangular.all('/customersurveyresult');
            rest.post(customersurveyresult).then(function (data){
                callback(data);
            });
        },

        //  通过交易ID查询客户分配的调查模板ID
        getCustomerSurveyResultByTransactionId:function (transactionId, callback) {
            var surveyResult = Restangular.one("/customersurveyresult/transaction",transactionId);
            return surveyResult.get('').then(function(data){
                callback(data);
            });
        }
    }
});