package com.fuze.bcp.api.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 工商企业查询
 * Created by Lily on 2017/8/3.
 */
public class BusinessDataBean implements Serializable {
    /**
     * code : 10000
     * charge : false
     * msg : 查询成功
     * result : {"Status":"200","Message":"查询成功","Result":{"TermStart":"2010-03-03T00:00:00+08:00","TeamEnd":"2030-03-02T00:00:00+08:00","CheckDate":"2016-04-20T00:00:00+08:00","Partners":[{"StockName":"刘德","StockType":"自然人股东","StockPercent":null,"IdentifyType":"","IdentifyNo":"","ShouldCapi":null,"ShoudDate":null,"InvestType":null,"InvestName":null,"RealCapi":null,"CapiDate":null,"Address":null}],"Employees":[{"Name":"黎万强","Job":"监事","CerNo":"","ScertName":""}],"Branches":[],"ChangeRecords":[{"ProjectName":"注册资本","BeforeContent":"5000万元","AfterContent":"185000万元","ChangeDate":"2016-03-24T00:00:00+08:00"}],"ContactInfo":{"WebSite":[{"Name":"小米科技","Url":"www.xiaomi.com"}],"PhoneNumber":"60606666-1000","Email":"chenchongwei@xiaomi.com"},"KeyNo":"9cce0780ab7644008b73bc2120479d31","Name":"小米科技有限责任公司","No":"110108012660422","BelongOrg":"海淀分局","OperName":"雷军","StartDate":"2010-03-03T00:00:00+08:00","EndDate":null,"Status":"在营（开业）企业","Province":"BJ","UpdatedDate":"2016-04-23T14:07:16.578+08:00","CreditCode":"91110108551385082Q","RegistCapi":"185000 万元","EconKind":"有限责任公司(自然人投资或控股)","Address":"北京市海淀区清河中街68号华润五彩城购物中心二期13层","Scope":"手机技术开发；手机生产、手机服务。（限海淀区永捷北路2号二层经营）；技术开发；货物进出口、技术进出口、代理进出口；销售通讯设备；维修仪器仪表；维修办公设备；承办展览展示活动；会议服务；筹备、策划、组织大型庆典。（企业依法自主选择经营项目，开展经营活动；依法须经批准的项目，经相关部门批准后依批准的内容开展经营活动；不得从事本市产业政策禁止和限制类项目的经营活动。）"}}
     */

    private String code;
    private Boolean charge;
    private String msg;
    private ResultBeanX result;

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

    public ResultBeanX getResult() {
        return result;
    }

    public void setResult(ResultBeanX result) {
        this.result = result;
    }

    public class ResultBeanX implements Serializable {
        /**
         * Status : 200
         * Message : 查询成功
         * Result : {"TermStart":"2010-03-03T00:00:00+08:00","TeamEnd":"2030-03-02T00:00:00+08:00","CheckDate":"2016-04-20T00:00:00+08:00","Partners":[{"StockName":"刘德","StockType":"自然人股东","StockPercent":null,"IdentifyType":"","IdentifyNo":"","ShouldCapi":null,"ShoudDate":null,"InvestType":null,"InvestName":null,"RealCapi":null,"CapiDate":null,"Address":null}],"Employees":[{"Name":"黎万强","Job":"监事","CerNo":"","ScertName":""}],"Branches":[],"ChangeRecords":[{"ProjectName":"注册资本","BeforeContent":"5000万元","AfterContent":"185000万元","ChangeDate":"2016-03-24T00:00:00+08:00"}],"ContactInfo":{"WebSite":[{"Name":"小米科技","Url":"www.xiaomi.com"}],"PhoneNumber":"60606666-1000","Email":"chenchongwei@xiaomi.com"},"KeyNo":"9cce0780ab7644008b73bc2120479d31","Name":"小米科技有限责任公司","No":"110108012660422","BelongOrg":"海淀分局","OperName":"雷军","StartDate":"2010-03-03T00:00:00+08:00","EndDate":null,"Status":"在营（开业）企业","Province":"BJ","UpdatedDate":"2016-04-23T14:07:16.578+08:00","CreditCode":"91110108551385082Q","RegistCapi":"185000 万元","EconKind":"有限责任公司(自然人投资或控股)","Address":"北京市海淀区清河中街68号华润五彩城购物中心二期13层","Scope":"手机技术开发；手机生产、手机服务。（限海淀区永捷北路2号二层经营）；技术开发；货物进出口、技术进出口、代理进出口；销售通讯设备；维修仪器仪表；维修办公设备；承办展览展示活动；会议服务；筹备、策划、组织大型庆典。（企业依法自主选择经营项目，开展经营活动；依法须经批准的项目，经相关部门批准后依批准的内容开展经营活动；不得从事本市产业政策禁止和限制类项目的经营活动。）"}
         */

        private String Status;
        private String Message;
        private ResultBean Result;

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public ResultBean getResult() {
            return Result;
        }

        public void setResult(ResultBean Result) {
            this.Result = Result;
        }

        public class ResultBean implements Serializable{
            /**
             * TermStart : 2010-03-03T00:00:00+08:00
             * TeamEnd : 2030-03-02T00:00:00+08:00
             * CheckDate : 2016-04-20T00:00:00+08:00
             * Partners : [{"StockName":"刘德","StockType":"自然人股东","StockPercent":null,"IdentifyType":"","IdentifyNo":"","ShouldCapi":null,"ShoudDate":null,"InvestType":null,"InvestName":null,"RealCapi":null,"CapiDate":null,"Address":null}]
             * Employees : [{"Name":"黎万强","Job":"监事","CerNo":"","ScertName":""}]
             * Branches : []
             * ChangeRecords : [{"ProjectName":"注册资本","BeforeContent":"5000万元","AfterContent":"185000万元","ChangeDate":"2016-03-24T00:00:00+08:00"}]
             * ContactInfo : {"WebSite":[{"Name":"小米科技","Url":"www.xiaomi.com"}],"PhoneNumber":"60606666-1000","Email":"chenchongwei@xiaomi.com"}
             * KeyNo : 9cce0780ab7644008b73bc2120479d31
             * Name : 小米科技有限责任公司
             * No : 110108012660422
             * BelongOrg : 海淀分局
             * OperName : 雷军
             * StartDate : 2010-03-03T00:00:00+08:00
             * EndDate : null
             * Status : 在营（开业）企业
             * Province : BJ
             * UpdatedDate : 2016-04-23T14:07:16.578+08:00
             * CreditCode : 91110108551385082Q
             * RegistCapi : 185000 万元
             * EconKind : 有限责任公司(自然人投资或控股)
             * Address : 北京市海淀区清河中街68号华润五彩城购物中心二期13层
             * Scope : 手机技术开发；手机生产、手机服务。（限海淀区永捷北路2号二层经营）；技术开发；货物进出口、技术进出口、代理进出口；销售通讯设备；维修仪器仪表；维修办公设备；承办展览展示活动；会议服务；筹备、策划、组织大型庆典。（企业依法自主选择经营项目，开展经营活动；依法须经批准的项目，经相关部门批准后依批准的内容开展经营活动；不得从事本市产业政策禁止和限制类项目的经营活动。）
             */

            private String TermStart;
            private String TeamEnd;
            private String CheckDate;
            private ContactInfoBean ContactInfo;
            private String KeyNo;
            private String Name;
            private String No;
            private String BelongOrg;
            private String OperName;
            private String StartDate;
            private Object EndDate;
            private String Status;
            private String Province;
            private String UpdatedDate;
            private String CreditCode;
            private String RegistCapi;
            private String EconKind;
            private String Address;
            private String Scope;
            private List<PartnersBean> Partners;
            private List<EmployeesBean> Employees;
            private List<?> Branches;
            private List<ChangeRecordsBean> ChangeRecords;

            public String getTermStart() {
                return TermStart;
            }

            public void setTermStart(String TermStart) {
                this.TermStart = TermStart;
            }

            public String getTeamEnd() {
                return TeamEnd;
            }

            public void setTeamEnd(String TeamEnd) {
                this.TeamEnd = TeamEnd;
            }

            public String getCheckDate() {
                return CheckDate;
            }

            public void setCheckDate(String CheckDate) {
                this.CheckDate = CheckDate;
            }

            public ContactInfoBean getContactInfo() {
                return ContactInfo;
            }

            public void setContactInfo(ContactInfoBean ContactInfo) {
                this.ContactInfo = ContactInfo;
            }

            public String getKeyNo() {
                return KeyNo;
            }

            public void setKeyNo(String KeyNo) {
                this.KeyNo = KeyNo;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getNo() {
                return No;
            }

            public void setNo(String No) {
                this.No = No;
            }

            public String getBelongOrg() {
                return BelongOrg;
            }

            public void setBelongOrg(String BelongOrg) {
                this.BelongOrg = BelongOrg;
            }

            public String getOperName() {
                return OperName;
            }

            public void setOperName(String OperName) {
                this.OperName = OperName;
            }

            public String getStartDate() {
                return StartDate;
            }

            public void setStartDate(String StartDate) {
                this.StartDate = StartDate;
            }

            public Object getEndDate() {
                return EndDate;
            }

            public void setEndDate(Object EndDate) {
                this.EndDate = EndDate;
            }

            public String getStatus() {
                return Status;
            }

            public void setStatus(String Status) {
                this.Status = Status;
            }

            public String getProvince() {
                return Province;
            }

            public void setProvince(String Province) {
                this.Province = Province;
            }

            public String getUpdatedDate() {
                return UpdatedDate;
            }

            public void setUpdatedDate(String UpdatedDate) {
                this.UpdatedDate = UpdatedDate;
            }

            public String getCreditCode() {
                return CreditCode;
            }

            public void setCreditCode(String CreditCode) {
                this.CreditCode = CreditCode;
            }

            public String getRegistCapi() {
                return RegistCapi;
            }

            public void setRegistCapi(String RegistCapi) {
                this.RegistCapi = RegistCapi;
            }

            public String getEconKind() {
                return EconKind;
            }

            public void setEconKind(String EconKind) {
                this.EconKind = EconKind;
            }

            public String getAddress() {
                return Address;
            }

            public void setAddress(String Address) {
                this.Address = Address;
            }

            public String getScope() {
                return Scope;
            }

            public void setScope(String Scope) {
                this.Scope = Scope;
            }

            public List<PartnersBean> getPartners() {
                return Partners;
            }

            public void setPartners(List<PartnersBean> Partners) {
                this.Partners = Partners;
            }

            public List<EmployeesBean> getEmployees() {
                return Employees;
            }

            public void setEmployees(List<EmployeesBean> Employees) {
                this.Employees = Employees;
            }

            public List<?> getBranches() {
                return Branches;
            }

            public void setBranches(List<?> Branches) {
                this.Branches = Branches;
            }

            public List<ChangeRecordsBean> getChangeRecords() {
                return ChangeRecords;
            }

            public void setChangeRecords(List<ChangeRecordsBean> ChangeRecords) {
                this.ChangeRecords = ChangeRecords;
            }

            public class ContactInfoBean implements Serializable{
                /**
                 * WebSite : [{"Name":"小米科技","Url":"www.xiaomi.com"}]
                 * PhoneNumber : 60606666-1000
                 * Email : chenchongwei@xiaomi.com
                 */

                private String PhoneNumber;
                private String Email;
                private List<WebSiteBean> WebSite;

                public String getPhoneNumber() {
                    return PhoneNumber;
                }

                public void setPhoneNumber(String PhoneNumber) {
                    this.PhoneNumber = PhoneNumber;
                }

                public String getEmail() {
                    return Email;
                }

                public void setEmail(String Email) {
                    this.Email = Email;
                }

                public List<WebSiteBean> getWebSite() {
                    return WebSite;
                }

                public void setWebSite(List<WebSiteBean> WebSite) {
                    this.WebSite = WebSite;
                }

                public class WebSiteBean implements Serializable{
                    /**
                     * Name : 小米科技
                     * Url : www.xiaomi.com
                     */

                    private String Name;
                    private String Url;

                    public String getName() {
                        return Name;
                    }

                    public void setName(String Name) {
                        this.Name = Name;
                    }

                    public String getUrl() {
                        return Url;
                    }

                    public void setUrl(String Url) {
                        this.Url = Url;
                    }
                }
            }

            public class PartnersBean implements Serializable{
                /**
                 * StockName : 刘德
                 * StockType : 自然人股东
                 * StockPercent : null
                 * IdentifyType :
                 * IdentifyNo :
                 * ShouldCapi : null
                 * ShoudDate : null
                 * InvestType : null
                 * InvestName : null
                 * RealCapi : null
                 * CapiDate : null
                 * Address : null
                 */

                private String StockName;
                private String StockType;
                private Object StockPercent;
                private String IdentifyType;
                private String IdentifyNo;
                private Object ShouldCapi;
                private Object ShoudDate;
                private Object InvestType;
                private Object InvestName;
                private Object RealCapi;
                private Object CapiDate;
                private Object Address;

                public String getStockName() {
                    return StockName;
                }

                public void setStockName(String StockName) {
                    this.StockName = StockName;
                }

                public String getStockType() {
                    return StockType;
                }

                public void setStockType(String StockType) {
                    this.StockType = StockType;
                }

                public Object getStockPercent() {
                    return StockPercent;
                }

                public void setStockPercent(Object StockPercent) {
                    this.StockPercent = StockPercent;
                }

                public String getIdentifyType() {
                    return IdentifyType;
                }

                public void setIdentifyType(String IdentifyType) {
                    this.IdentifyType = IdentifyType;
                }

                public String getIdentifyNo() {
                    return IdentifyNo;
                }

                public void setIdentifyNo(String IdentifyNo) {
                    this.IdentifyNo = IdentifyNo;
                }

                public Object getShouldCapi() {
                    return ShouldCapi;
                }

                public void setShouldCapi(Object ShouldCapi) {
                    this.ShouldCapi = ShouldCapi;
                }

                public Object getShoudDate() {
                    return ShoudDate;
                }

                public void setShoudDate(Object ShoudDate) {
                    this.ShoudDate = ShoudDate;
                }

                public Object getInvestType() {
                    return InvestType;
                }

                public void setInvestType(Object InvestType) {
                    this.InvestType = InvestType;
                }

                public Object getInvestName() {
                    return InvestName;
                }

                public void setInvestName(Object InvestName) {
                    this.InvestName = InvestName;
                }

                public Object getRealCapi() {
                    return RealCapi;
                }

                public void setRealCapi(Object RealCapi) {
                    this.RealCapi = RealCapi;
                }

                public Object getCapiDate() {
                    return CapiDate;
                }

                public void setCapiDate(Object CapiDate) {
                    this.CapiDate = CapiDate;
                }

                public Object getAddress() {
                    return Address;
                }

                public void setAddress(Object Address) {
                    this.Address = Address;
                }
            }

            public class EmployeesBean implements Serializable{
                /**
                 * Name : 黎万强
                 * Job : 监事
                 * CerNo :
                 * ScertName :
                 */

                private String Name;
                private String Job;
                private String CerNo;
                private String ScertName;

                public String getName() {
                    return Name;
                }

                public void setName(String Name) {
                    this.Name = Name;
                }

                public String getJob() {
                    return Job;
                }

                public void setJob(String Job) {
                    this.Job = Job;
                }

                public String getCerNo() {
                    return CerNo;
                }

                public void setCerNo(String CerNo) {
                    this.CerNo = CerNo;
                }

                public String getScertName() {
                    return ScertName;
                }

                public void setScertName(String ScertName) {
                    this.ScertName = ScertName;
                }
            }

            public class ChangeRecordsBean implements Serializable{
                /**
                 * ProjectName : 注册资本
                 * BeforeContent : 5000万元
                 * AfterContent : 185000万元
                 * ChangeDate : 2016-03-24T00:00:00+08:00
                 */

                private String ProjectName;
                private String BeforeContent;
                private String AfterContent;
                private String ChangeDate;

                public String getProjectName() {
                    return ProjectName;
                }

                public void setProjectName(String ProjectName) {
                    this.ProjectName = ProjectName;
                }

                public String getBeforeContent() {
                    return BeforeContent;
                }

                public void setBeforeContent(String BeforeContent) {
                    this.BeforeContent = BeforeContent;
                }

                public String getAfterContent() {
                    return AfterContent;
                }

                public void setAfterContent(String AfterContent) {
                    this.AfterContent = AfterContent;
                }

                public String getChangeDate() {
                    return ChangeDate;
                }

                public void setChangeDate(String ChangeDate) {
                    this.ChangeDate = ChangeDate;
                }
            }
        }
    }
}
