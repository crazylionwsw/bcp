/**
 * Created by LB on 2016-10-24.
 */

app.factory('repaymentwayService',function (Restangular){
    return {
        
        //查询列表数据
        getAll:function (callback){
            var rest = Restangular.all('/repaymentways');
            rest.get('').then(function (data){
                callback(data);
            });
        },
        
        
        save:function (repaymentway,callback){
            var rest = Restangular.all('/repaymentway');
            rest.post(repaymentway).then(function (data){
                callback(data);
            });
        },
        
        delete:function (id,callback) {
            var rest = Restangular.one('/repaymentway',id);
            rest.remove('').then(function (data) {
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/repaymentways?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        lookup: function(callback){
            var rest = Restangular.all('/repaymentways/lookups');
            return rest.get('').then(function (data){
                callback(data);
            });
        },
      
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/repaymentway/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        }
    }
});