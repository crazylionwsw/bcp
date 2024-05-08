/**
 * Created by LB on 2016-10-24.
 */

app.factory('dashboardService',function (Restangular){
    return {

        //日报查询
        queryDailyReport:function (billTypeCode,callback){
            var creditreportInfo= Restangular.one('/dailyreport',billTypeCode);
            creditreportInfo.get().then(function (data){
                callback(data);
            });
        },
        //日报查询
        queryDailyReportReapply:function (billTypeCode,callback){
            var creditreportInfo= Restangular.one('/dailyreport/',billTypeCode);
            creditreportInfo.one('reapply').get().then(function (data){
                callback(data);
            });
        }
    }
});