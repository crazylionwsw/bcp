/**
 * Created by admin on 2017/10/13.
 */

app.factory('pushTemplateService',function (Restangular){
    return {

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/pushtemplates?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        /**
         * 保存
         */
        save:function (pushtemplate,callback){
            var rest = Restangular.all('/pushtemplate');
            rest.post(pushtemplate).then(function (data){
                callback(data);
            });
        },

        /**
         * 删除推送模板
         * @param id
         * @param callback
         */
        delete:function (id,callback) {
            var rest = Restangular.one('/pushtemplate',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },


        /**
         * 基础数据，唯一性验证
         */
        checkUnique:function(entity, propname, propval, callback){
            var rest = Restangular.all('/validate/pushtemplate/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        //获取邮件模板
        lookup:function (callback) {
            var rest = Restangular.all('/pushtemplates/lookups');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

    }
});