/**
 * Created by LB on 2016-10-24.
 */

app.factory('cardemandService',function (Restangular, $filter){
    return {
        //  作废
        getAll:function (calllback){
            var cardemend = Restangular.all('/cardemands');
            cardemend.get('').then(function (data){
                calllback(data);
            })
        },
        
        /*查询*/
        search:function (search, currentPage, callback){
            search.currentPage = currentPage;
            var cardemends = Restangular.all('/cardemands/search');
            cardemends.post(search).then(function (data){
                callback(data);
            });
        },

        //查询回显数据
        getOne:function(id,callback){
            //对档案进行处理
            var cardemend = Restangular.one("/cardemand",id);
            return cardemend.get('').then(function(data){
                callback(data);
            });
        },

        //查询回显数据
        getOneByCustomerTransactionId:function(id,callback){
            var cardemend = Restangular.one("/cardemand/transaction",id);
            return cardemend.get('').then(function(data){
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
        

        //保存
        save:function (cardemand,callback){
            var cardemandInfo = Restangular.all('/cardemand');
            cardemandInfo.post(cardemand).then(function (data){
                callback(data);
            });
        },

        //  保存 是否需要反担保人
        saveCustomerDemandNeedCounterGuarantor:function (cardemand,callback){
            var cardemandInfo = Restangular.all('/cardemand/needGuarantor');
            cardemandInfo.post(cardemand).then(function (data){
                callback(data);
            });
        },

        updateSignInfo:function (cardemand,callback){
            var cardemandInfo = Restangular.all('/cardemand/signinfo');
            cardemandInfo.post(cardemand).then(function (data){
                callback(data);
            });
        },
       

        /**
         * 审查/审批/签批
         * @param cardemand
         * @param signinfo
         * @param callback
         */
        sign:function(id, signinfo, callback){
            var cardemandJson = Restangular.all('/cardemand/'+id+'/sign');
            cardemandJson.post(signinfo).then(function (data){
                callback(data);
            });
        },

        //TODO:资质审查，新审批
        temSign:function(id, temSignInfo, callback){
            var cardemandJson = Restangular.all('/cardemand/'+id+'/temsign');
            cardemandJson.post(temSignInfo).then(function (data){
                callback(data);
            });
        },

        getcheckinfo:function (customerId,callback) {
            var rest = Restangular.all("/customercheck?customerId="+customerId);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
    }
});