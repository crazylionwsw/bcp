/**
 * Created by LB on 2016-10-24.
 */

app.factory('documentService',function (Restangular){
    return {

        //获取基础数据中所有的合同（DataStatus = SAVE）
        lookup:function (callback){
            var rest = Restangular.all('/documents/lookups');
            rest.get('').then(function (data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/documents?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        /**
         * 基础数据中保存合同
         * @param contract
         * @param callback
         */
        save:function (document,callback){
            var rest = Restangular.all('/document');
            rest.post(document).then(function (data){
                callback(data);
            });
        },

        /**
         * 删除文档模版
         * @param id
         * @param callback
         */
        delete:function (id,callback) {
            var rest = Restangular.one('/document',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },


        getTypesByIds: function (ids, callback){
            var rest = Restangular.all('/documents');
            rest.post(ids).then(function(data){
                callback(data);
            });
        },

        /**
         * 基础数据，唯一性验证
         * @param entity
         * @param propname
         * @param propval
         * @param callback
         */
        checkUnique:function(entity, propname, callback){
            var rest = Restangular.all('/validate/document/'+propname);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },
        
        //  获取某交易的某几种客户合同
        getByCustomerIdAndCustomerTransactionIdAndDocumentIds :function (customerId, customerTransactionId, documentIds, callback) {
            var customercontractInfo = Restangular.all('/customercontract/'+customerId+'/'+customerTransactionId);
            customercontractInfo.post(documentIds).then(function(data){
                callback(data);
            })
        },

        /**
         *      生成合同
         * @param force
         * @param customerContract
         * @param callback
         */
        saveCustomerContract: function(force,customerContract,callback) {
            var rest = Restangular.all('/customercontract?force='+force);
            rest.post(customerContract).then(function (data){
                callback(data);
            });
        },
    }
});