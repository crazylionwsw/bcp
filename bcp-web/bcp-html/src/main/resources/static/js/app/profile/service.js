/**
 * Created by LB on 2016-10-24.
 */

app.factory('loginUserService',function (Restangular){
    return {
        getAll: function (callback){
            var model = Restangular.all('/users');
            return model.get('').then(function(data){
                callback(data);
            });
        },
        //关于分页功能
        getPageData : function (currentPage,callback){
            var model = Restangular.all('/users?currentPage='+currentPage);
            return model.get('').then(function (data){
                callback(data);
            });
        },

        save:function (user,callback){
            var model = Restangular.all('/user');
            model.post(user).then(function (data){
                callback(data);
            });
        },

        getAllSysRoles:function (callback) {
            var model = Restangular.all('/roles');
            return model.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 根据id获取
         */
        getOne:function (id,callback){
            var rest = Restangular.one("/user",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        /**
         * 根据id获取
         */
        getOneloginuser:function (id,callback){
            var rest = Restangular.one("/loginuser",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        checkUniqueUser:function(entity,propname,propval,callback){
            var newCodeList = Restangular.all('/validate/user/'+propname+'/'+propval);
            newCodeList.post(entity).then(function(data){
                callback(data);
            })
        }

    }
});
