/**
 * Created by admin on 2018/3/22.
 */
app.factory('overdueService',function ($cookieStore,Restangular,$filter){
    return {

        /**
         * 列表页
         */
        search:function (search,currentPage, callback){
            search.currentPage = currentPage;
            var overduerecords = Restangular.all('/overduerecord/search');
            overduerecords.post(search).then(function (data){
                callback(data);
            });
        },
        /**
         * 从交易发起逾期记录
         */
        create:function (id,callback){
            var overduerecord = Restangular.all('/overduerecord/create/'+id);
            overduerecord.get('').then(function (data){
                callback(data);
            });
        },
        /**
         * 详情页
         */
        getOne:function(id,dataStatus,callback){
            var overduerecord=Restangular.one("/overduerecord/"+id+'/'+dataStatus);
            return overduerecord.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 根据当前交易和月份查询
         */
        getOverdueByMonth:function (transactionId,month,callback) {
            var overdueBymonth = Restangular.one("/overduerecord/month?transactionId="+transactionId+'&&month='+month);
            return overdueBymonth.get("").then(function (data) {
                callback(data);
            });
        },

        /**
         * 保存逾期记录
         */
        save:function (overduerecord,callback){
            var overduerecordInfo = Restangular.all('/overduerecord');
            overduerecordInfo.post(overduerecord).then(function (data){
                callback(data);
            });
        },

        /**
         * 通过交易id获取
         */
        getOneByCustomerTransactionId:function(customerTransactionId,callback){
            var rest=Restangular.one("/overduerecord/transaction",customerTransactionId);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 发送逾期提醒
         */
        sendMind:function(id,callback){
            var rest=Restangular.one("/send/overduerecord",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
    }
});