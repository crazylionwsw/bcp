package com.fuze.bcp.file.service.impl;

import com.fuze.bcp.file.domain.EmailObject;
import com.fuze.bcp.file.repository.EmailObjectRepository;
import com.fuze.bcp.file.service.IEmailObjectService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/9/12.
 */
@Service
public class EmailObjectServiceImpl extends BaseDataServiceImpl<EmailObject, EmailObjectRepository> implements IEmailObjectService {
}
