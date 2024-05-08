package com.fuze.bcp.bd.business;

import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.bean.CashSourceEmployeeBean;
import com.fuze.bcp.api.bd.bean.SourceRateBean;
import com.fuze.bcp.api.bd.bean.SourceRateLookupBean;
import com.fuze.bcp.api.bd.service.ICashSourceBizService;
import com.fuze.bcp.bd.domain.CashSource;
import com.fuze.bcp.bd.domain.CashSourceEmployee;
import com.fuze.bcp.bd.domain.SourceRate;
import com.fuze.bcp.bd.repository.CashSourceRepository;
import com.fuze.bcp.bd.service.ICashSourceEmployeeService;
import com.fuze.bcp.bd.service.ICashSourceService;
import com.fuze.bcp.bd.service.ISourceRateService;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.APITreeLookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/6/14.
 */
@Service
public class BizCashSourceService implements ICashSourceBizService {

    @Autowired
    ICashSourceEmployeeService iCashSourceEmployeeService;

    @Autowired
    ICashSourceService iCashSourceService;

    @Autowired
    ISourceRateService iSourceRateService;

    @Autowired
    MappingService mappingService;

    @Autowired
    CashSourceRepository cashSourceRepository;


    @Override
    public ResultBean<List> actGetAllCashSourceType() {
        List<Map> list = new ArrayList<>();
        Map map = new HashMap<>();
        map.put("id", String.valueOf(CashSource.TYPE_BANK));
        map.put("label", "银行");
        list.add(map);

        map = new HashMap<>();
        map.put("id", String.valueOf(CashSource.TYPE_FIRM));
        map.put("label", "金融公司");
        list.add(map);

        map = new HashMap<>();
        map.put("id", String.valueOf(CashSource.TYPE_P2P));
        map.put("label", "P2P产品");
        list.add(map);

        map = new HashMap<>();
        map.put("id", String.valueOf(CashSource.TYPE_PERSONAL));
        map.put("label", "个人资金");
        list.add(map);

        map = new HashMap<>();
        map.put("id", String.valueOf(CashSource.TYPE_SELF));
        map.put("label", "自有资金");
        list.add(map);

        return ResultBean.getSucceed().setD(list);
    }

    @Override
    public ResultBean<List<CashSourceBean>> actGetCashSources() {
        List<CashSource> cashSources = iCashSourceService.getAll();
        return ResultBean.getSucceed().setD(mappingService.map(cashSources,CashSourceBean.class));
    }
    @Override
    public ResultBean<List<CashSourceBean>> actGetCashSourceBanks() {
        List<CashSource> cashSources = iCashSourceService.getCashSourcesBySourceType(CashSource.TYPE_BANK);
        return ResultBean.getSucceed().setD(mappingService.map(cashSources,CashSourceBean.class));
    }

    @Override
    public ResultBean<CashSourceBean> actSaveCashSource(CashSourceBean cashSourceBean) {
        CashSource cashSource = mappingService.map(cashSourceBean, CashSource.class);
        String parentId = cashSourceBean.getParentId();
        if(parentId != null){
            CashSource parent = iCashSourceService.getOne(parentId);
            if(parent != null){
                cashSource.setParentId(parentId);
            }
        }else{
            String pId = "0";
            cashSource.setParentId(pId);
        }
        cashSource = iCashSourceService.save(cashSource);
        return ResultBean.getSucceed().setD(mappingService.map(cashSource, CashSourceBean.class));
    }

    @Override
    public ResultBean<CashSourceBean> actDeleteCashSource(String cashsourceId) {
        CashSource cashSource = iCashSourceService.getOne(cashsourceId);
        if (cashSource != null) {
            cashSource = iCashSourceService.delete(cashsourceId);
            return ResultBean.getSucceed().setD(mappingService.map(cashSource, CashSourceBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<APITreeLookupBean>> actLookupCashSources() {
        List<CashSource> cashSources = iCashSourceService.getLookups(null, 0);
        return ResultBean.getSucceed().setD(mappingService.map(cashSources, APITreeLookupBean.class));
    }

    @Override
    public ResultBean<DataPageBean<CashSourceBean>> actGetCashSources(Integer currentPage) {
        Page<CashSource> cashSources = iCashSourceService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(cashSources, CashSourceBean.class));
    }


    @Override
    public ResultBean<List<CashSourceBean>> actGetCashSources(String parentId) {

        List<CashSource> cashSources = cashSourceRepository.findAllByParentId(parentId);
        if (cashSources == null) {
            cashSources = new ArrayList<CashSource>();
        }
        return ResultBean.getSucceed().setD(mappingService.map(cashSources, CashSourceBean.class));
    }

    @Override
    public ResultBean<CashSourceBean> actGetCashSource(String id) {
        CashSource cashSource = iCashSourceService.getOne(id);
        if (cashSource == null) {
            return ResultBean.getFailed();
        } else {
            return ResultBean.getSucceed().setD(mappingService.map(cashSource, CashSourceBean.class));
        }
    }

    @Override
    public ResultBean<CashSourceEmployeeBean> actSaveCashSourceEmployee(CashSourceEmployeeBean cashSourceEmployeeBean) {
        CashSourceEmployee cashSourceEmployee = mappingService.map(cashSourceEmployeeBean,CashSourceEmployee.class);
        cashSourceEmployee = iCashSourceEmployeeService.save(cashSourceEmployee);
        if(cashSourceEmployee != null){
            cashSourceEmployeeBean = mappingService.map(cashSourceEmployee,CashSourceEmployeeBean.class);
            return ResultBean.getSucceed().setD(cashSourceEmployeeBean);
        }
//        CashSourceEmployee cashSourceEmployee1 = iCashSourceEmployeeService.save(mappingService.map(cashSourceEmployeeBean, CashSourceEmployee.class));
//        return ResultBean.getSucceed().setD(mappingService.map(cashSourceEmployee1, CashSourceEmployeeBean.class));
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CashSourceEmployeeBean> actDeleteCashSourceEmployee(String cashSourceemployeeId) {
        CashSourceEmployee cashSourceEmployee = iCashSourceEmployeeService.getOne(cashSourceemployeeId);
        if (cashSourceEmployee != null) {
            cashSourceEmployee = iCashSourceEmployeeService.delete(cashSourceemployeeId);
            return ResultBean.getSucceed().setD(mappingService.map(cashSourceEmployee, CashSourceEmployeeBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<APILookupBean>> actLookupCashSourceEmployees() {
        List<CashSourceEmployee> cashSourceEmployees = iCashSourceEmployeeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(cashSourceEmployees, APILookupBean.class));
    }

    @Override
    public ResultBean<DataPageBean<CashSourceEmployeeBean>> actGetCashSourceEmployees(Integer currentPage) {
        Page<CashSourceEmployee> cashSourceEmployees = iCashSourceEmployeeService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(cashSourceEmployees, CashSourceEmployeeBean.class));
    }

    @Override
    public ResultBean<List<CashSourceEmployeeBean>> actGetCashSourceEmployees(String cashSourceId) {
        List<CashSourceEmployee> cashSourceEmployees = iCashSourceEmployeeService.getByCashSourceId(cashSourceId);
        if (cashSourceEmployees == null)
            cashSourceEmployees = new ArrayList<CashSourceEmployee>();

        return ResultBean.getSucceed().setD(mappingService.map(cashSourceEmployees,CashSourceEmployeeBean.class));
    }

    @Override
    public ResultBean<CashSourceEmployeeBean> actGetCashSourceEmployee(String id) {
        CashSourceEmployee cashSourceEmployee = iCashSourceEmployeeService.getOne(id);
        if(cashSourceEmployee != null){
            return ResultBean.getSucceed().setD(mappingService.map(cashSourceEmployee, CashSourceEmployeeBean.class));
        }
        return ResultBean.getFailed();

    }

    @Override
    public ResultBean<SourceRateBean> actSaveSourceRate(SourceRateBean sourceRateBean) {
        SourceRate sourceRate = iSourceRateService.save(mappingService.map(sourceRateBean, SourceRate.class));
        return ResultBean.getSucceed().setD(mappingService.map(sourceRate, SourceRateBean.class));
    }

    @Override
    public ResultBean<SourceRateBean> actDeleteSourceRate(String sourceRateId) {
        SourceRate sourceRate = iSourceRateService.getOne(sourceRateId);
        if (sourceRate != null) {
            sourceRate = iSourceRateService.delete(sourceRateId);
            return ResultBean.getSucceed().setD(mappingService.map(sourceRate, SourceRateBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<SourceRateLookupBean>> actLookupSourceRates() {
        List<SourceRate> sourceRates = iSourceRateService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(sourceRates, SourceRateLookupBean.class));
    }

    @Override
    public ResultBean<DataPageBean<SourceRateBean>> actGetSourceRates(Integer currentPage) {
        Page<SourceRate> sourceRates = iSourceRateService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(sourceRates, SourceRateBean.class));
    }

    @Override
    public ResultBean<List<SourceRateBean>> actGetSourceRates(String cashSourceId) {
        List<SourceRate> sourceRates = iSourceRateService.getByCashSourceId(cashSourceId);
        if (sourceRates == null)
            sourceRates = new ArrayList<SourceRate>();

        return ResultBean.getSucceed().setD(mappingService.map(sourceRates,SourceRateBean.class));

    }

    @Override
    public ResultBean<SourceRateBean> actGetSourceRate(String id) {
        SourceRate sourceRate = iSourceRateService.getOne(id);
        return ResultBean.getSucceed().setD(mappingService.map(sourceRate, SourceRateBean.class));
    }

    @Override
    public ResultBean<List<CashSourceBean>> actFindChildBank(String branchCode) {
       List<CashSource> cashSources = iCashSourceService.findChildBank(branchCode);
        return ResultBean.getSucceed().setD(mappingService.map(cashSources,CashSourceBean.class));
    }

    @Override
    public ResultBean<List<CashSourceEmployeeBean>> actGetAvaliableCashSourceEmployee() {
        List<CashSourceEmployee> cashSourceEmployees = iCashSourceEmployeeService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(cashSourceEmployees,CashSourceEmployeeBean.class));
    }
}
