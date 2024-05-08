/**
 * Created by LB on 2016-10-24.
 */

app.factory('billtypeService',function (Restangular){
    return {

        //取得单据列表
        lookup:function (callback) {
            var rest = Restangular.all('/billtypes/lookups');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        /**
         * 删除单据
         * @param id
         * @param callback
         */
        delete:function (id,callback) {
            var rest = Restangular.one('/billtype',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/billtypes?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        //添加或修改
        save:function (billtype,callback){
            var rest = Restangular.all('/billtype');
            rest.post(billtype).then(function (data){
                callback(data);
            });
        },

        //唯一性验证
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/billtype/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        getAllContracts: function (callback){
            var rest = Restangular.all('/documents');
            rest.get('').then(function (data){
                callback(data);
            });
        },
        
        getOneByCode : function (code, callback) {
            var rest = Restangular.one('/billtype',code);
            rest.get('').then(function (data) {
                callback(data);
            })
            
        }
    }
});
