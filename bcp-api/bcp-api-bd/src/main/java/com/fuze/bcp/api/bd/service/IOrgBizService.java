package com.fuze.bcp.api.bd.service;

import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.bean.EmployeeLookupBean;
import com.fuze.bcp.api.bd.bean.EmployeeUserBean;
import com.fuze.bcp.api.bd.bean.OrgBean;
import com.fuze.bcp.bean.APITreeLookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/6/9.
 */
public interface IOrgBizService {

    /**
     *获取员工列表，不带分页
     * @param
     * @return
     */
    ResultBean<List<EmployeeBean>> actGetEmployees( );


    /**
     * 添加员工信息
     *
     * @param employeeBean
     * @return
     */
    ResultBean<EmployeeUserBean> actSaveEmployee(EmployeeUserBean employeeBean,Boolean isLeader);

    /**
     * 保存员工信息
     *
     * @param employeeBean
     * @return
     */
    ResultBean<EmployeeBean> actSaveEmployee(EmployeeBean employeeBean);

    /**
     * 删除员工信息
     *
     * @param id
     * @return
     */
    ResultBean<EmployeeBean> actDeleteEmployee(@NotNull String id);

    /**
     * 根据ID获取员工信息
     *
     * @param id
     * @return
     */
    ResultBean<EmployeeBean> actGetEmployee(@NotNull String id);

    ResultBean<EmployeeBean> actGetEmployeeByWcUserId(String wcUserId);

    /**
     * 根据微信openID获取员工信息
     */
    ResultBean<EmployeeBean> actGetEmployeeByWXOpenId(String wxOpenId);

    /**
     * 根据部门ID获取渠道经理
     * @param id
     * @return
     */
    ResultBean<List<EmployeeBean>> actGetEmployeeManagersByOrginfoId(String id);

    /**
     * 获取该员工下所有员工
     * @param employeeId
     * @return
     */
    ResultBean<List<EmployeeBean>> actGetChildEmployees(String employeeId);

    /**
     * 获取部门下所有员工
     * @param orgId
     * @return
     */
    ResultBean<List<EmployeeBean>> actGetOrgEmployees(String orgId);

    /**
     * 获取员工信息 （可用的数据）
     *
     * @return
     */
    ResultBean<List<EmployeeLookupBean>> actLookupEmployees();

    /**
     * 查找员工
     * @param currentPage
     * @param employeeBean
     * @return
     */
    ResultBean<DataPageBean<EmployeeBean>> searchEmployee(Integer currentPage,EmployeeBean employeeBean);

    /**
     * 分页获取员工信息
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<EmployeeBean>> actGetEmployees(@NotNull @Min(0) Integer currentPage);

    /**
     * 获取某部门下所有员工
     * 注意：部门为树型结构，如果该部门有子级部门，要返回时子级部门的员工
     *
     * @param currentPage
     * @param orgId
     * @return
     */
    ResultBean<DataPageBean<EmployeeBean>> actGetEmployees(@NotNull @Min(0) Integer currentPage, @NotNull String orgId);

    /**
     * 获取该部门以及子部门下所有的而员工信息
     * @param currentPage
     * @param orgId
     * @return
     */
    ResultBean<DataPageBean<EmployeeBean>> actGetAllEmployees(@NotNull @Min(0) Integer currentPage, @NotNull String orgId);

    /**
     * 根据用户ID获取该用户同级所有员工和下级所有员工
     * @param userId
     * @return
     */
    ResultBean<List<EmployeeLookupBean>> actGetEmployeesList(String userId);

    /**
     * 获取用户的员工信息
     * @param userId
     * @return
     */
    ResultBean<EmployeeLookupBean> actGetEmployeeByLogin(String userId);

    /**
     * 批量获取员工
     * @param ids
     * @return
     */
    ResultBean<List<EmployeeBean>> actGetEmployeeByIds(List<String> ids);

    ResultBean<Set<EmployeeBean>>  actGetEmployeeByIds(Set<String> ids);


    /**
     * 添加组织架构
     *
     * @param orgBean
     * @return
     */
    ResultBean<OrgBean> actSaveOrg(OrgBean orgBean);

    /**
     * 删除组织架构
     *
     * @param id
     * @return
     */
    ResultBean<OrgBean> actDeleteOrg(@NotNull String id);

    /**
     * 根据ID获取组织架构
     *
     * @param id
     * @return
     */
    ResultBean<OrgBean> actGetOrg(@NotNull String id);

    /**
     * 获取组织架构 (仅可用数据）
     *
     * @return
     */
    ResultBean<List<APITreeLookupBean>> actLookupOrgs();

    /**
     * 分页获取组织架构
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<OrgBean>> actGetOrgs(@NotNull @Min(0) Integer currentPage);

    //获取所有的组织架构
    ResultBean<List<OrgBean>> actGetAllOrgs();

    /**
     * 获取下级部门
     *
     * @param parentId
     * @return
     */
    ResultBean<List<OrgBean>> actGetOrgs(@NotNull String parentId);

    /**
     * 通过登录用户ID获取员工信息
     * @param loginUerId
     * @return
     */
    ResultBean<EmployeeBean> actFindEmployeeByLoginUserId(String loginUerId);

    /**
     * 获取用户姓名
     * @param loginUerId
     * @return
     */
    ResultBean<String> actGetNameByLoginUserId(String loginUerId);

    /**
     * 通过登录用户ID获取最上级部门信息
     * @param loginUerId
     * @return
     */
    ResultBean<OrgBean> actFindSuperOrgByLoginUserId(String loginUerId);


    /**
     * 通过员工ID，获取最上级部门信息
     * @param employeeId
     * @return
     */
    ResultBean<OrgBean> actFindSuperOrgByEmployeeId(String employeeId);

    /**
     *  根据部门ID，获取最上级部门信息
     * @param orgId
     * @return
     */
    ResultBean<OrgBean> actFindSuperOrgByOrgId(String orgId);

    /**
     * 获取渠道经理
     */
    ResultBean<List<EmployeeBean>> actGetEmployeeManagers();

    /**
     * 获取分期经理
     * @return
     */
    ResultBean<List<EmployeeBean>> actGetBusinessManagers();

    /**
     * 获取部门负责人所负责部门及下级部门的ID
     * @param id
     * @return
     */
    ResultBean<List<String>> getAllChildOrginfoIds(String id);

    /**
     *根据角色获取员工
     */
    ResultBean<List<EmployeeBean>> actGetEmployeeByRole(String roleId);

    //获取多个部门
    ResultBean<List<OrgBean>> actGetOrgsByIds(List<String> ids);

    ResultBean<List<String>> actGetEmployees(String orginfoId);


    /**
     * 根据登录用户的ID，获取用户所管理部门的ID，包含下级部门
     * @param loginUserId
     * @param includeChild
     * @return
     */
    ResultBean<List<String>>   actGetAllOrginfoIdByLoginUserId(String loginUserId,Boolean includeChild);

    ResultBean<List<OrgBean>> actGetOrgsByLoginUser(String loginUserId);

    /**
     * 通过部门id获取渠道经理
     */
    ResultBean<List<EmployeeBean>> actGetEmployeeManager(String orgId);

    ResultBean<Boolean> actCheckEmployeeCell(String cell);

    ResultBean<EmployeeBean> actBindEmployeeManager(String cell, String employeeOpenId);

}
