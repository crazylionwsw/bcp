package com.fuze.wechat.service;

import com.fuze.wechat.base.ResultBean;
import com.fuze.wechat.domain.Employee;

import java.util.List;

/**
 * Created by admin on 2017/5/31.
 */
public interface IEmployeeService {

    Employee getOneByWxUserId(String wcUserId);

    Employee getEmployeeByWxOpenId(String wxOpenId);

    List<Employee> findAllByDataStatusAndCell(Integer ds, String cell);

    ResultBean<Boolean> actCheckEmployeeCell(String cell);

    ResultBean<Employee> actBindEmployeeManager(String cell, String employeeOpenId, String employeeMpOpenId);

}
