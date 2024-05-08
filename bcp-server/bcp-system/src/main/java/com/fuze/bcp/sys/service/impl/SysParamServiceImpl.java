package com.fuze.bcp.sys.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import com.fuze.bcp.sys.domain.SysParam;
import com.fuze.bcp.sys.repository.SysParamRepository;
import com.fuze.bcp.sys.service.ISysParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lenovo on 2017-06-13.
 */
@Service
public class SysParamServiceImpl extends BaseDataServiceImpl<SysParam, SysParamRepository> implements ISysParamService {


    private static Logger logger = LoggerFactory.getLogger(SysParamServiceImpl.class);

    private final String MSG_KEY_NOTEXIST = "根据主键%s查询不到系统参数！";

    private final String MSG_NULL = "输入的参数%s不能为空！";

    private final String MSG_CODE_NOTEXIST = "根据参数编码%s查询不到系统参数，请输入正确的参数编码！";

    private final String MSG_DISCARD = "参数编码%s已经作废，如需启用请直接重新添加参数编码！";

    private final String MSG_FORMART_ERROR = "根据参数编码%s获取对应的参数格式%s出现错误！";

    private final String MSG_PARSE_ERROR = "参数%s的值解析为%s对象时出错！参数值为%s";

    private final String MSG_EXIST_CODE = "参数%s已经存在于系统，参数值为%s，请查询后进行修改！";

    private final String MSG_JSON_ERROR = "%s转换为字符串出现错误！";

    private final String MSG_MAPS_NULL = "业务类型编码%s，对应的系统参数，不存在！";


    @Autowired
    SysParamRepository sysParamRepository;

    @Override
    public ResultBean<Map<?, ?>> getMap(String businessTypeCode) {

        ResultBean<SysParam> resultBean = getAndCheckParameterCode(businessTypeCode);
        if (resultBean.failed())
            return ResultBean.getFailed().setM(resultBean.getM());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(resultBean.getD().getParameterValue(), Map.class);
            return ResultBean.getSucceed().setD(map);
        } catch (IOException ex) {
            String error = String.format(MSG_PARSE_ERROR, businessTypeCode, "键值对", resultBean.getD().getParameterValue());
            logger.error(error, ex);
            return ResultBean.getFailed().setM(error);
        }
    }


    /**
     * 检查参数编码的正确性
     *
     * @param code
     * @return
     */
    private ResultBean<SysParam> getAndCheckParameterCode(String code) {

        SysParam systemParameter = sysParamRepository.findOneByCode(code);

        if (systemParameter == null) {
            return ResultBean.getFailed().setM(String.format(MSG_CODE_NOTEXIST, code));
        }

        //  数据状态为 作废
        if (systemParameter.getDataStatus() == DataStatus.DISCARD) {
            return ResultBean.getFailed().setM(String.format(MSG_DISCARD, code));
        }

        return ResultBean.getSucceed().setD(systemParameter);

    }

}
