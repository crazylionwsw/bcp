/**
 * Created by LB on 2016-10-24.
 */

app.factory('subScribeService',function (Restangular){
    return {

        //保存
        save:function (msgsubscribe,callback){
            var rest = Restangular.all('/msg/subscribe/save');
            rest.post(msgsubscribe).then(function (data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/msg/subscribes?currentPage='+currentPage);
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
            var rest = Restangular.one('/msg/subscribe/',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },
        
        getWebSocketUrl:function (callback) {
            var rest = Restangular.all('/websocket');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        search:function (msgsubscribe, currentPage, callback){
            var rest = Restangular.all('/msg/subscribe/search?currentPage='+currentPage);
            rest.post(msgsubscribe).then(function (data){
                callback(data);
            });
        }

    }
});