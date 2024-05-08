package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.Employee;
import com.fuze.bcp.bd.repository.EmployeeRepository;
import com.fuze.bcp.bd.service.IEmployeeService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/6/2.
 */
@Service
public class EmployeeServiceImpl extends BaseDataServiceImpl<Employee, EmployeeRepository> implements IEmployeeService {


    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Employee getOneByOrgInfoId(String orgId) {//
        return employeeRepository.findOneByOrgInfoId(orgId);
    }

    @Override
    public Page<Employee> findByOrgInfoId(Integer currentPage, String orgId) {
        PageRequest pr = new PageRequest(currentPage,20);
        return employeeRepository.findAllByOrgInfoIdOrderByEmployeeNoAsc(orgId,pr);

    }

    @Override
    public Page<Employee> findByOrgInfoIds(Integer currentPage, List<String> orgIds) {
        PageRequest pr = new PageRequest(currentPage,20);
        return employeeRepository.findAllByOrgInfoIdInOrderByEmployeeNoAsc(orgIds,pr);
    }

    @Override
    public Employee getByLoginUserId(String userId) {
        return employeeRepository.findByDataStatusAndLoginUserId(DataStatus.SAVE, userId);
    }

    @Override
    public List<Employee> findByOrgInfoId(String orgId) {
        return employeeRepository.findByOrgInfoId(orgId);
    }

    @Override
    public List<Employee> lookupByOrgInfoId(String orgId) {
        return employeeRepository.findByDataStatusAndOrgInfoId(DataStatus.SAVE, orgId);
    }

    @Override
    public List<Employee> getAllByDataStatus(Integer save) {
        return employeeRepository.findByDataStatus(save);
    }


    @Override
    public List<Employee> getEmployeeManagersByOrginfoId(String id) {
        return employeeRepository.findByOrgInfoId(id);
    }

    @Override
    public Set<Employee> getAvaliableList(Set<String> ids) {
        return employeeRepository.findByDataStatusAndIdIn(DataStatus.SAVE, ids);
    }

    @Override
    public Employee getOneByWxUserId(String wcUserId) {
        return employeeRepository.findByDataStatusAndWxUserId(DataStatus.SAVE, wcUserId);
    }

    @Override
    public Employee getEmployeeByWxOpenId(String wxOpenId) {
        return employeeRepository.findByDataStatusAndWxOpenid(DataStatus.SAVE,wxOpenId);
    }

    public List<Employee> findByCell(String cell) {
        return employeeRepository.findAllByCell(cell);
    }
}
