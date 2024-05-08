/**
 * Created by LB on 2016-10-24.
 * 系统资源服务类
 */

app.factory('sysresourceService',function (Restangular){
    return {
        //查询树下级列表数据
        getChildren:function (id,callback){
            var rest = Restangular.one('sysresource',id).all('sysresources');
            rest.get('').then(function (data){
                callback(data);
            });
        },

        //保存
        save:function (sysresource,callback){
            var rest = Restangular.all('/sysresource');
            rest.post(sysresource).then(function (data){
                callback(data);
            });
        },

        lookup:function (callback) {
            var rest = Restangular.all('/sysresources/lookups');
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        checkUnique:function(entity, propname, propval, callback){
            var rest = Restangular.all('/validate/sysresource/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },
        
        getByIds: function (ids, callback){
            var rest = Restangular.all('/sysresources/pickup');
            rest.post(ids).then(function(data){
                callback(data);
            });
        },
        
        //  删除系统参配项信息---第一次删除：为作废，第二次删除：为真删除
        delete:function (sysResource, callback) {
            var rest = Restangular.one('/sysresource',sysResource.id)
            rest.remove().then(function (data) {
                callback(data);
            })
        },
        
    }
});
