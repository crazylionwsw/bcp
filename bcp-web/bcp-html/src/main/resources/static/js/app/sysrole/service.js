/**
 * Created by LB on 2016-10-24.
 */

app.factory('sysroleService',function (Restangular){
    return {

        getDataByPage:function (currentPage,callback){
            var rest = Restangular.all('/roles?currentPage='+currentPage);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        save:function(sysrole,callback){
            var rest = Restangular.all('/role');
            rest.post(sysrole).then(function (data){
                callback(data);
            });
        },
        
        checkUniqueSysRole:function(entity, propname, propval, callback){
            var rest = Restangular.all('/validate/role/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        getByIds: function (ids, callback){
            var rest = Restangular.all('/roles/pickup');
            rest.post(ids).then(function(data){
                callback(data);
            });
        },
        
       //  删除系统参配项信息---第一次删除：为作废，第二次删除：为真删除
        delete:function (sysrole, callback) {
            var rest = Restangular.one('/role',sysrole.id)
            rest.remove().then(function (data) {
                callback(data);
            })
        },

        lookup:function (callback) {
            var rest = Restangular.all('/roles/lookups');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 根据id获取
         */
        getOne:function (id,callback){
            var rest = Restangular.one("/role",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getUserApproval:function (id,callback){
            var rest = Restangular.one("/user/" + id + '/approval');
            return rest.get('').then(function(data){
                callback(data);
            });
        },
    }
});

