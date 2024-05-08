package com.fuze.bcp.api.creditcar.bean;


import com.fuze.bcp.bean.APIBaseBean;
import lombok.Data;

import java.util.*;


/**
 * 征信拍照
 * Created by Lily on 2017/07/17.
 */
@Data
public class CreditPhotographBean extends APICarBillBean {

    /**
     * 待生成
     */
    public final static  Integer UPLOAD_INIT = 0;

    /**
     * 正在生成
     */
    public final static Integer UPLOAD_ONGOING = 1;

    /**
     * 已生成
     */
    public final static Integer UPLOAD_SUCCEED = 8;

    /**
     * 生成失败
     */
    public final static Integer UPLOAD_FAILED = 9;

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
     * 状态
     */
    private Integer status = UPLOAD_INIT;

    private CreditCustomerBean creditCustomerBean;

    public CreditCustomerBean getCreditCustomerBean() {
        return creditCustomerBean;
    }

    public void setCreditCustomerBean(CreditCustomerBean creditCustomerBean) {
        this.creditCustomerBean = creditCustomerBean;
    }


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
