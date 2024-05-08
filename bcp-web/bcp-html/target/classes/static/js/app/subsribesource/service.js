/**
 * Created by LB on 2016-10-24.
 */

app.factory('subsribesourceService',function (Restangular){
    return {

        //保存
        save:function (subsribesource,callback){
            var rest = Restangular.all('/subsribesource');
            rest.post(subsribesource).then(function (data){
                callback(data);
            });
        },

        /*  订阅源分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/subsribesources?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        //取得可用订阅源列表
        lookup:function (callback) {
            var rest = Restangular.all('/subsribesources/lookups');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        //获得订阅组列表
        getGroupName:function (callback) {
            var rest = Restangular.all('/msg/subscribe/groups');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        delete:function (id,callback) {
            var rest = Restangular.one('/subsribesource',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        //唯一性验证
        checkUnique:function(entity,propname,propvalue,callback){
            var rest = Restangular.all('/validate/subsribesource?propname='+propname+'&propvalue='+propvalue);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        getByIds: function (ids, callback){
            var rest = Restangular.all('/subsribesources/pickup');
            rest.post(ids).then(function(data){
                callback(data);
            });
        },

        getNameByGroupName: function (groupName, callback){
            var rest = Restangular.one('/msg/subscribe/source?groupName='+groupName);
            rest.get('').then(function(data){
                callback(data);
            });
        }


    }
});