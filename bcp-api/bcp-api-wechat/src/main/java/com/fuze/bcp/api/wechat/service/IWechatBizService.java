package com.fuze.bcp.api.wechat.service;

import com.fuze.bcp.api.wechat.bean.AccessToken;
import com.fuze.bcp.api.wechat.bean.LoanQueryBean;
import com.fuze.bcp.api.wechat.bean.WechatLoginResult;
import com.fuze.bcp.api.wechat.bean.WechatUserResult;
import com.fuze.bcp.bean.ResultBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2018/2/2.
 */
public interface IWechatBizService {

    ResultBean getOrg(String token, String id) throws Exception;

    /**
     * 同步部门
     */
    ResultBean syncOrg() throws Exception;

    /**
     * 更新部门
     */
    ResultBean updateOrg(Map orgMap) throws Exception;

    ResultBean deleteOrg(String id) throws Exception;

    ResultBean createOrg(String id) throws Exception;

    /**
     * 新增所有员工
     */
    ResultBean createEmployee() throws Exception;

    /**
     * 新增一条员工
     */
    ResultBean saveEmployee(String employeeId) throws Exception;

    ResultBean getEmployee(String userId) throws Exception;

    ResultBean deleteEmployee(String userId) throws Exception;

    ResultBean deleteEmployees(List userList) throws Exception;

    ResultBean updateEmployee(String employeeId) throws Exception;

    ResultBean getEmployeeByOrg(String orgId, Integer child) throws Exception;

    ResultBean deleteAllInfos() throws Exception;

    ResultBean<AccessToken> accessToken() throws IOException;

    ResultBean<AccessToken> accessToken(Boolean flag) throws IOException;

    ResultBean<AccessToken> accessTokenW() throws IOException;

    ResultBean<AccessToken> accessTokenW(Boolean flag) throws IOException;

    ResultBean<WechatLoginResult> oAuth2GetUserByCode(String token, String code, String agentId);

    ResultBean<String> oAuth2Url(String redirect_uri);

    ResultBean<String> getMemberGuidByCode(String token, String code, String agentId);

    ResultBean<WechatUserResult> getWcUser(String userId, String token);

    ResultBean<Map> sendCardMessage(String userId, LoanQueryBean loanQueryBean);

    String getCorpId();

    String getSecret();

    String getAgentId();

    String getBelieveIp();

    ResultBean getAngent()throws Exception;

}
