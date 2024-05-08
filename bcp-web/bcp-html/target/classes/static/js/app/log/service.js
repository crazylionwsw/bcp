/**
 * Created by LB on 2016-10-24.
 */

app.factory('logService',function (Restangular){
    return {
        //关于分页功能
        getPageData: function (currentPage, callback) {
            var loginlog = Restangular.all('/loginlogs/page?currentPage=' + currentPage);
            return loginlog.get('').then(function (data) {
                callback(data);
            });
        },
        
        //删除
        delete:function (loginlog,callback){
            var loginlogInfo = Restangular.one('/loginlog',loginlog.id);
            loginlogInfo.remove().then(function (data){
                callback(data);
            });
        },
    }

});