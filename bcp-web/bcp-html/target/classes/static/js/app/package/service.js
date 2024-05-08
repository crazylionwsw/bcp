/**
 * Created by LB on 2016-10-24.
 */

app.factory('packageService',function (Restangular){
    return {

        getDataByPage : function (currentPage,callback){
            var orderInfo = Restangular.all('/ordernewcars?currentPage='+currentPage);
            orderInfo.get('').then(function (data) {
                callback(data);
            });
        },
        //添加
        save:function (customerimage,callback){
            if(!customerimage.id)
                customerimage.dataStatus = 1;
            var customerimageInfo = Restangular.all('/customerimage');
            customerimageInfo.post(customerimage).then(function (data){
                callback(data);
            });
        },
        //删除
        delete:function(id,callback){
            var rest = Restangular.one('/customerimage', id);
            rest.remove().then(function (data){
                callback(data);
            });
        },
        
        /*查询*/
        search:function (customer, currentPage, callback){
            var orders = Restangular.all('/ordernewcars/search?currentPage='+currentPage);
            orders.post(customer).then(function (data){
                callback(data);
            });
        },
        
        //查询回显数据
        getOne:function(id,callback){
            var orderInfo=Restangular.one("/order",id);
            return orderInfo.get('').then(function(data){
                callback(data);
            });
        },
        
        getCashSources:function(id, callback) {
            var rest = Restangular.one("order",id).all("cashsources");
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        package:function(customerPackage, callback) {
            var rest = Restangular.all("/customerimages/package");
            return rest.post(customerPackage).then(function(data){
                callback(data);
            });
        },
        getAllCustomerPackages:function (order,callback){
            var customerPackageMsg = Restangular.all('/customerpackages');
            return customerPackageMsg.post(order).then(function (data){
                callback(data);
            });
        },
        downloadpackage:function(id,callback){
            var packageMsg = Restangular.one('/customerpackage',id).all('download');
            return packageMsg.get('').then(function(data){
                callback(data);
            })
        }
    }
});