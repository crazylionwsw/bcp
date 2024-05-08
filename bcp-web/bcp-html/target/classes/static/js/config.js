// config

var app =
    angular.module('app')
        .config(
            ['$controllerProvider', '$compileProvider', '$filterProvider', '$provide','datepickerConfig', 'datepickerPopupConfig','momentPickerProvider','laddaProvider',
                function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide, datepickerConfig, datepickerPopupConfig,momentPickerProvider,laddaProvider) {

                    // lazy controller, directive and service
                    app.controller = $controllerProvider.register;
                    app.directive  = $compileProvider.directive;
                    app.filter     = $filterProvider.register;
                    app.factory    = $provide.factory;
                    app.service    = $provide.service;
                    app.constant   = $provide.constant;
                    app.value      = $provide.value;

                    datepickerConfig.showWeeks = false;
                    datepickerConfig.formatMonth = 'M月';
                    datepickerConfig.formatDayHeader = 'EEE';
                    datepickerConfig.formatDayTitle = 'yyyy年-M月';
                    datepickerConfig.formatMonthTitle = 'yyyy年';

                    datepickerPopupConfig.currentText = '今天';
                    datepickerPopupConfig.clearText = '清除';
                    datepickerPopupConfig.closeText = '关闭';

                    momentPickerProvider.options({
                        /* Picker properties */
                        locale:        'zh-cn',
                        format:        'L LTS',
                        minView:       'second',
                        maxView:       'minute',
                        startView:     'day',
                        autoclose:     true,
                        today:         true,
                        keyboard:      true,

                        /* Extra: Views properties */
                        leftArrow:     '&larr;',
                        rightArrow:    '&rarr;',
                        yearsFormat:   'YYYY',
                        monthsFormat:  'MMM',
                        daysFormat:    'D',
                        hoursFormat:   'HH:[00]',
                        minutesFormat: moment.localeData().longDateFormat('LT').replace(/[aA]/, ''),
                        secondsFormat: 'ss',
                        minutesStep:   5,
                        secondsStep:   1
                    });

                    laddaProvider.setOption({ /* optional */
                        style: 'zoom-in',
                        spinnerSize: 35,
                        spinnerColor: '#ffffff'
                    });
                }
            ])
        .config(['$translateProvider', function($translateProvider){
            // Register a loader for the static files
            // So, the module will search missing translation tables under the specified urls.
            // Those urls are [prefix][langKey][suffix].
            $translateProvider.useStaticFilesLoader({
                prefix: 'l10n/',
                suffix: '.js'
            });
            // Tell the module what language to use by default
            $translateProvider.preferredLanguage('cn');
            // Tell the module to store the language in the local storage
            $translateProvider.useLocalStorage();
        }])
        .config(['RestangularProvider', function(RestangularProvider) {
            RestangularProvider.setBaseUrl('/json');
            RestangularProvider.setDefaultHeaders({'Client-Type': 'PC'});

            RestangularProvider.addResponseInterceptor(function (data, operation, what, url, response, deferred){
                //登陆不做数据剥离
                if( !url.contains("/json/login") && !url.contains("/json/oauth2url/")) {
                    /*对返回的数据处理*/
                    if (data.c == "0") {
                        return data.d;
                    } else if (data.c == "9") {
                        return data.d;
                    } else {
                        //console.log(data.m);
                    }
                }
                return data;
            });
        }])
        .config(function (localStorageServiceProvider) {
            localStorageServiceProvider
                .setPrefix('BCP')
                .setNotify(true, true);
        })
        .run(function(Restangular, localStorageService, loginService, $location){
            Restangular.addFullRequestInterceptor(function (elem, operation, path, url, headers, params, httpConfig){
                if("/json/login" != url) {
                    return {
                        headers: _.extend(headers, {'User-Token': localStorageService.get('User-Token')}),
                    };
                }
            });
            Restangular.addErrorInterceptor(function(response){
                if ( response.status == 401 ) {

                    var loginAlert = localStorageService.get('loginAlert'); //用来控制只弹出一次
                    if (!loginAlert) {
                        alert("登录信息失效或被异地登录,请重新登录！");
                        localStorageService.set('loginAlert', 1);
                        loginService.logout(function(){
                             $location.path("/auth/signin");
                        });
                    }

                } else {
                    // Some other unknown Error.
                    console.log( response );
                }
                // Stop the promise chain.
                return false;
            });
        });