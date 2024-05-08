<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style type="text/css">
        body {
            margin-left: 45px;
            margin-right: 45px;
            font-family: SimSun;
            font-size: 10px;
            line-height: 3em;
            text-indent: 2em;
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

        img{
            width:200px;
            height: 500px;
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
<p  style="font-size:25px;font-weight: bold;text-align: center">关于《北京市小客车更新指标确认通知书》

    &emsp;&emsp;<span style="text-align: center">或新车《行驶证》售后归档的说明</span><br/></p>
<p style="font-size:18px;margin-left: 60px;">北京分行:</p>
<p style="font-size:18px; text-indent:2em;margin-left: 60px;width: 26em;word-wrap: break-word;">客户 <SPAN style="border-bottom:1px solid black"><#if customer??>${customer.name !''}</#if></SPAN>（证件号<SPAN style="border-bottom:1px solid black"><#if customer??>${customer.identifyNo !''}</#if></SPAN>）于<SPAN style="border-bottom:1px solid black"><#if cardealer??>${cardealer.name !''}</#if></SPAN> 购买 <SPAN style="border-bottom:1px solid black"><#if carbrand??>${carbrand.name !''}</#if><#if cartype??>${cartype.name !''}</#if></SPAN> 汽车,申请额度 <SPAN style="border-bottom:1px solid black">${customerloan.creditAmount/10000 !0}</SPAN> 万元。该客户为旧车置换，购置新车,由于该店采取提车后拍卖旧车的方式销售，故《更新指标》证明或新车《行驶证》只能售后提供给我行，特此说明！</p>
<p style="font-size:18px; text-indent:2em;margin-left: 60px;width: 26em;word-wrap: break-word;">我支行承诺严格落实售后一个月之内将《北京市小客车更新指标确认通知书》或新车《行驶证》随申请资料一并归档之我行，并保证购车指标为北京指标、“申请人与指标人关系”与《购车分期申请表》中保持一致，以备后续内控检查。</p>
<br/>
<p style="font-size:18px; margin-left: 370px;">支行名称：<SPAN style="border-bottom:1px solid black"><#if cashsource??>${cashsource.shortName !''}</#if></SPAN></p>
<p style="font-size:18px; margin-left: 370px;">经办人：___________</p>
<p style="font-size:18px; margin-left: 190px;">支行银行卡职能科室科长签字：___________</p>
<p style="font-size:18px; margin-left: 300px;">日期：_____年______月______</p>
</body>
</html>
