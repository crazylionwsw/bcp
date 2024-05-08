/**
 * Created by LB on 2016-10-24.
 */

app.factory('bankcardService',function (Restangular){
    return {
  
        /**
         * 获取列表页面
         * @param currentPage
         * @param callback
         */
        getDataByPage : function (currentPage,status,callback){
            var bankcards = Restangular.all('/bankcards?currentPage='+currentPage+"&status="+status);
            bankcards.get('').then(function (data) {
                callback(data);
            });
        },
        
        /*查询*/
        search:function (search, currentPage,callback){
            search.currentPage = currentPage;
            var bankcards = Restangular.all('/bankcards/search');
            bankcards.post(search).then(function (data){
                callback(data);
            });
        },
        
        /**
         * 根据Id获取卡业务信息
         * @param id
         * @param callback
         * @returns {*|Promise}
         */
        getOne:function(id,callback){
            var bankcardInfo=Restangular.one("/bankcard",id);
            return bankcardInfo.get('').then(function(data){
                callback(data);
            });
        },
        submit:function (bankcard,id,callback){
            if(!bankcard.id)
                bankcard.dataStatus = 1;
            var bankcardInfo = Restangular.all('bankcard');
            bankcardInfo.post(bankcard).then(function (data){
                callback(data);
            });
        },

        /*添加功能*/
        save:function (bankcard,callback){
            if(!bankcard.id)
                bankcard.dataStatus = 1;
            var bankcardInfo = Restangular.all('/bankcard');
            bankcardInfo.post(bankcard).then(function (data){
                callback(data);
            });
        },

        saveReceiveCardName:function (customerTransactionId,receiveCardName,callback){
            var bankcardInfo = Restangular.all('/bankcard?customerTransactionId='+customerTransactionId+'&receiveCardName='+receiveCardName);
            bankcardInfo.post('').then(function (data){
                callback(data);
            });
        },
        
        /**
         * 执行工作流
         * @param bankcard
         * @param approveStatus
         * @param start
         * @param callback
         */
        sign:function(bankcard,approveStatus,start,callback){
            var rest = Restangular.all('/bankcard/sign?approveStatus='+approveStatus+'&start='+start);
            rest.post(bankcard).then(function(data){
                callback(data);
            })
        },
        
        getOneByCustomerTransactionId:function(customerTransactionId,callback){
            var bankcardInfo=Restangular.one("/bankcard/transaction",customerTransactionId);
            return bankcardInfo.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 重新制卡
         * @param bankcardId
         * @param callback
         * @returns {*|Promise}
         */
        remakecard:function(bankcardId,comment,callback){
            var rest=Restangular.all('/bankcard/remakecard?bankcardId='+bankcardId+'&comment='+comment);
            rest.get('').then(function(data){
                callback(data);
            });
        },

        realDeleteReport:function (bankcard,callback) {
            var rest = Restangular.all('/clear/report');
            rest.post(bankcard).then(function(data){
                callback(data);
            })
        }
    }
});