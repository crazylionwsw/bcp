/**
 * Created by Administrator on 2016/12/29.
 */

app.factory('creditusageService',function(Restangular){
    return{
        //查询列表数据
        getCreditUsages:function (callback){
            var creditusage = Restangular.all('/creditusages');
            creditusage.get('').then(function (data){
                callback(data);
            });
        },
        
        //保存
        saveCreditUsage:function (creditusage,callback){
            creditusage.dataStatus = 1;
            var creditUsageInfo = Restangular.all('/creditusage');
            creditUsageInfo.post(creditusage).then(function (data){
                callback(data);
            });
        },

        checkUniqueCreditUsage:function(message,propname,propval,callback){
            var creditusage = Restangular.all('/validate/creditusage/'+propname+'/'+propval);
            creditusage.post(message).then(function(data){
                callback(data);
            })
        }
    }
})