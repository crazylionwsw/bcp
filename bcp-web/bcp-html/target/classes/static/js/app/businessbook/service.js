/**
 * Created by LB on 2016-10-18.
 */

app.factory("businessbookService",function ($http,Restangular){

    return {

        getAll:function (selectTime,callback){
            var rest = Restangular.all('/businessbooks?selectTime='+selectTime);
            rest.get('').then(function(data){
                callback(data);
            })
        },
        
        
        //关于分页功能
        getPageData : function (currentPage,status,callback){
            var rest = Restangular.all('/customertransactions?currentPage='+currentPage+'&status='+status);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        search: function (searchFilter,currentPage,callback) {
            searchFilter.currentPage = currentPage;
            var rest = Restangular.all('/customertransactions/search');
            return rest.post(searchFilter).then(function (data){
                callback(data);
            });
        },
        
        getOne:function(id,callback){
            var rest = Restangular.one("/customertransaction",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        save:function (customertransaction, callback) {
            var rest = Restangular.all('/customertransaction');
            rest.post(customertransactions).then(function (data) {
                callback(data);
            })
        },

        getStage:function(id, bizcode, callback){
            var rest = Restangular.one("/customertransaction",id).all('stage');
            return rest.post(bizcode).then(function(data){
                callback(data);
            });
        },
    }
});

