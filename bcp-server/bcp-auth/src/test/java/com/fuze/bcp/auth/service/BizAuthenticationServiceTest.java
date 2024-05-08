package com.fuze.bcp.auth.service;

import com.fuze.bcp.api.auth.bean.LogManagementBean;
import com.fuze.bcp.api.auth.bean.LoginUserBean;
import com.fuze.bcp.api.auth.bean.SysResourceBean;
import com.fuze.bcp.api.auth.bean.SysRoleBean;
import com.fuze.bcp.api.auth.service.IAuthenticationBizService;
import com.fuze.bcp.bean.APITreeLookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ on 2017/6/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class BizAuthenticationServiceTest {

    @Autowired
    private IAuthenticationBizService iAuthenticationBizService;

    @Test
    public void testLogin() {
        ResultBean<String> res = iAuthenticationBizService.actGetCellCode("1231");
        System.out.println(res.getD());
    }

    @Test
    public void testActSaveSysRole() {
        SysRoleBean sysRoleBean = new SysRoleBean();
        sysRoleBean.setName("test1");
        sysRoleBean.setId("59391bef63a6e83880e39c8d");
        List list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");
        list.add("eee");
        list.add("fff");
        list.add("ggg");

        sysRoleBean.setSysResourceIds(list);
        ResultBean<SysRoleBean> result = iAuthenticationBizService.actSaveSysRole(sysRoleBean);
        System.out.println(result);
    }

    @Test
    public void testGetOneByID() {
        ResultBean<SysRoleBean> resultBean = iAuthenticationBizService.actGetSysRole("58980f1db4eaf7f74fefec9a");
        System.out.println(resultBean.getD().toString());

    }

    @Test
    public void testList() {
        List list = new ArrayList<String>();
        list.add("58980f1db4eaf7f74fefec9a");
        list.add("589aafafb4eaf75b685d0adf");
        list.add("58ac06679509f254e5a9c556");
        ResultBean<List<SysRoleBean>> data = iAuthenticationBizService.actGetSysRolesByIds(list);
        System.out.println(data);

    }

    @Test
    public void testGetSysRolesPage() {

        ResultBean<DataPageBean<SysRoleBean>> data = iAuthenticationBizService.actGetSysRoles(0); // 0

        System.out.println(data);

    }

    @Test
    public void testGetAllSysResource() {

        ResultBean<DataPageBean<SysResourceBean>> datas = iAuthenticationBizService.actGetSysResources(0);

        System.out.println(datas);

    }

    @Test
    public void testDelSysResource() {

        SysResourceBean sysResourceBean = new SysResourceBean();
        sysResourceBean.setId("5940a68ffd8ee33478f478b2");
        ResultBean<SysResourceBean> resourceBeanResultBean = iAuthenticationBizService.actDeleteSysResource(sysResourceBean.getId());
//        iAuthenticationBizService.actDeleteSysResource(resourceBeanResultBean.getD());
        System.out.println(resourceBeanResultBean);
    }

    @Test
    public void testSaveSysResource() {

        SysResourceBean sysResource = new SysResourceBean();
        sysResource.setName("test1");
        iAuthenticationBizService.actSaveSysResource(sysResource);
    }

    @Test
    public void testLookupSysResourcesPage() {

        ResultBean<List<APITreeLookupBean>> data = iAuthenticationBizService.actLookupSysResources();

        System.out.println(data);

    }

    @Test
    public void testGetSysResources() {
        List<String> ids = new ArrayList<>();
        ids.add("589824dbacd2a85e6481aed2");
        ids.add("5898250eacd2a85e6481aed3");
        ids.add("589825b5acd2a85e6481aed7");
        ResultBean<List<SysResourceBean>> data = iAuthenticationBizService.actGetSysResources(ids);

        System.out.println(data);
    }

    @Test
    public void testGetSysResourcesByPID() {

        ResultBean<List<SysResourceBean>> data = iAuthenticationBizService.actGetChildSysResources("589824dbacd2a85e6481aed2");

        System.out.println(data);
    }


    @Test
    public void testToken(){
        ResultBean<List<LogManagementBean>> listResultBean = iAuthenticationBizService.actGetLogUpCount();
    }


}
