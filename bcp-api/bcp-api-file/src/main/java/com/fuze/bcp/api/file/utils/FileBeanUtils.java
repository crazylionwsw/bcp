package com.fuze.bcp.api.file.utils;

import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.utils.MD5Util;
import com.fuze.bcp.utils.QingStorUtils;
import com.qingstor.sdk.exception.QSException;
import com.qingstor.sdk.service.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
/**
 * Created by CJ on 2017/8/18.
 */
public class FileBeanUtils {
    static Logger logger = LoggerFactory.getLogger(FileBeanUtils.class);

    /**
     * 上传文件的重试次数
     */
    static int tryTimes = 3;

    /**
     * 上传文件并且计算MD5值
     * @param fileBean
     * @param inputStream
     * @param fileMD5
     * @throws IOException
     */
    public static void uploadFile(FileBean fileBean, InputStream inputStream,String  fileMD5) throws IOException {
        QingStorUtils qingStorUtils = QingStorUtils.getInstance();
        QSException laseEx = null;
        for(int i=0;i<tryTimes; i++) {
            try {
                qingStorUtils.createObject(fileBean.getBucketName(), fileBean.getZoneName(), fileBean.getObjectname(), fileBean.getContentType(), inputStream, fileBean.getContentLength());
                if(!StringUtils.isEmpty(fileMD5)){
                    String uploadMD5 = MD5Util.getFileMD5(downloadFile(fileBean));
                    if(StringUtils.isEmpty(uploadMD5)){
                        //青云获取的文件MD5为空，则删除青云重新上传。
                        qingStorUtils.deleteObject(fileBean.getBucketName(), fileBean.getZoneName(), fileBean.getObjectname());
                        continue;
                    }
                    if(fileMD5.equals(uploadMD5)){
                        logger.info("文件上传到青云对象存储系统成功，并且MD5校验正确！");
                        return;
                    }else{
                        //MD5不相等，则重新上传
                        qingStorUtils.deleteObject(fileBean.getBucketName(), fileBean.getZoneName(), fileBean.getObjectname());
                        continue;
                    }
                }else{
                    return;
                }
            } catch (QSException e) {
                logger.error("上传青云对象存储系统第【%s】次失败！",e);
                if( i==2){
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (Exception ex) {
                        logger.error("流关闭失败", ex);
                    }
                    //第三次抛出异常
                    throw new IOException("尝试将文件上传青云对象存储系统3次后失败，请检查原因!",laseEx);
                }
            }
        }
    }

    public static void uploadFile(FileBean fileBean, InputStream inputStream) throws IOException {
        uploadFile(fileBean,inputStream,null);
        return;
    }

    public static InputStream downloadFile(FileBean fileBean) throws IOException {
        QingStorUtils qingStorUtils = QingStorUtils.getInstance();
        try {
            Bucket.GetObjectOutput getObjectOutput = qingStorUtils.getOneObject(fileBean.getBucketName(), fileBean.getZoneName(), fileBean.getObjectname());
            return getObjectOutput.getBodyInputStream();
        } catch (QSException e) {
            throw new IOException("文件下载失败", e);
        }
    }
}