package com.fuze.bcp.api.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.businessbook.BusinessBookBean;
import com.fuze.bcp.api.creditcar.bean.businessbook.BusinessBookExcelBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import java.util.List;

/**
 * Created by ${Liu} on 2017/12/18.
 */
public interface IBusinessBookBizService {

    /**
     * 获取台账列表
     */
    ResultBean<List<BusinessBookBean>> actGetBusinessBooks(String selectTime);


    /**
     * 导出excel表格
     */
    ResultBean<List<BusinessBookExcelBean>> actExportBusinessBook(String selectTime);

}
