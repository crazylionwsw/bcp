/**
 * Created by LB on 2016-10-24.
 */

app.factory('dmvpledgeService',function (Restangular,$filter){
    return {

        /*模糊查询*/
        search:function (searchFilter, currentPage,callback){
            searchFilter.currentPage = currentPage;
            var dmvpledges = Restangular.all('/dmvpledges/search');
            dmvpledges.post(searchFilter).then(function (data){
                callback(data);
            });
        },

        /* 分页功能 */
        getDataByPage:function (currentPage,status,callback){
            var dmvpledges = Restangular.all('/dmvpledges?currentPage='+currentPage+'&status='+status);
            dmvpledges.get('').then(function (data){
                callback(data);
            });
        },
        getOne:function(id,callback){
            var dmvpledgeInfo=Restangular.one("/dmvpledge",id);
            return dmvpledgeInfo.get('').then(function(data){
                callback(data);
            });
        },
        getOneByCustomerTransactionId:function(transactionId,callback){
            var dmvpledgeInfo=Restangular.one("/dmvpledge/transaction",transactionId);
            return dmvpledgeInfo.get('').then(function(data){
                callback(data);
            });
        },
        /*添加功能*/
        saveBankPledge:function (bankpledge,callback){
            var bankpledgeInfo = Restangular.all('bankpledge');
            bankpledgeInfo.post(bankpledge).then(function (data){
                callback(data);
            });
        },
        
        saveDmvPledge:function (dmvpledge,callback){
            var dmvPledgeInfo = Restangular.all('dmvpledge');
            dmvPledgeInfo.post(dmvpledge).then(function (data){
                callback(data);
            });
        },

        getOneCarRegistry:function(id,callback){
            var carRegistryInfo = Restangular.one("/carregistry",id);
            return carRegistryInfo.get('').then(function(data){
                callback(data);
            });
        },
        getOnePurchaseCarOrder:function(purchaseCarOrderId,callback){
            var purchaseCarOrder=Restangular.one("/order",id);
            return purchaseCarOrder.get('').then(function(data){
                callback(data);
            });
        },
        getOneBankpledgeByPurchaseCarOrderId : function (id, callback){
            var rest = Restangular.one("/order",id).all('bankpledge');
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        /**
         * 执行流程
         * @param dmvpledge
         * @param callback
         */
        sign:function(dmvpledge,callback){
            var rest = Restangular.all('/dmvpledge/sign');
            rest.post(dmvpledge).then(function(data){
                callback(data);
            })
        },

    }
});