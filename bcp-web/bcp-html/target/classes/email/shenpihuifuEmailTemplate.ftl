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
		border: 0px;
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
			<td><b>客户 ${customer.name !""}审批回复！</b><br></td>
		</tr>
		<tr>
			<td>
				<span class="fontSet">尊敬的分期经理${employee.name !""}，您好!</span>
				<hr class="hrDotted"/><br>
				附件是客户${customer.name !""}的审批回复，请尽快审核！<br>
				<br>
			</td>
		</tr>
		<tr><td><b>富择分期敬上</b></td></tr>
	</table>
	</div>
</body>
</html>