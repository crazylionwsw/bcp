/**
 * Created by LB on 2016-10-24.
 */

app.factory('terminalService',function (Restangular){
    return {
        
        //获取绑定记录列表（带分页）
        getPageData: function (currentPage, callback) {
            var rest = Restangular.all('/devices/binds?currentPage=' + currentPage);
            return rest.get('').then(function (data) {
                callback(data);
            });
        },
        
        //保存绑定记录
        save:function (terminalbind,callback){
            var rest = Restangular.all('/device/bind');
            rest.post(terminalbind).then(function (data){
                callback(data);
            });
        },
        
        //查找绑定记录
        search:function(user,currentPage,callback){
            var username = user.username;
            var rest =  Restangular.all('/devices/binds/search/'+username+'?currentPage='+currentPage);
            return rest.get('').then(function (data) {
                callback(data);
            });
        }
    }
});