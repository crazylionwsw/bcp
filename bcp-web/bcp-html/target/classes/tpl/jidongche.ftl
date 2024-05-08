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
            line-height: 4em;
        }

        table {
            margin: auto;
            width: 100%;
            height:600px;
            border-collapse: collapse;
            border: 2px solid #444444;
        }

        th,td,tr {
            border: 1px solid #444444;
            font-size: 16px;
            margin-left: 3px;
            height: 53px;
        }

        .tab{
            /*page-break-after: always;*/
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



<body align="center" >
<br/>
<div class="tab">
    <p align="center" style="font-size:32px">办理机动车业务委托授权书</p>

    <p style="font-size:24px;">北京市公安局公安交通管理局车辆管理所：</p>
    <p style="font-size:24px; text-indent:2em;word-wrap: break-word;">兹授权被委托人____代表<SPAN style="border-bottom:1px solid">中国工商银行股份有限公司北京<#if cashsource??>${cashsource.shortName !''}</#if></SPAN>（抵押权人）办理<SPAN style="border-bottom:1px solid">&nbsp;抵押登记&nbsp;</SPAN>（委托范围）业务。</p>
    <p style="font-size:24px;text-indent:2em">抵押权人确认：经办人在申请上述业务时签署的文件、
        提供的资料有效，代表抵押权人的意愿。
    <span style="font-size:24px">委托办理车号牌或识别代号：<span style="border-bottom:1px solid;">&nbsp;<#if customercar??>${customercar.licenseNumber !''}</#if>&nbsp;</span>。</span>
    <span style="font-size:24px">本委托有效期：____年__月___日至____年__月___日。</span></p>
    <p style="font-size:24px">本授权为经办业务授权，再次委托无效。</p><br/>
    <p style="font-size:24px">抵押权人（盖章）：</p><br/>
    <p style="font-size:24px;text-indent:2em">授权经办日期：____年____月____日</p>

</div>










</body>
</html>
