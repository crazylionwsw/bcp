/**
 * Created by LB on 2016-10-24.
 */

app.factory('carvaluationService',function (Restangular,$filter){
    return {
        getAll:function (calllback){
            var rest = Restangular.all('/carregistries');
            rest.get('').then(function (data){
                calllback(data);
            });
        },
        /* 分页功能 */
        getDataByPage:function (approveStatus,currentPage,callback){
            var rest = Restangular.all('/carvaluations?currentPage='+currentPage+'&approveStatus='+approveStatus);
            rest.get('').then(function (data){
                callback(data);
            });
        },
        /*查询*/
        search:function (carvaluation, currentPage, callback){
            var rest = Restangular.all('/carvaluation/search?currentPage='+currentPage);
            rest.post(carvaluation).then(function (data){
                callback(data);
            });
        },
        /*确认*/
        savePass:function (carvaluation,callback){
            var rest = Restangular.all('/carvaluation/pass');
            rest.post(carvaluation).then(function (data){
                callback(data);
            });
        },

        //取消
        cancelSave:function (carvaluation,callback){
            var rest = Restangular.all('/carvaluation/cancel');
            rest.post(carvaluation).then(function (data){
                callback(data);
            });
        },

        /*保存*/
        saveInfo:function (carvaluation,callback){
            var rest = Restangular.all('/carvaluation');
            rest.post(carvaluation).then(function (data){
                callback(data);
            });
        },

        //查询回显数据
        getOne:function(id,callback){
            var rest=Restangular.one("/carvaluation/valuation/",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        //查询回显数据
        delete:function(id,callback){
            var rest=Restangular.one("/carvaluation",id);
            return rest.remove('').then(function(data){
                callback(data);
            });
        },

        getCarValuationFinishOrder:function (id,callback) {
            var rest=Restangular.one("/carvaluation/finish/",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        }

    }
});