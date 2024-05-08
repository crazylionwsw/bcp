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
            font-size: 10px;
            margin-left: 2px;
            height: 18px;
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
<table>
    <tr style="background:gray"  align="center">
        <td colspan="8" align="center">报批信息汇总表         客户类别：${declaration.customerClass !""}类</td>
    </tr>
    <tr>
        <td align="center" rowspan="8">基<br/>本<br/>信<br/>息</td>
        <td align="center" rowspan="2">要素信息</td>
        <td align="center" colspan="2">单位属性</td>
        <td align="center" colspan="4">${declaration.nature !""}</td>
    </tr>
    <tr>
        <td align="center" colspan="2">授信额</td>
        <td align="center">${customerloan.creditAmount + customerloan.bankFeeAmount !0}元</td>
        <td align="center" colspan="2">客户年收入</td>
        <td align="center"><#if customer.customerJob??>${customer.customerJob.salary!0}万元</#if></td>
    </tr>
    <tr>
        <td align="center" rowspan="2">申请人</td>
        <td align="center" colspan="2">姓名</td>
        <td align="center">${customer.name !''}</td>
        <td align="center" colspan="2">手机号</td>
        <td align="center">
            <#if customer.cells??&&(customer.cells?size > 0)>
                ${customer.cells[0] !''}<br/>${declaration.customerCellVerify?string("已核实","未核实") !'未核实'}
            </#if>
        </td>
    </tr>
    <tr>
        <td align="center" colspan="2">单位名称</td>
        <td align="center"><#if customer.customerJob??>${customer.customerJob.companyName !''}</#if></td>
        <td align="center" colspan="2">单位电话</td>
        <td align="center"><#if customer.customerJob??>${customer.customerJob.hrCell !''}</#if></td>
    </tr>
    <#if matecustomer??>
        <tr>
            <td align="center">配偶</td>
            <td align="center" colspan="2">姓名</td>
            <td align="center">${matecustomer.name !''}</td>
            <td align="center" colspan="2"></td>
            <td align="center"></td>
        </tr>
    <#else>
        <tr>
            <td align="center">配偶</td>
            <td align="center" colspan="6">无</td>
        </tr>
    </#if>
    <tr>
        <td align="center" rowspan="3">抵押人</td>
        <td align="center" colspan="2">姓名</td>
        <td align="center">
        <#if customerdemand.relation == '0'>
                ${customer.name!''}
            <#else>
                ${pledgecustomer.name !''}
        </#if>
        </td>
        <td align="center" colspan="2">与申请人关系</td>
        <td align="center">${customerdemand.relationName !''}</td>
    </tr>
    <tr>
        <td align="center" colspan="2">手机号</td>
        <td align="center">
        <#if customerdemand.relation == '0'>
            <#if customer.cells??&&(customer.cells?size > 0)>
                ${customer.cells[0] !''}<br/>${declaration.customerCellVerify?string("已核实","未核实") !'未核实'}
            </#if>
        <#else>
            <#if pledgecustomer.cells??&&(pledgecustomer.cells?size > 0)>
                ${pledgecustomer.cells[0] !''}<br/>${declaration.pledgeCellVerify?string("已核实","未核实") !'未核实'}
            </#if>
        </#if>
        </td>
        <td align="center" colspan="2">指标状态</td>
        <td align="center">
            <#if customerdemand.indicatorStatus?? && (customerdemand.indicatorStatus == 1)>
                现标
            <#elseif customerdemand.indicatorStatus?? && (customerdemand.indicatorStatus == 2)>
                外迁
            <#elseif customerdemand.indicatorStatus?? && (customerdemand.indicatorStatus == 3)>
                报废
            <#elseif customerdemand.indicatorStatus?? && (customerdemand.indicatorStatus == 4)>
                本地置换
            </#if>
        </td>
    </tr>
    <tr>
        <#if customerdemand.indicatorStatus?? && (customerdemand.indicatorStatus != 1)>
            <td align="center" colspan="2">预计指标形成时间</td>
            <td align="center">${customerdemand.retrieveDate !""}</td>
        <#else>
            <td align="center" colspan="2"></td>
            <td align="center"></td>
        </#if>
        <td align="center" colspan="2"></td>
        <td align="center"></td>
    </tr>
    <tr>
        <td align="center" rowspan="<#if purchasecarorder.businessTypeCode == 'OC'>9<#else>8</#if>" colspan="2">车辆及贷款信息</td>
        <td align="center" colspan="2">车辆类型</td>
        <td align="center"><#if purchasecarorder.businessTypeCode == 'NC' >新车<#elseif purchasecarorder.businessTypeCode == 'OC'>二手车</#if></td>
        <td align="center" colspan="2">品牌车型</td>
        <td align="center">${cartype.fullName !''}</td>
    </tr>
    <#if purchasecarorder.businessTypeCode == 'OC'>
        <tr>
            <td align="center" colspan="2">二手车架号</td>
            <td align="center">${customercar.vin !''}</td>
            <td align="center" colspan="2"></td>
            <td align="center"></td>
        </tr>
    </#if>
    <tr>
        <td align="center" colspan="2">开票价格</td>
        <td align="center">${customerloan.receiptPrice !0}元</td>
        <td align="center" colspan="2"><#if purchasecarorder.businessTypeCode == 'OC' >评估价格</#if></td>
        <td align="center"><#if purchasecarorder.businessTypeCode == 'OC' >${carvaluation.price !''}</#if></td>
    </tr>
    <tr>
        <td align="center" colspan="2">申请分期额</td>
        <td align="center">${customerloan.creditAmount !0}元</td>
        <td align="center" colspan="2">授信额</td>
        <td align="center">${customerloan.creditAmount + customerloan.bankFeeAmount !0}元</td>
    </tr>
    <tr>
        <td align="center" colspan="2">首付比例</td>
        <td align="center">${customerloan.downPaymentRatio !0}%</td>
        <td align="center" colspan="2">商贷期限</td>
        <td align="center"><#if customerloan.rateType??>${customerloan.rateType.months !0}期</#if></td>
    </tr>
    <tr>
        <td align="center" colspan="2">手续费交纳方式</td>
        <td align="center">${customerloan.chargePaymentWayName !""}</td>
        <td align="center" colspan="2">商贷手续费</td>
        <td align="center">${customerloan.bankFeeAmount - customerloan.compensatoryAmount !0}元</td>
    </tr>
    <tr>
        <td align="center" colspan="2">贴息方案</td>
        <#if purchasecarorder.businessTypeCode == "NC">
            <td align="center">${customerloan.compensatoryWay !""}</td>
        <#else>
            <td align="center">无</td>
        </#if>
        <td align="center" colspan="2">贴息期限</td>
        <#if purchasecarorder.businessTypeCode == "NC">
            <td align="center">${customerloan.compensatoryMonth!0}期</td>
        <#else>
            <td align="center">无</td>
        </#if>
    </tr>
    <tr>
        <td align="center" colspan="2">手续费总额</td>
        <td align="center">${customerloan.bankFeeAmount !0}元</td>
        <td align="center" colspan="2">贴息总额</td>
        <#if purchasecarorder.businessTypeCode == "NC">
            <td align="center">${customerloan.compensatoryAmount !0}元</td>
        <#else>
            <td align="center">无</td>
        </#if>
    </tr>
    <tr>
        <td align="center" colspan="2">客户年收入</td>
        <td align="center"><#if customer.customerJob??>${customer.customerJob.salary !0}万元</#if></td>
        <td align="center" colspan="2">平均年还款</td>
        <td align="center">${customerloan.perSalary !0}元</td>
    </tr>
    <tr>
        <td align="center" colspan="2">法院查询信息</td>
        <td colspan="6">
            <div style="display:block;width:46em;height:auto;word-wrap: break-word">
                ${declaration.fayuanQueryComment !''}
            </div>
        </td>
    </tr>
    <tr>
        <td align="center"  colspan="2">特殊情况说明</td>
        <td colspan="6">
            <div style="display:block;width:46em;height:auto;word-wrap: break-word">
                ${declaration.specialComment!""}
            </div>
        </td>
    </tr>
    <tr>
        <td align="center" colspan="2">材料清单</td>
        <td colspan="6">
            <#if customerimages?? && (customerimages?size>0)>
                <#list customerimages as item>
                ${item_index+1}、${item["name"]}<br/>
                </#list>
            </#if>
        </td>
    </tr>
</table>

<div style="font-size:18px;page-break-before: always" class="tab">
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