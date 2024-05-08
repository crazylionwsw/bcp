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
            height:600px;
            border-collapse: collapse;
            border: 2px solid #444444;
            align: center;
        }

        th,td,tr {
            border: 1px solid #444444;
            font-size: 16px;
            margin-left: 3px;
            height: 53px;
            word-wrap:break-word;
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
            text-align: left;
            margin: 0;
            margin-left: 50%;
            padding: 0;
        }

        table.innertable {
            border: 0px;
            border-collapse: collapse;
            border-spacing:0px;
        }
        table.innertable td {
            border: 2px solid #444;
        }
        table.innertable td {
            border-top-color: black;
            border-left-color: black;　　　　
        }
        table.innertable tr:last-child td{
            border-bottom-color: black;
        }
        table.innertable tr td:last-child{
            border-right-color: black;
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
        <table>
            <tr style="background:gray"  align="center">
                <td colspan="4" align="center">${customer.name !''}的银行流水  ${declaration.customerClass !""}类</td>
            </tr>
            <tr>
                <td align="center">个人负债总额</td>
                <td align="center">${declaration.paymentToIncome.fuzhaiAmount !0}元</td>
                <td align="center">月还款<br/>（含本次负债）</td>
                <td align="center">${declaration.paymentToIncome.monthRepayment !0}元</td>
            </tr>
            <tr>
                <td align="center">6个月合计收入</td>
                <td align="center">${declaration.paymentToIncome.totalIncome !0}元</td>
                <td align="center">6个月月均收入</td>
                <td align="center">${declaration.paymentToIncome.perIncome !0}元</td>
            </tr>
            <tr>
                <td align="center">收入来源</td>
                <td align="center" colspan="3">
                    <#if declaration.paymentToIncome?? && declaration.paymentToIncome.sourceIncomes ?? && (declaration.paymentToIncome.sourceIncomes?size >0)>
                        <#list declaration.paymentToIncome.sourceIncomes as sourceIncome>
                            <#if sourceIncome == 'af'>
                                申请表&nbsp;&nbsp;&nbsp;&nbsp;
                            <#elseif sourceIncome == 'ic'>
                                收入证明&nbsp;&nbsp;&nbsp;&nbsp;
                            <#elseif sourceIncome == 'ssc'>
                                社保卡&nbsp;&nbsp;&nbsp;&nbsp;
                            <#elseif sourceIncome == 'bf'>
                                银行流水&nbsp;&nbsp;&nbsp;&nbsp;
                            </#if>
                        </#list>
                    </#if>
                </td>
            </tr>
            <tr>
                <td align="center">账户说明</td>
                <td colspan="3">
                    <div style="display:block;width:30em;word-break:break-all;">
                        ${declaration.paymentToIncome.accountDescription !''}
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <table style="border:none;margin: 0;" class="innertable">
                        <tr>
                            <td align="center" colspan="2"><#if declaration.customerClass?? && declaration.customerClass == '四'>个人<#else>帐户一</#if>收入流水       （万元）</td>
                        </tr>
                        <#if declaration.paymentToIncome.accountList??&&(declaration.paymentToIncome.accountList?size > 0)>
                            <#list declaration.paymentToIncome.accountList as account1>
                                <tr>
                                    <td align="center">${account1.year}  年  ${account1.month} 月</td>
                                    <td align="center">${account1.income}</td>
                                </tr>
                            </#list>
                        </#if>
                    </table>
                </td>
                <td colspan="2">
                    <table style="border:none;margin: 0;" class="innertable">
                        <tr>
                            <td align="center" colspan="2"><#if declaration.customerClass?? && declaration.customerClass == '四'>企业<#else>帐户二</#if>收入流水       （万元）</td>
                        </tr>
                        <#if declaration.paymentToIncome.accountList2??&&(declaration.paymentToIncome.accountList2?size > 0)>
                            <#list declaration.paymentToIncome.accountList2 as account2>
                                <tr>
                                    <td align="center">${account2.year}  年  ${account2.month} 月</td>
                                    <td align="center">${account2.income}</td>
                                </tr>
                            </#list>
                        </#if>
                    </table>
                </td>
            </tr>
        </table>
    </body>
</html>