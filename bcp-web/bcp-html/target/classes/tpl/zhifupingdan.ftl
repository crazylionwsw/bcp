<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title></title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
body {
	margin-left: 45px;
	margin-right: 45px;
	font-family: Arial Unicode MS;
	font-size: 10px;
}

table {
	margin: auto;
	width: 100%;
	border-collapse: collapse;
	border: 1px solid #444444;
}

th,td {
	border: 1px solid #444444;
	font-size: 10px;
	margin-left: 5px;
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
	text-align: left;
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
<body align="center">
<p align="center" style="font-size:24px">北京富择风险管理顾问有限公司垫资支付凭单</p><br/><br/>

<p style="font-size:15px">资金用途:<u>${customer.name !''}</u><#if appointpayment?? && (appointpayment.chargeParty == 1)>贷款额<#elseif appointpayment?? && (appointpayment.chargeParty == 0)>贷款差额</#if></p>
<p style="font-size:15px;">实付人民币大写：<u>${appointpayment.APPOINTPAYAMOUNT !''}元整</u>(￥：<u>${appointpayment.appointPayAmount !''}.00</u>)</p>
<p style="font-size:15px;">分期经理：${employee.username !''}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
部门经理：${employeeleader.username !''}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
汇款人：${operatename !''}</p>

<br/>
<div style="position:absolute;left: 500px;top: 208px;"><#if imagepath??><img src="${imagepath}/52.png" /></#if> </div>
	<p style="font-size:17px;margin-left: 500px">${date}
	</p>

	</body>
</html>
