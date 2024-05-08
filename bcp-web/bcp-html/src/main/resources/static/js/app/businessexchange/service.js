/**
 * Created by LB on 2016-10-24.
 */

app.factory('businessexchangeService',function (Restangular,$filter){
    return {
        
        search:function (search, currentPage, callback){
            search.currentPage = currentPage;
            var orders = Restangular.all('/businessexchanges/search');
            orders.post(search).then(function (data){
                callback(data);
            });
        },
        //查询详情
        getOne:function(id,callback){
            var orderInfo=Restangular.one("/businessexchange",id);
            return orderInfo.get('').then(function(data){
                callback(data);
            });
        },

        //根据交易id查询回显数据
        getOneByCustomerTransactionId:function(id,callback){
            var orderInfo=Restangular.one("/businessexchange/transaction",id);
            return orderInfo.get('').then(function(data){
                callback(data);
            });
        },

        //获取调整单和客户签约的数据对比
        compareExchangeData:function (proname,customerTrasnactionId,id,callback) {
            var rest = Restangular.all('/businessexchange/compare/'+proname+'/'+customerTrasnactionId+'/'+ id);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        //审批
        sign:function(id, signInfo, callback){
            var rest = Restangular.all('/businessexchange/'+id+'/sign');
            rest.post(signInfo).then(function (data){
                callback(data);
            });
        }

    }
});