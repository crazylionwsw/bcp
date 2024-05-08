/**
 * Created by LB on 2016-10-24.
 */

app.factory('versionService',function (Restangular){

    return {

        //关于分页功能
        getPageData : function (currentPage,callback){
            var rest = Restangular.all('/apkversions?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },


        saveVersion:function(version,callback) {
            var rest = Restangular.all('/apkversion');
            rest.post(version).then(function (data) {
                callback(data);
            });
        },

        //TODO:删除
        delete:function(id,callback) {
            var rest = Restangular.one('/apkversion',id);
            rest.remove('').then(function (data) {
                callback(data);
            });
        },

        //唯一性验证
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/apkversion?propname='+propname+'&propval='+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        }
    }
});

