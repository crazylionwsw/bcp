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
            line-height: 4em;
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
            /*page-break-before: always;*/
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
<span style="font-size:19px;font-weight: bold;" >通知时间:${dateTime}</span><br/>
<span align="center" style="margin-left: 200px;font-size:27px;font-weight: bold;">客户资料交接单</span><br/>
<#if receptfile??&&receptfile.fileNames?? && (receptfile.fileNames?size>0) >
    <#list receptfile.fileNames as fileName>
        <span style="font-size:21px">
            <#if fileName != "信用卡">
                ${fileName_index +1 }、${fileName !""}<br/>
                <#else>
                ${fileName_index +1 }、${fileName !""}(卡号:<#if customercard??>${customercard.cardNo !""}</#if>)
            </#if>
        </span>
    </#list>
</#if>
<p style="font-size:18px;word-wrap: break-word;"><span style="TEXT-DECORATION: underline;border-bottom:1px solid black;font-size:18px;font-weight: bold;">重要提示</span><br/>1、请您于每月______日前还款,首期偿还金额、以后每期还款金额以刷卡凭条为准。请留意客服(95588)发送的还款信息。
2、贷款期间,请您务必每年按时续保,投保范围不少于下列险种:车损险、盗抢险、第三者责任险(保额不少于20万元)、机动车交通事故责任强制保险(二手车增加自燃险);保险单或保险凭证应载明银行(贷款人)为保险第一受益人。
3、确认:在办理本次信用卡汽车专项分期付款业务中,申请人、抵押人所签署的文件均是申请人、抵押人本人签署,并承担相应的法律责任。
4、请您还清贷款后拨打010-85995610咨询办理机动车登记证书的领取及车辆解除抵押事宜。</p>
<p style="font-size:18px;margin-left: 460px;">签收人：<br/>年&nbsp;&nbsp;月&nbsp;&nbsp;日</p>
</body>
</html>
