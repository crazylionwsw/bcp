package com.fuze.bcp.api.timertask.service;

import com.fuze.bcp.api.timertask.bean.ScheduleBean;
import com.fuze.bcp.bean.ResultBean;

/**
 * Created by CJ on 2017/7/3.
 */
public interface IScheduleBizService {

    ResultBean<ScheduleBean> actSaveSchedule(ScheduleBean scheduleBean);

    ResultBean<ScheduleBean> actDeleteSchedule(String id);

    ResultBean<ScheduleBean> actLookupSchedules();

    ResultBean<ScheduleBean> actGetSchedules(Integer currentPage);

    ResultBean<ScheduleBean> actDoStart(String id);

    ResultBean<ScheduleBean> actDoStop(String id);


}
