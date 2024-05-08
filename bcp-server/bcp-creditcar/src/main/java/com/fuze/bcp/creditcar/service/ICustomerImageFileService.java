package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.CustomerImageFile;
import com.fuze.bcp.service.IBaseService;

import java.util.List;

/**
 * Created by Lily on 2017/7/19.
 */
public interface ICustomerImageFileService extends IBaseService<CustomerImageFile> {
    List<CustomerImageFile> getCustomerImageByCustomer(String id);

    List<CustomerImageFile> findCustomerImagesByIds(List<String> ids);

    List<CustomerImageFile> findByCustomerIdAndCustomerImageType(String customerId, String customerImageTypeCode);

    List<CustomerImageFile> getCustomerTransactionImages(String customerId, String transactionId, List<String> customerImageTypeCodes);

    List<CustomerImageFile> getCustomerTransactionImages(String customerId, String transactionId);

    List<CustomerImageFile> saveCustomerImages(String customerId, String transactionId, List<CustomerImageFile> customerImages);

    List<CustomerImageFile> saveCustomerImages(List<CustomerImageFile> customerImages);

    List<CustomerImageFile> getImages(String id,List<String> customerImageTypeCode);

    CustomerImageFile getCustomerImageFile(String customerId,String transactionId,String imgCode);

    CustomerImageFile getCustomerTransactionImage(String transactionId, String imageTypeCode);

    List<CustomerImageFile> getCustomerImagesByTransactionIdAndImageTypeCode(String transactionId, String imageTypeCode);
}
