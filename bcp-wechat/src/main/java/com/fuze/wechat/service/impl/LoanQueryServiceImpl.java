package com.fuze.wechat.service.impl;

import com.fuze.wechat.base.DataStatus;
import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.Employee;
import com.fuze.wechat.domain.LoanQuery;
import com.fuze.wechat.repository.EmployeeRepository;
import com.fuze.wechat.repository.LoanQueryRepository;
import com.fuze.wechat.service.IEnterpriseWeChatService;
import com.fuze.wechat.service.ILoanQueryService;
import com.fuze.wechat.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class LoanQueryServiceImpl implements ILoanQueryService {

    Logger logger = LoggerFactory.getLogger(LoanQueryServiceImpl.class);

    @Autowired
    IEnterpriseWeChatService iEnterpriseWeChatService;

    @Autowired
    LoanQueryRepository loanQueryRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public ResultBean<LoanQuery> actSaveLoanQuery(LoanQuery loanQuery) {

        //TODO:根据用户录入的房贷月供、商业保险年缴额、打卡工资，计算客户可以贷款的金额
        if (loanQuery.getWorkSalary() != null && loanQuery.getWorkSalary() != 0) {
            loanQuery.setLoanAmount(Double.valueOf(loanQuery.getWorkSalary() * 80));
            logger.info("根据打卡工资"+loanQuery.getWorkSalary()+"计算贷款金额" + loanQuery.getLoanAmount() );
        }else if(loanQuery.getApoci() != null && loanQuery.getApoci() != 0){
            loanQuery.setLoanAmount(Double.valueOf(loanQuery.getApoci() * 80));
            logger.info("根据商业保险年缴额"+loanQuery.getApoci()+"计算贷款金额" + loanQuery.getLoanAmount() );
        }else if(loanQuery.getMonthlyAmount() != null && loanQuery.getMonthlyAmount() != 0){
            loanQuery.setLoanAmount(Double.valueOf(loanQuery.getMonthlyAmount() * 120));
            logger.info("根据房贷月供"+loanQuery.getMonthlyAmount()+"计算贷款金额" + loanQuery.getLoanAmount() );
        }
        if (loanQuery.getExpectedLoanAmount() == null || loanQuery.getExpectedLoanAmount() == 0.0) {
            loanQuery.setExpectedLoanAmount(loanQuery.getLoanAmount());
        }

        loanQuery.setCell(StringUtils.trimAllWhitespace(loanQuery.getCell()));
        loanQuery.setTs(DateTimeUtils.getCreateTime());
        loanQuery = loanQueryRepository.save(loanQuery);

        // 微信公众号来源默认用户 王闯： odWAuw95CksUalB72oXLa21Mf53g  周庆文 odWAuw2BVGlZrggxcpbVzsRzgm7k
        Employee defaultPublicEmployee = employeeRepository.findByDataStatusAndWxOpenid(DataStatus.SAVE, "odWAuw2BVGlZrggxcpbVzsRzgm7k");

        // 微信小程序来源默认用户  王闯：                               周庆文：oegDT5Ci3Ys8_Hi9d-q9wbnIWkYI
        Employee defaultMpEmployee = employeeRepository.findByDataStatusAndMpOpenId(DataStatus.SAVE, "oegDT5Ci3Ys8_Hi9d-q9wbnIWkYI");

        if (loanQuery != null){
            Employee employee = new Employee();
            if ("WP".equals(loanQuery.getCreateType())) {
                if (!StringUtils.isEmpty(loanQuery.getShareOpenId()) && !loanQuery.getShareOpenId().equals(loanQuery.getOpenId())){
                    employee = employeeRepository.findByDataStatusAndWxOpenid(DataStatus.SAVE, loanQuery.getShareOpenId());
                } else {
                    employee = defaultPublicEmployee;
                }
                if (employee == null){
                    employee = defaultPublicEmployee;
                }
            } else if ("MP".equals(loanQuery.getCreateType())) {
                if (!StringUtils.isEmpty(loanQuery.getShareMpOpenId()) && !loanQuery.getShareMpOpenId().equals(loanQuery.getMpOpenId())){
                    employee = employeeRepository.findByDataStatusAndMpOpenId(DataStatus.SAVE, loanQuery.getShareMpOpenId());
                } else {
                    employee = defaultMpEmployee;
                }
                if (employee == null){
                    employee = defaultMpEmployee;
                }
            }

            if (StringUtils.isEmpty(employee.getWxUserId())){
                logger.error(String.format("员工【%s】未设置企业微信ID，该信息将发给王闯", employee.getUsername()));
            }
            Map map = iEnterpriseWeChatService.sendCardMessage( StringUtils.isEmpty(employee.getWxUserId()) ? defaultPublicEmployee.getWxUserId() : employee.getWxUserId(), loanQuery).getD();
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

        return ResultBean.getSucceed().setD(loanQuery);
    }

    @Override
    public ResultBean<LoanQuery> actGetLoanQuery(String loanQueryId) {
        LoanQuery availableOne = loanQueryRepository.findOneById(loanQueryId);
        if(availableOne != null){
            return ResultBean.getSucceed().setD(availableOne);
        }
        return ResultBean.getFailed();
    }
}
