package com.fuze.bcp.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.utils.FileBeanUtils;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.qingstor.sdk.exception.QSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/json", method = {RequestMethod.GET, RequestMethod.POST})
public class FileController {


    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    IFileBizService iFileBizService;


    /**
     * 下载图片文件
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
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
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
            logger.error("文件下载失败", e);
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
     * 文件上传
     *
     * @param mfile
     * @return
     * @throws IOException
     * @throws QSException
     */
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean actUpFile(@RequestParam("file") MultipartFile mfile) {
        FileBean fileBean = new FileBean(mfile);
        fileBean.setUrl("http://" + fileBean.getBucketName() + "." + fileBean.getZoneName() + ".qingstor.com/" + fileBean.getObjectname());
        try {
            FileBeanUtils.uploadFile(fileBean, mfile.getInputStream());
            return iFileBizService.actSaveFile(fileBean);
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            return ResultBean.getFailed().setM("文件上传失败");
        }
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

    /**
     *删除文件  (直接删除)
     */
    @RequestMapping(value = "/real/file",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean actRealDeleteFile(@PathVariable String id){
        return iFileBizService.actRealDeleteFileById(id);
    }

}
