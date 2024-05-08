package com.fuze.bcp.creditcar.repository;

import com.fuze.bcp.creditcar.domain.Declaration;

/**
 *      银行报批
 * Created by zqw on 2017/8/5.
 */
public interface DeclarationRepository extends BaseBillRepository<Declaration,String> {

    Declaration findOneById(String id);
}
