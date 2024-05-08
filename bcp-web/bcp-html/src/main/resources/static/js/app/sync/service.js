/**
 * Created by LB on 2016-10-24.
 */

app.factory('syncService',function (Restangular){
    return {

        //关于分页功能
        getPageData: function (currentPage, callback) {
            var sync = Restangular.all('/cars?currentPage=' + currentPage);
            return sync.get('').then(function (data) {
                callback(data);
            });
        },
        //删除
        delete:function (sync,callback){
            var syncInfo = Restangular.one('/sync',sync.id);
            syncInfo.remove().then(function (data){
                callback(data);
            });
        },
        getByIds: function (ids, callback){
            var sysroles = Restangular.all('/sync/');
            sysroles.post(ids).then(function(data){
                callback(data);
            });
        },
        //获得全部车型品牌
        getAllcarBrands: function (callback){
            var sync = Restangular.all('/carbrands');
            return sync.get('').then(function (data) {
                callback(data);
            });
        },

        //更新一个品牌的车型
        syncCarType: function (id, callback){
            var sync = Restangular.all('/sync/',id);
            sync.get(id).then(function(data){
                callback(data);
            });
        },

    }
});