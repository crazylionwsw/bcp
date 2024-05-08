package com.fuze.bcp.file.service.impl;

import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.utils.FileBeanUtils;
import com.fuze.bcp.file.domain.TemplateObject;
import com.fuze.bcp.file.repository.TemplateObjectRepository;
import com.fuze.bcp.file.service.ITemplateObjectService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.entity.StringTemplateSource;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import com.fuze.bcp.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;

/**
 * Created by CJ on 2017/8/16.
 */
@Service
public class TemplateObjectServiceImpl extends BaseDataServiceImpl<TemplateObject, TemplateObjectRepository> implements ITemplateObjectService {

    Logger logger = LoggerFactory.getLogger(TemplateObjectServiceImpl.class);

    @Autowired
    IFileBizService iFileBizService;

    @Override
    public Object findTemplateSource(String s) throws IOException {
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            ResultBean<FileBean> result = iFileBizService.actGetFile(s);
            if (result.failed()) {
                return null;
            }
            FileBean fileBean = result.getD();
            inputStream = FileBeanUtils.downloadFile(fileBean);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return new StringTemplateSource(s, sb.toString(), DateTimeUtils.getSimpleDateFormat().parse(fileBean.getTs()).getTime());
        } catch (ParseException e) {
            logger.error("", e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return null;
    }

    @Override
    public long getLastModified(Object templateSource) {
        return ((StringTemplateSource) templateSource).getLastModified();
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) {
        return new StringReader(((StringTemplateSource) templateSource).getSource());
    }

    @Override
    public void closeTemplateSource(Object templateSource) {
    }

    @Override
    public TemplateObject getTemplateObjectByCode(String code) {
        return super.baseRepository.findOneByCode(code);
    }
}
