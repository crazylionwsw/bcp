package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.CashSource;
import com.fuze.bcp.repository.TreeDataRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by sean on 2016/11/29.
 */
public interface CashSourceRepository extends TreeDataRepository<CashSource,String> {

    List<CashSource> findAllBySourceTypeAndParentIdIsNull(int type);

    List<CashSource> findAllByCodeLike(String branchCode, Sort name);

    List<CashSource> findAllByParentId(String parentId);

    List<CashSource> findAllBySourceType(int type);

}
