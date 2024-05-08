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
        }

        table {
            margin: auto;
            width: 100%;
            border-collapse: collapse;
            border: 2px solid #444444;
        }

        th,td,tr {
            border: 1px solid #444444;
            font-size: 16px;
            margin-left: 3px;
            width: 25%;
            height: 40px;
            text-align: center;
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
            size:5.5in 10in;
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
<br/><br/><br/><br/>
<p style="font-size:20px;"><b>附件八：</b></p><br/><br/>
<BR/><BR/>
<BR/><BR/>
<p align="center"  style="font-size:24px"><b>中国工商银行</b><br/></p>
<p  align="center" style="font-size:24px"><b>信用卡汽车专项分期付款/担保合同</b></p>
<br/>
<p style="font-size:14.5px;" >分期款项发放人（抵押权人、质押权人、债权人）：中国工商银行_________________________</p>
<p style="font-size:14.5px;" >分期付款申请人（含共同申请人）：<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${name  !''}</SPAN></p>
<p style="font-size:14.5px;" >共同偿债人：_________________________________</p>
<p style="font-size:14.5px;" >抵押人（含抵押物共有人）：_________________________________</p>
<p style="font-size:14.5px;" >出质人（含质物公有人）：_________________________________</p>

<p align="center" style="font-size:14.5px;"><b>【重要提示】</b></p>
<p style="font-size:14.5px; text-indent:2em;line-height:1.7em;"><b>本合同系根据有关法律法规，在平等自愿的基础上协商订立而成，所有合同条款均是各方意思的
    真实表示。为充分维护各方的合法权益，分期款项发放人特提请各方在签订本合同前仔细阅读本合同
    条款，并特别关注本合同条款中的黑体字部分。各方在此确认已经充分理解了本合同所有条款尤其是
    黑体字部分的含义及相应的法律后果。</b></p><br/>
<p style="font-size:14.5px; text-indent:2em;line-height:1.7em;">本合同各方根据有关法律法规，在平等、自愿的基础上，为明确责任，恪守信用，签订本合同，
    并承诺共同遵守。</p><br/>
<p align="center" style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第一部分 基本约定</b></p>
<p style="font-size:14.5px; text-indent:2em;line-height:1.7em;">第一条&nbsp;分期付款申请人向汽车销售商<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${carDealer !''}</SPAN>购买汽车（具体品牌型号<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${carname !''}</SPAN>）,车辆总价<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${realPrice!''}</SPAN>为人民
    币（大写)<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${CARPRICE !''}</SPAN>元，分期付款申请人自行支付首付款<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${firstPayment !''}</SPAN>元，剩余购车款，分期付款申请人申请通过其在分期款项发放人申请办理的汽车专项分期业务以
    透支方式支付，透支金额为人民币（大写）<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${CREDITAMOUNT !''}</SPAN>元。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">第二条&nbsp;在同时满足以下条件的前提下，分期款项发放人同意按照第四条约定的方式，发放分期款
    项：</p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> 2.1&nbsp;分期付款申请人已经与汽车销售商签订了购车合同并以分期付款申请人自有资金支付分期款
    项发放人认可的首付款；</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> 2.2&nbsp;分期付款申请人已经就汽车专项分期中国工商银行信用卡的申办和使用与分期款项发放人签
    署了《牡丹信用卡领用合约（个人卡）》；</p>
<p style="font-size:14px;text-indent:2em;line-height:1.7em;"> 2.3&nbsp;分期付款申请人已经向分期款项发放人提供了分期款项发放人认可的合法有效的担保，并办
    理了有关担保手续。担保方式为<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">北京富择风险管理顾问有限公司全程连带保证担保、保证金质押担
        保和车辆抵押</SPAN>；</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">2.4&nbsp;分期付款申请人已经按照分期款项发放人要求提供了有关资料；</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">2.5&nbsp;其他。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">(1)________________________________________ </p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">(2)________________________________________ </p>
<p style="font-size:14.5px; text-indent:2em;line-height:1.7em;">第三条&nbsp;汽车专项分期款项为专用款项，除本合同第一条指定的用途外，不得用于其它任何用途。
    分期付款申请人根据本合同第四条约定使用信用卡直接支付的，分期付款申请人承诺向指定汽车经销
    商/合作机构<u>北京富择风险管理顾问有限公司</u>进行支付。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> <b>第四条&nbsp;分期付款申请人授权分期款项发放人按以下第<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">2</SPAN>种方式发放分期款项：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> 4.1&nbsp;在分期付款申请人使用用于汽车专项分期业务的中国工商银行信用卡进行刷卡并签署签购
    单记账扣款后，将上述透支资金一次性划付给汽车销售商（合作机构北京富择风险管理顾问有限公司）。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> <b>4.2&nbsp;分期付款申请人授权委托分期款项发放人<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${bankName !''}</SPAN>
    将用于汽车专项分期的中国工商银行信用卡（卡号______________）进行启用；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>授权分期款项发放人将汽车专项分期金额（大写）<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${CREDITAMOUNT !''}</SPAN>元，扣收后支付给该汽车销售
    商/合作机构<u>北京富择风险管理顾问有限公司</u>，入账账户信息如下：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"> <b> 账户名称：<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">北京富择风险管理顾问有限公司</SPAN></b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"> <b> 账&nbsp;&nbsp;号：<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">0200003419201441822</SPAN></b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"> <b> 开&nbsp;户行：<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${bankName !''}</SPAN></b></p><br/>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"> <b> 账户名称：________________________________________ </b> </p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">  <b>账&nbsp;&nbsp;号：________________________________________ </b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">  <b>开&nbsp;户行：________________________________________ </b></p><br/>

<p style="font-size:14.5px;text-indent:2em;line-height:1em;"> <b> 账户名称：________________________________________   </b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">  <b>账&nbsp;&nbsp;号：________________________________________  </b> </p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">  <b>开&nbsp;户行：________________________________________ </b></p><br/>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>分期付款申请人支付的个性化服务费金额（大写）________元。</b> </p>
<p style="font-size:14px;text-indent:2em;line-height:1.7em;"><b>第五条&nbsp;分期付款申请人使用用于汽车专项分期的中国工商银行信用卡透支支付购车消费款后，
    以按月分期等额方式向分期款项发放人偿还透支的资金。分期还款共分<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${months !''}</SPAN>期，首期偿还的金额为人
    民币<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${firstPayment !''}</SPAN>元，以后每期偿还的金额为人民币<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${monthlyPayments !''}</SPAN>元。分期付款申请人每期的透支款项应从
    透支次月起于每月的<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">15</SPAN>日前偿还。分期付款申请人应在汽车专项分期付款账户中按时足额存入每
    期须偿还的款项，并在此不可撤销地授权分期款项发放人从中直接扣款受偿。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">汽车专项分期每期扣款金额入账后，计息规则以及违约金等费用计收规则与普通消费相同。<b>
    分期付款申请人知悉并认可每月账单中的分期金额将全额计入该期账单的还款额，分期付款申请人需
    按账单所示金额足额还款，避免因未能及时足额还款而产生违约金。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第六条&nbsp;分期付款申请人应按<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">首付支付</SPAN>的方式及<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${correspondingRates!''}</SPAN>的分期付款手续费率向分期款项发放人
    支付分期付款手续费<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">${charge!''}</SPAN>元。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>手续费一经支付，除本合同另有约定的情形外，分期款项发放人均不向分期付款申请人退还手续
    费。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>收费相关咨询（投诉）请联系95588或分期款项发放人营业网点。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第七条&nbsp;如分期付款申请人没有按本合同约定及时足额存入还款资金，或者分期付款申请人用于
    汽车专项分期付款的账户被法院等有权机关采取冻结、扣划等强制措施导致分期款项发放人无法扣款
    受偿的，分期款项发放人有权按照《中国工商银行牡丹信用卡章程》以及《牡丹信用卡领用合约（个
    人卡）》规定向分期付款申请人收取透支利息、复利、违约金等。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第八条&nbsp;分期付款申请人的权利与义务</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>8.1&nbsp;按照本合同约定按时足额归还透支资金，承担本合同项下分期款项发放人为实现债权而支出
    的费用（包括但不限于诉讼费、律师费、催收费等）。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>8.2&nbsp;如实提供分期款项发放人所要求的资料，配合分期款项发放人调查、审查和检查，并对业务
    审查中提供材料的真实性、准确性、完整性与合法性负责。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>8.3&nbsp;在履行本合同的过程中应遵守本合同、《中国工商银行牡丹信用卡章程》以及《牡丹信用卡
    领用合约（个人卡）》的有关约定。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>8.4&nbsp;保证不将用于汽车专项分期的中国工商银行信用卡以及本合同项下获取的透支资金以任何
    形式用于本合同约定以外的其他用途。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>8.5&nbsp;汽车销售商对分期付款申请人所购车辆承担所有产品品质、服务品质等与产品有关的所有责
    任，分期款项发放人仅提供分期付款业务，与提供商品和服务的经销商之间无代理、经销关系。分期
    付款申请人保证不以其与汽车销售商之间的购车合同纠纷、争议为由影响其在本合同项下义务的履行。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>8.6&nbsp;当发生本项以下任一情况时，最迟应在事件发生后5日内以书面形式通知分期款项发放人：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（1）履行债务能力明显下降；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（2）涉及或可能涉及刑事案件或重大经济纠纷；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（3）住所、通讯地址、联系电话、工作单位发生变更；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（4）与汽车销售商提前解除或终止购车合同；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（5）其他影响或可能影响履行本合同项下有关义务的。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>8.7&nbsp;本合同项下之担保如发生了不利于分期款项发放人债权的变化，分期付款申请人应按照分期
    款项发放人要求及时另行 提供令分期款项发放人满意的担保。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>8.8&nbsp;在分期付款期间，对未记入账户的分期付款款项，可以办理展期，也可办理提前还款，但应
    经分期款项发放人书面同意。同一笔分期付款每天只允许办理一次提前还款；同一笔分期付款只能办
    理一次展期，展期期数应在分期款项发放人规定的最长期限范围内。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>8.9&nbsp;对分期款项发放人向其寄出或以其他方式送达的催收文件，应及时签收并在签收后3日内将
    回执寄还分期款项发放人。</b></p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第九条&nbsp;分期款项发放人的权利与义务</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>9.1&nbsp;在分期付款申请人满足本合同第二条约定的前提下，按照本合同约定提供透支资金，否则应
    退还已经收取的手续费。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>9.2&nbsp;对于分期付款申请人在本合同项下的所有应付未付款项，分期付款申请人同意并授权分期款
    项发放人从分期付款申请人开立于中国工商银行股份有限公司及其所有分支机构的任一账户中予以
    扣收。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>9.3&nbsp;在本合同有效期内发生以下任一事项的，有权要求分期付款申请人立即清偿透支款项、利息、
    手续费、违约金、实现债权的费用等全部债务，直至取消本合同项下的汽车专项分期，将全部未扣款
    项一次性计入分期付款申请人信用卡账户，且无须为正当行使上述权利所引起的任何损失负责：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（1）分期付款申请人违反本合同第八条的有关约定；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（2）分期付款申请人与汽车销售商提前解除或终止购车合同；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（3）分期付款申请人未按本合同约定支付手续费；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（4）分期付款申请人资信或财务状况发生恶化；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（5）分期付款申请人丧失或部分丧失民事行为能力；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（6）本合同项下之担保发生了不利于分期款项发放人债权的变化，且分期付款申请人未能按照
    分期款项发放人要求及时另行提供令分期款项发放人满意的担保；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（7）分期付款申请人以车辆抵押的，分期付款期间未能如期续保并将保险单正本交付分期款项
    发放人；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（8）分期付款申请人交易、还款记录情况异常或出现突发性欺诈风险等情况；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（9）发生其他可能影响分期款项发放人实现债权的事件。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>9.4当分期付款申请人发生拖欠本合同项下债务或其他违约行为时，分期款项发放人有权向有关
    部门或单位通报，或采取将分期付款申请人违约信息公布在中国工商银行网站等公告催收方式。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">第十条&nbsp;用于汽车专项分期的中国工商银行信用卡遗失、毁损、被盗或因其他原因不能使用
    时，分期付款申请人应及时通过电话银行、网上银行或分期款项发放人指定营业网点办理查询、挂失手续，
    并可通过电话银行或分期款项发放人指定营业网点办理补办或换领手续，本合同有关约定自动及于分
    期付款申请人补办或换领的新卡，分期款项发放人与分期付款申请人不再另行签署补充协议。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">第十一条&nbsp;《中国工商银行信用卡汽车专项分期付款业务申请表》、《中国工商银行牡丹信用卡
    章程》、《牡丹信用卡领用合约（个人卡）》为本合同附件，系本合同有效组成部分。附件约定与本
    合同条款不一致的，以本合同约定为准；本合同未尽事宜，按上述附件约定执行。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b> 第十二条&nbsp;分期付款共同申请人/共同偿债人</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b> 12.1&nbsp;本合同如涉及二人以上（含）分期付款共同申请，任一分期付款申请人均应履行本合同项
    下义务，对全部分期款项承担连带清偿责任，分期款项放款人有权向任一分期付款申请人追索未偿还
    的分期付款款项。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>12.2&nbsp;本合同如涉及共同偿债人，对本合同项下的债务承担无条件、不可撤销共同偿债责任。如
    分期付款申请人未能按照上述合同约定清偿债务，分期款项发放人有权直接要求共同偿债人进行清偿，
    共同偿债人未按分期款项发放人的要求进行清偿的，共同偿债人同意并授权分期款项发放人从共同偿
    债人开立于中国工商银行股份有限公司及其所有分支机构的任一账户中扣划有关款项。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">本合同涉及的共同偿债人的责任属于债务加入而非保证担保。</p><br/><br/>
<p  align="center" style="font-size:14.5px;line-height:1.7em;"><b>第二部分 具体条款</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">本合同项下信用卡汽车专项分期由分期付款申请人或第三方提供抵押担保时，抵押人承诺并遵守
    如下条款：</p>
<p style="font-size:14.5px;text-indent:2em;"><b>第十三条&nbsp;&nbsp;抵押物</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">13.1&nbsp;抵押人自愿向分期款项发放人提供抵押担保，抵押物详见《抵押物清单》。《抵押物清单》
    作为本合同附件，与本合同具有同等法律效力。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">13.2&nbsp;《抵押物清单》对抵押物价值的约定，不作为分期款项发放人处分该抵押物时的估价依据，
    不对分期款项发放人行使抵押权构成任何限制。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>13.3&nbsp;分期款项发放人抵押权的效力及于抵押物产生的孳息、抵押物的从物、从权利、附属物、
    添附物，以及因抵押物毁损、灭失或被征用等而产生的保险金、赔偿金、补偿金或其他形式的替代物。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">13.4&nbsp;抵押人应妥善保管和使用抵押物，保证抵押物的完好，分期款项发放人有权随时检查抵押
    物的使用管理情况,抵押人应予配合。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">13.5&nbsp;本合同签订后，抵押权设立前，抵押人出租抵押物的，应当经分期款项发放人书面同意；
    抵押权存续期间，抵押人出租抵押物的，应当通知分期款项发放人并将抵押事实告知承租人，且出租
    期限不得长于贷款期限。本合同签订后，抵押人转让抵押物、为第三方设立担保或以其他方式处分抵
    押物的，应事先征得分期款项发放人书面同意，并将抵押事实告知买受人等相关人员。由此取得的收
    益，按照第十三条13.9项的约定处理。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">13.6&nbsp;抵押物发生或可能发生毁损、灭失的，抵押人应及时告知分期款项发放人，并立即采取措
    施防止损失扩大，及时向分期款项发放人提交有关主管机关出具的毁损、灭失证明。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em; ">13.7&nbsp;抵押房屋被划入拆迁范围，抵押人和分期付款申请人需在拆迁公告后10日内告知分期款
    项发放人，分期款项发放人有权要求分期付款申请人和抵押人提供新的担保，抵押人和分期付款申请
    人未及时履行告知义务，或者尽管告知了分期款项发放人，但未向分期款项发放人另行提供分期款项
    发放人接受的担保，亦未按照分期款项发放人要求提前清偿分期付款申请人在本合同项下全部分期债
    务的，分期款项发放人有权利要求分期付款申请人提前归还全部分期款项，要求抵押人以拆迁补偿款
    归还分期付款申请人在本合同项下的全部分期债务。</p>
<p style="font-size:14.5px;text-indent:2em; line-height:1.7em;"><b>13.8&nbsp;抵押人的行为足以使抵押物价值减少的，应立即停止其行为；造成抵押物价值减少时，抵
    押人应及时恢复抵押物的价值，或提供与减少的价值相当的担保。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>13.9&nbsp;抵押人所获得的抵押物的赔偿金、补偿金、保险金以及处分抵押物所得收益，抵押人同意
    分期款项发放人可选择下列方式进行处理：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>A、清偿或提前清偿分期付款申请人在本合同项下的债务；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>B、存入分期款项发放人指定帐户，以担保分期付款申请人债务的履行；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>C、用于修复抵押物，以恢复抵押物的价值；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>D、双方另行约定的其他方式。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">抵押人或分期付款申请人提供符合分期款项发放人要求的新担保的，抵押人可将本项上述价款
    自由处分。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">13.10&nbsp;抵押人因隐瞒抵押物存在权属争议、被查封、被扣押、已设定抵押或已出租等情况，
    而给分期款项发放人造成损失的，应向分期款项发放人予以足额赔偿。</p>
<p style="font-size:14.5px;text-indent:2em;"><b>第十四条&nbsp;抵押担保范围</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">抵押担保的范围包括但不限于透支本金、利息、手续费、违约金、实现债权的费用和申请人所有
    其他应付费用。</p>
<p style="font-size:14.5px;text-indent:2em;"><b>第十五条&nbsp;抵押登记</b></p>
<p style="font-size:14px;text-indent:2em;line-height:1.7em;">15.1&nbsp;本合同签订后，抵押人应及时到有关登记机关办理抵押登记手续，抵押登记证明在汽车分
    期全部还清之前由分期款项发放人保管；抵押登记事项发生变化依法需进行变更登记的，抵押人应及
    时办理变更登记;分期款项发放人应对抵押人办理有关登记事项给予必要协助和配合。抵押人将抵押
    登记变更为最高额抵押登记的，须另行向分期款项发放人提出书面申请。<b>抵押人在放款后<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">25</SPAN>天内未
        办理抵押登记手续，分期款项发放人有权宣布合同项下债务提请到期，汽车分期申请人应立即清偿
        透支款项、利息、手续费、违约金、实现债权的费用等全部债务。本合同项下抵押登记费用由抵押人承
        担,法律法规另有规定的除外。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">15.2&nbsp;分期付款申请人按照合同约定履行完毕各项义务后，分期款项发放人应积极协助抵押人办
    理注销登记手续。</p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> <b>第十六条&nbsp;保险</b></p>
<p style="font-size:14px;text-indent:2em;line-height:1.7em;"><b>16.1</b>&nbsp;本合同签订后<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">15</SPAN>日内，抵押人方应按分期款项发放人要求办妥车辆保险手续，投保险种
    须包括但不限于交通强制险、车辆损失险、第三者责任险、<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">车辆盗抢险</SPAN>。抵押人须以车辆全价投保，
    投保期限不得短于汽车分期期限，可一次性趸缴或逐年投保。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">如果因保险机构的原因无法一次性办妥的，抵押人应及时办理续保手续，确保本合同有效期内抵
    押物的财产保险不间断。本合同项下的保险费用由抵押人承担。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> 16.2&nbsp;保险单中应当注明，出险时分期款项发放人为优先受偿人（第一受益人），保险人应直接
    将保险金支付给分期款项发放人。保险单中不应有任何限制分期款项发放人权益的条款。在主债权履
    行完毕前，保险单正本由分期款项发放人保管。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b> 16.3&nbsp;在本合同有效期内，抵押人不得以任何理由中断或撤销保险；如保险中断，分期款项发放
    人有权要求分期付款申请人立即清偿透支款项、利息、手续费、违约金、实现债权的费用等全部债务，
    直至取消本合同项下的汽车专项分期付款，将全部未扣款项一次性计入分期付款申请人信用卡账户，
    且无须为正当行使上述权利所引起的任何损失负责。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">16.4&nbsp;在本合同有效期内，抵押物如发生保险事故，保险赔偿金按照第十三条13.9项的约定处理。</p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第十七条&nbsp;抵押权的实现</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>17.1&nbsp;发生下列情形之一，分期款项发放人可以与抵押人协商以抵押物折价或者拍卖、变卖抵押
    物，以所得价款优先受偿或按第十三条13.9项约定的其他方式处理；未就抵押权实现方式达成一致
    的，分期款项发放人可请求人民法院拍卖、变卖抵押物。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（1）主债权到期（包括被宣布提前到期）分期付款申请人未予清偿；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（2）发生第十三条13.8、13.9、13.10项所述情形，抵押人未及时恢复抵押物的价值或提供与
    减少的价值相当的担保;</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（3）分期款项发放人依法可以处分抵押物的其他情形。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>17.2&nbsp;分期款项发放人处分抵押物时，抵押人应予积极配合。对本合同项下依法设定抵押的抵押
    人及其所扶养家属居住的房屋，在人民法院裁定或判决拍卖、变卖或者抵债后，抵押人应当在六个月
    内主动腾空房屋。如果分期款项发放人同意向抵押人提供临时住房，抵押人应当向分期款项发放人支
    付租金；已经产生的租金，应当从房屋拍卖或者变卖价款中优先扣除。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>17.3&nbsp;分期款项发放人在处分抵押物过程中所产生的实现债权的费用，应当从拍卖或者变卖价款
    中优先扣除。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第十八条&nbsp;抵押人陈述与保证</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">抵押人向分期款项发放人作如下陈述与保证：</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">18.1&nbsp;抵押人是抵押物的所有人，并享有对抵押物的完全处分权，抵押物不存在任何所有权、使
    用权方面的争议。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">18.2&nbsp;抵押物不属于共有财产，或虽属于共有财产但已就抵押事项征得共有人书面同意。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第十九条&nbsp;抵押人承诺</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">19.1&nbsp;未经分期款项发放人书面同意，不将抵押物再设立任何形式的抵押、质押，也不将抵押物
    出租、转让、馈赠给任何第三人，并保护抵押物不受任何侵害。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>19.2&nbsp;承担分期款项发放人为实现本合同项下债权及担保权利而产生费用，包括但不限于律师费、
    评估费、拍卖费等。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">19.3&nbsp;分期款项发放人抵押权受到或可能受到来自任何第三方的侵害时，及时通知并协助分期款
    项发放人免受侵害。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">19.4&nbsp;分期款项发放人实现抵押权时给予积极配合，不设置任何障碍限制分期款项发放人抵押
    权的行使。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">19.5&nbsp;发生下列情形之一，及时通知分期款项发放人：</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">（1）抵押物涉及或可能涉及重大经济纠纷、诉讼、仲裁，或财产被依法查封、扣押或监管；</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">（2）住所、工作单位、联系方式等发生变更。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">19.6&nbsp;对分期款项发放人发出的书面通知及时签收。</p>

<p style="font-size:14.5px;text-indent:2em;"> <b>第二十条&nbsp;分期款项发放人承诺</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">20.1&nbsp;对分期付款申请人在履行本合同项下义务时所提交的有关文件、财务资料及其他相关资料
    中的未公开信息保密，但法律、法规及规章另有规定的除外。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">20.2&nbsp;处分抵押物的所得在偿还抵押担保范围内的全部债务后还有剩余的，将剩余部分及时退还
    抵押人。</p><br/>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">本合同项下分期款项由分期付款申请人或第三方提供质押担保时，出质人承诺并遵守如下条款：</p>

<p style="font-size:14.5px;text-indent:2em;"><b>第二十一条&nbsp;质物</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">21.1&nbsp;出质人自愿为分期付款申请人在本合同项下的债务向分期款项发放人提供质押担保，质物
    详见本合同附件《质物清单》。《质物清单》作为本合同附件，与本合同具有同等法律效力。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> 21.2&nbsp;《质物清单》对质物价值的约定，不作为分期款项发放人处分质物时的估价依据，不对分
    期款项发放人行使质权构成任何限制。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">21.3&nbsp;分期款项发放人质权的效力及于质物所生的孳息，以及因质物毁损、灭失或被征用等而产
    生的保险金、赔偿金、补偿金或其他形式的替代物。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>21.4&nbsp;质物价值发生了不利于分期付款债权的变动的，分期款项发放人有权要求分期付款申请人
    或出质人补足质物价值或另行提供相应担保，分期付款申请人或出质人应在分期款项发放人要求的时
    间内补足质物价值或另行提供相应担保。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>21.5&nbsp;分期付款存续期间，根据质物价值与分期付款余额的比率，本合同项下质物设定下列警戒
    线和平仓线：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>外币存款、个人外汇资金的预警线为1.1，平仓线为1.05；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>基金的预警线为1.2，平仓线为1.1。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>黄金和外币个人理财产品的预警线为1.1，平仓线为1.05。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>当质物价值下降到警戒线时，出质人应当在分期款项发放人要求的期限内追加担保以补足因质物
    价值下降造成的质押价值缺口；当质物价值下降到平仓线时，出质人同意分期款项发放人处置质物
    并以所得价款优先受偿。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>出质人同意分期款项发放人可以调整警戒线和平仓线，调整时通过中国工商银行股份有限公司网
    站予以公告。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>21.6&nbsp;出质人质权的效力及于质物所生的孳息，以及因质物毁损、灭失或被征用等而产生的保险
    金、赔偿金、补偿金或其他形式的替代物。质物为基金时，质押效力及于质押基金在质押期间产生的
    法定孳息以及因质押基金份额拆分所导致的基金份额增加部分。如用于质押的基金分红方式为现金分
    红的，出质人授权分期款项发放人在质押期间将分红形式变更为红利再投资。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>21.7&nbsp;分期款项发放人负有妥善保管质物的义务，因保管不善造成质物灭失或者毁损的，分期款
    项发放人应当承担民事责任。分期款项发放人不能妥善保管质物可能使其灭失或者毁损的，出质人可
    以要求分期款项发放人将质物提存，或要求提前清偿债务而返还质物。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>21.8&nbsp;出质人所获得的质物的赔偿金、补偿金、保险金以及处分质物所得收益，出质人同意分期
    款项发放人可选择下列方式进行处理：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>(1)清偿或提前清偿分期付款申请人在本合同项下的债务；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>(2)存入分期款项发放人指定帐户，以担保本合同项下债务的履行；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>(3)用于修复质物，以恢复质物的价值；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>(4)双方另行约定的其他方式。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;">出质人或分期付款申请人提供符合分期款项发放人要求的新担保的，出质人可将上述价款自由处
    分。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第二十二条&nbsp;质押担保范围</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">质押担保范围包括但不限于透支本金、利息、手续费、违约金、实现债权的费用和申请人所有其
    他应付费用。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第二十三条&nbsp;交付和登记</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">23.1&nbsp;本合同签订后，出质人应将质物或权利凭证交付分期款项发放人，分期款项发放人验收无
    误后应向出质人出具收押凭据。 </p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">23.2&nbsp;本合同项下质物依法须办理质押登记的，出质人和分期款项发放人应及时到有关登记机关
    办理质押登记手续；登记事项发生变化依法需进行变更登记的，出质人和分期款项发放人应及时办理
    变更登记。分期付款申请人按照合同约定履行完毕各项义务后，分期款项发放人应积极协助出质人办
    理注销登记手续，并将相关权属文件退还出质人。本合同项下质押登记费用由出质人承担,法律法规
    另有规定的除外。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">第二十四条&nbsp;质物的处分</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>24.1&nbsp;发生下列情形之一，分期款项发放人有权将质物拍卖、变卖、兑现、提现，以所得价款优
    先受偿或按第二十一条21.7项约定的其他方式处理；或经与出质人协商将质物折价以抵偿分期付款
    申请人所欠债务：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（1）分期付款到期（包括被宣布提前到期）分期付款申请人未予清偿的；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（2）发生第二十一条21.4条所述情形，出质人或分期付款申请人未按分期款项发放人要求补足
    质物价值的；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（3）质物价值下降到第二十一条21.5条约定的警戒线，出质人未按分期款项发放人要求追加担
    保，或质物价值下降到第二十一条21.5条约定的平仓线的；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（4）分期款项发放人与出质人约定将质物兑现或提现偿还到期债务；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1em;"><b>（5）分期款项发放人依法可以处分质物的其他情形。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>24.2&nbsp;质物的兑现或提现日期先于债务履行期届满日的,分期款项发放人可以在质物到期日兑现
    或提现，并与出质人协商,将所得价款提前清偿所担保的债权或存入分期款项发放人指定账户，以担
    保分期付款申请人债务的履行。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>24.3&nbsp;质物的兑现或提现日期后于债务履行期届满日,出质人授权分期款项发放人在实现债权时
    可提前兑现或提现并以所得价款清偿债务，因提前兑现或提现产生的损失由出质人承担。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">24.4&nbsp;处分质物的所得在偿还质押担保范围内分期付款申请人的全部债务后还有剩余的，分期款
    项发放人应将剩余部分及时退还出质人。</p><br/>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> <b>第二十五条&nbsp;费用</b></p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>分期款项发放人为实现本合同项下债权及担保权利而产生费用，包括但不限于律师费、评估费、
    拍卖费等由分期付款申请人承担。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第二十六条&nbsp;担保承诺</b></p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>26.1&nbsp;发生下列情形之一，无需经担保人同意，担保人仍继续按照本合同的约定履行担保责任：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（1）因中国人民银行调整利率政策而导致主债权数额发生变化的；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（2）分期款项发放人将其在本合同项下的权利转让给任何其他方的。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>26.2&nbsp;如果分期款项发放人主债权存在其他担保的，不论该担保是由债务人提供还是由第三人提
    供，分期款项发放人有权自行决定实现担保的顺序，分期付款申请人承诺不因此而提出抗辩。分期款
    项发放人放弃、变更或丧失本合同项下其他担保权益的，担保人的担保责任仍持续有效，不因此而无
    效或减免。</b></p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第二十七条&nbsp;违约及违约责任</b></p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>27.1&nbsp;发生下列情形之一的，分期付款发放人可以采取第二十六条26.2项所列措施：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（1）分期付款申请人未完全、适当地遵守或履行其在本合同项下的任何承诺、保证、义务或责
    任；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（2）分期付款申请人或担保人提供虚假资料或隐瞒重要事实；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（3）分期付款申请人死亡或被宣告死亡、失踪或被宣告失踪，或者成为限制民事行为能力人或
    丧失民事行为能力，而无继承人、受遗赠人、监护人或财产代管人，或其继承人、受遗赠人、监护人
    或财产代管人拒绝代分期付款申请人履行本合同项下义务的；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（4）分期付款申请人涉及刑事案件、诉讼、仲裁、纠纷或分期付款申请人因被羁押、刑事拘留、
    劳动教养等被限制人身自由的；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（5）分期付款申请人的其他任何债务在到期（包括被宣布提前到期）后未予清偿，或者分期付
    款申请人不履行其应当承担的担保责任或其他义务，已经或可能影响分期付款申请人在本合同项下义
    务的履行的；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（6）本合同项下担保发生了不利于分期款项发放人债权的变化且未按照分期款项发放人要求恢
    复担保财产价值或另行提供分期款项发放人认可的新的担保的；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（7）抵押房屋被有权机关查封、被人民法院或仲裁委员会裁决以物抵债、被裁决给第三人的；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>27.2  发生第二十六条26.1项约定情形的，分期款项发放人可以采取下列一项或多项措施：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（1）要求分期付款申请人限期纠正违约行为；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（2）要求分期付款申请人另行提供分期款项发放人认可的担保；</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（3）要求分期付款申请人立即清偿透支款项、利息、手续费、违约金、实现债权的费用等全部
    债务，直至取消本合同项下的汽车专项分期，将全部未扣款项一次性计入分期付款申请人信用卡账户。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>（4） 法律法规规定、本合同约定或分期款项发放人认为必要的其他措施。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">27.3&nbsp;本合同生效后，任何一方不履行其在本合同项下的任何义务，或违背其在本合同项下所作
    的任何陈述、保证与承诺的，即构成违约。因此而给对方造成损失的，应予赔\偿，违约方按______________
    的标准支付违约金。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">27.4&nbsp;除本合同另有约定外，任何一方违约，其他方有权采取中华人民共和国法律法规及规章规
    定的其他任何措施。</p>
<p style="font-size:14.5px;text-indent:2em;"><b>第二十八条&nbsp;公证</b></p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">经各方协商，各方可对本合同进行强制执行公证。分期付款申请人和担保人同意本合同经公证后
    具有强制执行效力，分期付款申请人或担保人不履行本合同项下义务的，分期款项发放人可以依法向
    有管辖权的人民法院申请执行。</p><br/>
<p style="font-size:14.5px;px;text-indent:2em;line-height:1.7em;"> 第二十九条&nbsp;生效、变更和解除</p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> 29.1&nbsp;本合同自各方当事人签订（签字/盖章）之日起生效，至分期付款申请人清偿本合同项下全
    部债务时终止。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">29.2&nbsp;本合同的任何变更须由分期款项发放人、分期付款申请人与合同当事人_____________
    ___________________________等各方共同协商一致并以书面形式作出。变更条款或协议
    构成本合同的一部分，与本合同具有同等法律效力。除变更部分外，本合同其余部分依然有效，变更
    部分生效前本合同原条款仍然有效。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> 29.3&nbsp;本合同任何条款的无效或不可执行，不影响其他条款的有效性和可执行性，也不影响整个
    合同的效力。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> 29.4&nbsp;本合同的变更和解除，不影响缔约各方要求赔偿损失的权利。本合同的解除，不影响本合
    同中有关争议解决条款的效力。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">本合同一式<SPAN style="TEXT-DECORATION: underline;border-bottom:1px solid black">4</SPAN>份，分期款项发放人、分期付款申请人与合同当事人_________________
    等各方各执一份，具有同等法律效力。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"> <b>第三十条&nbsp;争议解决</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>本合同履行过程中发生纠纷，各方应积极协商解决；协商不成，按下列第___种方式解决：</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>30.1提交_____仲裁委员会在_____进行仲裁。</b> </p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>30.2向分期款项发放人所在地人民法院提起诉讼。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>30.3向合同签订地人民法院提起诉讼（合同签订地：_______）。</b></p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>30.4其他：_____________ </b></p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;"><b>第三十一条&nbsp;其他</b></p>

<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">31.1&nbsp;未经分期款项发放人书面同意，抵押人、出质人、保证人、分期付款申请人，分期付款共
    同申请人/共同偿债人不得转让其在本合同项下的全部或部分权利或义务。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">31.2&nbsp;分期款项发放人未行使或部分行使或迟延行使本合同项下的任何权利，不构成对该权利或
    任何其他权利的放弃或变更，也不影响其进一步行使该权利或任何其他权利。</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">31.3&nbsp;分期付款申请人、分期付款共同申请人、担保人、共同偿债人（本合同项下统称为授权人）
    同意向分期款项发放人作如下授权：</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">（1）为本合同订立和履行之目的，自本合同生效之日至本合同项下义务履行完毕之日，分期款
    项发放人有权要求授权人提供分期款项发放人需要的相关信息，并在合同履行及贷后管理过程中有权
    通过金融信用信息基础数据库和其它依法设立的信用数据库查询、使用授权人的信用报告和相关信息；</p>
<p style="font-size:14.5px;text-indent:2em;line-height:1.7em;">（2）分期款项发放人有权依据相关法律法规或其它规范性文件或金融监管机构要求，将与本合
    同有关的信息（包括授权人违约等不良信息）和其他相关信息（包括授权人及配偶、身份、职业和居
    住地址等个人基本信息，在个人贷款、各类信用卡和对外担保等信用活动中形成的交易记录等个人信
    贷交易信息、个人住房公积金（缴存）、个人养老保险金（缴存）、个人养老保险（缴存）、个人电
    信缴存等相关信用信息）提供给金融信用信息基础数据库和其他设立的信用数据库。</p>
<p style="font-size:14.5px;px"><b>第三十二条&nbsp;合同当事人约定的其他事项</b></p>

<p style="font-size:16px;line-height:1.7em;">32.1 ______________________________________________________</p>

<p class="tab" style="font-size:16px;line-height:1.7em;">32.2 ______________________________________________________</p>


<div class="tab" >
    <p style="font-size: 20px;">附件1：</p>
    <br/>
    <p align="center" style="font-size: 25px;">中国工商银行<br/>信用卡汽车专项分期付款业务抵押物清单</p>
    <p align="right" style="font-size:14.5px;">单位：平方米，元</p>
    <table>
        <tr>
            <th align="center" colspan="5">汽车抵押</th>
        </tr>
        <tr>
            <td>车牌号码</td>
            <td>京${licenseNumber!''}</td>
            <td>车辆类型</td>
            <td></td>
        </tr>
        <tr>
            <td>厂牌型号</td>
            <td>${carBrand!''}</td>
            <td>车架号码</td>
            <td>${vin!''}</td>
        </tr>
        <tr>
            <td>发动机号</td>
            <td></td>
            <td>购车价款</td>
            <td>${realPrice !''}</td>
        </tr>
        <tr>
            <td style="height: 66px;">机动车登记证书编号</td>
            <td colspan="3"></td>
        </tr>
    </table>
    <table>
        <tr>
            <th align="center" colspan="5">房产抵押</th>
        </tr>
        <tr>
            <td>房产抵押物</td>
            <td>抵押人</td>
            <td>建筑面积</td>
            <td>抵押物价值</td>
            <td>房产权属证书（编号）</td>
        </tr>
        <tr>
            <td>房产1</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>房产2</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>房产1地址</td>
            <td colspan="4"></td>
        </tr>
        <tr>
            <td>房产2地址</td>
            <td colspan="4"></td>
        </tr>
    </table>
</div>

</body>
</html>
