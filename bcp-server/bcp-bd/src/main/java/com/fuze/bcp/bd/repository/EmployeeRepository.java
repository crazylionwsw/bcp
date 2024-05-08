package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.Employee;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

/**
 * Created by guotaiping on 2016/10/20.
 */
public interface EmployeeRepository extends BaseDataRepository<Employee,String>{

    /**
     * 根据人名进行查找
     * @param username
     * @param pageable
     * @return
     */
    Page<Employee> findByUsernameLike(String username, Pageable pageable);

    /**
     * 查找部门的人员，带分页
     * @param orgInfoId
     * @return
     */
    Page<Employee> findAllByOrgInfoIdOrderByEmployeeNoAsc(String orgInfoId, Pageable pageable);

    /**
     * 查询所有的员工（包括子部门）
     * @param orgInfoIds
     * @param pageable
     * @return
     */
    Page<Employee> findAllByOrgInfoIdInOrderByEmployeeNoAsc(List<String> orgInfoIds, Pageable pageable);

    /**
     * 不带分页
     * @param
     * @param sort
     * @return
     */
    List<Employee> findByOrgInfoId(String orgInfoId, Sort sort);

    /**
     * 根据手机号查找
     * @param cell
     * @return
     */
    Employee findByCell(String cell);

    /**
     * 根据登录账号查询员工信息
     * @param loginUserId
     * @return
     */
    Employee findByDataStatusAndLoginUserId(Integer ds, String loginUserId);

    /**
     * 取得部门下员工列表
     * @param save
     * @param orgInfoId
     * @return
     */
    List<Employee> findByDataStatusAndOrgInfoId(Integer save, String orgInfoId);


    Employee findOneByOrgInfoId(String orgInfoId);

    /**
     *返回子级部门员工列表
     * @param orgInfoId
     * @return
     */
    List<Employee> findByOrgInfoId(String orgInfoId);

    /**
     * 正常状态的员工
     * @param save
     * @return
     */
    List<Employee> findByDataStatus(Integer save);

    Set<Employee> findByDataStatusAndIdIn(Integer ds, Set<String> ids);


    Employee findByDataStatusAndWxUserId(Integer save, String wcUserId);


    Employee findByDataStatusAndWxOpenid(Integer save,String wxOpenId);

    List<Employee> findAllByCell(String cell);

}
