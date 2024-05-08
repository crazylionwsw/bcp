package com.fuze.bcp.enterprise.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lily on 2017/12/8.
 */
@RestController
@RequestMapping(value = "/json", method = {RequestMethod.GET, RequestMethod.POST})
public class SysParamController {
    @Autowired
    private IParamBizService iParamService;

    /**
     * 【PAD API】-- 获取系统参数配置项
     *
     * @param code code
     * @return
     */
    @RequestMapping(value = "/sysparam/{code}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getSysParam(@PathVariable("code") String code) {
        ResultBean resultBean = iParamService.actGetString(code);
        if (resultBean.isSucceed()) {
            JSONArray object = JSONObject.parseObject((String) resultBean.getD(), JSONArray.class);
            JSONObject res = new JSONObject();
            res.put("code", code);
            res.put("paramValue", object);
            return ResultBean.getSucceed().setD(res);
        }
        return resultBean;
    }

}
