package com.fuze.bcp.api.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 个人诉讼查询
 * Created by Lily on 2017/8/3.
 */
public class PersonalActionBean implements Serializable{
    /**
     * code : 10000
     * charge : false
     * msg : 查询成功
     * result : {"resp_data":{"body":{"body":[{"yiwu":"","relatedParty":null,"lxqk":"","pname":"","sortTime":"","idcardNo":"","yjCode":"","court":"","dataType":"shixin","sex":"","province":"","postTime":"","jtqx":"","shixinId":"","age":"","yjdw":"","caseNo":""},{"execMoney":"","sortTime":"","zxggId":"","title":"","proposer":"","caseNo":"","body":"","dataType":"zxgg","court":"","status":"","pname":"","idcardNo":"","caseState":""},{"sortTime":"","judge":"","anyouType":"","trialProcedure":"","dataType":"cpws","court":"","courtRank":"","yiju":"","anyou":"","cpwsId":"","caseCause":"","caseType":"","body":"","partys":[{"birthday":"","status":"","pname":"","address":"","lawOffice":"","title":"","partyType":"","idcardNo":"","lawyer":""}],"judgeResult":"","title":"","caseNo":""},{"sortTime":"","body":"","sex":"","phone":"","updateTime":"","execCourt":"","relatedParty":"","whfx":"","idcardNo":"","birthPlace":"","bjbx":"","caseCode":"","address":"","email":"","sourceName":"","age":"","pname":"","wdhmdId":"","datasource":"","yhje":"","mobile":""},{"sortTime":"","body":"","bgDate":"","partyType":"","court":"","proposer":"","idcardNo":"","bgtId":"","caseCause":"","unnexeMoney":"","address":"","pname":"","caseNo":"","yiju":"","execMoney":""},{"sortTime":"","body":"","ktggId":"","plaintiff":"","organizer":"","courtroom":"","relatedParty":"","court":"","party":"","title":"","caseCause":"","judge":"","caseNo":"","defendant":""},{"sortTime":"","body":"","fyggId":"","layout":null,"relatedParty":"","pname":"","court":"","ggType":""},{"member":null,"sortTime":"","clerkPhone":"","caseType":"","body":null,"trialProcedure":"","ajlcId":"","sentencingDate":"","status":0,"caseStatus":0,"organizer":"","filingDate":"","court":null,"ajlcStatus":"","chiefJudge":"","caseCause":"","trialLimitDate":"","clerk":"","judge":"","actionObject":"0","pname":"","caseNo":"","effectiveDate":""}]},"code":"1000","msg":"查询成功"}}
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
         * resp_data : {"body":{"body":[{"yiwu":"","relatedParty":null,"lxqk":"","pname":"","sortTime":"","idcardNo":"","yjCode":"","court":"","dataType":"shixin","sex":"","province":"","postTime":"","jtqx":"","shixinId":"","age":"","yjdw":"","caseNo":"","execMoney":"","zxggId":"","title":"","proposer":"","body":"","status":"","caseState":"","judge":"","anyouType":"","trialProcedure":"","courtRank":"","yiju":"","anyou":"","cpwsId":"","caseCause":"","caseType":"","partys":[{"birthday":"","status":"","pname":"","address":"","lawOffice":"","title":"","partyType":"","idcardNo":"","lawyer":""}],"judgeResult":"","phone":"","updateTime":"","execCourt":"","whfx":"","birthPlace":"","bjbx":"","caseCode":"","address":"","email":"","sourceName":"","wdhmdId":"","datasource":"","yhje":"","mobile":"","bgDate":"","partyType":"","bgtId":"","unnexeMoney":"","ktggId":"","plaintiff":"","organizer":"","courtroom":"","party":"","defendant":"","fyggId":"","layout":null,"ggType":"","member":null,"clerkPhone":"","ajlcId":"","sentencingDate":"","caseStatus":0,"filingDate":"","ajlcStatus":"","chiefJudge":"","trialLimitDate":"","clerk":"","actionObject":"0","effectiveDate":""},{"execMoney":"","sortTime":"","zxggId":"","title":"","proposer":"","caseNo":"","body":"","dataType":"zxgg","court":"","status":"","pname":"","idcardNo":"","caseState":""},{"sortTime":"","judge":"","anyouType":"","trialProcedure":"","dataType":"cpws","court":"","courtRank":"","yiju":"","anyou":"","cpwsId":"","caseCause":"","caseType":"","body":"","partys":[{"birthday":"","status":"","pname":"","address":"","lawOffice":"","title":"","partyType":"","idcardNo":"","lawyer":""}],"judgeResult":"","title":"","caseNo":""},{"sortTime":"","body":"","sex":"","phone":"","updateTime":"","execCourt":"","relatedParty":"","whfx":"","idcardNo":"","birthPlace":"","bjbx":"","caseCode":"","address":"","email":"","sourceName":"","age":"","pname":"","wdhmdId":"","datasource":"","yhje":"","mobile":""},{"sortTime":"","body":"","bgDate":"","partyType":"","court":"","proposer":"","idcardNo":"","bgtId":"","caseCause":"","unnexeMoney":"","address":"","pname":"","caseNo":"","yiju":"","execMoney":""},{"sortTime":"","body":"","ktggId":"","plaintiff":"","organizer":"","courtroom":"","relatedParty":"","court":"","party":"","title":"","caseCause":"","judge":"","caseNo":"","defendant":""},{"sortTime":"","body":"","fyggId":"","layout":null,"relatedParty":"","pname":"","court":"","ggType":""},{"member":null,"sortTime":"","clerkPhone":"","caseType":"","body":null,"trialProcedure":"","ajlcId":"","sentencingDate":"","status":0,"caseStatus":0,"organizer":"","filingDate":"","court":null,"ajlcStatus":"","chiefJudge":"","caseCause":"","trialLimitDate":"","clerk":"","judge":"","actionObject":"0","pname":"","caseNo":"","effectiveDate":""}]},"code":"1000","msg":"查询成功"}
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
             * body : {"body":[{"yiwu":"","relatedParty":null,"lxqk":"","pname":"","sortTime":"","idcardNo":"","yjCode":"","court":"","dataType":"shixin","sex":"","province":"","postTime":"","jtqx":"","shixinId":"","age":"","yjdw":"","caseNo":"","execMoney":"","zxggId":"","title":"","proposer":"","body":"","status":"","caseState":"","judge":"","anyouType":"","trialProcedure":"","courtRank":"","yiju":"","anyou":"","cpwsId":"","caseCause":"","caseType":"","partys":[{"birthday":"","status":"","pname":"","address":"","lawOffice":"","title":"","partyType":"","idcardNo":"","lawyer":""}],"judgeResult":"","phone":"","updateTime":"","execCourt":"","whfx":"","birthPlace":"","bjbx":"","caseCode":"","address":"","email":"","sourceName":"","wdhmdId":"","datasource":"","yhje":"","mobile":"","bgDate":"","partyType":"","bgtId":"","unnexeMoney":"","ktggId":"","plaintiff":"","organizer":"","courtroom":"","party":"","defendant":"","fyggId":"","layout":null,"ggType":"","member":null,"clerkPhone":"","ajlcId":"","sentencingDate":"","caseStatus":0,"filingDate":"","ajlcStatus":"","chiefJudge":"","trialLimitDate":"","clerk":"","actionObject":"0","effectiveDate":""},{"execMoney":"","sortTime":"","zxggId":"","title":"","proposer":"","caseNo":"","body":"","dataType":"zxgg","court":"","status":"","pname":"","idcardNo":"","caseState":""},{"sortTime":"","judge":"","anyouType":"","trialProcedure":"","dataType":"cpws","court":"","courtRank":"","yiju":"","anyou":"","cpwsId":"","caseCause":"","caseType":"","body":"","partys":[{"birthday":"","status":"","pname":"","address":"","lawOffice":"","title":"","partyType":"","idcardNo":"","lawyer":""}],"judgeResult":"","title":"","caseNo":""},{"sortTime":"","body":"","sex":"","phone":"","updateTime":"","execCourt":"","relatedParty":"","whfx":"","idcardNo":"","birthPlace":"","bjbx":"","caseCode":"","address":"","email":"","sourceName":"","age":"","pname":"","wdhmdId":"","datasource":"","yhje":"","mobile":""},{"sortTime":"","body":"","bgDate":"","partyType":"","court":"","proposer":"","idcardNo":"","bgtId":"","caseCause":"","unnexeMoney":"","address":"","pname":"","caseNo":"","yiju":"","execMoney":""},{"sortTime":"","body":"","ktggId":"","plaintiff":"","organizer":"","courtroom":"","relatedParty":"","court":"","party":"","title":"","caseCause":"","judge":"","caseNo":"","defendant":""},{"sortTime":"","body":"","fyggId":"","layout":null,"relatedParty":"","pname":"","court":"","ggType":""},{"member":null,"sortTime":"","clerkPhone":"","caseType":"","body":null,"trialProcedure":"","ajlcId":"","sentencingDate":"","status":0,"caseStatus":0,"organizer":"","filingDate":"","court":null,"ajlcStatus":"","chiefJudge":"","caseCause":"","trialLimitDate":"","clerk":"","judge":"","actionObject":"0","pname":"","caseNo":"","effectiveDate":""}]}
             * code : 1000
             * msg : 查询成功
             */

            private BodyBeanX body;
            private String code;
            private String msg;

            public BodyBeanX getBody() {
                return body;
            }

            public void setBody(BodyBeanX body) {
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

            public class BodyBeanX implements Serializable{
                private List<BodyBean> body;

                public List<BodyBean> getBody() {
                    return body;
                }

                public void setBody(List<BodyBean> body) {
                    this.body = body;
                }

                public class BodyBean implements Serializable{
                    /**
                     * yiwu :
                     * relatedParty : null
                     * lxqk :
                     * pname :
                     * sortTime :
                     * idcardNo :
                     * yjCode :
                     * court :
                     * dataType : shixin
                     * sex :
                     * province :
                     * postTime :
                     * jtqx :
                     * shixinId :
                     * age :
                     * yjdw :
                     * caseNo :
                     * execMoney :
                     * zxggId :
                     * title :
                     * proposer :
                     * body :
                     * status :
                     * caseState :
                     * judge :
                     * anyouType :
                     * trialProcedure :
                     * courtRank :
                     * yiju :
                     * anyou :
                     * cpwsId :
                     * caseCause :
                     * caseType :
                     * partys : [{"birthday":"","status":"","pname":"","address":"","lawOffice":"","title":"","partyType":"","idcardNo":"","lawyer":""}]
                     * judgeResult :
                     * phone :
                     * updateTime :
                     * execCourt :
                     * whfx :
                     * birthPlace :
                     * bjbx :
                     * caseCode :
                     * address :
                     * email :
                     * sourceName :
                     * wdhmdId :
                     * datasource :
                     * yhje :
                     * mobile :
                     * bgDate :
                     * partyType :
                     * bgtId :
                     * unnexeMoney :
                     * ktggId :
                     * plaintiff :
                     * organizer :
                     * courtroom :
                     * party :
                     * defendant :
                     * fyggId :
                     * layout : null
                     * ggType :
                     * member : null
                     * clerkPhone :
                     * ajlcId :
                     * sentencingDate :
                     * caseStatus : 0
                     * filingDate :
                     * ajlcStatus :
                     * chiefJudge :
                     * trialLimitDate :
                     * clerk :
                     * actionObject : 0
                     * effectiveDate :
                     */

                    private String yiwu;
                    private Object relatedParty;
                    private String lxqk;
                    private String pname;
                    private String sortTime;
                    private String idcardNo;
                    private String yjCode;
                    private String court;
                    private String dataType;
                    private String sex;
                    private String province;
                    private String postTime;
                    private String jtqx;
                    private String shixinId;
                    private String age;
                    private String yjdw;
                    private String caseNo;
                    private String execMoney;
                    private String zxggId;
                    private String title;
                    private String proposer;
                    private String body;
                    private String status;
                    private String caseState;
                    private String judge;
                    private String anyouType;
                    private String trialProcedure;
                    private String courtRank;
                    private String yiju;
                    private String anyou;
                    private String cpwsId;
                    private String caseCause;
                    private String caseType;
                    private String judgeResult;
                    private String phone;
                    private String updateTime;
                    private String execCourt;
                    private String whfx;
                    private String birthPlace;
                    private String bjbx;
                    private String caseCode;
                    private String address;
                    private String email;
                    private String sourceName;
                    private String wdhmdId;
                    private String datasource;
                    private String yhje;
                    private String mobile;
                    private String bgDate;
                    private String partyType;
                    private String bgtId;
                    private String unnexeMoney;
                    private String ktggId;
                    private String plaintiff;
                    private String organizer;
                    private String courtroom;
                    private String party;
                    private String defendant;
                    private String fyggId;
                    private Object layout;
                    private String ggType;
                    private Object member;
                    private String clerkPhone;
                    private String ajlcId;
                    private String sentencingDate;
                    private int caseStatus;
                    private String filingDate;
                    private String ajlcStatus;
                    private String chiefJudge;
                    private String trialLimitDate;
                    private String clerk;
                    private String actionObject;
                    private String effectiveDate;
                    private List<PartysBean> partys;

                    public String getYiwu() {
                        return yiwu;
                    }

                    public void setYiwu(String yiwu) {
                        this.yiwu = yiwu;
                    }

                    public Object getRelatedParty() {
                        return relatedParty;
                    }

                    public void setRelatedParty(Object relatedParty) {
                        this.relatedParty = relatedParty;
                    }

                    public String getLxqk() {
                        return lxqk;
                    }

                    public void setLxqk(String lxqk) {
                        this.lxqk = lxqk;
                    }

                    public String getPname() {
                        return pname;
                    }

                    public void setPname(String pname) {
                        this.pname = pname;
                    }

                    public String getSortTime() {
                        return sortTime;
                    }

                    public void setSortTime(String sortTime) {
                        this.sortTime = sortTime;
                    }

                    public String getIdcardNo() {
                        return idcardNo;
                    }

                    public void setIdcardNo(String idcardNo) {
                        this.idcardNo = idcardNo;
                    }

                    public String getYjCode() {
                        return yjCode;
                    }

                    public void setYjCode(String yjCode) {
                        this.yjCode = yjCode;
                    }

                    public String getCourt() {
                        return court;
                    }

                    public void setCourt(String court) {
                        this.court = court;
                    }

                    public String getDataType() {
                        return dataType;
                    }

                    public void setDataType(String dataType) {
                        this.dataType = dataType;
                    }

                    public String getSex() {
                        return sex;
                    }

                    public void setSex(String sex) {
                        this.sex = sex;
                    }

                    public String getProvince() {
                        return province;
                    }

                    public void setProvince(String province) {
                        this.province = province;
                    }

                    public String getPostTime() {
                        return postTime;
                    }

                    public void setPostTime(String postTime) {
                        this.postTime = postTime;
                    }

                    public String getJtqx() {
                        return jtqx;
                    }

                    public void setJtqx(String jtqx) {
                        this.jtqx = jtqx;
                    }

                    public String getShixinId() {
                        return shixinId;
                    }

                    public void setShixinId(String shixinId) {
                        this.shixinId = shixinId;
                    }

                    public String getAge() {
                        return age;
                    }

                    public void setAge(String age) {
                        this.age = age;
                    }

                    public String getYjdw() {
                        return yjdw;
                    }

                    public void setYjdw(String yjdw) {
                        this.yjdw = yjdw;
                    }

                    public String getCaseNo() {
                        return caseNo;
                    }

                    public void setCaseNo(String caseNo) {
                        this.caseNo = caseNo;
                    }

                    public String getExecMoney() {
                        return execMoney;
                    }

                    public void setExecMoney(String execMoney) {
                        this.execMoney = execMoney;
                    }

                    public String getZxggId() {
                        return zxggId;
                    }

                    public void setZxggId(String zxggId) {
                        this.zxggId = zxggId;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getProposer() {
                        return proposer;
                    }

                    public void setProposer(String proposer) {
                        this.proposer = proposer;
                    }

                    public String getBody() {
                        return body;
                    }

                    public void setBody(String body) {
                        this.body = body;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }

                    public String getCaseState() {
                        return caseState;
                    }

                    public void setCaseState(String caseState) {
                        this.caseState = caseState;
                    }

                    public String getJudge() {
                        return judge;
                    }

                    public void setJudge(String judge) {
                        this.judge = judge;
                    }

                    public String getAnyouType() {
                        return anyouType;
                    }

                    public void setAnyouType(String anyouType) {
                        this.anyouType = anyouType;
                    }

                    public String getTrialProcedure() {
                        return trialProcedure;
                    }

                    public void setTrialProcedure(String trialProcedure) {
                        this.trialProcedure = trialProcedure;
                    }

                    public String getCourtRank() {
                        return courtRank;
                    }

                    public void setCourtRank(String courtRank) {
                        this.courtRank = courtRank;
                    }

                    public String getYiju() {
                        return yiju;
                    }

                    public void setYiju(String yiju) {
                        this.yiju = yiju;
                    }

                    public String getAnyou() {
                        return anyou;
                    }

                    public void setAnyou(String anyou) {
                        this.anyou = anyou;
                    }

                    public String getCpwsId() {
                        return cpwsId;
                    }

                    public void setCpwsId(String cpwsId) {
                        this.cpwsId = cpwsId;
                    }

                    public String getCaseCause() {
                        return caseCause;
                    }

                    public void setCaseCause(String caseCause) {
                        this.caseCause = caseCause;
                    }

                    public String getCaseType() {
                        return caseType;
                    }

                    public void setCaseType(String caseType) {
                        this.caseType = caseType;
                    }

                    public String getJudgeResult() {
                        return judgeResult;
                    }

                    public void setJudgeResult(String judgeResult) {
                        this.judgeResult = judgeResult;
                    }

                    public String getPhone() {
                        return phone;
                    }

                    public void setPhone(String phone) {
                        this.phone = phone;
                    }

                    public String getUpdateTime() {
                        return updateTime;
                    }

                    public void setUpdateTime(String updateTime) {
                        this.updateTime = updateTime;
                    }

                    public String getExecCourt() {
                        return execCourt;
                    }

                    public void setExecCourt(String execCourt) {
                        this.execCourt = execCourt;
                    }

                    public String getWhfx() {
                        return whfx;
                    }

                    public void setWhfx(String whfx) {
                        this.whfx = whfx;
                    }

                    public String getBirthPlace() {
                        return birthPlace;
                    }

                    public void setBirthPlace(String birthPlace) {
                        this.birthPlace = birthPlace;
                    }

                    public String getBjbx() {
                        return bjbx;
                    }

                    public void setBjbx(String bjbx) {
                        this.bjbx = bjbx;
                    }

                    public String getCaseCode() {
                        return caseCode;
                    }

                    public void setCaseCode(String caseCode) {
                        this.caseCode = caseCode;
                    }

                    public String getAddress() {
                        return address;
                    }

                    public void setAddress(String address) {
                        this.address = address;
                    }

                    public String getEmail() {
                        return email;
                    }

                    public void setEmail(String email) {
                        this.email = email;
                    }

                    public String getSourceName() {
                        return sourceName;
                    }

                    public void setSourceName(String sourceName) {
                        this.sourceName = sourceName;
                    }

                    public String getWdhmdId() {
                        return wdhmdId;
                    }

                    public void setWdhmdId(String wdhmdId) {
                        this.wdhmdId = wdhmdId;
                    }

                    public String getDatasource() {
                        return datasource;
                    }

                    public void setDatasource(String datasource) {
                        this.datasource = datasource;
                    }

                    public String getYhje() {
                        return yhje;
                    }

                    public void setYhje(String yhje) {
                        this.yhje = yhje;
                    }

                    public String getMobile() {
                        return mobile;
                    }

                    public void setMobile(String mobile) {
                        this.mobile = mobile;
                    }

                    public String getBgDate() {
                        return bgDate;
                    }

                    public void setBgDate(String bgDate) {
                        this.bgDate = bgDate;
                    }

                    public String getPartyType() {
                        return partyType;
                    }

                    public void setPartyType(String partyType) {
                        this.partyType = partyType;
                    }

                    public String getBgtId() {
                        return bgtId;
                    }

                    public void setBgtId(String bgtId) {
                        this.bgtId = bgtId;
                    }

                    public String getUnnexeMoney() {
                        return unnexeMoney;
                    }

                    public void setUnnexeMoney(String unnexeMoney) {
                        this.unnexeMoney = unnexeMoney;
                    }

                    public String getKtggId() {
                        return ktggId;
                    }

                    public void setKtggId(String ktggId) {
                        this.ktggId = ktggId;
                    }

                    public String getPlaintiff() {
                        return plaintiff;
                    }

                    public void setPlaintiff(String plaintiff) {
                        this.plaintiff = plaintiff;
                    }

                    public String getOrganizer() {
                        return organizer;
                    }

                    public void setOrganizer(String organizer) {
                        this.organizer = organizer;
                    }

                    public String getCourtroom() {
                        return courtroom;
                    }

                    public void setCourtroom(String courtroom) {
                        this.courtroom = courtroom;
                    }

                    public String getParty() {
                        return party;
                    }

                    public void setParty(String party) {
                        this.party = party;
                    }

                    public String getDefendant() {
                        return defendant;
                    }

                    public void setDefendant(String defendant) {
                        this.defendant = defendant;
                    }

                    public String getFyggId() {
                        return fyggId;
                    }

                    public void setFyggId(String fyggId) {
                        this.fyggId = fyggId;
                    }

                    public Object getLayout() {
                        return layout;
                    }

                    public void setLayout(Object layout) {
                        this.layout = layout;
                    }

                    public String getGgType() {
                        return ggType;
                    }

                    public void setGgType(String ggType) {
                        this.ggType = ggType;
                    }

                    public Object getMember() {
                        return member;
                    }

                    public void setMember(Object member) {
                        this.member = member;
                    }

                    public String getClerkPhone() {
                        return clerkPhone;
                    }

                    public void setClerkPhone(String clerkPhone) {
                        this.clerkPhone = clerkPhone;
                    }

                    public String getAjlcId() {
                        return ajlcId;
                    }

                    public void setAjlcId(String ajlcId) {
                        this.ajlcId = ajlcId;
                    }

                    public String getSentencingDate() {
                        return sentencingDate;
                    }

                    public void setSentencingDate(String sentencingDate) {
                        this.sentencingDate = sentencingDate;
                    }

                    public int getCaseStatus() {
                        return caseStatus;
                    }

                    public void setCaseStatus(int caseStatus) {
                        this.caseStatus = caseStatus;
                    }

                    public String getFilingDate() {
                        return filingDate;
                    }

                    public void setFilingDate(String filingDate) {
                        this.filingDate = filingDate;
                    }

                    public String getAjlcStatus() {
                        return ajlcStatus;
                    }

                    public void setAjlcStatus(String ajlcStatus) {
                        this.ajlcStatus = ajlcStatus;
                    }

                    public String getChiefJudge() {
                        return chiefJudge;
                    }

                    public void setChiefJudge(String chiefJudge) {
                        this.chiefJudge = chiefJudge;
                    }

                    public String getTrialLimitDate() {
                        return trialLimitDate;
                    }

                    public void setTrialLimitDate(String trialLimitDate) {
                        this.trialLimitDate = trialLimitDate;
                    }

                    public String getClerk() {
                        return clerk;
                    }

                    public void setClerk(String clerk) {
                        this.clerk = clerk;
                    }

                    public String getActionObject() {
                        return actionObject;
                    }

                    public void setActionObject(String actionObject) {
                        this.actionObject = actionObject;
                    }

                    public String getEffectiveDate() {
                        return effectiveDate;
                    }

                    public void setEffectiveDate(String effectiveDate) {
                        this.effectiveDate = effectiveDate;
                    }

                    public List<PartysBean> getPartys() {
                        return partys;
                    }

                    public void setPartys(List<PartysBean> partys) {
                        this.partys = partys;
                    }

                    public class PartysBean implements Serializable{
                        /**
                         * birthday :
                         * status :
                         * pname :
                         * address :
                         * lawOffice :
                         * title :
                         * partyType :
                         * idcardNo :
                         * lawyer :
                         */

                        private String birthday;
                        private String status;
                        private String pname;
                        private String address;
                        private String lawOffice;
                        private String title;
                        private String partyType;
                        private String idcardNo;
                        private String lawyer;

                        public String getBirthday() {
                            return birthday;
                        }

                        public void setBirthday(String birthday) {
                            this.birthday = birthday;
                        }

                        public String getStatus() {
                            return status;
                        }

                        public void setStatus(String status) {
                            this.status = status;
                        }

                        public String getPname() {
                            return pname;
                        }

                        public void setPname(String pname) {
                            this.pname = pname;
                        }

                        public String getAddress() {
                            return address;
                        }

                        public void setAddress(String address) {
                            this.address = address;
                        }

                        public String getLawOffice() {
                            return lawOffice;
                        }

                        public void setLawOffice(String lawOffice) {
                            this.lawOffice = lawOffice;
                        }

                        public String getTitle() {
                            return title;
                        }

                        public void setTitle(String title) {
                            this.title = title;
                        }

                        public String getPartyType() {
                            return partyType;
                        }

                        public void setPartyType(String partyType) {
                            this.partyType = partyType;
                        }

                        public String getIdcardNo() {
                            return idcardNo;
                        }

                        public void setIdcardNo(String idcardNo) {
                            this.idcardNo = idcardNo;
                        }

                        public String getLawyer() {
                            return lawyer;
                        }

                        public void setLawyer(String lawyer) {
                            this.lawyer = lawyer;
                        }
                    }
                }
            }
        }
    }
}
