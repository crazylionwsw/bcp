/**
 * Created by LB on 2016-10-24.
 */

app.factory('cashsourceService',function (Restangular){
    return {
        //树上级目录
        getCashTypes:function (callback) {
            var rest = Restangular.all('/cashsources/types');
            return rest.get("").then(function(data){
                callback(data);
            });
        },

        //查询列表数据
        getChildren:function (id, callback){
            var rest = Restangular.one('/cashsource',id).all('cashsources');
            rest.get('').then(function (data){
                callback(data);
            });
        },

        //查询所有的支行
        getChildBank:function (callback) {
        var cashsource = Restangular.all('/cashsources/childBanks');
        return cashsource.get("").then(function (data) {
            callback(data);
        });
    },


    //可用数据
        lookup:function (callback) {
            var cashsource = Restangular.all('/cashsources/lookups');
            return cashsource.get("").then(function (data) {
                callback(data);
            });
        },

        //保存资金来源
        saveCashSource:function (sourceType,callback){
            var rest = Restangular.all('/cashsource');
            rest.post(sourceType).then(function (data){
                callback(data);
            });
        },

        /*        根据  资金来源ID    查询资金来源信息          */
        getCashSource:function (id,callback){
            var rest = Restangular.one("/cashsource",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        //资金利率列表
        getRatesByCaseSource : function (cashsource,callback){
            var rest = Restangular.one("/cashsource", cashsource.id).all('sourcerates');
            return rest.get("").then(function (data){
                callback(data);
            });
        },

        //资金利率列表
        looupSourceRates : function (callback){
            var rest = Restangular.all("/cashsource/sourcerates");
            return rest.get("").then(function (data){
                callback(data);
            });
        },

        //资金机构协作用户
        getEmployeesByCashSource : function (cashsource,callback){
            var rest = Restangular.one("cashsource", cashsource.id).all('employees');
            return rest.get("").then(function (data){
                callback(data);
            });
        },


        //添加，修改利率信息
        saveSourceRate:function (sourcerate, callback){
            var rest = Restangular.all('/cashsource/sourcerate');
            rest.post(sourcerate).then(function (data){
                callback(data);
            });
        },

        //添加，修改协作用户
        saveEmployee:function (cashSourceEmployee, callback){
            var rest = Restangular.all('/cashsource/employee');
            rest.post(cashSourceEmployee).then(function (data){
                callback(data);
            });
        },


        //获得银行职工角色
        getBankEmployeeRoles:function (callback){
            var rest = Restangular.all('/cashsource/roles');
            rest.get('').then(function (data){
                callback(data);
            });
        },
        
        //根据ID查询银行职工角色
        getBankEmployeeRoleNameById:function (ids,callback){
            var rest = Restangular.all('/cashsource/role');
            return rest.post(ids).then(function(data){
                callback(data);
            });
        },
        /**
         * 唯一性验证
         * @param entity
         * @param propname
         * @param propval
         * @param callback
         */
        checkUniqueSourceRate:function(entity, propname, propval, callback){
            var rest = Restangular.all('/validate/sourcerate/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        /**
         * 唯一性验证
         * @param entity
         * @param propname
         * @param propval
         * @param callback
         */
        checkUniqueCashSource:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/cashsource/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },

        checkUniqueCashSourceEmployee:function(entity,propname,propval,callback){
            var rest = Restangular.all('/validate/cashsourceemployee/'+propname+'/'+propval);
            rest.post(entity).then(function(data){
                callback(data);
            })
        },
        /**
         * 删除资金渠道类型
         * @param selectSource
         * @param callback
         */
        deleteSource:function (selectSource,callback) {
            var id = selectSource.id;
            var rest = Restangular.one('/cashsource',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },
        /**
         * 删除资金利率
         * @param selectSource
         * @param callback
         */
        deleteRate:function (sourcerate,callback) {
            var id = sourcerate.id;
            var rest = Restangular.one('/cashsource/sourcerate',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },
        /**
         * 删除协作用户
         * @param sourcerate
         * @param callback
         */
        deleteEmployee:function (employee,callback) {
            var id = employee.id;
            var rest = Restangular.one('/cashsource/employee',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        getEmployeesByCashSourceById: function (id,callback){
            var rest = Restangular.one("/cashsource/employee",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        //获取所有银行协作员工
        getEmployeeCashsource: function (callback){
            var rest = Restangular.all('/cashsource/employees/avaliable');
            return rest.get('').then(function(data){
                callback(data);
            });
        }

    }
});
