/**
 * Created by LB on 2016-10-24.
 */

app.factory('sysparamService',function (Restangular){
    return {
        //查询列表数据
        getAll:function (callback){
            var rest = Restangular.all('/params');
            rest.get('').then(function (data){
                callback(data);
            });
        },

        //保存
        save:function (sysparam,callback){
            var rest = Restangular.all('/param');
            rest.post(sysparam).then(function (data){
                callback(data);
            });
        },

        //  删除系统参配项信息---第一次删除：为作废，第二次删除：为真删除
        delete:function (sysparam, callback) {
            var rest = Restangular.one('/param',sysparam.id)
            rest.remove().then(function (data) {
                callback(data);
            })
        },

        //唯一性验证
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/sysparam/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        //      获取 Map 类型的系统参配项
        getMapByCode : function (code, callback) {
            var rest = Restangular.one('/param/map',code);
            return rest.get('').then(function (data) {
                callback(data);
            })
        },
        //      获取 List 类型的系统参配项
        getListByCode : function (code, callback) {
            var rest = Restangular.one('/param/list',code);
            return rest.get('').then(function (data) {
                callback(data);
            })
        },
        //      获取 String 类型的系统参配项
        getStringByCode : function (code, callback) {
            var rest = Restangular.one('/param/string',code);
            return rest.get('').then(function (data) {
                callback(data);
            })
        },
        //      获取 Integer类型的系统参配项
        getIntegerByCode : function (code, callback) {
            var rest = Restangular.one('/param/int',code);
            return rest.get('').then(function (data) {
                callback(data);
            })
        }

    }
});