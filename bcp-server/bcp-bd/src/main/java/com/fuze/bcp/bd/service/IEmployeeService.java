package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.Employee;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/5/31.
 */
public interface IEmployeeService extends IBaseDataService<Employee> {

    Employee getByLoginUserId(String userId);

    Page<Employee> findByOrgInfoId(Integer currentPage, String orgId);

    Page<Employee> findByOrgInfoIds(Integer currentPage, List<String> orgIds);

    Employee getOneByOrgInfoId(String orgId);

    List<Employee> findByOrgInfoId(String orgId);

    /**
     * 获取该部门下所有可用员工
     * @param orgId
     * @return
     */
    List<Employee> lookupByOrgInfoId(String orgId);

    List<Employee> getAllByDataStatus(Integer save);

    /**
     * 根据部门id获取到该部门下的所有员工
     * @param id
     * @return
     */
    List<Employee> getEmployeeManagersByOrginfoId(String id);

    Set<Employee> getAvaliableList(Set<String> ids);

    Employee getOneByWxUserId(String wcUserId);


    Employee getEmployeeByWxOpenId(String wxOpenId);

    List<Employee> findByCell(String cell);

}
