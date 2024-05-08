/**
 * Created by LB on 2016-10-24.
 */

app.factory('salespolicyService',function (Restangular){
    return {
        //查询列表数据
        getAll: function (callback){
            var rest = Restangular.all('/salespolicys');
            rest.get('').then(function(data){
                callback(data);
            });
        },

        lookup: function (callback){
            var imagetypes = Restangular.all('/salespolicy/lookups');
            imagetypes.get('').then(function(data){
                callback(data);
            });
        },

        //保存
        saveSalesPolicy:function (salespolicy,callback){
            salespolicy.dataStatus=1;
            var rest = Restangular.all('/salespolicy');
            rest.post(salespolicy).then(function (data){
                callback(data);
            });
        },

        //删除
        delete:function (salespolicy,callback) {
            var rest = Restangular.one('/salespolicy',salespolicy.id);
            rest.remove().then(function(data){
                callback(data);
            });
        },
        
        //唯一性验证
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/salespolicy/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        }
    }
});
