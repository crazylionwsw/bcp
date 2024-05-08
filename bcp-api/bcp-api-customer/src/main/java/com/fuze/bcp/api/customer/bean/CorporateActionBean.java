package com.fuze.bcp.api.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 企业诉讼
 * Created by Lily on 2017/8/3.
 */
public class CorporateActionBean implements Serializable {
    /**
     * code : 10000
     * charge : false
     * msg : 查询成功
     * result : {"resp_data":{"msg":"查询成功","code":"1000","body":{"detailed":[{"status":0,"body":null,"proposer":null,"dataType":"zxgg","pname":"福安市正凯船务有限公司","caseState":"0","zxggId":"c20173509zhihui122_t20170413_pfaszkcwyxgs","caseNo":"（2017）闽09执恢122号","court":"宁德市中级人民法院","sortTime":1492012800000,"title":null,"idcardNo":"75139468-8","execMoney":425000},{"wdhmd":[{"sortTime":1446307200000,"body":null,"sex":null,"phone":null,"updateTime":null,"execCourt":null,"relatedParty":null,"whfx":null,"idcardNo":"44152219950625****","birthPlace":null,"bjbx":"3000.00","caseCode":null,"address":"福建省宁德市蕉城区木材公司","email":"qyl_888@126.com","sourceName":"拍来贷","age":null,"pname":"张冬澍","wdhmdId":"AVMH_Ld5fu8bWnowhLd5","datasource":null,"yhje":"7647.33","mobile":"18605051668"}]},{"relatedParty":null,"province":"福建","age":47,"caseNo":"（2016）闽0981执02826号","sortTime":1471536000000,"yjdw":"福安市人民法院","dataType":"shixin","pname":"李仁杰","shixinId":"c2016350981zhi2826_t20160819_plirenjie","postTime":1478448000000,"court":"福安市人民法院","yiwu":"","jtqx":"违反财产报告制度","idcardNo":"3522261969****0030","sex":"男","yjCode":"(2015)安民初字第1178号","lxqk":"全部未履行"},{"ktgg":[{"sortTime":1525622400000,"body":null,"ktggId":"AU2AWyb76aXWqCOGevnG","plaintiff":null,"organizer":null,"courtroom":null,"relatedParty":null,"court":"舟山市普陀区人民法院","party":null,"title":null,"caseCause":"","judge":null,"caseNo":"（2009）舟普民执字第636号","defendant":null}]},{"bgt":[{"sortTime":1419868800000,"body":"","bgDate":1427644800000,"partyType":"P","court":"","proposer":null,"idcardNo":"","bgtId":"","caseCause":"","unnexeMoney":28111.82,"address":null,"pname":"张美根","caseNo":"","yiju":"","execMoney":28111.82}]},{"cpws":[{"sortTime":4277116800000,"body":"","caseType":"mscd","cpwsId":"17809422","trialProcedure":"","court":"","anyou":"A0820229","partys":[{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":null,"status":"d","pname":"付广卫","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null}],"title":"","caseCause":"","judge":null,"caseNo":"（2015）临法商初字第787号","judgeResult":"","anyouType":"A","yiju":"","courtRank":4}]},{"ajlc":[{"member":null,"sortTime":1476806400000,"clerkPhone":null,"caseType":"民事","body":"","trialProcedure":"司法确认","ajlcId":"AVfoHxQuxL_h1T6qAQiQ","sentencingDate":null,"status":1,"caseStatus":0,"organizer":"附城人民法庭","filingDate":null,"court":null,"ajlcStatus":"已立案","chiefJudge":null,"caseCause":"请求确认人民调解协议效力","trialLimitDate":"2015-05-27","clerk":"陈采红","judge":"杨伟雄","actionObject":"0","pname":"","caseNo":"（2014）梅丰法附调确字第27号","effectiveDate":null}]},{"ggType":"裁判文书","sortTime":1463500800000,"court":"福安市人民法院","fyggId":"c2015anminchu1178_t20160518","layout":null,"relatedParty":null,"body":"","pname":"","dataType":"fygg"}],"basic":{"zxggPageNum":1,"shixinCount":29,"ajlcCount":3,"range":10,"cpwsCount":10,"ktggPageNum":1,"fyggCount":8,"msg":"成功返回","code":"s","pageNo":1,"cpwsPageNum":1,"ktggCount":3,"wdhmdCount":0,"bgtCount":13,"zxggCount":8,"searchSeconds":0.26,"fyggPageNum":1,"wdhmdPageNum":0,"ajlcPageNum":1,"bgtPageNum":2,"totalPageNum":8,"totalCount":74,"shixinPageNum":3}}}}
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
         * resp_data : {"msg":"查询成功","code":"1000","body":{"detailed":[{"status":0,"body":null,"proposer":null,"dataType":"zxgg","pname":"福安市正凯船务有限公司","caseState":"0","zxggId":"c20173509zhihui122_t20170413_pfaszkcwyxgs","caseNo":"（2017）闽09执恢122号","court":"宁德市中级人民法院","sortTime":1492012800000,"title":null,"idcardNo":"75139468-8","execMoney":425000,"wdhmd":[{"sortTime":1446307200000,"body":null,"sex":null,"phone":null,"updateTime":null,"execCourt":null,"relatedParty":null,"whfx":null,"idcardNo":"44152219950625****","birthPlace":null,"bjbx":"3000.00","caseCode":null,"address":"福建省宁德市蕉城区木材公司","email":"qyl_888@126.com","sourceName":"拍来贷","age":null,"pname":"张冬澍","wdhmdId":"AVMH_Ld5fu8bWnowhLd5","datasource":null,"yhje":"7647.33","mobile":"18605051668"}],"relatedParty":null,"province":"福建","age":47,"yjdw":"福安市人民法院","shixinId":"c2016350981zhi2826_t20160819_plirenjie","postTime":1478448000000,"yiwu":"","jtqx":"违反财产报告制度","sex":"男","yjCode":"(2015)安民初字第1178号","lxqk":"全部未履行","ktgg":[{"sortTime":1525622400000,"body":null,"ktggId":"AU2AWyb76aXWqCOGevnG","plaintiff":null,"organizer":null,"courtroom":null,"relatedParty":null,"court":"舟山市普陀区人民法院","party":null,"title":null,"caseCause":"","judge":null,"caseNo":"（2009）舟普民执字第636号","defendant":null}],"bgt":[{"sortTime":1419868800000,"body":"","bgDate":1427644800000,"partyType":"P","court":"","proposer":null,"idcardNo":"","bgtId":"","caseCause":"","unnexeMoney":28111.82,"address":null,"pname":"张美根","caseNo":"","yiju":"","execMoney":28111.82}],"cpws":[{"sortTime":4277116800000,"body":"","caseType":"mscd","cpwsId":"17809422","trialProcedure":"","court":"","anyou":"A0820229","partys":[{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":null,"status":"d","pname":"付广卫","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null}],"title":"","caseCause":"","judge":null,"caseNo":"（2015）临法商初字第787号","judgeResult":"","anyouType":"A","yiju":"","courtRank":4}],"ajlc":[{"member":null,"sortTime":1476806400000,"clerkPhone":null,"caseType":"民事","body":"","trialProcedure":"司法确认","ajlcId":"AVfoHxQuxL_h1T6qAQiQ","sentencingDate":null,"status":1,"caseStatus":0,"organizer":"附城人民法庭","filingDate":null,"court":null,"ajlcStatus":"已立案","chiefJudge":null,"caseCause":"请求确认人民调解协议效力","trialLimitDate":"2015-05-27","clerk":"陈采红","judge":"杨伟雄","actionObject":"0","pname":"","caseNo":"（2014）梅丰法附调确字第27号","effectiveDate":null}],"ggType":"裁判文书","fyggId":"c2015anminchu1178_t20160518","layout":null},{"wdhmd":[{"sortTime":1446307200000,"body":null,"sex":null,"phone":null,"updateTime":null,"execCourt":null,"relatedParty":null,"whfx":null,"idcardNo":"44152219950625****","birthPlace":null,"bjbx":"3000.00","caseCode":null,"address":"福建省宁德市蕉城区木材公司","email":"qyl_888@126.com","sourceName":"拍来贷","age":null,"pname":"张冬澍","wdhmdId":"AVMH_Ld5fu8bWnowhLd5","datasource":null,"yhje":"7647.33","mobile":"18605051668"}]},{"relatedParty":null,"province":"福建","age":47,"caseNo":"（2016）闽0981执02826号","sortTime":1471536000000,"yjdw":"福安市人民法院","dataType":"shixin","pname":"李仁杰","shixinId":"c2016350981zhi2826_t20160819_plirenjie","postTime":1478448000000,"court":"福安市人民法院","yiwu":"","jtqx":"违反财产报告制度","idcardNo":"3522261969****0030","sex":"男","yjCode":"(2015)安民初字第1178号","lxqk":"全部未履行"},{"ktgg":[{"sortTime":1525622400000,"body":null,"ktggId":"AU2AWyb76aXWqCOGevnG","plaintiff":null,"organizer":null,"courtroom":null,"relatedParty":null,"court":"舟山市普陀区人民法院","party":null,"title":null,"caseCause":"","judge":null,"caseNo":"（2009）舟普民执字第636号","defendant":null}]},{"bgt":[{"sortTime":1419868800000,"body":"","bgDate":1427644800000,"partyType":"P","court":"","proposer":null,"idcardNo":"","bgtId":"","caseCause":"","unnexeMoney":28111.82,"address":null,"pname":"张美根","caseNo":"","yiju":"","execMoney":28111.82}]},{"cpws":[{"sortTime":4277116800000,"body":"","caseType":"mscd","cpwsId":"17809422","trialProcedure":"","court":"","anyou":"A0820229","partys":[{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":null,"status":"d","pname":"付广卫","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null}],"title":"","caseCause":"","judge":null,"caseNo":"（2015）临法商初字第787号","judgeResult":"","anyouType":"A","yiju":"","courtRank":4}]},{"ajlc":[{"member":null,"sortTime":1476806400000,"clerkPhone":null,"caseType":"民事","body":"","trialProcedure":"司法确认","ajlcId":"AVfoHxQuxL_h1T6qAQiQ","sentencingDate":null,"status":1,"caseStatus":0,"organizer":"附城人民法庭","filingDate":null,"court":null,"ajlcStatus":"已立案","chiefJudge":null,"caseCause":"请求确认人民调解协议效力","trialLimitDate":"2015-05-27","clerk":"陈采红","judge":"杨伟雄","actionObject":"0","pname":"","caseNo":"（2014）梅丰法附调确字第27号","effectiveDate":null}]},{"ggType":"裁判文书","sortTime":1463500800000,"court":"福安市人民法院","fyggId":"c2015anminchu1178_t20160518","layout":null,"relatedParty":null,"body":"","pname":"","dataType":"fygg"}],"basic":{"zxggPageNum":1,"shixinCount":29,"ajlcCount":3,"range":10,"cpwsCount":10,"ktggPageNum":1,"fyggCount":8,"msg":"成功返回","code":"s","pageNo":1,"cpwsPageNum":1,"ktggCount":3,"wdhmdCount":0,"bgtCount":13,"zxggCount":8,"searchSeconds":0.26,"fyggPageNum":1,"wdhmdPageNum":0,"ajlcPageNum":1,"bgtPageNum":2,"totalPageNum":8,"totalCount":74,"shixinPageNum":3}}}
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
             * msg : 查询成功
             * code : 1000
             * body : {"detailed":[{"status":0,"body":null,"proposer":null,"dataType":"zxgg","pname":"福安市正凯船务有限公司","caseState":"0","zxggId":"c20173509zhihui122_t20170413_pfaszkcwyxgs","caseNo":"（2017）闽09执恢122号","court":"宁德市中级人民法院","sortTime":1492012800000,"title":null,"idcardNo":"75139468-8","execMoney":425000},{"wdhmd":[{"sortTime":1446307200000,"body":null,"sex":null,"phone":null,"updateTime":null,"execCourt":null,"relatedParty":null,"whfx":null,"idcardNo":"44152219950625****","birthPlace":null,"bjbx":"3000.00","caseCode":null,"address":"福建省宁德市蕉城区木材公司","email":"qyl_888@126.com","sourceName":"拍来贷","age":null,"pname":"张冬澍","wdhmdId":"AVMH_Ld5fu8bWnowhLd5","datasource":null,"yhje":"7647.33","mobile":"18605051668"}]},{"relatedParty":null,"province":"福建","age":47,"caseNo":"（2016）闽0981执02826号","sortTime":1471536000000,"yjdw":"福安市人民法院","dataType":"shixin","pname":"李仁杰","shixinId":"c2016350981zhi2826_t20160819_plirenjie","postTime":1478448000000,"court":"福安市人民法院","yiwu":"","jtqx":"违反财产报告制度","idcardNo":"3522261969****0030","sex":"男","yjCode":"(2015)安民初字第1178号","lxqk":"全部未履行"},{"ktgg":[{"sortTime":1525622400000,"body":null,"ktggId":"AU2AWyb76aXWqCOGevnG","plaintiff":null,"organizer":null,"courtroom":null,"relatedParty":null,"court":"舟山市普陀区人民法院","party":null,"title":null,"caseCause":"","judge":null,"caseNo":"（2009）舟普民执字第636号","defendant":null}]},{"bgt":[{"sortTime":1419868800000,"body":"","bgDate":1427644800000,"partyType":"P","court":"","proposer":null,"idcardNo":"","bgtId":"","caseCause":"","unnexeMoney":28111.82,"address":null,"pname":"张美根","caseNo":"","yiju":"","execMoney":28111.82}]},{"cpws":[{"sortTime":4277116800000,"body":"","caseType":"mscd","cpwsId":"17809422","trialProcedure":"","court":"","anyou":"A0820229","partys":[{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":null,"status":"d","pname":"付广卫","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null}],"title":"","caseCause":"","judge":null,"caseNo":"（2015）临法商初字第787号","judgeResult":"","anyouType":"A","yiju":"","courtRank":4}]},{"ajlc":[{"member":null,"sortTime":1476806400000,"clerkPhone":null,"caseType":"民事","body":"","trialProcedure":"司法确认","ajlcId":"AVfoHxQuxL_h1T6qAQiQ","sentencingDate":null,"status":1,"caseStatus":0,"organizer":"附城人民法庭","filingDate":null,"court":null,"ajlcStatus":"已立案","chiefJudge":null,"caseCause":"请求确认人民调解协议效力","trialLimitDate":"2015-05-27","clerk":"陈采红","judge":"杨伟雄","actionObject":"0","pname":"","caseNo":"（2014）梅丰法附调确字第27号","effectiveDate":null}]},{"ggType":"裁判文书","sortTime":1463500800000,"court":"福安市人民法院","fyggId":"c2015anminchu1178_t20160518","layout":null,"relatedParty":null,"body":"","pname":"","dataType":"fygg"}],"basic":{"zxggPageNum":1,"shixinCount":29,"ajlcCount":3,"range":10,"cpwsCount":10,"ktggPageNum":1,"fyggCount":8,"msg":"成功返回","code":"s","pageNo":1,"cpwsPageNum":1,"ktggCount":3,"wdhmdCount":0,"bgtCount":13,"zxggCount":8,"searchSeconds":0.26,"fyggPageNum":1,"wdhmdPageNum":0,"ajlcPageNum":1,"bgtPageNum":2,"totalPageNum":8,"totalCount":74,"shixinPageNum":3}}
             */

            private String msg;
            private String code;
            private BodyBean body;

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public BodyBean getBody() {
                return body;
            }

            public void setBody(BodyBean body) {
                this.body = body;
            }

            public class BodyBean implements Serializable{
                /**
                 * detailed : [{"status":0,"body":null,"proposer":null,"dataType":"zxgg","pname":"福安市正凯船务有限公司","caseState":"0","zxggId":"c20173509zhihui122_t20170413_pfaszkcwyxgs","caseNo":"（2017）闽09执恢122号","court":"宁德市中级人民法院","sortTime":1492012800000,"title":null,"idcardNo":"75139468-8","execMoney":425000},{"wdhmd":[{"sortTime":1446307200000,"body":null,"sex":null,"phone":null,"updateTime":null,"execCourt":null,"relatedParty":null,"whfx":null,"idcardNo":"44152219950625****","birthPlace":null,"bjbx":"3000.00","caseCode":null,"address":"福建省宁德市蕉城区木材公司","email":"qyl_888@126.com","sourceName":"拍来贷","age":null,"pname":"张冬澍","wdhmdId":"AVMH_Ld5fu8bWnowhLd5","datasource":null,"yhje":"7647.33","mobile":"18605051668"}]},{"relatedParty":null,"province":"福建","age":47,"caseNo":"（2016）闽0981执02826号","sortTime":1471536000000,"yjdw":"福安市人民法院","dataType":"shixin","pname":"李仁杰","shixinId":"c2016350981zhi2826_t20160819_plirenjie","postTime":1478448000000,"court":"福安市人民法院","yiwu":"","jtqx":"违反财产报告制度","idcardNo":"3522261969****0030","sex":"男","yjCode":"(2015)安民初字第1178号","lxqk":"全部未履行"},{"ktgg":[{"sortTime":1525622400000,"body":null,"ktggId":"AU2AWyb76aXWqCOGevnG","plaintiff":null,"organizer":null,"courtroom":null,"relatedParty":null,"court":"舟山市普陀区人民法院","party":null,"title":null,"caseCause":"","judge":null,"caseNo":"（2009）舟普民执字第636号","defendant":null}]},{"bgt":[{"sortTime":1419868800000,"body":"","bgDate":1427644800000,"partyType":"P","court":"","proposer":null,"idcardNo":"","bgtId":"","caseCause":"","unnexeMoney":28111.82,"address":null,"pname":"张美根","caseNo":"","yiju":"","execMoney":28111.82}]},{"cpws":[{"sortTime":4277116800000,"body":"","caseType":"mscd","cpwsId":"17809422","trialProcedure":"","court":"","anyou":"A0820229","partys":[{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":null,"status":"d","pname":"付广卫","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null}],"title":"","caseCause":"","judge":null,"caseNo":"（2015）临法商初字第787号","judgeResult":"","anyouType":"A","yiju":"","courtRank":4}]},{"ajlc":[{"member":null,"sortTime":1476806400000,"clerkPhone":null,"caseType":"民事","body":"","trialProcedure":"司法确认","ajlcId":"AVfoHxQuxL_h1T6qAQiQ","sentencingDate":null,"status":1,"caseStatus":0,"organizer":"附城人民法庭","filingDate":null,"court":null,"ajlcStatus":"已立案","chiefJudge":null,"caseCause":"请求确认人民调解协议效力","trialLimitDate":"2015-05-27","clerk":"陈采红","judge":"杨伟雄","actionObject":"0","pname":"","caseNo":"（2014）梅丰法附调确字第27号","effectiveDate":null}]},{"ggType":"裁判文书","sortTime":1463500800000,"court":"福安市人民法院","fyggId":"c2015anminchu1178_t20160518","layout":null,"relatedParty":null,"body":"","pname":"","dataType":"fygg"}]
                 * basic : {"zxggPageNum":1,"shixinCount":29,"ajlcCount":3,"range":10,"cpwsCount":10,"ktggPageNum":1,"fyggCount":8,"msg":"成功返回","code":"s","pageNo":1,"cpwsPageNum":1,"ktggCount":3,"wdhmdCount":0,"bgtCount":13,"zxggCount":8,"searchSeconds":0.26,"fyggPageNum":1,"wdhmdPageNum":0,"ajlcPageNum":1,"bgtPageNum":2,"totalPageNum":8,"totalCount":74,"shixinPageNum":3}
                 */

                private BasicBean basic;
                private List<DetailedBean> detailed;

                public BasicBean getBasic() {
                    return basic;
                }

                public void setBasic(BasicBean basic) {
                    this.basic = basic;
                }

                public List<DetailedBean> getDetailed() {
                    return detailed;
                }

                public void setDetailed(List<DetailedBean> detailed) {
                    this.detailed = detailed;
                }

                public class BasicBean implements Serializable{
                    /**
                     * zxggPageNum : 1
                     * shixinCount : 29
                     * ajlcCount : 3
                     * range : 10
                     * cpwsCount : 10
                     * ktggPageNum : 1
                     * fyggCount : 8
                     * msg : 成功返回
                     * code : s
                     * pageNo : 1
                     * cpwsPageNum : 1
                     * ktggCount : 3
                     * wdhmdCount : 0
                     * bgtCount : 13
                     * zxggCount : 8
                     * searchSeconds : 0.26
                     * fyggPageNum : 1
                     * wdhmdPageNum : 0
                     * ajlcPageNum : 1
                     * bgtPageNum : 2
                     * totalPageNum : 8
                     * totalCount : 74
                     * shixinPageNum : 3
                     */

                    private int zxggPageNum;
                    private int shixinCount;
                    private int ajlcCount;
                    private int range;
                    private int cpwsCount;
                    private int ktggPageNum;
                    private int fyggCount;
                    private String msg;
                    private String code;
                    private int pageNo;
                    private int cpwsPageNum;
                    private int ktggCount;
                    private int wdhmdCount;
                    private int bgtCount;
                    private int zxggCount;
                    private double searchSeconds;
                    private int fyggPageNum;
                    private int wdhmdPageNum;
                    private int ajlcPageNum;
                    private int bgtPageNum;
                    private int totalPageNum;
                    private int totalCount;
                    private int shixinPageNum;

                    public int getZxggPageNum() {
                        return zxggPageNum;
                    }

                    public void setZxggPageNum(int zxggPageNum) {
                        this.zxggPageNum = zxggPageNum;
                    }

                    public int getShixinCount() {
                        return shixinCount;
                    }

                    public void setShixinCount(int shixinCount) {
                        this.shixinCount = shixinCount;
                    }

                    public int getAjlcCount() {
                        return ajlcCount;
                    }

                    public void setAjlcCount(int ajlcCount) {
                        this.ajlcCount = ajlcCount;
                    }

                    public int getRange() {
                        return range;
                    }

                    public void setRange(int range) {
                        this.range = range;
                    }

                    public int getCpwsCount() {
                        return cpwsCount;
                    }

                    public void setCpwsCount(int cpwsCount) {
                        this.cpwsCount = cpwsCount;
                    }

                    public int getKtggPageNum() {
                        return ktggPageNum;
                    }

                    public void setKtggPageNum(int ktggPageNum) {
                        this.ktggPageNum = ktggPageNum;
                    }

                    public int getFyggCount() {
                        return fyggCount;
                    }

                    public void setFyggCount(int fyggCount) {
                        this.fyggCount = fyggCount;
                    }

                    public String getMsg() {
                        return msg;
                    }

                    public void setMsg(String msg) {
                        this.msg = msg;
                    }

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public int getPageNo() {
                        return pageNo;
                    }

                    public void setPageNo(int pageNo) {
                        this.pageNo = pageNo;
                    }

                    public int getCpwsPageNum() {
                        return cpwsPageNum;
                    }

                    public void setCpwsPageNum(int cpwsPageNum) {
                        this.cpwsPageNum = cpwsPageNum;
                    }

                    public int getKtggCount() {
                        return ktggCount;
                    }

                    public void setKtggCount(int ktggCount) {
                        this.ktggCount = ktggCount;
                    }

                    public int getWdhmdCount() {
                        return wdhmdCount;
                    }

                    public void setWdhmdCount(int wdhmdCount) {
                        this.wdhmdCount = wdhmdCount;
                    }

                    public int getBgtCount() {
                        return bgtCount;
                    }

                    public void setBgtCount(int bgtCount) {
                        this.bgtCount = bgtCount;
                    }

                    public int getZxggCount() {
                        return zxggCount;
                    }

                    public void setZxggCount(int zxggCount) {
                        this.zxggCount = zxggCount;
                    }

                    public double getSearchSeconds() {
                        return searchSeconds;
                    }

                    public void setSearchSeconds(double searchSeconds) {
                        this.searchSeconds = searchSeconds;
                    }

                    public int getFyggPageNum() {
                        return fyggPageNum;
                    }

                    public void setFyggPageNum(int fyggPageNum) {
                        this.fyggPageNum = fyggPageNum;
                    }

                    public int getWdhmdPageNum() {
                        return wdhmdPageNum;
                    }

                    public void setWdhmdPageNum(int wdhmdPageNum) {
                        this.wdhmdPageNum = wdhmdPageNum;
                    }

                    public int getAjlcPageNum() {
                        return ajlcPageNum;
                    }

                    public void setAjlcPageNum(int ajlcPageNum) {
                        this.ajlcPageNum = ajlcPageNum;
                    }

                    public int getBgtPageNum() {
                        return bgtPageNum;
                    }

                    public void setBgtPageNum(int bgtPageNum) {
                        this.bgtPageNum = bgtPageNum;
                    }

                    public int getTotalPageNum() {
                        return totalPageNum;
                    }

                    public void setTotalPageNum(int totalPageNum) {
                        this.totalPageNum = totalPageNum;
                    }

                    public int getTotalCount() {
                        return totalCount;
                    }

                    public void setTotalCount(int totalCount) {
                        this.totalCount = totalCount;
                    }

                    public int getShixinPageNum() {
                        return shixinPageNum;
                    }

                    public void setShixinPageNum(int shixinPageNum) {
                        this.shixinPageNum = shixinPageNum;
                    }
                }

                public class DetailedBean implements Serializable{
                    /**
                     * status : 0
                     * body : null
                     * proposer : null
                     * dataType : zxgg
                     * pname : 福安市正凯船务有限公司
                     * caseState : 0
                     * zxggId : c20173509zhihui122_t20170413_pfaszkcwyxgs
                     * caseNo : （2017）闽09执恢122号
                     * court : 宁德市中级人民法院
                     * sortTime : 1492012800000
                     * title : null
                     * idcardNo : 75139468-8
                     * execMoney : 425000
                     * wdhmd : [{"sortTime":1446307200000,"body":null,"sex":null,"phone":null,"updateTime":null,"execCourt":null,"relatedParty":null,"whfx":null,"idcardNo":"44152219950625****","birthPlace":null,"bjbx":"3000.00","caseCode":null,"address":"福建省宁德市蕉城区木材公司","email":"qyl_888@126.com","sourceName":"拍来贷","age":null,"pname":"张冬澍","wdhmdId":"AVMH_Ld5fu8bWnowhLd5","datasource":null,"yhje":"7647.33","mobile":"18605051668"}]
                     * relatedParty : null
                     * province : 福建
                     * age : 47
                     * yjdw : 福安市人民法院
                     * shixinId : c2016350981zhi2826_t20160819_plirenjie
                     * postTime : 1478448000000
                     * yiwu :
                     * jtqx : 违反财产报告制度
                     * sex : 男
                     * yjCode : (2015)安民初字第1178号
                     * lxqk : 全部未履行
                     * ktgg : [{"sortTime":1525622400000,"body":null,"ktggId":"AU2AWyb76aXWqCOGevnG","plaintiff":null,"organizer":null,"courtroom":null,"relatedParty":null,"court":"舟山市普陀区人民法院","party":null,"title":null,"caseCause":"","judge":null,"caseNo":"（2009）舟普民执字第636号","defendant":null}]
                     * bgt : [{"sortTime":1419868800000,"body":"","bgDate":1427644800000,"partyType":"P","court":"","proposer":null,"idcardNo":"","bgtId":"","caseCause":"","unnexeMoney":28111.82,"address":null,"pname":"张美根","caseNo":"","yiju":"","execMoney":28111.82}]
                     * cpws : [{"sortTime":4277116800000,"body":"","caseType":"mscd","cpwsId":"17809422","trialProcedure":"","court":"","anyou":"A0820229","partys":[{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":null,"status":"d","pname":"付广卫","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null}],"title":"","caseCause":"","judge":null,"caseNo":"（2015）临法商初字第787号","judgeResult":"","anyouType":"A","yiju":"","courtRank":4}]
                     * ajlc : [{"member":null,"sortTime":1476806400000,"clerkPhone":null,"caseType":"民事","body":"","trialProcedure":"司法确认","ajlcId":"AVfoHxQuxL_h1T6qAQiQ","sentencingDate":null,"status":1,"caseStatus":0,"organizer":"附城人民法庭","filingDate":null,"court":null,"ajlcStatus":"已立案","chiefJudge":null,"caseCause":"请求确认人民调解协议效力","trialLimitDate":"2015-05-27","clerk":"陈采红","judge":"杨伟雄","actionObject":"0","pname":"","caseNo":"（2014）梅丰法附调确字第27号","effectiveDate":null}]
                     * ggType : 裁判文书
                     * fyggId : c2015anminchu1178_t20160518
                     * layout : null
                     */

                    private int status;
                    private Object body;
                    private Object proposer;
                    private String dataType;
                    private String pname;
                    private String caseState;
                    private String zxggId;
                    private String caseNo;
                    private String court;
                    private long sortTime;
                    private Object title;
                    private String idcardNo;
                    private int execMoney;
                    private Object relatedParty;
                    private String province;
                    private int age;
                    private String yjdw;
                    private String shixinId;
                    private long postTime;
                    private String yiwu;
                    private String jtqx;
                    private String sex;
                    private String yjCode;
                    private String lxqk;
                    private String ggType;
                    private String fyggId;
                    private Object layout;
                    private List<WdhmdBean> wdhmd;
                    private List<KtggBean> ktgg;
                    private List<BgtBean> bgt;
                    private List<CpwsBean> cpws;
                    private List<AjlcBean> ajlc;

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public Object getBody() {
                        return body;
                    }

                    public void setBody(Object body) {
                        this.body = body;
                    }

                    public Object getProposer() {
                        return proposer;
                    }

                    public void setProposer(Object proposer) {
                        this.proposer = proposer;
                    }

                    public String getDataType() {
                        return dataType;
                    }

                    public void setDataType(String dataType) {
                        this.dataType = dataType;
                    }

                    public String getPname() {
                        return pname;
                    }

                    public void setPname(String pname) {
                        this.pname = pname;
                    }

                    public String getCaseState() {
                        return caseState;
                    }

                    public void setCaseState(String caseState) {
                        this.caseState = caseState;
                    }

                    public String getZxggId() {
                        return zxggId;
                    }

                    public void setZxggId(String zxggId) {
                        this.zxggId = zxggId;
                    }

                    public String getCaseNo() {
                        return caseNo;
                    }

                    public void setCaseNo(String caseNo) {
                        this.caseNo = caseNo;
                    }

                    public String getCourt() {
                        return court;
                    }

                    public void setCourt(String court) {
                        this.court = court;
                    }

                    public long getSortTime() {
                        return sortTime;
                    }

                    public void setSortTime(long sortTime) {
                        this.sortTime = sortTime;
                    }

                    public Object getTitle() {
                        return title;
                    }

                    public void setTitle(Object title) {
                        this.title = title;
                    }

                    public String getIdcardNo() {
                        return idcardNo;
                    }

                    public void setIdcardNo(String idcardNo) {
                        this.idcardNo = idcardNo;
                    }

                    public int getExecMoney() {
                        return execMoney;
                    }

                    public void setExecMoney(int execMoney) {
                        this.execMoney = execMoney;
                    }

                    public Object getRelatedParty() {
                        return relatedParty;
                    }

                    public void setRelatedParty(Object relatedParty) {
                        this.relatedParty = relatedParty;
                    }

                    public String getProvince() {
                        return province;
                    }

                    public void setProvince(String province) {
                        this.province = province;
                    }

                    public int getAge() {
                        return age;
                    }

                    public void setAge(int age) {
                        this.age = age;
                    }

                    public String getYjdw() {
                        return yjdw;
                    }

                    public void setYjdw(String yjdw) {
                        this.yjdw = yjdw;
                    }

                    public String getShixinId() {
                        return shixinId;
                    }

                    public void setShixinId(String shixinId) {
                        this.shixinId = shixinId;
                    }

                    public long getPostTime() {
                        return postTime;
                    }

                    public void setPostTime(long postTime) {
                        this.postTime = postTime;
                    }

                    public String getYiwu() {
                        return yiwu;
                    }

                    public void setYiwu(String yiwu) {
                        this.yiwu = yiwu;
                    }

                    public String getJtqx() {
                        return jtqx;
                    }

                    public void setJtqx(String jtqx) {
                        this.jtqx = jtqx;
                    }

                    public String getSex() {
                        return sex;
                    }

                    public void setSex(String sex) {
                        this.sex = sex;
                    }

                    public String getYjCode() {
                        return yjCode;
                    }

                    public void setYjCode(String yjCode) {
                        this.yjCode = yjCode;
                    }

                    public String getLxqk() {
                        return lxqk;
                    }

                    public void setLxqk(String lxqk) {
                        this.lxqk = lxqk;
                    }

                    public String getGgType() {
                        return ggType;
                    }

                    public void setGgType(String ggType) {
                        this.ggType = ggType;
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

                    public List<WdhmdBean> getWdhmd() {
                        return wdhmd;
                    }

                    public void setWdhmd(List<WdhmdBean> wdhmd) {
                        this.wdhmd = wdhmd;
                    }

                    public List<KtggBean> getKtgg() {
                        return ktgg;
                    }

                    public void setKtgg(List<KtggBean> ktgg) {
                        this.ktgg = ktgg;
                    }

                    public List<BgtBean> getBgt() {
                        return bgt;
                    }

                    public void setBgt(List<BgtBean> bgt) {
                        this.bgt = bgt;
                    }

                    public List<CpwsBean> getCpws() {
                        return cpws;
                    }

                    public void setCpws(List<CpwsBean> cpws) {
                        this.cpws = cpws;
                    }

                    public List<AjlcBean> getAjlc() {
                        return ajlc;
                    }

                    public void setAjlc(List<AjlcBean> ajlc) {
                        this.ajlc = ajlc;
                    }

                    public class WdhmdBean implements Serializable{
                        /**
                         * sortTime : 1446307200000
                         * body : null
                         * sex : null
                         * phone : null
                         * updateTime : null
                         * execCourt : null
                         * relatedParty : null
                         * whfx : null
                         * idcardNo : 44152219950625****
                         * birthPlace : null
                         * bjbx : 3000.00
                         * caseCode : null
                         * address : 福建省宁德市蕉城区木材公司
                         * email : qyl_888@126.com
                         * sourceName : 拍来贷
                         * age : null
                         * pname : 张冬澍
                         * wdhmdId : AVMH_Ld5fu8bWnowhLd5
                         * datasource : null
                         * yhje : 7647.33
                         * mobile : 18605051668
                         */

                        private long sortTime;
                        private Object body;
                        private Object sex;
                        private Object phone;
                        private Object updateTime;
                        private Object execCourt;
                        private Object relatedParty;
                        private Object whfx;
                        private String idcardNo;
                        private Object birthPlace;
                        private String bjbx;
                        private Object caseCode;
                        private String address;
                        private String email;
                        private String sourceName;
                        private Object age;
                        private String pname;
                        private String wdhmdId;
                        private Object datasource;
                        private String yhje;
                        private String mobile;

                        public long getSortTime() {
                            return sortTime;
                        }

                        public void setSortTime(long sortTime) {
                            this.sortTime = sortTime;
                        }

                        public Object getBody() {
                            return body;
                        }

                        public void setBody(Object body) {
                            this.body = body;
                        }

                        public Object getSex() {
                            return sex;
                        }

                        public void setSex(Object sex) {
                            this.sex = sex;
                        }

                        public Object getPhone() {
                            return phone;
                        }

                        public void setPhone(Object phone) {
                            this.phone = phone;
                        }

                        public Object getUpdateTime() {
                            return updateTime;
                        }

                        public void setUpdateTime(Object updateTime) {
                            this.updateTime = updateTime;
                        }

                        public Object getExecCourt() {
                            return execCourt;
                        }

                        public void setExecCourt(Object execCourt) {
                            this.execCourt = execCourt;
                        }

                        public Object getRelatedParty() {
                            return relatedParty;
                        }

                        public void setRelatedParty(Object relatedParty) {
                            this.relatedParty = relatedParty;
                        }

                        public Object getWhfx() {
                            return whfx;
                        }

                        public void setWhfx(Object whfx) {
                            this.whfx = whfx;
                        }

                        public String getIdcardNo() {
                            return idcardNo;
                        }

                        public void setIdcardNo(String idcardNo) {
                            this.idcardNo = idcardNo;
                        }

                        public Object getBirthPlace() {
                            return birthPlace;
                        }

                        public void setBirthPlace(Object birthPlace) {
                            this.birthPlace = birthPlace;
                        }

                        public String getBjbx() {
                            return bjbx;
                        }

                        public void setBjbx(String bjbx) {
                            this.bjbx = bjbx;
                        }

                        public Object getCaseCode() {
                            return caseCode;
                        }

                        public void setCaseCode(Object caseCode) {
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

                        public Object getAge() {
                            return age;
                        }

                        public void setAge(Object age) {
                            this.age = age;
                        }

                        public String getPname() {
                            return pname;
                        }

                        public void setPname(String pname) {
                            this.pname = pname;
                        }

                        public String getWdhmdId() {
                            return wdhmdId;
                        }

                        public void setWdhmdId(String wdhmdId) {
                            this.wdhmdId = wdhmdId;
                        }

                        public Object getDatasource() {
                            return datasource;
                        }

                        public void setDatasource(Object datasource) {
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
                    }

                    public class KtggBean implements Serializable{
                        /**
                         * sortTime : 1525622400000
                         * body : null
                         * ktggId : AU2AWyb76aXWqCOGevnG
                         * plaintiff : null
                         * organizer : null
                         * courtroom : null
                         * relatedParty : null
                         * court : 舟山市普陀区人民法院
                         * party : null
                         * title : null
                         * caseCause :
                         * judge : null
                         * caseNo : （2009）舟普民执字第636号
                         * defendant : null
                         */

                        private long sortTime;
                        private Object body;
                        private String ktggId;
                        private Object plaintiff;
                        private Object organizer;
                        private Object courtroom;
                        private Object relatedParty;
                        private String court;
                        private Object party;
                        private Object title;
                        private String caseCause;
                        private Object judge;
                        private String caseNo;
                        private Object defendant;

                        public long getSortTime() {
                            return sortTime;
                        }

                        public void setSortTime(long sortTime) {
                            this.sortTime = sortTime;
                        }

                        public Object getBody() {
                            return body;
                        }

                        public void setBody(Object body) {
                            this.body = body;
                        }

                        public String getKtggId() {
                            return ktggId;
                        }

                        public void setKtggId(String ktggId) {
                            this.ktggId = ktggId;
                        }

                        public Object getPlaintiff() {
                            return plaintiff;
                        }

                        public void setPlaintiff(Object plaintiff) {
                            this.plaintiff = plaintiff;
                        }

                        public Object getOrganizer() {
                            return organizer;
                        }

                        public void setOrganizer(Object organizer) {
                            this.organizer = organizer;
                        }

                        public Object getCourtroom() {
                            return courtroom;
                        }

                        public void setCourtroom(Object courtroom) {
                            this.courtroom = courtroom;
                        }

                        public Object getRelatedParty() {
                            return relatedParty;
                        }

                        public void setRelatedParty(Object relatedParty) {
                            this.relatedParty = relatedParty;
                        }

                        public String getCourt() {
                            return court;
                        }

                        public void setCourt(String court) {
                            this.court = court;
                        }

                        public Object getParty() {
                            return party;
                        }

                        public void setParty(Object party) {
                            this.party = party;
                        }

                        public Object getTitle() {
                            return title;
                        }

                        public void setTitle(Object title) {
                            this.title = title;
                        }

                        public String getCaseCause() {
                            return caseCause;
                        }

                        public void setCaseCause(String caseCause) {
                            this.caseCause = caseCause;
                        }

                        public Object getJudge() {
                            return judge;
                        }

                        public void setJudge(Object judge) {
                            this.judge = judge;
                        }

                        public String getCaseNo() {
                            return caseNo;
                        }

                        public void setCaseNo(String caseNo) {
                            this.caseNo = caseNo;
                        }

                        public Object getDefendant() {
                            return defendant;
                        }

                        public void setDefendant(Object defendant) {
                            this.defendant = defendant;
                        }
                    }

                    public class BgtBean implements Serializable{
                        /**
                         * sortTime : 1419868800000
                         * body :
                         * bgDate : 1427644800000
                         * partyType : P
                         * court :
                         * proposer : null
                         * idcardNo :
                         * bgtId :
                         * caseCause :
                         * unnexeMoney : 28111.82
                         * address : null
                         * pname : 张美根
                         * caseNo :
                         * yiju :
                         * execMoney : 28111.82
                         */

                        private long sortTime;
                        private String body;
                        private long bgDate;
                        private String partyType;
                        private String court;
                        private Object proposer;
                        private String idcardNo;
                        private String bgtId;
                        private String caseCause;
                        private double unnexeMoney;
                        private Object address;
                        private String pname;
                        private String caseNo;
                        private String yiju;
                        private double execMoney;

                        public long getSortTime() {
                            return sortTime;
                        }

                        public void setSortTime(long sortTime) {
                            this.sortTime = sortTime;
                        }

                        public String getBody() {
                            return body;
                        }

                        public void setBody(String body) {
                            this.body = body;
                        }

                        public long getBgDate() {
                            return bgDate;
                        }

                        public void setBgDate(long bgDate) {
                            this.bgDate = bgDate;
                        }

                        public String getPartyType() {
                            return partyType;
                        }

                        public void setPartyType(String partyType) {
                            this.partyType = partyType;
                        }

                        public String getCourt() {
                            return court;
                        }

                        public void setCourt(String court) {
                            this.court = court;
                        }

                        public Object getProposer() {
                            return proposer;
                        }

                        public void setProposer(Object proposer) {
                            this.proposer = proposer;
                        }

                        public String getIdcardNo() {
                            return idcardNo;
                        }

                        public void setIdcardNo(String idcardNo) {
                            this.idcardNo = idcardNo;
                        }

                        public String getBgtId() {
                            return bgtId;
                        }

                        public void setBgtId(String bgtId) {
                            this.bgtId = bgtId;
                        }

                        public String getCaseCause() {
                            return caseCause;
                        }

                        public void setCaseCause(String caseCause) {
                            this.caseCause = caseCause;
                        }

                        public double getUnnexeMoney() {
                            return unnexeMoney;
                        }

                        public void setUnnexeMoney(double unnexeMoney) {
                            this.unnexeMoney = unnexeMoney;
                        }

                        public Object getAddress() {
                            return address;
                        }

                        public void setAddress(Object address) {
                            this.address = address;
                        }

                        public String getPname() {
                            return pname;
                        }

                        public void setPname(String pname) {
                            this.pname = pname;
                        }

                        public String getCaseNo() {
                            return caseNo;
                        }

                        public void setCaseNo(String caseNo) {
                            this.caseNo = caseNo;
                        }

                        public String getYiju() {
                            return yiju;
                        }

                        public void setYiju(String yiju) {
                            this.yiju = yiju;
                        }

                        public double getExecMoney() {
                            return execMoney;
                        }

                        public void setExecMoney(double execMoney) {
                            this.execMoney = execMoney;
                        }
                    }

                    public class CpwsBean implements Serializable{
                        /**
                         * sortTime : 4277116800000
                         * body :
                         * caseType : mscd
                         * cpwsId : 17809422
                         * trialProcedure :
                         * court :
                         * anyou : A0820229
                         * partys : [{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":null,"status":"d","pname":"付广卫","idcardNo":null,"lawyer":null},{"birthday":null,"title":"","partyType":"","lawOffice":null,"address":"","status":"d","pname":"","idcardNo":null,"lawyer":null}]
                         * title :
                         * caseCause :
                         * judge : null
                         * caseNo : （2015）临法商初字第787号
                         * judgeResult :
                         * anyouType : A
                         * yiju :
                         * courtRank : 4
                         */

                        private long sortTime;
                        private String body;
                        private String caseType;
                        private String cpwsId;
                        private String trialProcedure;
                        private String court;
                        private String anyou;
                        private String title;
                        private String caseCause;
                        private Object judge;
                        private String caseNo;
                        private String judgeResult;
                        private String anyouType;
                        private String yiju;
                        private int courtRank;
                        private List<PartysBean> partys;

                        public long getSortTime() {
                            return sortTime;
                        }

                        public void setSortTime(long sortTime) {
                            this.sortTime = sortTime;
                        }

                        public String getBody() {
                            return body;
                        }

                        public void setBody(String body) {
                            this.body = body;
                        }

                        public String getCaseType() {
                            return caseType;
                        }

                        public void setCaseType(String caseType) {
                            this.caseType = caseType;
                        }

                        public String getCpwsId() {
                            return cpwsId;
                        }

                        public void setCpwsId(String cpwsId) {
                            this.cpwsId = cpwsId;
                        }

                        public String getTrialProcedure() {
                            return trialProcedure;
                        }

                        public void setTrialProcedure(String trialProcedure) {
                            this.trialProcedure = trialProcedure;
                        }

                        public String getCourt() {
                            return court;
                        }

                        public void setCourt(String court) {
                            this.court = court;
                        }

                        public String getAnyou() {
                            return anyou;
                        }

                        public void setAnyou(String anyou) {
                            this.anyou = anyou;
                        }

                        public String getTitle() {
                            return title;
                        }

                        public void setTitle(String title) {
                            this.title = title;
                        }

                        public String getCaseCause() {
                            return caseCause;
                        }

                        public void setCaseCause(String caseCause) {
                            this.caseCause = caseCause;
                        }

                        public Object getJudge() {
                            return judge;
                        }

                        public void setJudge(Object judge) {
                            this.judge = judge;
                        }

                        public String getCaseNo() {
                            return caseNo;
                        }

                        public void setCaseNo(String caseNo) {
                            this.caseNo = caseNo;
                        }

                        public String getJudgeResult() {
                            return judgeResult;
                        }

                        public void setJudgeResult(String judgeResult) {
                            this.judgeResult = judgeResult;
                        }

                        public String getAnyouType() {
                            return anyouType;
                        }

                        public void setAnyouType(String anyouType) {
                            this.anyouType = anyouType;
                        }

                        public String getYiju() {
                            return yiju;
                        }

                        public void setYiju(String yiju) {
                            this.yiju = yiju;
                        }

                        public int getCourtRank() {
                            return courtRank;
                        }

                        public void setCourtRank(int courtRank) {
                            this.courtRank = courtRank;
                        }

                        public List<PartysBean> getPartys() {
                            return partys;
                        }

                        public void setPartys(List<PartysBean> partys) {
                            this.partys = partys;
                        }

                        public class PartysBean implements Serializable{
                            /**
                             * birthday : null
                             * title :
                             * partyType :
                             * lawOffice : null
                             * address :
                             * status : d
                             * pname :
                             * idcardNo : null
                             * lawyer : null
                             */

                            private Object birthday;
                            private String title;
                            private String partyType;
                            private Object lawOffice;
                            private String address;
                            private String status;
                            private String pname;
                            private Object idcardNo;
                            private Object lawyer;

                            public Object getBirthday() {
                                return birthday;
                            }

                            public void setBirthday(Object birthday) {
                                this.birthday = birthday;
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

                            public Object getLawOffice() {
                                return lawOffice;
                            }

                            public void setLawOffice(Object lawOffice) {
                                this.lawOffice = lawOffice;
                            }

                            public String getAddress() {
                                return address;
                            }

                            public void setAddress(String address) {
                                this.address = address;
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

                            public Object getIdcardNo() {
                                return idcardNo;
                            }

                            public void setIdcardNo(Object idcardNo) {
                                this.idcardNo = idcardNo;
                            }

                            public Object getLawyer() {
                                return lawyer;
                            }

                            public void setLawyer(Object lawyer) {
                                this.lawyer = lawyer;
                            }
                        }
                    }

                    public class AjlcBean implements Serializable{
                        /**
                         * member : null
                         * sortTime : 1476806400000
                         * clerkPhone : null
                         * caseType : 民事
                         * body :
                         * trialProcedure : 司法确认
                         * ajlcId : AVfoHxQuxL_h1T6qAQiQ
                         * sentencingDate : null
                         * status : 1
                         * caseStatus : 0
                         * organizer : 附城人民法庭
                         * filingDate : null
                         * court : null
                         * ajlcStatus : 已立案
                         * chiefJudge : null
                         * caseCause : 请求确认人民调解协议效力
                         * trialLimitDate : 2015-05-27
                         * clerk : 陈采红
                         * judge : 杨伟雄
                         * actionObject : 0
                         * pname :
                         * caseNo : （2014）梅丰法附调确字第27号
                         * effectiveDate : null
                         */

                        private Object member;
                        private long sortTime;
                        private Object clerkPhone;
                        private String caseType;
                        private String body;
                        private String trialProcedure;
                        private String ajlcId;
                        private Object sentencingDate;
                        private int status;
                        private int caseStatus;
                        private String organizer;
                        private Object filingDate;
                        private Object court;
                        private String ajlcStatus;
                        private Object chiefJudge;
                        private String caseCause;
                        private String trialLimitDate;
                        private String clerk;
                        private String judge;
                        private String actionObject;
                        private String pname;
                        private String caseNo;
                        private Object effectiveDate;

                        public Object getMember() {
                            return member;
                        }

                        public void setMember(Object member) {
                            this.member = member;
                        }

                        public long getSortTime() {
                            return sortTime;
                        }

                        public void setSortTime(long sortTime) {
                            this.sortTime = sortTime;
                        }

                        public Object getClerkPhone() {
                            return clerkPhone;
                        }

                        public void setClerkPhone(Object clerkPhone) {
                            this.clerkPhone = clerkPhone;
                        }

                        public String getCaseType() {
                            return caseType;
                        }

                        public void setCaseType(String caseType) {
                            this.caseType = caseType;
                        }

                        public String getBody() {
                            return body;
                        }

                        public void setBody(String body) {
                            this.body = body;
                        }

                        public String getTrialProcedure() {
                            return trialProcedure;
                        }

                        public void setTrialProcedure(String trialProcedure) {
                            this.trialProcedure = trialProcedure;
                        }

                        public String getAjlcId() {
                            return ajlcId;
                        }

                        public void setAjlcId(String ajlcId) {
                            this.ajlcId = ajlcId;
                        }

                        public Object getSentencingDate() {
                            return sentencingDate;
                        }

                        public void setSentencingDate(Object sentencingDate) {
                            this.sentencingDate = sentencingDate;
                        }

                        public int getStatus() {
                            return status;
                        }

                        public void setStatus(int status) {
                            this.status = status;
                        }

                        public int getCaseStatus() {
                            return caseStatus;
                        }

                        public void setCaseStatus(int caseStatus) {
                            this.caseStatus = caseStatus;
                        }

                        public String getOrganizer() {
                            return organizer;
                        }

                        public void setOrganizer(String organizer) {
                            this.organizer = organizer;
                        }

                        public Object getFilingDate() {
                            return filingDate;
                        }

                        public void setFilingDate(Object filingDate) {
                            this.filingDate = filingDate;
                        }

                        public Object getCourt() {
                            return court;
                        }

                        public void setCourt(Object court) {
                            this.court = court;
                        }

                        public String getAjlcStatus() {
                            return ajlcStatus;
                        }

                        public void setAjlcStatus(String ajlcStatus) {
                            this.ajlcStatus = ajlcStatus;
                        }

                        public Object getChiefJudge() {
                            return chiefJudge;
                        }

                        public void setChiefJudge(Object chiefJudge) {
                            this.chiefJudge = chiefJudge;
                        }

                        public String getCaseCause() {
                            return caseCause;
                        }

                        public void setCaseCause(String caseCause) {
                            this.caseCause = caseCause;
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

                        public String getJudge() {
                            return judge;
                        }

                        public void setJudge(String judge) {
                            this.judge = judge;
                        }

                        public String getActionObject() {
                            return actionObject;
                        }

                        public void setActionObject(String actionObject) {
                            this.actionObject = actionObject;
                        }

                        public String getPname() {
                            return pname;
                        }

                        public void setPname(String pname) {
                            this.pname = pname;
                        }

                        public String getCaseNo() {
                            return caseNo;
                        }

                        public void setCaseNo(String caseNo) {
                            this.caseNo = caseNo;
                        }

                        public Object getEffectiveDate() {
                            return effectiveDate;
                        }

                        public void setEffectiveDate(Object effectiveDate) {
                            this.effectiveDate = effectiveDate;
                        }
                    }
                }
            }
        }
    }
}
