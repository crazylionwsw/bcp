/**
 * Created by LB on 2016-10-24.
 */

app.factory('deviceService',function (Restangular){
    return {
        
        //获取设备的使用记录
        getAllDeviceUsages: function (callback) {
            var deviceUsages = Restangular.all('/deviceUsages');
            return deviceUsages.get('').then(function (data) {
                callback(data);
            });
        },
        

        //关于分页功能
        getPageData: function (currentPage, callback) {
            var terminaldevices = Restangular.all('/devices?currentPage=' + currentPage);
            return terminaldevices.get('').then(function (data) {
                callback(data);
            });
        },

        bind:function (bind,callback){
            var rest = Restangular.all('/device/bind');
            rest.post(bind).then(function (data){
                callback(data);
            });
        },

        //保存设备使用记录
        saveDeviceUsage:function(deviceUsage,callback){
            deviceUsage.dataStatus = 1;
            var rest = Restangular.all('/device/usage');
            rest.post(deviceUsage).then(function (data){
                callback(data);
            });
        },

        search:function(loginUserId,currentPage,callback){
            var rest =  Restangular.all('/terminalbinds/search?currentPage='+currentPage+'&loginUserId='+loginUserId);
            return rest.get('').then(function (data) {
                callback(data);
            });
        },

        //保存设备
        saveDevice:function (device,callback){
            var rest = Restangular.all('/device');
            rest.post(device).then(function (data){
                callback(data);
            });
        },

        checkUniqueTerminalDevice:function(entity,propname,propval,callback){
            var newCodeList = Restangular.all('/validate/terminaldevice/'+propname+'/'+propval);
            newCodeList.post(entity).then(function(data){
                callback(data);
            })
        },

        //删除
        delete:function (terminal,callback) {
            var rest = Restangular.one('/device',terminal.id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },
        
        getOne:function (id,callback) {
            var rest = Restangular.one('/device',id);
            rest.get('').then(function (data) {
                callback(data);
            })
        }
    }
});