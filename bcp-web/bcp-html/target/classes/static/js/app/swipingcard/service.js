/**
 * Created by gqr on 2017/8/17.
 */
app.factory('swipingcardService',function (Restangular, $filter) {

    return {
        // getDataByPage:function (currentPage,status,callback) {
        //     var rest=Restangular.all('/swipingcards?currentPage='+currentPage+'&status='+status);
        //     rest.get('').then(function (data){
        //         callback(data);
        //     })
        // },
        search:function (search, currentPage, callback){
            search.currentPage = currentPage;
            var rest = Restangular.all('/swipingcards/search');
            rest.post(search).then(function (data){
                callback(data);
            });
        },
        
        getOne:function(id,callback){
            var rest=Restangular.one("/swipingcard",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        getOneByCustomerTransactionId:function(transactionId,callback){
            var rest=Restangular.one("/swipingcard/transaction",transactionId);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        saveInfo:function (swipingcard,callback) {
            var rest = Restangular.all("/swipingcard");
            rest.post(swipingcard).then(function (data) {
                callback(data);
            });
        },

        saveStatus:function (id,status, callback){
            var swipingcard = Restangular.all('/swipingcard/status?id='+id+'&status='+status);
            swipingcard.get('').then(function (data){
                callback(data);
            });
        },

        sign:function(id,signinfo,callback){
            var rest = Restangular.all('/swipingcard/'+id+'/sign');
            rest.post(signinfo).then(function(data){
                callback(data);
            })
        }
    }
});