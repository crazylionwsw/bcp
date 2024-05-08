package com.fuze.bcp.api.file.service;

import com.fuze.bcp.api.file.bean.DocumentTypeBean;
import com.fuze.bcp.api.file.bean.EmailObjectBean;
import com.fuze.bcp.api.file.bean.PushObjectBean;
import com.fuze.bcp.api.file.bean.TemplateObjectBean;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/8/16.
 */
public interface ITemplateBizService {

    ResultBean<TemplateObjectBean> actSaveTemplate(TemplateObjectBean templateObjectBean);

    ResultBean<String> actCreateFileByContract(String contractId, String transactionId, Map tempMap);

    ResultBean<String> actCreateStrByTemplate(String templateId, String transactionId, Map tempMap);

    ResultBean<String> actCreateFileByTemplate(String templateId, String transactionId, String fileType);

    ResultBean<String> actCreateFileByTemplate(String templateId, String transactionId, Map map, String fileType);

    ResultBean<String> actCreateFileByParam(String name, String transactionId, Map map, String fileType);

    ResultBean<TemplateObjectBean> actGetTemplate(String templateId);

    ResultBean<TemplateObjectBean> actGetTemplateByCode(String code);

    ResultBean<DataPageBean<TemplateObjectBean>> actGetTemplates(Integer currentPage);

    ResultBean<TemplateObjectBean> actDeleteTemplate(String templateId);

    ResultBean<TemplateObjectBean> actGetTemplates();

    ResultBean<EmailObjectBean> actSaveEmailObject(EmailObjectBean emailObjectBean);

    ResultBean<EmailObjectBean> actGetEmailObject(String emailId);

    ResultBean<EmailObjectBean> actDeleteEmailObject(String emailId);

    ResultBean<DataPageBean<EmailObjectBean>> actGetEmailObjects(Integer currentPage);

    ResultBean<PushObjectBean> actSavePushObject(PushObjectBean pushObjectBean);

    ResultBean<PushObjectBean> actGetPushObject(String pushId);

    ResultBean<PushObjectBean> actDeletePushObject(String pushId);

    ResultBean<DataPageBean<PushObjectBean>> actGetPushObjects(Integer currentPage);

    ResultBean<DocumentTypeBean> actGetDocumentType(String id);

    /**
     * 获取文档模版(带分页)
     *
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<DocumentTypeBean>> actGetDocumentTypes(Integer currentPage);

    /**
     * 获取文档模版(仅可用数据)
     *
     * @return
     */
    ResultBean<List<APILookupBean>> actLookupDocumentTypes();

    /**
     * 获取可用的邮件模板
     * @return
     */
    ResultBean<List<EmailObjectBean>> actLookupEmailObjects();

    /**
     * 获取可用的推送模板
     * @return
     */
    ResultBean<List<PushObjectBean>> actLookupPushObjects();

    /**
     * 保存文档模版
     *
     * @param contract
     * @return
     */
    ResultBean<DocumentTypeBean> actSaveDocumentType(DocumentTypeBean contract);

    /**
     * 根据 编码获取
     *
     * @param code
     * @return
     */
    ResultBean<DocumentTypeBean> actGetDocumentTypeByCode(String code);

    /**
     * 根据  模板对象（TemplateObject）的ID 获取
     *
     * @param templateObjectId 模板对象（TemplateObject）的ID
     * @return
     */
    ResultBean<DocumentTypeBean> actGetContractByTemplateObjectId(String templateObjectId);

    /**
     * 删除文档模版
     *
     * @param contractId
     * @return
     */
    ResultBean<DocumentTypeBean> actDeleteDocumentType(String contractId);

    /**
     * 获取文档模版所有数据
     *
     * @return
     */
    ResultBean<List<DocumentTypeBean>> actGetDocumentTypes();

    /**
     * 获取文档模版所有数据
     *
     * @param ids 文档模板ID
     * @return
     */
    ResultBean<List<DocumentTypeBean>> actGetDocumentTypes(List<String> ids);

}
