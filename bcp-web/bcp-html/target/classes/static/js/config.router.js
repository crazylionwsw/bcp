'use strict';

/**
 * Config for the router
 */
angular.module('app')
    .run(
        ['$rootScope', '$state', '$stateParams','$routeParams',
            function ($rootScope,   $state,   $stateParams, $routeParams) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;
                $rootScope.$routeParams = $routeParams;
            }
        ]
    )
    .run(function(editableOptions, editableThemes) {
        editableThemes.bs3.inputClass = 'input-sm';
        editableThemes.bs3.buttonsClass = 'btn-sm';
        editableOptions.theme = 'bs3';
    })
    .config(
        ['$stateProvider', '$urlRouterProvider',
            function ($stateProvider, $urlRouterProvider) {

                $urlRouterProvider.when('/wxlogin',function($location, $state, $rootScope){
                    $rootScope.wxLoginCode = true;
                    $state.go("auth.signin",{code:$location.$$search.code});
                }).otherwise('/app/dashboard-v2');
                $stateProvider
                    .state('survey', {
                        url: '/survey/:id',
                        templateUrl: 'tpl/view/survey/index.html',
                        controller:'surveyController',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load([
											'js/app/survey/controller.js',
											'js/app/survey/service.js'
                                        ]);
                                }]
                        }
                    })
                    /*统计图标*/
                    .state('charts',{
                        url:'/charts/:uid/:mode',
                        templateUrl:'tpl/view/echarts/index.html',
                        controller:'echartsController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load('ui.select').then(
                                    function () {
                                        return $ocLazyLoad.load([
                                            'js/app/echarts/controller.js',
                                            'js/app/echarts/service.js',
                                            'js/app/dashboard/controller.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/loginuser/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/balanceaccount/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/device/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/employee/service.js'

                                        ]);
                                    });
                            }]
                        }
                    })
                    .state('app', {
                        abstract: true,
                        url: '/app',
                        templateUrl: 'tpl/app.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load([
                                            'js/controllers/chart.js',
                                            'js/app/auth/service.js'
                                        ]);
                                }]
                        }
                    })
                   /* .state('app.dashboard-v1', {
                        url: '/dashboard-v1',
                        templateUrl: 'tpl/app_dashboard_v1.html',
                    })*/
                  /*  .state('app.dashboard-v1', {
                        url: '/dashboard-v1',
                        templateUrl: 'tpl/app_dashboard_v1.html',
                        controller:'dashboardController',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load([
                                            'js/app/dashboard/controller.js',
                                            'js/app/dashboard/service.js'
                                        ]);
                                }]
                        }
                    })*/
                    .state('app.dashboard-v2', {
                        url: '/dashboard-v2',
                        templateUrl: 'tpl/app_dashboard_v2.html',
                        controller:'dashboardController',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load([
                                        'js/app/dashboard/controller.js',
                                        'js/app/dashboard/service.js'
                                    ]);
                                }]
                        }
                    })

                    /*  系统管理 */
                    .state('app.systems',{
                        url:'/systems',
                        template:'<div class="app-content-body app-content-full fade-in-up" ui-view>'
                    })
                    /*  系统参数管理 */
                    .state('app.systems.sysparam',{
                        url:'/sysparam',
                        templateUrl:'tpl/view/sysparam/index.html',
                        controller:'sysparamController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/sysparam/controller.js',
                                                'js/app/sysparam/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    /*  业务管理 */
                    .state('app.systems.business',{
                        url:'/business',
                        templateUrl:'tpl/view/business/index.html',
                        controller:'businessController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/business/controller.js',
                                                'js/app/business/service.js',
                                                'js/app/billtype/service.js',
                                                'js/app/imagetype/service.js',
                                                'js/app/fee/service.js',
                                                'js/app/cashsource/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    /*  合同管理 */
                    .state('app.systems.document',{
                        url:'/document',
                        templateUrl:'tpl/view/document/index.html',
                        controller:'documentController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/document/controller.js',
                                                'js/app/document/service.js',
                                                'js/app/msgtemplatectrl/service.js',
                                                'js/app/imagetype/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    /*  单据管理 */
                    .state('app.systems.billtype',{
                        url:'/billtype',
                        templateUrl:'tpl/view/billtype/index.html',
                        controller:'billtypeController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/billtype/controller.js',
                                                'js/app/billtype/service.js',
                                                'js/app/imagetype/service.js',
                                                'js/app/document/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    //业务事件类型
                    .state('app.systems.businesseventtype',{
                        url:'/businesseventtype',
                        templateUrl:'tpl/view/businesseventtype/index.html',
                        controller:'businesseventtypeController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/businesseventtype/controller.js',
                                                'js/app/businesseventtype/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    /*  车型列表 */
                    .state('app.systems.cartype',{
                        url:'/cartypes',
                        templateUrl:'tpl/view/cartype/index.html',
                        controller:'carTypeController',
                        resolve:{
                            deps:['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/cartype/controller.js',
                                    'js/app/cartype/service.js'
                                ])
                            }]
                        }
                    })
                    /*  组织机构 */
                    .state('app.systems.orginfo',{
                        url:'/orginfos',
                        templateUrl:'tpl/view/orginfo/index.html',
                        controller:'orginfoController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/loginuser/service.js',
                                                'js/app/orginfo/controller.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/province/service.js',
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    .state('app.systems.orginfoedit',{
                        url:'/orginfo/edit/:id',
                        templateUrl:'tpl/view/orginfo/form.html',
                        controller:'orginfoController',
                        resolve:{
                            deps :['$ocLazyLoad',
                                function ($ocLazyLoad){
                                    return $ocLazyLoad.load('angularBootstrapNavTree').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/orginfo/controller.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/creditproduct/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    /*  系统资源 */
                    .state('app.systems.resource',{
                        url:'/resources',
                        templateUrl:'tpl/view/sysresource/index.html',
                        controller:'sysresourceController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('angularBootstrapNavTree').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/sysresource/controller.js',
                                                'js/app/sysresource/service.js',
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /*  设备管理 */
                    .state('app.systems.device',{
                        url:'/devices',
                        templateUrl:'tpl/view/device/index.html',
                        controller:'deviceController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/device/controller.js',
                                                'js/app/device/service.js',
                                                'js/app/orginfo/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    /**
                     * 登录管理
                     */
                    .state('app.systems.logmanagement',{
                        url:'/logmanagement',
                        templateUrl:'tpl/view/logmanagement/index.html',
                        controller:'logManagementController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/logmanagement/controller.js',
                                                'js/app/logmanagement/service.js',
                                                'js/app/orginfo/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    /*  绑定记录 */
                    .state('app.systems.bind',{
                        url:'/binds',
                        templateUrl:'tpl/view/bind/index.html',
                        controller:'bindController',
                        resolve:{
                            deps:['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/bind/controller.js',
                                    'js/app/bind/service.js',
                                    'js/app/loginuser/service.js',
                                    'js/app/device/service.js'
                                ])
                            }]
                        }
                    })
                    /*  登录用户列表 */
                    .state('app.systems.loginuser',{
                        url:'/loginusers',
                        templateUrl:'tpl/view/loginuser/index.html',
                        controller:'loginUserController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/loginuser/controller.js',
                                                'js/app/loginuser/service.js',
                                                'js/app/sysrole/service.js',
                                                'js/app/workflow/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    /*  版本控制  */
                    .state('app.systems.apkversion',{
                        url:'/versions',
                        templateUrl:'tpl/view/version/index.html',
                        controller:'versionController',
                        resolve:{
                            deps:['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/version/controller.js',
                                    'js/app/version/service.js',
                                ])
                            }]
                        }
                    })
                    /*  日志管理  */
                    .state('app.systems.log',{
                        url:'/logs',
                        templateUrl:'tpl/view/log/index.html',
                        controller:'logController',
                        resolve:{
                            deps:['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/log/controller.js',
                                    'js/app/log/service.js',
                                    'js/app/loginuser/service.js'
                                ])
                            }]
                        }
                    })
                    /*  数据管理  */
                    .state('app.systems.sync',{
                        url:'/sync',
                        templateUrl:'tpl/view/sync/index.html',
                        controller:'syncController',
                        resolve:{
                            deps:['$ocLazyLoad',
                                function ($ocLazyLoad){
                                    return $ocLazyLoad.load(['ui.select']).then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/app/sync/controller.js',
                                                'js/app/sync/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })

                    /*任务消息管理*/
                    .state('app.systems.taskdescribes',{
                        url:'/taskdescribes',
                        templateUrl:'tpl/view/taskdescribes/index.html',
                        controller:'tasksController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/taskdescribes/controller.js',
                                                'js/app/taskdescribes/service.js',
                                                'js/app/businesseventtype/service.js',
                                                'js/app/msgsubscribe/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })

                    /*  档案管理 */
                    .state('app.systems.imagetype',{
                        url:'/imagetypes',
                        templateUrl:'tpl/view/imagetype/index.html',
                        controller:'imagetypeController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/imagetype/controller.js',
                                                'js/app/imagetype/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    /*  意见反馈 */
                    .state('app.systems.feedback',{
                        url:'/feedback',
                        templateUrl:'tpl/view/feedback/index.html',
                        controller:'feedBackController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/feedback/controller.js',
                                                'js/app/feedback/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    /**
                     * 销售政策
                     */
                    .state('app.systems.salespolicy',{
                        url:'/salespolicy',
                        templateUrl:'tpl/view/salespolicy/index.html',
                        controller:'salespolicyController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/salespolicy/controller.js',
                                                'js/app/salespolicy/service.js',
                                                'js/app/province/service.js',
                                                'js/app/business/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    /*收费项*/
                    .state('app.systems.fee',{
                        url:'/fee',
                        templateUrl:'tpl/view/fee/index.html',
                        controller:'feeController',
                        resolve:{
                            deps:['$ocLazyLoad',function ($ocLazyLoad) {
                                return $ocLazyLoad.load([
                                    'js/app/fee/controller.js',
                                    'js/app/fee/service.js'
                                ])
                            }]
                        }
                    })

                    /*  资金来源 */
                    .state('app.systems.cashsource',{
                        url:'/cashsources',
                        templateUrl:'tpl/view/cashsource/index.html',
                        controller:'cashsourceController',
                        resolve:{
                            deps :['$ocLazyLoad',
                                function ($ocLazyLoad){
                                    return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/cashsource/controller.js',
                                                'js/app/cashsource/service.js',
                                                'js/app/guaranteeway/service.js',
                                                'js/app/repaymentway/service.js',
                                                'js/app/sysparam/service.js',
                                                'js/app/imagetype/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    /*还款方式*/
                    .state('app.systems.repaymentway',{
                        url:'/repaymentway',
                        templateUrl:'tpl/view/repaymentway/index.html',
                        controller:'repaymentwayController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/repaymentway/controller.js',
                                    'js/app/repaymentway/service.js'
                                ])
                            }]
                        }
                    })

                    /*担保方式*/
                    .state('app.systems.guaranteeway',{
                            url:'/guaranteeway',
                            templateUrl:'tpl/view/guaranteeway/index.html',
                            controller:'guaranteewayController',
                            resolve:{
                                deps: ['$ocLazyLoad',
                                    function( $ocLazyLoad ){
                                        return $ocLazyLoad.load('ui.select').then(
                                            function(){
                                                return $ocLazyLoad.load([
                                                    'js/app/guaranteeway/controller.js',
                                                    'js/app/guaranteeway/service.js',
                                                    'js/app/imagetype/service.js'
                                                ]);
                                            }
                                        );
                                    }
                                ]
                            }
                        }
                    )
                    /*地区管理*/
                    .state('app.systems.province',{
                        url:'/province',
                        templateUrl:'tpl/view/province/index.html',
                        controller:'provinceController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/province/controller.js',
                                                'js/app/province/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    /*  系统角色列表 */
                    .state('app.systems.sysrole',{
                        url:'/roles',
                        templateUrl:'tpl/view/sysrole/index.html',
                        controller:'sysroleController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/sysrole/controller.js',
                                                'js/app/sysrole/service.js',
                                                'js/app/sysresource/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /*  系统管理 */
                    .state('app.business',{
                        url:'/business',
                        template:'<div class="app-content-body app-content-full fade-in-up" ui-view>'
                    })
                    /*  分期产品 */
                    .state('app.business.creditproduct',{
                        url:'/creditproduct',
                        templateUrl:'tpl/view/creditproduct/index.html',
                        controller:'creditproductController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select']).then(
                                    function(){
                                        return $ocLazyLoad.load([
                                            'js/app/creditproduct/controller.js',
                                            'js/app/creditproduct/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/repaymentway/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/province/service.js',
                                            'js/app/cashsource/service.js'
                                        ]);
                                    }
                                );
                            }]
                        }
                    })

                    /*  渠道管理 */
                    .state('app.business.cardealer',{
                        url:'/cardealer',
                        templateUrl:'tpl/view/cardealer/index.html',
                        controller:'cardealerController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/cardealer/controller.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/cartype/service.js',
                                                'js/app/cashsource/service.js',
                                                'js/app/province/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/business/service.js',
                                                'js/app/employee/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/sysparam/service.js',
                                                'js/app/workflow/service.js',
                                                'js/app/customertransaction/service.js',
                                                'js/app/paymentbill/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /*  渠道员工 */
                    .state('app.business.dealeremployee',{
                        url:'/dealeremployee',
                        templateUrl:'tpl/view/dealeremployee/index.html',
                        controller:'dealeremployeeController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/dealeremployee/controller.js',
                                    'js/app/dealeremployee/service.js'
                                ])
                            }]
                        }
                    })
                    //  渠道分组
                    .state('app.business.dealergroup',{
                        url:'/dealergroup',
                        templateUrl:'tpl/view/dealergroup/index.html',
                        controller:'dealerGroupController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/dealergroup/controller.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/sysparam/service.js',
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /*渠道分成 */
                    .state('app.business.dealersharing',{
                        url:'/dealersharing',
                        templateUrl:'tpl/view/dealersharing/index.html',
                        controller:'dealersharingController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(

                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/dealersharing/controller.js',
                                                'js/app/dealersharing/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/sysparam/service.js',
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    //渠道分成详细
                    .state('app.business.dealersharingdetails',{
                        url:'/dealersharing/details/:id',
                        templateUrl:'tpl/view/dealersharing/details.html',
                        controller:'dealersharingController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(

                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/dealersharing/controller.js',
                                                'js/app/dealersharing/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/sysparam/service.js',
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    /*分组分成 */
                    .state('app.business.groupsharing',{
                        url:'/groupsharing',
                        templateUrl:'tpl/view/groupsharing/index.html',
                        controller:'groupsharingController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(

                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/groupsharing/controller.js',
                                                'js/app/groupsharing/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/sysparam/service.js',
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })
                    .state('app.business.groupsharingdetails',{
                        url:'/groupsharing/details/:id',
                        templateUrl:'tpl/view/groupsharing/details.html',
                        controller:'groupsharingController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(

                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/groupsharing/controller.js',
                                                'js/app/groupsharing/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/sysparam/service.js',
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /**
                     * 促销管理
                     */
                    .state('app.business.promotionpolicy',{
                        url:'/promotionpolicy',
                        templateUrl:'tpl/view/promotionpolicy/index.html',
                        controller:'promotionpolicyController',
                        resolve:{
                            deps:['$ocLazyLoad',function ($ocLazyLoad) {
                                return $ocLazyLoad.load([
                                    'js/app/promotionpolicy/controller.js',
                                    'js/app/promotionpolicy/service.js'
                                ])
                            }]
                        }
                    })
                    /**
                     * 销售政策
                     */
                    // .state('app.business.salespolicy',{
                    //     url:'/salespolicy',
                    //     templateUrl:'tpl/view/salespolicy/index.html',
                    //     controller:'salespolicyController',
                    //     resolve:{
                    //         deps:['$ocLazyLoad',function ($ocLazyLoad) {
                    //             return $ocLazyLoad.load([
                    //                 'js/app/salespolicy/controller.js',
                    //                 'js/app/salespolicy/service.js'
                    //             ])
                    //         }]
                    //     }
                    // })
                    .state('app.business.salespolicy',{
                        url:'/salespolicy',
                        templateUrl:'tpl/view/salespolicy/index.html',
                        controller:'salespolicyController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/salespolicy/controller.js',
                                                'js/app/salespolicy/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /*分成标准*/
                    .state('app.systems.banksettlementstandard',{
                        url:'/banksettlementstandard',
                        templateUrl:'tpl/view/banksettlementstandard/index.html',
                        controller:'banksettlementstandardController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/banksettlementstandard/controller.js',
                                                'js/app/banksettlementstandard/service.js',
                                                'js/app/cashsource/service.js',
                                                'js/app/sysparam/service.js',
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })

                    /*  模版管理 */
                    .state('app.systems.msgtemplatectrl',{
                        url:'/msgtemplatectrl',
                        templateUrl:'tpl/view/msgtemplatectrl/index.html',
                        controller:'msgTemplatelController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/msgtemplatectrl/controller.js',
                                                'js/app/msgtemplatectrl/service.js',
                                                'js/app/imagetype/service.js',
                                                'js/app/document/service.js',
                                                'js/app/sysparam/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })

                    /* 通知管理 */
                    .state('app.systems.notice',{
                        url:'/notice',
                        templateUrl:'tpl/view/notice/index.html',
                        controller:'noticeController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/notice/controller.js',
                                                'js/app/notice/service.js',
                                                'js/app/loginuser/service.js',
                                                'js/app/sysrole/service.js',
                                                'js/app/msgtemplates/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/cardealer/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })

                    //问卷调查
                    .state('app.systems.questioncategory',{
                        url:'/questioncategory',
                        templateUrl:'tpl/view/questioncategory/index.html',
                        controller:'questioncategoryController',
                        resolve:{
                            deps:['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/app/questioncategory/controller.js',
                                                'js/app/questioncategory/service.js',
                                                'js/app/workflow/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/customertransaction/service.js',
                                            ]);
                                        }
                                    )
                                }
                            ]
                        }
                    })
                        
                    /**
                     * 顾客调查模板
                     */
                    .state('app.systems.customersurveytemplate',{
                        url:'/customersurveytemplate',
                        templateUrl:'tpl/view/customersurveytemplate/index.html',
                        controller:'customersurveytemplateController',
                        resolve:{
                            deps:['$ocLazyLoad',function($ocLazyLoad){
                                return $ocLazyLoad.load('ui.select').then(function(){
                                    return $ocLazyLoad.load([
                                        'js/app/customersurveytemplate/controller.js',
                                        'js/app/customersurveytemplate/service.js',
                                        'js/app/questioncategory/service.js'
                                    ])
                                })
                            }]
                        }
                    })

                    /**
                     * 邮件模版emailtemplate
                     */
                    .state('app.systems.emailtemplate',{
                        url:'/emailtemplate',
                        templateUrl:'tpl/view/emailtemplate/index.html',
                        controller:'emailTemplateController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/emailtemplate/controller.js',
                                                'js/app/emailtemplate/service.js',
                                                'js/app/msgtemplatectrl/service.js',
                                                'js/app/document/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })


                    /**
                     * 推送模版pushtemplate
                     */
                    .state('app.systems.pushtemplate',{
                        url:'/pushtemplate',
                        templateUrl:'tpl/view/pushtemplate/index.html',
                        controller:'pushTemplateController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/pushtemplate/controller.js',
                                                'js/app/pushtemplate/service.js',
                                                'js/app/msgtemplatectrl/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    /**
                     * 订阅源
                     */
                    // .state('app.systems.subsribesource',{
                    //     url:'/subsribesource',
                    //     templateUrl:'tpl/view/subsribesource/index.html',
                    //     controller:'subsribesourceController',
                    //     resolve:{
                    //         deps: ['$ocLazyLoad',
                    //             function( $ocLazyLoad ){
                    //                 return $ocLazyLoad.load('ui.select').then(
                    //                     function(){
                    //                         return $ocLazyLoad.load([
                    //                             'js/app/subsribesource/controller.js',
                    //                             'js/app/subsribesource/service.js'
                    //                         ]);
                    //                     }
                    //                 );
                    //             }
                    //         ]
                    //     }
                    // })

                    /**
                     * 贴息管理
                     */
                    .state('app.business.compensatorypolicy',{
                        url:'/compensatorypolicy',
                        templateUrl:'tpl/view/compensatorypolicy/index.html',
                        controller:'compensatorypolicyController',
                        resolve:{
                            deps :['$ocLazyLoad',
                                function ($ocLazyLoad){
                                    return $ocLazyLoad.load(['ui.select','textAngular']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/compensatorypolicy/controller.js',
                                                'js/app/compensatorypolicy/service.js',
                                                'js/app/cartype/service.js',
                                                'js/app/compensatorypolicyinfo/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /**
                     * 贴息公式
                     */
                    .state('app.business.compensatorypolicyinfo',{
                        url:'/compensatorypolicyinfo',
                        templateUrl:'tpl/view/compensatorypolicyinfo/index.html',
                        controller:'compensatorypolicyinfoController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/compensatorypolicyinfo/controller.js',
                                    'js/app/compensatorypolicyinfo/service.js'
                                ])
                            }]
                        }
                    })

                    /**贴息管理*/
                    .state('app.business.compensatorypolicyedit',{
                    url:'/compensatorypolicy/edit/:id',
                        templateUrl:'tpl/view/compensatorypolicy/form.html ',
                        controller:'compensatorypolicyController',
                        resolve:{
                            deps :['$ocLazyLoad',
                                function ($ocLazyLoad){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/compensatorypolicy/controller.js',
                                                'js/app/compensatorypolicy/service.js',
                                                'js/app/cartype/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })

                    /*客户管理   */
                    .state('app.business.customer',{
                        url:'/customer/:cid',
                        templateUrl:'tpl/view/customer/index.html',
                        controller:'customerController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/customer/controller.js',
                                                'js/app/customer/service.js',
                                                'js/app/sysparam/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/customertransaction/service.js',
                                                'js/app/cartype/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/document/service.js',
                                                'js/app/workflow/service.js',
                                                'js/app/cashsource/service.js',
                                                'js/app/employee/service.js',
                                                'js/app/business/service.js',
                                                'js/app/ordernewcar/service.js',
                                                'js/app/appointpayment/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/carregistry/service.js',
                                                'js/app/enhancement/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.business.customerdatacompletion',{
                        url:'/customer/query/:id',
                        templateUrl:'tpl/view/customer/data_completion.html',
                        controller:'customerController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/customer/controller.js',
                                    'js/app/customer/service.js',
                                    'js/app/customerimage/service.js',
                                    'js/app/cartype/service.js',
                                    'js/app/cardealer/service.js',
                                    'js/app/employee/service.js',
                                    'js/app/orginfo/service.js',
                                    'js/app/creditproduct/service.js',
                                    'js/app/document/service.js',
                                    'js/app/package/service.js',
                                    'js/app/ordernewcar/service.js',
                                    'js/app/cashsource/service.js',
                                    'js/app/enhancement/service.js'
                                ])
                            }]
                        }
                    })
                    /*销售汇总 */
                    .state('app.business.statistic',{
                        url:'/statistic',
                        templateUrl:'tpl/view/statistic/index.html',
                        controller:'statisticController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/statistic/controller.js',
                                                'js/app/statistic/service.js',
                                                'js/app/cartype/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/employee/service.js',
                                                'js/app/customer/service.js',
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /*客户资料打印   */
                    .state('app.business.printer',{
                        url:'/printer',
                        templateUrl:'tpl/view/printer/index.html',
                        controller:'printerController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/printer/controller.js',
                                                'js/app/printer/service.js',
                                                'js/app/cartype/service.js',
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    .state('app.business.printeredit',{
                        url:'/printer/edit/:id',
                        templateUrl:'tpl/view/printer/form.html',
                        controller:'printerController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/printer/controller.js',
                                                'js/app/printer/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/document/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /*  渠道信息编辑 */
                    .state('app.business.cardealeredit',{
                            url:'/cardealer/edit/:id',
                            templateUrl:'tpl/view/cardealer/form.html',
                            controller:'cardealerFormController',
                            resolve:{
                                deps: ['$ocLazyLoad',
                                    function( $ocLazyLoad ){
                                        return $ocLazyLoad.load('ui.select').then(
                                            function(){
                                                return $ocLazyLoad.load([
                                                    'js/app/cardealer/controller.js',
                                                    'js/app/cardealer/service.js',
                                                    'js/app/creditproduct/service.js',
                                                    'js/app/cartype/service.js',
                                                    'js/app/employee/service.js',
                                                    'js/app/business/service.js',
                                                    'js/app/cashsource/service.js',
                                                    'js/app/orginfo/service.js',
                                                    'js/app/province/service.js'
                                                ]);
                                            }
                                        );
                                    }
                                ]
                            }
                        }
                    )

                    /**
                     * 任务消息处理
                     */
                    .state('app.system.seeTasks',{
                            url:'/taskdescribe/see/:id',
                            templateUrl:'tpl/view/taskdescribes/form.html',
                            controller:'tasksFormController',
                            resolve:{
                                deps: ['$ocLazyLoad',
                                    function( $ocLazyLoad ){
                                        return $ocLazyLoad.load('angularBootstrapNavTree').then(
                                            function(){
                                                return $ocLazyLoad.load([
                                                    'js/app/taskdescribes/controller.js',
                                                    'js/app/taskdescribes/service.js',
                                                ]);
                                            }
                                        );
                                    }
                                ]
                            }
                        }
                    )
                    /**
                     * 消息模版
                     */
                    .state('app.systems.msgtemplates',{
                        url:'/msgtemplates',
                        templateUrl:'tpl/view/msgtemplates/index.html',
                        controller:'msgController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/msgtemplates/controller.js',
                                                'js/app/msgtemplates/service.js',
                                                'js/app/emailtemplate/service.js',
                                                'js/app/pushtemplate/service.js',
                                                'js/app/sysparam/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /**
                     * 订阅维护
                     */
                    .state('app.systems.msgsubscribe',{
                        url:'/msgsubscribe',
                        templateUrl:'tpl/view/msgsubscribe/index.html',
                        controller:'subScribeController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/msgsubscribe/controller.js',
                                                'js/app/msgsubscribe/service.js',
                                                'js/app/subsribesource/service.js',
                                                'js/app/businesseventtype/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/cashsource/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/sysrole/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /*  客户业务 */
                    .state('app.business.customertransaction',{
                        url:'/customertransaction/:cname',
                        templateUrl:'tpl/view/customertransaction/index.html',
                        controller:'customerTransactionController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                    function () {
                                        return $ocLazyLoad.load([
                                            'js/app/customertransaction/controller.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/business/service.js',
                                            'js/app/enhancement/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/decompress/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/decompress/service.js',
                                            'js/app/overduerecord/service.js'
                                        ]);
                                    }
                                );
                            }]
                        }
                    })
                    /*  业务台账 */
                    .state('app.business.businessbook',{
                        url:'/businessbook/:cname',
                        templateUrl:'tpl/view/businessbook/index.html',
                        controller:'businessbookController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load('ui.select').then(
                                    function () {
                                        return $ocLazyLoad.load([
                                            'js/app/businessbook/controller.js',
                                            'js/app/businessbook/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/business/service.js',
                                            'js/app/enhancement/service.js',
                                            'js/app/customerimage/service.js'
                                        ]);
                                    }
                                );
                            }]
                        }
                    })
                    /*  资质审查 */
                    .state('app.business.cardemand',{
                        billTypeCode: 'A001',
                        url:'/cardemand/:tid',
                        templateUrl:'tpl/view/cardemand/index.html',
                        controller:'cardemandController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                    function () {
                                        return $ocLazyLoad.load([
                                            'js/app/cardemand/controller.js',
                                            'js/app/cardemand/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/document/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/creditphotograph/service.js',
                                            'js/app/declaration/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/fee/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/customersurveytemplate/service.js',
                                            'js/app/questioncategory/service.js',
                                            'js/app/sysrole/service.js',
                                            'js/app/paymentbill/service.js'
                                        ]);
                                    }
                                );
                            }]
                        }
                    })
                    /*  新车分期订单 */
                    .state('app.business.ordernewcar',{
                        billTypeCode: 'A002',
                        url:'/ordernewcars/:tid',
                        templateUrl:'tpl/view/ordernewcar/index.html',
                        controller:'orderNewCarController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                    function(){
                                        return $ocLazyLoad.load([
                                            'js/app/ordernewcar/controller.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/creditproduct/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/document/service.js',
                                            'js/app/cardemand/service.js',
                                            'js/app/declaration/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/fee/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/poundagesettlement/service.js',
                                            'js/app/survey/service.js',
                                            'js/app/questioncategory/service.js',
                                            'js/app/sysrole/service.js',
                                            'js/app/paymentbill/service.js'
                                        ]);
                                    }
                                );
                            }]
                        }
                    })

                    /*  业务调整 */
                    .state('app.business.businessexchange',{
                        billTypeCode: 'A030',
                        url:'/businessexchanges/:tid',
                        templateUrl:'tpl/view/businessexchange/index.html',
                        controller:'businessexchangeController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                    function(){
                                        return $ocLazyLoad.load([
                                            'js/app/businessexchange/controller.js',
                                            'js/app/businessexchange/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/creditproduct/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/document/service.js',
                                            'js/app/declaration/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/fee/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/sysrole/service.js',
                                            'js/app/paymentbill/service.js'
                                        ]);
                                    }
                                );
                            }]
                        }
                    })

                    /*  缴费管理 */
                    .state('app.business.paymentbill',{
                        billTypeCode: 'A025',
                        url:'/paymentbills/:tid',
                        templateUrl:'tpl/view/paymentbill/index.html',
                        controller:'paymentbillController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                    function(){
                                        return $ocLazyLoad.load([
                                            'js/app/paymentbill/controller.js',
                                            'js/app/paymentbill/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/creditproduct/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/document/service.js',
                                            'js/app/declaration/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/fee/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/sysrole/service.js'
                                        ]);
                                    }
                                );
                            }]
                        }
                    })


                    //预约刷卡
                    .state('app.business.appointswipingcard',{
                        billTypeCode: 'A026',
                        url:'/appointswipingcard/:tid',
                        templateUrl:'tpl/view/appointswipingcard/index.html',
                        controller:'appointswipingcardController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['ui.select','angularBootstrapNavTree']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/appointswipingcard/controller.js',
                                                'js/app/appointswipingcard/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/customerimage/service.js',
                                                'js/app/cashsource/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/cartype/service.js',
                                                'js/app/workflow/service.js',
                                                'js/app/employee/service.js',
                                                'js/app/billtype/service.js',
                                                'js/app/guaranteeway/service.js',
                                                'js/app/sysparam/service.js',
                                                'js/app/ordernewcar/service.js',
                                                'js/app/customertransaction/service.js',
                                                'js/app/fee/service.js',
                                                'js/app/bankcard/service.js',
                                                'js/app/employee/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/paymentbill/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    //渠道刷卡
                    .state('app.business.swipingcard',{
                        billTypeCode: 'A019',
                        url:'/swipingcard/:tid',
                        templateUrl:'tpl/view/swipingcard/index.html',
                        controller:'swipingcardController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select','angularBootstrapNavTree']).then(
                                    function(){
                                        return $ocLazyLoad.load([
                                            'js/app/swipingcard/controller.js',
                                            'js/app/swipingcard/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/bankcard/service.js',
                                            'js/app/paymentbill/service.js'
                                        ]);
                                    }
                                );
                            }]
                        }
                    })

                    //渠道还款
                    .state('app.business.dealerrepayment',{
                        url:'/dealerrepayment',
                        templateUrl:'tpl/view/dealerrepayment/index.html',
                        controller:'dealerRepaymentController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select','angularBootstrapNavTree']).then(
                                    function(){
                                        return $ocLazyLoad.load([
                                            'js/app/dealerrepayment/controller.js',
                                            'js/app/dealerrepayment/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/appointpayment/service.js',
                                            'js/app/bankcard/service.js',
                                            'js/app/paymentbill/service.js'
                                        ]);
                                    }
                                );
                            }]
                        }
                    })
                    /*  垫资支付 */
                    .state('app.business.appointpayment',{
                        billTypeCode: 'A004',
                        url:'/appointpayment/:tid',
                        templateUrl:'tpl/view/appointpayment/index.html',
                        controller:'appointpaymentController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select','angularBootstrapNavTree']).then(
                                    function() {
                                        return $ocLazyLoad.load([
                                            'js/app/appointpayment/controller.js',
                                            'js/app/appointpayment/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/document/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/cardemand/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/fee/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/appointswipingcard/service.js',
                                            'js/app/bankcard/service.js',
                                            'js/app/paymentbill/service.js'
                                        ])
                                    }
                                )
                            }]
                        }
                    })
                    /*  转移过户 */
                    .state('app.business.cartransfer',{
                        billTypeCode: 'A023',
                        url:'/cartransfer/:tid',
                        templateUrl:'tpl/view/cartransfer/index.html',
                        controller:'cartransferController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select','angularBootstrapNavTree']).then(
                                    function() {
                                        return $ocLazyLoad.load([
                                            'js/app/cartransfer/controller.js',
                                            'js/app/cartransfer/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/document/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/cardemand/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/fee/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/carregistry/service.js',
                                            'js/app/paymentbill/service.js'
                                        ])
                                    }
                                )
                            }]
                        }
                    })
                    /*  银行报批 */
                    .state('app.business.declaration',{
                        billTypeCode: 'A015',
                        url:'/declaration/:tid',
                        templateUrl:'tpl/view/declaration/index.html',
                        controller:'declarationController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select','textAngular']).then(
                                    function() {
                                        return $ocLazyLoad.load([
                                            'js/app/declaration/controller.js',
                                            'js/app/declaration/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/cardemand/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/document/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/fee/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/loginuser/service.js',
                                            'js/app/taskdescribes/service.js',
                                            'js/app/msgtemplates/service.js'
                                        ])
                                    }
                                )
                            }]
                        }
                    })

                    /*  预约提车 */
                    .state('app.business.pickupcar',{
                        url:'/pickupcar',
                        templateUrl:'tpl/view/pickupcar/index.html',
                        controller:'pickupcarController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/pickupcar/controller.js',
                                    'js/app/pickupcar/service.js',
                                    'js/app/billtype/service.js',
                                    'js/app/cardealer/service.js',
                                    'js/app/customer/service.js',
                                    'js/app/employee/service.js',
                                    'js/app/workflow/service.js',
                                    'js/app/customertransaction/service.js',
                                    'js/app/cashsource/service.js',
                                    'js/app/orginfo/service.js',
                                    'js/app/customerimage/service.js',
                                    'js/app/sysparam/service.js'
                                ])
                            }]
                        }
                    })
                    /*  车辆上牌 */
                    .state('app.business.carregistry',{
                        billTypeCode: 'A005',
                        url:'/carregistry/:tid',
                        templateUrl:'tpl/view/carregistry/index.html',
                        controller:'carregistryController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select','angularBootstrapNavTree']).then(
                                    function() {
                                        return $ocLazyLoad.load([
                                            'js/app/carregistry/controller.js',
                                            'js/app/carregistry/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/document/service.js',
                                            'js/app/paymentbill/service.js'
                                        ])
                                    }
                                )
                            }]
                        }
                    })
                    /*  车辆评估 */
                    .state('app.business.carvaluation',{
                        billTypeCode: 'A021',
                        url:'/carvaluation',
                        templateUrl:'tpl/view/carvaluation/index.html',
                        controller:'carvaluationController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load(['ui.select']).then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/carvaluation/controller.js',
                                                'js/app/carvaluation/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/employee/service.js',
                                                'js/app/workflow/service.js',
                                                'js/app/customertransaction/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/sysparam/service.js',
                                                'js/app/customerimage/service.js',
                                                'js/app/cashsource/service.js',
                                                'js/app/ordernewcar/service.js',
                                                'js/app/billtype/service.js',
                                                'js/app/cartype/service.js',
                                                'js/app/guaranteeway/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /*  资料快递 */
                    .state('app.business.fileexpress',{
                        url:'/fileexpress',
                        templateUrl:'tpl/view/fileexpress/index.html',
                        controller:'fileexpressController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/fileexpress/controller.js',
                                    'js/app/fileexpress/service.js',
                                    'js/app/cardealer/service.js',
                                    'js/app/customer/service.js',
                                    'js/app/employee/service.js',
                                    'js/app/cashsource/service.js',
                                    'js/app/orginfo/service.js',
                                    'js/app/customerimage/service.js',
                                    'js/app/billtype/service.js',
                                    'js/app/customertransaction/service.js'
                                ])
                            }]
                        }
                    })
                    /*  银行制卡 */
                    .state('app.business.bankcard',{
                        billTypeCode:'A011',
                        url:'/bankcard/:tid',
                        templateUrl:'tpl/view/bankcard/index.html',
                        controller:'bankcardController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                    function () {
                                        return $ocLazyLoad.load([
                                            'js/app/bankcard/controller.js',
                                            'js/app/bankcard/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/cartype/service.js'
                                        ]
                                    )
                                })
                            }]
                        }
                    })
                    .state('app.business.bankcardedit',{
                        url:'/bankcard/edit/:id',
                        templateUrl:'tpl/view/bankcard/form.html',
                        controller:'bankcardController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/bankcard/controller.js',
                                    'js/app/bankcard/service.js',
                                    'js/app/customerimage/service.js',
                                    'js/app/customer/service.js',
                                    'js/app/customertransaction/service.js'
                                ])
                            }]
                        }
                    })
                    /*  车辆抵押 */
                    .state('app.business.dmvpledge',{
                        billTypeCode: 'A008',
                        url:'/dmvpledge/:tid',
                        templateUrl:'tpl/view/dmvpledge/index.html',
                        controller:'dmvpledgeController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['angularBootstrapNavTree','ui.select']).then(
                                    function () {
                                        return $ocLazyLoad.load([
                                            'js/app/dmvpledge/controller.js',
                                            'js/app/dmvpledge/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/document/service.js',
                                            'js/app/cardemand/service.js'
                                            ]
                                        )
                                    })
                            }]
                        }
                    })
                    /*  资料打包 */
                    .state('app.business.package',{
                        url:'/package',
                        templateUrl:'tpl/view/package/index.html',
                        controller:'packageController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/package/controller.js',
                                    'js/app/package/service.js',
                                    'js/app/customer/service.js',
                                    'js/app/cardealer/service.js',
                                    'js/app/ordernewcar/service.js',
                                    'js/app/cashsource/service.js',
                                    'js/app/customertransaction/service.js',
                                    'js/app/cartype/service.js',
                                    'js/app/document/service.js',
                                    'js/app/orginfo/service.js',
                                    'js/app/workflow/service.js',
                                    'js/app/sysparam/service.js',
                                    'js/app/guaranteeway/service.js',
                                    'js/app/customerimage/service.js',
                                    'js/app/fee/service.js',
                                    'js/app/billtype/service.js',
                                    'js/app/employee/service.js'
                                ])
                            }]
                        }
                    })

                    /*取消订单*/
                    .state('app.business.cancelorder',{
                        billTypeCode: 'A012',
                        url:'/cancelorder/:tid',
                        templateUrl:'tpl/view/cancelorder/index.html',
                        controller:'cancelorderController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select','angularBootstrapNavTree']).then(
                                    function() {
                                        return $ocLazyLoad.load([
                                            'js/app/cancelorder/controller.js',
                                            'js/app/cancelorder/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/document/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/fee/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/paymentbill/service.js'
                                        ])
                                    }
                                )
                            }]
                        }
                    })

                    .state('app.business.rejectcustomer',{
                        url:'/rejectcustomer',
                        templateUrl:'tpl/view/rejectcustomer/index.html',
                        controller:'rejectcustomerController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select','angularBootstrapNavTree']).then(
                                    function() {
                                        return $ocLazyLoad.load([
                                            'js/app/rejectcustomer/controller.js',
                                            'js/app/rejectcustomer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/document/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/billtype/service.js'
                                        ])
                                    }
                                )
                            }]
                        }
                    })

                    /*资料补全*/
                    .state('app.business.enhancement',{
                        url:'/enhancement',
                        templateUrl:'tpl/view/enhancement/index.html',
                        controller:'enhancementController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/enhancement/controller.js',
                                                'js/app/enhancement/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/workflow/service.js',
                                                'js/app/employee/service.js',
                                                'js/app/cartype/service.js',
                                                'js/app/guaranteeway/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/document/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/fee/service.js',
                                                'js/app/customerimage/service.js',
                                                'js/app/billtype/service.js',
                                                'js/app/cashsource/service.js',
                                                'js/app/sysparam/service.js',
                                                'js/app/customertransaction/service.js',
                                                'js/app/ordernewcar/service.js',
                                                'js/app/paymentbill/service.js'
                                            ]);
                                        }
                                    );
                                }
                            ]
                        }
                    })
                    /*  资料归还 */
                    .state('app.business.returninfo',{
                        billTypeCode: 'A009',
                        url:'/returninfo/:tid',
                        templateUrl:'tpl/view/returninfo/index.html',
                        controller:'returninfoController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load([
                                    'js/app/returninfo/controller.js',
                                    'js/app/returninfo/service.js',
                                    'js/app/customer/service.js',
                                    'js/app/cardealer/service.js',
                                    'js/app/employee/service.js',
                                    'js/app/customertransaction/service.js',
                                    'js/app/orginfo/service.js',
                                    'js/app/cashsource/service.js',
                                    'js/app/customerimage/service.js',
                                    'js/app/sysparam/service.js',
                                    'js/app/billtype/service.js',
                                ])
                            }]
                        }
                    })

                    /*审核模板*/
                    .state('app.systems.notetpl',{
                            url:'/notetpl',
                            templateUrl:'tpl/view/notetpl/index.html',
                            controller:'notetplController',
                            resolve:{
                                deps: ['$ocLazyLoad',
                                    function( $ocLazyLoad ){
                                        return $ocLazyLoad.load('ui.select').then(
                                            function(){
                                                return $ocLazyLoad.load([
                                                    'js/app/notetpl/controller.js',
                                                    'js/app/notetpl/service.js'
                                                ]);
                                            }
                                        );
                                    }
                                ]
                            }
                        }
                    )
                    /*重新签约*/
                    .state('app.business.resetorder',{
                        billTypeCode:'A014',
                        url:'/resetorder/:tid',
                        templateUrl:'tpl/view/resetorder/index.html',
                        controller:'resetorderController',
                        resolve:{
                            deps:['$ocLazyLoad',
                                function($ocLazyLoad){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/app/resetorder/controller.js',
                                                'js/app/resetorder/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/employee/service.js',
                                                'js/app/workflow/service.js',
                                                'js/app/cashsource/service.js',
                                                'js/app/orginfo/service.js',
                                                'js/app/cartype/service.js',
                                                'js/app/ordernewcar/service.js',
                                                'js/app/sysparam/service.js',
                                                'js/app/customertransaction/service.js',
                                                'js/app/paymentbill/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    /*支行结算*/
                    .state('app.business.poundagesettlement',{
                        url:'/poundagesettlements',
                        templateUrl:'tpl/view/poundagesettlement/index.html',
                        controller:'poundagesettlementController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load('ui.select').then(
                                    function () {
                                        return $ocLazyLoad.load([
                                            'js/app/poundagesettlement/controller.js',
                                            'js/app/poundagesettlement/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/customertransaction/service.js'
                                        ]);
                                    });
                            }]
                        }
                    })
                    /*收款计划*/
                    .state('app.business.chargefeeplan',{
                    url:'/chargefeeplan',
                    templateUrl:'tpl/view/chargefeeplan/index.html',
                    controller:'chargefeeplanController',
                    resolve:{
                        deps :['$ocLazyLoad',function ($ocLazyLoad){
                            return $ocLazyLoad.load('ui.select').then(
                                function () {
                                    return $ocLazyLoad.load([
                                        'js/app/chargefeeplan/controller.js',
                                        'js/app/chargefeeplan/service.js',
                                        'js/app/cardealer/service.js',
                                        'js/app/customer/service.js',
                                        'js/app/loginuser/service.js',
                                        'js/app/customertransaction/service.js',
                                        'js/app/sysparam/service.js',
                                        'js/app/orginfo/service.js',
                                        'js/app/cashsource/service.js',
                                        'js/app/workflow/service.js',
                                        'js/app/ordernewcar/service.js',
                                        'js/app/billtype/service.js',
                                        'js/app/employee/service.js'
                                    ]);
                                });
                            }]
                        }
                    })

                    //业务报表
                    .state('app.business.dailyreport',{
                        url:'/report',
                        templateUrl:'tpl/view/report/index.html',
                        controller:'reportController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/report/controller.js',
                                                'js/app/report/service.js',
                                                'js/app/sysparam/service.js'
                                            ]);
                                        }
                                    );
                            }]
                        }
                    })

                    //银行日流水
                    .state('app.business.bankdailystatement',{
                        url:'/bankdailystatement',
                        templateUrl:'tpl/view/bankdailystatement/index.html',
                        controller:'bankdailystatementController',
                        resolve:{
                            deps: ['$ocLazyLoad',
                                function( $ocLazyLoad ){
                                    return $ocLazyLoad.load('ui.select').then(
                                        function(){
                                            return $ocLazyLoad.load([
                                                'js/app/bankdailystatement/controller.js',
                                                'js/app/bankdailystatement/service.js',
                                                'js/app/cardealer/service.js',
                                                'js/app/cashsource/service.js',
                                                'js/app/ordernewcar/service.js',
                                                'js/app/customer/service.js',
                                                'js/app/cartype/service.js',
                                                'js/app/customertransaction/service.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.business.balanceaccount',{
                    url:'/balanceaccount',
                    templateUrl:'tpl/view/balanceaccount/index.html',
                    controller:'balanceaccountController',
                    resolve:{
                        deps :['$ocLazyLoad',function ($ocLazyLoad){
                            return $ocLazyLoad.load('ui.select').then(
                                function () {
                                    return $ocLazyLoad.load([
                                        'js/app/balanceaccount/controller.js',
                                        'js/app/balanceaccount/service.js',
                                        'js/app/cardealer/service.js',
                                        'js/app/customer/service.js',
                                        'js/app/loginuser/service.js',
                                        'js/app/customertransaction/service.js',
                                        'js/app/sysparam/service.js',
                                        'js/app/orginfo/service.js',
                                        'js/app/cashsource/service.js',
                                        'js/app/workflow/service.js',
                                        'js/app/ordernewcar/service.js',
                                        'js/app/billtype/service.js',
                                        'js/app/customerimage/service.js',
                                        'js/app/paymentbill/service.js'
                                    ]);
                                });
                            }]
                        }
                    })

                    /*  解押管理 */
                    .state('app.business.decompress',{
                        billTypeCode: 'A031',
                        url:'/decompress/:tid',
                        templateUrl:'tpl/view/decompress/index.html',
                        controller:'decompressController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select','angularBootstrapNavTree']).then(
                                    function() {
                                        return $ocLazyLoad.load([
                                            'js/app/decompress/controller.js',
                                            'js/app/decompress/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/document/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/sysparam/service.js',
                                            'js/app/sysrole/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/fee/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/paymentbill/service.js'
                                        ])
                                    }
                                )
                            }]
                        }
                    })

                    /*  逾期记录 */
                    .state('app.business.overdue',{
                        billTypeCode: 'A032',
                        url:'/overduerecord/:tid',
                        templateUrl:'tpl/view/overduerecord/index.html',
                        controller:'overdueController',
                        resolve:{
                            deps :['$ocLazyLoad',function ($ocLazyLoad){
                                return $ocLazyLoad.load(['ui.select','angularBootstrapNavTree']).then(
                                    function() {
                                        return $ocLazyLoad.load([
                                            'js/app/overduerecord/controller.js',
                                            'js/app/overduerecord/service.js',
                                            'js/app/cardealer/service.js',
                                            'js/app/customer/service.js',
                                            'js/app/employee/service.js',
                                            'js/app/workflow/service.js',
                                            'js/app/customerimage/service.js',
                                            'js/app/document/service.js',
                                            'js/app/orginfo/service.js',
                                            'js/app/cartype/service.js',
                                            'js/app/cashsource/service.js',
                                            'js/app/billtype/service.js',
                                            'js/app/sysrole/service.js',
                                            'js/app/ordernewcar/service.js',
                                            'js/app/fee/service.js',
                                            'js/app/guaranteeway/service.js',
                                            'js/app/customertransaction/service.js',
                                            'js/app/paymentbill/service.js',
                                            'js/app/sysparam/service.js'
                                        ])
                                    }
                                )
                            }]
                        }
                    })
                    
                    .state('auth', {
                        url: '/auth',
                        template: '<div ui-view class="fade-in-right-big smooth"></div>',
                        resolve: {
                            deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                                return $ocLazyLoad.load([
                                    'js/app/auth/controller.js',
                                    'js/app/auth/service.js'
                                ])
                            }]
                        }

                    })
                    .state('auth.signin', {
                        url: '/signin',
                        params:{code:null},
                        templateUrl: 'tpl/view/auth/signin.html',
                        controller:'authController'
                    })
                    .state('auth.404', {
                        url: '/404',
                        templateUrl: 'tpl/page_404.html'
                    })

            }
        ]
    );