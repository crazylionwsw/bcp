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
<p align="center" style="font-size:32px">关于${customer.name !''}汽车指标确认书的说明</p>
<p style="font-size:20px; line-height:1em;">北京分行：</p>
<p style="font-size:20px; line-height:2.3em; text-indent:2em;width: 30em;word-wrap: break-word;">客户${customer.name !''}(证件号${customer.identifyNo !''})，用其${customerdemand.relationName !''}<#if pledgecustomer?? && pledgecustomer.name != " ">${pledgecustomer.name !''}</#if>（证件号<#if pledgecustomer?? && pledgecustomer.name != " ">${pledgecustomer.identifyNo !''}</#if>）指标申请分期购车，该客户为旧车置换,购置新车。由于该客户指标正在更新中，故《更新指标》暂时无法提供给贵行。该客户将在调额前提供汽车更新指标确认书。特此说明！</p><br/><br/><br/><br/><br/>
<p style="font-size:20px;margin-left: 360px;">北京富择风险管理顾问有限公司</p>
<p style="font-size:20px;margin-left: 400px;">时间：${date !''}
</p>
</body>
</html>
