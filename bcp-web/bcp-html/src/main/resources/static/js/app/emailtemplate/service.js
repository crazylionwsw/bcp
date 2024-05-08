/**
 * Created by LB on 2016-10-24.
 */

app.factory('emailTemplateService',function (Restangular){
    return {

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/emailobjects?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        /**
         * 保存
         */
        save:function (emailobject,callback){
            var rest = Restangular.all('/emailobject');
            rest.post(emailobject).then(function (data){
                callback(data);
            });
        },

        /**
         * 删除文档模版
         * @param id
         * @param callback
         */
        delete:function (id,callback) {
            var rest = Restangular.one('/emailobject',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },


        /**
         * 基础数据，唯一性验证
         */
        checkUnique:function(entity, propname, propval, callback){
            var rest = Restangular.all('/validate/emailobject/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        //获取邮件模板
        lookup:function (callback) {
            var rest = Restangular.all('/emailobjects/lookups');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

    }
});