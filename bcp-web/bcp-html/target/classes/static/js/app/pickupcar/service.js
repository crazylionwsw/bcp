/**
 * Created by LB on 2016-10-24.
 */

app.factory('pickupcarService',function (Restangular, $filter){
    return {
        getAll:function (calllback){
            var pickupcar = Restangular.all('/pickupcars');
            pickupcar.get('').then(function (data){
                calllback(data);
            });
        },

        /* 分页功能 */
        getDataByPage:function (currentPage,callback){
            var pickupcarInfo = Restangular.all('/pickupcars?currentPage='+currentPage);
            pickupcarInfo.get('').then(function (data){
                callback(data);
            });
        },
        search:function (customer, currentPage,callback){
            var pickupcares = Restangular.all('/pickupcars/search?currentPage='+currentPage);
            pickupcares.post(customer).then(function (data){
                callback(data);
            });
        },
        
        getOne:function(id,callback){
            var pickupcarInfo=Restangular.one("/pickupcar",id);
            return pickupcarInfo.get('').then(function(data){
                callback(data);
            });
        },
        
        save:function (pickupcar,callback){
            var pickupcarMsg = Restangular.all('/pickupcar');
            pickupcarMsg.post(pickupcar).then(function (data){
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
        
        sign:function(pickupcar,signinfo,callback){
            //修改pickupcar的审核状态
            //pickupcar.approveStatus = signinfo.result;

            //为cardemand添加审核记录
            if(!pickupcar.signInfos) pickupcar.signInfos = [];
            pickupcar.signInfos.push(signinfo);

            //调用json-service保存pickupcar
            var pickupcarJson = Restangular.all('/pickupcar');
            pickupcarJson.post(pickupcar).then(function (data){
                callback(data);
            });
        }
    }
});