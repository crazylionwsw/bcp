/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('noticeController', function ($scope, noticeService, loginUserService, sysroleService, msgService, orginfoService, $modal, $q, $filter) {

    $scope.tableTitle = "通知管理";
    $scope.tableDesc = "通知管理";

    $scope.notices = [];//返回列表数据
    $scope.notice = {}; //对象

    $scope.users = [];

    $scope.sendTypes = [];

    $scope.currentPage = 1;


    //列表
    $scope.init = function () {
        $scope.changePage();

        loginUserService.lookUp(function (data) {
            $scope.users = data;
        });

        sysroleService.lookup(function (data) {
            $scope.sysRoles = data;
        });

        msgService.getSendType(function (data) {
            $scope.sendTypes = data;
        });

        orginfoService.lookup(function (data) {
            $scope.orgInfos = data;
        });

        orginfoService.getEmployeesLookup(function (data) {
            $scope.employees = data;
        })
    };

    //类型
    $scope.typeNames = [
        {
            code: 'type_1',
            typeName: '通知'
        },
        {
            code: 'type_2',
            typeName: '公告'
        }
    ];

    /**
     * 分页功能
     */
    $scope.changePage = function () {
        noticeService.getDataByPage($scope.currentPage - 1, function (data) {
            $scope.notices = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage + 1;//当前页
            $scope.totalPages = data.totalPages;
        });
    };

    /**
     * 执行初始化
     */
    $scope.init();

    /* 删除 */
    $scope.delete = function (notice, index) {
        var msg = "确定要作废此数据  ?";
        if (notice.dataStatus == 9) {
            msg = "确定要删除此数据  ?";
        }
        if (confirm(msg)) {
            var id = notice.id;
            noticeService.delete(id, function (data) {
                $scope.init();
            });
        }
    };

    //恢复数据
    $scope.renew = function (notice, index) {
        if (confirm("确定要恢复该条数据?")) {
            notice.dataStatus = 1;
            noticeService.save(notice, function (data) {
                if (notice.dataStatus != 1) {
                    alert("恢复数据出错!")
                }
            })
        }
    };

    $scope.edit = function (notice) {
        /*  判断是编辑还是添加 */
        if (notice.id != "" && notice.id != null) {
            $scope.modalTitle = "编辑通知管理数据";
            $scope.notice = angular.copy(notice);
        } else {
            $scope.modalTitle = "添加通知管理数据";
            $scope.notice = {dataStatus: 1};
        }


        var modalInstance = $modal.open({
            scope: $scope,
            animation: true,
            templateUrl: "noticeForm.html",
            backdrop:'static',
            controller: function ($scope) {

                /*关闭模态框*/
                $scope.cancel = function () {
                    modalInstance.close();
                }

                $scope.titleShow = true;

                //判断选择类型
                $scope.selectType = function (type) {
                    $scope.visible = false;
                    if (type.code == 'type_2') {
                        $scope.hiheDiv = false;
                        $scope.notice.fromGroup = "";
                        $scope.notice.groupId = "";
                        $scope.notice.orgId = "";
                        $scope.notice.loginUserNames = [];

                    } else {
                        $scope.hiheDiv = true;
                    }
                };

                $scope.selectSend = function (send) {
                    if (send.code == 'sms') {
                        $scope.titleShow = false;
                        $scope.notice.title = "";
                    } else {
                        $scope.titleShow = true;
                    }

                }

                if (notice.sendType == 'sms') {
                    $scope.titleShow = false;
                } else {
                    $scope.titleShow = true;
                }

                if (notice.type == 'type_2') {
                    $scope.hiheDiv = false;
                } else {
                    $scope.hiheDiv = true;
                }

                /*保存用户信息*/
                $scope.save = function () {

                    noticeService.save($scope.notice, function (data) {
                        if (!$scope.notice.id) {
                            $scope.notices.push(data);
                        } else {
                            var index = $scope.notices.indexOf(notice);
                            $scope.notices[index] = data;
                        }
                        $scope.notice = data;
                        modalInstance.close();
                    });
                }
            }
        });
    }

    //恢复数据
    $scope.renew = function (notice, index) {
        if (confirm("确定要恢复该条数据?")) {
            notice.dataStatus = 1;
            noticeService.save(notice, function (data) {
                if (notice.dataStatus != 1) {
                    alert("恢复数据出错!")
                }
            })
        }
    };

    //信息发送
    $scope.send = function (notice, index) {
        var id = notice.id;
        noticeService.send(id, function (data) {
            for (var i = 0; i < $scope.notices.length; i++) {
                if ($scope.notices[i].id == data.id) {
                    $scope.notices[i].sendTime = data.sendTime;
                    $scope.notices[i].status = 3;
                }
            }
        })
    };

    //选择切换
    $scope.selectRole = function (notice) {
        $scope.notice.orgId = "";
        $scope.notice.loginUserNames = [];
    };

    $scope.selectEmp = function (notice) {
        $scope.notice.groupId = "";
        $scope.notice.orgId = "";
    };


    $scope.selectOrg = function (notice) {
        $scope.notice.loginUserNames = [];
        $scope.notice.groupId = "";
    };


});