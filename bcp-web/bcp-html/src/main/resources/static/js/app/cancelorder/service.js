/**
 * Created by zxp on 2017/3/13.
 */
app.factory('cancelorderService',function (Restangular){
    return {
        
        /*查询*/
        search:function (search,currentPage,callback){
            search.currentPage = currentPage;
            var cancelorders = Restangular.all('/cancelorders/search');
            cancelorders.post(search).then(function (data){
                callback(data);
            });
        },
        
        getOne: function (id, callback) {
            var cancelorderInfo = Restangular.one("/cancelorder", id);
            return cancelorderInfo.get('').then(function (data) {
                callback(data);
            });
        },

        //查询回显数据
        getOneByCustomerTransactionId:function(id,callback){
            //对档案进行处理
            var cancelInfo = Restangular.one("/cancelorder/transaction",id);
            return cancelInfo.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 审查/审批/签批
         * @param cardemand
         * @param signinfo
         * @param callback
         */
        sign:function(id, signinfo, callback){
            var cancelorderJson = Restangular.all('/cancelorder/'+id+'/sign');
            cancelorderJson.post(signinfo).then(function (data){
                callback(data);
            });
        }
    }

});