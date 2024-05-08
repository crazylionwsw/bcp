/**
 * Created by zxp on 2017/11/14.
 */
app.factory('echartService',function(Restangular){
   return{

        // 分期、渠道经理数据统计
        getManagerCharts:function (employeeId, searchDate, billTypeCode, callback) {
             var echart= Restangular.all('/manager/'+employeeId+'/charts?searchdate='+searchDate+'&billTypeCode='+billTypeCode);
             echart.get('').then(function (data){
                callback(data);
             });
        },
        
        // 部门数据统计
        getOrgCharts:function (orginfoid, searchDate, billTypeCode, callback) {
            var echart= Restangular.all('/orginfo/'+orginfoid+'/charts?searchdate='+searchDate+'&billTypeCode='+billTypeCode);
            echart.get('').then(function (data){
                callback(data);
            });
        },
        
        //  获取部门添加的渠道数量
        getOrgCarDealerCount:function (orginfoid, searchDate, callback) {
            var echart= Restangular.all('/orginfo/'+orginfoid+'/cardealer/count?searchdate='+searchDate);
            echart.get('').then(function (data){
                callback(data);
            });
        },
        
        //获取渠道经理添加的渠道数量
        getManagerCarDealerCount:function (employeeId, searchDate, callback) {
           var echart= Restangular.all('/manager/'+employeeId+'/cardealer/count?searchdate='+searchDate);
           echart.get('').then(function (data){
               callback(data);
           });
        },
   }
});
