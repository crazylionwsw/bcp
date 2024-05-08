<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style type="text/css">
    </style>
</head>
    <body align="center">
        <p style="font-size: 1em;">关于客户${customer.name!""}的情况说明</p>
        <p style="font-size: 1em;">
            <ol>
                <li>
                    购车分期情况<br/>
                    客户拟购买${cartype.fullName!"XXXXX"}，该车成交价格为人民币<#if customerloan??>${customerloan.realPrice /10000 !0}<#else>XXXXX</#if>万元（<#if purchasecarorder.businessTypeCode?? && purchasecarorder.businessTypeCode == 'NC'>新车指导价<#if cartype??>${cartype.price /10000 !0}<#else>XXXXX</#if><#else>评估价<#if carvaluation??>${carvaluation.price /10000 !0}<#else>XXXXX</#if></#if>万元），客户首付<#if customerloan??>${customerloan.downPayment /10000 !0}<#else>XXXXX</#if>万元，申请分期贷款<#if customerloan??>${customerloan.creditAmount / 10000 !0}<#else>XXXXX</#if>万元，占比分别是${customerloan.downPaymentRatio!0}%和${customerloan.creditRatio!0}%，申请期数是${customerloan.rateType.months}期，手续费<#if customerloan??>${customerloan.bankFeeAmount/10000 !0}<#else>XXXXX</#if>万元，申请分期合计<#if customerloan??>${customerloan.applyAmount/10000!0}<#else>XXXXX</#if>万元。
                </li>
                <li>
                    基本情况<br/>
                    客户${customer.name!""}，性别<#if customer.gender ==0>男<#else>女</#if>，${customer.age!"XXXXX"}岁（${customer.birthday!"XXXXX"}年生人）。<#if customer.customerJob??>目前在${customer.customerJob.companyName!"XXXXX"}工作，担任${customer.customerJob.job!"XXXXX"}</#if>。企业经营情况，如需要请补充说明。
                </li>
                <li>
                    资产情况<br/>
                    如需要请补充说明。
                </li>
                <li>
                    负债情况<br/>
                    如需要请补充说明。
                </li>
                <li>
                    指标情况<br/>
                    购车指标是用申请人${purchasecarorder.indicatorStatusName!""}获得。
                </li>
                <li>
                    还款能力分析<br/>
                    <#if declaration.paymentToIncome?? &&  declaration.paymentToIncome.sourceIncomes?? && (declaration.paymentToIncome.sourceIncomes?size > 0)>
                        客户收入流水的主要来源为：
                            <#list declaration.paymentToIncome.sourceIncomes as sourceIncome>
                                <#if sourceIncome == 'af'>申请表 </#if>
                                <#if sourceIncome == 'ic'>收入证明 </#if>
                                <#if sourceIncome == 'ssc'>社保卡 </#if>
                                <#if sourceIncome == 'bf'>银行流水 </#if>
                            </#list>，
                    </#if>如需要请补充说明。
                </li>
                <li>
                    社保情况<br/>
                    ${customer.name!''}有北京社保卡，缴存单位与申请单位${declaration.companyIsSame?string("一致","不一致") !'不一致'}，缴存基数${declaration.insuranceBase !0}元;<#if matecustomer??>据客户告知，配偶${matecustomer.name!""}没有缴纳社保，因此未提供社保卡。</#if>
                </li>
            </ol>
        </p>
    </body>
</html>
<#--
关于客户${customer.name!""}的情况说明<br/>

1、购车分期情况<br/>
    客户拟购买${cartype.fullName!"XXXXX"}，该车成交价格为人民币<#if customerloan??>${customerloan.realPrice /10000 !0}<#else>XXXXX</#if>万元（<#if purchasecarorder.businessTypeCode?? && purchasecarorder.businessTypeCode == 'NC'>新车指导价<#if cartype??>${cartype.price /10000 !0}<#else>XXXXX</#if><#else>评估价<#if carvaluation??>${carvaluation.price /10000 !0}<#else>XXXXX</#if></#if>万元），客户首付<#if customerloan??>${customerloan.downPayment /10000 !0}<#else>XXXXX</#if>万元，申请分期贷款<#if customerloan??>${customerloan.creditAmount / 10000 !0}<#else>XXXXX</#if>万，占比分别是${customerloan.downPaymentRatio!0}%和${customerloan.creditRatio!0}%，申请期数是${customerloan.rateType.months}期，手续费<#if customerloan??>${customerloan.bankFeeAmount/10000 !0}<#else>XXXXX</#if>万，申请分期合计<#if customerloan??>${customerloan.applyAmount/10000!0}<#else>XXXXX</#if>万元。<br/>
2、 基本情况<br/>
    客户${customer.name!""}，性别<#if customer.gender ==0>男<#else>女</#if>，${customer.age!"XXXXX"}岁（${customer.birthday!"XXXXX"}年生人）。<#if customer.customerJob??>目前在${customer.customerJob.companyName!"XXXXX"}工作，担任${customer.customerJob.job!"XXXXX"}</#if>。企业经营情况，如需要请补充说明。<br/>
3、 资产情况<br/>
    如需要请补充说明。<br/>
4、负债情况<br/>
    如需要请补充说明。<br/>
5、指标情况<br/>
    购车指标是用申请人${customerdemand.indicatorStatusName!""}获得。<br/>
6、 还款能力分析<br/>
    如需要请补充说明。<br/>
7、 社保情况<br/>
    ${customer.name!''}有北京社保卡，缴存单位与申请单位${declaration.companyIsSame?string("一致","不一致") !'不一致'}，缴存基数${declaration.insuranceBase !0}元。<#if matecustomer??>据客户告知，配偶${matecustomer.name!""}没有缴纳社保，因此未提供社保卡。</#if><br/>
-->
