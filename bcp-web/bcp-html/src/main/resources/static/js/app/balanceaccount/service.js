/**
 * Created by zxp on 2017/11/9.
 */
/**
 * Created by Sean on 2017-09-12.
 */

app.factory('balanceaccountService',function (Restangular){
    return {
        getDataByPage:function (currentPage,status,callback){
            var bankcard = Restangular.all('/balanceaccounts/?currentPage='+currentPage +"&status="+status);
            bankcard.get('').then(function (data){
                callback(data);
            });
        },
        getOne:function (id, callback) {
            var rest = Restangular.one("/balanceaccount", id);
            return rest.get('').then(function (data) {
                callback(data);
            })
        },
        getOneDetails:function (id, callback) {
            var rest = Restangular.one("/balanceaccount", id);
            return rest.post("details").then(function (data) {
                callback(data);
            })
        },
        countOneMonth:function (year,month,loginUserId,callback) {
            var rest=Restangular.one('/balanceaccount/count/?year='+year+"&month="+month+"&loginUserId="+loginUserId);
            return rest.get().then(function (data) {
                callback(data);
            })
        },
        countOne:function (year,month,loginUserId,callback) {
            var rest=Restangular.one('/balanceaccounts/count/?year='+year+"&month="+month+"&loginUserId="+loginUserId);
            return rest.get().then(function (data) {
                callback(data);
            })
        },
        getBankCardByOrderId:function (orderId,callback) {
            var chargefeeplan = Restangular.one('/order/', orderId);
            chargefeeplan.one('bankcardapply').get().then(function (data) {
                callback(data);
            });
        },
        getOrderByOrderId:function (orderId,callback) {
            var chargefeeplan = Restangular.one('/order/', orderId);
            chargefeeplan.one('bankcardapply').get().then(function (data) {
                callback(data);
            });
        },
        //保存档案数据
        save:function (balanceaccount,callback){
            var orderInfo = Restangular.all('/balanceaccount/info');
            orderInfo.post(balanceaccount).then(function (data){
                callback(data);
            });
        },
        checkOne:function (id,loginUserId,signinfo,callback) {
            var chargefeeplan = Restangular.all('/balanceaccount/check/?id='+id+'&loginUserId='+loginUserId);
            chargefeeplan.post(signinfo).then(function (data) {
                callback(data);
            });
        },
        pay:function (id,loginUserId,callback) {
            var chargefeeplan = Restangular.all('/balanceaccount/pay/?id='+id+'&loginUserId='+loginUserId);
            chargefeeplan.get('').then(function (data) {
                callback(data);
            });
        },
        payOver:function (id,loginUserId,callback) {
            var chargefeeplan = Restangular.all('/balanceaccount/payOver/?id='+id+'&loginUserId='+loginUserId);
            chargefeeplan.get('').then(function (data) {
                callback(data);
            });
        },
    }
});