package com.fuze.bcp.sys.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuze.bcp.api.sys.bean.SysParamBean;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.sys.domain.SysParam;
import com.fuze.bcp.sys.service.ISysParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class BizParamService implements IParamBizService {

    private static Logger logger = LoggerFactory.getLogger(BizParamService.class);

    @Value("${fuze.bcp.web.server.url}")
    private String webServerUrl;

    @Autowired
    ISysParamService iSysParamService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Override
    public ResultBean<SysParamBean> actGetSysParam(String paramId) {
        SysParam sysParam = iSysParamService.getOne(paramId);
        if (sysParam != null) {
            return ResultBean.getSucceed().setD(mappingService.map(sysParam, SysParamBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<SysParamBean> actSaveSysParam(SysParamBean sysParam) {
        SysParam sysParam1 = iSysParamService.save(mappingService.map(sysParam, SysParam.class));
        if (sysParam1 != null) {
            return ResultBean.getSucceed().setD(mappingService.map(sysParam1, SysParamBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<SysParamBean>> actGetSysParams(Integer currentPage) {
        Page<SysParam> sysParams = iSysParamService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(sysParams, SysParamBean.class));
    }

    @Override
    public ResultBean<List<SysParamBean>> actGetSysParams() {
        List<SysParam> sysParams = iSysParamService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(sysParams, SysParamBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupParams() {
        List<SysParam> sysParams = iSysParamService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(sysParams, APILookupBean.class));
    }

    @Override
    public ResultBean<SysParamBean> actDeleteSysParam(String sysParamId) {

        SysParam sysParam = iSysParamService.getOne(sysParamId);
        if (sysParam != null) {
            sysParam = iSysParamService.delete(sysParamId);
            if (sysParam != null) {
                return ResultBean.getSucceed().setD(mappingService.map(sysParam, SysParamBean.class));
            }
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<String> actGetString(String code) {
        ResultBean<SysParam> resultBean = this.getAndCheckParamCode(code);
        if (resultBean.failed())
            return ResultBean.getFailed().setM(resultBean.getM());
        return ResultBean.getSucceed().setD(resultBean.getD().getParameterValue());
    }

    private ResultBean<SysParam> getAndCheckParamCode(String code) {
        SysParam sysParam = iSysParamService.getOneByCode(code);
        if (sysParam == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_PARAM_CODE_NOTEXIST"), code));
        }
        //  数据状态为 作废
        if (sysParam.getDataStatus() == DataStatus.DISCARD) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_PARAM_DISCARD"), code));
        }

        return ResultBean.getSucceed().setD(sysParam);
    }

    @Override
    public ResultBean<Integer> actGetInteger(String code) {
        ResultBean<SysParam> resultBean = getAndCheckParamCode(code);
        if (resultBean.failed())
            return ResultBean.getFailed().setM(resultBean.getM());
        try {
            return ResultBean.getSucceed().setD(Integer.parseInt(resultBean.getD().getParameterValue()));
        } catch (NumberFormatException ex) {
            logger.error(String.format(messageService.getMessage("MSG_PARAM_FORMART_ERROR"), code, "Double"), ex);
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_PARAM_FORMART_ERROR"), code, "Double"));
        }
    }

    @Override
    public ResultBean<Double> actGetDouble(String code) {
        ResultBean<SysParam> resultBean = this.getAndCheckParamCode(code);
        if (resultBean.failed())
            return ResultBean.getFailed().setM(resultBean.getM());
        try {
            return ResultBean.getSucceed().setD(Double.parseDouble(resultBean.getD().getParameterValue()));
        } catch (NumberFormatException ex) {
            logger.error(String.format(messageService.getMessage("MSG_PARAM_FORMART_ERROR"), code, "Double"), ex);
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_PARAM_FORMART_ERROR"), code, "Double"));
        }
    }

    @Override
    public ResultBean<Boolean> actGetBoolean(String code) {
        ResultBean<SysParam> resultBean = this.getAndCheckParamCode(code);
        if (resultBean.failed())
            return ResultBean.getFailed().setM(resultBean.getM());
        try {
            return ResultBean.getSucceed().setD(Boolean.parseBoolean(resultBean.getD().getParameterValue()));
        } catch (NumberFormatException ex) {
            String error = String.format(messageService.getMessage("MSG_PARAM_FORMART_ERROR"), code, "Boolean");
            logger.error(error, ex);
            return ResultBean.getFailed().setM(error);
        }
    }

    @Override
    public ResultBean<List<?>> actGetList(String code) {
        ResultBean<SysParam> resultBean = this.getAndCheckParamCode(code);
        if (resultBean.failed())
            return ResultBean.getFailed().setM(resultBean.getM());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList list = objectMapper.readValue(resultBean.getD().getParameterValue(), ArrayList.class);
            return ResultBean.getSucceed().setD(list);
        } catch (IOException ex) {
            String error = String.format(messageService.getMessage("MSG_PARSE_ERROR"), code, "集合", resultBean.getD().getParameterValue());
            logger.error(error, ex);
            return ResultBean.getFailed().setM(error);
        }
    }

    @Override
    public ResultBean<Map<?, ?>> actGetMap(String code) {
        ResultBean<SysParam> resultBean = this.getAndCheckParamCode(code);
        if (resultBean.failed())
            return ResultBean.getFailed().setM(resultBean.getM());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map list = objectMapper.readValue(resultBean.getD().getParameterValue(), Map.class);
            return ResultBean.getSucceed().setD(list);
        } catch (IOException ex) {
            String error = String.format(messageService.getMessage("MSG_PARSE_ERROR"), code, "键值对", resultBean.getD().getParameterValue());
            logger.error(error, ex);
            return ResultBean.getFailed().setM(error);
        }
    }

    /**
     *  从POM文件中获取，Web的访问路径
     * @return
     */
    public ResultBean<String> actGetWebServerUrl(){
        return ResultBean.getSucceed().setD(webServerUrl);
    }

    @Override
    public ResultBean<List<String>> actGetParamByCode(String code) throws Exception{
        SysParam param = iSysParamService.getOneByCode(code);
        if(param == null){
            return ResultBean.getFailed();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList arrayList = objectMapper.readValue(param.getParameterValue(), ArrayList.class);
        return ResultBean.getSucceed().setD(arrayList);
    }

}
