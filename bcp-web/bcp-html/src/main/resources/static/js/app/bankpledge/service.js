/**
 * Created by LB on 2016-10-24.
 */

app.factory('bankpledgeService',function (Restangular,$filter){
    return {
        
        /* 分页功能 */
       getDataByPage:function (status, currentPage, callback){
            var rest = Restangular.one('bankpledges',status).all('?currentPage='+currentPage);
            rest.get('').then(function (data){
                callback(data);
            });
        },

        submit:function (bankpledge,id,callback){
            if(!bankpledge.id)
                bankpledge.dataStatus = 1;
            var bankpledgeInfo = Restangular.one('carregistry',id).all('bankpledge');
            bankpledgeInfo.post(bankpledge).then(function (data){
                callback(data);
            });
        },

        save:function (bankpledge,id,callback){
            var bankpledgeInfo = Restangular.all('/bankpledge');
            bankpledgeInfo.post(bankpledge).then(function (data){
                callback(data);
            });
        },

        getOne:function(id,callback){
            var bankpledgeInfo=Restangular.one("/bankpledge",id);
            return bankpledgeInfo.get('').then(function(data){
                callback(data);
            });
        },
        
    }
});