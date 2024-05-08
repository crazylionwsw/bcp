package com.fuze.bcp.file.service.impl;

import com.fuze.bcp.file.domain.PushObject;
import com.fuze.bcp.file.repository.PushObjectRepository;
import com.fuze.bcp.file.service.IPushObjectService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/9/12.
 */
@Service
public class PushObjectServiceImpl extends BaseDataServiceImpl<PushObject, PushObjectRepository> implements IPushObjectService {
}
