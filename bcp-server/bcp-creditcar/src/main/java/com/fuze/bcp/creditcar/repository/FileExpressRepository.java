package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.FileExpress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 档案快递信息
 */
public interface FileExpressRepository extends BaseBillRepository<FileExpress,String> {

    Page<FileExpress> findByDataStatusOrderByTsDesc(Integer save, Pageable pageable);

}
