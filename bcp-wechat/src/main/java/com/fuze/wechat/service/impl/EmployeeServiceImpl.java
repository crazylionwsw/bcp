package com.fuze.wechat.service.impl;

import com.fuze.wechat.base.DataStatus;
import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.Employee;
import com.fuze.wechat.repository.EmployeeRepository;
import com.fuze.wechat.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by admin on 2017/6/2.
 */
@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Employee getOneByWxUserId(String wcUserId) {
        return employeeRepository.findByDataStatusAndWxUserId(DataStatus.SAVE, wcUserId);
    }

    @Override
    public Employee getEmployeeByWxOpenId(String wxOpenId) {
        return employeeRepository.findByDataStatusAndWxOpenid(DataStatus.SAVE,wxOpenId);
    }

    public List<Employee> findAllByDataStatusAndCell(Integer ds, String cell) {
        return employeeRepository.findAllByDataStatusAndCell(ds, cell);
    }

    public ResultBean<Boolean> actCheckEmployeeCell(String cell){
        cell = StringUtils.trimAllWhitespace(cell);
        List<Employee> employees = employeeRepository.findAllByDataStatusAndCell(DataStatus.SAVE ,cell);
        if (employees.size() > 0){
            return ResultBean.getSucceed().setD(true);
        }
        return ResultBean.getFailed().setD(false);
    }

    public ResultBean<Employee> actBindEmployeeManager(String cell, String employeeOpenId, String employeeMpOpenId) {
        cell = StringUtils.trimAllWhitespace(cell);
        List<Employee> employees = employeeRepository.findAllByDataStatusAndCell(DataStatus.SAVE ,cell);
        if (employees.size() > 0){
            Employee employee = employees.get(0);
            if (!StringUtils.isEmpty(employee.getWxOpenid())){
                return ResultBean.getFailed().setM("该手机号已经绑定！");
            }

            if (!StringUtils.isEmpty(employeeOpenId)) employee.setWxOpenid(employeeOpenId);
            if (!StringUtils.isEmpty(employeeMpOpenId)) employee.setMpOpenId(employeeMpOpenId);

            employee = employeeRepository.save(employee);
            return ResultBean.getSucceed().setD(employee);
        }
        return ResultBean.getFailed().setM("系统中未查到相应的用户信息！");
    }
}
