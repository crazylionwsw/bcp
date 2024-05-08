package com.fuze.bcp.utils;

import com.qingstor.sdk.config.EvnContext;
import com.qingstor.sdk.exception.QSException;
import com.qingstor.sdk.service.Bucket;
import com.qingstor.sdk.service.QingStor;
import com.qingstor.sdk.service.Types;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by CJ on 2017/6/20.
 */
public class QingStorUtils {

    public static final String QY_ACCESS_KEY_ID = "HZQVZKEHNXWLDYXTZWYQ";

    public static final String QY_SECRET_ACCESS_KEY = "jn07gPIlMfVMIcFlQZh9pyxj9MjZUDbZ3zXnyQ99";

    public static final String DEFAULT_BUCKET = "imagestoretest";

    public static final String DEFAULT_ZOO = "pek3a";

    private static QingStorUtils qingStorUtils;

    public static QingStorUtils getInstance() {
        if (qingStorUtils == null){
            synchronized(QingStorUtils.class){
                if (qingStorUtils == null)
                    qingStorUtils = new QingStorUtils();
            }
        }
        return qingStorUtils;
    }

    private QingStorUtils() {

    }

    public Bucket getBucket(String bucketName, String zoneName) throws QSException {
        QingStor storService = new QingStor(new EvnContext(QY_ACCESS_KEY_ID, QY_SECRET_ACCESS_KEY));
        Bucket bucket = storService.getBucket(bucketName, zoneName);
        return bucket;
    }

    public Bucket.PutObjectOutput createObject(String bucketName, String zoneName, String objectName, String contentType, File file) throws QSException {
        Bucket bucket = getBucket(bucketName, zoneName);
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        input.setContentType(contentType);
        input.setBodyInputFile(file);
        input.setContentLength(file.length());
        Bucket.PutObjectOutput putObjectOutput = bucket.putObject(objectName, input);
        return putObjectOutput;
    }

    public Bucket.PutObjectOutput createObject(String bucketName, String zoneName, String objectName, String contentType, InputStream inputStream, Long fileSize) throws QSException {
        Bucket bucket = getBucket(bucketName, zoneName);
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        input.setContentType(contentType);
        input.setBodyInputStream(inputStream);
        input.setContentLength(fileSize);
        Bucket.PutObjectOutput putObjectOutput = bucket.putObject(objectName, input);
        return putObjectOutput;
    }

    public Bucket.DeleteObjectOutput deleteObject(String bucketName, String zoneName, String objectname) throws QSException {
        Bucket bucket = getBucket(bucketName, zoneName);
        Bucket.DeleteObjectOutput deleteObjectOutput = bucket.deleteObject(objectname);
        return deleteObjectOutput;
    }

    public Bucket.ListObjectsOutput getObjects(String bucketName, String zoneName) throws QSException {
        Bucket bucket = getBucket(bucketName, zoneName);
        Bucket.ListObjectsOutput listObjectsOutput = bucket.listObjects(null);
        return listObjectsOutput;

    }

    public Bucket.GetObjectOutput getOneObject(String bucketName, String zoneName, String objectname) throws QSException {
        Bucket bucket = getBucket(bucketName, zoneName);
        return bucket.getObject(objectname, null);
    }


    public static void main(String[] args) throws IOException, QSException {
        QingStorUtils qingStorUtils = QingStorUtils.getInstance();
//        File file = new File("e:/aaa.png");
//        if(!file.exists()){
//            file.createNewFile();
//        }
//        InputStream inputStream = new FileInputStream(file);
//        Bucket.PutObjectOutput putObjectOutput = qingStorUtils.createObject("imagestoretest", "pek3a", "aaa","image/png", file);
//        System.out.println(file.length());
//        System.out.println(putObjectOutput.getUrl());
//        Bucket.GetObjectOutput getObjectOutput = qingStorUtils.getOneObject("imagestoretest", "pek3a", "mfile");
//        InputStream inputStream = getObjectOutput.getBodyInputStream();
//        int a = inputStream.read();
//        while (a != -1){
//            System.out.println((char) a);
//            a = inputStream.read();
//        }
        Bucket.ListObjectsOutput listObjectsOutput = qingStorUtils.getObjects("imagestoretest", "pek3a");
        while (listObjectsOutput.getKeys().size() > 0) {
            List<Types.KeyModel> keys = listObjectsOutput.getKeys();
            for (Types.KeyModel k : keys) {
                qingStorUtils.deleteObject("imagestoretest", "pek3a", k.getKey());
            }
            System.out.println("清除数据"+listObjectsOutput);
            listObjectsOutput = qingStorUtils.getObjects("imagestoretest", "pek3a");
        }


    }
}
