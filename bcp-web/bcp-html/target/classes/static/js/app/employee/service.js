/**
 * Created by LB on 2016-10-18.
 */

app.factory("employeeService",function ($http,Restangular){

    return {
        /**
         * 根据id获取
         * @param customer
         * @param callback
         */
        getOne:function (id,callback){
            var rest = Restangular.one("/org/employee",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        save:function (employee,callback){
            var rest = Restangular.all('/employee');
            rest.post(employee).then(function (data){
                callback(data);
            });
        },

        lookup:function (callback) {
            var rest = Restangular.all("/employee/lookups");
            return rest.get('').then(function (data) {
                callback(data);
            })
        },

        /*获取渠道经理*/
        getChannelManager:function (callback) {
            var rest = Restangular.all('/employee/employeemanager');
            rest.get('').then(function (data) {
                callback(data);
            })
        },

        /*获取分期经理*/
        getStageManager:function (callback) {
            var rest = Restangular.all('/employee/businessmanager');
            rest.get('').then(function (data) {
                callback(data);
            })
        },

        /*获取员工列表*/
        getAllEmployee:function (callback) {
            var rest = Restangular.all('/employee/allemployee');
            rest.get('').then(function (data) {
                callback(data);
            })
        },
        
    }
});

