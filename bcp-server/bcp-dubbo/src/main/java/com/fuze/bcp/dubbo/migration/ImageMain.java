package com.fuze.bcp.dubbo.migration;

import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.utils.FileBeanUtils;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.transaction.domain.CustomerTransaction;
import com.fuze.bcp.creditcar.domain.CustomerImageFile;
import com.fuze.bcp.dubbo.migration.mongo.MongoConnect;
import com.fuze.bcp.file.domain.BcpFile;
import com.fuze.bcp.utils.NumberHelper;
import com.fuze.bcp.utils.QingStorUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/10/22.
 */
public class ImageMain {

    public static void main(String[] args) {
        MongoTemplate source = MongoConnect.getMongoTemplate("139.198.11.30", 38289, "root", "admin", "2017Fuzefenqi998", "fzfq-prod");
        MongoTemplate target = MongoConnect.getMongoTemplate("172.16.2.5", 27017, "mongolive", "admin", "FuzefenqiPa88word", "bcp_v11");
        List<CustomerTransaction> customerTransactions = target.find(new Query(Criteria.where("ts").gt("2017-03-01 00:00:00").lte("2017-05-01 00:00:00")), CustomerTransaction.class);
        for (int i = 0; i < customerTransactions.size();  i = i + 100) {
            int index = i;
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100 && index + j < customerTransactions.size(); j++) {
                    CustomerTransaction transaction = customerTransactions.get(index + j);
                    //客户档案资料迁移
                    try {
                        saveCustomerImages(source, target, transaction.getCustomerId(), transaction.getId());
                    } catch (Exception e) {
                        System.out.println("error:customerId:" + transaction.getCustomerId() + ", transactionId:" + transaction.getId());
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    public static void saveCustomerImages(MongoTemplate source, MongoTemplate target, String customerId, String tid) throws IOException {
        Query query = new Query();
        query.addCriteria(Criteria.where("customerId").is(customerId));
        List<BasicDBObject> customerImages = source.find(query, BasicDBObject.class, "cus_imagefile");
        for (int i = 0; i < customerImages.size(); i++) {

            Map obj = customerImages.get(i).toMap();

            CustomerImageFile customerImageFile = new CustomerImageFile();

            com.mongodb.DBRef customerImageType = (com.mongodb.DBRef) obj.get("customerImageType");
            Query typeQuery = new Query(Criteria.where("_id").is(customerImageType.getId()));
            BasicDBObject imageType = source.findOne(typeQuery, BasicDBObject.class,customerImageType.getCollectionName());
            customerImageFile.setCustomerImageTypeCode(imageType.getString("code"));
            if("B012".equals(customerImageFile.getCustomerImageTypeCode())){
                customerImageFile.setCustomerImageTypeCode("B050");
            }
            if("B035".equals(customerImageFile.getCustomerImageTypeCode())){
                customerImageFile.setCustomerImageTypeCode("B034");
            }
            if("B037".equals(customerImageFile.getCustomerImageTypeCode())){
                customerImageFile.setCustomerImageTypeCode("B018");
            }
            if("B038".equals(customerImageFile.getCustomerImageTypeCode())){
                customerImageFile.setCustomerImageTypeCode("B022");
            }
            if("B046".equals(customerImageFile.getCustomerImageTypeCode())){
                customerImageFile.setCustomerImageTypeCode("B033");
            }
            customerImageFile.setId(obj.get("_id").toString());
            customerImageFile.setCustomerId(customerId);
            customerImageFile.setCustomerTransactionId(tid);
            customerImageFile.setDataStatus(DataStatus.SAVE);
            customerImageFile.setLoginUserId((String) obj.get("loginUserId"));
            customerImageFile.setIsValid((Boolean) obj.get("isValid"));
            customerImageFile.setResolvedStatus((Boolean) obj.get("resolvedStatus"));
            customerImageFile.setTs((String) obj.get("ts"));
            List<String> fileIds = (List<String>) obj.get("fileIds");
            customerImageFile.setFileIds(fileIds);
            target.save(customerImageFile);
            System.out.println("保存customerImageFile");
            if (fileIds != null) {
                for (String fileId : fileIds) {
                    //获取文件
                    try {
                        System.out.println("保存文件，fileId：" + fileId);
                        GridFS gridFS = new GridFS(source.getDb());
                        GridFSDBFile gridFile = gridFS.findOne(new ObjectId(fileId));
                        if(gridFile != null){
                            //TODO: 保存FileBean;
                            FileBean fileBean = new FileBean();
                            fileBean.setId(fileId);
                            fileBean.setFileName(gridFile.getFilename());
                            fileBean.setContentType(gridFile.getContentType());
                            fileBean.setContentLength(gridFile.getLength());
                            fileBean.setBucketName(QingStorUtils.DEFAULT_BUCKET);
                            fileBean.setZoneName(QingStorUtils.DEFAULT_ZOO);
                            fileBean.setObjectname(NumberHelper.getRundomCode());
                            fileBean.setTs(gridFile.getUploadDate().toString());
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            gridFile.writeTo(byteArrayOutputStream);
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                            FileBeanUtils.uploadFile(fileBean, byteArrayInputStream);
                            BcpFile bcpFile = new BcpFile();
                            bcpFile.setId(fileBean.getId());
                            bcpFile.setFileName(gridFile.getFilename());
                            bcpFile.setContentType(gridFile.getContentType());
                            bcpFile.setContentLength(gridFile.getLength());
                            bcpFile.setBucketName(QingStorUtils.DEFAULT_BUCKET);
                            bcpFile.setZoneName(QingStorUtils.DEFAULT_ZOO);
                            bcpFile.setObjectname(fileBean.getObjectname());
                            bcpFile.setTs(gridFile.getUploadDate().toString());
                            System.out.println(fileBean.getObjectname());
                            target.save(bcpFile);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}