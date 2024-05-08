/**
 * Created by LB on 2016-10-18.
 */

app.factory("customerService",function ($http,Restangular){

    return {
        getAll: function (callback){
             var customerInfo = Restangular.all('/customers');
            return customerInfo.get('').then(function(data){
                 callback(data);
             });
        },

        getLookUpCustomers: function (callback){
            var customerInfo = Restangular.all('/customers/avaliable');
            return customerInfo.get('').then(function(data){
                callback(data);
            });
        },
        //关于分页功能
        getPageData : function (currentPage,callback){
            var customerInfo = Restangular.all('/customers?currentPage='+currentPage);
            return customerInfo.get('').then(function (data){
                callback(data);
            });
        },

        //查询客户信息
        getOne:function(id,callback){
            var customerInfo = Restangular.one("/customer",id);
            return customerInfo.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 通过客户ID获取该客户的所有的合同
         * @param customerId
         * @param callback
         */
        getContractsByCustomerId: function(customerId, callback) {
            var rest = Restangular.one("/customer",customerId).all('contracts');
            rest.get('').then(function (data){
                callback(data);
            });
        },

        saveCustomerContract: function(force,customerContract,callback) {
            var rest = Restangular.all('/customercontract?force='+force);
            rest.post(customerContract).then(function (data){
                callback(data);
            });
        },

        //查询合同
        getByIds: function (ids, callback){
            var customerContracts = Restangular.all('/customerContracts');
            customerContracts.post(ids).then(function(data){
                callback(data);
            });
        },

        /*  修改客户数据状态 */
        remove:function (cardemand,callback){
            cardemand.dataStatus=9;
            var user = Restangular.all('/cardemand');
            user.post(cardemand).then(function (data){
                callback(data);
            });
        },

        /*  客户的添加/修改 */
        saveCustomer:function (customer,callback){

            var customerMsg = Restangular.all('/customer');
            customerMsg.post(customer).then(function (data){
                callback(data);
            });
        },

        
        /*查询*/
        search:function (customer, currentPage, callback){
            var customers = Restangular.all('/customers/search?currentPage='+currentPage);
            customers.post(customer).then(function (data){
                callback(data);
            });
        },

        getOneCardmend:function(id,callback){
            var customerInfo = Restangular.one("/cardemand",id);
            return customerInfo.get('').then(function(data){
                callback(data);
            });
        },


        getCardemandByCustomerId : function (id, callback){
            var rest = Restangular.one("/customer",id).all('cardemand');
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        getOrderByCustomerId : function (id, callback){
            var rest = Restangular.one("/customer",id).all('order');
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        getPaymentByCustomerId :function (id, callback){
            var rest = Restangular.one("/customer",id).all('payment');
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        getPickupcarByCustomerId:function (id, callback){
            var rest = Restangular.one("/customer",id).all('pickupcar');
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        getCarregistryByCustomerId:function (id, callback){
            var rest = Restangular.one("/customer",id).all('carregistry');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        //  修改客户年收入
        updateCustomerSalary:function(customer,callback){
            var customerInfo = Restangular.all('/customer/salary');
             customerInfo.post(customer).then(function (data){
                callback(data);
            });
        },


        //通过id取得经销商
        getCarDealer:function(id,callback){
            var rest = Restangular.one("/cardealer",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },


        getCustomerImages:function(customerid,callback){
            var customerMsg = Restangular.one('/customer',customerid).all('images');
            customerMsg.get('').then(function(data){
                callback(data);
            })
        },

        getShenpihan:function (customerid,callback) {
            var imageMsg = Restangular.one('/customer',customerid).all('shenpihan');
            imageMsg.get('').then(function (data) {
                callback(data);
            })
        },
        //      通过 购车预算ID  查询到购车预算信息
        getLoan:function(id,callback){
            var customerLoan = Restangular.one("/customer/loan",id);
            return customerLoan.get('').then(function(data){
                callback(data);
            });
        },
        //      通过 购车预算ID  查询到购车预算信息
        getCar:function(id,callback){
            var customerCar = Restangular.one("/customer/car",id);
            return customerCar.get('').then(function(data){
                callback(data);
            });
        },
        //      保存  银行批付额度
        updateApprovedAmount:function (loan,callback){
            var customerLoan = Restangular.all('/customer/loan/');
            customerLoan.post(loan).then(function (data){
                callback(data);
            });
        },
        /**
         * 保存客户卡信息
         * @param customercard
         * @param callback
         */
        saveCustomerCard:function(customercard,callback){
            var rest = Restangular.all('/customercard');
            rest.post(customercard).then(function(data){
                callback(data);
            });
        },
        /**
         * 通过客户交易Id查询客户卡信息
         * @param customerTransactionId
         * @param callback
         * @returns {*|Promise}
         */
        getOneCustomerCard:function(customerTransactionId,callback){
            var rest = Restangular.one("/customercard?customerTransactionId="+customerTransactionId);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 获取客户还款信息
         */
        getRepaymentInfoByCustomerTransactionId:function (customerTransactionId,callback) {
            var rest = Restangular.one("/customer/repayment",customerTransactionId);
            return rest.get('').then(function (data) {
                callback(data);
            });
        },

        /**
         * 获取缴费单信息
         */
        getCustomerFeesByCustomerTransactionId:function (customerTransactionId,callback) {
            var fee = Restangular.one("/customer/fee",customerTransactionId);
            return fee.get('').then(function (data) {
                callback(data);
            });
        },

        getCustomerCarByCustomerId:function (id, callback){
            var rest = Restangular.one("/customer",id).all('car');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getCarValuationByVin:function (vin,callback) {
            var rest = Restangular.one("/carvaluation",vin);
            return rest.get('').then(function (data) {
                callback(data);
            })
        },

        getCustomerCarByCustomerTransactionId:function (customerTransactionId,callback) {
            var rest = Restangular.one("/customer/transaction/car",customerTransactionId);
            return rest.get('').then(function (data) {
                callback(data);
            })
        },

        getCustomerLoanByCustomerTransactionId:function (customerTransactionId,callback) {
            var rest = Restangular.one("/customer/transaction/loan",customerTransactionId);
            return rest.get('').then(function (data) {
                callback(data);
            })
        },

        updateCustomerJob:function (id, customerJob, callback) {
            var rest = Restangular.one("/customer/job",id);
            return rest.post(customerJob).then(function (data) {
                callback(data);
            })
        },

        //  保存客户的自雇信息
        updateCustomerType:function (customerId,isSelfEmployed,callback){
            var customerMsg = Restangular.all('/customer/' + customerId + '/'+isSelfEmployed);
            customerMsg.get('').then(function (data){
                callback(data);
            });
        },

        // 查询业务调整后的购车信息
        getBusinessExchangeCar:function(id,callback){
            var customerCar = Restangular.one("/customer/adjust/car",id);
            return customerCar.get('').then(function(data){
                callback(data);
            });
        },
        // 查询业务调整后的贷款信息
        getBusinessExchangeLoan:function(id,callback){
            var customerLoan = Restangular.one("/customer/adjust/loan",id);
            return customerLoan.get('').then(function(data){
                callback(data);
            });
        },

        // 查询业务调整后的贷款信息通过交易id
        getBusinessExchangeLoanByCustomerTransactionId:function(customerTransactionId,callback){
            var customerLoan = Restangular.one("/customer/transaction/exchangeloan",customerTransactionId);
            return customerLoan.get('').then(function(data){
                callback(data);
            });
        },

        getBusinessExchangeCarByCustomerTransactionId:function(customerTransactionId,callback){
            var exchangeCar = Restangular.one("/customer/transaction/exchangecar",customerTransactionId);
            return exchangeCar.get('').then(function(data){
                callback(data);
            });
        }
    }

});

