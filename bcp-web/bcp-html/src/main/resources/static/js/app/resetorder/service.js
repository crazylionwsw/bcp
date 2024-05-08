/**
 * Created by lily on 2017/4/18.
 */
app.factory('resetorderService',function(Restangular,$filter){
    return {
        /**
         * 查询列表(带分页功能)
         * @param approveStatus
         * @param currentPage
         * @param callback
         */
        getPageData : function(approveStatus,currentPage,callback){
            var rest = Restangular.all('/resetorders?approveStatus='+approveStatus+'&=currentPage'+currentPage);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        /**
         * 批量查询
         * @param customer
         * @param currentPage
         * @param approveStatus
         * @param callback
         */
        search:function(customer,approveStatus,currentPage,callback){
          var resetorders = Restangular.all('/resetorders/search?approveStatus='+approveStatus+'&=currentPage'+currentPage);
            resetorders.post(customer).then(function(data){
                callback(data);
            });
        },
        
        /**
         * 查询回显数据
         * @param id
         * @param callback
         * @returns {*|Promise}
         */
        getOne:function(id,callback){
            var rest = Restangular.one("/resetorder",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        //查询回显数据
        getOneByCustomerTransactionId:function(id,callback){
            //对档案进行处理
            var cardemend = Restangular.one("/cardemand/transaction",id);
            return cardemend.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 审查/审批/签批
         * @param resetorder
         * @param signinfo
         * @param callback
         */
        sign:function(id,signinfo,callback){
            var rest = Restangular.all('/resetorder/'+id+'/sign');
            rest.post(signinfo).then(function(data){
                callback(data);
            })
        }
    }
});