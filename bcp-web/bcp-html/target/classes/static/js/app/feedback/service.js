/**
 * Created by LB on 2016-10-24.
 */

app.factory('feedBackService',function (Restangular){
    return {
        /*  意见反馈分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/feedbacks?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },
        //删除
        delete:function (id,callback) {
            var rest = Restangular.one('/feedback',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        }

    }
});