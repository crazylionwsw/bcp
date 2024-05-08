<html>
<head>
<style type="text/css">
	#mainSet{
		width: 630px;
		border: 3px solid #D9F4FF;
	}
	
	#tabSet{
		text-align: left;
		width: 551px;
		height: 349px;
		border: none;
		cellspacing: 0px;
		cellpadding: 0px;
		margin-top: 25px;
		margin-bottom: 25px;
		margin-left: 40px;
	}
	
	.fontSet{
		height: 28px;
		font-size: 14px;
		color: #333;
	}
	
	.hrDotted{
		border: 1px dotted #9F9F9F;
	}

</style>
</head>
<body>
	<div id="mainSet">
	<table id="tabSet">
		<tr>
			<td><b><#if cashsource??>${cashsource.shortName !""}</#if>担保模式购车分期 ${customer.name !""} ${customer.identifyNo!""}</b><br></td>
		</tr>
		<tr>
			<td>
				<span class="fontSet">尊敬的银行专员，您好!</span>
				<hr class="hrDotted"/><br>
					${cashsource.shortName !""}提单一笔合作机构富择担保模式购车分期业务。客户${customer.name !""}，${customer.customerJob.companyName !""}员工，申请分期金额${customerloan.creditAmount !0}元，分${customerloan.rateType.months !''}期，手续费${customerloan.bankFeeAmount !0}元，合计 ${customerloan.creditAmount + customerloan.bankFeeAmount !0}元，烦请审批。
				<br>
			</td>
		</tr>
		<tr><td><b>富择分期敬上</b></td></tr>
	</table>
	</div>
</body>
</html>