/**
 * Created by gqr on 2017/8/17.
 */
app.factory('appointswipingcardService',function (Restangular, $filter) {

    return {
        
        getDataByPage:function (currentPage,status,callback) {
            var appointswipingcard=Restangular.all('/appointswipingcards?currentPage='+currentPage+'&status='+status);
            appointswipingcard.get('').then(function (data){
                callback(data);
            })
        },
        
        search:function (searchFilter, currentPage, callback){
            searchFilter.currentPage = currentPage;
            var appointswipingcards = Restangular.all('/appointswipingcards/search');
            appointswipingcards.post(searchFilter).then(function (data){
                callback(data);
            });
        },
        
        getOne:function(id,callback){
            var appointswipingcard=Restangular.one("/appointswipingcard",id);
            return appointswipingcard.get('').then(function(data){
                callback(data);
            });
        },
        
        getOneByCustomerTransactionId:function(id,callback){
            var appointswipingcard=Restangular.one("/appointswipingcard/transaction",id);
            return appointswipingcard.get('').then(function(data){
                callback(data);
            });
        },

        saveStatus:function (id,status, callback){
            var appointswipingcard = Restangular.all('/appointswipingcard/status?id='+id+'&status='+status);
            appointswipingcard.get('').then(function (data){
                callback(data);
            });
        },

        sign:function(id, signinfo, callback){
            var paymentJson = Restangular.all('/appointswipingcard/'+id+'/sign');
            paymentJson.post(signinfo).then(function (data){
                callback(data);
            });
        }

    }
});