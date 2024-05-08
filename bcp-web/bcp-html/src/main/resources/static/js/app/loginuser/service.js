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

        lookUp: function (callback){
            var rest = Restangular.all('/user/lookup');
            return rest.get('').then(function(data){
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

        /*查询*/
        search:function (loginUser, currentPage, callback){
            var loginuser = Restangular.all('/users/search?currentPage='+currentPage);
            loginuser.post(loginUser).then(function (data){
                callback(data);
            });
        },

        save:function (user,callback){
            var model = Restangular.all('/user');
            model.post(user).then(function (data){
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
         * 根据用户名查询  登录用户
         */
        getOneByUserName:function (username,callback) {
            var rest = Restangular.one("/user",username);
            return rest.get('').then(function (data) {
                callback(data);
            })
        },

        checkUnique:function(entity,propname,propval,callback){
            var newCodeList = Restangular.all('/validate/user/'+propname+'/'+propval);
            newCodeList.post(entity).then(function(data){
                callback(data);
            })
        },
        
        //  删除系统参配项信息---第一次删除：为作废，第二次删除：为真删除
        delete:function (user, callback) {
            var userInfo = Restangular.one('/user',user.id)
            userInfo.remove().then(function (data) {
                callback(data);
            })
        },

        //恢复用户
        renewLoginUser:function (user,callback){
            var model = Restangular.all('/renew/user');
            model.post(user).then(function (data){
                callback(data);
            });
        }

    }
});
