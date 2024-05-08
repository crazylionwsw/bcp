package com.fuze.bcp.wechat.business;

import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.wechat.bean.LoanQueryBean;
import com.fuze.bcp.api.wechat.service.ILoanQueryBizService;
import com.fuze.bcp.api.wechat.service.IWechatBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.wechat.domain.LoanQuery;
import com.fuze.bcp.wechat.service.ILoanQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ${Liu} on 2018/4/18.
 * 公众号信息服务类
 */
@Service
public class BizLoanQueryService implements ILoanQueryBizService {

    Logger logger = LoggerFactory.getLogger(BizLoanQueryService.class);

    @Autowired
    ILoanQueryService iLoanQueryService;

    @Autowired
    IWechatBizService iWechatBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<LoanQueryBean> actSaveLoanQuery(LoanQueryBean loanQueryBean) {

        //TODO:根据用户录入的房贷月供、商业保险年缴额、打卡工资，计算客户可以贷款的金额
        if (loanQueryBean.getWorkSalary() != null && loanQueryBean.getWorkSalary() != 0) {
            loanQueryBean.setLoanAmount(Double.valueOf(loanQueryBean.getWorkSalary() * 80));
            logger.info("根据打卡工资"+loanQueryBean.getWorkSalary()+"计算贷款金额\"+loanQueryBean.getLoanAmount()");
        }else if(loanQueryBean.getApoci() != null && loanQueryBean.getApoci() != 0.0){
            loanQueryBean.setLoanAmount(Double.valueOf(loanQueryBean.getApoci() * 80));
            logger.info("\"根据商业保险年缴额"+loanQueryBean.getApoci()+"计算贷款金额\"+loanQueryBean.getLoanAmount()");
        }else if(loanQueryBean.getMonthlyAmount() != null && loanQueryBean.getMonthlyAmount() != 0.0){
            loanQueryBean.setLoanAmount(Double.valueOf(loanQueryBean.getMonthlyAmount() * 120));
            logger.info("\"根据房贷月供"+loanQueryBean.getMonthlyAmount()+"计算贷款金额\"+loanQueryBean.getLoanAmount()");
        }
        loanQueryBean.setCell(org.springframework.util.StringUtils.trimAllWhitespace(loanQueryBean.getCell()));
        LoanQuery loanQuery = iLoanQueryService.save(mappingService.map(loanQueryBean, LoanQuery.class));

        if(loanQuery != null && loanQuery.getShareOpenId() != null) {
            logger.info("本次操作分享人的微信openId为:"+loanQuery.getShareOpenId());
            //TODO:需判断分享人(ID)是不是公司员工
            EmployeeBean employeeBean = iOrgBizService.actGetEmployeeByWXOpenId(loanQuery.getShareOpenId()).getD();
            if (employeeBean != null && employeeBean.getWxUserId() != null) {
                Map map = iWechatBizService.sendCardMessage(employeeBean.getWxUserId(), mappingService.map(loanQuery, LoanQueryBean.class)).getD();
                if (map != null ) {
                    if (!map.containsKey("errcode") || "0".equals(map.get("errcode"))) {
                        logger.info("企业微信通知消息发送成功:"+map.get("errcode")+map.get("errmsg"));
                    } else {
                        logger.error("企业微信通知消息发送失败:"+map.get("errcode")+map.get("errmsg"));
                    }
                } else {
                    logger.error("企业微信通知消息发送出现未知结果");
                }
            }
        }

        return ResultBean.getSucceed().setD(mappingService.map(loanQuery, LoanQueryBean.class));
    }

    @Override
    public ResultBean<List<LoanQueryBean>> actGetAllLoanQuery() {
        List<LoanQuery> avaliableAll = iLoanQueryService.getAvaliableAll();
        if(avaliableAll != null){
            return ResultBean.getSucceed().setD((mappingService.map(avaliableAll,LoanQueryBean.class)));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<LoanQueryBean> actGetLoanQuery(String loanQueryId) {
        LoanQuery availableOne = iLoanQueryService.getAvailableOne(loanQueryId);
        if(availableOne != null){
            return ResultBean.getSucceed().setD(mappingService.map(availableOne,LoanQueryBean.class));
        }
        return ResultBean.getFailed();
    }
}
