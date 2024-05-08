package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.creditcar.bean.ReceptFileBean;
import com.fuze.bcp.api.creditcar.service.IReceptFileBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.ReceptFile;
import com.fuze.bcp.creditcar.service.IReceptFileService;
import com.fuze.bcp.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ${Liu} on 2017/9/19.
 */
@Service
public class BizReceptFileService implements IReceptFileBizService{

    @Autowired
    IReceptFileService iReceptFileService;

    @Autowired
    MappingService mappingService;


    @Override
    public ResultBean<ReceptFileBean> actGetReceptFile(String customerTransactionId) {
        ReceptFile receptFile = iReceptFileService.getOneByCustomerTransactionId(customerTransactionId);
        if(receptFile != null){
            return ResultBean.getSucceed().setD(mappingService.map(receptFile,ReceptFileBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<ReceptFileBean> actSaveReceptFile(ReceptFileBean receptFileBean) {
        ReceptFile receptFile = mappingService.map(receptFileBean,ReceptFile.class);
        receptFile = iReceptFileService.save(receptFile);
        return ResultBean.getSucceed().setD(mappingService.map(receptFile,ReceptFileBean.class));
    }
}
