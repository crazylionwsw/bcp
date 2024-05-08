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
	
	.remind{
		color: red;
		height: 28px;
		padding-top: 10px;
	}

</style>
</head>
<body>
	<div id="mainSet">
	<table id="tabSet">
		<tr>
			<td><b>富择分期（www.fuzefenqi.com）重新设置密码通知</b><br></td>
		</tr>
		<tr>
			<td>
				<span class="fontSet">${userName}，您忘记了密码！</span>
				<hr class="hrDotted"/><br>
				登陆账户为：${userName} <br>
				请点击链接进行修改，如安全限制没法点击，可以直接将链接地址复制到浏览器地址栏进行修改操作。<br>
				<i><b>修改密码地址：</b><br>${activityUrl}</i><br>
				<span class="remind">（此链接三十分钟内有效，请及时修改！）</span><br>
			</td>
		</tr>
		<tr>
			<td>
				<a href='${activityUrl}' target="newactivate">直接点击进入修改</a>
				<hr class="hrDotted"/><br>
			</td>
		</tr>		
		<tr><td><b>富择分期敬上</b></td></tr>
	</table>
	</div>
</body>
</html>


