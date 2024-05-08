/**
 * Created by zxp on 2017/9/20.
 */
app.factory('chargefeeplanService',function (Restangular) {
    return {

        getOne: function (id, callback) {
            var chargefeeInfo = Restangular.one("/chargefeeplan", id);
            return chargefeeInfo.get('').then(function (data) {
                callback(data);
            });
        },
        findErrorStatus:function(status,callback){
            var chargefeeInfo = Restangular.one("/chargefeeplan/status?status="+ status);
            return chargefeeInfo.get('').then(function (data) {
                callback(data);
            });
        },
        getOneDetaiil:function (id,callback) {
            var chargefeeInfo = Restangular.one("/chargefeeplanDetail", id);
            return chargefeeInfo.get('').then(function (data) {
                callback(data);
            });
        },
        search: function (search, currentPage, callback) {
            search.currentPage = currentPage;
            var chargefeeplan = Restangular.all('/chargefeeplans/search?currentPage=' + currentPage);
            chargefeeplan.post(search).then(function (data) {
                callback(data);
            });
        },
        getBefore: function (beforeDate, callback) {
            var chargefeeplan = Restangular.all('/chargefeeplans/count?beforeDate=' + beforeDate);
            chargefeeplan.get('').then(function (data) {
                callback(data);
            });
        },
        refresh: function (id,signinfo, callback) {
            var chargefeeplan = Restangular.all('/chargefeeplans/'+ id+'/reset/');
            chargefeeplan.post(signinfo).then(function (data) {
                callback(data);
            });
        },
        check: function (choseArr,loginUserId,signinfo, callback) {
            var chargefeeplan = Restangular.all('/chargefeeplan/check?choseArr=' + choseArr+'&loginUserId='+loginUserId);
            chargefeeplan.post(signinfo.signinfo).then(function (data) {
                callback(data);
            });
        },
        checkOne: function (id,loginUserId,signinfo, callback) {
            var chargefeeplan = Restangular.all('/chargefeeplan/checkOne?id=' + id+'&loginUserId='+loginUserId);
            chargefeeplan.post(signinfo.signinfo).then(function (data) {
                callback(data);
            });
        },
        uncheck:function (choseArr,signinfo,callback) {
            var chargefeeplan = Restangular.all('/chargefeeplan/uncheck?choseArr='+ choseArr);
            chargefeeplan.post(signinfo).then(function (data) {
                callback(data);
            });
        },
        uncheckOne:function (id,signinfo,callback) {
            var chargefeeplan = Restangular.all('/chargefeeplan/uncheckOne?id='+ id);
            chargefeeplan.post(signinfo).then(function (data) {
                callback(data);
            });
        },
        getBankCardByTransactionId: function (transactionId, callback) {
            var chargefeeplan = Restangular.one('/bankcard/transaction/', transactionId);
            chargefeeplan.get('').then(function (data) {
                callback(data);
            });
        },
        countOne:function (year,month,signinfo,callback) {
            var rest=Restangular.all('/chargefeeplan/count/?year='+year+"&month="+month);
            rest.post(signinfo).then(function (data) {
                callback(data);
            })
        },
        findError:function (callback) {
            var rest=Restangular.one('/chargefeeplan/find');
            return rest.get().then(function (data) {
                callback(data);
            })
        }

    }

})