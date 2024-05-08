/**
 * Created by LB on 2016-10-24.
 */

app.factory('provinceService',function (Restangular){
    return {
       
        //查询树列表数据
        getProvinces:function (id,callback){
            var rest = Restangular.one('/province', id).all('provinces');
            rest.get('').then(function (data){
                callback(data);
            });
        },

        lookup:function(callback) {
            var rest = Restangular.all("/provinces/lookups");
            rest.get('').then(function (data) {
                callback(data);
            });
        },

        save:function (province,callback) {
            var rest = Restangular.all('/province');
            rest.post(province).then(function (data){
                callback(data);
            });
        },

        deleteProvince:function (province,callback) {
            var id = province.id;
            var rest = Restangular.one('/province',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        checkUniqueProvince:function(entity,propname,propval,callback) {
            var rest = Restangular.all('/validate/province/' + propname + '/' + propval);
            rest.post(entity).then(function (data) {
                callback(data);
            })
        },

        getChildren:function (id,callback){
            var rest = Restangular.one('province',id).all('provinces');
            rest.get('').then(function (data){
                callback(data);
            });
        }
    }
});