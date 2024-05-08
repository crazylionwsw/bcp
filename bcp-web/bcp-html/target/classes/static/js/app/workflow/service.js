/**
 * Created by LB on 2016-10-24.
 */

app.factory('workflowService',function (Restangular){
    return {

        getAllGroups:function (callback) {
            var rest = Restangular.all('/workflow/groups');
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        assignUserGroups: function(username, groupIds, callback) {
            var rest = Restangular.one('/workflow/user', username).all('groups');
            return rest.post(groupIds).then(function(data){
                callback(data);
            });
        },

        getUserGroups:function (username, callback){
            var rest = Restangular.one('/workflow/user', username).all('groups');
            rest.get('').then(function(data){
                callback(data);
            });
        },

        getUserGroupsByActivitiUserId:function (activitiuserid, callback){
            //var rest = Restangular.one('/workflow/user', activitiuserid).all('groups');
            var rest = Restangular.one('/workflow/user/groups',activitiuserid);
            rest.get('').then(function(data){
                callback(data);
            });
        },

        getTaskCurrentGroup:function (businessKey,callback){
            var teskMsg = Restangular.all('/workflow/currentgroup?businessKey='+businessKey);
            return teskMsg.get('').then(function(data){
                callback(data);
            });
        },

        getTaskCurrentGroups:function (businessKey,callback){
            var teskMsg = Restangular.all('/workflow/currentgroups?businessKey='+businessKey);
            return teskMsg.get('').then(function(data){
                callback(data);
            });
        },
        
        //通过单据的ID查询该单据的唯一工作流数据
        getByBillId:function (id,callback) {
            var workflowinfo = Restangular.one("/workflow/bill", id);
            return workflowinfo.get('').then(function (data) {
                callback(data);
            });
        },

        //通过单据的ID查询该单据的唯一工作流数据
        getByBillIdAndCode:function (id,callback) {
            var workflowinfo = Restangular.one("/workflow/bill/temsign/"+id);
            return workflowinfo.get('').then(function (data) {
                callback(data);
            });
        },
        sign:function (id,signInfo,callback) {
            var workflowinfo = Restangular.all("/workflow/bill/"+ id+'/sign');
            workflowinfo.post(signInfo).then(function (data) {
                callback(data);
            })
        },
        saveSignInfo :function (id,signInfo,callback) {
            var workflowinfo = Restangular.all("/workflow/bill/"+ id+'/comment');
            workflowinfo.post(signInfo).then(function (data) {
                callback(data);
            })
        },

        /***********************************************************************************************/
        getMyTasks:function (userid, businessKey,callback){
            var teskMsg = Restangular.all('/workflow/mytask?userid='+userid+'&businessKey='+businessKey);
            teskMsg.get('').then(function(data){
                callback(data);
            });
        },

        /***********************************************************************************************/

        getMyOwnTasks:function (userid, businessKey,callback){
            var teskMsg = Restangular.all('/task/search?userid='+userid+'&businessKey=');
            teskMsg.get('').then(function(data){
                callback(data);
            });
        },
        getActivitiNodeProperty:function (businessKey,callback) {
            var teskMsg = Restangular.all('/task/node?businessKey='+businessKey);
            teskMsg.get('').then(function(data){
                callback(data);
            });
        },

        againFlow:function (userid, businessKey,callback){
            var teskMsg = Restangular.all('/workflow/bill/'+userid+'/'+businessKey+'/');
            teskMsg.get('').then(function(data){
                callback(data);
            });
        },
        
        checkBankCardApplyStatus:function (transactionid, callback) {
            var bankcard = Restangular.one("/bankcard/status", transactionid);
            return bankcard.get('').then(function (data) {
                callback(data);
            });
        },

        reset :function (bill,callback) {
            var rest = Restangular.all("/workflow/bill/reset");
            rest.post(bill).then(function (data) {
                callback(data);
            })
        },

    }
});

