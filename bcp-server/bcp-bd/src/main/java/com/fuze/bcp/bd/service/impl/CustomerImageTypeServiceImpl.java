package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.CustomerImageType;
import com.fuze.bcp.bd.repository.CustomerImageTypeRepository;
import com.fuze.bcp.bd.service.ICustomerImageTypeService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/6/12.
 */
@Service
public class CustomerImageTypeServiceImpl extends BaseDataServiceImpl<CustomerImageType, CustomerImageTypeRepository> implements ICustomerImageTypeService {

    @Autowired
    CustomerImageTypeRepository customerImageTypeRepository;

    @Override
    public CustomerImageType getCustomerImageTypeByCode(String code) {
        return customerImageTypeRepository.findOneByCode(code);
    }

    @Override
    public CustomerImageType FindCustomerImageTypeById(String id) {
        return customerImageTypeRepository.findOne(id);
    }

    @Override
    public List<CustomerImageType> findCustomerImageTypesByIds(List<String> ids) {
        return customerImageTypeRepository.findByDataStatusAndIdIn(DataStatus.SAVE,ids);
    }

    @Override
    public List<CustomerImageType> findCustomerImageTypesByCodes(List<String> codes) {
        return customerImageTypeRepository.findByDataStatusAndCodeIn(DataStatus.SAVE,codes);
    }

    @Override
    public List<CustomerImageType> getAllOrderByCode() {
        return customerImageTypeRepository.findAllByOrderByCodeAsc();
    }

    @Override
    public Page<CustomerImageType> getCustomerImageTypesByCodes(List<String> businessTypeImageTypeCodes, Integer pageIndex, Integer pageSize) {
        PageRequest pr = new PageRequest(pageIndex, pageSize,CustomerImageType.getTsSort());
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        criteria.and("code").in(businessTypeImageTypeCodes);
        List<CustomerImageType> customerImageTypes =  mongoTemplate.find( Query.query(criteria).with(pr),CustomerImageType.class);
        return new PageImpl<CustomerImageType>(customerImageTypes,pr,mongoTemplate.count(Query.query(criteria), CustomerImageType.class));
    }
}
