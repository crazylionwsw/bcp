package com.fuze.bcp.file.service;

import com.fuze.bcp.file.domain.TemplateObject;
import com.fuze.bcp.service.IBaseDataService;
import freemarker.cache.TemplateLoader;

/**
 * Created by CJ on 2017/8/16.
 */
public interface ITemplateObjectService extends IBaseDataService<TemplateObject>, TemplateLoader {
    TemplateObject getTemplateObjectByCode(String code);
}
