package com.fuze.bcp.bd.service;

import com.fuze.bcp.bd.domain.CashSource;
import com.fuze.bcp.service.ITreeDataService;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by admin on 2017/6/2.
 */
public interface ICashSourceService extends ITreeDataService<CashSource> {


    List<CashSource> findChildBank(String branchCode);

    List<CashSource> getByParentId(String parentid);

    List<CashSource> getCashSourcesBySourceType(Integer sourceType);

}
