
app.factory("cardealerService",function (Restangular){

    return {

        /*  分页功能 */
        getDataByPage: function(currentPage,callback){
            var rest = Restangular.all('/cardealers/page?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        /**
         * 经销商列表
         */
        lookup: function (callback){
            var rest = Restangular.all('/cardealer/lookups');
             rest.get('').then(function(data){
                callback(data);
            });
        },

        getAllCarDealers:function (callback) {
            var rest = Restangular.all('/cardealers');
            rest.get('').then(function (data) {
                callback(data);
            })
        },

        getCardelearTransaction:function (id,callback) {
            var rest = Restangular.one('/cardealer/transaction',id);
            rest.get('').then(function (data) {
                callback(data);
            })
        },

        /**
         * 根据ID回显
         */
        getOne:function(id,callback){
            var cardealer=Restangular.one("/cardealer",id);
            return cardealer.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 审查/审批/签批
         */
        sign:function(id, signinfo, callback){
            var cardealerJson = Restangular.all('/cardealer/'+id+'/sign');
            cardealerJson.post(signinfo).then(function (data){
                callback(data);
            });
        },

        /*  渠道对应的员工分页功能 */
        getDealerEmployees: function(id,currentPageCarDealer,callback){
            var cardealer = Restangular.one('cardealer',id).all('/employees?currentPageCarDealer='+currentPageCarDealer);
            return cardealer.get('').then(function (data){
                callback(data);
            });
        },

        //保存
        saveCarDealer:function (cardealer,callback){
            var cardealerMsg = Restangular.all('/cardealer');
            cardealerMsg.post(cardealer).then(function (data){
                callback(data);
            });
        },

        //添加渠道员工
        saveDealerEmployee:function (dealeremployee,callback){
            var dealeremployeesInfo = Restangular.all('/cardealer/employee');
            dealeremployeesInfo.post(dealeremployee).then(function (data){
                callback(data);
            });
        },

        deleteDealerEmployee:function (dealeremployee,callback){
            var id = dealeremployee.id;
            var rest = Restangular.one('/cardealer/employee',id);
            rest.remove('').then(function (data){
                callback(data);
            });
        },
        
        //验证
        checkUniqueCarDealer:function(message,propname,propval,callback){
            var cardealer = Restangular.all('/validate/cardealer/'+propname+'/'+propval);
            cardealer.post(message).then(function(data){
                callback(data);
            })
        },
        
        //验证
        checkUniqueDealerEmployee:function(message,propname,propval,callback){
            var dealeremployee = Restangular.all('/validate/dealeremployee/'+propname+'/'+propval);
            dealeremployee.post(message).then(function(data){
                callback(data);
            })
        },

        //验证集团
        checkUniqueDealerGroup:function(message,propname,propval,callback){
            var rest = Restangular.all('/validate/dealergroup/'+propname+'/'+propval);
            rest.post(message).then(function(data){
                callback(data);
            })
        },
        //      通过  经销商ID  查询 经销商的渠道报单行
        getCashSource:function(id,callback) {
            var rest = Restangular.one("/cardealer",id).all("cashsource");
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        
        //      通过  经销商ID  查询 经销商的渠道合作行
        getCooperationCashSource:function(id,callback) {
            var rest = Restangular.one("/cardealer",id).all("cooperationCashsource");
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        //      通过 银行ID  查询 CashSource
        getOneByCashSourceId:function(id,callback){
            var cashsource=Restangular.one("/cashsource",id);
            return cashsource.get('').then(function(data){
                callback(data);
            });
        },

        delete:function (cardealer, callback) {
            var rest = Restangular.one('/cardealer',cardealer.id);
            rest.remove().then(function (data) {
                callback(data);
            })
        },
        /**
         * 查找分期经理
         * @param ids
         * @param callback
         */
        getBussinessManByIds: function (ids, callback){
            var rest = Restangular.all('/employee/pickup');
            rest.post(ids).then(function(data){
                callback(data);
            });
        },

        /*查询*/
        search:function (cardealerQuery, currentPage, callback){
            var rest = Restangular.all('/cardealer/search?currentPage='+currentPage);
            rest.post(cardealerQuery).then(function (data){
                callback(data);
            });
        },

        /**
         *      判断  当前登录用户 是否是当前经销商所属部门的部门经理
         * @param orginfoId
         * @param loginEmployeeId
         * @param callback
         */
        checkAuditPermission:function (orginfoId, loginEmployeeId, callback) {
            var rest = Restangular.one('/cardealer?orginfoId='+orginfoId + '&loginEmployeeId='+loginEmployeeId);
            rest.get().then(function (data) {
                callback(data);
            })
        },


        /*  渠道分组*/
        getDealerGroups: function(currentPage, callback){
            var rest = Restangular.all('/cardealer/groups?currentPage='+currentPage);
            return rest.get('').then(function (data){
                callback(data);
            });
        },

        //保存
        saveDealerGroup:function (group,callback){
            var rest = Restangular.all('/cardealer/group');
            rest.post(group).then(function (data){
                callback(data);
            });
        },

        deleteDealerGroup:function (id,callback){
            var rest = Restangular.one('/cardealer/group',id);
            rest.remove('').then(function (data){
                callback(data);
            });
        },

        getDealerGroup:function (id,callback){
            var rest = Restangular.one('/cardealer/group',id);
            rest.get('').then(function (data){
                callback(data);
            });
        },

        /**
         * 渠道分组列表
         */
        lookupDealerGroups: function (callback){
            var rest = Restangular.all('/cardealer/groups/lookups');
            rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 保存分成比例
         */
        saveSharingRatio: function (sharingRatio, callback){
            var rest = Restangular.all('/cardealer/sharingratio');
            rest.post(sharingRatio).then(function(data){
                callback(data);
            });
        },

        /**
         * 保存分成比例
         */
        getSharingRatio: function (id, callback){
            var rest = Restangular.one('/cardealer',id).all('sharingratio');
            rest.get('').then(function(data){
                callback(data);
            });
        },

        //获取单条渠道员工信息
        getDealerEmployeeById: function(id,callback){
            var cardealer = Restangular.one('/cardealer/employeesub',id);
            return cardealer.get('').then(function (data){
                callback(data);
            });
        },

            //获取所有渠道员工
        getEmployeeCardealer: function (callback){
            var rest = Restangular.all('/cardealer/employee/avaliable');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getCarDealerByIds:function (tids,callback) {
            var rest = Restangular.all('/cardealer/more');
            rest.post(tids).then(function (data){
                callback(data);
            });
        },

        transferDealer:function (cardealer,callback) {
            var bid = cardealer.bid;
            var rest = Restangular.all('/cardealer/transfer?bid='+bid);
            rest.post(cardealer.tid).then(function (data){
                callback(data);
            });
        }
    }
});

