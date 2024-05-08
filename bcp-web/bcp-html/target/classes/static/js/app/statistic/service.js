/**
 * Created by lily on 2017-05-04.
 */

app.factory('statisticService',function (Restangular){
    return {
        /**
         * 获取销售汇总列表
         * @param saleMonth
         * @param callback
         */
        getAll : function(saleMonth,callback){
            var rest = Restangular.all('/statistics?saleMonth='+saleMonth);
            rest.get('').then(function(data){
                callback(data);
            })
        },
        /**
         * 下载销售汇总列表
         * @param downSaleMonth
         * @param callback
         */
        downLoad : function(downSaleMonth,callback){
            var reset = Restangular.all('/statistic/downLoad?downSaleMonth='+downSaleMonth);
            reset.get('').then(function (data) {
                callback(data);
            })
        }
    }
});