package com.fuze.bcp.file.service.impl;

import com.fuze.bcp.file.domain.DocumentType;
import com.fuze.bcp.file.repository.DocumentTypeRepository;
import com.fuze.bcp.file.service.IDocumentTypeService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2017/7/4.
 */
@Service
public class DocumentTypeServiceImpl extends BaseDataServiceImpl<DocumentType,DocumentTypeRepository> implements IDocumentTypeService {

    @Autowired
    DocumentTypeRepository documentTypeRepository;

    @Override
    public List<DocumentType> findByContractIds(List<String> ids) {
        return documentTypeRepository.findByDataStatusAndIdIn(DataStatus.SAVE, ids);
    }

    @Override
    public Page<DocumentType> getAllOrderByAsc(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage,20);
        return documentTypeRepository.findAllByOrderByCodeAsc(pr);
    }

    @Override
    public DocumentType getOneByTemplateObjectId(String templateObjectId) {
        return documentTypeRepository.findOneByTemplateObjectId(templateObjectId);
    }

}
