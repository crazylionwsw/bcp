package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.creditcar.domain.Declaration;
import com.fuze.bcp.creditcar.repository.DeclarationRepository;
import com.fuze.bcp.creditcar.service.IDeclarationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *     Created by zqw on 2017/8/5.
 */
@Service
public class DeclarationServiceImpl extends BaseBillServiceImpl<Declaration, DeclarationRepository> implements IDeclarationService {

    @Autowired
    DeclarationRepository declarationRepository;

}
