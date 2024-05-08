package com.fuze.bcp.service.impl;

import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.SearchBean;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.repository.BaseRepository;
import com.fuze.bcp.service.IBaseService;
import com.fuze.bcp.utils.Collections3;
import com.fuze.bcp.utils.DateTimeUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by admin on 2017/6/2.
 */
public class BaseServiceImpl<T extends MongoBaseEntity, R extends BaseRepository<T, String>> implements IBaseService<T> {

    @Autowired
    protected R baseRepository;

    @Autowired
    public MongoTemplate mongoTemplate;

    @Override
    public T getOne(String id) {
        return baseRepository.findOne(id);
    }

    @Override
    public T getAvailableOne(String id) {
        return baseRepository.findOneByDataStatusAndId(DataStatus.SAVE, id);
    }

    @Override
    public T getAvaliableOneSortBy(Sort sort) {
        return baseRepository.findOneByDataStatus(DataStatus.SAVE, sort);
    }

    @Override
    public List<T> getAll() {
        return baseRepository.findAll();
    }

    @Override
    public List<T> getAll(Sort sort) {
        return baseRepository.findAll(sort);
    }

    @Override
    public List<T> getAvaliableAll() {
        return baseRepository.findAllByDataStatus(DataStatus.SAVE);
    }

    @Override
    public List<T> getAvaliableAll(Sort sort) {
        return baseRepository.findAllByDataStatus(DataStatus.SAVE,sort);
    }


    @Override
    public Page<T> getAll(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20);
        return baseRepository.findAll(pr);
    }

    @Override
    public Page<T> getAvaliableAll(Integer currentPage) {
        PageRequest pr = new PageRequest(currentPage, 20);
        return baseRepository.findByDataStatus(DataStatus.SAVE, pr);
    }

    @Override
    public Page<T> getAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }

    @Override
    public Page<T> getAvaliableAll(Pageable pageable) {
        return baseRepository.findByDataStatus(DataStatus.SAVE, pageable);
    }

    @Override
    public List<T> getAvaliableList(List<String> ids) {
        return baseRepository.findByDataStatusAndIdIn(DataStatus.SAVE, ids);
    }

    @Override
    public Page<T> search(String q) {
        return null;
    }

    @Override
    public T tempSave(T t) {
        t.setDataStatus(DataStatus.TEMPSAVE);
        return this.save(t);
    }

    @Override
    public T delete(String id) {
        T t = baseRepository.findOne(id);
        if (t != null) {
            if (t.getDataStatus() == DataStatus.DISCARD) {
                baseRepository.delete(id);
            } else {
                t.setDataStatus(DataStatus.DISCARD);
                t = baseRepository.save(t);
            }
        }
        return t;
    }

    @Override
    public T discard(String id) {
        T t = baseRepository.findOne(id);
        if (t != null) {
            t.setDataStatus(DataStatus.DISCARD);
            t = baseRepository.save(t);
        }
        return t;
    }

    @Override
    public T deleteReal(String id) {
        T t = baseRepository.findOne(id);
        if (t != null) {
            baseRepository.delete(id);
        }
        return t;
    }

    @Override
    public void deleteReal(List<T> ts) {
        baseRepository.delete(ts);
    }

    @Override
    public void deleteRealByIds(List<String> ids) {
        for (String id : ids) {
            deleteReal(id);
        }
    }

    @Override
    public T save(T t) {
        if (t.getId() == null)
            t.setTs(DateTimeUtils.getCreateTime());
        else if (t.getTs() == null) {
            Document entity = t.getClass().getAnnotation(Document.class);
            String collectionName = entity.collection();
            BasicDBObject obj = mongoTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(t.getId()))), BasicDBObject.class, collectionName);
            if (obj != null) {
                t.setTs((String) obj.get("ts"));
            }
        }
        return baseRepository.save(t);
    }

    @Override
    public List<T> save(List<T> ts) {
        ts.forEach(t -> save(t));
        return ts;
    }

    public BasicDBObject queryCollectionBasicDBObject(Query query, String collectionName){
        BasicDBObject obj = mongoTemplate.findOne(query, BasicDBObject.class, collectionName);
        return  obj;
    }

    public List<?> getAllFieldList(Query query, String collectionName,final String propertyName) {
        final List<String>    ids = new ArrayList<String>();
        mongoTemplate.executeQuery(query, collectionName, new DocumentCallbackHandler() {
            public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {
                String pvalue = null;
                if ("_id".equals(propertyName)){
                    pvalue = ((ObjectId)dbObject.get(propertyName)).toString();
                } else {
                    pvalue = (String)dbObject.get(propertyName);
                }
                if(pvalue != null){
                    ids.add(pvalue);}
                return ;
            }
        });
        return ids;
    }

    public List<ObjectId> getIdsList(Query query, String collectionName, final String propertyName) {
        final List<ObjectId>  ids = new ArrayList<ObjectId>();
        mongoTemplate.executeQuery(query, collectionName, new DocumentCallbackHandler() {
            public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {
                ObjectId pvalue = new ObjectId(dbObject.get(propertyName).toString());
                if(pvalue != null){
                    ids.add(pvalue);
                }
                return ;
            }
        });
        return ids;
    }

    public List<String> getIdStringList(Query query, String collectionName, final String propertyName) {
        final List<String>  ids = new ArrayList<String>();
        mongoTemplate.executeQuery(query, collectionName, new DocumentCallbackHandler() {
            public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {
                String pvalue =  (String) dbObject.get(propertyName);
                if(pvalue != null){
                    ids.add(pvalue);
                }
                return ;
            }
        });
        return ids;
    }

    //  根据查询条件，查询部门信息
    public List<String> getOrgInfoIdsByOrgInfoId(String orginfoId){
        List<String> childOrgInfos = new ArrayList<String>();
        childOrgInfos.add(orginfoId);
        List<BasicDBObject> basicDBObjects = mongoTemplate.find(Query.query(Criteria.where("parentId").is(orginfoId)).addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE)), BasicDBObject.class,"bd_orginfo");
        if (basicDBObjects != null && basicDBObjects.size() > 0){
            for (BasicDBObject bb : basicDBObjects){
                childOrgInfos.addAll(this.getOrgInfoIdsByOrgInfoId(bb.get("_id").toString()));
            }
        }
        return childOrgInfos;
    }

    //  根据查询条件，获取符合条件的交易ID
    public List<String> getSearchTransactionIds(SearchBean searchBean, int stageNum){
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        //  查询customerId
        query.addCriteria(Criteria.where("customerId").in(this.getCustomerIdsBySearchBean(searchBean,0)));

        //  查询部门信息
        if (!StringUtils.isEmpty(searchBean.getOrginfoId())){
            query.addCriteria(Criteria.where("orginfoId").in(this.getOrgInfoIdsByOrgInfoId(searchBean.getOrginfoId())));
        }
        //  查询分期经理
        if (!StringUtils.isEmpty(searchBean.getBusinessManagerId())){
            query.addCriteria(Criteria.where("employeeId").is(searchBean.getBusinessManagerId()));
        }
        //  查询渠道信息
        if (!StringUtils.isEmpty(searchBean.getCarDealerId())){
            query.addCriteria(Criteria.where("carDealerId").is(searchBean.getCarDealerId()));
        }
        //  查询报单行
        if (!StringUtils.isEmpty(searchBean.getCashSourceId())){
            query.addCriteria(Criteria.where("cashSourceId").is(searchBean.getCashSourceId()));
        }
        //  根据当前阶段，查询借款信息
        List<String> ids = new ArrayList<>();
        if (stageNum == SearchBean.STAGE_TRANSACTION){
            ids.addAll(this.getTransactionIdsBySearchBean(searchBean));
        } else if (stageNum == SearchBean.STAGE_DEMAND){
            ids.addAll(this.getTransactionIdsByDemandCustomerLoanId(searchBean));
        } else {
            ids.addAll(this.getTransactionIdsByOrderCustomerLoanId(searchBean));
        }

        //  业务类型
        if (searchBean.getNc() && !searchBean.getOc()){//只查询新车
            query.addCriteria(Criteria.where("businessTypeCode").is("NC"));
        } else if (!searchBean.getNc() && searchBean.getOc()){//只查询二手车
            query.addCriteria(Criteria.where("businessTypeCode").is("OC"));
        }

        //  指标人
        if (!StringUtils.isEmpty(searchBean.getPledgeName())){
            //  符合条件的交易IDs
            List<String> accordIds = new ArrayList<>();
            accordIds = Collections3.stringListDistinct(Collections3.union(this.getTransactionIdsByPledgeCustomerIsAnother(searchBean),this.getTransactionIdsByPledgeCustomerIsSelf(searchBean)));
            ids = Collections3.intersection(ids,accordIds);//取交集
        }

        //  配偶
        if (!StringUtils.isEmpty(searchBean.getMateName())){
            //  符合条件的交易IDs
            List<String> accordIds = new ArrayList<>();
            accordIds = this.getTransactionIdsByMateCustomer(searchBean);
            ids = Collections3.intersection(ids,accordIds);//取交集
        }

        //查询刷卡信息
        if(!StringUtils.isEmpty(searchBean.getSwipingCardTime())){
            //  符合条件的交易IDs
            List<String> accordIds = new ArrayList<>();
            accordIds = Collections3.stringListDistinct(Collections3.union(this.getTransactionIdsBySwipingCardTime(searchBean),this.getTransactionIdsBySwipingTrusteeTime(searchBean)));
            ids = Collections3.intersection(ids,accordIds);//取交集
        }
        query.addCriteria(Criteria.where("_id").in(ids));

        return (List<String>) this.getAllFieldList(query,"cus_transaction","_id");
    }

    /**
     *    根据查询条件，查询客户信息
     * @param searchBean
     * @param relation  0：本人  1：配偶 2：指标人
     * @return
     */
    public List<String> getCustomerIdsBySearchBean(SearchBean searchBean,Integer relation){
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        String  name = "";
        switch(relation){
            case 0:
                name = searchBean.getName();
                break;
            case 1:
                name = searchBean.getMateName();
                break;
            case 2:
                name = searchBean.getPledgeName();
                break;
        }
        if (!StringUtils.isEmpty(name)){
            query.addCriteria(Criteria.where("name").regex(Pattern.compile("^.*"+ name +".*$", Pattern.CASE_INSENSITIVE)));
        }
        if (!StringUtils.isEmpty(searchBean.getIdentifyNo())){
            query.addCriteria(Criteria.where("identifyNo").regex(Pattern.compile("^.*"+ searchBean.getIdentifyNo() +".*$", Pattern.CASE_INSENSITIVE)));
        }
        return (List<String>) this.getAllFieldList(query, "so_customer", "_id");
    }

    //  查询配偶的交易IDS
    public List<String> getTransactionIdsByMateCustomer(SearchBean searchBean){
        Criteria criteria = new Criteria();
        criteria.and("dataStatus").is(DataStatus.SAVE);
        criteria.and("mateCustomerId").in(this.getCustomerIdsBySearchBean(searchBean,1));
        return (List<String>) this.getAllFieldList(new Query().addCriteria(criteria), "so_customerdemand", "customerTransactionId");
    }

    //  查询指标人不是本人的交易IDS
    public List<String> getTransactionIdsByPledgeCustomerIsAnother(SearchBean searchBean){
        Criteria criteria = new Criteria();
        criteria.and("dataStatus").is(DataStatus.SAVE);
        criteria.and("relation").gt("0");
        criteria.and("pledgeCustomerId").in(this.getCustomerIdsBySearchBean(searchBean,2));
        return (List<String>) this.getAllFieldList(new Query().addCriteria(criteria), "so_customerdemand", "customerTransactionId");
    }

    //  查询指标人是本人的交易IDS
    public List<String> getTransactionIdsByPledgeCustomerIsSelf(SearchBean searchBean){
        Criteria criteria = new Criteria();
        criteria.and("dataStatus").is(DataStatus.SAVE);
        criteria.and("relation").is("0");
        criteria.and("customerId").in(this.getCustomerIdsBySearchBean(searchBean,2));
        return (List<String>) this.getAllFieldList(new Query().addCriteria(criteria), "so_customerdemand", "customerTransactionId");
    }

    /**
     * 根据查询条件中的借款信息 查询出资质审查符合条件的customerTransactionIds
     * @param searchBean
     * @return
     */
    public List<String> getTransactionIdsByDemandCustomerLoanId(SearchBean searchBean){
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        //  资质审查所有的 customerLoanIds
        List<String> demandCustomerLoanIds = (List<String>) this.getAllFieldList(query, "so_customerdemand", "customerLoanId");
        //  资质审查所有的 customerLoanIds  与  符合查询条件的 所有的 customerLoanIds 的 交集
        List<String> intersection = Collections3.intersection(demandCustomerLoanIds, this.getCustomerLoanIdsBySearchBean(searchBean));
        query.addCriteria(Criteria.where("customerLoanId").in(intersection));
        return (List<String>) this.getAllFieldList(query, "so_customerdemand", "customerTransactionId");
    }

    /**
     * 根据查询条件中的借款信息 查询出客户签约符合条件的customerTransactionIds
     * @param searchBean
     * @return
     */
    public List<String> getTransactionIdsByOrderCustomerLoanId(SearchBean searchBean){
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        //  客户签约所有的 customerLoanIds
        List<String> orderCustomerLoanIds = (List<String>) this.getAllFieldList(query, "so_purchasecar", "customerLoanId");
        //  客户签约所有的 customerLoanIds  与  符合查询条件的 所有的 customerLoanIds 的 交集
        List<String> intersection = Collections3.intersection(orderCustomerLoanIds, this.getCustomerLoanIdsBySearchBean(searchBean));
        query.addCriteria(Criteria.where("customerLoanId").in(intersection));
        return (List<String>) this.getAllFieldList(query, "so_purchasecar", "customerTransactionId");
    }

    //  通过查询条件中的借款信息,查询符合条件的 customerLoanIds
    public List<String> getCustomerLoanIdsBySearchBean(SearchBean searchBean){
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if (!StringUtils.isEmpty(searchBean.getStartLimitAmount()) && !StringUtils.isEmpty(searchBean.getEndLimitAmount())){
            query.addCriteria(Criteria.where("creditAmount").gte(searchBean.getStartLimitAmount()).lte(searchBean.getEndLimitAmount()));
        } else if (!StringUtils.isEmpty(searchBean.getStartLimitAmount()) && StringUtils.isEmpty(searchBean.getEndLimitAmount())) {
            query.addCriteria(Criteria.where("creditAmount").gte(searchBean.getStartLimitAmount()));
        } else if (StringUtils.isEmpty(searchBean.getStartLimitAmount()) && !StringUtils.isEmpty(searchBean.getEndLimitAmount())) {
            query.addCriteria(Criteria.where("creditAmount").lte(searchBean.getEndLimitAmount()));
        }
        if (!StringUtils.isEmpty(searchBean.getCreditMonths()) && searchBean.getCreditMonths() != 0){
            query.addCriteria(Criteria.where("rateType.months").is(searchBean.getCreditMonths()));
        }
        //  只查询贴息，不查商贷
        if (searchBean.getCompensatory() && !searchBean.getBusiness()){
            query.addCriteria(Criteria.where("compensatoryInterest").is(1));
        } else if (!searchBean.getCompensatory() && searchBean.getBusiness()){//  不查询贴息，只查商贷
            query.addCriteria(Criteria.where("compensatoryInterest").is(0));
        }
        return (List<String> ) this.getAllFieldList(query, "cus_loan", "_id");
    }

    //  通过查询条件中的借款信息,查询出符合条件的 customerTransactionIds
    public List<String> getTransactionIdsBySearchBean(SearchBean searchBean){
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if (!StringUtils.isEmpty(searchBean.getStartLimitAmount())) {
            query.addCriteria(Criteria.where("creditAmount").gte(searchBean.getStartLimitAmount()));
        }
        if (!StringUtils.isEmpty(searchBean.getEndLimitAmount())) {
            query.addCriteria(Criteria.where("creditAmount").lte(searchBean.getEndLimitAmount()));
        }
        if (!StringUtils.isEmpty(searchBean.getCreditMonths()) && searchBean.getCreditMonths() != 0){
            query.addCriteria(Criteria.where("rateType.months").is(searchBean.getCreditMonths()));
        }
        //  只查询贴息，不查商贷
        if (searchBean.getCompensatory() && !searchBean.getBusiness()){
            query.addCriteria(Criteria.where("compensatoryInterest").is(1));
        } else if (!searchBean.getCompensatory() && searchBean.getBusiness()){//  不查询贴息，只查商贷
            query.addCriteria(Criteria.where("compensatoryInterest").is(0));
        }
        List<String> list = (List<String>) this.getAllFieldList(query, "cus_loan", "customerTransactionId");
        return Collections3.stringListDistinct(list);
    }

    /**
     *店刷
     */
    public List<String> getTransactionIdsBySwipingCardTime(SearchBean searchBean){
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        query.addCriteria(Criteria.where("swipingShopTime").regex(String.format(searchBean.getSwipingCardTime(),"m")));
        return (List<String>) this.getAllFieldList(query, "so_bankcard_apply", "customerTransactionId");
    }

    /**
     *银行刷
     */
    public List<String> getTransactionIdsBySwipingTrusteeTime(SearchBean searchBean){
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        query.addCriteria(Criteria.where("swipingTrusteeTime").regex(String.format(searchBean.getSwipingCardTime(),"m")));
        return (List<String>) this.getAllFieldList(query, "so_bankcard_apply", "customerTransactionId");
    }

}
