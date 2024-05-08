/**
 * Created by LB on 2016-10-24.
 */

app.factory('msgTemplatelService',function (Restangular){
    return {

        //取得模版列表
        lookup:function (callback) {
            var rest = Restangular.all('/templates/lookups');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        /**
         * 删除模版
         * @param id
         * @param callback
         */
        delete:function (id,callback) {
            var rest = Restangular.one('/template',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/templates?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        //添加或修改
        save:function (template,temId,callback){
            var rest = Restangular.all('/template/save/'+temId);
            rest.post(template).then(function (data){
                callback(data);
            });
        },

        //唯一性验证
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/templateobj?propname='+propname+'&propval='+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },
        
        getOne:function (id,callback) {
            var rest = Restangular.one('/template',id);
            rest.get('').then(function(data){
                callback(data);
            });
        },
        
        getAll:function (callback) {
            var templateObjects = Restangular.all('/templates/all');
            templateObjects.get('').then(function (data){
                callback(data);
            })
        }

    }
});
