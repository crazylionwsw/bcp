/**
 * Created by admin on 2017-12-06.
 */

app.factory('reportService',function (Restangular){
    return {

        //日报查询
        queryDailyReport:function (billTypeCode,sdate,orgid,callback){
            var creditreportInfo= Restangular.one('/dailyreport',billTypeCode);
            var param = {};

            if(sdate){
                param["searchdate"] = sdate;
            }
            if(orgid){
                param["orgid"]=orgid;
            }
            creditreportInfo.get(param).then(function (data){
                callback(data);
            });
        },
        //日报查询
        queryDailyReportReapply:function (billTypeCode,sdate,orgid,callback){
            var creditreportInfo= Restangular.one('/dailyreport/',billTypeCode);
            var param = {};
            if(sdate){
                param["searchdate"] = sdate;
            }
            if(orgid){
                param["orgid"]=orgid;
            }
            creditreportInfo.one('reapply').get().then(function (data){
                callback(data);
            });
        },
        //警报查询
        queryDailyReportWarnging:function (billTypeCode,sdate,callback){
            var creditreportInfo= Restangular.one('/dailyreport/',billTypeCode);
            creditreportInfo.one('warning').get().then(function (data){
                callback(data);
            });
        },

    }
});