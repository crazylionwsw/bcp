/**
 * Created by Lily on 2017-06-22.
 */

app.factory('creditphotographService',function (Restangular,$filter){
    return {
        /**
         * 分页功能
         * @param uploadFinish
         * @param currentPage
         * @param callback
         */
        getPageData : function (currentPage,uploadFinish,callback){
            var rest = Restangular.all('/creditphotographs?currentPage='+currentPage+'&uploadFinish='+uploadFinish);
            rest.get('').then(function (data){
                callback(data); 
            });
        },
        /**
         * 查询回显数据
         * @param id
         * @param callback
         * @returns {*|Promise}
         */
        getOne:function(id, callback){
            var rest = Restangular.one("/creditphotograph",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        /**
         * 批量查询
         * @param customer
         * @param currentPage
         * @param uploadFinish
         * @param callback
         */
        search:function (customer,currentPage,uploadFinish,callback){
            var creditreportquerys = Restangular.all('/creditphotographs/search?currentPage='+currentPage+'&uploadFinish='+uploadFinish);
            creditreportquerys.post(customer).then(function (data){
                callback(data);
            });
        },
        /**
         *     根据  客户ID  查询回显数据
         * @param id
         * @param callback
         * @returns {*|Promise}
         */
        getOneByCustomerId:function(customerid, callback){
            var rest = Restangular.one("/creditphotograph/bycustomerid",customerid);
            return rest.get('').then(function(data){
                callback(data);
            });
        }
    }
});