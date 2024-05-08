<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style type="text/css">
        body {
            margin-left: 25px;
            margin-right: 45px;
            font-family: Arial Unicode MS;
            font-size: 10px;
        }

        table {
            margin: auto;
            width: 100%;
            height:80px;
            border-collapse: collapse;
            border: 2px solid #444444;
             align: center;
        }

        th,td,tr {
            border: 1px solid #444444;
            font-size: 10px;
            margin-left: 2px;
            height: 25px;
            align: center;
        }
        

        .tab{
            page-break-after: always;
        }


        .mcContent {
            line-height: 180%;
            padding: 20px;
        }

        .logo {
            text-align: center;
        }

        .title {
            text-align: center;
            font-weight: bold;
            font-size: 20px;
        }

        .notes {
            font-weight: normal;
            margin-left: 5px;
            margin-right: 5px;
            line-height: 18px;
        }

        .text_content {
            margin-left: 5px;
            margin-right: 5px;
            line-height: 18px;
        }

        .sum_insured_first_row {
            width: 20%;
        }

        .sum_insured_span {
            font-size: 10px;
        }

        .special_agreements_div {
            page-break-before: always;
            font-size: 14px;
            margin-top: 20px;
        }

        .special_agreements_div .special_agreements {
            font-size: 18px;
            font-weight: bold;
        }

        .title_right {
            width: 100%;
            margin: 0 auto;
        }

        .title_right p {
            text-align: center;
            margin: 0;
            margin-left: 50%;
            padding: 0;
        }

        @page {
            size: 8.5in 11in;
        @
        bottom-center
        {
            content
            :
                    "page "
                    counter(
                            page
                    )
                    " of  "
                    counter(
                            pages
                    );
        }

        .signature {
            margin: 0 auto;
            clear: both;
            font-size: 16px;
            font-weight: bold;
        }

        .signature_table {
            /* 	font-size: 16px; */
            font-weight: bold;
        }

    </style>
</head>



<body >
 <p style="font-size:24px;">附件六</p>
 <p style="font-size:27px;" align="center">&emsp;&emsp;&emsp;中国工商银行信用卡汽车专项&emsp;&emsp;
&emsp;&emsp;分期付款业务台账</p>
<table>
<tr style="background:gray"  align="center">
	<td colspan="8" align="center">客户基本信息</td>
</tr>
<tr>
	<td align="center">姓名</td>
	<td align="center">${customer.name !''}</td>
	<td colspan="2" align="center">身份证件类<br/>型及号码</td>
	<td align="center">${customer.identifyNo !''}</td>
	<td align="center">卡号</td>
	<td align="center" colspan="2"></td>
</tr>
<tr>
	<td align="center">单位名称</td>
	<td align="center"><#if customer.customerJob??>${customer.customerJob.companyName !''}</#if></td>
	<td align="center" colspan="2">单位地址</td>
	<td align="center"><#if customer.customerJob??>${customer.customerJob.companyAddress !''}</#if></td>
	<td align="center">单位电话</td>
	<td align="center" colspan="2"><#if customer.customerJob??>${customer.customerJob.hrCell !''}</#if></td>
</tr>
<tr>
	<td align="center">家庭住址</td>
	<td align="center">${customer.contactAddress !''}</td>
	<td align="center" colspan="2">住宅电话</td>
	<td align="center"></td>
	<td align="center">手机号码</td>
	<td align="center" colspan="2">
        <#if customer.cells??&&(customer.cells?size > 0)>
            ${customer.cells[0] !''}
        </#if>
    </td>
</tr>
<tr style="background:gray;" align="center">
	<td colspan="8" align="center">汽车专项分期付款信息</td>
</tr>
<tr>
 	<td align="center">经销商名称</td>
 	<td align="center" colspan="3">${cardealer.name !''}</td>
 	<td align="center">经销商地址</td>
 	<td align="center" colspan="3">${cardealer.address !''}</td>
 	
</tr>
<tr>
	<td align="center">联系人</td>
	<td align="center">${cardealer.manager !''}</td>
	<td align="center">联系电话</td>
	<td align="center">${cardealer.telephone !''}</td>
	<td align="center">传真</td>
	<td align="center"></td>
	<td align="center">POS编号</td>
	<td align="center" style="width: 50px;"></td>
</tr>
<tr>
    <td align="center">汽车品牌</td>
    <td align="center">${carbrand.name !''}</td>
    <td align="center">汽车型号</td>
    <td align="center">${cartype.name !''}</td>
    <td align="center">担保方式</td>
    <td align="center">${customerloan.guaranteeWayName !''}</td>
    <td align="center">合同编号</td>
    <td align="center"></td>
</tr>
<tr>
	 <td align="center">分期金额</td>
	 <td align="center">${customerloan.creditAmount !''}</td>
	 <td align="center">分期期数</td>
	 <td align="center"><#if customerloan.rateType??>${customerloan.rateType.months !''}</#if></td>
	 <td align="center">分期起止日期</td>
	 <td align="left">起：</td>
     <td align="left" colspan="2">至：</td>
</tr>
<tr>
	<td align="center">发卡机构名称</td>
	<td align="center" colspan="2"></td>
	<td align="center">联系人</td>
	<td align="center"></td>
	<td align="center">联系电话</td>
	<td align="center" colspan="2"></td>
</tr>
<tr style="background:gray;" align="center">
	<td colspan="8" align="center">担保信息</td>
</tr>
<tr>
	<td align="center" rowspan="2">抵押</td>
	<td align="center" colspan="2">抵押人</td>
	<td align="center" colspan="1">${pledgecustomer.name !''}</td>
	<td align="center" colspan="1">押品种类</td>
	<td align="center" colspan="3">□汽车            □房产</td>
</tr>
<tr>
	<td align="center" colspan="2">押品权证号</td>
	<td align="center" colspan="1"></td>
	<td align="center" colspan="1">抵押登记日期</td>
	<td align="center" colspan="3"></td>
</tr>
<tr>
	<td align="center"  rowspan="2">质押</td>
	<td align="center" colspan="2">出质人</td>
	<td align="center"></td>
	<td align="center">质物名称</td>
	<td align="center"></td>
	<td align="center">质物价值</td>
	<td align="center"></td>
</tr>
<tr>
	<td align="center" colspan="2">权属证明/权利凭证</td>
	<td align="center" colspan="1"></td>
	<td align="center" colspan="1">质物登记日期</td>
	<td align="center" colspan="3"></td>
</tr>
<tr>
	<td align="center">保证</td>
	<td align="center" colspan="2">保证人</td>
	<td align="center" colspan="5"></td>
</tr>
<tr style="background:gray;" align="center">
	<td colspan="8" align="center">还款/展期/违约/催收记录</td>
</tr>
<tr>
	<td align="center">日期</td>
    <td align="center" colspan="2">事项</td>
	<td align="center">经办人</td>
	<td align="center">审批人</td>
	<td align="center" colspan="3">备注</td>
</tr>
<tr>
	<td align="center"></td>
    <td align="center" colspan="2"></td>
	<td align="center"></td>
	<td align="center"></td>
	<td align="center" colspan="3"></td>
</tr>
<tr>
	<td align="center"></td>
    <td align="center" colspan="2"></td>
	<td align="center"></td>
	<td align="center"></td>
	<td align="center" colspan="3"></td>
</tr>
<tr>
	<td align="center"></td>
    <td align="center" colspan="2"></td>
	<td align="center"></td>
	<td align="center"></td>
	<td align="center" colspan="3"></td>
</tr>
<tr>
	<td align="center"></td>
    <td align="center" colspan="2"></td>
	<td align="center"></td>
	<td align="center"></td>
	<td align="center" colspan="3"></td>
</tr>
<tr>
	<td align="center"></td>
    <td align="center" colspan="2"></td>
	<td align="center"></td>
	<td align="center"></td>
	<td align="center" colspan="3"></td>
</tr>
</table>
</body>
</html>
