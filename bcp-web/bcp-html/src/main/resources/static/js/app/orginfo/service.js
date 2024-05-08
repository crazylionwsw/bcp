/**
 * Created by LB on 2016-10-24.
 */

app.factory('orginfoService',function (Restangular){
    return {

        //查询树列表数据
        getOrgs:function (id, callback){
            if(!id) {
                id = "0";
            }
            var rest = Restangular.one('/org', id).all("orgs");
            rest.get('').then(function (data){
                callback(data);
            });
        },

        saveOrg:function (orginfo,callback){
            var rest = Restangular.all('/org');
            rest.post(orginfo).then(function (data){
                callback(data);
            });
        },

        getAll:function (callback) {
            var rest = Restangular.all('/orgs');
            rest.get('').then(function (data) {
              callback(data);
          })
        },

        lookup:function (callback) {
            var rest = Restangular.all('/org/lookups');
            rest.get('').then(function (data) {
                callback(data);
            })
        },

        /**
         * 根据id获取
         */
        getOrg:function (id,callback){
            var rest = Restangular.one("/org",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        //添加，修改员工信息
        saveEmployee:function (employee,isLeader,callback){
            var rest = Restangular.all('/org/employee/'+isLeader);
            rest.post(employee).then(function (data){
                callback(data);
            });
        },

        /**
         * 根据id获取
         * @param customer
         * @param callback
         */
        getEmployee:function (id,callback){
            var rest = Restangular.one("/org/employee",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 根据部门id查渠道经理
         * @param id
         * @param callback
         */
        getEmployeesByOrgInfoId:function (id,callback) {
            var rest = Restangular.one('/cardealer/managers',id);
            rest.get('').then(function (data) {
                callback(data);
            })
        },
        

        /*查询*/
        searchEmployees:function (employee,currentPage, callback){
            var rest = Restangular.all('/org/employee/search?currentPage='+currentPage);
            rest.post(employee).then(function (data){
                callback(data);
            });
        },


        //员工列表-有分页
        getEmployees : function (orginfoId,currentPage,callback){
            var rest = Restangular.one('org',orginfoId).all('employees');
            return rest.get("", {currentPage: currentPage}).then(function (data){
                callback(data);
            });
        },

        getEmployeesLookup : function (callback){
            var rest = Restangular.all('/employee/lookups');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        
        //验证
        checkUniqueEmployee:function(message,propname,propvalue,callback){
            var employee = Restangular.all('/validate/employee?propname='+propname+'&propvalue='+propvalue);
            employee.post(message).then(function(data){
                callback(data);
            })
        },
        
        checkUniqueOrgInfo:function(message,propname,propval,callback){
            var orginfo = Restangular.all('/validate/orginfo/'+propname+'/'+propval);
            orginfo.post(message).then(function(data){
                callback(data);
            })
        },

        deleteEmployee:function (employee,callback) {
            var id = employee.id;
            var rest = Restangular.one('/org/employee',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        deleteOrg:function (orginfo,callback) {
            var id = orginfo.id;
            var rest = Restangular.one('/org',id);
            rest.remove('').then(function(data){
                callback(data);
            });
        },

        /**
         * 根据loginUserId获取
         * @param callback
         */
        getEmployeeByLoginUserId:function (id,callback){
            var rest = Restangular.one("/employee/loginuser",id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 根据loginUserId获取
         * @param callback
         */
        getNameByLoginUserId:function (id,callback){
            var rest = Restangular.one("/loginuser",id).all('name');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         * 根据loginUserId获取
         * @param callback
         */
        getSuperOrgByLoginUserId:function (id,callback){
            var rest = Restangular.one("/org/super",id).all('loginuser');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        /**
         *      根据  员工ID        查询员工最上级组织信息
         * @param id
         * @param callback
         * @returns {*|Promise}
         */
        getSuperOrgByEmployeeId:function (id,callback){
            var rest = Restangular.one("/org/super",id).all('employee');
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        //  根据部门ID，查询最上级部门信息
        getSuperOrgByOrgId:function (id,callback) {
            var rest = Restangular.one('/org/super',id).all('org');
            rest.get('').then(function(data){
                callback(data);
            });
        },

        getEmployeeById:function (id,callback){
            var rest = Restangular.one("/employee/sub/"+id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },
        //根据角色查询员工
        getEmployeeByRoleId:function (id,callback){
            var rest = Restangular.one("/employee/role/"+id);
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        getByIds: function (ids, callback){
            var rest = Restangular.all('/orginfo/pickup');
            rest.post(ids).then(function(data){
                callback(data);
            });
        },
        
        //根据loginUserId判断当前登录用户是否为部门经理(新车金融部、渠道一部.....)
        getOrgsByLoginUserId:function (loginUserId,callback){
            var rest = Restangular.one("/orginfos/" + loginUserId+"/loginuser");
            return rest.get('').then(function(data){
                callback(data);
            });
        },

        syncEmployee:function (id,callback) {
            var rest = Restangular.one('/wechat/save/employee',id);
            rest.get('').then(function (data) {
                callback(data);
            })
        }
    }
});
