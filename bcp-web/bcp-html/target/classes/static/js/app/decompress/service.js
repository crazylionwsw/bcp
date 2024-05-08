/**
 * Created by LB on 2016-10-24.
 */
app.factory('decompressService',function ($cookieStore,Restangular,$filter){
    return {

        /**
         * 列表页
         * @param search
         * @param currentPage
         * @param callback
         */
        search:function (search,currentPage, callback){
            search.currentPage = currentPage;
            var decompresss = Restangular.all('/decompresss/search');
            decompresss.post(search).then(function (data){
                callback(data);
            });
        },
        /**
         * 详情页
         * @param id
         * @param callback
         */
        getOne:function(id,dataStatus,callback){
            var decompress=Restangular.one("/decompress/"+id+'/'+dataStatus);
            return decompress.get('').then(function(data){
                callback(data);
            });
        },
        /**
         * 通过交易id获取
         * @param customerTransactionId
         * @param callback
         */
        getOneByCustomerTransactionId:function(customerTransactionId,callback){
            var rest=Restangular.one("/decompress/transaction",customerTransactionId);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        /**
         * 审批
         * @param id
         * @param signinfo
         * @param callback
         */
        sign:function(id, signInfo, callback){
            var decompressJson = Restangular.all('/decompress/'+id+'/sign');
            decompressJson.post(signInfo).then(function (data){
                callback(data);
            });
        },

        /**
         * pc创建解押单
         * @param decompress
         * @param callback
         */
        create:function (id,callback){
            var decompressInfo = Restangular.all('/create/decompress/'+id);
            decompressInfo.get('').then(function (data){
                callback(data);
            });
        },


        /*  客户的添加/修改 */
        save:function (decompress,callback){
            var decompressInfo = Restangular.all('/decompress');
            decompressInfo.post(decompress).then(function (data){
                callback(data);
            });
        }
    }
});