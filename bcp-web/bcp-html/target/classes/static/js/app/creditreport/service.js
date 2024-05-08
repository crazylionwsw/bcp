/**
 * Created by LB on 2016-10-24.
 */

app.factory('creditreportService',function (Restangular,$filter){
    return {
        
        /* 分页功能 */
        getPageData : function (currentPage,approveStatus,callback){
            var creditreportquery = Restangular.all('/creditreportquerys?currentPage='+currentPage+'&approveStatus='+approveStatus);
            creditreportquery.get('').then(function (data){
                callback(data);
            });
        },
        /*批量查询creditreportquerys*/
        search:function (customer,approveStatus, currentPage, callback){
            var creditreportquerys = Restangular.all('/creditreportquerys/search?currentPage='+currentPage+'&approveStatus='+approveStatus);
            creditreportquerys.post(customer).then(function (data){
                callback(data);
            });
        },
        //查询回显数据
        getOne:function(id, callback){
            var creditreportquery = Restangular.one("/creditreportquery",id);
            return creditreportquery.get('').then(function(data){
                callback(data);
            });
        },

        //获取关联的CarDemand
        getCarDemand:function(id,callback){
            var rest = Restangular.one("/creditreportquery",id).all('cardemand');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        //上传征信查询报告
        save:function (creditreportquery,callback){
            var creditreportqueryInfo = Restangular.all('/creditreportquery');
            creditreportqueryInfo.post(creditreportquery).then(function (data){
                callback(data);
            });
        },

        //提交征信查询，保存时要把Status设置为STATUS_FINISH
        submit:function(id,callback) {
            var creditreportqueryInfo = Restangular.one("/creditreportquery",id);
            creditreportqueryInfo.post().then(function (data){
                callback(data);
            });
        },

        /**
         * 审查/审批/签批
         * @param cardemand
         * @param signinfo
         * @param callback
         */
        sign:function(creditreportquery, signinfo, callback){

            //为creditreportquery添加审批记录
            if (!creditreportquery.signInfos) creditreportquery.signInfos = [];
            creditreportquery.signInfos.push(signinfo);

            //调用json-api保存creditreportquery
            var creditreportqueryJson = Restangular.all('/creditreportquery/sign');
            creditreportqueryJson.post(creditreportquery).then(function (data){
                callback(data);
            });
        },
    }
});