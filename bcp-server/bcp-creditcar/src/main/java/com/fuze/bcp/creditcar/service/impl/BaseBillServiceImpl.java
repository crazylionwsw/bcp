package com.fuze.bcp.creditcar.service.impl;

import com.fuze.bcp.api.creditcar.bean.CustomerContractBean;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.creditcar.service.ICustomerContractBizService;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.api.file.bean.DocumentTypeBean;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.api.msg.service.IMessageBizService;
import com.fuze.bcp.api.workflow.service.IWorkflowBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.creditcar.domain.BaseBillEntity;
import com.fuze.bcp.creditcar.repository.BaseBillRepository;
import com.fuze.bcp.creditcar.service.IBaseBillService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import com.fuze.bcp.utils.SimpleUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/7/19.
 */
public class BaseBillServiceImpl<T extends BaseBillEntity, R extends BaseBillRepository<T, String>> extends BaseServiceImpl<T, R> implements IBaseBillService<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseBillServiceImpl.class);

    @Autowired
    MessageService messageService;

    @Autowired
    ICustomerImageFileBizService iCustomerImageFileBizService;

    @Autowired
    IMessageBizService iMessageBizService;

    @Autowired
    ICustomerContractBizService iCustomerContractBizService;

    @Autowired
    ITemplateBizService iTemplateBizService;

    @Autowired
    IWorkflowBizService iWorkflowBizService;

    @Override
    public Page<T> findAllByOrderByTsDesc(int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return baseRepository.findAllByDataStatusOrderByTsDesc(DataStatus.SAVE, page);
    }

    @Override
    public Page<T> findAllByApproveStatusOrderByTsDesc(int approveStatus, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return baseRepository.findAllByApproveStatusAndDataStatusOrderByTsDesc(approveStatus, DataStatus.SAVE, page);
    }

    @Override
    public T findByCustomerId(String customerId) {
        return baseRepository.findOneByCustomerId(customerId);
    }

    @Override
    public List<T> findAllByCustomerId(String customerId) {
        return baseRepository.findAllByCustomerId(customerId);
    }

    @Override
    public Page<T> findAllByCustomerIds(List<String> customerIds, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return baseRepository.findByDataStatusAndCustomerIdInOrderByTsDesc(DataStatus.SAVE, customerIds, page);
    }

    @Override
    public Page<T> findAllByApproveStatusAndCustomerIds(List<String> customerIds, int approveStatus, int currentPage) {
        PageRequest page = new PageRequest(currentPage, 20);
        return baseRepository.findByDataStatusAndApproveStatusAndCustomerIdInOrderByTsDesc(DataStatus.SAVE, approveStatus, customerIds, page);
    }

    @Override
    public T findByCustomerTransactionId(String transactionId) {
        return baseRepository.findOneByCustomerTransactionId(transactionId);
    }

    @Override
    public T findAvailableOneByCustomerTransactionId(String transactionId) {
        return baseRepository.findAvailableOneByCustomerTransactionIdAndDataStatus(transactionId,DataStatus.SAVE);
    }

    @Override
    public Page<T> findByLoginUserId(String loginUserId, int currentPage, int currentSize) {
        PageRequest page = new PageRequest(currentPage, currentSize, T.getTsSort());
        return baseRepository.findByLoginUserId(loginUserId, page);
    }

    /**
     * 非已取消,正在进行中，审核状态(初始化，审核中，拒绝)
     * @param loginUserId
     * @param currentPage
     * @param currentSize
     * @return
     */
    @Override
    public Page<T> findPendingItemsByUser(Class<? extends T> t, String loginUserId, List<String> tids, int currentPage, int currentSize) {
        PageRequest page = new PageRequest(currentPage, currentSize, T.getTsSort());
        Query query = new Query();
        List<Integer> ass = new ArrayList<Integer>();
        ass.add(ApproveStatus.APPROVE_INIT);
        ass.add(ApproveStatus.APPROVE_ONGOING);
        ass.add(ApproveStatus.APPROVE_REAPPLY);

        Criteria c = Criteria.where("dataStatus").ne(DataStatus.DISCARD).and("loginUserId").is(loginUserId).and("approveStatus").in(ass).and("customerTransactionId").in(tids);
        query.addCriteria(c);
        query.with(page);

        long total = mongoTemplate.count(query, t);
        List items = mongoTemplate.find(query, t);
        return new PageImpl(items, page, total);
    }

    /**
     * 已取消/状态已完成/状态为正常(通过，拒绝)
     * @param loginUserId
     * @param currentPage
     * @param currentSize
     * @return
     */
    @Override
    public Page<T> findCompletedItemsByUser(Class<? extends T> t, String loginUserId, List<String> tids, int currentPage, int currentSize) {
        PageRequest page = new PageRequest(currentPage, currentSize, T.getTsSort());
        Query query = new Query();
        Criteria c = Criteria.where("dataStatus").ne(DataStatus.DISCARD).and("loginUserId").is(loginUserId);

        List<Integer> ass = new ArrayList<Integer>();
        ass.add(ApproveStatus.APPROVE_PASSED);
        ass.add(ApproveStatus.APPROVE_REJECT);
        c.orOperator(Criteria.where("approveStatus").in(ass), Criteria.where("customerTransactionId").in(tids));
        query.addCriteria(c);
        query.with(page);

        long total = mongoTemplate.count(query, t);
        List items = mongoTemplate.find(query, t);
        return new PageImpl(items, page, total);
    }

    @Override
    public T findByCustomerIdOrderByTsDesc(String customerId) {
        return baseRepository.findOneByCustomerIdOrderByTsDesc(customerId);
    }

    @Override
    public Boolean checkEditable(T bill) {
        if (bill.getApproveStatus() == ApproveStatus.APPROVE_REJECT)
            return false;
        if (bill.getApproveStatus() == ApproveStatus.APPROVE_PASSED)
            return false;

        return true;
    }

    @Override
    public ResultBean<T> getEditableBill(String id) {
        T bill = baseRepository.findOne(id);
        if (bill == null)
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_SIGN_NONULL"));
        if (bill.getApproveStatus() == ApproveStatus.APPROVE_REJECT || bill.getApproveStatus() == ApproveStatus.APPROVE_PASSED)
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_SIGN_COMPLETE"));
        if (bill.getApproveStatus() == ApproveStatus.APPROVE_ONGOING)
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_SIGN_NOSUBMIT"));

        return ResultBean.getSucceed().setD(bill);
    }

    /**
     *
     * 日报
     * @param date
     * @param t
     * @return
     */
    @Override
    public Map<Object, Object> getDailyReport(String orgid,String date, T t) {
        Map<Object,Object>  resultMap = getBillReportData(orgid,null,null,date,t);
        return resultMap;
    }

    /**
     * 按照员工进行业务统计
     *
     * @param employeeId
     * @param date
     * @param t
     * @return
     */
    public  Map<Object,Object>  getEmployeeReport(String employeeId,String date, T t ) {
        Map<Object,Object>  resultMap = getBillReportData(null,employeeId,null,date,t);
        return resultMap;
    }

    /**
     * 按照渠道进行业务统计
     *
     * @param carDealerId
     * @param date
     * @param t
     * @return
     */
    public Map<Object,Object>  getCarDealerReport(String carDealerId, String date, T t) {
        Map<Object,Object>  resultMap = getBillReportData(null,null,carDealerId,date,t);
        return resultMap;
    }

    /**
     * 查询符合条件的TransactionID
     * @param orginfoID
     * @param employeeID
     * @param cardealerId
     * @param cashSourceId
     * @param businessTypeCode
     * @return
     */
    protected List<String>  queryTransactionIds(String orginfoID,String employeeID,String cardealerId,String cashSourceId,String businessTypeCode,List<ObjectId> transactionIds,List<Integer> statusList){
        if(StringUtils.isEmpty(orginfoID)
                && StringUtils.isEmpty(employeeID)
                && StringUtils.isEmpty(cardealerId)
                && StringUtils.isEmpty(cashSourceId)
                && StringUtils.isEmpty(businessTypeCode)
                && transactionIds == null && statusList==null
                ){
            return null;
        }
        List<String>    orginfoIds = null;
        if(!StringUtils.isEmpty(orginfoID)) {
            orginfoIds = this.getOrgInfoIdsByOrgInfoId(orginfoID);
        }
        //全部新增签约
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if(orginfoIds != null && orginfoIds.size()>0){
            criteria =criteria.and("orginfoId").in(orginfoIds);
        }
        if(!StringUtils.isEmpty(employeeID)){
            criteria =criteria.and("employeeId").is(employeeID);
        }
        if(!StringUtils.isEmpty(cardealerId)){
            criteria =criteria.and("carDealerId").is(cardealerId);
        }
        if(!StringUtils.isEmpty(cashSourceId)){
            criteria =criteria.and("cashSourceId").is(cashSourceId);
        }
        if(!StringUtils.isEmpty(businessTypeCode)){
            criteria = criteria.and("businessTypeCode").is(businessTypeCode);
        }
        if(transactionIds != null){
            criteria = criteria.and("_id").in(transactionIds);
        }
        if(statusList != null){
            criteria = criteria.and("status").in(statusList);
        }
        //查找符合条件的交易
        List<String> tIds = (List<String>) getAllFieldList(new Query(criteria), "cus_transaction", "_id");

        return tIds;
    }
    /**
     * 获取日报总数
     * @param orginfoID
     * @param employeeId
     * @param cardealerId
     * @param date
     * @param t
     * @return
     */
    public Map<Object,Object> getBillReportData(String orginfoID,String employeeId,String cardealerId,String date, T t) {

        List<String>    transactionIds = queryTransactionIds(orginfoID,employeeId,cardealerId,null,null,null, getNoCanceledStatus());

        //业务总数
        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        Criteria baseCriteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if (!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s", date);
            baseCriteria.and("ts").regex(regex, "m");
        }
        if(transactionIds != null){
            baseCriteria.and("customerTransactionId").in(transactionIds);
        }
        long count = mongoTemplate.count(Query.query(baseCriteria), t.getClass());
        resultMap.put("total", count);

        //正在进行中的
        Query query = Query.query(baseCriteria);
        query.addCriteria(Criteria.where("approveStatus").is(ApproveStatus.APPROVE_ONGOING));
        count = mongoTemplate.count(query, t.getClass());
        resultMap.put("ongoing", count);

        List<Integer> approveStatusList = new ArrayList<Integer>();
        approveStatusList.add(ApproveStatus.APPROVE_PASSED);
        approveStatusList.add(ApproveStatus.APPROVE_REAPPLY);
        approveStatusList.add(ApproveStatus.APPROVE_REJECT);

        //当天审核通过或者拒绝，驳回的
        baseCriteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if (!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s", date);
            baseCriteria.and("approveDate").regex(regex, "m");
        }
        if(transactionIds != null){
            baseCriteria.and("customerTransactionId").in(transactionIds);
        }
        baseCriteria.and("approveStatus").in(approveStatusList);

        List<AggregationOperation> aos = new ArrayList<AggregationOperation>();
        aos.add(Aggregation.match(baseCriteria));
        aos.add(Aggregation.group("approveStatus").count().as("totalCount"));

        AggregationResults<BasicDBObject> wholeResults = mongoTemplate.aggregate(Aggregation.newAggregation(aos), t.getClass(), BasicDBObject.class);
        List<BasicDBObject> result = wholeResults.getMappedResults();
        for (BasicDBObject r : result) {
            int approveStatus = r.getInt("_id");
            String key = "0";
            if (approveStatus == ApproveStatus.APPROVE_PASSED) {
                key = "passed";
            }
            if (approveStatus == ApproveStatus.APPROVE_REAPPLY) {
                key = "reapply";
            }
            if (approveStatus == ApproveStatus.APPROVE_REJECT) {
                key = "reject";
            } else {
                resultMap.put("0", r.get("totalCount"));
            }
            resultMap.put(key, r.get("totalCount"));
        }

        //取消的业务（包含取消中和取消完成的）
        transactionIds = queryTransactionIds(orginfoID,employeeId,cardealerId,null,null,null,getCanceledStatus());
        baseCriteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        if (!StringUtils.isEmpty(date)) {
            String regex = String.format("^%s", date);
            baseCriteria.and("ts").regex(regex, "m");
        }
        if(transactionIds != null){
            baseCriteria.and("customerTransactionId").in(transactionIds);
        }
        count = mongoTemplate.count(Query.query(baseCriteria), t.getClass());
        resultMap.put("canceled", count);

        resultMap.putIfAbsent("passed", 0);
        resultMap.putIfAbsent("reapply", 0);
        resultMap.putIfAbsent("reject", 0);
        return resultMap;
    }

    /**
     * 根据dataStatus获取全部的数据，然后获取某个属性的值数组
     *
     * @param dataStatus
     * @param propertyName
     * @return
     */
    public List<String> getAllIdListByDataStatus(int dataStatus, String collectionName, final String propertyName) {
        Criteria criteria = Criteria.where("dataStatus").is(DataStatus.SAVE);
        final List<String>    ids = new ArrayList<String>();
        mongoTemplate.executeQuery(Query.query(criteria), collectionName, new DocumentCallbackHandler() {
            public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {
                String pvalue = null;
                if ("_id".equals(propertyName)){
                    pvalue = ((ObjectId)dbObject.get(propertyName)).toString();
                } else {
                    pvalue = (String)dbObject.get(propertyName);
                }
                if(pvalue != null){
                    ids.add(pvalue);
                }
                return ;
            }
        });
        return ids;
    }

    /**
     * 根据ID不在列表的数据，并且在某个日期之前
     * @param ds
     * @param approveStatus
     * @param ids
     * @return
     */
    public  List<T> findByDataStatusAndApproveStatusAndIdNotInAndTsLessThan(Integer ds, Integer approveStatus, List<String> ids,String date){
        return baseRepository.findByDataStatusAndApproveStatusAndIdNotInAndTsLessThan(ds,approveStatus,ids,date);
    };

    public List<Integer>    getCanceledStatus(){
        List<Integer>    status = new ArrayList<Integer>();
        status.add(CustomerTransactionBean.TRANSACTION_CANCELLED);
        status.add(CustomerTransactionBean.TRANSACTION_CANCELLING);
        return  status;
    }

    public List<Integer>    getNoCanceledStatus(){
        List<Integer>    status = new ArrayList<Integer>();
        status.add(CustomerTransactionBean.TRANSACTION_FINISH);
        status.add(CustomerTransactionBean.TRANSACTION_INIT);
        status.add(CustomerTransactionBean.TRANSACTION_PROCESSING);
        status.add(CustomerTransactionBean.TRANSACTION_WARNING);
        status.add(CustomerTransactionBean.TRANSACTION_STOP);
        return  status;
    }



    /**
     *  根据查询条件，查询
     * @param t              当前查询的单据
     * @param searchBean    查询信息
     * @param stageNum      当前查询阶段的值
     * @return
     */
    public Page<T> findAllBySearchBean(Class<? extends T> t, SearchBean searchBean,int stageNum,String userId) {
        Query query = new Query();
        //  渠道还款,数据状态单独区分
        if (!"A020".equals(searchBean.getBillTypeCode()) && !"A019".equals(searchBean.getBillTypeCode()))
            query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));

        if (searchBean.getTimeName() != null && !StringUtils.isEmpty(searchBean.getStartTime()) && !StringUtils.isEmpty(searchBean.getEndTime())){
            query.addCriteria(Criteria.where(searchBean.getTimeName()).gte(searchBean.getStartTime()).lt(SimpleUtils.getOffsetDaysTodayStrOf(searchBean.getEndTime(), 1)));
        } else if (searchBean.getTimeName() != null && !StringUtils.isEmpty(searchBean.getStartTime()) && StringUtils.isEmpty(searchBean.getEndTime())) {
            query.addCriteria(Criteria.where(searchBean.getTimeName()).gte(searchBean.getStartTime()));
        } else if (searchBean.getTimeName() != null && StringUtils.isEmpty(searchBean.getStartTime()) && !StringUtils.isEmpty(searchBean.getEndTime())) {
            query.addCriteria(Criteria.where(searchBean.getTimeName()).lt(SimpleUtils.getOffsetDaysTodayStrOf(searchBean.getEndTime(), 1)));
        }

        if (!StringUtils.isEmpty(searchBean.getStartApproveTime()) && !StringUtils.isEmpty(searchBean.getEndApproveTime())){
            query.addCriteria(Criteria.where("approveDate").gte(searchBean.getStartApproveTime()).lt(SimpleUtils.getOffsetDaysTodayStrOf(searchBean.getEndApproveTime(), 1)));
        } else if (!StringUtils.isEmpty(searchBean.getStartApproveTime()) && StringUtils.isEmpty(searchBean.getEndApproveTime())) {
            query.addCriteria(Criteria.where("approveDate").gte(searchBean.getStartApproveTime()));
        } else if (StringUtils.isEmpty(searchBean.getStartApproveTime()) && !StringUtils.isEmpty(searchBean.getEndApproveTime())) {
            query.addCriteria(Criteria.where("approveDate").lt(SimpleUtils.getOffsetDaysTodayStrOf(searchBean.getEndApproveTime(), 1)));
        }

        /*
        //  bug,暂时作废
        query.addCriteria(new Criteria()
                .andOperator(
                        !StringUtils.isEmpty(searchBean.getStartTime()) ? Criteria.where(searchBean.getTimeName()).gte(searchBean.getStartTime()) : Criteria.where(searchBean.getTimeName()).ne(null),
                        !StringUtils.isEmpty(searchBean.getEndTime()) ? Criteria.where(searchBean.getTimeName()).lt(SimpleUtils.getOffsetDaysTodayStrOf(searchBean.getEndTime(), 1)) : Criteria.where(searchBean.getTimeName()).ne(null),
                        !StringUtils.isEmpty(searchBean.getStartApproveTime()) ? Criteria.where("approveDate").gte(searchBean.getStartApproveTime()) : Criteria.where("approveDate").ne(null),
                        !StringUtils.isEmpty(searchBean.getEndApproveTime()) ? Criteria.where("approveDate").lt(SimpleUtils.getOffsetDaysTodayStrOf(searchBean.getEndApproveTime(), 1)) : Criteria.where("approveDate").ne(null)
                )
        );
        */

        if (searchBean.getStatusName() != null && searchBean.getStatusValue() != null && searchBean.getStatusValue() != -1 && searchBean.getStatusValue() != 100){
            query.addCriteria(Criteria.where(searchBean.getStatusName()).is(searchBean.getStatusValue()));
        }

        if(searchBean.getTimeName() != null && !StringUtils.isEmpty(searchBean.getDayTime())){
            query.addCriteria(Criteria.where(searchBean.getTimeName()).regex(searchBean.getDayTime(),"m"));
        }

        if ("A004".equals(searchBean.getBillTypeCode()) && searchBean.getAdvancedPay() != 0){
            query.addCriteria(Criteria.where("advancedPay").is(searchBean.getAdvancedPay()));
        }

        if (!this.checkSearchBeanNull(searchBean)){
            query.addCriteria(Criteria.where("customerTransactionId").in(this.getSearchTransactionIds(searchBean,stageNum)));
        }

        //  查询待处理数据
        if (searchBean.getStatusValue() == 100 && !StringUtils.isEmpty(userId)){
            List<String> ids = iWorkflowBizService.actGetMyBillByLoginUserId(searchBean.getBillTypeCode(), userId).getD();
            if (ids != null){
                query.addCriteria(Criteria.where("_id").in(ids));
            }
        }

        Pageable pageable = new PageRequest(searchBean.getCurrentPage(),searchBean.getPageSize());
        if (searchBean.getSortWay().equals("ASC")){
            query.with(T.getSortASC(searchBean.getSortName())).with(pageable);
        } else {
            query.with(T.getSortDESC(searchBean.getSortName())).with(pageable);
        }

        List list = mongoTemplate.find(query,t);
        Page page  = new PageImpl(list,pageable, mongoTemplate.count(query,t));
        return page;
    }

    public boolean checkSearchBeanNull(SearchBean searchBean){
        if (!StringUtils.isEmpty(searchBean.getName())){
            return  false;
        }
        if (!StringUtils.isEmpty(searchBean.getIdentifyNo())){
            return  false;
        }
        if (!StringUtils.isEmpty(searchBean.getPledgeName())){
            return  false;
        }
        if (!StringUtils.isEmpty(searchBean.getMateName())){
            return  false;
        }
        if (!StringUtils.isEmpty(searchBean.getOrginfoId())){
            return false;
        }
        if (!StringUtils.isEmpty(searchBean.getBusinessManagerId())){
            return false;
        }
        if (!StringUtils.isEmpty(searchBean.getCarDealerId())){
            return false;
        }
        if (!StringUtils.isEmpty(searchBean.getStartLimitAmount()) || !StringUtils.isEmpty(searchBean.getEndLimitAmount())){
            return false;
        }
        if (searchBean.getCreditMonths() != 0){
            return false;
        }
        if (!searchBean.getCompensatory() || !searchBean.getBusiness()){
            return false;
        }
        if (!searchBean.getNc() || !searchBean.getOc()){
            return false;
        }
        return true;
    }

    /**
     *      公用，  作废
     *     给分期经理，发送档案资料或合同
     * @param t
     * @param imageTypeCodes
     * @param contractCodes
     */
    public void sendImagesAndContractsToEmployee(T t, List<String> imageTypeCodes, List<String> contractCodes){
        Query query = new Query();

        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        query.addCriteria(Criteria.where("_id").is(t.getEmployeeId()));
        Map employee = queryCollectionBasicDBObject(query,"bd_employee").toMap();
        if (StringUtils.isEmpty(employee.get("email"))){
            logger.error(String.format(messageService.getMessage("MSG_EMPLOYEE_NULL_EMAIL"), (String) employee.get("username")));
            return;
        }
        Map emailInfo = new HashMap();

        StringBuilder imageAndContractNames = new StringBuilder();
        List<String> fileIds = new ArrayList<String>();
        query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        query.addCriteria(Criteria.where("_id").is(t.getCustomerId()));
        Map customer = queryCollectionBasicDBObject(query,"so_customer").toMap();
        Map customerImageTypeBean;
        for (String customerImageTypeCode : imageTypeCodes){
            query = new Query();
            query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
            query.addCriteria(Criteria.where("code").is(customerImageTypeCode));
            customerImageTypeBean = queryCollectionBasicDBObject(query, "bd_customerimagetype").toMap();
            if (customerImageTypeBean != null) {
                List<CustomerImageFileBean> customerImageFileBeans = iCustomerImageFileBizService.actFindByCustomerIdAndCustomerImageType(t.getCustomerId(), customerImageTypeCode).getD();
                for (CustomerImageFileBean customerImageFileBean : customerImageFileBeans){
                    List<String> customerImageFileBeanFileIds = customerImageFileBean.getFileIds();
                    if (customerImageFileBeanFileIds.size() > 0){
                        imageAndContractNames.append((String) customerImageTypeBean.get("name") + " ");
                        fileIds.addAll(customerImageFileBeanFileIds);
                    } else {
                        logger.error(String.format(messageService.getMessage("MSG_CUSTOMERIAMGETYPEFILE_NULL_FILEIDS"),customerImageTypeCode,(String) customer.get("name")));
                    }
                }
            } else {
                logger.error(String.format(messageService.getMessage("MSG_CUSTOMERIMAGETYPE_NOTFIND_NAME"), (String) customerImageTypeBean.get("name")));
            }
        }

        DocumentTypeBean documentTypeBean;
        for (String contractCode : contractCodes){
            documentTypeBean = iTemplateBizService.actGetDocumentTypeByCode(contractCode).getD();
            if (documentTypeBean != null) {

                CustomerContractBean customerContract = iCustomerContractBizService.actGetTransactionContract(t.getCustomerId(), t.getCustomerTransactionId(), documentTypeBean.getId()).getD();
                if (customerContract != null) {
                    fileIds.add(customerContract.getFileId());
                    imageAndContractNames.append(documentTypeBean.getName() + " ");
                } else {
                    customerContract = new CustomerContractBean();
                    customerContract.setCustomerId(t.getCustomerId());
                    customerContract.setCustomerTransactionId(t.getCustomerTransactionId());
                    customerContract.setDocumentId(documentTypeBean.getId());
                    customerContract = iCustomerContractBizService.actCreateCustomerContract(true, customerContract).getD();
                    if (customerContract != null) {
                        fileIds.add(customerContract.getFileId());
                        imageAndContractNames.append(documentTypeBean.getName() + " ");
                    } else {
                        logger.error(String.format(messageService.getMessage("MSG_DOCUMENT_BULL_TYPECODE_CUSTOMER"), contractCode, (String) customer.get("name")));
                    }
                }
            } else {
                logger.error(String.format(messageService.getMessage("MSG_DOCUMENT_NOTFIND_CODE"), contractCode));
            }
        }

        emailInfo.put("subject",String.format("客户【%s】的【%s】，请查收！",(String) customer.get("name"),imageAndContractNames));
        emailInfo.put("body",String.format("尊敬的分期经理【%s】，您好!<br/>附件是客户【%s】的【%s】，请尽快查收！",(String) employee.get("username"),(String) customer.get("name"),imageAndContractNames));
        emailInfo.put("fileIds",fileIds);
        if (fileIds.size() > 0)
            iMessageBizService.actSendEmail((String) employee.get("email"),emailInfo);
    }

    @Override
    public void deleteOneByCustomerTransactionId(String customerTransactionId) {
        baseRepository.deleteOneByCustomerTransactionId(customerTransactionId);
    }
}
