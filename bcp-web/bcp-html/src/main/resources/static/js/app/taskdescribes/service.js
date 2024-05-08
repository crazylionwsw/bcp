/**
 * Created by LB on 2016-10-24.
 */

app.factory('tasksService',function (Restangular){
    return {

        //保存一级任务内容
        save:function (taskdescribe,callback){
            var rest = Restangular.all('/taskdescribe/save');
            rest.post(taskdescribe).then(function (data){
                callback(data);
            });
        },

        //保存二级任务内容
        saveDescribe:function (taskDescribeChild,id,pid,callback){
            var url = '/taskdescribe/dynamic/save?taskDescId='+id;
            if(pid){
                url = url + "&parentId="+pid
            }
            var rest = Restangular.all(url);
            rest.post(taskDescribeChild).then(function (data){
                callback(data);
            });
        },

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/taskdescribe/page?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },


        delete:function (id,callback) {
            var rest = Restangular.one('/taskdescribe',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        removeTask:function (id,callback) {
            var rest = Restangular.one('/taskdescribe/dynamic',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },


        seeTasksChild:function (id,callback) {
            var rest = Restangular.one('/taskdescribe',id).all('/');
            return rest.all('/dynamicBeans').get('').then(function (data){
                callback(data);
            });
        },
        
        //唯一性验证
        checkUnique:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/taskdescribe/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        getMsgRecord:function (id,callback) {
            var rest = Restangular.one('/msg/record',id);
            rest.get('').then(function(data){
                callback(data);
            });
        },

        getMsgRecords:function (ids,callback) {
            var rest = Restangular.one('/msg/records',ids);
            rest.get('').then(function(data){
                callback(data);
            });
        }
    }
});