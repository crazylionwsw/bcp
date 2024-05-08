/**
 * Created by LB on 2016-10-18.
 */

app.factory("customerTransactionService",function ($http,Restangular){

    return {

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
            rest.post(customertransaction).then(function (data) {
                callback(data);
            })
        },

        getStage:function(id, callback){
            var rest = Restangular.one("/customertransaction",id).all('stage');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getCar:function(id, callback){
            var rest = Restangular.one("/customertransaction",id).all('car');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getLoan:function(id, callback){
            var rest = Restangular.one("/customertransaction",id).all('loan');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getList:function (ids, callback){
            var rest = Restangular.all('/customertransactions/list');
            rest.post(ids).then(function (data){
                callback(data);
            });
        },

        mergeTransactions: function(bills, callback) {
            var tids = [];
            angular.forEach(bills, function(bill){
                tids.push(bill.customerTransactionId);
            });

            var rest = Restangular.all('/customertransactions/list');
            rest.post(tids).then(function (data){

                var transactions = data;
                var tMap = {};
                angular.forEach(transactions, function(t){
                    tMap[t.id] = t;
                });
                angular.forEach(bills, function(bill, index){
                    if (tMap[bill.customerTransactionId])
                        bills[index].transaction = tMap[bill.customerTransactionId];
                });

                callback(bills);
            });
        },

        createFileNumber:function (id,callback) {
            var rest = Restangular.one("/customertransaction",id).all('createFileNumer');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getCompensatorytransactions:function (search,callback){
            var rest = Restangular.all('/compensatorytransactions');
            rest.post(search).then(function (data){
                callback(data);
            });
        },

        checkBillRequiredImageType : function (billTypeCode, id, callback) {
            var rest = Restangular.one("/customertransaction/"+id+"/"+billTypeCode+"/checkimage");
            return rest.get('').then(function(data){
                callback(data);
            });
        }
    }
});

