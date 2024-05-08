/**
 * Created by LB on 2016-10-24.
 */

app.factory('orderService',function (Restangular,$filter){
    return {
        
        search:function (search, currentPage, callback){
            search.currentPage = currentPage;
            var orders = Restangular.all('/orders/search');
            orders.post(search).then(function (data){
                callback(data);
            });
        },
        
        //保存档案数据
        save:function (order,callback){
            var orderInfo = Restangular.all('/order');
            orderInfo.post(order).then(function (data){
                callback(data);
            });
        },

        //查询回显数据
        getOne:function(id,callback){
            var orderInfo=Restangular.one("/order",id);
            return orderInfo.get('').then(function(data){
                callback(data);
            });
        },

        //查询回显数据
        getOneByCustomerTransactionId:function(id,callback){
            var orderInfo=Restangular.one("/order/transaction",id);
            return orderInfo.get('').then(function(data){
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
            var orderJson = Restangular.all('/order/'+id+'/sign');
            orderJson.post(signinfo).then(function (data){
                callback(data);
            });
        },
        //TODO:资质审查，新审批
        temSign:function(id, temSignInfo, callback){
            var rest = Restangular.all('/order/'+id+'/temsign');
            rest.post(temSignInfo).then(function (data){
                callback(data);
            });
        },

        getCashSources:function(id,callback){
            var rest = Restangular.one('/order',id).all('cashsources');
            rest.get('').then(function(data){
                callback(data);
            })
        },

        saveRecept:function (receptfile,callback) {
            var rest = Restangular.all('/receptfile');
            rest.post(receptfile).then(function (data){
                callback(data);
            });
        },
        //  通过交易ID查询客户交接资料
        getReceptFileByCutomerTransactionId:function (customerTrasnactionId, callback) {
            var orderInfo=Restangular.one("/receptfile/transaction",customerTrasnactionId);
            return orderInfo.get('').then(function(data){
                callback(data);
            });
        },
        
        compareData : function (customerTrasnactionId,proname,callback) {
            var rest = Restangular.all('/order/compare/'+customerTrasnactionId+'/'+proname);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        refresh : function (orderId,callback) {
            var rest = Restangular.all('poundageSettlement/refresh/'+orderId+'/order');
            rest.get('').then(function(data){
                callback(data);
            })
        }
        
    }
});