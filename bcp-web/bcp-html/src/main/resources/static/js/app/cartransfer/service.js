/**
 * Created by LB on 2016-10-24.
 */

app.factory('cartransferService',function (Restangular, $filter){
    return {


        /*查询*/
        search:function (search,currentPage, callback){
            search.currentPage = currentPage;
            var cartransfers = Restangular.all('/cartransfers/search');
            cartransfers.post(search).then(function (data){
                callback(data);
            });
        },

        //查询回显数据
        getOne:function(id,callback){
            //对档案进行处理
            var cartransfer = Restangular.one("/cartransfer",id);
            return cartransfer.get('').then(function(data){
                callback(data);
            });
        },

        //查询回显数据
        getOneByCustomerTransactionId:function(id,callback){
            //对档案进行处理
            var cartransfer = Restangular.one("/cartransfer/transaction",id);
            return cartransfer.get('').then(function(data){
                callback(data);
            });
        },

        //保存
        save:function (cardemand,callback){
            var cartransferInfo = Restangular.all('/cartransfer');
            cartransferInfo.post(cardemand).then(function (data){
                callback(data);
            });
        },

        sign:function (id,signinfo,callback) {
            var cartransferInfo = Restangular.all("/cartransfer/"+id+'/sign');
            cartransferInfo.post(signinfo).then(function (data) {
                callback(data);
            })
        }
    }
});