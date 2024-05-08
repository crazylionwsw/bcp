/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('msgController',function ($scope, msgService,emailTemplateService,pushTemplateService,sysparamService, $modal, $q){
    var treeCtrl;
    $scope.tableTitle = "消息模版管理";
    $scope.modalTitle = "消息模版操作";

    $scope.messageTemplates  = [];//返回列表数据
    $scope.messageTemplate = {};

    $scope.sendTypes = [];
    $scope.descTypes = [];

    $scope.currentPage =  1;

    $scope.showemail = false;
    $scope.showcontent = false;
    $scope.showpush = false;


    //列表
    $scope.init = function (){
        $scope.changePage();
    };

    /**
     * 分页功能
     */
    $scope.changePage = function (){
        msgService.getDataByPage($scope.currentPage-1,function (data){
            $scope.messageTemplates = data.result;
            $scope.totalItems = data.totalCount;//数据总条数
            $scope.pageSize = data.pageSize;//分页单位
            $scope.currentPage = data.currentPage+1;//当前页
            $scope.totalPages = data.totalPages;
        });

        msgService.getSendType(function (data) {
            $scope.sendTypes = data;
        });

        emailTemplateService.lookup(function (data) {
            $scope.emailTypes = data;
        });

        pushTemplateService.lookup(function (data) {
            $scope.pushTypes = data;
        });

        msgService.getDescType(function (data) {
            $scope.descTypes = data;
        });

        //获取系统参数中的元数据
        sysparamService.getListByCode("METADATAS",function (data) {
            $scope.metaDatas = data;
        })

    }

    /**
     * 执行初始化
     */
    $scope.init();

    //添加或修改
    $scope.edit = function (messageTemplate,index){

        /*  判断id进行回显 */
        if(messageTemplate.id != "" && messageTemplate.id != null){
            $scope.modalTitle="编辑消息模版";
            $scope.selectedMessageTemplate = messageTemplate;

            $scope.messageTemplate = angular.copy(messageTemplate);

            //    元数据
            /*var metaDatas = $scope.messageTemplate.metaDatas;
            $scope.messageTemplate.metaDataTags = [];
            for(var i in metaDatas){
                $scope.messageTemplate.metaDataTags.push({text: metaDatas[i]});
            }*/

            //发送类型
            var sendType = $scope.messageTemplate.sendType;
            if(sendType == "email"){
                $scope.showemail = true;
                $scope.showcontent = false;
                $scope.showpush = false;
            }else if (sendType == "pad"){
                $scope.showemail = false;
                $scope.showcontent = false;
                $scope.showpush = true;
            }else {
                $scope.showemail = false;
                $scope.showcontent = true;
                $scope.showpush = false;
            }

        } else {
            $scope.showemail = false;
            $scope.showcontent = false;
            $scope.showpush = false;
            $scope.modalTitle="添加消息模版";
            $scope.messageTemplate = {dataStatus: 1};
        }

        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'msgForm.html',
            backdrop:'static',
            controller:function ($scope,$modalInstance,$q){
                
                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }

                /* 保存业务信息 */
                $scope.save = function (){
                    //元数据处理
                    var messageTemplate = $scope.messageTemplate;
                        /*messageTemplate.metaDatas = [];
                        if(messageTemplate.metaDataTags != null){
                            $.each(messageTemplate.metaDataTags, function(j){
                                messageTemplate.metaDatas[j] = messageTemplate.metaDataTags[j].text;
                            });
                        }*/

                    msgService.save(messageTemplate,function (data){
                        if(messageTemplate != null){
                            if (messageTemplate.id == "" || messageTemplate.id == null) {
                                $scope.messageTemplates.push(data);
                            } else {
                                var index = $scope.messageTemplates.indexOf($scope.selectedMessageTemplate);
                                $scope.messageTemplates[index] = data;

                            }
                            $scope.messageTemplate = data;
                            /* 操作完成自动关闭模态框 */
                            modalInstance.close();
                        }

                    });
                }
            }
        });
    }

    //选择发送类型判断
    $scope.selectChannel = function (channel,index) {
        //判断发送类型是否为邮箱
        if(channel.code == "email"){
            $scope.showemail = true;
            $scope.showcontent = false;
            $scope.showpush = false;
        }else if(channel.code == "pad"){
            $scope.showemail = false;
            $scope.showcontent = false;
            $scope.showpush = true;
        }else {
            $scope.showemail = false;
            $scope.showcontent = true;
            $scope.showpush = false;
        }
    };

    /* 删除 */
    $scope.delete = function (messageTemplate,index) {
        var msg = "确定要作废数据?";
        if (messageTemplate.dataStatus == 9) {
            msg = "确定要删除数据?";
        }
        if (confirm(msg)) {
            var id = messageTemplate.id;
            msgService.delete(id, function (data) {
                $scope.init();
            });
        }
    }

    /**
     * 详情页面
     * @param taskdescribe
     */
    $scope.seeMsg = function (messageTemplate) {

        /*  判断id进行回显 */
        if(messageTemplate.id != "" && messageTemplate.id != null){
            $scope.modalTitle="查看详细模版内容";
            $scope.messageTemplate = messageTemplate;
        }
        var modalInstance = $modal.open({
            scope:$scope,
            animation:true,
            size: 'lg',
            templateUrl:'seeMsgForm.html',

            controller:function ($scope,$modalInstance,$q){

                /*  关闭模态框 */
                $scope.cancel = function ($modalInstance){
                    modalInstance.close();
                }

            }
        });
    };

    //恢复数据
    $scope.renew = function (messageTemplate,index) {
        if(confirm("确定要恢复该条数据?")){
            messageTemplate.dataStatus = 1;
            msgService.save(messageTemplate,function (data) {
                if(messageTemplate.dataStatus != 1){
                    alert("恢复数据出错!")
                }
            })
        }
    };

    $scope.checkUnique = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.messageTemplate;
            msgService.checkUnique(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };
});