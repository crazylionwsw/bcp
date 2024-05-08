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
            height:65px;
            border-collapse: collapse;
            border: 2px solid #444444;
             align: center;
        }

        th,td,tr {
            border: 1px solid #444444;
            font-size: 10px;
            margin-left: 2px;
            height: 18px;
            align: center;
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

        table.innertable {
            border: 0px;
            border-collapse: collapse;
            border-spacing:0px;
        }
        table.innertable td {
            border: 2px solid #444;
        }
        table.innertable td {
            border-top-color: white;
            border-left-color: white;　　　　
        }
        table.innertable tr:last-child td{
            border-bottom-color: white;
        }
        table.innertable tr td:last-child{
            border-right-color: white;
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
<div style="font-size:18px;" class="tab">
    <div style="font-size:18px;" class="tab">
        <#if customerimages?? && (customerimages?size>0)>
            <#list customerimages as item >
                <p align="center" style="font-size:15px;text-indent:2em">${item["name"]}</p>
                <#list item["fileUrls"] as fileurl >
                    <p style="text-align:center"><img src="${fileurl}" style="width:315px;"/></p>
                </#list>
            </#list>
        </#if>
    </div>
</div>
</body>
</html>