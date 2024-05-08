/**
 * Created by LB on 2016-10-18.
 */

app.factory("rejectcustomerService",function ($http,Restangular){

    return {
        
        /*查询*/
        search:function (search, currentPage, callback){
            search.currentPage = currentPage;
            var customers = Restangular.all('/rejectcustomers/search');
            customers.post(search).then(function (data){
                callback(data);
            });
        },
    }

});

