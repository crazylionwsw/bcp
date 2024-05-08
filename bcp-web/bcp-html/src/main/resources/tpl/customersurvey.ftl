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
<table>
    <tr style="background:gray"  align="center">
        <td colspan="5" align="center">问卷调查汇总</td>
    </tr>
    <tr>
        <td align="center" rowspan="2">基<br/>本<br/>信<br/>息</td>
        <td align="center">姓名</td>
        <td align="center">${customer.name !''}</td>
        <td align="center">手机号</td>
        <td align="center">
            <#if customer.cells??&&(customer.cells?size > 0)>
                ${customer.cells[0] !''}<br/>
            </#if>
        </td>
    </tr>
    <tr>
        <td align="center">单位名称</td>
        <td align="center"><#if customer.customerJob??>${customer.customerJob.companyName !''}</#if></td>
        <td align="center">单位电话</td>
        <td align="center"><#if customer.customerJob??>${customer.customerJob.hrCell !''}</#if></td>
    </tr>
    <tr>
        <td colspan="5">
            <#if surveyresult?? && surveyresult.result?? && (surveyresult.result?size>0)>
                <#list surveyresult.result as item >
                    <p align="left" style="font-size:20px;text-indent:2em">${item_index+1}、<#if item.question??>${item.question.content !''}</#if></p>
                    <#if item.question?? && (item.question.questionType =='radio' || item.question.questionType =='text')>
                        <p align="left" style="font-size:15px;text-indent:4em">${item.answerContents !''}</p>
                    <#elseif item.question?? && (item.question.questionType =='checklist')>
                        <#if item.answerContents?? && (item.answerContents?size>0)>
                            <#list item.answerContents as key >
                                <p align="left" style="font-size:15px;text-indent:4em">${key !''}</p>
                            </#list>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </td>
    </tr>
</table>
<#--<div style="font-size:18px;page-break-before: always" class="tab">
    <div style="font-size:18px;" class="tab">
        <#if surveyresult?? && surveyresult.result?? && (surveyresult.result?size>0)>
            <#list surveyresult.result as item >
                <p align="left" style="font-size:20px;text-indent:2em">${item_index+1}、<#if item.question??>${item.question.content !''}</#if></p>
                <#if item.question?? && (item.question.questionType =='radio' || item.question.questionType =='text')>
                        <p align="left" style="font-size:15px;text-indent:4em">${item.answerContents !''}</p>
                    <#elseif item.question?? && (item.question.questionType =='checklist')>
                        <#if item.answerContents?? && (item.answerContents?size>0)>
                            <#list item.answerContents?keys as key >
                                <p align="left" style="font-size:15px;text-indent:4em">${key !''}</p>
                            </#list>
                        </#if>
                </#if>
            </#list>
        </#if>
    </div>
</div>-->
</body>
</html>