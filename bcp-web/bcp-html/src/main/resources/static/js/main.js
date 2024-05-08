'use strict';

/* Controllers */

angular.module('app')
    .controller('AppCtrl', ['$scope', '$translate', 'localStorageService', '$localStorage', '$window','$location','$log','loginService','subScribeService','FileUploader','$timeout','$rootScope', '$websocket', 'toaster',
        function($scope,   $translate, localStorageService, $localStorage, $window, $location, $log, loginService, subScribeService, FileUploader, $timeout, $rootScope, $websocket, toaster ) {
            // add 'ie' classes to html
            var isIE = !!navigator.userAgent.match(/MSIE/i);
            isIE && angular.element($window.document.body).addClass('ie');
            isSmartDevice( $window ) && angular.element($window.document.body).addClass('smart');

            // config
            $scope.app = {
                name: '富择ERP系统',
                version: '1.2.201712161200',
                // for chart colors
                color: {
                    primary: '#7266ba',
                    info:    '#23b7e5',
                    success: '#27c24c',
                    warning: '#fad733',
                    danger:  '#f05050',
                    light:   '#e8eff0',
                    dark:    '#3a3f51',
                    black:   '#1c2b36'
                },
                settings: {
                    themeID: 1,
                    navbarHeaderColor: 'bg-black',
                    navbarCollapseColor: 'bg-white-only',
                    asideColor: 'bg-black',
                    headerFixed: true,
                    asideFixed: false,
                    asideFolded: false,
                    asideDock: false,
                    container: false
                }
            };

            // save settings to local storage
            if ( angular.isDefined($localStorage.settings) ) {
                $scope.app.settings = $localStorage.settings;
            } else {
                $localStorage.settings = $scope.app.settings;
            }
            
            $scope.$watch('app.settings', function(){
                if( $scope.app.settings.asideDock  &&  $scope.app.settings.asideFixed ){
                    // aside dock and fixed must set the header fixed.
                    $scope.app.settings.headerFixed = true;
                }
                // save to local storage
                $localStorage.settings = $scope.app.settings;
            }, true);

            // messages
            $rootScope.msgCount = 0;
            //TODO: get unread messages from server
            //TODO: check count of unread messages

            // user
            if (localStorageService.get('user')) {
                $rootScope.user = localStorageService.get('user');
            }
            $rootScope.sysUrl = null;
            $rootScope.wxLoginCode = false;
            subScribeService.getWebSocketUrl(function (data) {
                $rootScope.sysUrl = data;
            });
            
            //监测localStorageService
            var ws, asyncMessages = {'CARTYPE': '车型同步完成！'};
            $rootScope.$on('LocalStorageModule.notification.setitem', function(event, msg){
                //检查用户数据
                if (msg.key == 'user' && msg.newvalue != 'null') {

                    var userData = localStorageService.get('user');

                    //修改$rootScope
                    $rootScope.user = userData;

                    //连接websocket，并开始接收websocket消息
                    ws = $websocket('ws://'+$rootScope.sysUrl+'/websocket/' + userData.userID);

                    //收到消息
                    ws.onMessage(function(message){

                        var msg = message.data;
                        //判断是否为异步操作消息
                        var asyncProc = localStorageService.get('ASYNC_PROC');

                        var index = asyncProc.indexOf(msg);
                        if(index > -1){
                            asyncProc.splice(index, 1); // will remove item
                            localStorageService.set('ASYNC_PROC', asyncProc);
                            toaster.pop('success', '系统消息', asyncMessages[msg], 0);
                        }
                    });

                    ws.onError(function(e){
                        console.log('connection Error', e);
                    });

                    ws.onClose(function(e){
                        console.log('connection closed', e);
                    });

                    ws.onOpen(function() {
                        console.log('connection open');
                    });
                }
            });
            $rootScope.$on('LocalStorageModule.notification.removeitem', function(event, msg){
                var userData = localStorageService.get('user');
                if (userData) {
                    
                }
            });


            $scope.checkLogin = function(){
                if (!loginService.isLogin()) {
                    $scope.signout();
                }
            };

            //注销
            $scope.signout = function(){
                loginService.logout(function(){
                    $location.path("/auth/signin");
                });
            };
            
            //修改头像
            $scope.changeAvatar = function(){
                $timeout(function() {
                    angular.element('#myAvatar').trigger('click');
                }, 0);
            };
            $scope.avatarUploader = new FileUploader({
                url: '/json/fileimage',
                autoUpload: true,
                removeAfterUpload: true
            });
            $scope.avatarUploader.onSuccessItem = function(fileItem, response, status, headers) {
                $scope.user.employee.avatarFileId = response.d;
                $scope.user.employee.avatar = '/json/fileimage/' + response.d;
                loginService.saveEmployee($scope.user.employee, function(data){
                    $scope.user.employee = data;
                    $scope.user.employee.avatar = '/json/fileimage/' + $scope.user.employee.avatarFileId;
                });
            };

            //保存帐户信息
            $scope.saveAccount = function(){
                //如果设置了新密码
                if ($scope.password && $scope.password.trim().length) {
                    $scope.user.employee.loginUser = {password: $scope.password.trim()};
                }

                loginService.saveEmployee($scope.user.employee, function(data){
                    if (data.cell != $scope.user.employee.cell) {
                        $scope.signout();
                    }
                    $scope.user.employee = data;

                    //localStorageService.set('user', $scope.user);
                });
            };

            $scope.$on('$stateChangeStart',
                function(evt, toState, toParams, fromState, fromParams) {

                    if (toState.name != 'survey' && toState.name != 'charts' && $rootScope.wxLoginCode ) {
                        $scope.checkLogin(); //检查是否登录

                        var userData = localStorageService.get("user");
                        if (userData && userData.userName == 'admin') {
                            return true;
                        } else {
                            if(loginService.checkAuthorize(toState) == false){
                                alert("没有操作权限！");
                                evt.preventDefault();
                                $location.path("/app/dashboard-v2");
                            }
                        }
                    }
                }
            );

            function isSmartDevice( $window )
            {
                // Adapted from http://www.detectmobilebrowsers.com
                var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
                // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
                return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
            }

            //中文时间
            moment.locale('zh-cn');
            
        }]);