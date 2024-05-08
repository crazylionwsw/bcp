package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.creditcar.domain.BankCardApply;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/8/21.
 */
public interface IBankCardApplyService extends IBaseBillService<BankCardApply> {

    Page<BankCardApply> findByDataStatusAndStatusOrderByTsDesc(Integer currentPage, Integer save, Integer status);

    Page<BankCardApply> findByCustomerIdInAndDataStatusAndStatusOrderByTsDesc(List<String> customerIds, Integer save, int status, int currentPage);

    void deleteBankCard(BankCardApplyBean bankCardApply);

    List<ObjectId> getDailySwipingMoneyTransactionObjectIds(String date);

    Map<Object,Object>  getEmployeeReport(String date, BankCardApply bankCardApply ,String employeeId);
    List<String> getDailySwipingMoneyTransactionIds(String date);


    Page<BankCardApply> findByCashSourceIdOrderByTsDesc(String cashSourceId, Integer pageindex,Integer pagesize);

    Page<BankCardApply> findByCashSourceIdAndStatusOrderByTsAsc(String cashSourceId, Integer pageindex,Integer pagesize, Integer status);

    BankCardApply findByIdAndCashSourceIdOrderByTsDesc(String key,String cashSourceId);

    Page<BankCardApply> findByLoginUserIdAndApproveStatusIn(String loginUserId, List<Integer> as, Integer pageIndex, Integer pageSize);

    List<BankCardApply> getByStatus(List<Integer> ds);
}
