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
            border: 0px solid #444444;
        }

        th,td {
            border: 0px solid #444444;
            font-size: 10px;
            margin-left: 5px;
        }

        img{
            width: 350px;
            height: 215px;
            padding-top: 200px;
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
<div style="width:315px;margin:350px auto 0 auto;">
    <table style="width:0%;border: 0px; border-spacing: 0; border-collapse: 0;">
        <tbody>
            <#if fileIds?? && (fileIds?size > 0 )>
                <#list fileIds as fileId>
                <tr>
                    <td><#if urlPath??><img src="${urlPath}/${fileId}" style=""/></#if></td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>

</body>
</html>
