/**
 * 银行报批
 */
app.factory('poundagesettlementService',function (Restangular,$filter){
    return {

        /* 分页功能 */
        getDataByPage:function (currentPage,callback){
            var poundagesettlementInfo = Restangular.all('/poundageSettlements?currentPage='+currentPage);
            poundagesettlementInfo.get('').then(function (data){
                callback(data);
            });
        },
        
        /* 查询功能 */
        search:function (userName,poundagesettlement,startTime,endTime,currentPage,callback){
            var poundagesettlementInfo = Restangular.all('/poundageSettlements/search?currentPage='+currentPage+'&userName='+userName+'&startTime='+startTime+'&endTime='+endTime);
            poundagesettlementInfo.post(poundagesettlement).then(function (data){
                callback(data);
            });
        },

        //查询回显数据
        getOne:function(poundagesettlementId,callback){
            var rest = Restangular.one("/poundageSettlement",poundagesettlementId);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getOneByCustomerTransactionId:function(id,callback){
            var orderInfo=Restangular.one("/poundageSettlement/transaction",id);
            return orderInfo.get('').then(function(data){
                callback(data);
            });
        },
        
        getMarketingCodeByCustomerTransactionId:function(id,callback){
            var orderInfo=Restangular.one("/poundageSettlement/marketingCode",id);
            return orderInfo.get('').then(function(data){
                callback(data);
            });
        },

        //查询回显数据
        getOneByOrderId:function(orderId,callback){
            var rest = Restangular.one('/order/',orderId).all('poundageSettlement');
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        //  更新数据
        refreshData:function (id, callback) {
            var rest = Restangular.all('poundageSettlement/refresh/'+id+'/transaction');
            return rest.get('').then(function(data){
                callback(data);
            });
        }

    }
});