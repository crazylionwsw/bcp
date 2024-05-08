/**
 * Created by LB on 2016-10-18.
 */

app.factory("printerService",function ($http,Restangular){

    return {
        getAll: function (callback){
             var user = Restangular.all('/customers');
            return user.get('').then(function(data){
                 callback(data);
             });
        },
        //关于分页功能
        getPageData : function (currentPage,callback){
            var user = Restangular.all('/customers?currentPage='+currentPage);
            return user.get('').then(function (data){
                callback(data);
            });
        },
        /*查询*/
        search:function (customer, currentPage, callback){
            var customers = Restangular.all('/customer/search?currentPage='+currentPage);
            customers.post(customer).then(function (data){
                callback(data);
            });
        },

        /*  修改客户数据状态 */
        remove:function (customer,callback){
            customer.dataStatus=9;
            var user = Restangular.all('/customer');
            user.post(customer).then(function (data){
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

       
        getOne:function(id,callback){
            var customerInfo = Restangular.one("/customer",id);
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
        //通过id取得经销商
        getCarDealer:function(id,callback){
            var rest = Restangular.one("/cardealer",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getImagetypes:function(customerid,callback){
            var customerMsg = Restangular.one('/customer',customerid).all('images');
            customerMsg.get('').then(function(data){
                callback(data);
            })
        }
    }
});

