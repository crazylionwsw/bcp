/**
 * Created by LB on 2017-8-11.
 */

app.factory('businesseventtypeService',function (Restangular){
    return {

        //取得事件类型列表
        lookup:function (callback) {
            var rest = Restangular.all('/businesseventtype/lookups');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        /**
         * 删除
         * @param id
         * @param callback
         */
        delete:function (id,callback) {
            var rest = Restangular.one('/businesseventtype',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/businesseventtypes?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        //添加或修改
        save:function (businessEventType,callback){
            var rest = Restangular.all('/businesseventtype');
            rest.post(businessEventType).then(function (data){
                callback(data);
            });
        },

        //唯一性验证
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/businessEventType/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        getOneByBusinessType:function (businessType,callback){
            var rest = Restangular.one("/businesseventtype",businessType);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

    }
});
