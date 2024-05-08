/**
 * Created by LB on 2016-10-24.
 */

app.factory('logManagementService',function (Restangular){
    return {

        //列表
        getAllList: function (callback) {
            var terminaldevices = Restangular.all('/loguser/count');
            return terminaldevices.get('').then(function (data) {
                callback(data);
            });
        },

        //下线
        offline:function (logmanagement,callback) {
            var rest = Restangular.one('/logmanagement/offline/'+logmanagement.logName+'/'+logmanagement.logType);
            rest.remove('').then(function(data){
                callback(data);
            });
        }
        
    }
});