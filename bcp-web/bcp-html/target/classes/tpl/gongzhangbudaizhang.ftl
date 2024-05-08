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

<br/>
<div class="tab">
    <div>
        <div style="margin-left:100px; margin-top:105px;float:left;"><#if imagepath??><img src="${imagepath}/zye.png"  /></#if></div>
        <div style="margin-left:10px; margin-top:7px;float:left;"></div>
    </div>
    <div style="clear:both"></div>
</div>
<div class="tab">
    <div>
        <div style="margin-left:100px; margin-top:50px;float:left;"><#if imagepath??><img src="${imagepath}/zye.png"  /></#if></div>
        <div style="margin-left:10px; margin-top:7px;float:left;"></div>
    </div>
    <div style="clear:both"></div>
</div>
<div class="tab">
    <div>
        <div style="margin-left:100px; margin-top:50px;float:left;"><#if imagepath??><img src="${imagepath}/zye.png"  /></#if></div>
        <div style="margin-left:10px; margin-top:7px;float:left;"></div>

    </div>
    <div style="clear:both"></div>
</div>
<div class="tab" style="page-break-after: avoid;">
    <div>
        <div style="margin-left:100px; margin-top:50px;float:left;"><#if imagepath??><img src="${imagepath}/zye.png"  /></#if></div>
        <div style="margin-left:10px; margin-top:7px;float:left;"></div>
    </div>
    <div style="clear:both"></div>
</div>
</body>
</html>
