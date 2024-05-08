package com.fuze.bcp.api.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 个人对外投资
 * Created by Lily on 2017/8/3.
 */
public class PersonalInvestmentBean implements Serializable {
    /**
     * code : 10000
     * charge : false
     * msg : 查询成功
     * result : {"resp_data":{"body":{"ryPosFRList":[{"ryName":"查询人姓名","entName":" 企业 (机构 )名称","regNo":" 注册号","entType":"企业 (机构 )类型","regCap":"注册资本 (万元 )","regCapCur":"注册资本币种","entStatus":"企业状态"}],"ryPosShaList":[{"ryName":"查询人姓名","entName":"企业 (机构 )名称","regNo":"注册号","entType":"企业 (机构 )类型","regCap":"注册资本 (万元 )","regCapCur":"注册资本币种","subconam":"认缴出资额(万元)","currentcy":"认缴出资币种","entStatus":"企业状态"}],"ryPosPerList":[{"ryName":"查询人姓名","entName":"企业 (机构 )名称","regNo":"注册号","entType":"企业 (机构 )类型","regCap":"注册资本 (万元 )","regCapCur":"注册资本币种","entStatus":"企业状态","position":"职务"}],"punishBreakList":[{"caseCode":"案号","iNameClean":"被执行人姓名/名称","type":"失信人类型","sexyClean":"性别","ageClean":"年龄","cardNum":"身份证号码","ysfzd":"身份证原始发地","businessEntity":"法定代表人/负责人姓名","regDateClean":"立案时间","publishDateClean":"公布时间","courtName":"执行法院","areaNameClean":"省份","gistID":"执行依据文号","gistUnit":"做出执行依据单位","duty":"生效法律文书确定的义务","disruptTypeName":"失信被执行人为具体情形","performance":"被执行人的履情况","performedPart":"已履行","unperformPart":"未履行","focusNumber":"关注次数"}],"punishedList":[{"caseCode":"案号","iNameClean":"被执行人姓名/名称","cardNumClean":"身份证号码/注册号","sexyClean":"性别","ageClean":"年龄","areaNameClean":"省份","ysfzd":"身份证原始发地","courtName":"执行法院","regDateClean":"立案时间","caseState":"案件状态","execMoney":"执行标的","focusNumber":"关注次数"}],"aliDebtList":[{"iNameClean":"欠贷人姓名/名称","areaNameClean":"省份","cardNumClean":"身份证号码/企业注册号","ysfzd":"身份证原始发地","sexyClean":"性别","qked":"欠款额度","ageClean":"年龄","wyqk":"违约情况","dkdqsj":"贷款到期时间","legalPerson":"法定代表人","tbzh":"淘宝账户","dkqx":"贷款期限"}]},"code":"1000","msg":"请求成功"}}
     */

    private String code;
    private Boolean charge;
    private String msg;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isCharge() {
        return charge;
    }

    public void setCharge(Boolean charge) {
        this.charge = charge;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public class ResultBean implements Serializable {
        /**
         * resp_data : {"body":{"ryPosFRList":[{"ryName":"查询人姓名","entName":" 企业 (机构 )名称","regNo":" 注册号","entType":"企业 (机构 )类型","regCap":"注册资本 (万元 )","regCapCur":"注册资本币种","entStatus":"企业状态"}],"ryPosShaList":[{"ryName":"查询人姓名","entName":"企业 (机构 )名称","regNo":"注册号","entType":"企业 (机构 )类型","regCap":"注册资本 (万元 )","regCapCur":"注册资本币种","subconam":"认缴出资额(万元)","currentcy":"认缴出资币种","entStatus":"企业状态"}],"ryPosPerList":[{"ryName":"查询人姓名","entName":"企业 (机构 )名称","regNo":"注册号","entType":"企业 (机构 )类型","regCap":"注册资本 (万元 )","regCapCur":"注册资本币种","entStatus":"企业状态","position":"职务"}],"punishBreakList":[{"caseCode":"案号","iNameClean":"被执行人姓名/名称","type":"失信人类型","sexyClean":"性别","ageClean":"年龄","cardNum":"身份证号码","ysfzd":"身份证原始发地","businessEntity":"法定代表人/负责人姓名","regDateClean":"立案时间","publishDateClean":"公布时间","courtName":"执行法院","areaNameClean":"省份","gistID":"执行依据文号","gistUnit":"做出执行依据单位","duty":"生效法律文书确定的义务","disruptTypeName":"失信被执行人为具体情形","performance":"被执行人的履情况","performedPart":"已履行","unperformPart":"未履行","focusNumber":"关注次数"}],"punishedList":[{"caseCode":"案号","iNameClean":"被执行人姓名/名称","cardNumClean":"身份证号码/注册号","sexyClean":"性别","ageClean":"年龄","areaNameClean":"省份","ysfzd":"身份证原始发地","courtName":"执行法院","regDateClean":"立案时间","caseState":"案件状态","execMoney":"执行标的","focusNumber":"关注次数"}],"aliDebtList":[{"iNameClean":"欠贷人姓名/名称","areaNameClean":"省份","cardNumClean":"身份证号码/企业注册号","ysfzd":"身份证原始发地","sexyClean":"性别","qked":"欠款额度","ageClean":"年龄","wyqk":"违约情况","dkdqsj":"贷款到期时间","legalPerson":"法定代表人","tbzh":"淘宝账户","dkqx":"贷款期限"}]},"code":"1000","msg":"请求成功"}
         */

        private RespDataBean resp_data;

        public RespDataBean getResp_data() {
            return resp_data;
        }

        public void setResp_data(RespDataBean resp_data) {
            this.resp_data = resp_data;
        }

        public class RespDataBean implements Serializable{
            /**
             * body : {"ryPosFRList":[{"ryName":"查询人姓名","entName":" 企业 (机构 )名称","regNo":" 注册号","entType":"企业 (机构 )类型","regCap":"注册资本 (万元 )","regCapCur":"注册资本币种","entStatus":"企业状态"}],"ryPosShaList":[{"ryName":"查询人姓名","entName":"企业 (机构 )名称","regNo":"注册号","entType":"企业 (机构 )类型","regCap":"注册资本 (万元 )","regCapCur":"注册资本币种","subconam":"认缴出资额(万元)","currentcy":"认缴出资币种","entStatus":"企业状态"}],"ryPosPerList":[{"ryName":"查询人姓名","entName":"企业 (机构 )名称","regNo":"注册号","entType":"企业 (机构 )类型","regCap":"注册资本 (万元 )","regCapCur":"注册资本币种","entStatus":"企业状态","position":"职务"}],"punishBreakList":[{"caseCode":"案号","iNameClean":"被执行人姓名/名称","type":"失信人类型","sexyClean":"性别","ageClean":"年龄","cardNum":"身份证号码","ysfzd":"身份证原始发地","businessEntity":"法定代表人/负责人姓名","regDateClean":"立案时间","publishDateClean":"公布时间","courtName":"执行法院","areaNameClean":"省份","gistID":"执行依据文号","gistUnit":"做出执行依据单位","duty":"生效法律文书确定的义务","disruptTypeName":"失信被执行人为具体情形","performance":"被执行人的履情况","performedPart":"已履行","unperformPart":"未履行","focusNumber":"关注次数"}],"punishedList":[{"caseCode":"案号","iNameClean":"被执行人姓名/名称","cardNumClean":"身份证号码/注册号","sexyClean":"性别","ageClean":"年龄","areaNameClean":"省份","ysfzd":"身份证原始发地","courtName":"执行法院","regDateClean":"立案时间","caseState":"案件状态","execMoney":"执行标的","focusNumber":"关注次数"}],"aliDebtList":[{"iNameClean":"欠贷人姓名/名称","areaNameClean":"省份","cardNumClean":"身份证号码/企业注册号","ysfzd":"身份证原始发地","sexyClean":"性别","qked":"欠款额度","ageClean":"年龄","wyqk":"违约情况","dkdqsj":"贷款到期时间","legalPerson":"法定代表人","tbzh":"淘宝账户","dkqx":"贷款期限"}]}
             * code : 1000
             * msg : 请求成功
             */

            private BodyBean body;
            private String code;
            private String msg;

            public BodyBean getBody() {
                return body;
            }

            public void setBody(BodyBean body) {
                this.body = body;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public  class BodyBean implements Serializable{
                private List<RyPosFRListBean> ryPosFRList;
                private List<RyPosShaListBean> ryPosShaList;
                private List<RyPosPerListBean> ryPosPerList;
                private List<PunishBreakListBean> punishBreakList;
                private List<PunishedListBean> punishedList;
                private List<AliDebtListBean> aliDebtList;

                public List<RyPosFRListBean> getRyPosFRList() {
                    return ryPosFRList;
                }

                public void setRyPosFRList(List<RyPosFRListBean> ryPosFRList) {
                    this.ryPosFRList = ryPosFRList;
                }

                public List<RyPosShaListBean> getRyPosShaList() {
                    return ryPosShaList;
                }

                public void setRyPosShaList(List<RyPosShaListBean> ryPosShaList) {
                    this.ryPosShaList = ryPosShaList;
                }

                public List<RyPosPerListBean> getRyPosPerList() {
                    return ryPosPerList;
                }

                public void setRyPosPerList(List<RyPosPerListBean> ryPosPerList) {
                    this.ryPosPerList = ryPosPerList;
                }

                public List<PunishBreakListBean> getPunishBreakList() {
                    return punishBreakList;
                }

                public void setPunishBreakList(List<PunishBreakListBean> punishBreakList) {
                    this.punishBreakList = punishBreakList;
                }

                public List<PunishedListBean> getPunishedList() {
                    return punishedList;
                }

                public void setPunishedList(List<PunishedListBean> punishedList) {
                    this.punishedList = punishedList;
                }

                public List<AliDebtListBean> getAliDebtList() {
                    return aliDebtList;
                }

                public void setAliDebtList(List<AliDebtListBean> aliDebtList) {
                    this.aliDebtList = aliDebtList;
                }

                public  class RyPosFRListBean implements Serializable{
                    /**
                     * ryName : 查询人姓名
                     * entName :  企业 (机构 )名称
                     * regNo :  注册号
                     * entType : 企业 (机构 )类型
                     * regCap : 注册资本 (万元 )
                     * regCapCur : 注册资本币种
                     * entStatus : 企业状态
                     */

                    private String ryName;
                    private String entName;
                    private String regNo;
                    private String entType;
                    private String regCap;
                    private String regCapCur;
                    private String entStatus;

                    public String getRyName() {
                        return ryName;
                    }

                    public void setRyName(String ryName) {
                        this.ryName = ryName;
                    }

                    public String getEntName() {
                        return entName;
                    }

                    public void setEntName(String entName) {
                        this.entName = entName;
                    }

                    public String getRegNo() {
                        return regNo;
                    }

                    public void setRegNo(String regNo) {
                        this.regNo = regNo;
                    }

                    public String getEntType() {
                        return entType;
                    }

                    public void setEntType(String entType) {
                        this.entType = entType;
                    }

                    public String getRegCap() {
                        return regCap;
                    }

                    public void setRegCap(String regCap) {
                        this.regCap = regCap;
                    }

                    public String getRegCapCur() {
                        return regCapCur;
                    }

                    public void setRegCapCur(String regCapCur) {
                        this.regCapCur = regCapCur;
                    }

                    public String getEntStatus() {
                        return entStatus;
                    }

                    public void setEntStatus(String entStatus) {
                        this.entStatus = entStatus;
                    }
                }

                public  class RyPosShaListBean implements Serializable{
                    /**
                     * ryName : 查询人姓名
                     * entName : 企业 (机构 )名称
                     * regNo : 注册号
                     * entType : 企业 (机构 )类型
                     * regCap : 注册资本 (万元 )
                     * regCapCur : 注册资本币种
                     * subconam : 认缴出资额(万元)
                     * currentcy : 认缴出资币种
                     * entStatus : 企业状态
                     */

                    private String ryName;
                    private String entName;
                    private String regNo;
                    private String entType;
                    private String regCap;
                    private String regCapCur;
                    private String subconam;
                    private String currentcy;
                    private String entStatus;

                    public String getRyName() {
                        return ryName;
                    }

                    public void setRyName(String ryName) {
                        this.ryName = ryName;
                    }

                    public String getEntName() {
                        return entName;
                    }

                    public void setEntName(String entName) {
                        this.entName = entName;
                    }

                    public String getRegNo() {
                        return regNo;
                    }

                    public void setRegNo(String regNo) {
                        this.regNo = regNo;
                    }

                    public String getEntType() {
                        return entType;
                    }

                    public void setEntType(String entType) {
                        this.entType = entType;
                    }

                    public String getRegCap() {
                        return regCap;
                    }

                    public void setRegCap(String regCap) {
                        this.regCap = regCap;
                    }

                    public String getRegCapCur() {
                        return regCapCur;
                    }

                    public void setRegCapCur(String regCapCur) {
                        this.regCapCur = regCapCur;
                    }

                    public String getSubconam() {
                        return subconam;
                    }

                    public void setSubconam(String subconam) {
                        this.subconam = subconam;
                    }

                    public String getCurrentcy() {
                        return currentcy;
                    }

                    public void setCurrentcy(String currentcy) {
                        this.currentcy = currentcy;
                    }

                    public String getEntStatus() {
                        return entStatus;
                    }

                    public void setEntStatus(String entStatus) {
                        this.entStatus = entStatus;
                    }
                }

                public  class RyPosPerListBean implements Serializable{
                    /**
                     * ryName : 查询人姓名
                     * entName : 企业 (机构 )名称
                     * regNo : 注册号
                     * entType : 企业 (机构 )类型
                     * regCap : 注册资本 (万元 )
                     * regCapCur : 注册资本币种
                     * entStatus : 企业状态
                     * position : 职务
                     */

                    private String ryName;
                    private String entName;
                    private String regNo;
                    private String entType;
                    private String regCap;
                    private String regCapCur;
                    private String entStatus;
                    private String position;

                    public String getRyName() {
                        return ryName;
                    }

                    public void setRyName(String ryName) {
                        this.ryName = ryName;
                    }

                    public String getEntName() {
                        return entName;
                    }

                    public void setEntName(String entName) {
                        this.entName = entName;
                    }

                    public String getRegNo() {
                        return regNo;
                    }

                    public void setRegNo(String regNo) {
                        this.regNo = regNo;
                    }

                    public String getEntType() {
                        return entType;
                    }

                    public void setEntType(String entType) {
                        this.entType = entType;
                    }

                    public String getRegCap() {
                        return regCap;
                    }

                    public void setRegCap(String regCap) {
                        this.regCap = regCap;
                    }

                    public String getRegCapCur() {
                        return regCapCur;
                    }

                    public void setRegCapCur(String regCapCur) {
                        this.regCapCur = regCapCur;
                    }

                    public String getEntStatus() {
                        return entStatus;
                    }

                    public void setEntStatus(String entStatus) {
                        this.entStatus = entStatus;
                    }

                    public String getPosition() {
                        return position;
                    }

                    public void setPosition(String position) {
                        this.position = position;
                    }
                }

                public  class PunishBreakListBean implements Serializable{
                    /**
                     * caseCode : 案号
                     * iNameClean : 被执行人姓名/名称
                     * type : 失信人类型
                     * sexyClean : 性别
                     * ageClean : 年龄
                     * cardNum : 身份证号码
                     * ysfzd : 身份证原始发地
                     * businessEntity : 法定代表人/负责人姓名
                     * regDateClean : 立案时间
                     * publishDateClean : 公布时间
                     * courtName : 执行法院
                     * areaNameClean : 省份
                     * gistID : 执行依据文号
                     * gistUnit : 做出执行依据单位
                     * duty : 生效法律文书确定的义务
                     * disruptTypeName : 失信被执行人为具体情形
                     * performance : 被执行人的履情况
                     * performedPart : 已履行
                     * unperformPart : 未履行
                     * focusNumber : 关注次数
                     */

                    private String caseCode;
                    private String iNameClean;
                    private String type;
                    private String sexyClean;
                    private String ageClean;
                    private String cardNum;
                    private String ysfzd;
                    private String businessEntity;
                    private String regDateClean;
                    private String publishDateClean;
                    private String courtName;
                    private String areaNameClean;
                    private String gistID;
                    private String gistUnit;
                    private String duty;
                    private String disruptTypeName;
                    private String performance;
                    private String performedPart;
                    private String unperformPart;
                    private String focusNumber;

                    public String getCaseCode() {
                        return caseCode;
                    }

                    public void setCaseCode(String caseCode) {
                        this.caseCode = caseCode;
                    }

                    public String getINameClean() {
                        return iNameClean;
                    }

                    public void setINameClean(String iNameClean) {
                        this.iNameClean = iNameClean;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getSexyClean() {
                        return sexyClean;
                    }

                    public void setSexyClean(String sexyClean) {
                        this.sexyClean = sexyClean;
                    }

                    public String getAgeClean() {
                        return ageClean;
                    }

                    public void setAgeClean(String ageClean) {
                        this.ageClean = ageClean;
                    }

                    public String getCardNum() {
                        return cardNum;
                    }

                    public void setCardNum(String cardNum) {
                        this.cardNum = cardNum;
                    }

                    public String getYsfzd() {
                        return ysfzd;
                    }

                    public void setYsfzd(String ysfzd) {
                        this.ysfzd = ysfzd;
                    }

                    public String getBusinessEntity() {
                        return businessEntity;
                    }

                    public void setBusinessEntity(String businessEntity) {
                        this.businessEntity = businessEntity;
                    }

                    public String getRegDateClean() {
                        return regDateClean;
                    }

                    public void setRegDateClean(String regDateClean) {
                        this.regDateClean = regDateClean;
                    }

                    public String getPublishDateClean() {
                        return publishDateClean;
                    }

                    public void setPublishDateClean(String publishDateClean) {
                        this.publishDateClean = publishDateClean;
                    }

                    public String getCourtName() {
                        return courtName;
                    }

                    public void setCourtName(String courtName) {
                        this.courtName = courtName;
                    }

                    public String getAreaNameClean() {
                        return areaNameClean;
                    }

                    public void setAreaNameClean(String areaNameClean) {
                        this.areaNameClean = areaNameClean;
                    }

                    public String getGistID() {
                        return gistID;
                    }

                    public void setGistID(String gistID) {
                        this.gistID = gistID;
                    }

                    public String getGistUnit() {
                        return gistUnit;
                    }

                    public void setGistUnit(String gistUnit) {
                        this.gistUnit = gistUnit;
                    }

                    public String getDuty() {
                        return duty;
                    }

                    public void setDuty(String duty) {
                        this.duty = duty;
                    }

                    public String getDisruptTypeName() {
                        return disruptTypeName;
                    }

                    public void setDisruptTypeName(String disruptTypeName) {
                        this.disruptTypeName = disruptTypeName;
                    }

                    public String getPerformance() {
                        return performance;
                    }

                    public void setPerformance(String performance) {
                        this.performance = performance;
                    }

                    public String getPerformedPart() {
                        return performedPart;
                    }

                    public void setPerformedPart(String performedPart) {
                        this.performedPart = performedPart;
                    }

                    public String getUnperformPart() {
                        return unperformPart;
                    }

                    public void setUnperformPart(String unperformPart) {
                        this.unperformPart = unperformPart;
                    }

                    public String getFocusNumber() {
                        return focusNumber;
                    }

                    public void setFocusNumber(String focusNumber) {
                        this.focusNumber = focusNumber;
                    }
                }

                public  class PunishedListBean implements Serializable{
                    /**
                     * caseCode : 案号
                     * iNameClean : 被执行人姓名/名称
                     * cardNumClean : 身份证号码/注册号
                     * sexyClean : 性别
                     * ageClean : 年龄
                     * areaNameClean : 省份
                     * ysfzd : 身份证原始发地
                     * courtName : 执行法院
                     * regDateClean : 立案时间
                     * caseState : 案件状态
                     * execMoney : 执行标的
                     * focusNumber : 关注次数
                     */

                    private String caseCode;
                    private String iNameClean;
                    private String cardNumClean;
                    private String sexyClean;
                    private String ageClean;
                    private String areaNameClean;
                    private String ysfzd;
                    private String courtName;
                    private String regDateClean;
                    private String caseState;
                    private String execMoney;
                    private String focusNumber;

                    public String getCaseCode() {
                        return caseCode;
                    }

                    public void setCaseCode(String caseCode) {
                        this.caseCode = caseCode;
                    }

                    public String getINameClean() {
                        return iNameClean;
                    }

                    public void setINameClean(String iNameClean) {
                        this.iNameClean = iNameClean;
                    }

                    public String getCardNumClean() {
                        return cardNumClean;
                    }

                    public void setCardNumClean(String cardNumClean) {
                        this.cardNumClean = cardNumClean;
                    }

                    public String getSexyClean() {
                        return sexyClean;
                    }

                    public void setSexyClean(String sexyClean) {
                        this.sexyClean = sexyClean;
                    }

                    public String getAgeClean() {
                        return ageClean;
                    }

                    public void setAgeClean(String ageClean) {
                        this.ageClean = ageClean;
                    }

                    public String getAreaNameClean() {
                        return areaNameClean;
                    }

                    public void setAreaNameClean(String areaNameClean) {
                        this.areaNameClean = areaNameClean;
                    }

                    public String getYsfzd() {
                        return ysfzd;
                    }

                    public void setYsfzd(String ysfzd) {
                        this.ysfzd = ysfzd;
                    }

                    public String getCourtName() {
                        return courtName;
                    }

                    public void setCourtName(String courtName) {
                        this.courtName = courtName;
                    }

                    public String getRegDateClean() {
                        return regDateClean;
                    }

                    public void setRegDateClean(String regDateClean) {
                        this.regDateClean = regDateClean;
                    }

                    public String getCaseState() {
                        return caseState;
                    }

                    public void setCaseState(String caseState) {
                        this.caseState = caseState;
                    }

                    public String getExecMoney() {
                        return execMoney;
                    }

                    public void setExecMoney(String execMoney) {
                        this.execMoney = execMoney;
                    }

                    public String getFocusNumber() {
                        return focusNumber;
                    }

                    public void setFocusNumber(String focusNumber) {
                        this.focusNumber = focusNumber;
                    }
                }

                public class AliDebtListBean implements Serializable{
                    /**
                     * iNameClean : 欠贷人姓名/名称
                     * areaNameClean : 省份
                     * cardNumClean : 身份证号码/企业注册号
                     * ysfzd : 身份证原始发地
                     * sexyClean : 性别
                     * qked : 欠款额度
                     * ageClean : 年龄
                     * wyqk : 违约情况
                     * dkdqsj : 贷款到期时间
                     * legalPerson : 法定代表人
                     * tbzh : 淘宝账户
                     * dkqx : 贷款期限
                     */

                    private String iNameClean;
                    private String areaNameClean;
                    private String cardNumClean;
                    private String ysfzd;
                    private String sexyClean;
                    private String qked;
                    private String ageClean;
                    private String wyqk;
                    private String dkdqsj;
                    private String legalPerson;
                    private String tbzh;
                    private String dkqx;

                    public String getINameClean() {
                        return iNameClean;
                    }

                    public void setINameClean(String iNameClean) {
                        this.iNameClean = iNameClean;
                    }

                    public String getAreaNameClean() {
                        return areaNameClean;
                    }

                    public void setAreaNameClean(String areaNameClean) {
                        this.areaNameClean = areaNameClean;
                    }

                    public String getCardNumClean() {
                        return cardNumClean;
                    }

                    public void setCardNumClean(String cardNumClean) {
                        this.cardNumClean = cardNumClean;
                    }

                    public String getYsfzd() {
                        return ysfzd;
                    }

                    public void setYsfzd(String ysfzd) {
                        this.ysfzd = ysfzd;
                    }

                    public String getSexyClean() {
                        return sexyClean;
                    }

                    public void setSexyClean(String sexyClean) {
                        this.sexyClean = sexyClean;
                    }

                    public String getQked() {
                        return qked;
                    }

                    public void setQked(String qked) {
                        this.qked = qked;
                    }

                    public String getAgeClean() {
                        return ageClean;
                    }

                    public void setAgeClean(String ageClean) {
                        this.ageClean = ageClean;
                    }

                    public String getWyqk() {
                        return wyqk;
                    }

                    public void setWyqk(String wyqk) {
                        this.wyqk = wyqk;
                    }

                    public String getDkdqsj() {
                        return dkdqsj;
                    }

                    public void setDkdqsj(String dkdqsj) {
                        this.dkdqsj = dkdqsj;
                    }

                    public String getLegalPerson() {
                        return legalPerson;
                    }

                    public void setLegalPerson(String legalPerson) {
                        this.legalPerson = legalPerson;
                    }

                    public String getTbzh() {
                        return tbzh;
                    }

                    public void setTbzh(String tbzh) {
                        this.tbzh = tbzh;
                    }

                    public String getDkqx() {
                        return dkqx;
                    }

                    public void setDkqx(String dkqx) {
                        this.dkqx = dkqx;
                    }
                }
            }
        }
    }
}
