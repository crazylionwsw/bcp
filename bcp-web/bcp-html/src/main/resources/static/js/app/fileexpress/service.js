/**
 * Created by LB on 2016-10-24.
 */

app.factory('fileexpressService',function (Restangular,$filter){
    return {
        /* 分页功能 */
        getDataByPage:function (currentPage,callback){
            var rest = Restangular.all('/fileexpresses?currentPage='+currentPage);
            rest.get('').then(function (data){
                callback(data);
            });
        },

        /*查询*/
        search:function (customer, currentPage, callback){
            var rest = Restangular.all('/fileexpress/search?currentPage='+currentPage);
            rest.post(customer).then(function (data){
                callback(data);
            });
        },
        
        saveFileExpress:function (fileexpress,callback){
            var rest = Restangular.all('/fileexpress');
            rest.post(fileexpress).then(function (data){
                callback(data);
            });
        },
        
        //查询回显数据
        getOne:function(id,callback){
            var rest=Restangular.one("/fileexpress",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

    }
});