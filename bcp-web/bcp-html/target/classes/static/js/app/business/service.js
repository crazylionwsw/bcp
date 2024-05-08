/**
 * Created by LB on 2016-10-24.
 */

app.factory('businessService',function (Restangular){
    return {

        //保存
        save:function (business,callback){
            var rest = Restangular.all('/businesstype');
            rest.post(business).then(function (data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/businesses?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        //取得可用业务类型列表
        lookup:function (callback) {
            var rest = Restangular.all('/businesstypes/lookups');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        //取得可用业务类型列表(带利率）
        lookupWithRates:function (callback) {
            var rest = Restangular.all('/businesstypes/lookup/rates');
            rest.get('').then(function (data) {
                callback(data);
            });
        },        

        delete:function (id,callback) {
            var rest = Restangular.one('/businesstype',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        //唯一性验证
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/business/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        getByIds: function (codes, callback){
            var rest = Restangular.all('/businesstype/pickup');
            rest.post(codes).then(function(data){
                callback(data);
            });
        },
    }
});