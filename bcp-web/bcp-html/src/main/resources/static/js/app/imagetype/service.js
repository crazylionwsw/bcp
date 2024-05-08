/**
 * Created by LB on 2016-10-24.
 */

app.factory('imagetypeService',function (Restangular){
    return {
        //查询列表数据
        getAll: function (callback){
            var imagetypes = Restangular.all('/imagetypes');
            imagetypes.get('').then(function(data){
                callback(data);
            });
        },

        lookup: function (callback){
            var imagetypes = Restangular.all('/imagetypes/lookups');
            imagetypes.get('').then(function(data){
                callback(data);
            });
        },

        //保存
        save:function (imagetype,callback){
            imagetype.dataStatus=1;
            var imagetypes = Restangular.all('/imagetype');
            imagetypes.post(imagetype).then(function (result){
                callback(result);
            });
        },

        //删除
        delete:function (imagetype,callback) {
            var imagetype = Restangular.one('/imagetype',imagetype.id);
            imagetype.remove().then(function(data){
                callback(data);
            });
        },

        //废除
        discardImageType:function (imagetype,callback){
            imagetype.dataStatus = 9;
            var imagetypes = Restangular.all('/imagetype');
            imagetypes.post(imagetype).then(function (data){
                callback(data);
            });
        },

        checkUniqueImagetype:function(entity,propname,propval,callback){
            var newCodeList = Restangular.all('/validate/customerimagetype/'+propname+'/'+propval);
            newCodeList.post(entity).then(function(data){
                callback(data);
            })
        },

        //获取基础数据中所有的合同（DataStatus = SAVE）
        getAllContract:function (callback){
            var rest = Restangular.all('/documents/lookups');
            rest.get('').then(function (data){
                callback(data);
            });
        },

        getAllSysRoles:function (callback) {
            var model = Restangular.all('/roles/lookups');
            return model.get('').then(function(data){
                callback(data);
            });
        },

        delImgFile:function (imagetype,fileid,callback) {
            var id = imagetype.id;
            var rest = Restangular.one('/image/'+id+'/'+fileid);
            return rest.remove('').then(function (data) {
               callback(data);
            });
        },
    }
});
