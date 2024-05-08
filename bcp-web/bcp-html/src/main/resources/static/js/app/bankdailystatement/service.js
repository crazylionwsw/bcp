/**
 * Created by admin on 2017-11-17.
 */

app.factory('bankdailystatementService',function (Restangular){
    return {
        /**
         *      根据 渠道行、报单行 日期 查询 某天的 报单行 所有的手续费分成情况
         * @param poundageSettlement
         * @param callback
         */
        searchdeclaration:function (poundageSettlement, callback){
            var bankdailystatements = Restangular.all('/bankdailystatement/searchdeclaration');
            bankdailystatements.post(poundageSettlement).then(function (data){
                callback(data);
            });
        },
        /**
         *      根据 渠道行、报单行 日期 查询 某天的 渠道行 所有的手续费分成情况
         * @param poundageSettlement
         * @param callback
         */
        searchchannel:function (poundageSettlement, callback){
            var bankdailystatements = Restangular.all('/bankdailystatement/searchchannel');
            bankdailystatements.post(poundageSettlement).then(function (data){
                callback(data);
            });
        },
        /**
         *      根据 渠道行、报单行 日期 查询 某天的 渠道行 所有的手续费分成情况
         * @param poundageSettlement
         * @param callback
         */
        searchtotal:function (poundageSettlement,waycode,callback){
            var bankdailystatements = Restangular.all('/bankdailystatement/summary/'+waycode);
            bankdailystatements.post(poundageSettlement).then(function (data){
                callback(data);
            });
        },

    }
});