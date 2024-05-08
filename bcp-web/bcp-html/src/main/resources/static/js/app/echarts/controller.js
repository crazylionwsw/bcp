/**
 * Created by zxp on 2017/11/14.
 */
'use strict';

app.controller('echartsController',function ($scope,$modal,echartService,orginfoService,$rootScope,$state,$stateParams,$cookies,$cookieStore,$localStorage,toaster,$filter,localStorageService){
    localStorageService.set("User-Token", $cookies.webToken);
    //  饼状图
    $scope.demandArr = ['新增', '通过', '驳回', '拒绝'];
    $scope.demandArrNum = [];
    $scope.orderArr = ['新增', '通过', '驳回', '拒绝'];
    $scope.orderArrNum =[];
    $scope.paymentArr = ['新增', '通过', '驳回'];
    $scope.paymentArrNum = [];
    $scope.carArr = ['上牌','过户','抵押'];
    $scope.carArrNum = [];

    //  柱形图
    $scope.amountArr = ['签约新增', '签约通过', '刷卡完成', '上牌 / 过户', '抵押完成'];
    $scope.amountArrNum = [];
    $scope.servicefeeArr = ['签约新增', '签约通过', '刷卡完成', '上牌 / 过户', '抵押完成'];
    $scope.servicefeeArrNum = [];
    $scope.chargeArr = ['签约新增', '签约通过', '刷卡完成', '上牌 / 过户', '抵押完成'];
    $scope.chargeArrNum= [];

    //  渠道数据统计
    $scope.cardealerCounts = 0;
    $scope.cardealerCarCount = 0;
    $scope.totalReceiveAmount = 0;

    //  渠道经理的部门信息
    $scope.orginfos = [];
    $scope.selectedOrg = {};

    $scope.searchDate;

    //  页面展示模式
    $scope.mode = 1;// 分期经理： 个人模式【1】   渠道经理：团队模式【2】,个人模式【3】

    $scope.$parent.app.settings.headerFixed = false;

    $scope.init = function () {

        if($stateParams.uid && $stateParams.mode){

            $scope.mode = $stateParams.mode;

            orginfoService.getEmployeeByLoginUserId($stateParams.uid,function (employee) {
               $scope.employee = employee;
            });
            
            //判断是否展示div
            orginfoService.getOrgsByLoginUserId($stateParams.uid,function (data) {
                if(data && data.length > 0){
                    $scope.orginfos = data;
                    $scope.selectedOrg = $scope.orginfos[$scope.orginfos.length - 1];
                }
                $scope.search();
            });
        }
    };

    //  资质
    $scope.$watch('demand',function (demand) {
        if (demand && demand !== undefined){
            $scope.demandArrNum.push({value: demand.total, name: '新增'});
            $scope.demandArrNum.push({value: demand.passed, name: '通过'});
            $scope.demandArrNum.push({value: demand.reapply, name: '驳回'});
            $scope.demandArrNum.push({value: demand.reject, name: '拒绝'});
        }
    });

    $scope.$watch('order',function (order) {
        if (order && order != undefined){

            //签约新增贷款额
            $scope.amountArrNum.push({name: '签约新增', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalAllAmount') / 10000});
            $scope.amountArrNum.push({name: '签约通过', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalPassAmount') / 10000});
            $scope.amountArrNum.push({name: '刷卡完成', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalSwingAmount') / 10000});
            $scope.amountArrNum.push({
                name: '上牌/过户',
                value: ($filter('MapControl')(order,'getValuesSumByRegexKey','totalRegistryAmount') + $filter('MapControl')(order,'getValuesSumByRegexKey','totalTransAmount')) / 10000
            });
            $scope.amountArrNum.push({name: '抵押完成', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalDmvpAmount') / 10000});

            //手续费
            $scope.chargeArrNum.push({name: '签约新增', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalAllCharge')});
            $scope.chargeArrNum.push({name: '签约通过', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalPassCharge')});
            $scope.chargeArrNum.push({name: '刷卡完成', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalSwingCharge')});
            $scope.chargeArrNum.push({
                name: '上牌/过户',
                value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalRegistryCharge') + $filter('MapControl')(order,'getValuesSumByRegexKey','totalTransCharge')
            });
            $scope.chargeArrNum.push({name: '抵押完成', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalDmvpCharge')});

            //服务费
            $scope.servicefeeArrNum.push({name: '签约新增', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalAllServiceFee')});
            $scope.servicefeeArrNum.push({name: '签约通过', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalPassServiceFee')});
            $scope.servicefeeArrNum.push({name: '刷卡完成', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalSwingServiceFee')});
            $scope.servicefeeArrNum.push({
                name: '上牌/过户',
                value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalRegistryServiceFee') + $filter('MapControl')(order,'getValuesSumByRegexKey','totalTransServiceFee')
            });
            $scope.servicefeeArrNum.push({name: '抵押完成', value: $filter('MapControl')(order,'getValuesSumByRegexKey','totalDmvpServiceFee')});

            //渠道上牌完成金额      车辆抵押登记证已收取
            $scope.totalReceiveAmount = $filter('MapControl')(order,'getValuesSumByRegexKey','totalReceiveAmount') / 10000;

        }
    });

    //  签约
    $scope.$watch('order',function (order) {
        if (order && order != undefined) {
            $scope.orderArrNum.push({value: order.total, name: '新增'});
            $scope.orderArrNum.push({value: order.passed, name: '通过'});
            $scope.orderArrNum.push({value: order.reapply, name: '驳回'});
            $scope.orderArrNum.push({value: order.reject, name: '拒绝'});
        }
    });
    
    //  垫资
    $scope.$watch("payment",function (payment) {
        if (payment && payment != undefined) {
            $scope.paymentArrNum.push({value: payment.total, name: '新增'});
            $scope.paymentArrNum.push({value: payment.passed, name: '通过'});
            $scope.paymentArrNum.push({value: payment.reapply, name: '驳回'});
        }
    });
    
    //  提车上牌
    $scope.$watch("carregistry",function (carregistry) {
        if (carregistry && carregistry != undefined) {
            $scope.carArrNum.push({value: carregistry.passed ? carregistry.passed : 0, name: '上牌'});
        }
    });

    //  抵押
    $scope.$watch("dmvpledge",function (dmvpledge) {
        if (dmvpledge && dmvpledge != undefined) {
            $scope.carArrNum.push({value: dmvpledge.end ? dmvpledge.end : 0, name: '抵押'});

            //渠道完成上牌数量  登记证已收取
            $scope.cardealerCarCount = dmvpledge.receive;

            //渠道完成订单数量
            $scope.dmvpCounts = dmvpledge.end;
        }
    });

    //  过户
    $scope.$watch("cartransfer",function (cartransfer) {
        if (cartransfer && cartransfer != undefined ) {
            $scope.carArrNum.push({value: cartransfer.passed ? cartransfer.passed : 0, name: '过户'});
        }
    });

    //  分期、渠道经理统计查询
    $scope.stageCount = function () {
        $scope.clearEcharts();
        $scope.$watch("employee",function (employee) {
            if (employee && employee.id){
                echartService.getManagerCharts(employee.id,$scope.searchDate,"A001",function (demand) {
                    $scope.demand = demand;
                });
                echartService.getManagerCharts(employee.id,$scope.searchDate,"A002",function (order) {
                    $scope.order = order;
                });
                echartService.getManagerCharts(employee.id,$scope.searchDate,"A004",function (payment) {
                    $scope.payment = payment;
                });
                echartService.getManagerCharts(employee.id,$scope.searchDate,"A005",function (carregistry) {
                    $scope.carregistry = carregistry;
                });
                echartService.getManagerCharts(employee.id,$scope.searchDate,"A008",function (dmvpledge) {
                    $scope.dmvpledge = dmvpledge;
                });
                echartService.getManagerCharts(employee.id,$scope.searchDate,"A023",function (cartransfer) {
                    $scope.cartransfer = cartransfer;
                });
                echartService.getManagerCarDealerCount(employee.id,$scope.searchDate,function (cardealer) {
                    $scope.cardealerCounts = cardealer.channelCount;
                });
            }
        })
    };

    //  组织机构查询统计
    $scope.orgCount = function (orgInfoId) {
        echartService.getOrgCharts(orgInfoId,$scope.searchDate,"A001",function (demand) {
            $scope.demand = demand;
        });
        echartService.getOrgCharts(orgInfoId,$scope.searchDate,"A002",function (order) {
            $scope.order = order;
        });
        echartService.getOrgCharts(orgInfoId,$scope.searchDate,"A004",function (payment) {
            $scope.payment = payment;
        });
        echartService.getOrgCharts(orgInfoId,$scope.searchDate,"A005",function (carregistry) {
            $scope.carregistry = carregistry;
        });
        echartService.getOrgCharts(orgInfoId,$scope.searchDate,"A008",function (dmvpledge) {
            $scope.dmvpledge = dmvpledge;
        });
        echartService.getOrgCharts(orgInfoId,$scope.searchDate,"A023",function (cartransfer) {
            $scope.cartransfer = cartransfer;
        });
        echartService.getOrgCarDealerCount(orgInfoId,$scope.searchDate,function (cardealer) {
            $scope.cardealerCounts = cardealer.channelCount;
        });
    };

    //  登陆用户为渠道经理时，选择【个人业务统计】、【团队业务统计】，默认为【团队业务统计】
    $scope.changeChannelCount = function (num) {
        $scope.clearEcharts();
        if (num == 2){//【团队业务统计】
            var orgInfoId = $scope.orginfos[$scope.orginfos.length - 1].id;
            $scope.orgCount(orgInfoId);
        } else if (num == 3){//【个人业务统计】
            $scope.stageCount();
        }
    };

    //更换时间
    $scope.changeDate = function(){
        $scope.clearEcharts();
        $scope.search();
    };

    $scope.changeOrg = function () {
        $scope.clearEcharts();
        $scope.orgCount($scope.selectedOrg.id);
    };

    $scope.resetDate=function () {
        $scope.searchDate = $filter('momentDate')(new Date(),'YYYY-MM');
    };

    $scope.search = function () {
        if(!$scope.searchDate) {
            $scope.resetDate();
        }
        if ($scope.mode == 1){
            $scope.stageCount();
        } else {
            $scope.changeChannelCount($scope.mode);
        }
    };

    $scope.clearEcharts = function () {

        $scope.demandArrNum = [];
        $scope.orderArrNum =[];
        $scope.paymentArrNum = [];
        $scope.carArrNum = [];
        $scope.cardealerCounts = 0;
        $scope.amountArrNum = [];
        $scope.chargeArrNum = [];
        $scope.servicefeeArrNum = [];

    };

    $scope.init();

});
