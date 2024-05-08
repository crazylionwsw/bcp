/**
 * Created by lily on 2017-05-04.
 */

app.factory('dealersharingService',function (Restangular){
    return {

        getAll : function(salemonth,orgId,callback){
            var rest = Restangular.all('/lookup/dealerdetail?salemonth='+salemonth+'&orgId='+orgId);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,month,orgId,callback){
            var rest = Restangular.all('/dealersharings?currentPage='+currentPage+'&month='+month+'&orgId='+orgId);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        creatDealerDetail : function(saleMonth, orgId,callback){
            var rest = Restangular.all('/dealerdetail/create/?saleMonth='+saleMonth+'&orgId='+orgId);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        getDetails:function(id, callback) {
            var rest = Restangular.one("/dealersharing",id).all('details');
            rest.get('').then(function(data){
                callback(data);
            })
        },

        saveInfo:function (id,carDealerId,sharingDetailId,status,sharingRatio,sharingAmount,mainPartType,callback){
            var rest = Restangular.all('/confirm/status/?id='+id+'&carDealerId='+carDealerId+'&sharingDetailId='+sharingDetailId+'&status='+status+'&sharingRatio='+sharingRatio+'&sharingAmount='+sharingAmount+'&mainPartType='+mainPartType);
            rest.get('').then(function (data){
                callback(data);
            });
        },

        getOne:function(id, callback) {
            var rest = Restangular.one("/lookup/dealersharingdetail/?id="+id);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        balanceDealerSharing:function (dealerSharingId,callback) {
            var rest = Restangular.all('/sharing/balancecardealersharing?id='+dealerSharingId);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        getOneDealerSharing:function(id, callback) {
            var rest = Restangular.one("/dealer/sharing",id);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        refrshDealerSharing : function(dealerId,saleMonth,callback){
            var rest = Restangular.all('/creat/dealer/sharing/?dealerId='+dealerId+'&saleMonth='+saleMonth);
            rest.get('').then(function(data){
                callback(data);
            })
        }


    }
});