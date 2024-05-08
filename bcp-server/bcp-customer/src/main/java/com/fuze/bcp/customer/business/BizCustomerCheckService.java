package com.fuze.bcp.customer.business;

import api.DefaultBusinessDataApi;
import api.DefaultCorporateActionApi;
import api.DefaultPersonalActionApi;
import api.DefaultPersonalInvestmentApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.api.customer.service.ICustomerCheckBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.customer.domain.Customer;
import com.fuze.bcp.customer.domain.CustomerCheck;
import com.fuze.bcp.customer.service.ICustomerCheckService;
import com.fuze.bcp.customer.service.ICustomerService;
import com.fuze.bcp.service.MappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Lily on 2017/8/3.
 */
@Service
public class BizCustomerCheckService implements ICustomerCheckBizService {

    /**
     * 设置企业诉讼接口查询页码数
     */
    private static final String CUSTOMERINFO_CORPORATEACTION_PAGENO = "1";
    /**
     * 设置企业诉讼接口当前显示数据条数(不能超过20)
     */
    private static final String CUSTOMERINFO_CORPORATEACTION_RANGE = "10";

    private static final String CUSTOMERCHECK_REQUEST_SUCCESS = "10000";

    private static final Logger logger = LoggerFactory.getLogger(BizCustomerCheckService.class);

    @Autowired
    ICustomerCheckService iCustomerCheckService;
    @Autowired

    IParamBizService iParamBizService;

    @Autowired
    ICustomerService iCustomerService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<CustomerCheckBean> actFindCustomerCheck(String id, String customerId) {
        logger.info("开始进行用户核查！"+id);
        CustomerCheckBean customerCheckBean = new CustomerCheckBean();
        //获取客户信息
        Customer customer = iCustomerService.getOne(customerId);
        if(customer != null){
            //查询客户的信息是否存在
            CustomerCheck customerCheck = iCustomerCheckService.findCustomerCheckByCustomerId(customerId);
            Map data = customerCheck.getExtData();
            if(customerCheck == null){
                customerCheck.setCustomerId(customer.getId());
                //获取系统参数key值
                Map<?, ?> JDWXkey = iParamBizService.actGetMap("CUSTOMER_INFO_KEY").getD();
                String key = (String) JDWXkey.get("key");
                //个人对外投资
                PersonalInvestmentBean personalInvestmentJson = this.personalInvestment(customer.getIdentifyNo(),key ).getD();
                if (personalInvestmentJson != null){
                    if(personalInvestmentJson.getCode() .equals(CUSTOMERCHECK_REQUEST_SUCCESS)){
                        customerCheck.setPersonalInvestmentBean(personalInvestmentJson);
                        data.put("【个人对外投资】接口返回信息",personalInvestmentJson.getCode()+":"+personalInvestmentJson.getMsg());
                        //判断该客户是否是法人代表,如果该客户为公司法人则查询企业诉讼信息
                        if(personalInvestmentJson.getResult().getResp_data().getBody() != null){
                            if(personalInvestmentJson.getResult().getResp_data().getBody().getRyPosFRList().get(0).getRyName().equals(customer.getName()) && personalInvestmentJson.getResult().getResp_data().getBody().getRyPosFRList().get(0).getEntStatus().contains("在营")){
                                //企业诉讼
                                CorporateActionBean corporateActionJson = this.corporateAction(personalInvestmentJson.getResult().getResp_data().getBody().getRyPosFRList().get(0).getEntName(), CUSTOMERINFO_CORPORATEACTION_PAGENO, CUSTOMERINFO_CORPORATEACTION_RANGE, key).getD();
                                if (corporateActionJson != null){
                                    if(corporateActionJson.getCode().equals(CUSTOMERCHECK_REQUEST_SUCCESS)) {
                                        customerCheck.setCorporateActionBean(corporateActionJson);
                                        data.put("【企业诉讼】接口返回信息",corporateActionJson.getCode()+":"+corporateActionJson.getMsg());
                                        logger.info("【京东万象接口】：企业诉讼查询完成");
                                    }else {
                                        data.put("【企业诉讼】接口返回错误信息",corporateActionJson.getCode()+":"+corporateActionJson.getMsg());
                                    }
                                }
                            }else{
                                data.put("【企业诉讼】","由于不是公司法人所以不用查询企业诉讼接口");
                            }
                        }else{
                            data.put("【企业诉讼】","个人对外投资法人为空，因此不用查询企业诉讼接口");
                        }
                    }else{
                        data.put("【个人对外投资】接口返回错误信息",personalInvestmentJson.getCode()+":"+personalInvestmentJson.getMsg());
                    }
                    logger.info("【京东万象接口】：个人对外投资信息查询完成");
                }
                //企业工商数据查询
                BusinessDataBean businessDataJson = this.businessData(customer.getCustomerJob().getCompanyName(), key).getD();
                if (businessDataJson != null){
                    if(businessDataJson.getCode() .equals(CUSTOMERCHECK_REQUEST_SUCCESS)){
                        customerCheck.setBusinessDataBean(businessDataJson);
                        data.put("【企业工商数据查询】接口返回信息",businessDataJson.getCode()+":"+businessDataJson.getMsg());
                    }else{
                        data.put("【企业工商数据查询】接口返回错误信息",businessDataJson.getCode()+":"+businessDataJson.getMsg());
                    }
                    logger.info("【京东万象接口】：企业工商数据查询完成");
                }
                //个人诉讼
                PersonalActionBean personalActionJson = this.personalAction(customer.getName(), customer.getIdentifyNo(), key).getD();
                if(personalActionJson != null){
                    if(personalActionJson.getCode().equals(CUSTOMERCHECK_REQUEST_SUCCESS)){
                        customerCheck.setPersonalActionBean(personalActionJson);
                        data.put("【个人诉讼】接口返回信息",personalActionJson.getCode()+":"+personalActionJson.getMsg());
                    }else {
                        data.put("【个人诉讼】接口返回错误信息",personalActionJson.getCode()+":"+personalActionJson.getMsg());
                    }
                    logger.info("【京东万象接口】：个人诉讼查询完成");
                }
                CustomerCheck info = iCustomerCheckService.save(customerCheck);
                if(info != null){
                    logger.info("【京东万象接口】：该任务查询完成");
                    return ResultBean.getSucceed().setM("【京东万象接口】：该任务查询完成");
                }else{
                    return ResultBean.getFailed().setM("客户核对信息保存失败!");
                }
            }
            return ResultBean.getFailed().setM("客户信息已存在,不能重复查询!");
        }
        return ResultBean.getFailed().setM("客户信息不存在，请核对信息!");
    }

    @Override
    public ResultBean<CustomerCheckBean> actFindCustomerCheckByCustomerId(String customerId) {
        CustomerCheck customerCheck = iCustomerCheckService.findCustomerCheckByCustomerId(customerId);
        if ( customerCheck != null ) {
            CustomerCheckBean customerCheckBean = new CustomerCheckBean();
            customerCheckBean.setBusinessDataBean(customerCheck.getBusinessDataBean());
            customerCheckBean.setCorporateActionBean(customerCheck.getCorporateActionBean());
            customerCheckBean.setPersonalActionBean(customerCheck.getPersonalActionBean());
            customerCheckBean.setPersonalInvestmentBean(customerCheck.getPersonalInvestmentBean());
            customerCheckBean.setCustomerId(customerCheck.getCustomerId());
            return ResultBean.getSucceed().setD(customerCheckBean);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<Integer> actFindCustomerCheckCountByCustomerId(String customerId) {
        Integer count = iCustomerCheckService.FindCustomerCheckCountByCustomerId(customerId);
        return ResultBean.getSucceed().setD(count);
    }

    /**
     * 个人对外投资
     * @param idNo
     * @param appkey
     * @return
     */
    public ResultBean<PersonalInvestmentBean> personalInvestment(String idNo, String appkey) {
        PersonalInvestmentBean personalInvestmentJson = new PersonalInvestmentBean();
        DefaultPersonalInvestmentApi api = new DefaultPersonalInvestmentApi();
        String response = null;
        try {
            logger.info("客户【"+idNo+"】开始查询个人对外投资接口");
            response = api.perInv(idNo, appkey);
            logger.info("客户【"+idNo+"】查询个人对外投资接口已完成,查询结果:"+response);
            personalInvestmentJson = JSONObject.parseObject(response,PersonalInvestmentBean.class);
            return ResultBean.getSucceed().setD(personalInvestmentJson);
        } catch (Exception e) {
            return ResultBean.getFailed().setM(e.getMessage());
        }

    }

    /**
     * 企业工商数据查询
     * @param keyword
     * @param appkey
     * @return
     */
    public ResultBean<BusinessDataBean> businessData(String keyword, String appkey){
        BusinessDataBean businessDataJson = new BusinessDataBean();
        DefaultBusinessDataApi api = new DefaultBusinessDataApi();
        String response = null;
        try {
            logger.info("企业【"+keyword+"】开始查询企业工商数据查询接口");
            response = api.getDetailsByName(keyword, appkey);
            logger.info("企业【"+keyword+"】企业工商数据查询接口已完成,查询结果:"+response);
            businessDataJson = JSON.parseObject(response, BusinessDataBean.class);
            return ResultBean.getSucceed().setD(businessDataJson);
        } catch (Exception e) {
            return ResultBean.getFailed().setM(e.getMessage());
        }
    }

    /**
     * 个人诉讼
     * @param name
     * @param idcardNo
     * @param appkey
     * @return
     */
    public ResultBean<PersonalActionBean> personalAction(String name, String idcardNo, String appkey) {
        PersonalActionBean personalActionJson = new PersonalActionBean();
        DefaultPersonalActionApi api = new DefaultPersonalActionApi();
        String response = null;
        try {
            logger.info("客户【"+name+"】开始查询个人诉讼接口");
            response = api.gerensusong(name, idcardNo, appkey);
            logger.info("客户【"+name+"】个人诉讼接口已完成，查询结果:"+response);
            personalActionJson = JSONObject.parseObject(response,PersonalActionBean.class);
            return ResultBean.getSucceed().setD(personalActionJson);
        } catch (Exception e) {
            return ResultBean.getFailed().setM(e.getMessage());
        }
    }

    /**
     * 企业诉讼
     */
    public ResultBean<CorporateActionBean> corporateAction(String entName, String pageno, String range, String appkey){
        CorporateActionBean corporateActionJson = new CorporateActionBean();
        DefaultCorporateActionApi api = new DefaultCorporateActionApi();
        String response = null;
        try {
            logger.info("企业【"+entName+"】开始查询企业诉讼接口");
            response = api.susong(entName, pageno, range, appkey);
            logger.info("企业【"+entName+"】企业诉讼接口已完成，查询结果:"+response);
            corporateActionJson = JSONObject.parseObject(response,CorporateActionBean.class);
            return ResultBean.getSucceed().setD(corporateActionJson);
        } catch (Exception e) {
            return ResultBean.getFailed().setM(e.getMessage());
        }
    }
}
