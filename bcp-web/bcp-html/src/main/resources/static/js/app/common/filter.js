'use strict';

/* Filters */
// need load the moment.js to use this filter. 
app.filter('dataStatusFilter', function() {
    return function(data) {
        if( '0'== data)
            return "临时保存";
        else if('9'== data)
            return "作废";
        else if('1'==data)
            return "正常";
        else if('8'==data)
            return "锁定";
    }
});

app.filter('typeFilter', function() {
    return function(data) {
        if( 'radio'== data)
            return "单选题";
        else if('text'== data)
            return "填空题";
        else if('checklist'==data)
            return "多选题";
    }
});

app.filter('maritalStatusFilter', function() {
    return function(data) {
        if( 'unmarried'== data)
            return "未婚";
        else if('married'== data)
            return "已婚";
        else if('divorced'== data)
            return "离异";
        else if('widowed' == data)
            return "丧偶";
        else if('other' == data)
            return "其他";
    }
});



app.filter('channelFilter',function () {
    return function (data) {
        if('sms' == data)
            return "短信";
        else if('email' == data)
            return "邮箱";
        else if('websocket' == data)
            return "WEB页面消息";
        else if('wechat' == data)
            return "微信";
        else if('pad' == data)
            return "PAD推送";
        else if('mobile' == data)
            return "移动APP";
        else if('notify' == data)
            return "系统内部通知消息";
    }
});

app.filter('cashSourceFilter', function() {
    return function(data) {
        if( '0'== data)
            return "银行";
        else if('1'== data)
            return "金融公司";
        else if('2'==data)
            return "P2P产品";
        else if('3'==data)
            return "个人资产";
        else if('9'==data)
            return "自有资金";
    }
});

//集团分成状态
app.filter('GroupSharingFilter', function() {
    return function(data) {
        if( '0'== data)
            return "初始化";
        else if('1'== data)
            return "修正";
        else if('2'==data)
            return "核对";
        else if('3'==data)
            return "复核";
        else if('4'==data)
            return "结算";
    }
});

app.filter('carvaluationFilter', function() {
    return function(data) {
        if( '1'== data)
            return "待确认";
        else if('2'==data)
            return "已确认";
    }
});

app.filter('customerClassFilter', function() {
    return function(data) {
        if( '1'== data)
            return "一";
        else if('2'== data)
            return "二";
        else if('5'==data)
            return "五";
        else if('3'==data)
            return "三";
        else if('4'==data)
            return "四";
        else
            return "未分";
    }
});

//收款计划表状态过滤
app.filter('RepaymentPlanFilter', function() {
    return function(data) {
        if( '0'== data)
            return "未计算";
        else if('1'== data)
            return "计算正确";
        else if('2'== data)
            return "核对正确";
        else if('3'== data)
            return "核对错误";
        else
            return "计算错误";
    }
});

//对账总表数据状态
app.filter('BalanceAccountFilter', function() {
    return function(data) {
        if( '0'== data)
            return "未导出";
        else if('2'== data)
            return "开始付款";
        else
            return "付款完成";
    }
});
app.filter('AccountFilter', function() {
    return function(data) {
        if( '0'== data)
            return "已结算待核对";
        else if('1'== data)
            return "已核对待付款";
        else if('2'== data)
            return "付款中待确认";
        else if('9'== data)
            return "已付款已结算";
    }
});



app.filter('employeelFilter',function () {
    return function (data) {
        if('1' == data)
            return "报批专职";
        else if('2' == data)
            return "合同专职";
        else if('3' == data)
            return "信用卡专职";
    }
});

app.filter('StatusFilter', function() {
    return function(data) {
        if( '0'== data)
            return "处理中";
        else if('11'== data)
            return "已取消";
        else if('8'== data)
            return "已完成";
        else if('16'== data)
            return "取消中";
    }
});

app.filter('CooperateStatusFilter', function() {
    return function(data) {
        if( '0'== data)
            return "商谈阶段";
        else if('1'== data)
            return "正常合作";
        else if('9'== data)
            return "终止合作";
    }
});

app.filter('accountTypeFilter', function() {
    return function(data) {
        if( '0'== data)
            return "个人账户";
        else if('1'== data)
            return "公司账户";
    }
});
app.filter('accountWayFilter',function(){
    return function(data){
        if('0' == data){
            return "收付款账户";
        }else if('1'== data){
            return "付款账户";
        }else if('2' == data){
            return "收款账户";
        }
    }
});

app.filter('yesno', function() {
    return function(data) {
        if( true== data)
            return "是";
        else
            return "否";
    }
});
//请注意区分
app.filter('zeroone', function() {
    return function(data) {
        if( 1 == data)
            return "是";
        else
            return "否";
    }
});

app.filter('carvaluationOrderFilter', function() {
    return function(data) {
        if( true == data)
            return "交易使用中";
        else
            return "交易未使用";
    }
});

app.filter('onezero', function() {
    return function(data) {
        if( 0 == data)
            return "是";
        else
            return "否";
    }
});

app.filter('chargePaymentWayFilter',function(){
    return function(data){
        if("WHOLE" == data){
            return "趸交";
        }else if("STAGES" == data){
            return "分期";
        } else{
            return "趸交";
        }
    }
});

/**
 *  客户性别
 * */
app.filter('gender', function() {
    return function(data) {
        if( '-1' == data)
            return "未标识";
        else if('0' == data)
            return "男";
        else if('1' == data){
            return "女";
        }
    }
});

app.filter('mainPartTypeFilter', function() {
    return function(data) {
        if( '1' == data)
            return "渠道";
        else if('2' == data)
            return "销售人员";
        else if('3' == data){
            return "渠道经理";
        }
    }
});

/**
 * 审核状态         作废
 * */     
app.filter('statusFilter',function(){
    return function(data){
        if("0" == data){
            return "初始化状态";
        }else if("1" == data){
            return "审核中";
        }else if("2" == data){
            return "拒绝";
        }else if("9" == data){
            return "通过";
        }else if("12" == data){
            return "驳回";
        }
    }
});

//垫资项目
app.filter('paymentTypeFilter',function(){
    return function(data){
        if("0" == data){
            return "贴息金额";
        }else if("1" == data){
            return "贷款金额";
        }else if("2" == data){
            return "贴息金额+贷款金额";
        }
    }
});

//预约垫资中的垫资完成时间
app.filter('advancedPayOCFilter',function(){
    return function(data){
        if("1" == data){
            return "出交易票前垫资";
        }else if("2" == data){
            return "出交易票后垫资";
        }
    }
});

app.filter('advancedPayNCFilter',function(){
    return function(data){
        if("1" == data){
            return "开发票前垫资";
        }else if("2" == data){
            return "开发票后垫资";
        }
    }
});


app.filter('socialInsuranceFilter',function(){
    return function(data){
        if("3months" == data){
            return "3个月内";
        }else if("6months" == data){
            return "半年以内";
        }else if("1year" == data){
            return "1年到2年";
        }else if("3years" == data){
            return "3年以上";
        }
    }
});

/*签批状态*/
app.filter('signStatusFilter',function(){
    return function(data){
        if("0" == data){
            return "初始";
        }else if("2" == data){
            return "通过";
        }else if("8" == data){
            return "驳回";
        }else if("9" == data){
            return "拒绝";
        }
    }
});
//反馈意见类型过滤器
app.filter('feedBackFilter',function(){
    return function(data){
        if("0" == data){
            return "产品建议";
        }else if("1" == data){
            return "程序错误";
        }else if("2" == data){
            return "其他意见";
        }
    }
});

app.filter('flagStatusFilter',function(){
    return function(data){
        if("0" == data){
            return "备注";
        }else if("1" == data){
            return "提交";
        }else if("2" == data){
            return "审核";
        }else if("3" == data){
            return "重审";
        }
    }
});

//通知管理类型过滤器
app.filter('noticeTypeFilter',function(){
    return function(data){
        if("type_1" == data){
            return "通知";
        }else if("type_2" == data){
            return "公告";
        }else if("type_3" == data){
            return "消息";
        }
    }
});

//通知管理发送类型过滤器
app.filter('noticeSendTypeFilter',function(){
    return function(data){
        if("sms" == data){
            return "短信";
        }else if("email" == data){
            return "邮箱";
        }else if("websocket" == data){
            return "WEB页面消息";
        }else if("wechat" == data){
            return "微信";
        }else if("pad" == data){
            return "PAD 推送";
        }else if("mobile" == data){
            return "移动APP";
        }
    }
});
//通知管理发送状态过滤器
app.filter('noticeStatusFilter',function(){
    return function(data){
        if("1" == data){
            return "未发送";
        }else if("2" == data){
            return "发送中";
        }else if("3" == data){
            return "发送成功";
        }else if("4" == data){
            return "发送失败";
        }
    }
});

app.filter('operateStatusFilter',function(){
    return function(data){
        if("1" == data){
            return "运营";
        }else if("0" == data){
            return "非运营";
        }
    }
});

/*审核状态*/
app.filter('approveStatusFilter',function(){
    return function(data){
        if("0" == data){
            return "待审核";
        }else if("1" == data){
            return "审核中";
        }else if("2" == data){
            return "审核通过";
        }else if("8" == data){
            return "驳回待提交";
        }else if("9" == data){
            return "拒绝";
        }
    }
});
/*交易状态*/
app.filter('transactionStatusFilter',function(){
    return function(data){
        if("0" == data){
            return "待进行";
        }else if("9" == data){
            return "进行中";
        }else if("8" == data){
            return "已完成";
        }else if("11" == data){
            return "已取消";
        }else if("16" == data){
            return "取消中";
        }else if("18" == data){
            return "被拒绝";
        }
    }
});

app.filter('AppointPaymentStatusFilter',function(){
    return function(data){
        if(0 == data){
            return "业务待提交";
        }else if(1 == data){
            return "已提交待审核";
        }else if(2 == data){
            return "部门已审批";
        }else if(3 == data){
            return "财务已审批";
        }else if(4 == data){
            return "风控已审批";
        }else if(5 == data){
            return "财务已支付";
        }
    }
});

app.filter('AppointPaymentApproveStatusFilter',function(){
    return function(data){
        if(1 == data){
            return "待支付";
        }else if(2 == data){
            return "已支付";
        }else if(8 == data){
            return "驳回待提交";
        }else if(9 == data){
            return "拒绝";
        }
    }
});

app.filter('WorkStatusFilter',function(){
    return function(data){
        if(0 == data){
            return "待提交";
        }else if(1 == data){
            return "待确认";
        }else if(2 == data){
            return "已确认";
        }
    }
});

app.filter('DecompressFilter',function () {
    return function (data) {
       if(0 == data){
           return "待提交";
       } else if (1 == data){
           return "待确认";
       }else if (2 == data){
           return"已确认";
       }else if (100 == data){
           return "待处理";
       }
    }
});

app.filter('DealerRepaymentStatusFilter',function(){
    return function(data){
        if(0 == data){
            return "未还款";
        }else if(1 == data){
            return "待确认";
        }else if(2 == data){
            return "已确认";
        }
    }
});

app.filter('SwipingCardStatusFilter',function(){
    return function(data){
        if(0 == data){
            return "未提交";
        }else if(1 == data){
            return "待确认";
        }else if(2 == data){
            return "已确认";
        }
    }
});

/*  报批状态*/
app.filter('DeclarationStatusFilter',function(){
    return function(data){
        if(0 == data){
            return "未报批";
        }else if(1 == data){
            return "报批通过";
        }else if(2 == data){
            return "报批拒绝";
        }else if(3 == data){
            return "报批调整";
        }else if(4 == data){
            return "报批中";
        }
    }
});

app.filter('DeclarationResultFilter',function(){
    return function(data){
        if(0 == data){
            return "未反馈";
        }else if(2 == data){
            return "报批通过";
        }else if(8 == data){
            return "报批调整";
        }else if(9 == data){
            return "报批拒绝";
        }
    }
});

app.filter('DmvpStatusFilter', function() {
    return function(data) {
        if( '0'== data)
            return "登记证待收取";
        else if('1'== data)
            return "登记证已收取";
        else if('2'== data)
            return "银行抵押合同已打印";
        else if('3'== data)
            return "银行抵押合同已盖章";
        else if('4'== data)
            return "已送达车管所开始抵押";
        else if('5'== data)
            return "车管所抵押已完成";
        else if('16'== data)
            return "业务在取消中";
        else if('11'== data)
            return "业务已经取消";
        else
            return "状态错误";
    }
});

/*领取、归还*/
app.filter('usageTypeFilter',function(){
    return function(data){
        if("0" == data){
            return "归还";
        }else if("1" == data){
            return "领取";
        }
    }
});
/*垫资支付方式*/
app.filter('chargePartyFilter',function(){
    return function(data){
        if("0" == data){
            return "差额支付";
        }else if("1" == data){
            return "全额支付";
        }
    }
});

app.filter('BusinessTypeFilter',function(){
    return function(data){
        if("NC" == data){
            return "新车";
        }else if("OC" == data){
            return "二手车";
        }else if("CC" == data){
            return "测试";
        }
    }
});

//解押类型过滤
app.filter('decompressTypeFilter',function(){
    return function(data){
        if("NORMALPAYMENT" == data){
            return "正常解押";
        }else if("BEFOREPAYMENT" == data){
            return "提前还款";
        }
    }
});

app.filter('PaymentBillFilter',function(){
    return function(data){
        if("CARPAY" == data){
            return "购车缴费";
        }else {
            return "解押缴费";
        }
    }
});


app.filter('NatureFilter',function(){
    return function(data){
        if("dxgq" == data){
            return "大型国企";
        }else if("500q" == data){
            return "世界500强企业";
        }else if("sydw" == data){
            return "列入国家编制的事业单位";
        }else if("gwy" == data){
            return "公务员";
        }else if("teacher" == data){
            return "教师";
        }else if("doctor" == data){
            return "医生";
        }else if("arm" == data){
            return "军人";
        }
    }
});

app.filter('careerFilter',function(){
    return function(data){
        if("legalperson" == data){
            return "企业法人";
        }else if("salesmanager" == data){
            return "销售经理";
        }else if("salesman" == data){
            return "销售人员";
        }
    }
});

app.filter('CardStatusFilter', function() {
    return function(data) {
        if( '0'== data)
            return "待制卡";
        else if('1'== data)
            return "已制卡";
        else if('2'== data)
            return "已取卡";
        else if('3'== data)
            return "已启卡";
        else if('4'== data)
            return "已调额";
        else if('5'== data)
            return "已刷卡";
        else if('6'== data)
            return "已领卡";
        else if('9'== data)
            return "已销卡";
        else if('16'== data)
            return "业务在取消中";
        else if('11'== data)
            return "业务已经取消";
        else if('99'== data)
            return "代启卡失败";
        else
            return "状态错误";
    }
});

app.filter('chargefellplanFilter',function(){
    return function(data){
        if("1" == data){
            return "计算正确";
        }else if("2" == data){
            return "核对通过";
        }else if("3" == data){
            return "核对错误";
        }else if("4" == data){
            return "取消核对";
        }else if("9" == data){
            return "计算错误";
        }
    }
});

app.filter('auditStatusFilter',function(){
    return function(data){
        if("0" == data){
            return "初审";
        }else if("1" == data){
            return "终审";
        }
    }
});

app.filter('auditRoleFilter',function(){
    return function(data){//G_ASSISTANT_APPROVAL，G_FIRST_APPROVAL，G_SECOND_APPROVAL，G_CHIEF_APPROVAL
        if("G_ASSISTANT_APPROVAL" == data){
            return "助理审批官";
        }else if("G_FIRST_APPROVAL" == data){
            return "一级审批官";
        }else if("G_SECOND_APPROVAL" == data){
            return "二级审批官";
        }else if("G_CHIEF_APPROVAL" == data){
            return "首席审批官";
        }
    }
});

app.filter('taskInfoFilter',function(){
    return function(data){//G_ASSISTANT_APPROVAL，G_FIRST_APPROVAL，G_SECOND_APPROVAL，G_CHIEF_APPROVAL
        if("CustomerDemand_QueryCreditReport" == data){
            return "征信查询";
        }else {
            return "";
        }
    }
});

app.filter('imgsrc',function(){
    return function(data){
        return '/json/file/download/' + data;
    }
});

app.filter('strList',function(){
    return function(data){
        if (angular.isArray(data)) {
            return data.join(' , ');
        } else {
            return data;
        }
    }
});

app.filter('placeholder',function(){
    return function(data){
        if (angular.isUndefined(data)) { return 'N/A';}

        if (angular.isString(data)) {
            if (data) {
                return data;
            } else {
                return 'N/A';
            }
        } else {
            return data;
        }
    }
});

app.filter('mergeRateTypes',function(){
    return function(rates, rate){
        var i = 0, len = rates.length;
        for(; i < len; i++){
            if (rates[i].months == rate.months) {
                return rates[i];
            }
        }

        return rate;
    }
});

app.filter('getSourceRateType',function(){
    return function(rates, sourceRateId){
        var i = 0, len = rates.length;
        for(; i < len; i++){
            if (rates[i].sourceRateId == sourceRateId) {
                return rates[i];
            }
        }

        return rate;
    }
});

app.filter('getByImageTypeCode',function(){
    return function(input, code){
        var i = 0, len = input.length;
        for(; i < len; i++){
            if (input[i].customerImageTypeCode == code) {
                return input[i];
            }
        }
        return null;
    }
});

app.filter('checkByImageTypeCode',function(){
    return function(input, code){
        var i = 0, len = input.length;
        for(; i < len; i++){
            if (input[i] == code) {
                return true;
            }
        }
        return false;
    }
});

app.filter('getByContractsId',function(){
    return function(input, id){
        var i = 0, len = input.length;
        for(; i < len; i++){
            if (input[i].id == id) {
                return input[i];
            }
        }

        return null;
    }
});

/**
 * 通过CarTypeIds过滤分期产品
 */
app.filter('filterCreditProductsByCarTypeIds',function(){
    return function(input, carTypeIds){
        var creditProducts = [];

        if (carTypeIds && carTypeIds.length > 0) {
            for(var j = 0; j < input.length; j++) {
                var include = false;
                if (input[j].carTypeIds && input[j].carTypeIds.length > 0) {
                    for(var i = 0; i < input[j].carTypeIds.length; i++) {
                        if (carTypeIds.indexOf(input[j].carTypeIds[i]) > -1) {
                            include = true;
                            break;
                        }
                    }
                } else {
                    include = true;
                }

                if (include) creditProducts.push(input[j]);
            }
            return creditProducts;
        }

        return input;
    }
});

app.filter('filterCreditProductsByBusinessTypeId',function(){
    return function(input, businessTypeId){
        var creditProducts = [];

        if (businessTypeId) {
            var i = 0;
            for(; i < input.length; i++) {
                var include = true;
                if (input[i].businessTypeIds && input[i].businessTypeIds.length > 0) {
                    if (input[i].businessTypeIds.indexOf(businessTypeId) < 0) {
                        include = false;
                        break;
                    }
                }

                if (include) creditProducts.push(input[i]);
            }
            return creditProducts;
        }

        return input;
    }
});

app.filter('filterCreditProductsByCashSourceId',function(){
    return function(input, cashSourceId){
        var creditProducts = [];

        if (cashSourceId) {
            var i = 0;
            for(; i < input.length; i++) {
                var include = true;
                if (input[i].cashSourceId) {
                    if (input[i].cashSourceId != cashSourceId) {
                        include = false;
                        break;
                    }
                }

                if (include) creditProducts.push(input[i]);
            }
            return creditProducts;
        }

        return input;
    }
});

app.filter('motiveTypeFilter', function() {
    return function(data) {
        if( '0'== data)
            return "汽油";
        else if('1'== data)
            return "柴油";
        else if('2'== data)
            return "混合动力";
        else
            return "电池";
    }
});

app.filter('levelFilter', function() {
    return function(data) {
        if( '1'== data)
            return "一级经销商";
        else if('2'== data)
            return "二级经销商";
    }
});

app.filter('loginLogFilter', function() {
    return function(data) {
        if( '0'== data)
            return "登录成功";
        else if('9'== data)
            return "登录失败";
    }
});

app.filter('buttonShowFilter', function() {
    return function(data) {
        if( '部门经理'== data )
            return "审批";
        else if('审查专员'== data || '审查组'== data)
            return "资料审查";
        else if('审批专员'== data || '审批组'== data || '审批人'== data)
            return "贷款审批";
        else if('财务部'== data)
            return "核对";
    }
});
app.filter('relationFilter', function() {
    return function(data) {
        if( '0'== data)
            return "本人";
        else if('1'== data)
            return "父子";
        else if('2'== data)
            return "配偶";
        else if('3'== data)
            return "其他";
        else
            return "关系异常";
    }
});

app.filter('indicatorStatusFilter', function() {
    return function(data) {
        if( '1'== data)
            return "现标";
        else if('2'== data)
            return "外迁";
        else if('3'== data)
            return "报废";
        else if('4'== data)
            return "本地置换";
    }
});

app.filter('moveoutDateFilter', function() {
    return function(data) {
        if( '1'== data)
            return "现标时间";
        else if('2'== data)
            return "外迁时间";
        else if('3'== data)
            return "报废时间";
        else if('4'== data)
            return "本地置换时间";
    }
});

app.filter('creditphotographFilter', function() {
    return function(data) {
        if( true== data)
            return "已上传";
        else if(false == data)
            return "待上传";
    }
});

app.filter('creditphotographStatus', function() {
    return function(data) {
        if( '0'== data)
            return "待生成";
        else if('1' == data)
            return "正在生成";
        else if('8' == data)
            return "已生成";
        else if('9' == data)
            return "生成失败";
    }
});

app.filter('sharingConfirmStatus', function() {
    return function(data) {
        if( '0'== data)
            return "初始化";
        else if('1' == data)
            return "已修正";
        else if('2' == data)
            return "已核对";
        else if('3' == data)
            return "已复核";
        else if('4' == data)
            return "已结算";
    }
});

app.filter('mainPartTypeStatus', function() {
    return function(data) {
        if( '1'== data)
            return "渠道";
        else if('2' == data)
            return "销售人员";
        else if('3' == data)
            return "渠道经理";
    }
});

app.filter('momentDate', function() {
    return function(date, format) {
        return moment(date).format(format);
    }
});

app.filter('billTimeName', function() {
    return function(data) {
        if('A002' == data)
            return "签约时间";
        else if('A004' == data)
            return "垫资时间";
        else if('A005' == data)
            return "上牌日期";
        else if('A008' == data)
            return "业务发生时间";
        else if('A011' == data)
            return "申请时间";
        else if('A013' == data)
            return "截止时间";
        else if('A020' == data)
            return "提交时间";
        else if('A023' == data)
            return "转移过户日期";
        else if('A026' == data)
            return "预计刷卡日期";
        else if('A031' == data)
            return "解押时间";
        else if('A032' == data)
            return "逾期时间";
        else
            return  "提交时间";
    }
});

//  身份证有效期过滤器
app.filter('IdentifyValidFilter', function() {
    return function(data) {
        if (data) {
            var index = data.indexOf(":");
            if (index > 0){
                return data.substring(0, index) + "至" + data.substring(index + 1);
            }
            return data;
        }
        return null;
    }
});

app.filter('momentDate', function() {
    return function(date, format) {
        if (date && date != undefined){
            return moment(date).format(format);
        }
        return null;
    }
});

app.filter('RejectCustomerRelation', function() {
    return function(data) {
        if (data == '0'){
            return "贷款人";
        } else if (data == '1'){
            return "配偶";
        } else if (data == '2'){
            return "指标人";
        } else {
            return "其他";
        }
    }
});

app.filter('StringToList', function() {
    return function(data, suffix) {
        if (data){
            var list = [];
            var stringList = data.split(suffix ? suffix : ",");
            angular.forEach(stringList,function (item, index) {
                list.push(item.trim());
            });
            return list;
        }
        return null;
    }
});

app.filter('ListToString', function() {
    return function(list, suffix) {
        if (data){
            var string = list.join(suffix ? suffix : ",");
            return string;
        }
        return "";
    }
});

app.filter('MapControl', function() {
    return function(map,controlway, key) {
        if (map){
            var newMap = {};
            var keys = _.keys(map);
            var values = _.values(map);
            switch (controlway){
                case 'remove':
                    angular.forEach(keys,function (item, index) {
                        if (item != key) {
                            newMap[item] = values[index];
                        }
                    });
                    return newMap;
                case 'getValuesByRegexKey':
                    var list = [];
                    angular.forEach(keys,function (item, index) {
                        if (item.indexOf(key) >= 0) {
                            list[index] = values[index];
                        }
                    });
                    return list;
                case 'getValuesSumByRegexKey':
                    var sum = 0.0;
                    angular.forEach(keys,function (item, index) {
                        if (item.indexOf(key) >= 0) {
                            sum += values[index];
                        }
                    });
                    return sum;
            }
        }
        return null;
    }
});
