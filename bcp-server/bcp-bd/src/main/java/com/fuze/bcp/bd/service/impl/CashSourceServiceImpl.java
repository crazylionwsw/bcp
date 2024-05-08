package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.CashSource;
import com.fuze.bcp.bd.repository.CashSourceRepository;
import com.fuze.bcp.bd.service.ICashSourceService;
import com.fuze.bcp.service.impl.TreeDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CJ on 2017/6/14.
 */
@Service
public class CashSourceServiceImpl extends TreeDataServiceImpl<CashSource, CashSourceRepository> implements ICashSourceService {

    @Autowired
    private CashSourceRepository cashSourceRepository;

    @Override
    public List<CashSource> findChildBank(String branchCode) {
        return cashSourceRepository.findAllByCodeLike(branchCode,CashSource.getSortASC("name"));
    }
	
	@Override
    public List<CashSource> getByParentId(String parentId) {
        return cashSourceRepository.findAllByParentId(parentId);
    }

    @Override
    public List<CashSource> getCashSourcesBySourceType(Integer sourceType) {
        return cashSourceRepository.findAllBySourceType(sourceType);
    }
}
