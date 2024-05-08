/**
 * Created by zxp on 2017/3/5.
 */
app.factory('compensatorypolicyService',function (Restangular) {
    return{

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/compensatorypolicys?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        save:function (compensatorypolicy,callback){
            var rest = Restangular.all('/compensatorypolicy');
            rest.post(compensatorypolicy).then(function (data){
                callback(data);
            });
        },

        getOne:function(id,callback){
            var rest = Restangular.one("/compensatorypolicy",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        delete:function (compensatorypolicy,callback) {
            var id = compensatorypolicy.id
            var rest = Restangular.one('/compensatorypolicy',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        checkUnique:function(entity,propname,propval,callback){
            var creditproduct = Restangular.all('/validate/compensatorypolicy/'+propname+'/'+propval);
            creditproduct.post(entity).then(function(data){
                callback(data);
            })
        }
    }
});