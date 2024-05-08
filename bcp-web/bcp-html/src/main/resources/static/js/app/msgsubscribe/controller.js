/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('subScribeController',function ($scope, subScribeService,subsribesourceService,businesseventtypeService,customerService,orginfoService,cardealerService,cashsourceService,sysroleService,$modal, $q){
    var treeCtrl;
    $scope.tableTitle = "消息订阅维护管理";
    $scope.modalTitle = "消息订阅维护操作";

    $scope.msgsubscribes  = [];//返回列表数据
    $scope.msgsubscribe = {};


    $scope.search = {businessType:null,userChannel:null};
    $scope.searchFilter = {businessType:null,userChannel:null};

    $scope.sendTypes = [];
    $scope.descTypes = [];

    $scope.currentPage =  1;

    $scope.users = [];

    $scope.groupNames = [];
    //客户
    $scope.customer = [];
    //组织员工
    $scope.employee = [];
    //经销商员工
    $scope.dealeremployee = [];
    //银行协作员工
    $scope.cashsourceEmployee = [];
    //角色
    $scope.roles = [];

    //订阅ren
    $scope.submans = [];
    $scope.roleTypes = [];

    $scope.roleShow = false;

    $scope.msgsubscribe.subScribeSourceIds =[];

    //列表
    $scope.init = function (){
        $scope.changePage();

        subScribeService.getSendType(function (data) {
            $scope.sendTypes = data;
        });

        subScribeService.getDescType(function (data) {
            $scope.descTypes = data;
        });

        subsribesourceService.lookup(function (data) {
            $scope.users = data;
        });

        /*获取订阅组*/
        subsribesourceService.getGroupName(function (data) {
            $scope.groupNames = data;
        });

        customerService.getLookUpCustomers(function (customerdata) {
            var customers = [];
            angular.forEach(customerdata,function (item, index) {
                var customer = {id:item.id,username:item.name};
                customers[index] = customer;
            });
            $scope.customer = customers;
        });

        sysroleService.lookup(function (roldata) {
            var roles = [];
            angular.forEach(roldata,function (item, index) {
                var role = {id:item.id,name:item.name};
                roles[index] = role;
            });
            $scope.roles = roles;
        })


        orginfoService.getEmployeesLookup(function (employeedata) {
            var employees = [];
            angular.forEach(employeedata,function (item, index) {
                var employee = {id:item.id,username:item.username};
                employees[index] = employee;
            });
            $scope.employee = employees;
        });

        cardealerService.getEmployeeCardealer(function (dealeremployeedata) {
            var dealeremployees = [];
            angular.forEach(dealeremployeedata,function (item, index) {
                var dealeremployee = {id:item.id,username:item.username};
                dealeremployees[index] = dealeremployee;
            });
            $scope.dealeremployee = dealeremployees;
        });

        cashsourceService.getEmployeeCashsource(function (cashsourceEmployeedata) {
            var cashsourceEmployees = [];
            angular.forEach(cashsourceEmployeedata,function (item, index) {
                var cashsourceEmployee = {id:item.id,username:item.username};
                cashsourceEmployees[index] = cashsourceEmployee;
            });
            $scope.cashsourceEmployee = cashsourceEmployees;
        })

    };


    //订阅分组
    $scope.scribeTypes = [
        {
            id:'so_customer',
            type:'客户'
        },
        {
            id:'bd_employee',
            type:'组织员工'
        },
        {
            id:'bd_dealeremployee',
            type:'经销商员工'
        },
        {
            id:'bd_cashsourceemployee',
            type:'银行协作用户'
        }
    ];

    $scope.changePage = function (pageIndex) {
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        if( $scope.searchFilter.businessType || $scope.searchFilter.userChannel){
            subScribeService.search($scope.searchFilter,$scope.currentPage-1,function (data){
                $scope.msgsubscribes = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages=data.totalPages;//总页数

            })
        } else {
            subScribeService.getDataByPage($scope.currentPage-1,function (data){
                $scope.msgsubscribes = data.result;//返回的数据
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages = data.totalPages; //总页数

            });
        }

    }

    //根据订阅类型赋值订阅人
    $scope.selectType = function (sub) {
        $scope.msgsubscribe.subScribeSourceIds = [];
        // $scope.roleTypes = [];
        if(sub.id == 'so_customer'){
            $scope.submans = $scope.customer;
        }else if(sub.id == 'bd_employee'){
            $scope.submans = $scope.employee;
            $scope.roleShow = true;
            $scope.roleTypes = $scope.roles;
        }else if(sub.id == 'bd_dealeremployee'){
            $scope.submans = $scope.dealeremployee;
        }else if(sub.id == 'bd_cashsourceemployee'){
            $scope.submans = $scope.cashsourceEmployee;
        }
    };

    $scope.selectRole = function (role) {
        //$scope.msgsubscribe.subScribeSourceIds = [];
        orginfoService.getEmployeeByRoleId(role.id,function (emdata) {
            angular.forEach(emdata,function (em) {
                $scope.msgsubscribe.subScribeSourceIds.push(em.id);
            })
            
        });
        $scope.submans = $scope.employee;
    };

    /**
     * 执行初始化
     */
    $scope.init();

    //添加或修改
    $scope.edit = function (msgsubscribe){
        $scope.roleShow = false;
        /*  判断id进行回显 */
        if(msgsubscribe.id != "" && msgsubscribe.id != null){
            $scope.modalTitle="编辑订阅维护模版";
            $scope.msgsubscribe = angular.copy(msgsubscribe);
            if(msgsubscribe.scribeType == 'so_customer'){
                $scope.submans = $scope.customer;
            }else if(msgsubscribe.scribeType == 'bd_employee'){
                $scope.submans = $scope.employee;
                $scope.roleShow = false;
                $scope.roleTypes = $scope.roles;
            }else if(msgsubscribe.scribeType == 'bd_dealeremployee'){
                $scope.submans = $scope.dealeremployee;
            }else if(msgsubscribe.scribeType == 'bd_cashsourceemployee'){
                $scope.submans = $scope.cashsourceEmployee;
            }else if(msgsubscribe.scribeType == 'au_sysrole'){
                $scope.submans = $scope.employee;
            }

        } else {
            $scope.modalTitle="添加订阅维护模版";
            $scope.msgsubscribe = {dataStatus: 1};
            //$scope.visible = false;
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'subForm.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance,$q){

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }

                /* 保存 */
                $scope.save = function (){
                    subScribeService.save($scope.msgsubscribe,function (data){
                        if (!$scope.msgsubscribe.id) {
                            $scope.msgsubscribes.push(data);
                        } else {
                            var index = $scope.msgsubscribes.indexOf(msgsubscribe);
                            $scope.msgsubscribes[index] = data;
                        }
                        $scope.msgsubscribe = data;
                        /* 操作完成自动关闭模态框 */
                        modalInstance.close();
                    });
                }
            }
        });
    }

    /* 删除 */
    $scope.delete = function (msgsubscribe,index) {
        var msg = "确定要作废数据?";
        if (msgsubscribe.dataStatus == 9) {
            msg = "确定要删除数据?";
        }
        if (confirm(msg)) {
            var id = msgsubscribe.id;
            subScribeService.delete(id, function (data) {
                //$scope.init();
                if (msgsubscribe.dataStatus == 9) {
                    $scope.msgsubscribes.splice(index, 1);
                } else {
                    $scope.msgsubscribes[index] = data;
                }
            });
        }
    };

    //恢复数据
    $scope.renew = function (msgsubscribe,index) {
        if(confirm("确定要恢复该条数据?")){
            msgsubscribe.dataStatus = 1;
            subScribeService.save(msgsubscribe,function (data) {
                if(msgsubscribe.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    /**
     * 查询
     */
    $scope.search = function() {
        $scope.searchFilter = {};
        $scope.changePage(1);
    };

    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:"query.html",
            controller:function($scope,$modalInstance){
                //we do nothing now!
            }
        })
    };

    /**
     * 清空查询
     */
    $scope.clearQuery = function() {
        $scope.searchFilter = {businessType:null,userChannel:null};
        $scope.changePage(1);
    };

    /**
     * 查询
     */
    $scope.queryBill = function (search) {
        $scope.searchFilter = search;
        $scope.changePage(1);
    }

});