/**
 * Created by LB on 2016-10-24.
 */
app.factory('appointpaymentService',function ($cookieStore,Restangular,$filter){
    return {
        
        /* 分页功能 */
        getDataByPage:function (currentPage,status,callback){
            var appointpayments = Restangular.all('/appointpayments?currentPage='+currentPage+'&status='+status);
            appointpayments.get('').then(function (data){
                callback(data);
            });
        },

        /*批量查询payment*/
        search:function (search,currentPage, callback){
            search.currentPage = currentPage;
            var appointpayments = Restangular.all('/appointpayments/search');
            appointpayments.post(search).then(function (data){
                callback(data);
            });
        },
        getOne:function(id,callback){
            var appointpayment=Restangular.one("/appointpayment",id);
            return appointpayment.get('').then(function(data){
                callback(data);
            });
        },
        /* 删除功能deletePayment*/
        deleteAppointPayment:function (payment,callback){
            payment.dataStatus=9;
            var paymentInfo = Restangular.all('/appointpayment');
            paymentInfo.post(payment).then(function (data){
                callback(data);
            });
        },

        /**
         * 审查/审批/签批
         * @param payment
         * @param signinfo
         * @param callback
         */
        sign:function(id, signinfo, callback){
            var paymentJson = Restangular.all('/appointpayment/'+id+'/sign');
            paymentJson.post(signinfo).then(function (data){
                callback(data);
            });
        },
        /**
         * 审核按钮变换
         * @param signinfo
         */
        judgeLastStep:function (businessKey,callback){
            var teskMsg = Restangular.all('/task/judge?businessKey='+businessKey);
            teskMsg.get('').then(function(data){
                callback(data);
            });
        },
        
        savePayTime:function (appointpayment, callback){
            var payments = Restangular.all('/appointpayment/payTime');
            payments.post(appointpayment).then(function (data){
                callback(data);
            });
        },
        
        saveStatus:function (id,status, callback){
            var payments = Restangular.all('/appointpayment/status?id='+id+'&status='+status);
            payments.get('').then(function (data){
                callback(data);
            });
        },

        getOneByCustomerTransactionId:function(customerTransactionId,callback){
            var rest=Restangular.one("/appointpayment/transaction",customerTransactionId);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 获取所有的CarDealerIds
         * @param callback
         */
        getCustomerTransactionsByCarDealerId:function(carDealerId,callback){
            var appointpayment=Restangular.one("/appointpayments",carDealerId);
            return appointpayment.get('').then(function(data){
                callback(data);
            });
        }

    }
});