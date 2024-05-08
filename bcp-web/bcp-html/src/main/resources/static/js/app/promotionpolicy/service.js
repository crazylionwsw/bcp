/**
 * Created by zxp on 2017/3/5.
 */
app.factory('promotionpolicyService',function (Restangular) {
    return{
        save:function (salespolicy,callback){
            var rest = Restangular.all('/promotionpolicy');
            rest.post(salespolicy).then(function (data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/promotionpolicys?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        delete:function (id,callback) {
            var rest = Restangular.one('/promotionpolicy',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        checkUnique:function(entity,propname,propval,callback){
            var creditproduct = Restangular.all('/validate/promotionpolicy/'+propname+'/'+propval);
            creditproduct.post(entity).then(function(data){
                callback(data);
            })
        }

    }
});
