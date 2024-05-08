<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style type="text/css">
        body {
            margin-left: 15px;
          	margin-right: 15px;
            font-family: Arial Unicode MS;
            font-size: 10px;
        }

        table {
            margin: auto;
            width: 100%;
            height:70px;
            border-collapse: collapse;
            border: 2px solid #444444;
            align: center;
        }

        th,td,tr {
            border: 1px solid #444444;
            font-size: 10px;
            margin-left: 2px;
            height: 20px;
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

        table.innertable {
            border: none;
            width: 100%;
            border-collapse: collapse;
            border-spacing:0px;
        }
        table.innertable td {
            border: 1px solid #444;
        }
        table.innertable td {
            border-top-color: darkgray;
            border-left-color: darkgray;　　　　
        }
        table.innertable tr:last-child td{
            border-bottom-color: darkgray;
        }
        table.innertable tr td:last-child{
            border-right-color: darkgray;
        }

        @page {
            size: 8.5in 11in;
        @
        bottom-center {
            content: "page " counter(
                    page
            ) " of  " counter(
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
        }
    </style>
</head>

    <body>
        <table>
            <tr style="background:gray"  align="center">
                <td colspan="11" align="center">信用卡分期业务审核表         客户类型：${declaration.customerClass !''}类</td>
            </tr>
            <tr>
                <td align="center">申请人</td>
                <td align="center">${customer.name !''}</td>
                <td align="center">身份证号</td>
                <td align="center" colspan="2">${customer.identifyNo !''}</td>
                <td align="center">单位</td>
                <td align="center" colspan="3"><#if customer.customerJob??>${customer.customerJob.companyName !''}</#if></td>
                <td align="center">职务</td>
                <td align="center"><#if customer.customerJob??>${customer.customerJob.job !""}</#if></td>
            </tr>
            <#if declaration.needCommon?? && declaration.needCommon>
                <tr>
                    <td align="center">共同申请人</td>
                    <td align="center"><#if matecustomer??>${matecustomer.name !''}</#if></td>
                    <td align="center">身份证号</td>
                    <td align="center" colspan="2"><#if matecustomer??>${matecustomer.identifyNo !''}</#if></td>
                    <td align="center">单位</td>
                    <td align="center" colspan="3"><#if matecustomer?? && matecustomer.customerJob??>${matecustomer.customerJob.companyName !''}</#if></td>
                    <td align="center">职务</td>
                    <td align="center"><#if matecustomer?? && matecustomer.customerJob??>${matecustomer.customerJob.job !''}</#if></td>
                </tr>
            </#if>
            <tr>
                <td align="center">抵押人</td>
                <td align="center"><#if pledgecustomer??>${pledgecustomer.name !''}</#if></td>
                <td align="center">身份证号</td>
                <td align="center" colspan="2"><#if pledgecustomer??>${pledgecustomer.identifyNo !''}</#if></td>
                <td align="center" colspan="2">与申请人关系</td>
                <td align="center"><#if customerdemand??>${customerdemand.relationName !''}</#if></td>
                <td align="center"></td>
                <td align="center">手续费缴纳方式</td>
                <td align="center">${customerloan.chargePaymentWayName!''}</td>
            </tr>
            <tr>
                <td align="center">车辆品牌</td>
                <td align="center"><#if carbrand??>${carbrand.name !''}</#if></td>
                <td align="center">车价</td>
                <td align="center"><#if customerloan??>${customerloan.applyAmount !''}元</#if></td>
                <td align="center">首付比例</td>
                <td align="center"><#if customerloan??>${customerloan.downPaymentRatio !0}%</#if></td>
                <td align="center">售车方</td>
                <td align="center" colspan="2"><#if cardealer??>${cardealer.name !''}</#if></td>
                <td align="center">新车\二手车</td>
                <td align="center"><#if declaration.businessTypeCode == 'NC'>新车<#else>二手车</#if></td>
            </tr>
            <tr>
                <td colspan="2" align="center">申请分期金额</td>
                <td align="center"><#if customerloan??>${customerloan.creditAmount !0}元</#if></td>
                <td align="center">申请期限</td>
                <td align="center"><#if customerloan?? && customerloan.rateType??>${customerloan.rateType.months !0}期</#if></td>
                <td align="center">费率</td>
                <td align="center"><#if customerloan?? && customerloan.rateType??>${customerloan.rateType.ratio * 100 !0}%</#if></td>
                <td align="center">手续费金额</td>
                <td align="center"><#if customerloan??>${customerloan.bankFeeAmount !0}元</#if></td>
                <td align="center">月均还款</td>
                <td align="center"><#if customerloan??>${customerloan.perMonthlyAmount !0}元</#if></td>
            </tr>
            <tr>
                <td colspan="3" align="center">征信查询结果</td>
                <td colspan="3" align="center">社保卡查询结果</td>
                <td colspan="2" align="center">工商查询结果</td>
                <td colspan="3" align="center">法院执行查询结果</td>
            </tr>
            <tr>
                <td colspan="3" valign="top">
                    <div style="display:block;text-indent: 2em;width:16em;height:auto;word-wrap: break-word;">
                        ${declaration.creditReportComment !''}
                    </div>
                </td>
                <td colspan="3" valign="top">
                    <div style="display:block;text-indent: 2em;width:16em;height:auto;word-wrap: break-word">
                        ${declaration.socialSecurityComment !''}
                    </div>
                </td>
                <td colspan="2" valign="top">
                    <div style="display:block;text-indent: 2em;width:12em;height:auto;word-wrap: break-word">
                        ${declaration.icbcQueryComment !''}
                    </div>
                </td>
                <td colspan="3" valign="top">
                    <div style="display:block;text-indent: 2em;width:16em;height:auto;word-wrap: break-word">
                        ${declaration.fayuanQueryComment !''}
                    </div>
                </td>
            </tr>
            <#if declaration.oneTotalIncome !=0 >
                <tr>
                    <td colspan="6" align="center">银行流水审阅结果</td>
                    <td align="center">账户类型</td>
                    <td colspan="4" align="center"><#if declaration.paymentToIncome??>${declaration.paymentToIncome.accountOneType !''}</#if></td>
                </tr>
                <tr>
                    <td align="center">日期</td>
                    <td align="center">金额<br/>(万元)</td>
                    <td align="center">日期</td>
                    <td align="center">金额<br/>(万元)</td>
                    <td align="center">日期</td>
                    <td align="center">金额<br/>(万元)</td>
                    <td align="center">收入合计(万元)</td>
                    <td align="center">月均收入(万元)</td>
                    <td align="center" colspan="3">备注</td>
                </tr>
                <#list 0..declaration.oneNum-1 as col>
                    <tr>
                        <td align="center"><#if declaration.oneAccounts[0]?? && declaration.oneAccounts[0][col]??>${declaration.oneAccounts[0][col].year!""}年${declaration.oneAccounts[0][col].month!""}月</#if></td>
                        <td align="center"><#if declaration.oneAccounts[0]?? && declaration.oneAccounts[0][col]??>${declaration.oneAccounts[0][col].income!""}</#if></td>
                        <td align="center"><#if declaration.oneAccounts[1]?? && declaration.oneAccounts[1][col]??>${declaration.oneAccounts[1][col].year!""}年${declaration.oneAccounts[1][col].month!""}月</#if></td>
                        <td align="center"><#if declaration.oneAccounts[1]?? && declaration.oneAccounts[1][col]??>${declaration.oneAccounts[1][col].income!""}</#if></td>
                        <td align="center"><#if declaration.oneAccounts[2]?? && declaration.oneAccounts[2][col]??>${declaration.oneAccounts[2][col].year!""}年${declaration.oneAccounts[2][col].month!""}月</#if></td>
                        <td align="center"><#if declaration.oneAccounts[2]?? && declaration.oneAccounts[2][col]??>${declaration.oneAccounts[2][col].income!""}</#if></td>
                        <#if col == 0 >
                            <td align="center" rowspan="<#if declaration.oneNum??>${declaration.oneNum !1}<#else>1</#if>">${declaration.oneTotalIncome !0}</td>
                            <td align="center" rowspan="<#if declaration.oneNum??>${declaration.oneNum !1}<#else>1</#if>">${declaration.onePerIncome !0}</td>
                            <td align="center" colspan="3" rowspan="<#if declaration.oneNum??>${declaration.oneNum !1}<#else>1</#if>">
                                <div style="display: block;text-indent:2em;width: 14em;word-wrap: break-word;">
                                    ${declaration.paymentToIncome.accountOneDescription !''}
                                </div>
                            </td>
                        </#if>
                    </tr>
                </#list>
            </#if>
            <#if declaration.twoTotalIncome !=0 >
                <tr>
                    <td colspan="6" align="center">银行流水审阅结果</td>
                    <td align="center">账户类型</td>
                    <td colspan="4" align="center"><#if declaration.paymentToIncome??>${declaration.paymentToIncome.accountTwoType !''}</#if></td>
                </tr>
                <tr>
                    <td align="center">日期</td>
                    <td align="center">金额<br/>(万元)</td>
                    <td align="center">日期</td>
                    <td align="center">金额<br/>(万元)</td>
                    <td align="center">日期</td>
                    <td align="center">金额<br/>(万元)</td>
                    <td align="center">收入合计(万元)</td>
                    <td align="center">月均收入(万元)</td>
                    <td align="center" colspan="3">备注</td>
                </tr>
                <#list 0..declaration.twoNum-1 as col>
                    <tr>
                        <td align="center"><#if declaration.twoAccounts[0]?? && declaration.twoAccounts[0][col]??>${declaration.twoAccounts[0][col].year!""}年${declaration.twoAccounts[0][col].month!""}月</#if></td>
                        <td align="center"><#if declaration.twoAccounts[0]?? && declaration.twoAccounts[0][col]??>${declaration.twoAccounts[0][col].income!""}</#if></td>
                        <td align="center"><#if declaration.twoAccounts[1]?? && declaration.twoAccounts[1][col]??>${declaration.twoAccounts[1][col].year!""}年${declaration.twoAccounts[1][col].month!""}月</#if></td>
                        <td align="center"><#if declaration.twoAccounts[1]?? && declaration.twoAccounts[1][col]??>${declaration.twoAccounts[1][col].income!""}</#if></td>
                        <td align="center"><#if declaration.twoAccounts[2]?? && declaration.twoAccounts[2][col]??>${declaration.twoAccounts[2][col].year!""}年${declaration.twoAccounts[2][col].month!""}月</#if></td>
                        <td align="center"><#if declaration.twoAccounts[2]?? && declaration.twoAccounts[2][col]??>${declaration.twoAccounts[2][col].income!""}</#if></td>
                        <#if col == 0 >
                            <td align="center" rowspan="<#if declaration.twoNum??>${declaration.twoNum !1}<#else>1</#if>">${declaration.twoTotalIncome !0}</td>
                            <td align="center" rowspan="<#if declaration.twoNum??>${declaration.twoNum !1}<#else>1</#if>">${declaration.twoPerIncome !0}</td>
                            <td align="center" colspan="3" rowspan="<#if declaration.twoNum??>${declaration.twoNum !1}<#else>1</#if>">
                                <div style="display: block;text-indent:2em;width: 14em;word-wrap: break-word;">
                                    ${declaration.paymentToIncome.accountTwoDescription !''}
                                </div>
                            </td>
                        </#if>
                    </tr>
                </#list>
            </#if>
        </table>
    </body>
</html>