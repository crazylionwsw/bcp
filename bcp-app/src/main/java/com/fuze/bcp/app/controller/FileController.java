package com.fuze.bcp.app.controller;

import com.fuze.bcp.api.bd.bean.EmployeeBean;
import com.fuze.bcp.api.bd.service.IOrgBizService;
import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.utils.FileBeanUtils;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.utils.MD5Util;
import com.fuze.bcp.utils.StringHelper;
import com.qingstor.sdk.exception.QSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user on 2017/6/20.
 */
@RestController
@RequestMapping(value = "/json/app", method = {RequestMethod.GET, RequestMethod.POST})
public class FileController extends BaseController {

    Logger logger = LoggerFactory.getLogger(FileController.class);

    private final String MSG_ERROR_FILESIZE_ZERO = "上传的图片大小为0，请重新进行上传!";
    private final String MSG_ERROR_FILEREAD_ERROR = "读取上传的文件流出错，请重新上传文件！";
    private final String MSG_ERROR_FILEREAD_MD5NULL = "读取上传的文件流MD5值为空，请重新上传文件！";
    private final String MSG_ERROR_FILEREAD_MD5ERROR = "上传的文件流MD5值为【%s】与接口提供的MD5值不相等【%s】，请重新上传文件！";


    @Autowired
    IFileBizService iFileBizService;

    @Autowired
    IOrgBizService iOrgBizService;

    /**
     * 【PAD-API】-- 下载图片文件
     *
     * @param id
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/file/download/{id}", method = RequestMethod.GET)
    @ResponseBody
    public void downloadFile(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
        //重置响应头
        response.reset();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            FileBean resultBean = iFileBizService.actGetFile(id).getD();
            if (resultBean != null) {
                InputStream inputStream = FileBeanUtils.downloadFile(resultBean);
                response.setContentType(resultBean.getContentType());
                response.setHeader("Content-disposition", "filename=" + new String(resultBean.getFileName().getBytes("utf-8"), "ISO8859-1"));
                response.setHeader("Content-Length", String.valueOf(resultBean.getContentLength()));
                bis = new BufferedInputStream(inputStream);
                bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length)) ) {
                    bos.write(buff, 0, bytesRead);
                }
            } else {
                bos = new BufferedOutputStream(response.getOutputStream());
                response.setHeader("Content-disposition", "face; filename=nofound.txt");
                response.setHeader("Content-Length", "200");
                bos.write("文件不存在了！".getBytes());
            }
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("文件上传失败", e);
        } finally {
            try {
                if (bis != null)
                    bis.close();
            } catch (Exception ex) {
                logger.error("InputStream 关闭失败", ex);
            }
            try {
                if (bos != null)
                    bos.close();
            } catch (Exception ex) {
                logger.error("OutputStream 关闭失败", ex);
            }
        }
    }

    /**
     * 获取文件列表(带分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<DataPageBean<FileBean>> actGetFiles(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        ResultBean<DataPageBean<FileBean>> files = iFileBizService.actGetFiles(currentPage);
        return files;
    }

    /**
     * 【PAD-API】-- 文件上传
     *
     * @param mfile
     * @return
     * @throws IOException
     * @throws QSException
     */
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean actUpFile(@RequestParam("file") MultipartFile mfile,
                                @RequestParam(value="md5", required = false)String strMD5) {
        if (mfile == null) {
            return ResultBean.getFailed().setM("文件为空！");
        }
        if(!mfile.isEmpty()){
            if(!StringUtils.isEmpty(strMD5)) {
                try {
                    String uploadMD5 = MD5Util.getFileMD5(mfile.getInputStream());
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
        FileBean fileBean = new FileBean(mfile);
        try {
            FileBeanUtils.uploadFile(fileBean, mfile.getInputStream(), strMD5);
            ResultBean<FileBean> resultBean = iFileBizService.actSaveFile(fileBean);
            return resultBean.setM("文件上传成功！");
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            return ResultBean.getFailed().setM(e.getMessage());
        }
    }

    /**
     * 【PAD-API】-- 头像上传
     *
     * @param mfile
     * @return
     */
    @RequestMapping(value = "/header/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean actUploadHeader(@RequestParam("file") MultipartFile mfile) {
        if (mfile == null) {
            return ResultBean.getFailed().setM("头像文件为空！");
        }
        FileBean fileBean = new FileBean(mfile);
        fileBean.setUrl("http://" + fileBean.getBucketName() + "." + fileBean.getZoneName() + ".qingstor.com/" + fileBean.getObjectname());
        try {
            FileBeanUtils.uploadFile(fileBean, mfile.getInputStream());
            fileBean = iFileBizService.actSaveFile(fileBean).getD();
            if (fileBean != null && StringHelper.isNotBlock(fileBean.getId())) {
                EmployeeBean employeeBean = iOrgBizService.actFindEmployeeByLoginUserId(super.getOperatorId()).getD();
                if (employeeBean != null) {
                    employeeBean.setAvatarFileId(fileBean.getId());
                    return this.iOrgBizService.actSaveEmployee(employeeBean);
                } else {
                    return ResultBean.getFailed().setM("查找员工信息失败，请联系管理员！");
                }
            }
        } catch (Exception e) {
            logger.error("文件上传失败", e);
        }
        return ResultBean.getFailed().setM("用户头像上传失败，请重试！");
    }

    /**
     * 删除文件
     *
     * @param id
     */
    @RequestMapping(value = "/file/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean actDeleteFile(@PathVariable String id) throws QSException {
        iFileBizService.actDeleteFileById(id);
        return ResultBean.getSucceed();
    }
}
