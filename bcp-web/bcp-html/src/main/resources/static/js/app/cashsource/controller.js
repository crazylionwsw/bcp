/**
 * Created by LB on 2016-10-24.
 */

app.controller('cashsourceController', function ($scope, cashsourceService, guaranteewayService, repaymentwayService, imagetypeService, sysparamService,$modal,$q,toaster) {

    var treeData,treeCtrl;

    $scope.title = '资金来源管理';
    //$scope.tableTitle = "资金利率列表";

    $scope.cashsourceTree = treeCtrl ={} ;

    $scope.selectLabel = '';
    $scope.selectedCashsource = null;

    //      选中的分支
    $scope.selectBranch = null;

    $scope.showSource = true;
    $scope.checked1 = false;
    $scope.checked2 = false;

    //开始日期大于结束日期
    $scope.checkEndDate = function(value) {
        if(value){
            if (moment(value).isAfter($scope.sourcerate.startDate)) {
                return true;
            } else {
                return false;
            }
        }else {
            return true;
        }

    };
    /**
     * 所有的资金来源方
     * @type {{}}
     */
    $scope.cashsources = treeData =  [];
    //合作支行
    $scope.cashsource = {};
    //资金利率
    $scope.sourcerates = [];
    $scope.sourcerate = {};

    //还款方式
    $scope.repaymentways = [];

    //期率类型
    $scope.rateTypes = [];
    $scope.rateType = {};
    /**
     *资金来源类型
     */
    $scope.sourceTypes = {};

    /**
     *取得银行角色
     */
    $scope.bankEmployeeRoles = {};

    $scope.cashType = null;
    

    /**
     * 选定资金来源
     * @param branch
     */
    $scope.selectSource = function (branch) {

        if(branch.children.length == 0){
            $scope.getChildData(branch.id);
        }

        $scope.selectBranch = branch;
        $scope.parantBranch = $scope.cashsourceTree.get_parent_branch(branch);
        $scope.selectLabel = branch.label;
        branch.selected = true;

        if ($scope.selectedCashsource)  $scope.selectedCashsource.selected = false;

        if(branch.data.code && branch.data.code.indexOf("YH-GSYH-BJ-") >= 0){
            //支行
            $scope.showSource = false;
            $scope.checked2 = true;
        }else{
            $scope.showSource = true;
            $scope.checked1 = true;
        }

        $scope.sourcerates = [];
        if (branch.data) {
            $scope.getSourceRates(branch.data);
            $scope.getEmployees(branch.data);
            $scope.selectedCashsource = branch.data;
        } else {
            $scope.selectedCashsource = null;
        }
    };

    /**
     * 获取利率列表
     */
    $scope.getSourceRates = function (cashsource){
        cashsourceService.getRatesByCaseSource(cashsource, function (data) {
            $scope.sourcerates = data;
        });
    };


    /**
     * 获取员工列表
     */
    $scope.getEmployees = function (cashsource){
        cashsourceService.getEmployeesByCashSource(cashsource, function (data) {
            $scope.employees = data;
        });
    };
    

    $scope.init = function (){
        //从后台获取数据
        cashsourceService.getCashTypes(function(data){
            data.forEach(function(item){
                item.uid = item.id;
                treeData.push(item);
            });
            $scope.sourceTypes = data;
        });

        //取得还款方式数据
        repaymentwayService.lookup(function (data) {
            $scope.repaymentways = data;
        });
        //档案类型
        imagetypeService.lookup(function (data) {
            $scope.packageImageTypes = data;
        });

        //  获得银行协作员工角色
        sysparamService.getListByCode("BANK_EMPLOYEE_ROLE",function (data) {
            $scope.bankEmployeeRoles = data;
        })
    };

    /**
     * 显示下级对象
     */
    $scope.getChildData = function(parentData){
        cashsourceService.getChildren(parentData,function(data){
            var selectBranch =  treeCtrl.get_selected_branch();
            data.forEach(function(item){
                var classes = item.dataStatus == 9 ? ['text-danger'] : [];
                var treenode ={label:item.name, id:item.id, uid:item.id, data:item, classes:classes};
                if (item.shortName) {
                    treenode ={label:item.shortName, id:item.id, uid:item.id, data:item, classes:classes};
                }

                treeCtrl.add_branch(selectBranch,treenode);
            });
        });
    };

    $scope.checkUniqueSourceRate = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.sourcerate;
            cashsourceService.checkUniqueSourceRate(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    $scope.checkUniqueCashSource = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.cashsource;
            cashsourceService.checkUniqueCashSource(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    $scope.checkUniqueCashSourceEmployee = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.employee;
            cashsourceService.checkUniqueCashSourceEmployee(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();

    /**
     * 资金利率编辑
     * @param orginfo
     */
    $scope.editSourceRate = function (sourcerate,index) {

        if($scope.selectedCashsource){

            /*  判断id进行回显 */
            if(sourcerate.id != "" && sourcerate.id != null){
                $scope.modalTitle = "编辑资金利率";
                /*使用条件*/
                var limits = sourcerate.limits;
                sourcerate.limitsString = [];
                for(var i in limits){
                    sourcerate.limitsString.push({text: limits[i]});
                }

                $scope.sourcerate = angular.copy(sourcerate);
                $scope.sourcerate.cashSource = $scope.selectedCashsource;

                /*利率明细*/
                $scope.rateTypes = [];
                angular.forEach(sourcerate.rateTypes,function(data){
                    data.ratio = data.ratio*100;
                    $scope.rateTypes.push(data);
                });
            } else {
                $scope.modalTitle = "添加资金利率";
                $scope.rateTypes = [];
                $scope.sourcerate={dataStatus: 1, cashSource: $scope.selectedCashsource, cashSourceId: $scope.selectedCashsource.id};

                // if($scope.parantBranch){
                //     if ($scope.parantBranch.parent_uid != '0'){
                //         alert("对不起，该机构不可以添加资金利率！");
                //         return;
                //     }
                //     if ($scope.parantBranch.uid == '0'){
                //         alert("对不起，该机构不可以添加资金利率！");
                //         return;
                //     }
                // }

            }
            /*模态框*/
            var modalInstance;
            modalInstance = $modal.open({
                scope: $scope,
                animation: true,
                size: 'lg',
                templateUrl: "sourcerateForm.html",
                controller: function ($scope,$modalInstance) {

                    $scope.addRate = function () {
                        $scope.rateTypes.push({
                            id: $scope.rateTypes.length+1,
                            months: 0,
                            ratio: 0.0,
                            isNew: true
                        });
                    };
                    $scope.removeRate = function (index) {
                        $scope.rateTypes.splice(index,1);
                    };


                    /*关闭模态框*/
                    $scope.cancelMo = function () {
                        $scope.rateTypes = [];
                        angular.forEach(sourcerate.rateTypes,function(data){
                            data.ratio = data.ratio/100;
                            $scope.rateTypes.push(data);
                        });
                        modalInstance.close();
                    };


                    /*保存资金利率信息*/
                    $scope.save = function () {
                        $scope.sourcerate.rateTypes = $scope.rateTypes;
                        var sourcerate = $scope.sourcerate;
                        sourcerate.limits = [];
                        if(sourcerate.limitsString){
                            $.each(sourcerate.limitsString, function(k){
                                sourcerate.limits[k] = sourcerate.limitsString[k].text;
                            });
                        };

                        /*遍历利率*/
                        angular.forEach(sourcerate.rateTypes,function(data,index){
                            sourcerate.rateTypes[index].ratio = data.ratio/100;
                        });

                        cashsourceService.saveSourceRate(sourcerate,function (data){

                            if(sourcerate.id =="" || sourcerate.id == null){
                                $scope.sourcerates.push(data);
                            }else {
                                //var index = $scope.sourcerates.indexOf(sourcerate);
                                $scope.sourcerates[index] = data;
                            }
                            $scope.sourcerate = data;
                            modalInstance.close();
                            toaster.pop('success','资金利率','保存成功！');
                        });
                    }
                }
            });
        }else{
            alert("请选择资金机构！");
        }
    };

    /* 删除资金利率信息*/
    $scope.deleteSourceRate = function (sourcerate,index) {

        var msg = "确定要作废数据  ?";
        if (sourcerate.dataStatus == 9) {
            msg = "确定要删除数据  ?";
        }
        if (confirm(msg)) {
            cashsourceService.deleteRate(sourcerate, function (data) {
                if (sourcerate.dataStatus == 9) {
                    $scope.sourcerates.splice(index, 1);
                } else {
                    $scope.sourcerates[index] = data;
                }
            });
        }
    };

    //恢复资金利率
    $scope.renewSourcerate = function (sourcerate,index) {
        if(confirm("确定要恢复该条数据?")){
            sourcerate.dataStatus = 1;
            cashsourceService.saveSourceRate(sourcerate,function (data) {
                if(sourcerate.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };


    /**
     * 编辑协作用户
     * @param employee
     */
    $scope.editEmployee = function (employee) {
        if($scope.selectedCashsource){
            /*  判断id进行回显 */
            if(employee.id != "" && employee.id != null){
                $scope.modalTitle = "编辑协作用户";
                $scope.employee = angular.copy(employee);
                // employee.emailTags = [];
                // for(var i in email){
                //     employee.emailTags.push({text: email[i]});
                // }
                // var cell = employee.cell;
                // employee.cellTags = [];
                // for(var i in cell){
                //     employee.cellTags.push({text: cell[i]});
                // }
                // var roles = employee.roles;
                // employee.rolesTags = [];
                // for(var i in roles){
                //     employee.rolesTags.push({text: roles[i]});
                // }
                // $scope.employee = employee;
                $scope.employee.cashSourceId = $scope.selectedCashsource.id;
            } else {
                $scope.modalTitle = "添加协作用户";
                $scope.employee={dataStatus: 1, cashSourceId: $scope.selectedCashsource.id};
                // if($scope.parantBranch){
                //     if ($scope.parantBranch.uid == '0'){
                //         alert("对不起，该机构不可以添加协作用户！");
                //         return;
                //     }
                //     if ($scope.parantBranch.parent_uid == '0'){
                //         alert("对不起，该机构不可以添加协作用户！");
                //         return;
                //     }
                // }
            }
            /*模态框*/
            var modalInstance;
            modalInstance = $modal.open({
                scope: $scope,
                animation: true,
                templateUrl: "employeeForm.html",
                controller: function ($scope,$modalInstance) {

                    /*关闭模态框*/
                    $scope.cancel = function () {
                        modalInstance.close();
                    };

                    /*保存用户信息*/
                    $scope.save = function () {
                        //var employee = $scope.employee;
                        cashsourceService.saveEmployee($scope.employee, function (data){
                            if (!$scope.employee.id) {
                                $scope.employees.push(data);
                            } else {
                                var index = $scope.employees.indexOf(employee);
                                $scope.employees[index] = data;
                            }
                            $scope.employee = data;
                           /*关闭模态框*/
                            modalInstance.close();
                            toaster.pop('success','协作用户','保存成功！');
                        });
                    }
                }
            });
        }else{
            alert("请选择资金机构！");
        }
    };

    /* 废弃协作用户*/
    $scope.deleteEmployee = function (employee,index) {

        var msg = "确定要作废数据  ?";
        if (employee.dataStatus == 9) {
            msg = "确定要删除数据  ?";
        }
        if (confirm(msg)) {
            cashsourceService.deleteEmployee(employee, function (data) {
                if (employee.dataStatus == 9) {
                    $scope.employees.splice(index, 1);
                    toaster.pop('success','协作用户','删除成功！');
                } else {
                    $scope.employees[index] = data;
                    toaster.pop('success','协作用户','作废成功！');
                }
            });
        }
    };

    //协作用户恢复
    $scope.renew = function (employee,index) {
        if(confirm("确定要恢复该条数据?")){
            employee.dataStatus = 1;
            cashsourceService.saveEmployee(employee,function (data) {
                if(employee.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    /**
     * 删除资金渠道类型
     */
    $scope.removeCashSource = function (){
        var selectSource = $scope.selectedCashsource;
        if (selectSource.dataStatus == 1) {
            if (confirm("确定要作废数据  " + selectSource.name + "  ?")) {
                cashsourceService.deleteSource(selectSource, function (data) {
                    var selectBranch =  treeCtrl.get_selected_branch();
                    selectBranch.data.dataStatus = 9;
                        var classes = selectBranch.data.dataStatus == 9 ? ['text-danger'] : [];
                        var treenode ={label:selectBranch.name, id:selectBranch.id, uid:selectBranch.id, data:selectBranch, classes:classes};
                        if (selectBranch.shortName) {
                            treenode ={label:selectBranch.shortName, id:selectBranch.id, uid:selectBranch.id, data:selectBranch, classes:classes};
                        }
                        treeCtrl.get_selected_branch(selectBranch,treenode);

                        toaster.pop('success','资金来源','作废成功');
                });
            }
        }else if(selectSource.dataStatus == 9){
            /*if (confirm("确定要删除数据  " + selectSource.name + "  ?")) {
                cashsourceService.deleteSource(selectSource, function (data) {
                    treeData.forEach(function(item){
                        $scope.deleteNode(item,selectSource);
                    });
                    $scope.selectedCashsource = null;
                    toaster.pop('success','资金来源','删除成功');
                })
            }*/
            alert("资金来源已作废，不能再删除!")
            treeCtrl.select_next_branch();
        }
    };

    $scope.deleteNode = function(treenode, delnode){
        for(var i=0; i<treenode.children.length; i++){
            if(treenode.children[i].id== delnode.id){
                treenode.children.splice(i,1);
                return;
            }else{
                $scope.deleteNode(treenode.children[i],delnode);
            }
        }
    };

    /**
     * 资金来源编辑
     * @param orginfo
     */
    $scope.editCashSource = function (cashsource) {
        if (cashsource == 0) {
            $scope.modalTitle = "添加资金来源";
            $scope.cashsource = {dataStatus: 1};
            if ($scope.selectBranch){
                if ($scope.selectBranch.uid){
                    $scope.cashsource.sourceType = $scope.selectBranch.uid;
                }
            }
            if ($scope.parantBranch){
                $scope.cashsource.sourceType = $scope.parantBranch.id;
                if ($scope.parantBranch.parent_uid){
                    $scope.cashsource.sourceType = $scope.parantBranch.parent_uid;
                    if ($scope.selectBranch.children == '0'){
                        alert("对不起，该来源不能再次添加资金来源！");
                        return;
                    }
                }
            }
            if ($scope.selectedCashsource)
                $scope.cashsource.parentId = $scope.selectedCashsource.id;

        } else {
            $scope.modalTitle = "编辑资金来源";
            $scope.cashsource = angular.copy($scope.selectedCashsource);
            if($scope.selectedCashsource){
                cashsourceService.getCashSource($scope.selectedCashsource.id,function (data) {
                    $scope.cashsource = data;
                    var marketingCode = $scope.cashsource.marketingCode;
                    $scope.cashsource.marketingCodeString = [];
                    for(var i in marketingCode){
                        $scope.cashsource.marketingCodeString.push({text: marketingCode[i]});
                    }
                })
            }

        }

        /*模态框*/
        modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "cashsourceForm.html",
            controller: function ($scope) {

                /*关闭模态框*/
                $scope.cancel = function () {
                    modalInstance.close();
                };

                /*保存信息*/
                $scope.save = function () {
                    var cashsource = $scope.cashsource;
                    cashsource.marketingCode = [];
                    if(cashsource.marketingCodeString){
                        $.each(cashsource.marketingCodeString, function(k){
                            cashsource.marketingCode[k] = cashsource.marketingCodeString[k].text;
                        });
                    }
                    cashsourceService.saveCashSource($scope.cashsource, function (result) {
                        var treenode ={label:result.shortName,id:result.id,uid:result.id,data:result};
                        var selectedBranch =  treeCtrl.get_selected_branch();
                        if($scope.cashsource.id == "" || $scope.cashsource.id == null){
                            treeCtrl.add_branch(selectedBranch,treenode);
                        }else {
                            $scope.selectBranch.label = result.shortName;
                        }
                        // if (selectedBranch) {
                        //     if (cashsource == 0) { //添加
                        //         treeCtrl.add_branch(selectedBranch ,treenode);
                        //     } else { //修改
                        //         selectedBranch.label = result.shortName;
                        //     }
                        // }
                        var marketingCode = result.marketingCode;
                        $scope.cashsource.marketingCodeString = [];
                        for(var i in marketingCode){
                            $scope.cashsource.marketingCodeString.push({text: marketingCode[i]});
                        }
                        $scope.cashsource = result;
                    });
                    modalInstance.close();
                    toaster.pop('success','资金来源','保存成功');
                }
            }
        });
    };
});
