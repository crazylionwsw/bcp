/** 
 * Created by LB on 2016-10-15.
 */

app.factory('loginService', function ($log, Restangular, localStorageService,$localStorage) {
    var loginService = {};
    loginService.excludeStates = ["app.dashboard-v2","auth.signin","auth.wxlogin", "survey","charts"];

    loginService.login = function (user, callback) {

        var rest = Restangular.all('/login');
        rest.post(user).then(function (data) {
            callback(dealWithLoginData(data));
        });
    };

    /**
     * 注销
     * @param callback
     */
    loginService.logout = function (callback) {
        var token = localStorageService.get("User-Token");
        var rest = Restangular.one("/logout");
        rest.get('').then(function(data){
            if(data){
                localStorageService.remove("User-Token");
                localStorageService.remove("user");
            }
        });
        callback();
    };

    /**
     * 判断用户是否登录
     * @returns {*|boolean}
     */
    loginService.isLogin = function () {
        var token = localStorageService.get("User-Token");
        return token && "" != token;

        //检查token的有效性
        /*var rest = Restangular.one('/token', token).all('validation');
        rest.get('').then(function (data) {
            callback(data);
        });*/
    };

    /**
     * 检查用户权限
     * @param state
     * @returns {boolean}
     */
    loginService.checkAuthorize = function (state) {
        //授权检测，在页面跳转的时候进行验证授权是否有效
        var valid = false;

        //检查该router是否需要授权
        angular.forEach(loginService.excludeStates, function(val,key){
            if(val && val == state.name){ //如果该router不需要授权
                valid = true;
                return;
            }
        });


        if (!valid) { //如果该router需要授权
            var userData = localStorageService.get("user");
            angular.forEach(userData.permissions, function(val,key){
                if(val && val.url.contains(state.name)){
                    valid = true;
                    return;
                }
            });
        }

        return valid;
    };

    loginService.saveEmployee = function(employee, callback){
       var rest = Restangular.all('/user/profile');
        rest.post(employee).then(function (data){
            callback(data);
        });
    };

    loginService.getWeiXinCode = function(callback){
        var rest = Restangular.all('/oauth2');
        rest.get('').then(function (data){
            callback(data);
        });
    };

    loginService.loginByWeiXinCode = function(code, callback){
        var rest = Restangular.one('/oauth2url', code);
        rest.get('').then(function (data){
            callback(dealWithLoginData(data));
        });
    };

    dealWithLoginData = function (data) {
        if (data && data.c == 9) {
            return  data.m;
            //callback(data.m);
        } else {

            var userData = {
                'userID': data.d.userID,
                'userName': data.d.userName,
                'isSystem': data.d.isSystem,
                'activitiUserRoles': [],
                'employee': {username : data.d.userName, avatar: 'img/a0.jpg'},
                'permissions': data.d.permissions
            };

            if (userData.userName == 'admin') userData.employee.id = "-1";
            if (data.d.activitiUserRoles) {
                userData.activitiUserRoles = data.d.activitiUserRoles;
            }

            if (data.d.employee) {
                var employee = data.d.employee;
                //头像
                if (employee.avatarFileId) {
                    employee.avatar = '/json/file/download/' + employee.avatarFileId;
                } else {
                    employee.avatar = "img/a0.jpg";
                }
                userData.employee = employee;
            }

            localStorageService.set('user', userData);
            localStorageService.set('User-Token', data.d.token);

            return null;
        }
    };
    
    return loginService;
});