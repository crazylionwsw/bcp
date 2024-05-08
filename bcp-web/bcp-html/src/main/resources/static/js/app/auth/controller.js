/**
 * Created by LB on 2016-10-14.
 */

/**
 * 认证控制器
 */
app.controller('authController', function ($scope, $location, $state, $stateParams, Restangular, loginService, $rootScope, localStorageService, $loading) {
        $scope.user ={};

        $scope.init = function(){
            if($stateParams.code && $rootScope.wxLoginCode){
                $rootScope.wxLoginCode = false;
                loginService.loginByWeiXinCode($stateParams.code,function(data){
                    if(data) {
                        $scope.authError = data;
                    } else {
                        $state.go("app.dashboard-v2")
                    }
                });
            }
        };
        /*登录验证*/
        $scope.login = function () {
            localStorageService.remove('loginAlert');
            loginService.login($scope.user, function (data){
                if(data) {
                    $scope.authError = data;
                } else {
                    $location.path('/app/');
                }                 
            });
        };

        //  获取微信登录二维码
        $scope.wxCodeLogin = function () {
          loginService.getWeiXinCode(function (data) {
              if (data) window.open(data,"_self");
          })
        };
        $scope.init();

    });
