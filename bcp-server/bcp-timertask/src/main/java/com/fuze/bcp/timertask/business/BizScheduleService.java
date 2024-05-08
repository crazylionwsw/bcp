package com.fuze.bcp.timertask.business;

import com.fuze.bcp.api.timertask.bean.ScheduleBean;
import com.fuze.bcp.api.timertask.service.IScheduleBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.timertask.service.IQuartzEventService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/7/3.
 */
@Service
public class BizScheduleService implements IScheduleBizService {

    @Autowired
    IQuartzEventService iQuartzEventService;

    @Override
    public ResultBean<ScheduleBean> actSaveSchedule(ScheduleBean scheduleBean) {
        try {
            iQuartzEventService.addQuartz();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResultBean<ScheduleBean> actDeleteSchedule(String id) {
        return null;
    }

    @Override
    public ResultBean<ScheduleBean> actLookupSchedules() {
        return null;
    }

    @Override
    public ResultBean<ScheduleBean> actGetSchedules(Integer currentPage) {
        return null;
    }

    @Override
    public ResultBean<ScheduleBean> actDoStart(String id) {
        return null;
    }

    @Override
    public ResultBean<ScheduleBean> actDoStop(String id) {
        return null;
    }
}
