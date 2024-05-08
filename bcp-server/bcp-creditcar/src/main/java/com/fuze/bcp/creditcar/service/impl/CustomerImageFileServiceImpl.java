package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.CustomerImageFile;
import com.fuze.bcp.creditcar.repository.CustomerImageFileRepository;
import com.fuze.bcp.creditcar.service.ICustomerImageFileService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 2017/7/19.
 */
@Service
public class CustomerImageFileServiceImpl extends BaseServiceImpl<CustomerImageFile, CustomerImageFileRepository> implements ICustomerImageFileService {

    @Autowired
    CustomerImageFileRepository customerImageFileRepository;

    @Override
    public List<CustomerImageFile> getCustomerImageByCustomer(String id) {
        return customerImageFileRepository.findByCustomerId(id);
    }

    @Override
    public List<CustomerImageFile> findCustomerImagesByIds(List<String> ids) {
        return customerImageFileRepository.findByDataStatusAndIdIn(DataStatus.SAVE,ids);
    }

    @Override
    public List<CustomerImageFile> findByCustomerIdAndCustomerImageType(String customerId, String customerImageTypeCode) {
        return customerImageFileRepository.findByCustomerIdAndCustomerImageTypeCode(customerId,customerImageTypeCode);
    }

    @Override
    public List<CustomerImageFile> getImages(String id, List<String> customerImageTypeCode) {
        return customerImageFileRepository.findByIdAndCustomerImageTypeCodeIn(id,customerImageTypeCode);
    }

    @Override
    public List<CustomerImageFile> getCustomerTransactionImages(String customerId, String transactionId, List<String> customerImageTypeCodes) {
        return customerImageFileRepository.findByCustomerIdAndCustomerTransactionIdAndCustomerImageTypeCodeInAndDataStatus(customerId, transactionId, customerImageTypeCodes, DataStatus.SAVE);
    }

    @Override
    public List<CustomerImageFile> getCustomerTransactionImages(String customerId, String transactionId) {
        return customerImageFileRepository.findByCustomerIdAndCustomerTransactionId(customerId, transactionId);
    }

    public List<CustomerImageFile> saveCustomerImages(String customerId, String transactionId, List<CustomerImageFile> customerImages) {
        //获取旧数据
        List<CustomerImageFile> oldCustomerImages = customerImageFileRepository.findByCustomerIdAndCustomerTransactionIdAndDataStatus(customerId, transactionId, DataStatus.SAVE);
        List<CustomerImageFile> saveCustomerImageFiles = new ArrayList<CustomerImageFile>();

        if (oldCustomerImages == null || oldCustomerImages.size() == 0) {
            saveCustomerImageFiles = customerImages;
        } else {
            for (CustomerImageFile cif : customerImages) {
                CustomerImageFile cif2 = this.mergeCustomerImages(cif, oldCustomerImages);
                if (cif2 == null) {
                    cif2 = cif;
                }
                saveCustomerImageFiles.add(cif2);
            }
        }
        return customerImageFileRepository.save(saveCustomerImageFiles);
    }

    @Override
    public List<CustomerImageFile> saveCustomerImages(List<CustomerImageFile> customerImages) {
        return customerImageFileRepository.save(customerImages);
    }


    private CustomerImageFile mergeCustomerImages(CustomerImageFile cif, List<CustomerImageFile> customerImages) {
        for (CustomerImageFile cif2: customerImages) {
            if (cif2.getCustomerImageTypeCode().equals(cif.getCustomerImageTypeCode())) {
                cif2.setFileIds(cif.getFileIds());
                return cif2;
            }
        }

        return null;
    }

    @Override
    public CustomerImageFile getCustomerImageFile(String customerId, String transactionId, String imgCode) {
        if (StringUtils.isEmpty(customerId)){
            return customerImageFileRepository.findAllByCustomerTransactionIdAndCustomerImageTypeCodeAndDataStatus(transactionId,imgCode,DataStatus.SAVE);
        }
        return customerImageFileRepository.findAllByCustomerIdAndCustomerTransactionIdAndCustomerImageTypeCodeAndDataStatus(customerId,transactionId,imgCode,DataStatus.SAVE);
    }

    @Override
    public CustomerImageFile getCustomerTransactionImage(String transactionId, String imageTypeCode) {
        Criteria criteria = Criteria.where("dataStatus").gte(DataStatus.TEMPSAVE);
        criteria.and("customerTransactionId").is(transactionId);
        criteria.and("customerImageTypeCode").is(imageTypeCode);
        CustomerImageFile imageFile =  mongoTemplate.findOne(Query.query(criteria),CustomerImageFile.class);
        return imageFile;
    }

    @Override
    public List<CustomerImageFile> getCustomerImagesByTransactionIdAndImageTypeCode(String transactionId, String imageTypeCode) {
        Criteria criteria = Criteria.where("dataStatus").gte(DataStatus.TEMPSAVE);
        criteria.and("customerTransactionId").is(transactionId);
        criteria.and("customerImageTypeCode").is(imageTypeCode);
        List<CustomerImageFile> imageFiles =  mongoTemplate.find(Query.query(criteria),CustomerImageFile.class);
        return imageFiles;
    }
}
