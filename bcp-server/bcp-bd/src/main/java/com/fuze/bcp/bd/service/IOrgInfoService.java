package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.Orginfo;
import com.fuze.bcp.service.ITreeDataService;

import java.util.List;
/**
 * Created by admin on 2017/5/31.
 */
public interface IOrgInfoService extends ITreeDataService<Orginfo> {

    List<Orginfo> getAllByParentIdIsNullAndDataStatus(Integer ids);

    /**
     * 获取该员工管理的部门
     * @param leaderId
     * @return
     */
    List<Orginfo> getOrgByLeaderId(String leaderId);

    /**
     *      递归      获取最上级   的组织机构
     * @param orginfo
     * @return
     */
    Orginfo findSuperOrg(Orginfo orginfo);

    /**
     * 获取所属部门及下级部门的信息
     * @param orginfoId
     * @return
     */
    List<String> getOrgInfoIdsByOrgInfoId(String orginfoId);

    /**
     * 获取员工所属部门及下级部门的信息
     * @param employeeId
     * @return
     */
    List<String> getOrgInfoIdsByEmployeeId(String employeeId,boolean includeChild);



}
