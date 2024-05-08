/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('deviceController',['$scope','$modal','deviceService','toaster','$q',function ($scope,$modal,deviceService,toaster,$q){
    $scope.tableTitle = '设备管理';
    $scope.tableDesc = "设备管理列表";
    // $scope.title = '设备管理';
    // $scope.tableTitle = "设备管理列表";

    /*列表*/
    $scope.terminaldevices  = [];//返回的列表数据
    $scope.terminal = {};
    $scope.currentPage = 1;

    //终端设备信息
    $scope.deviceUsage = {};
    $scope.deviceUsages = [];

    //员工
    $scope.employee = {};
    $scope.employees = [];
    $scope.currentPage = 1;
    
    
    /**
     * 初始化加载列表和分页
     */
    $scope.init = function(){
        $scope.changePage();

    };

    $scope.changePage = function (){
        deviceService.getPageData($scope.currentPage-1,function(data){
            $scope.terminaldevices = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();
    
    $scope.delete = function (terminal,index){
            var msg = "确定要作废数据?";
            if (terminal.dataStatus == 9) {
                msg = "确定要删除数据?";
            }
            if (confirm(msg)) {
                deviceService.delete(terminal, function (data) {
                    if (terminal.dataStatus == 9) {
                        $scope.terminaldevices.splice(index, 1);
                    } else {
                        $scope.terminaldevices[index] = data;
                    }
                })
            }
    };
    
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.terminal;
            deviceService.checkUniqueTerminalDevice(entity,propname,propval,function(data){
                if(data == "false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    //保存
    $scope.edit = function (terminal){
        /*  判断id进行回显 */
        if(terminal.id != "" && terminal.id != null){
            $scope.modalTitle = "编辑设备";
            $scope.terminal = angular.copy(terminal);
        } else {
            $scope.modalTitle="添加设备";
            $scope.terminal = {dataStatus: 1};
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'modelForm.html',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 保存设备 */
                $scope.save = function (){
                    deviceService.saveDevice($scope.terminal,function (data){
                          if (!$scope.terminal.id) {
                            $scope.terminaldevices.push(data);
                        } else {
                            var index = $scope.terminaldevices.indexOf(terminal);
                            $scope.terminaldevices[index] = data;
                        }
                        $scope.terminal = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }

            }
        });
    };
    
    
    $scope.look = function (){
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'tpl/view/device/list.html',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                deviceService.getAllDeviceUsages(function (data){
                    $scope.deviceUsages = data;//返回的数据
                });

            }
        });
    };

    /**
     * 二维码
     */
    $scope.qrcode = function(terminal){
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'qrcode.html',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }
            }
        });
    };

    /**
     * 领取
     * @param terminal
     */
    $scope.receive = function (terminal){
        $scope.terminal = terminal;

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'receive.html',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                $scope.save = function (){
                    var usage = {usageType: 1, deviceId: $scope.terminal.id, employeeId: $scope.terminal.employeeId};
                    deviceService.saveDeviceUsage(usage,function (data){
                        $scope.deviceUsages.push(data);
                        $scope.init();
                    });
                    modalInstance.close();
                }
            }
        });
    };

    /**
     * 归还
     * @param terminal
     */
    $scope.return = function (terminal){
        $scope.terminal = terminal;

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'return.html',
            controller:function ($scope,$modalInstance){
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                $scope.save = function (){
                    var usage = {usageType: 0, deviceId: $scope.terminal.id, employeeId: $scope.terminal.employeeId};
                    deviceService.saveDeviceUsage(usage,function (data){
                        $scope.deviceUsages.push(data);
                        $scope.init();
                    });
                    modalInstance.close();
                }
            }
        });
    };

    /**
     * 查询
     */
    $scope.openQueryDialog = function (){
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:'tpl/view/device/query.html',
            controller:function ($scope,$http, $modalInstance){
                $scope.modalTitle = "查询";
                $scope.loginUserId = '';

                $scope.getLoginUsers = function(val) {
                    return $http.get('/json/users/'+val, {
                        params: {}
                    }).then(function(data){
                        var users = [];
                        angular.forEach(data.data.d, function(item){
                            users.push(item);
                        });
                        return users;
                    });
                };

                $scope.select = function($item, $model, $label, $event) {
                    $scope.loginUserId = $item.id;
                };

                /*清楚查询内容*/
                $scope.clearQuery = function() {
                    $scope.loginUserId = '';
                    $scope.changePage(1);
                    modalInstance.close();
                };

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                };

                /* 根据手机号查询设备名称 */
                $scope.queryTerminalBinds = function (){
                    var loginUserId = $scope.loginUserId;
                    var currentPage = $scope.currentPage;
                    deviceService.search(loginUserId,currentPage,function (data){
                        $scope.$parent.terminalbinds = data.content;
                        $scope.$parent.totalItems = data.totalElements;//数据总条数
                        $scope.$parent.pageSize = data.size;//分页单位
                        $scope.$parent.currentPage = data.number + 1;//当前页
                        $scope.$parent.totalPages=data.totalPages;//总页数
                    });

                    /* 操作完成自动关闭模态框 */
                    modalInstance.close();
                }
            }
        });
    };

    //恢复数据
    $scope.renew = function (terminal,index) {
        if(confirm("确定要恢复该条数据?")){
            terminal.dataStatus = 1;
            deviceService.saveDevice(terminal,function (data) {
                if(terminal.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };
    
    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.terminal;
            deviceService.checkUniqueTerminalDevice(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
}]);


