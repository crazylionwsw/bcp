package com.fuze.bcp.file.service;

import com.fuze.bcp.file.domain.DocumentType;
import com.fuze.bcp.service.IBaseDataService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by user on 2017/7/4.
 */
public interface IDocumentTypeService extends IBaseDataService<DocumentType>{

    List<DocumentType> findByContractIds(List<String> ids);

    Page<DocumentType> getAllOrderByAsc(Integer currentPage);

    DocumentType getOneByTemplateObjectId(String templateObjectId);

}
