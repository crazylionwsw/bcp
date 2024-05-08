/**
 * Created by LB on 2016-10-24.
 */

app.factory('returninfoService',function (Restangular){
    return {
        /*分页功能 资料归还*/
        getCarRegistriesByPage:function (currentPage,callback){
            var returninfos = Restangular.all('/returninfos?currentPage='+currentPage);
            returninfos.get('').then(function (data){
                callback(data);
            });
        },
        save:function (returninfo,callback){
            var returninfoInfo = Restangular.all('/returninfo');
            returninfoInfo.post(returninfo).then(function (data){
                callback(data);
            });
        },
        
        getOne:function(id,callback){
            var returninfoInfo=Restangular.one("/returninfo",id);
            return returninfoInfo.get('').then(function(data){
                callback(data);
            });
        },
        getOneCarRegistry:function(id,callback){
            var carRegistryInfo = Restangular.one("/returninfo",id);
            return carRegistryInfo.get('').then(function(data){
                callback(data);
            });
        },


        /*模糊查询*/
        search:function (customer, currentPage, callback){
            var returninfos = Restangular.all('/returninfos/search/?currentPage='+currentPage);
            returninfos.post(customer).then(function (data){
                callback(data);
            });
        },
    }
});