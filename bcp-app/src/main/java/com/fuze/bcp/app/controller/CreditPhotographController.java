package com.fuze.bcp.app.controller;

import com.fuze.bcp.api.creditcar.bean.CreditPhotographBean;
import com.fuze.bcp.api.creditcar.service.ICreditPhotographBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.utils.FileBeanUtils;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 文件上传下载的服务类
 * Created by Lily on 2017/7/26.
 */
@RestController
@RequestMapping(value = "/json/app", method = {RequestMethod.GET, RequestMethod.POST})
public class CreditPhotographController {

    private static final Logger logger = LoggerFactory.getLogger(CreditPhotographController.class);

    private final String MSG_ERROR_NOCUSTOMER = "根据输入的客户ID【%s】无法查询到客户信息！";
    private final String MSG_ERROR_TOTALZERO = "输入的征信图片总数不能小于1，请重新输入！";
    private final String MSG_ERROR_FILESIZE_ZERO = "上传的图片大小为0，请重新进行上传!";
    private final String MSG_ERROR_FILEREAD_ERROR = "读取上传的文件流出错，请重新上传文件！";
    private final String MSG_ERROR_FILEREAD_MD5NULL="读取上传的文件流MD5值为空，请重新上传文件！";
    private final String MSG_ERROR_FILEREAD_MD5ERROR="上传的文件流MD5值为【%s】与接口提供的MD5值不相等【%s】，请重新上传文件！";

    @Autowired
    ICreditPhotographBizService iCreditPhotographBizService;

    @Autowired
    ICustomerBizService iCustomerBizService;

    @Autowired
    IFileBizService iFileBizService;

    /**
     * 获取待上传图片的所有征信
     *
     * @return
     */
    @RequestMapping(value = "/{bankid}/creditreports", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<CreditPhotographBean>> getAll(@PathVariable("bankid") String bankid) {
        return iCreditPhotographBizService.actFindAllUnFinished(bankid);
    }

    /**
     * 获取已上传图片的所有征信
     *
     * @return
     */
    @RequestMapping(value = "/{bankid}/creditreports/history/{pageindex}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DataPageBean<CreditPhotographBean>> getAllFinish(@PathVariable("bankid") String bankid,
                                                                       @PathVariable("pageindex") Integer pageIndex,
                                                                       @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        return iCreditPhotographBizService.actFindAllFinished(bankid, pageIndex, pageSize);
    }

    /**
     * 上传征信报告图片文件
     *
     * @param totalNum
     * @param imageIndex
     * @param file
     * @return
     */
    @RequestMapping(value = "/creditreport/{customerid}/{total}/{imageIndex}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<String> uploadFiles(
            @PathVariable("customerid") String customerid,
            @PathVariable("total") Integer totalNum,
            @PathVariable("imageIndex") Integer imageIndex,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value="md5", required = false)String strMD5) {
        logger.info(file.getOriginalFilename());
        if(!file.isEmpty()){
            if(!StringUtils.isEmpty(strMD5)) {
                try {
                    String uploadMD5 = MD5Util.getFileMD5(file.getInputStream());
                    if(StringUtils.isEmpty(uploadMD5)){
                        return ResultBean.getFailed().setM(MSG_ERROR_FILEREAD_MD5NULL);
                    }
                    if(!uploadMD5.equals(strMD5)){
                        return ResultBean.getFailed().setM(String.format(MSG_ERROR_FILEREAD_MD5ERROR,uploadMD5,strMD5));
                    }
                }catch(IOException ioEx){
                    logger.error(ioEx.getMessage(),ioEx);
                    return ResultBean.getFailed().setM(MSG_ERROR_FILEREAD_ERROR);
                }
            }
        }else{
            return ResultBean.getFailed().setM(MSG_ERROR_FILESIZE_ZERO);
        }
        //客户ID不存在
        ResultBean<CustomerBean> rescus = iCustomerBizService.actGetCustomerById(customerid);
        if (rescus.getD() == null) {
            return ResultBean.getFailed().setM(String.format(MSG_ERROR_NOCUSTOMER, customerid));
        }
        //总数不能少于1
        if (totalNum < 1) {
            return ResultBean.getFailed().setM(MSG_ERROR_TOTALZERO);
        }
        FileBean fileBean = new FileBean(file);
        ResultBean<FileBean> resultBean = iFileBizService.actSaveFile(fileBean);
        String fileId = resultBean.getD().getId();
        if (resultBean.failed()) {
            //保存图片失败
            return ResultBean.getFailed().setM("保存图片失败!");
        }
        try {
            FileBeanUtils.uploadFile(fileBean, file.getInputStream(),strMD5);
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return ResultBean.getFailed().setM("文件上传失败!"+e.getMessage());
        }
        //判断之前是否存在已经保存的上传信息
        CreditPhotographBean creditPhotographBean = iCreditPhotographBizService.actFindCreditPhotographByCustomerId(customerid).getD();
        if (creditPhotographBean == null) {
            creditPhotographBean = new CreditPhotographBean();
            creditPhotographBean.setCustomerId(customerid);
            creditPhotographBean.setUploadFinish(false);
        }
        creditPhotographBean.addImageFile(imageIndex, fileId);
        creditPhotographBean = iCreditPhotographBizService.actSave(creditPhotographBean).getD();
        logger.info("文件上传完成，开始返回客户端消息！");
        return ResultBean.getSucceed().setD(fileId).setM("文件上传成功！");
    }


    /**
     * 上传征信报告图片文件
     *
     * @return
     */
    @RequestMapping(value = "/creditreport/{customerid}/finish", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<CreditPhotographBean> finishUpload(@PathVariable("customerid") String customerid) {
        return iCreditPhotographBizService.actFinishOneCustomer(customerid);
    }
}
