package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.file.bean.EmailObjectBean;
import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.bean.PushObjectBean;
import com.fuze.bcp.api.file.bean.TemplateObjectBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by CJ on 2017/8/18.
 */
@RestController
@RequestMapping("/json")
public class TemplateObjController {

    @Autowired
    IFileBizService iFileBizService;

    @Autowired
    ITemplateBizService iTemplateBizService;

    /**
     * 保存
     * @param templateObjectBean
     * @param fileBeanId
     * @return
     */
    @RequestMapping(value="/template/save/{id}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<TemplateObjectBean> saveTemplate(@RequestBody TemplateObjectBean templateObjectBean, @PathVariable("id") String fileBeanId) {
        ResultBean<FileBean> result = iFileBizService.actGetFile(fileBeanId);
        if (result.failed()) {
            return ResultBean.getFailed().setD("保存失败，未找到对应模板文件,ID:"+fileBeanId);
        }
        templateObjectBean.setFileBean(result.getD());
        return iTemplateBizService.actSaveTemplate(templateObjectBean);
    }

    /**
     * 获取模版列表数据(带分页)
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/templates",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTemplate(@RequestParam(value = "currentPage",defaultValue = "0")Integer currentPage){
        return iTemplateBizService.actGetTemplates(currentPage);
    }

    @RequestMapping(value = "/template/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteTemplate(@PathVariable("id") String id){
        return iTemplateBizService.actDeleteTemplate(id);
    }

    /**
     *      根据 ID   回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/template/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTemplate(@PathVariable("id") String id){
        return iTemplateBizService.actGetTemplate(id);
    }

    @RequestMapping(value = "/templates/all",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTemplates(){
        return iTemplateBizService.actGetTemplates();
    }


    /*************  邮件模版维护    ********************************/
    /**
     * 获取所有邮件模版(带分页)
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/emailobjects",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getEmailObjects(@RequestParam(value = "currentPage",defaultValue = "0")Integer currentPage){
        return iTemplateBizService.actGetEmailObjects(currentPage);
    }

    /**
     * 获取可用的邮件模板
     * @return
     */
    @RequestMapping(value = "/emailobjects/lookups",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupEmailObjects(){
        return iTemplateBizService.actLookupEmailObjects();
    }

    /**
     * 保存邮件模版
     * @param emailObjectBean
     * @return
     */
    @RequestMapping(value = "/emailobject",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveEmailObject(@RequestBody EmailObjectBean emailObjectBean){
        return iTemplateBizService.actSaveEmailObject(emailObjectBean);
    }

    /**
     * 删除邮件模版
     * @param emailId
     * @return
     */
    @RequestMapping(value = "/emailobject/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteEmailObject(@PathVariable("id") String emailId){
        return iTemplateBizService.actDeleteEmailObject(emailId);
    }

    /**
     * 获取单个邮件模版
     * @param emailId
     * @return
     */
    @RequestMapping(value = "/emailobject",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getOneEmailObject(@PathVariable("id") String emailId){
        return iTemplateBizService.actGetEmailObject(emailId);
    }


    /*************************推送模板***************************/
    /**
     * 获取所有推送模版(带分页)
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/pushtemplates",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getPushObjects(@RequestParam(value = "currentPage",defaultValue = "0")Integer currentPage){
        return iTemplateBizService.actGetPushObjects(currentPage);
    }


    /**
     * 保存推送模板
     * @param pushObjectBean
     * @return
     */
    @RequestMapping(value = "/pushtemplate",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean savePushObject(@RequestBody PushObjectBean pushObjectBean){
        return iTemplateBizService.actSavePushObject(pushObjectBean);
    }

    /**
     * 删除推送模板
     * @param pushId
     * @return
     */
    @RequestMapping(value = "/pushtemplate/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deletePushObject(@PathVariable("id") String pushId){
        return iTemplateBizService.actDeletePushObject(pushId);
    }

    /**
     * 获取可用的推送模板
     * @return
     */
    @RequestMapping(value = "/pushtemplates/lookups",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupPushObjects(){
        return iTemplateBizService.actLookupPushObjects();
    }


}
