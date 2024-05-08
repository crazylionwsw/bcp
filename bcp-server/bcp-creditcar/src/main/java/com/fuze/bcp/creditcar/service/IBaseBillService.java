package com.fuze.bcp.creditcar.service;

import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.service.IBaseService;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/11.
 */
public interface IBaseBillService<T extends BaseBillEntity> extends IBaseService<T> {

    Page<T> findAllByOrderByTsDesc(int currentPage);

    Page<T> findAllByApproveStatusOrderByTsDesc(int approveStatus, int currentPage);

    T findByCustomerId(String customerId);

    List<T> findAllByCustomerId(String customerId);



    Page<T> findAllByCustomerIds(List<String> customerIds, int currentPage);

    Page<T> findAllByApproveStatusAndCustomerIds(List<String> customerIds, int approveStatus, int currentPage);

    T findByCustomerTransactionId(String transactionId);

    T findAvailableOneByCustomerTransactionId(String transactionId);

    Page<T> findByLoginUserId(String loginUserId, int currentPage, int currentSize);

    Page<T> findPendingItemsByUser(Class<? extends T> t, String loginUserId, List<String> tids, int currentPage, int currentSize);

    Page<T> findCompletedItemsByUser(Class<? extends T> t, String loginUserId, List<String> tids, int currentPage, int currentSize);

    T findByCustomerIdOrderByTsDesc(String customerId);

    Boolean checkEditable(T bill);

    ResultBean<T> getEditableBill(String id);

    Map<Object, Object> getDailyReport(String orgid,String date, T t);

    Map<Object,Object>  getEmployeeReport(String employeeId,String date, T t);

    Map<Object,Object>  getCarDealerReport(String cardelaerId, String date, T t);


    /**
     * 根据ID不在列表的数据，并且在某个日期之前
     *
     * @param ds
     * @param approveStatus
     * @param ids
     * @return
     */
    List<T> findByDataStatusAndApproveStatusAndIdNotInAndTsLessThan(Integer ds, Integer approveStatus, List<String> ids, String date);

    List<String> getAllIdListByDataStatus(int dataStatus, String collectionName, String propertyName);

    List<ObjectId> getIdsList(Query query, String collectionName,  String propertyName);

     List<?> getAllFieldList(Query query, String collectionName, String propertyName);

    Page<T> findAllBySearchBean(Class<? extends T> t, SearchBean searchBean,int stageNum, String userId);

    //  发送档案资料或合同
    void sendImagesAndContractsToEmployee(T t, List<String> imageTypeCodes, List<String> contractCodes);

    void deleteOneByCustomerTransactionId(String customerTransactionId);
}
