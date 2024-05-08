package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.file.bean.DocumentTypeBean;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by user on 2017/7/4.
 */
@RestController
@RequestMapping(value = "/json")
public class ContractController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;

    @Autowired
    private ITemplateBizService iTemplateBizService;

    /**
     * 获取文档模版(分页，升序)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DataPageBean<DocumentTypeBean>> getContracts(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        ResultBean<DataPageBean<DocumentTypeBean>> d =iTemplateBizService.actGetDocumentTypes(currentPage);
        return d;
    }

    /**
     * 获取文档模版(仅可用数据)
     *
     * @return
     */
    @RequestMapping(value = "/documents/lookups", method = RequestMethod.GET)
    public ResultBean lookupContracts() {
        return iTemplateBizService.actGetDocumentTypes();
    }

    /**
     * 保存文档模版
     *
     * @return
     */
    @RequestMapping(value = "/document", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveContract(@RequestBody DocumentTypeBean documentTypeBean) {
        return iTemplateBizService.actSaveDocumentType(documentTypeBean);
    }

    /**
     * 删除文档模版
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/document/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteContract(@PathVariable("id") String id) {
        return iTemplateBizService.actDeleteDocumentType(id);
    }

    /**
     * 根据ids列表查询合同
     * @param ids
     * @return
     */
    @RequestMapping(value="/documents",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<DocumentTypeBean>> getContractsByIds(@RequestBody List<String> ids){
        return iTemplateBizService.actGetDocumentTypes(ids);
    }
}
