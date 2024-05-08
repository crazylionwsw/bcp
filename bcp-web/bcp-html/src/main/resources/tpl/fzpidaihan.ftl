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
<p align="center" style="font-size:20px;">中国工商银行购车专项分期付款业务通知单</p>
<p style="font-size: 16px;">${cardealer.name !''}：</p>
<p style="font-size:16px;text-indent:2em;width: 40em;word-wrap: break-word;">经审查，以下申请人购车分期付款申请已获我行核准，请贵公司据此协助申请人办理各项手续。</p>
<table>
    <tr>
        <td align="center">姓名</td>
        <td align="center">身份证号</td>
        <td align="center">汽车品牌</td>
        <td align="center">汽车型号</td>
        <td align="center">付款金额</td>
        <td align="center">分期期数</td>
    </tr>
    <tr>
        <td align="center">${customer.name !''}</td>
        <td align="center">${customer.identifyNo !''}</td>
        <td align="center">${carbrand.name !''}</td>
        <td align="center">${cartype.name !''}</td>
        <td align="center">${customerloan.creditAmount !''}</td>
        <td align="center"><#if customerloan.rateType??>${customerloan.rateType.months !''}</#if></td>
    </tr>
    <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
</table>
<div style="position:absolute;left: 210px;top: 240px;"><#if imagepath??><img src="${imagepath}/${bankImageName !'chaoyang.png'}"/></#if></div>
<p style="font-size:17px; margin-left: 400px;">中国工商银行</p>
<p style="font-size:17px;margin-left: 400px;">${date !''}</p>
</body>
</html>
