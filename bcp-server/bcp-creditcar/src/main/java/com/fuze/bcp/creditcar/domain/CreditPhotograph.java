package com.fuze.bcp.creditcar.domain;

/*import com.fuze.erp.common.entity.MongoBaseEntity;
import com.fuze.erp.domain.customer.Customer;*/

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

import static com.fuze.bcp.api.creditcar.bean.CreditPhotographBean.UPLOAD_INIT;

/*import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;*/

/**
 * 征信报告上传对象
 * Created by sean on 2016/10/20.
 */
@Document(collection="so_credit_photograph")
@Data
public class CreditPhotograph extends BaseBillEntity {

    public CreditPhotograph() {
        //super.setDataStatus(DataStatus.SAVE);
    }

    /**
     * 存储的原始文件
     */
    private Map<Integer,String> imageFiles = new TreeMap<Integer, String>();

    /**
     * 生成的PDF文件ID
     */
    private String padFileId = null;

    /**
     * 文件上传是否完成
     */
    private Boolean uploadFinish = false;

    /**
     * 文件上传时间
     */
    private String submitTime = null;

    /**
     *
     */
    private Integer status = UPLOAD_INIT;


    @Override
    public String getBillTypeCode() {
        return "A016";
    }

    public Map<Integer, String> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(Map<Integer, String> imageFiles) {
        this.imageFiles = imageFiles;
    }

    public String getPadFileId() {
        return padFileId;
    }

    public void setPadFileId(String padFileId) {
        this.padFileId = padFileId;
    }

    public Boolean getUploadFinish() {
        return uploadFinish;
    }

    public void setUploadFinish(Boolean uploadFinish) {
        this.uploadFinish = uploadFinish;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    /**
     * 添加一个图片
     * @param index
     * @param fileId
     */
    public void addImageFile(Integer index,String fileId){
        imageFiles.put(index,fileId);
    }

    /**
     * 获取文件的ID集合
     * @return
     */
    public List<String> getImageFileIds(){
        List<String>    fileIds = new ArrayList<String>();
        for (Iterator<Integer> it = imageFiles.keySet().iterator(); it.hasNext();) {
            fileIds.add(imageFiles.get(it.next()));
        }
        return fileIds;
    }

    public void clearImageFiles(){
        imageFiles.clear();
    }
}
