/**
 * Created by LB on 2016-10-24.
 */

app.factory('carregistryService',function (Restangular,$filter){
    return {
        getAll:function (calllback){
            var carregistry = Restangular.all('/carregistries');
            carregistry.get('').then(function (data){
                calllback(data);
            });
        },
        /*查询*/
        search:function (search, currentPage, callback){
            search.currentPage = currentPage;
            var carregistries = Restangular.all('/carregistries/search');
            carregistries.post(search).then(function (data){
                callback(data);
            });
        },
        /*添加*/
        save:function (carregistry,callback){
            var carregistryInfo = Restangular.all('/carregistry');
            carregistryInfo.post(carregistry).then(function (data){
                callback(data);
            });
        },

        //查询回显数据
        getOne:function(id,callback){
            var carregistry=Restangular.one("/carregistry",id);
            return carregistry.get('').then(function(data){
                callback(data);
            });
        },

        //查询回显数据
        getOneByCustomerTransactionId:function(id,callback){
            var carregistry=Restangular.one("/carregistry/transaction",id);
            return carregistry.get('').then(function(data){
                callback(data);
            });
        },

        sign:function (id,signinfo,callback) {
            var carregistryJson = Restangular.all("/carregistry/"+id+'/sign');
            carregistryJson.post(signinfo).then(function (data) {
                callback(data);
            })
        }
    }
});