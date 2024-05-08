package com.fuze.bcp.file.business;

import com.fuze.bcp.api.file.bean.*;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.api.file.utils.FileBeanUtils;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.file.domain.DocumentType;
import com.fuze.bcp.file.domain.EmailObject;
import com.fuze.bcp.file.domain.PushObject;
import com.fuze.bcp.file.domain.TemplateObject;
import com.fuze.bcp.file.service.IDocumentTypeService;
import com.fuze.bcp.file.service.IEmailObjectService;
import com.fuze.bcp.file.service.IPushObjectService;
import com.fuze.bcp.file.service.ITemplateObjectService;
import com.fuze.bcp.pdf.PdfHelper;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.service.MetaDataService;
import com.fuze.bcp.service.TemplateService;
import com.fuze.bcp.utils.NumberHelper;
import com.fuze.bcp.utils.QingStorUtils;
import com.fuze.bcp.word.CustomXWPFDocument;
import com.fuze.bcp.word.WordUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/8/16.
 */
@Service
public class BizTemplateService implements ITemplateBizService {

    Logger logger = LoggerFactory.getLogger(BizTemplateService.class);


    @Value("${fuze.bcp.filePath}")
    private String filePath;

    @Autowired
    MetaDataService metaDataService;

    @Autowired
    MessageService messageService;

    @Autowired
    MappingService mappingService;

    @Autowired
    ITemplateObjectService iTemplateObjectService;

    @Autowired
    IEmailObjectService iEmailObjectService;

    @Autowired
    IPushObjectService iPushObjectService;

    @Autowired
    private IDocumentTypeService iDocumentTypeService;

    @Autowired
    TemplateService templateService;

    @Autowired
    IFileBizService iFileBizService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    IParamBizService iParamBizService;

    @Override
    public ResultBean<TemplateObjectBean> actSaveTemplate(TemplateObjectBean templateObjectBean) {
        TemplateObject templateObject = mappingService.map(templateObjectBean, TemplateObject.class);
        if (templateObject.getId() != null) {
            TemplateObject templateObject1 = iTemplateObjectService.getOne(templateObject.getId());
            if (templateObject1.getFileId() != null) {
                if (templateObject1.getFileId() == templateObject.getFileId()) {
                    iFileBizService.actDeleteFileById(templateObject1.getFileId());
                }
            }
        }
        templateObject.setFileId(templateObjectBean.getFileBean() != null ? templateObjectBean.getFileBean().getId() : null);
        templateObject = iTemplateObjectService.save(templateObject);
        templateObjectBean.setId(templateObject.getId());
        return ResultBean.getSucceed().setD(templateObjectBean);
    }

    @Override
    public ResultBean<TemplateObjectBean> actGetTemplate(String templateId) {
        TemplateObject templateObject = iTemplateObjectService.getAvailableOne(templateId);
        if (templateObject == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_TEMPLATEOBJECT_NOTFOUN_ID"), templateId));
        }
        String fileId = templateObject.getFileId();
        FileBean fileBean = iFileBizService.actGetFile(fileId).getD();
        TemplateObjectBean templateObjectBean = mappingService.map(templateObject, TemplateObjectBean.class);
        templateObjectBean.setFileBean(fileBean);
        return ResultBean.getSucceed().setD(templateObjectBean);
    }

    @Override
    public ResultBean<TemplateObjectBean> actGetTemplateByCode(String code) {
        TemplateObject templateObject = iTemplateObjectService.getTemplateObjectByCode(code);
        if (templateObject == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_TEMPLATEOBJECT_NOTFOUN_ID"), code));
        }
        String fileId = templateObject.getFileId();
        FileBean fileBean = iFileBizService.actGetFile(fileId).getD();
        TemplateObjectBean templateObjectBean = mappingService.map(templateObject, TemplateObjectBean.class);
        templateObjectBean.setFileBean(fileBean);
        return ResultBean.getSucceed().setD(templateObjectBean);
    }

    @Override
    public ResultBean<TemplateObjectBean> actGetTemplates() {

        List<TemplateObjectBean> templateObjectBeanList = new ArrayList<TemplateObjectBean>();
        List<TemplateObject> templateObjectList = iTemplateObjectService.getAvaliableAll();
        for (TemplateObject to : templateObjectList) {
            String fileId = to.getFileId();
            FileBean fileBean = iFileBizService.actGetFile(fileId).getD();
            TemplateObjectBean templateObjectBean = mappingService.map(to, TemplateObjectBean.class);
            templateObjectBean.setFileBean(fileBean);
            templateObjectBeanList.add(templateObjectBean);
        }
        return ResultBean.getSucceed().setD(templateObjectBeanList);
    }

    @Override
    public ResultBean<EmailObjectBean> actSaveEmailObject(EmailObjectBean emailObjectBean) {
        EmailObject emailObject = mappingService.map(emailObjectBean, EmailObject.class);
        emailObject = iEmailObjectService.save(emailObject);
        return ResultBean.getSucceed().setD(mappingService.map(emailObject, EmailObjectBean.class));
    }

    @Override
    public ResultBean<EmailObjectBean> actGetEmailObject(String id) {
        EmailObject emailObject = iEmailObjectService.getOne(id);
        if (emailObject != null) {
            return ResultBean.getSucceed().setD(mappingService.map(emailObject, EmailObjectBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<EmailObjectBean> actDeleteEmailObject(String id) {
        EmailObject emailObject = iEmailObjectService.delete(id);
        if (emailObject != null) {
            return ResultBean.getSucceed().setD(mappingService.map(emailObject, EmailObjectBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<EmailObjectBean>> actGetEmailObjects(Integer currentPage) {
        Page<EmailObject> emailObjects = iEmailObjectService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(emailObjects, EmailObjectBean.class));
    }

    @Override
    public ResultBean<PushObjectBean> actSavePushObject(PushObjectBean pushObjectBean) {
        PushObject pushObject = mappingService.map(pushObjectBean, PushObject.class);
        pushObject = iPushObjectService.save(pushObject);
        return ResultBean.getSucceed().setD(mappingService.map(pushObject, PushObjectBean.class));
    }

    @Override
    public ResultBean<PushObjectBean> actGetPushObject(String pushId) {
        PushObject pushObject = iPushObjectService.getOne(pushId);
        if (pushObject != null) {
            return ResultBean.getSucceed().setD(mappingService.map(pushObject, PushObjectBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PushObjectBean> actDeletePushObject(String pushId) {
        PushObject pushObject = iPushObjectService.delete(pushId);
        if (pushObject != null) {
            return ResultBean.getSucceed().setD(mappingService.map(pushObject, PushObjectBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<DataPageBean<PushObjectBean>> actGetPushObjects(Integer currentPage) {
        Page<PushObject> pushObjects = iPushObjectService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(pushObjects, PushObjectBean.class));
    }

    @Override
    public ResultBean<DataPageBean<TemplateObjectBean>> actGetTemplates(Integer currentPage) {
        Page<TemplateObject> templateObjects = iTemplateObjectService.getAll(currentPage);
        Page<TemplateObjectBean> templateObjectBeen = templateObjects.map(new Converter<TemplateObject, TemplateObjectBean>() {
            public TemplateObjectBean convert(TemplateObject source) {
                String fileId = source.getFileId();
                TemplateObjectBean tpl = mappingService.map(source, TemplateObjectBean.class);
                ResultBean<FileBean> fileBean = iFileBizService.actGetFile(fileId);
                tpl.setFileBean(fileBean.getD());
                return tpl;
            }
        });
        DataPageBean<TemplateObjectBean> destination = new DataPageBean<>();
        destination.setCurrentPage(templateObjectBeen.getNumber());
        destination.setPageSize(templateObjectBeen.getSize());
        destination.setTotalPages(templateObjectBeen.getTotalPages());
        destination.setTotalCount(templateObjectBeen.getTotalElements());
        for (TemplateObjectBean t : templateObjectBeen.getContent()) {
            destination.getResult().add(t);
        }
        return ResultBean.getSucceed().setD(destination);
    }

    @Override
    public ResultBean<TemplateObjectBean> actDeleteTemplate(String templateId) {
        TemplateObject templateObject = iTemplateObjectService.getOne(templateId);
        if (templateObject != null) {
            String fileId = templateObject.getFileId();
            FileBean fileBean = iFileBizService.actDeleteFileById(fileId).getD();
            TemplateObject templateObject1 = iTemplateObjectService.delete(templateId);
            TemplateObjectBean templateObjectBean = mappingService.map(templateObject1, TemplateObjectBean.class);
            if (fileBean != null) {
                templateObjectBean.setFileBean(fileBean);
            }
            return ResultBean.getSucceed().setD(templateObjectBean);
        }
        return ResultBean.getFailed();
    }

    public ResultBean<String> actCreateStrByTemplate(String templateId, String transactionId, Map tempMap) {
        TemplateObject templateObject = iTemplateObjectService.getAvailableOne(templateId);
        List<String> metaDatas = templateObject.getMetaDatas();
        Map tpDataMap = this.getTemplateData(transactionId, metaDatas);
        if (metaDatas.contains(MetaDataService.CUSTOMERIMAGES)) { // 客户customerImage
            Map imgMap = templateService.getCustomerImageFileMap(transactionId);
            tpDataMap.putAll(imgMap);
        }
        if (metaDatas.contains(MetaDataService.TEMPLATEIMAGES)) { // 模板需要的档案资料customerImage
            Map imgMap = templateService.getTemplateCustomerImageFilesMap(templateObject.getImageTypeCodes(), transactionId);
            tpDataMap.putAll(imgMap);
        }
        if (tempMap != null) { // 自定义参数可以 key value
            tpDataMap.putAll(tempMap);
        }
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
        configuration.setTemplateLoader(iTemplateObjectService);
        Template tpl = null;
        try {
            tpl = configuration.getTemplate(templateObject.getFileId());
            StringWriter writer = new StringWriter();
            tpl.process(tpDataMap, writer);
            writer.flush();
            String content = writer.toString();
            return ResultBean.getSucceed().setD(content);
        } catch (Exception e) {
            logger.error("error create content by template", e);
            return ResultBean.getFailed().setM("");
        }
    }

    @Override
    public ResultBean<String> actCreateFileByTemplate(String templateId, String transactionId, String fileType) {
        return actCreateFileByTemplate(templateId, transactionId, null, fileType);
    }

    @Override
    public ResultBean<String> actCreateFileByContract(String contractId, String transactionId, Map tempMap) {
        DocumentType contract = iDocumentTypeService.getAvailableOne(contractId);
        return actCreateFileByTemplate(contract.getTemplateObjectId(), transactionId, tempMap, contract.getFileType());
    }

    @Override
    public ResultBean<String> actCreateFileByTemplate(String templateId, String transactionId, Map tempMap, String fileType) {
        TemplateObject templateObject = iTemplateObjectService.getAvailableOne(templateId);
        if (templateObject == null) {
            return ResultBean.getFailed().setM("find error templateObject");
        }
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = createStreamByTemplate(templateObject, transactionId, tempMap, fileType);
            if (outputStream == null) {
                return ResultBean.getFailed();
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            FileBean fileBean = null;
            if ("pdf".equals(fileType)) {
                fileBean = new FileBean((long) outputStream.size(), templateObject.getName() + "." + fileType, QingStorUtils.DEFAULT_BUCKET, QingStorUtils.DEFAULT_ZOO, NumberHelper.getRundomCode(), "application/pdf");
            } else if ("docx".equals(fileType)) {
                fileBean = new FileBean((long) outputStream.size(), templateObject.getName() + "." + fileType, QingStorUtils.DEFAULT_BUCKET, QingStorUtils.DEFAULT_ZOO, NumberHelper.getRundomCode(), "application/msword");
            }
            FileBeanUtils.uploadFile(fileBean, byteArrayInputStream);
            String fileId = iFileBizService.actSaveFile(fileBean).getD().getId();
            return ResultBean.getSucceed().setD(fileId);
        } catch (Exception e) {
            logger.error("生成文档失败", e);
            return ResultBean.getFailed();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 调用次方法后请关闭流
     *
     * @param templateObject
     * @param transactionId
     * @param tempMap
     * @param fileType
     * @return
     * @throws Exception
     */
    private ByteArrayOutputStream createStreamByTemplate(TemplateObject templateObject, String transactionId, Map tempMap, String fileType) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if ("pdf".equals(fileType)) {
            ITextRenderer render = getPDFContentByTemplate(templateObject, transactionId, tempMap);
            render.layout();
            render.createPDF(outputStream);
            render.finishPDF();
            return outputStream;
        } else if ("docx".equals(fileType)) {
            getWORDContentByTemplate(templateObject, transactionId, tempMap).write(outputStream);
            return outputStream;
        }
        return null;
    }

    @Override
    public ResultBean<String> actCreateFileByParam(String name, String transactionId, Map tempMap, String fileType) {

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = createStreamByParam(transactionId, tempMap);
            if (outputStream == null) {
                return ResultBean.getFailed();
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            FileBean fileBean = null;
            if ("docx".equals(fileType)) {
                fileBean = new FileBean((long) outputStream.size(), name + "." + fileType, QingStorUtils.DEFAULT_BUCKET, QingStorUtils.DEFAULT_ZOO, NumberHelper.getRundomCode(), "application/msword");
            }
            FileBeanUtils.uploadFile(fileBean, byteArrayInputStream);
            String fileId = iFileBizService.actSaveFile(fileBean).getD().getId();
            return ResultBean.getSucceed().setD(fileId);
        } catch (Exception e) {
            logger.error("生成文档失败", e);
            return ResultBean.getFailed();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ByteArrayOutputStream createStreamByParam(String transactionId, Map tempMap) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream is = null;
        try{
            List<String> metaDatas = new ArrayList<String>();
            metaDatas.add("declaration");
            Map<String, Object> tpDataMap = this.getTemplateData(transactionId, metaDatas);
            Map<String,Object> declarationMap = (Map<String,Object>) tpDataMap.get("declaration");
            is = new ByteArrayInputStream(((String) declarationMap.get("stageApplication")).getBytes("utf-8"));
            POIFSFileSystem fs = new POIFSFileSystem();
            fs.createDocument(is, "WordDocument");
            fs.writeFilesystem(outputStream);
        }catch (Exception e){
            logger.error("Word文件生成失败", e);
        }finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception ex) {
                logger.error("InputStream 关闭失败", ex);
            }
        }
        return outputStream;
        //WordUtil.htmlStringToWord2((String) declarationMap.get("stageApplication"),"GBK",outputStream);
    }

    private Map getTemplateData(String transactionId, List<String> metaDatas) {
        return templateService.getMetaDatas(transactionId, metaDatas);
    }

    private ITextRenderer getPDFContentByTemplate(TemplateObject templateObject, String transactionId, Map tempMap) {
        List<String> metaDatas = templateObject.getMetaDatas();
        try {
            Map tpDataMap = this.getTemplateData(transactionId, metaDatas);
            if (metaDatas.contains(MetaDataService.CUSTOMERIMAGES)) { // 客户customerImage
                Map imgMap = templateService.getCustomerImageFileMap(transactionId);
                tpDataMap.putAll(imgMap);
            }
            if (metaDatas.contains(MetaDataService.TEMPLATEIMAGES)) { // 模板需要的档案资料customerImage
                Map imgMap = templateService.getTemplateCustomerImageFilesMap(templateObject.getImageTypeCodes(), transactionId);
                tpDataMap.putAll(imgMap);
            }
            if (tempMap != null) { // 自定义参数可以 key value
                tpDataMap.putAll(tempMap);
            }
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
            configuration.setTemplateLoader(iTemplateObjectService);
            Template tpl = configuration.getTemplate(templateObject.getFileId());
            StringWriter writer = new StringWriter();
            tpl.process(tpDataMap, writer);
            writer.flush();
            String content = writer.toString();
            ITextRenderer render = PdfHelper.getRender(filePath);
            render.setDocumentFromString(content);
            return render;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static void getDocMap(Map<String, Object> tpDataMap, String k, Map target) {
        for (String key : tpDataMap.keySet()) {
            Object obj = tpDataMap.get(key);
            String k2 = "";
            k2 = k + "." + key;
            if (obj instanceof Map) {
                Map<String, Object> temp = (Map<String, Object>) obj;
                getDocMap(temp, k2, target);
            } else {
                target.put(k2.substring(1), obj);
            }
        }
    }

    private CustomXWPFDocument getWORDContentByTemplate(TemplateObject templateObject, String transactionId, Map map) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        InputStream inputStream = null;
        try {
            Map resultMap = new HashMap();
            List<String> metaDatas = templateObject.getMetaDatas();
            Map<String, Object> tpDataMap = this.getTemplateData(transactionId, metaDatas);
            getDocMap(tpDataMap, "", resultMap);

            if (map != null) {
                resultMap.putAll(map);
            }

            FileBean fileBean = iFileBizService.actGetFile(templateObject.getFileId()).getD();
            inputStream = FileBeanUtils.downloadFile(fileBean);
            //
            Map<String, Integer> wordFontSize = (Map<String, Integer>) iParamBizService.actGetMap("WORD_FONT_SIZE").getD();
            return WordUtil.generateWord(resultMap, inputStream,wordFontSize);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public ResultBean<DataPageBean<DocumentTypeBean>> actGetDocumentTypes(Integer currentPage) {
        Page<DocumentType> contracts = iDocumentTypeService.getAllOrderByAsc(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(contracts, DocumentTypeBean.class));
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupDocumentTypes() {
        List<DocumentType> contracts = iDocumentTypeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(contracts, DocumentTypeBean.class));
    }

    @Override
    public ResultBean<List<EmailObjectBean>> actLookupEmailObjects() {
        List<EmailObject> emailObjects = iEmailObjectService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(emailObjects, EmailObjectBean.class));
    }

    @Override
    public ResultBean<List<PushObjectBean>> actLookupPushObjects() {
        List<PushObject> pushObjects = iPushObjectService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(pushObjects, PushObjectBean.class));
    }

    @Override
    public ResultBean<DocumentTypeBean> actSaveDocumentType(DocumentTypeBean contract) {
        DocumentType contract1 = iDocumentTypeService.save(mappingService.map(contract, DocumentType.class));
        return ResultBean.getSucceed().setD(mappingService.map(contract1, DocumentTypeBean.class));
    }

    @Override
    public ResultBean<DocumentTypeBean> actGetDocumentType(String id) {
        DocumentType contract = iDocumentTypeService.getOne(id);
        if (contract == null) {
            return ResultBean.getFailed().setM("合同不存在");
        }
        return ResultBean.getSucceed().setD(mappingService.map(contract, DocumentTypeBean.class));
    }

    @Override
    public ResultBean<DocumentTypeBean> actGetDocumentTypeByCode(String code) {
        DocumentType contract = iDocumentTypeService.getOneByCode(code);
        if (contract == null) {
            return ResultBean.getFailed().setM("合同不存在");
        }
        return ResultBean.getSucceed().setD(mappingService.map(contract, DocumentTypeBean.class));
    }

    @Override
    public ResultBean<DocumentTypeBean> actGetContractByTemplateObjectId(String templateObjectId) {
        DocumentType contract = iDocumentTypeService.getOneByTemplateObjectId(templateObjectId);
        if (contract == null) {
            return ResultBean.getFailed().setM("合同不存在");
        }
        return ResultBean.getSucceed().setD(mappingService.map(contract, DocumentTypeBean.class));
    }

    @Override
    public ResultBean<DocumentTypeBean> actDeleteDocumentType(String contractId) {
        DocumentType contract = iDocumentTypeService.getOne(contractId);
        if (contract != null) {
            contract = iDocumentTypeService.delete(contractId);
            return ResultBean.getSucceed().setD(mappingService.map(contract, DocumentTypeBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<DocumentTypeBean>> actGetDocumentTypes() {
        List<DocumentType> contracts = iDocumentTypeService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(contracts, DocumentTypeBean.class));
    }

    @Override
    public ResultBean<List<DocumentTypeBean>> actGetDocumentTypes(List<String> ids) {
        List<DocumentType> contracts = iDocumentTypeService.findByContractIds(ids);
        return ResultBean.getSucceed().setD(mappingService.map(contracts, DocumentTypeBean.class));
    }


}
