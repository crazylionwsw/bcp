package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.bean.SysResourceBean;
import com.fuze.bcp.api.auth.bean.SysRoleBean;
import com.fuze.bcp.api.bd.bean.*;
import com.fuze.bcp.api.bd.service.IValidateBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.bean.DealerGroupBean;
import com.fuze.bcp.api.creditcar.bean.CustomerSurveyTemplateBean;
import com.fuze.bcp.api.customer.bean.QuestionCategoryBean;
import com.fuze.bcp.api.customer.bean.QuestionsBean;
import com.fuze.bcp.api.file.bean.DocumentTypeBean;
import com.fuze.bcp.api.file.bean.EmailObjectBean;
import com.fuze.bcp.api.file.bean.PushObjectBean;
import com.fuze.bcp.api.file.bean.TemplateObjectBean;
import com.fuze.bcp.api.mq.bean.TaskDescribeBean;
import com.fuze.bcp.api.msg.bean.MessageTemplateBean;
import com.fuze.bcp.api.msg.bean.SubSribeSourceBean;
import com.fuze.bcp.api.sys.bean.APKReleaseBean;
import com.fuze.bcp.api.sys.bean.SysParamBean;
import com.fuze.bcp.api.sys.bean.TerminalDeviceBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LGJ on 2017/8/13.
 */
@RestController
@RequestMapping(value = "/json/validate")
public class BeanValidateController {

    @Autowired
    private IValidateBizService iValidateBizService;

    /*                  权限管理            唯一性       校验            START                                        */
    /**
     *     系统资源
     * @return
     */
    @RequestMapping(value = "/sysresource/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateResources(@RequestBody SysResourceBean sysResourceBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(sysResourceBean,propname,val);
    }

    /**
     *     系统角色
     * @return
     */
    @RequestMapping(value = "/role/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateSysRole(@RequestBody SysRoleBean sysRoleBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(sysRoleBean,propname,val);
    }

    /**
     *     登录用户
     * @return
     */
    @RequestMapping(value = "/user/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateLoginUser(@RequestBody LoginUserBean loginUserBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(loginUserBean,propname,val);
    }


    /*                  系统管理            唯一性       校验            START                                        */

    /**
     *     设备管理
     * @param terminalDeviceBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/terminaldevice/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateTerminalDevice(@RequestBody TerminalDeviceBean terminalDeviceBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(terminalDeviceBean,propname,val);
    }

    /**
     * 版本管理
     * @param apkReleaseBean
     * @param propname
     * @param propval
     * @return
     */
    @RequestMapping(value = "/apkversion",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateAPKRelease(@RequestBody APKReleaseBean apkReleaseBean, @RequestParam String propname, @RequestParam String propval){
        return iValidateBizService.actCheckUnique(apkReleaseBean,propname,propval);
    }

    /**
     *  系统参数
     * @param sysParamBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/sysparam/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateSysParam(@RequestBody SysParamBean sysParamBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(sysParamBean,propname,val);
    }

    /**
     *  任务消息
     * @param taskDescribeBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/taskdescribe/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateMessageSubScribe(@RequestBody TaskDescribeBean taskDescribeBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(taskDescribeBean,propname,val);
    }

    /**
     *      消息模板
     * @param messageTemplateBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/messagetemplate/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateMessageTemplate(@RequestBody MessageTemplateBean messageTemplateBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(messageTemplateBean,propname,val);
    }

    /*                  系统管理            唯一性       校验            END                                        */

    /*                  基础数据            唯一性       校验            START                                        */

    /**
     *          银行利率
     * @param sourceRateBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/sourcerate/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateSourceRate(@RequestBody SourceRateBean sourceRateBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(sourceRateBean,propname,val);
    }

    /**
     *      资金机构
     * @param cashSourceBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/cashsource/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateCashSource(@RequestBody CashSourceBean cashSourceBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(cashSourceBean,propname,val);
    }

    @RequestMapping(value = "/cashsourceemployee/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateCashSourceEmployee(@RequestBody CashSourceEmployeeBean cashSourceEmployeeBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(cashSourceEmployeeBean,propname,val);
    }

    /**
     *      还款方式  属性校验
     * @param repaymentWayBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/repaymentway/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateRepaymentWay(@RequestBody RepaymentWayBean repaymentWayBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(repaymentWayBean,propname,val);
    }

    /**
     *         单据类型  属性校验
     * @param billTypeBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/billtype/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateBillType(@RequestBody BillTypeBean billTypeBean,@PathVariable("propname") String propname,@PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(billTypeBean,propname,val);
    }

    /**
     * 业务事件类型
     * @param businessEventTypeBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/businessEventType/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateBusinessEventType(@RequestBody BusinessEventTypeBean businessEventTypeBean,@PathVariable("propname") String propname,@PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(businessEventTypeBean,propname,val);
    }

    /**
     *      担保方式  属性校验
     * @param guaranteeWayBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/guaranteeway/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateGuaranteeWay(@RequestBody GuaranteeWayBean guaranteeWayBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(guaranteeWayBean,propname,val);
    }


    /**
     *      业务类型   属性唯一性校验
     * @param businessTypeBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/business/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateBusiness(@RequestBody BusinessTypeBean businessTypeBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(businessTypeBean,propname,val);
    }

    /**
     *      文档模板   属性唯一性校验
     * @param documentTypeBean
     * @param propname
     * @return
     */
    @RequestMapping(value = "/document/{propname}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateDocument(@RequestBody DocumentTypeBean documentTypeBean, @PathVariable("propname") String propname){
        return iValidateBizService.actCheckUnique(documentTypeBean,propname,documentTypeBean.getComment());
    }

    /**
     *      客户资料类型   属性唯一性校验
     * @param customerImageTypeBean
     * @param propname
     * @param val
     * @return
     */
    @RequestMapping(value = "/customerimagetype/{propname}/{val}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateImageType(@RequestBody CustomerImageTypeBean customerImageTypeBean, @PathVariable("propname") String propname, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(customerImageTypeBean,propname,val);
    }

    /**
     * 验证 车辆品牌编码名称唯一
     * @return
     */
    @RequestMapping(value="/carbrand/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateCarBrand(@RequestBody CarBrandBean carBrand, @PathVariable("propname")String name, @PathVariable("val")String val){
        return iValidateBizService.actCheckUnique(carBrand,name,val);
    }

    /**
     * 验证 车辆车型编码名称唯一
     * @return
     */
    @RequestMapping(value="/carmodel/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateCarModel(@RequestBody CarModelBean carModelBean, @PathVariable("propname") String name, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(carModelBean,name,val);
    }

    /**
     * 验证 车辆车型编码名称唯一
     * @return
     */
    @RequestMapping(value="/cartype/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateCarType(@RequestBody CarTypeBean carType, @PathVariable("propname") String name, @PathVariable("val") String val){
        return iValidateBizService.actCheckUnique(carType,name,val);
    }

    /**
     *  组织机构
     */
    @RequestMapping(value="/orginfo/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateOrginfo(@RequestBody OrgBean orginfo, @PathVariable("propname") String propname, @PathVariable("val") String propvalue){
        return iValidateBizService.actCheckUnique(orginfo,propname,propvalue);
    }

    /**
     *  员工
     */
    @RequestMapping(value="/employee",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateEmployee(@RequestBody EmployeeBean employeeBean, @RequestParam String propname, @RequestParam String propvalue){
        return iValidateBizService.actCheckUnique(employeeBean,propname,propvalue);
    }

    /**
     *  地区管理
     */
    @RequestMapping(value="/province/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateProvince(@RequestBody ProvinceBean provinceBean, @PathVariable("propname")String propname, @PathVariable("val")String propvalue){
        return iValidateBizService.actCheckUnique(provinceBean,propname,propvalue);
    }

    /**
     *  收费项
     */
    @RequestMapping(value="/feeitem/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateFeeItem(@RequestBody FeeItemBean feeItemBean, @PathVariable("propname")String propname, @PathVariable("val")String propvalue){
        return iValidateBizService.actCheckUnique(feeItemBean,propname,propvalue);
    }

    /**
     *  销售政策
     */
    @RequestMapping(value="/salespolicy/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateSalesPolicy(@RequestBody SalesPolicyBean salesPolicyBean, @PathVariable("propname")String propname, @PathVariable("val")String propvalue){
        return iValidateBizService.actCheckUnique(salesPolicyBean,propname,propvalue);
    }

    /**
     *模版管理
     */
    @RequestMapping(value="/templateobj",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateTemplate(@RequestBody TemplateObjectBean templateObjectBean, @RequestParam String propname, @RequestParam String propval){
        return iValidateBizService.actCheckUnique(templateObjectBean,propname,propval);
    }

    /**
     *问题分类
     */
    @RequestMapping(value="/questioncategory/{propname}/{propval}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateQuestioncategory(@RequestBody QuestionCategoryBean questionCategoryBean, @PathVariable("propname") String propname, @PathVariable("propval") String propval){

        int displayOrder;
        if ("displayOrder".equals(propname)){
            displayOrder =Integer.parseInt(propval);
            return iValidateBizService.actCheckUnique(questionCategoryBean,propname,displayOrder);
        }
        return iValidateBizService.actCheckUnique(questionCategoryBean,propname,propval);
    }

    /**
     *问题
     */
    @RequestMapping(value="/question/{propname}/{propval}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateQuestion(@RequestBody QuestionsBean questionsBean, @PathVariable("propname") String propname, @PathVariable("propval") String propval){
        if ("displayOrder".equals(propname)){
            int displayOrder;
            displayOrder =Integer.parseInt(propval);
            return iValidateBizService.actCheckUnique(questionsBean,propname,displayOrder);
        }
        return iValidateBizService.actCheckUnique(questionsBean,propname,propval);
    }

    /**
     * 邮件模版
     */
    @RequestMapping(value="/emailobject/{propname}/{propval}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateEmailObject(@RequestBody EmailObjectBean emailObjectBean, @PathVariable("propname") String propname, @PathVariable("propval") String propval){
        return iValidateBizService.actCheckUnique(emailObjectBean,propname,propval);
    }

    /**
     * 推送模版
     */
    @RequestMapping(value="/pushtemplate/{propname}/{propval}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validatePushObject(@RequestBody PushObjectBean pushObjectBean, @PathVariable("propname") String propname, @PathVariable("propval") String propval){
        return iValidateBizService.actCheckUnique(pushObjectBean,propname,propval);
    }



    /*                  基础数据            唯一性       校验            END                                        */

    /*                  产品管理            唯一性       校验            START                                        */

    /**
     *分期产品
     * @return
     */
    @RequestMapping(value="/creditproduct/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateCreditProduct(@RequestBody CreditProductBean creditProductBean, @PathVariable("propname")String propname, @PathVariable("val")String propvalue){
        return iValidateBizService.actCheckUnique(creditProductBean,propname,propvalue);
    }

    /**
     *促销管理
     */
    @RequestMapping(value="/promotionpolicy/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validatePromotionPolicy(@RequestBody PromotionPolicyBean promotionPolicyBean, @PathVariable("propname")String propname, @PathVariable("val")String propvalue){
        return iValidateBizService.actCheckUnique(promotionPolicyBean,propname,propvalue);
    }

    /**
     *贴息管理
     */
    @RequestMapping(value="/compensatorypolicy/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateCompensatoryPolicy(@RequestBody CompensatoryPolicyBean compensatoryPolicyBean, @PathVariable("propname")String propname, @PathVariable("val")String propvalue){
        return iValidateBizService.actCheckUnique(compensatoryPolicyBean,propname,propvalue);
    }

    /*                  产品管理            唯一性       校验            END                                        */

    /*                  客户管理            唯一性       校验            START                                        */
    @RequestMapping(value="/cardealer/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateCardealer(@RequestBody CarDealerBean carDealerBean, @PathVariable("propname")String propname, @PathVariable("val")String propvalue){
        return iValidateBizService.actCheckUnique(carDealerBean,propname,propvalue);
    }

    @RequestMapping(value="/customersurveytemplate/{propname}/{val}",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean validateCardealer(@RequestBody CustomerSurveyTemplateBean customerSurveyTemplateBean, @PathVariable("propname")String propname, @PathVariable("val")String propvalue){
        return iValidateBizService.actCheckUnique(customerSurveyTemplateBean,propname,propvalue);
    }

    /**
     * 订阅源唯一验证
     * @return
     */
    @RequestMapping(value = "/subsribesource",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateSubSribeSource(@RequestBody SubSribeSourceBean subSribeSourceBean, @RequestParam String propname, @RequestParam String propvalue){
        return iValidateBizService.actCheckUnique(subSribeSourceBean,propname,propvalue);
    }

    /**
     * 经销商集团
     * @return
     */
    @RequestMapping(value = "/dealergroup/{propname}/{propvalue}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean validateDealerGroup(@RequestBody DealerGroupBean dealerGroupBean, @PathVariable("propname") String propname, @PathVariable("propvalue") String propvalue){
        return iValidateBizService.actCheckUnique(dealerGroupBean,propname,propvalue);
    }

}
