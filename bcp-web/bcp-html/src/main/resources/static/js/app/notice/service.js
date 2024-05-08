/**
 * Created by LB on 2016-10-24.
 */

app.factory('noticeService',function (Restangular){
    return {
        /*  通知管理分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/notices?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },
        //删除
        delete:function (id,callback) {
            var rest = Restangular.one('/notice',id);
            return rest.remove('').then(function(data){
                callback(data);
            });
        },

        //保存
        save:function (notice,callback){
            var rest = Restangular.all('/notice');
            return rest.post(notice).then(function (data){
                callback(data);
            });
        },

        send:function (id,callback) {
            var rest = Restangular.one('/notice/send',id);
            return rest.get('').then(function (data) {
                callback(data);
            });
        }

    }
});