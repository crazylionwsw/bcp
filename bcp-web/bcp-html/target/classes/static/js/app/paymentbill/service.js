/**
 * Created by admin on 2018/3/8.
 */
app.factory('paymentbillService',function (Restangular,$filter){
    return {

        //列表页
        search:function (search, currentPage, callback){
            search.currentPage = currentPage;
            var paymentbills = Restangular.all('/paymentbills/search');
            paymentbills.post(search).then(function (data){
                callback(data);
            });
        },
        //查询回显数据
        getOne:function(id,callback){
            var paymentbillInfo=Restangular.one("/paymentbill",id);
            return paymentbillInfo.get('').then(function(data){
                callback(data);
            });
        },
        //审批
        sign:function(id, signinfo, callback){
            var paymentbillJson = Restangular.all('/paymentbill/'+id+'/sign');
            paymentbillJson.post(signinfo).then(function (data){
                callback(data);
            });
        },

        //统计当前客户几笔缴费单
        getPaymentCountByCustomerTransactionId:function(id,callback){
            var paymentbillInfo=Restangular.one("/paymentbill/count",id);
            return paymentbillInfo.get('').then(function(data){
                callback(data);
            });
        },

        //根据交易id查询缴费单（多条）
        getPaymentsByCustomerTransactionId:function (id,callback) {
            var paymentbillInfo = Restangular.one("/paymentbills/transaction",id);
            return paymentbillInfo.get('').then(function (data) {
                callback(data);
            })
        }
    }
});