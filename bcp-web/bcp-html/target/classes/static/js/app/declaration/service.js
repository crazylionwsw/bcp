/**
 * 银行报批
 */
app.factory('declarationService',function (Restangular,$filter){
    return {

        //列表页面
        getDataByPage:function (currentPage,callback){
            var declarations = Restangular.all('/declarations?currentPage='+currentPage);
            declarations.get('').then(function (data){
                callback(data);
            });
        },

        searchDeclaration:function (name,declaration,currentPage,callback) {
            var rest = Restangular.all('/declaration/search?currentPage='+currentPage+"&name="+name);
            rest.post(declaration).then(function (data){
                callback(data);
            });
        },
        
        //查询  id    回显数据
        getOne:function(id,callback){
            var rest = Restangular.one("/declaration",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        //查询      customerTransactionId      回显数据
        getOneByCustomerTransactionId:function(id,callback){
            var rest = Restangular.one("/declaration/transaction",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        //保存    银行报批数据
        save:function (declaration,callback){
            var rest = Restangular.all('/declaration');
            rest.post(declaration).then(function (data){
                callback(data);
            });
        },

        //报批
        submit:function (id,callback){
            var rest = Restangular.all('/declaration/'+id+'/submit');
            rest.post().then(function (data){
                callback(data);
            });
        },

        //银行反馈
        feedback:function (id,declarationResult,callback){
            var rest = Restangular.all('/declaration/'+id+'/feedback');
            rest.post(declarationResult.declarationResult).then(function (data){
                callback(data);
            });
        },
        
        //更新银行报批专项分期说明
        updateDeclarationStageApplication : function (id, callback) {
            var rest = Restangular.one("/declaration/"+id+'/update');
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        getDeclarationHistorys : function (transactionId,callback) {
            var rest = Restangular.one("/declaration/"+transactionId+'/historys');
            return rest.get('').then(function(data){
                callback(data);
            });
        }
        
    }
});