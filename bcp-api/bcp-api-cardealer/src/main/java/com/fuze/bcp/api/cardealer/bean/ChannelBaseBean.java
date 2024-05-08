package com.fuze.bcp.api.cardealer.bean;

import com.fuze.bcp.bean.APIBaseDataBean;
import com.fuze.bcp.bean.PayAccount;
import lombok.Data;

import java.util.List;

/**
 * Created by ZQW on 2018/3/19.
 */
@Data
public class ChannelBaseBean extends APIBaseDataBean {

    /**
     * 开发阶段，合同在签署中
     */
    public final static Integer STATUS_CONTRACT = 0;

    /**
     * 正常合作状态
     */
    public final static Integer STATUS_ONGOING = 1;

    /**
     * 终止合作状态
     */
    public final static Integer STATUS_STOP = 9;

    /**
     * 自我开发
     */
    public final static Integer SOURCE_FIRST = 1;

    /**
     * 合作支行推荐
     */
    public final static Integer SOURCE_SECOND = 2;

    /**
     *    公司
     */
    public final static Integer COMPANY_TYPE = 1;

    /**
     *    个人
     */
    public final static Integer PERSONAL_TYPE = 2;

    /**
     * 经销商来源
     */
    private Integer sourceDeals = SOURCE_FIRST;

    /**
     * 所在地址
     */
    private String address = null;

    /**
     * 所在区域
     */
    private String dealerRegion = null;

    /**
     * 手机
     */
    private String cell = null;

    /**
     * 座机
     */
    private String telephone = null;

    /**
     * 负责人
     */
    private String manager = null;

    /**
     *   渠道类型
     */
    private Integer channelType = COMPANY_TYPE;

    /**                           富择信息                                      **/
    /**
     * 所属的公司部门
     */
    private String orginfoId = null;

    /**
     * 渠道经理
     */
    private String employeeId = null;

    /**
     * 分期经理
     */
    private List<String> businessManIds = null;

    /**
     * 系统用户
     */
    private String loginUserId = null;

    /**
     * 部门领导
     */
    private String leaderId;

    /**
     * 报单行
     */
    private String cashSourceId = null;

    /**
     * 渠道合作支行---表示该渠道与某某支行合作
     */
    private String cooperationCashSourceId = null;

    /**
     * 合作状态，默认是合同签署中
     */
    private Integer status = STATUS_CONTRACT;

    /**
     * 开始生效日期
     */
    private String startDate = null;

    /**
     * 财务账户信息
     */
    private List<PayAccount> payAccounts = null;

    /**
     * 经销商的月成交量
     */
    private String turnover = null;

    /**
     * 贷款客户数
     */
    private String customerNumber = null;

    /**
     * 贷款总额
     */
    private String sumLoan = null;

    /**
     * 备注信息
     */
    private String commentInfo;

    /**
     * 经营业务(业务类型编码）
     */
    private List<String> businessTypeCodes;

    /**
     * 渠道集团
     */
    private String dealerGroupId;
}
