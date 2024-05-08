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
<p align="center" style="font-size:35px;">职业及收入证明</p>
<p style="font-size:18px;text-indent: 2em;word-wrap: break-word;">兹有<SPAN style="border-bottom:1px solid">&nbsp;&nbsp;${customer.name !''}&nbsp;&nbsp;</SPAN>同志，身份证号码<SPAN style="border-bottom:1px solid">&nbsp;&nbsp;${customer.identifyNo !''}&nbsp;&nbsp;</SPAN>,系我单位工作人员，在我单位工作<SPAN style="border-bottom:1px solid">&nbsp;&nbsp;<#if customer.customerJob??>${customer.customerJob.workDate !''}</#if>&nbsp;&nbsp;</SPAN>年，任<SPAN style="border-bottom:1px solid">&nbsp;&nbsp;<#if customer.customerJob??>${customer.customerJob.job !''}</#if>&nbsp;&nbsp;</SPAN>职务。其年收入为<SPAN style="border-bottom:1px solid">&nbsp;&nbsp;${customer.SALARY !''}&nbsp;&nbsp;</SPAN>万元（大写）<SPAN style="border-bottom:1px solid">&nbsp;&nbsp;<#if customer.customerJob??>${customer.customerJob.salary !''}</#if>&nbsp;&nbsp;</SPAN>万元（小写）。</p>
<p style="font-size:18px;">特此证明。</p>
<p style="font-size:18px;text-indent: 2em;">上述证明真实无误，我单位对此承担相应的法律责任。</p><br/><br/>
<p style="font-size:18px; margin-left: 420px;" >人事（劳资）部门公章</p>
<p style="font-size:18px; margin-left: 440px;">${date !''}</p><br/><br/>
<p style="font-size:18px;">备注：<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${customer.comment !''}</SPAN></p>
<p style="font-size:18px;">1.单位全称：<#if customer.customerJob??>${customer.customerJob.companyName !''}</#if></p>
<p style="font-size:18px;">2.法定地址：<#if customer.customerJob??>${customer.customerJob.companyAddress !''}</#if></p>
<p style="font-size:18px;">3.人事（劳资）部门负责人的姓名：<#if customer.customerJob??>${customer.customerJob.hrName !''}</#if></p>
<p style="font-size:18px;">4.联系电话：<#if customer.customerJob??>${customer.customerJob.hrCell !''}</#if>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;邮政编码:</p>
</body>
</html>
