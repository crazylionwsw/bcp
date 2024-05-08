package com.fuze.bcp.sys.business;

import com.fuze.bcp.api.sys.bean.LoginLogBean;
import com.fuze.bcp.api.sys.service.ILogBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.sys.domain.LoginLog;
import com.fuze.bcp.sys.service.ILoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class BizLogService implements ILogBizService {

    @Autowired
    MappingService mapingService;

    @Autowired
    ILoginLogService iLoginLogService;

    @Override
    public ResultBean<DataPageBean<LoginLogBean>> actGetLoginLogs(Integer currentPage) {
        Page<LoginLog> loginLogs = iLoginLogService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mapingService.map(loginLogs, LoginLogBean.class));
    }

    @Override
    public ResultBean<LoginLogBean> actSaveLoginLogs(LoginLogBean loginLogBean) {
        LoginLog loginLog = iLoginLogService.save(mapingService.map(loginLogBean, LoginLog.class));
        return ResultBean.getSucceed().setD(mapingService.map(loginLog, LoginLogBean.class));
    }

    @Override
    public ResultBean<List<LoginLogBean>> actGetLoginLogs() {
        List<LoginLog> loginLogs = iLoginLogService.getAll();
        return ResultBean.getSucceed().setD(mapingService.map(loginLogs, LoginLogBean.class));
    }

    @Override
    public ResultBean<LoginLogBean> actGetLoginLog(String id) {
        LoginLog loginLog = iLoginLogService.getOne(id);
        if (loginLog != null) {
            return ResultBean.getSucceed().setD(mapingService.map(loginLog, LoginLogBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<LoginLogBean> actDeleteLoginLogs(String id) {
        LoginLog loginLog = iLoginLogService.getOne(id);
        if (loginLog != null) {
            loginLog = iLoginLogService.delete(id);
            if (loginLog != null) {
                return ResultBean.getSucceed().setD(mapingService.map(loginLog, LoginLogBean.class));
            }
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<LoginLogBean>> actLookupLoginLogs() {
        List<LoginLog> loginLogs = iLoginLogService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mapingService.map(loginLogs, LoginLogBean.class));
    }
}
