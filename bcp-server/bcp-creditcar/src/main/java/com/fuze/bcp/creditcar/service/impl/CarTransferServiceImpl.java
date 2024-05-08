package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.creditcar.domain.CarTransfer;
import com.fuze.bcp.creditcar.repository.CarTransferRepository;
import com.fuze.bcp.creditcar.service.ICarTransferService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Lily on 2017/9/19.
 */
@Service
public class CarTransferServiceImpl extends BaseBillServiceImpl<CarTransfer,CarTransferRepository> implements ICarTransferService{

    @Override
    public List<ObjectId> getCarTransFinishTransactionObjectIds(String date) {
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if (!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s", date);
            criteria.and("transferDate").regex(regex, "m");
        } else {
            criteria.and("transferDate").ne(null);
        }
        List<ObjectId> datas = getIdsList(Query.query(criteria), "so_cartransfer", "customerTransactionId");
        return datas;
    }
}
