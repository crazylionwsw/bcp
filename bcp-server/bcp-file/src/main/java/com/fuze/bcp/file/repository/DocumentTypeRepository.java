package com.fuze.bcp.file.repository;

import com.fuze.bcp.file.domain.DocumentType;
import com.fuze.bcp.repository.BaseDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by LB on 2016-10-26.
 */
public interface DocumentTypeRepository extends BaseDataRepository<DocumentType,String> {

    Page<DocumentType> findAllByOrderByCodeAsc(Pageable pageable);

    DocumentType findOneByTemplateObjectId(String templateObjectId);
}
