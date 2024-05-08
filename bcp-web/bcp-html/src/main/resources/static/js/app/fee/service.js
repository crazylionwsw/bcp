/**
 * Created by Administrator on 2016/12/29.
 */

app.factory('feeService',function(Restangular){
    return{
        //查询列表数据
        lookup:function (callback){
            var feeitem = Restangular.all('/feeitems/lookups');
            feeitem.get('').then(function (data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var feeitems = Restangular.all('/feeitems?currentPage='+currentPage);
            return feeitems.get('').then(function (data){
                callback(data);
            });
        },
        
        //保存
        save:function (feeitem,callback){
            feeitem.dataStatus = 1;
            var feeItemInfo = Restangular.all('/feeitem');
            feeItemInfo.post(feeitem).then(function (data){
                callback(data);
            });
        },

        //删除
        delete:function (id,callback) {
            var feeRemove = Restangular.one('/feeitem',id);
            feeRemove.remove('').then(function (data) {
                callback(data);
            });
        },

        //删除
        getOne:function (id,callback) {
            var feeRemove = Restangular.one('/feeitem',id);
            feeRemove.get('').then(function (data) {
                callback(data);
            });
        },

        checkUnique:function(message,propname,propval,callback){
            var creditusage = Restangular.all('/validate/feeitem/'+propname+'/'+propval);
            creditusage.post(message).then(function(data){
                callback(data);
            })
        }
    }
});