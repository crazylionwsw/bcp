/**
 * Created by LB on 2016-10-24.
 */

app.factory('enhancementService',function (Restangular,$filter){
    return {

        search:function (search, currentPage, callback){
            search.currentPage = currentPage;
            var enhancements = Restangular.all('/enhancements/search');
            enhancements.post(search).then(function (data){
                callback(data);
            });
        },
        
        //查询回显数据
        getOne:function(id,callback){
            //对档案进行处理
            var enhancement=Restangular.one("/enhancement",id);
            return enhancement.get('').then(function(data){
                callback(data);
            });
        },
        
        //保存
        save:function (enhancement,callback){
            var enhancementInfo = Restangular.all('/enhancement');
            enhancementInfo.post(enhancement).then(function (data){
                callback(data);
            });
        },
        
        sign:function(id, signinfo, callback){
            var enhancementJson = Restangular.all('/enhancement/'+id+'/sign');
            enhancementJson.post(signinfo).then(function (data){
                callback(data);
            });
        },
        
        getImages:function (id, callback) {
            var enhancement=Restangular.one("/enhancement/"+id+'/images');
            return enhancement.get('').then(function(data){
                callback(data);
            });  
        },
    }
});