/**
 * Created by LB on 2016-10-24.
 */

app.factory('compensatorypolicyinfoService',function (Restangular){
    return {

        save:function (formula,callback){
            var rest = Restangular.all('/compensatorypolicy/formula');
            rest.post(formula).then(function (data){
                callback(data);
            });
        },
        
        delete:function (id,callback) {
            var rest = Restangular.one('/compensatorypolicy/formula',id);
            rest.remove('').then(function (data) {
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/compensatorypolicy/formulas?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },


      
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/repaymentway/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        //获取多条
        getAll:function (callback){
            var rest = Restangular.all('/formulas');
            rest.get('').then(function (data){
                callback(data);
            });
        },
    }
});