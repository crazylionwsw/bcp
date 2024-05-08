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
<p align="center" style="font-size:30px">担保承诺函</p>
<p style="font-size:15px">中国工商银行股份有限公司<u>北京</u><u><#if cashsource??>${cashsource.shortName !''}</#if></u>:</p><br/>
<p style="font-size:15px;text-indent:2em;word-wrap: break-word;">汽车专项分期申请人<u>${customer.name !''}</u>（身份证件类型及号码<u>${customer.identifyNo !''}</u>）向贵行申请办理信用卡汽车专项分期付款业务，我公司同意为其与贵行签订的《中国工商银行信用卡汽车专项分期付款/担保(抵押、质押及保证)合同》（编号：〔&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;〕支行〔&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;〕年〔&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;〕号，下称主合同）项下全部债务向贵行提供 一、三担保：</p>
<p style="font-size:15px;text-indent:2em;word-wrap: break-word;">一、全程连带责任保证担保。保证期间为自主合同确定的债权到期日（即最后一期还款到期日届满）之次日起两年；贵行根据主合同之约定要求持卡人提前清偿债务的，则保证期间为债权提前到期日之次日起两年。</p>
<p style="font-size:15px;text-indent:2em;word-wrap: break-word;">二、阶段性连带责任保证担保。保证期间为申请人签定《中国工商银行信用卡汽车专项分期付款/担保(抵押、质押及保证)合同》，自贵行放款之日起至抵押登记手续已办理完毕，且记载有正式抵押登记信息的抵押物他项权证书交付给贵行确认之日止。</p>
<p style="font-size:15px;text-indent:2em;word-wrap: break-word;">三、保证金质押担保。我公司已在贵行开立保证金专户，并存入一定金额的保证金，如持卡人未按时办理车辆抵押手续或履行其在主合同项下债务的，贵行有权扣划保证金用以清偿持卡人所欠贵行款项。</p>
<p style="font-size:15px;text-indent:2em;word-wrap: break-word;">上述担保事宜具体按照我公司与贵行签署的《中国工商银行信用卡汽车专项分期付款业务合作机构担保合同》的有关约定履行。</p>
<br/>
<br/>
<br/>
<br/>
<p style="font-size:17px;margin-left: 100px">担保人（公章）:
<div style="position:absolute;left: 250px;top: 578px;"><#if imagepath??><img src="${imagepath}/sq.png" style=""/></#if> </div>
</p>
<p style="font-size:17px;margin-left: 100px">法定代表人（负责人）或授权代理人（签字）:
<div style="position:absolute;left: 500px;top: 696px;"><#if imagepath??><img src="${imagepath}/zye.png" style=""/></#if> </div>
</p>

<p style="font-size:17px;margin-left: 100px;">时间：${date !''}
</p>
</body>
</html>
