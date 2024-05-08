app.factory('groupsharingService',function (Restangular){
    return {

        /*  分页功能 */
        getDataByPage: function(currentPage,month,groupId,callback){
            var rest = Restangular.all('/group/sharings?currentPage='+currentPage+'&month='+month+'&groupId='+groupId);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        getAll : function(groupId, currentPage, callback){
            var rest = Restangular.all('/groupsharings?currentPage='+currentPage + '&groupId='+groupId);
            rest.get('').then(function(data){
                callback(data);
            })
        },
        calcSharing : function(saleMonth, groupId, callback){
            var rest = Restangular.all('/creat/group/sharing/?groupId='+groupId+ '&saleMonth='+saleMonth);
            rest.get('').then(function(data){
                callback(data);
            })
        },
        refreshSharing : function(saleMonth, groupId, callback){
            var rest = Restangular.all('/groupsharings/refresh/?saleMonth='+saleMonth+ '&groupId='+groupId);
            rest.get('').then(function(data){
                callback(data);
            })
        },
        getDetails:function(id, currentPage, callback) {
            var rest = Restangular.all("/groupsharing/" + id + "/details?currentPage=" + currentPage);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        getOne:function(id, callback) {
            var rest = Restangular.one("/group/sharing",id);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        exportSharing : function(saleMonth, groupId, callback){
            var rest = Restangular.all('/groupsharings/export/?saleMonth='+saleMonth+ '&groupId='+groupId);
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

        getSharing:function (groupId,saleMonth,callback) {
            var rest = Restangular.all('/sharing/details/?groupId='+groupId+'&saleMonth='+saleMonth);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        getGroupSharingInfo:function (groupId,saleMonth,callback) {
            var rest = Restangular.all('/groupsharing/info/?groupId='+groupId+'&saleMonth='+saleMonth);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        balanceGroupSharing:function (groupSharingId,callback) {
            var rest = Restangular.all('/sharing/balancegroupsharing?id='+groupSharingId);
            rest.get('').then(function(data){
                callback(data);
            })
        },

        getGroupSharing:function (groupSharingId,callback) {
            var rest = Restangular.all('/sharing/getGroupSharing?id='+groupSharingId);
            rest.get('').then(function(data){
                callback(data);
            })
        }
    }
});