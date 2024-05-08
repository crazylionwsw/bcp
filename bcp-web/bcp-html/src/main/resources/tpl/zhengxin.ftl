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
                height:70px;
                border-collapse: collapse;
                border: 2px solid #444444;
                align: center;
            }

            th,td,tr {
                border: 1px solid #444444;
                font-size: 12px;
                margin-left: 2px;
                height: 20px;
                align: center;
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
                text-align: center;
                margin: 0;
                margin-left: 50%;
                padding: 0;
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

    <body >
        <table style="table-layout: auto">
            <tr style="background:gray"  align="center">
                <td colspan="7" align="center">${customer.name !''}的征信报告  ${declaration.customerClass !""}类</td>
            </tr>
            <tr>
                <td align="center" rowspan="6">征信信息</td>
                <td align="center">近12个月有无信用记录</td>
                <td align="center"><#if declaration.creditInfo.credit12Month==0>无<#else>有</#if></td>
                <td align="center">信用卡记录：<#if declaration.creditInfo.credit12MonthCard==0>无<#else>有</#if></td>
                <td align="center">连${declaration.creditInfo.credit12MonthCardMonth !0}累${declaration.creditInfo.credit12MonthCardCount !0}</td>
                <td align="center">贷款记录：<#if declaration.creditInfo.credit12MonthLoan==0>无<#else>有</#if> </td>
                <td align="center">连${declaration.creditInfo.credit12MonthLoanMonth !0}累${declaration.creditInfo.credit12MonthLoanCount !0}</td>
            </tr>
            <tr>
                <td align="center">近12-24个月有无信用记录</td>
                <td align="center"><#if declaration.creditInfo.credit24Month==0>无<#else>有</#if></td>
                <td align="center">信用卡记录：<#if declaration.creditInfo.credit24MonthCard==0>无<#else>有</#if></td>
                <td align="center">连${declaration.creditInfo.credit24MonthCardMonth !0}累${declaration.creditInfo.credit24MonthCardCount !0}</td>
                <td align="center">贷款记录：<#if declaration.creditInfo.credit24MonthLoan==0>无<#else>有</#if></td>
                <td align="center">连${declaration.creditInfo.credit24MonthLoanMonth !0}累${declaration.creditInfo.credit24MonthLoanCount !0}</td>
            </tr>
            <tr>
                <td align="center">近24-60个月有无信用记录</td>
                <td align="center"><#if declaration.creditInfo.credit36Month==0>无<#else>有</#if></td>
                <td align="center">信用卡记录：<#if declaration.creditInfo.credit36MonthCard==0>无<#else>有</#if></td>
                <td align="center">连${declaration.creditInfo.credit36MonthCardMonth !0}累${declaration.creditInfo.credit36MonthCardCount !0}</td>
                <td align="center">贷款记录：<#if declaration.creditInfo.credit36MonthLoan==0>无<#else>有</#if></td>
                <td align="center">连${declaration.creditInfo.credit36MonthLoanMonth !0}累${declaration.creditInfo.credit36MonthLoanCount !0}</td>
            </tr>
            <tr>
                <td align="center">近10天被查询次数</td>
                <td align="center">${declaration.creditInfo.query10Count !0}次</td>
                <td align="center">近20天被查询次数</td>
                <td align="center">${declaration.creditInfo.query20Count !0}次</td>
                <td align="center">近30天被查询次数</td>
                <td align="center">${declaration.creditInfo.query30Count !0}次</td>
            </tr>
            <tr>
                <td align="center">本人名下未结清车贷</td>
                <td align="center" colspan="2">${declaration.creditInfo.carLoanCount !0}笔</td>
                <td align="center" colspan="2">未结清贷款月还款总额</td>
                <td align="center">${declaration.creditInfo.carLoanAmount !0}元</td>
            </tr>
            <tr>
                <td align="center">异常征信情况说明</td>
                <td colspan="7">
                    <div style="display:block;width:40em;word-break:break-all;">
                        ${declaration.creditInfo.specialComment !''}
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>