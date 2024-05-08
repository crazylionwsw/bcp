package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.ReceptFile;

/**
 * Created by ${Liu} on 2017/9/19.
 */
public interface IReceptFileService extends IBaseBillService<ReceptFile> {

    ReceptFile getOneByCustomerTransactionId(String customerTransactionId);

}
