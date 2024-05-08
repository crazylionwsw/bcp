/**
 * Created by LB on 2016-10-24.
 */

app.factory('banksettlementstandardService',function (Restangular){
    return {

        /**
         * 作废、删除支行手续费分成标准
         * @param id
         * @param callback
         */
        delete:function (banksettlementstandard,callback) {
            var rest = Restangular.one('/bankSettlementStandard',banksettlementstandard.id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },
        
        getData: function(callback){
            var rest = Restangular.all('/bankSettlementStandards');
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        //添加或修改
        save:function (banksettlementstandard,callback){
            var rest = Restangular.all('/bankSettlementStandard');
            rest.post(banksettlementstandard).then(function (data){
                callback(data);
            });
        },
        
        check:function (channelId,declarationId,callback) {
            var rest = Restangular.all('/bankSettlementStandard/check/'+channelId+'/'+declarationId);
            rest.get('').then(function(data){
                callback(data);
            })
        }
    }
});
