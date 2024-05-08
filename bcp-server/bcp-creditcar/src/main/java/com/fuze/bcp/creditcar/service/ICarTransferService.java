package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.creditcar.domain.CarTransfer;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Lily on 2017/9/19.
 */
public interface ICarTransferService extends IBaseBillService<CarTransfer> {

    List<ObjectId> getCarTransFinishTransactionObjectIds(String date);

}
