/**
 * Created by gqr on 2017/8/17.
 */
app.factory('dealerRepaymentService',function (Restangular, $filter) {

    return {

        search:function (search, currentPage, callback){
            search.currentPage = currentPage;
            var rest = Restangular.all('/dealerrepayments/search');
            rest.post(search).then(function (data){
                callback(data);
            });
        },
        
        getOne:function(id,callback){
            var rest=Restangular.one("/dealerrepayment",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        saveInfo:function (dealerRepayment,callback) {
            var rest = Restangular.all("/dealerrepayment");
            rest.post(dealerRepayment).then(function (data) {
                callback(data);
            });
        },

        saveStatus:function (id,status, callback){
            var rest = Restangular.all('/dealerrepayment/status?id='+id+'&status='+status);
            rest.get('').then(function (data){
                callback(data);
            });
        },

        sign:function(id, signinfo, callback){
            var rest = Restangular.all('/dealerrepayment/'+id+'/sign');
            rest.post(signinfo).then(function (data){
                callback(data);
            });
        }
    }
});