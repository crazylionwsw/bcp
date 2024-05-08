/**
 * Created by LB on 2016-10-24.
 */

app.factory('msgService',function (Restangular){
    return {

        //保存
        save:function (messageTemplate,callback){
            var rest = Restangular.all('/msg/messageTemplate/save');
            rest.post(messageTemplate).then(function (data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/msg/messageTemplates?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        getSendType:function (callback) {
            var rest = Restangular.all('/msg/messageTemplate/lookupchannels');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        getDescType:function (callback) {
            var rest = Restangular.all('/msg/messageTemplate/lookupTaskType');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        delete:function (id,callback) {
            var rest = Restangular.one('/msg/messageTemplate/',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        //唯一性验证
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/messagetemplate/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        getMessageById:function (id,callback) {
            var rest = Restangular.one('/msg/log/',id);
            rest.get('').then(function(data){
                callback(data);
            });
        },

        getMessageLogByIds:function (ids,callback) {
            var rest = Restangular.one('/msg/logs',ids);
            rest.get('').then(function(data){
                callback(data);
            });
        }

    }
});