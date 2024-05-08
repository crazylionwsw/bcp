/**
 * Created by LB on 2016-10-24.
 */

app.factory('guaranteewayService',function (Restangular){
    return {

        save:function (guaranteeway,callback){
            var rest = Restangular.all('/guaranteeway');
            rest.post(guaranteeway).then(function (data){
                callback(data);
            });
        },
        
        
        getOne:function (id , callback) {
            var rest = Restangular.one('/guaranteeway',id);
            rest.get('').then(function(data){
                callback(data);
            });
        },

        delete:function (id,callback) {
            var rest = Restangular.one('/guaranteeway',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/guaranteeways?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        lookup: function(callback){
            var rest = Restangular.all('/guaranteeways/lookups');
            return rest.get('').then(function (data){
                callback(data);
            });
        },
        
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/guaranteeway/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        }
    }
});