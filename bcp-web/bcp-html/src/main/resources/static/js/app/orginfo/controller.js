/**
 * Created by LB on 2016-10-24.
 */
app.controller('orginfoController',['$scope','$modal','$filter','orginfoService','loginUserService', 'provinceService', '$state','$stateParams','FileUploader','$q','toaster','localStorageService',function($scope,$modal,$filter,orginfoService,loginUserService,provinceService,$state,$stateParams,FileUploader,$q,toaster,localStorageService){

    var treeData,treeCtrl;

    $scope.title = '组织机构管理';
    $scope.tableTitle = "员工列表";

    $scope.orginfoTree = treeCtrl={};
    $scope.selectLabel = {};

    //返回的员工列表数据
    $scope.employees = [];
    $scope.employee = {};

    $scope.notChange = false;
    
    
    //查询实体
    $scope.employeeQuery = {};

    /**
     * 所有的组织机构
     * @type {{}}
     */
    $scope.orginfos = treeData =  [];
    $scope.orginfo = {};
    $scope.selectedOrginfo = null;
    $scope.provinces = [];

    $scope.allOrgs = {};
    $scope.allOrg = [];

    //  用于  下拉框的组织架构
    $scope.orgInfos = [];
    $scope.orgInfo = {};
    $scope.userName = "";

    $scope.currentPage = 1;

    $scope.employeeFilter = {};
   

    //员工验证唯一
    $scope.checkUniqueEmployee = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.employee;
            orginfoService.checkUniqueEmployee(entity,propname,propval,function(data){
                if(data=='false'){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    //部门验证唯一
    $scope.checkUniqueOrgInfo = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.orginfo;
            orginfoService.checkUniqueOrgInfo(entity,propname,propval,function(data){
                if(data=='false'){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /**
     * 选定组织机构
     * @param branch
     */
    $scope.selectOrginfo = function(branch) {
        branch.selected = true;
        if (branch.children.length == 0) {
            $scope.getChildData(branch.id);
        }

        $scope.selectLabel = branch.label;
        if ($scope.selectedOrginfo)  $scope.selectedOrginfo.selected = false;
        if (branch.data) {
            $scope.selectedOrginfo = branch.data;
            $scope.getEmployees();
        } else {
            $scope.selectedOrginfo = null;
            $scope.getEmployees();
        }
    };
    
    /**
     * 获取左边选中的员工列表
     */
    $scope.getEmployees = function (){
        $scope.changeEmployeePage();
    };

    /*组织机构中员工分页功能*/
    $scope.changeEmployeePage = function (){
        var orgInfoId = "0";

        if($scope.selectedOrginfo)
            orgInfoId = $scope.selectedOrginfo.id;

        orginfoService.getEmployees(orgInfoId, $scope.currentPage - 1,function(data){
            $scope.employees = data.result;
            $scope.totalEmployeeItems = data.totalCount;//数据总条数
            $scope.pageEmployeeSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;
            $scope.totalEmployeePages=data.totalPages;//总页数
        });
    };

    /**
     * 组织机构编辑
     * @param orginfo
     */
    $scope.editOrginfo = function (orginfo){
       /* if($scope.selectedOrginfo && $scope.selectedOrginfo.dataStatus == 9){
            alert("该部门已作废，不可进行添加或编辑！");
        }else {*/

        //1是个修改，0是添加
        if(orginfo == 1){
            $scope.modalTitle="编辑组织机构";
            $scope.orginfo = $scope.selectedOrginfo;
            //$scope.orginfo = angular.copy($scope.selectedOrginfo);
        }else{
            $scope.modalTitle="添加组织机构";
            $scope.orginfo = {dataStatus: 1};
            if ($scope.selectedOrginfo)
                $scope.orginfo.parentId = $scope.selectedOrginfo.id;
        }

        /*模态框*/
        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "orginfoForm.html",
            controller: function ($scope) {

                /*关闭模态框*/
                $scope.cancelOrginfo = function () {
                    modalInstance.close();
                };

                /*保存信息*/
                $scope.saveOrginfo = function () {
                    if($scope.orginfo.parentId == "" || $scope.orginfo.parentId == null){
                        $scope.orginfo.parentId = "0";
                    }

                    orginfoService.saveOrg($scope.orginfo, function (result) {
                        var treenode ={label:result.name,id:result.id,uid:result.id,data:result};
                        var selectedBranch =  treeCtrl.get_selected_branch();

                        if ($scope.orginfo.id == "" || $scope.orginfo.id == null) {
                            treeCtrl.add_branch(selectedBranch ,treenode);
                        } else {
                            selectedBranch.label = result.name;
                        }
                        //$scope.orginfo = result;
                        modalInstance.close();
                        toaster.pop('success', '组织机构', '保存成功！');
                    });
                }
            }
        });
        // };
    };

    /**
     * 初始化
     */
    $scope.init = function (){
        //$scope.currentEmployeePage =1;
        $scope.getRootNode();
        //加载地区数据
        $scope.loadRegions();
    };

    /**
     * 加载地区数据
     */
    $scope.loadRegions = function () {
        provinceService.lookup(function (data) {
            $scope.provinces = data;
        });
    };

    /**
     * 构造根节点
     */
    $scope.getRootNode = function() {
        treeData.push({label:'富择分期',id:'0', uid:null, data:null});
        $scope.orginfos = treeData;
    };

    /**
     * 取得下级节点
     * @param parentData
     */
    $scope.getChildData = function(parentData){
        orginfoService.getOrgs(parentData,function(data){
            var selectBranch =  treeCtrl.get_selected_branch();
            data.forEach(function(item){
                var treenode ={label:item.name,id:item.id,uid:item.id,data:item};
                treeCtrl.add_branch(selectBranch,treenode);
            });
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();


    //使用指令查询
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
        $scope.employeeFilter = {};
        $scope.changeEmployeePage();
    };
    /**
     * 查询
     * @param user
     */
    $scope.queryBill = function (employee) {
        if($scope.selectedOrginfo != null && $scope.selectedOrginfo.id){
            employee.orgInfoId = $scope.employee.orgInfoId;
        }else {
            employee.orgInfoId = null;
        }
        $scope.employeeFilter = employee;
        $scope.currentPage = 1;
        orginfoService.searchEmployees(employee, $scope.currentPage-1,function(data){
            $scope.employees = data.result;
            $scope.totalEmployeeItems = data.totalCount;//数据总条数
            $scope.pageEmployeeSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalEmployeePages=data.totalPages;//总页数
        });
    };

    /***
     * 添加或修改员工信息，打开模态框
     */
    $scope.editEmployee = function (employee){

                //初始化员工数据
                var orginfo = null;
                if (employee.id != "" && employee.id != null) {
                    $scope.modalTitle="编辑员工信息";
                    $scope.employee = angular.copy(employee);
                    if ( employee.loginUserId != null && employee.loginUserId != '' ){
                        $scope.userName=$scope.employee.cell;
                        $scope.employee.selected = true;
                    }else{
                        $scope.userName = "";
                    }

                    orginfoService.getOrg(employee.orgInfoId,function (dataa) { //根据id查找部门
                        orginfo = dataa;
                        $scope.employee.isLeader = (orginfo.leaderId != null && orginfo.leaderId == employee.id) ? true : false;
                    })

                }  else {
                    if($scope.selectedOrginfo) {
                        orginfo = $scope.selectedOrginfo;
                        $scope.modalTitle="添加员工信息";
                        $scope.employee={dataStatus: 1, isLeader: false, orgInfoId: orginfo.id};
                        $scope.userName = "";
                    }else{
                        alert("请选择部门！");
                        return;
                    }
                }
                /*模态框*/
                var modalInstance = $modal.open({
                    scope: $scope,
                    animation: true,
                    size: 'lg',
                    templateUrl: "employeeForm.html",
                    controller: function ($scope) {

                        $scope.availableOrgs = [];
                        orginfoService.lookup(function(data){
                            $scope.availableOrgs = data;
                        });

                        /*关闭模态框*/
                        $scope.cancel = function () {
                            modalInstance.close();
                        };

                        /**
                         * 上传员工头像
                         */
                        $scope.fileUploaderAvatar = new FileUploader({
                            url: '/json/file/upload',
                            headers: {'User-Token':localStorageService.get('User-Token'), 'Client-Type':'PC'},
                        });
                        $scope.fileUploaderAvatar.onSuccessItem = function(fileItem, response, status, headers) {
                            $scope.employee.avatarFileId = response.d.id;
                        };
                        $scope.uploadAvatar = function(){
                            $scope.fileUploaderAvatar.uploadAll();
                        };

                        /**      添加员工信息时，点击“使用手机号登录”时，判断手机号码是否存在，存在就将手机号码作为登录账户，否则提示“请输入手机号码”       */
                        $scope.phoneAsAccount = function () {

                                //      选中
                                if ($scope.employee.selected){
                                    var cell = $scope.employee.cell;
                                    //      判断手机号码是否为空
                                    if  (cell == null || cell == "" || undefined == cell){
                                        alert("请检查下您的号码！");
                                        $scope.employee.selected = false;
                                    }else{
                                        $scope.userName = $scope.employee.cell;
                                    }
                                }else{
                                    $scope.userName = "";
                                    $scope.employee.loginUserId = "";
                                }
                        };
                        
                        /*保存员工信息*/
                        $scope.save = function () {
                            if($scope.employee.isLeader && $scope.employee.orgInfoId != $scope.selectedOrginfo.id){
                                    alert("部门领导改换部门,请先取消部门领导!");
                            }else{
                                if ($scope.employee.selected) {
                                    $scope.employee.loginUser = {
                                        username:$scope.userName,
                                        activitiUserId:$scope.employee.cell,
                                        dataStatus:1,
                                        deviceNum:1}
                                }

                                orginfoService.saveEmployee($scope.employee,$scope.employee.isLeader, function (data) {

                                    //设置部门领导
                                    if ($scope.employee.isLeader) {
                                        orginfo.leaderId = data.id;
                                        $scope.selectedOrginfo.leaderId = data.id;
                                    }else {
                                        if (orginfo.leaderId != null){
                                            if (orginfo.leaderId == $scope.employee.id){
                                                orginfo.leaderId = null;
                                                $scope.selectedOrginfo.leaderId = null;
                                            }
                                        }else {
                                            orginfo.leaderId = null;
                                            $scope.selectedOrginfo.leaderId = null;
                                        }
                                    }

                                    if ($scope.employee.orgInfoId != orginfo.id) {
                                        //TODO: 如果部门改变,把数据移出列表
                                            if(orginfo != null && orginfo.leaderId == $scope.employee.id){
                                                orginfo.leaderId = null;
                                                orginfoService.saveOrg(orginfo,function (data) {
                                                })
                                            }
                                        var index = $scope.employees.indexOf(employee);
                                        $scope.employees.splice(index, 1);
                                    }

                                    //如果添加的话，要在列表回显数据
                                    if (!$scope.employee.id) {
                                        $scope.employees.push(data);
                                    } else {
                                        var index = $scope.employees.indexOf(employee);
                                        $scope.employees[index] = data;
                                    }
                                    $scope.employee = data;
                                    /*关闭模态框*/
                                    modalInstance.close();
                                    toaster.pop('success', '员工信息', '保存成功！');
                                })
                            }
                        }
                    }
                });
    };

    /**
     * 删除组织机构
     */
    /*$scope.deleteOrginfo = function (){
        if(!$scope.employees.length){
            if ($scope.selectedOrginfo) {
                if ($scope.selectedOrginfo.dataStatus != 9 ){
                    if (confirm("确定要作废  " + $scope.selectedOrginfo.name + "  ?")) {
                        orginfoService.deleteOrg($scope.selectedOrginfo, function (data) {
                            var selectBranch =  treeCtrl.get_selected_branch();
                            // selectBranch.data.dataStatus = 9;
                            toaster.pop('success','组织机构','作废成功');
                        })
                    }
                }else if($scope.selectedOrginfo.dataStatus == 9){
                    if (confirm("确定要删除  " + $scope.selectedOrginfo.name + "  ?")) {
                        orginfoService.deleteOrg($scope.selectedOrginfo, function (data) {
                            treeData.forEach(function(item){
                                $scope.deleteNode(item, $scope.selectedOrginfo);
                            });
                            $scope.selectedOrginfo = null;
                            treeCtrl.select_next_branch();
                            toaster.pop('success','组织机构','删除成功');
                        })
                    }
                }
            }
        }else{
            alert("请先删除对应的员工信息");
        }
    };

    $scope.deleteNode = function(treenode, delnode){
        for(var i=0; i < treenode.children.length; i++){
            if(treenode.children[i].id== delnode.id){
                treenode.children.splice(i,1);
                return;
            }else{
                $scope.deleteNode(treenode.children[i],delnode);
            }
        }
    };*/


    /*删除员工信息*/
    $scope.deleteEmployee = function (employee,index){

            var msg = "确定要作废数据  " + employee.username + "  ?";
            if (employee.dataStatus == 9) {
                msg = "确定要删除数据  " + employee.username + "  ?";
            }
            if (confirm(msg)) {
                orginfoService.deleteEmployee(employee, function (data) {
                    if (employee.dataStatus == 9) {
                        $scope.employees.splice(index, 1);
                    } else {
                        $scope.employees[index] = data;
                    }
                    $scope.changeEmployeePage();
                })
            }
    };

    //恢复员工信息
    $scope.renew = function (employee,index) {
        if(confirm("确定要恢复该条数据?")){
            employee.dataStatus = 1;
            if(employee.id == $scope.selectedOrginfo.leaderId){
                $scope.employee.isLeader = true;
            }else {
                $scope.employee.isLeader = false;
            }
            orginfoService.saveEmployee(employee,$scope.employee.isLeader,function (data) {

            })
        }
    };

    /**
     * 添加至企业微信
     */
    $scope.syncEmployee = function (employee,index) {
        var eid = employee.id;
        orginfoService.syncEmployee(eid,function (data) {
            if(data){
                alert(data);
            }
        })
    }

}]);
