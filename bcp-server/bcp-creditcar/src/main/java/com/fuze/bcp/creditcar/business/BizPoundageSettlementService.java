package com.fuze.bcp.creditcar.business;

import com.fuze.bcp.api.bd.bean.BankSettlementStandardBean;
import com.fuze.bcp.api.bd.bean.CashSourceBean;
import com.fuze.bcp.api.bd.service.IBankSettlementStandardBizService;
import com.fuze.bcp.api.bd.service.ICashSourceBizService;
import com.fuze.bcp.api.cardealer.bean.CarDealerBean;
import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.transaction.bean.CustomerTransactionBean;
import com.fuze.bcp.api.creditcar.bean.PoundageSettlementBean;
import com.fuze.bcp.api.creditcar.bean.bankcardapply.BankCardApplyBean;
import com.fuze.bcp.api.creditcar.service.IPoundageSettlementBizService;
import com.fuze.bcp.api.customer.bean.CustomerBean;
import com.fuze.bcp.api.customer.bean.CustomerLoanBean;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.api.transaction.service.ICustomerTransactionBizService;
import com.fuze.bcp.bean.ApproveStatus;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.creditcar.domain.*;
import com.fuze.bcp.creditcar.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.OrderAssignUtil;
import com.fuze.bcp.utils.SimpleUtils;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;


/**
 * Created by zqw on 2017/8/29.
 */
@Service
public class BizPoundageSettlementService implements IPoundageSettlementBizService{

    private static final Logger logger = LoggerFactory.getLogger(BizPoundageSettlementService.class);

    @Autowired
    private IPoundageSettlementService iPoundageSettlementService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IBankSettlementStandardBizService iBankSettlementStandardBizService;

    @Autowired
    private ICarDealerBizService iCarDealerBizService;

    @Autowired
    private ICashSourceBizService iCashSourceBizService;

    @Autowired
    private ICustomerBizService iCustomerBizService;

    @Autowired
    private ICustomerTransactionBizService iCustomerTransactionBizService;

    @Autowired
    private ICancelOrderService iCancelOrderService;

    @Autowired
    private IBankCardApplyService iBankCardApplyService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MappingService mappingService;

    @Autowired
    private MessageService messageService;

    @Override
    public ResultBean<PoundageSettlementBean> actGetPoundageSettlements(Integer currentPage) {
        Page<PoundageSettlement> poundageSettlements = iPoundageSettlementService.getAllByOrderByOrderTimeDesc(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(poundageSettlements,PoundageSettlementBean.class));
    }

    @Override
    public ResultBean<PoundageSettlementBean> actSearchPoundageSettlements(int currentPage, String userName, String startTime, String endTime, PoundageSettlementBean poundageSettlementBean) {

        PoundageSettlement poundageSettlement = mappingService.map(poundageSettlementBean, PoundageSettlement.class);
        Page<PoundageSettlement> poundageSettlementPage ;
        if (!"".equals(userName)){
            //查询Customer,
            List<String> customerIds = new ArrayList<String>();
            CustomerBean customerBean = new CustomerBean();
            customerBean.setName(userName);
            List<CustomerBean> customers = iCustomerBizService.actSearchCustomer(customerBean).getD();
            for(CustomerBean c: customers) {
                customerIds.add(c.getId());
            }
            poundageSettlementPage = iPoundageSettlementService.findAllByCustomerIdsAndStartTimeAndEndTimeAndPoundageSettlement(customerIds,startTime,endTime,poundageSettlement,currentPage);
        } else {
            poundageSettlementPage = iPoundageSettlementService.findAllByCustomerIdsAndStartTimeAndEndTimeAndPoundageSettlement(new ArrayList<String>(),startTime,endTime,poundageSettlement,currentPage);
        }

        return ResultBean.getSucceed().setD(mappingService.map(poundageSettlementPage,PoundageSettlementBean.class));
    }

    @Override
    public ResultBean<PoundageSettlementBean> actGetOne(String id) {
        PoundageSettlement poundageSettlement = iPoundageSettlementService.getOne(id);
        if ( poundageSettlement != null ){
            return ResultBean.getSucceed().setD(mappingService.map(poundageSettlement,PoundageSettlementBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PoundageSettlementBean> actGetOneByCustomerTransactionId(String customerTransactionId) {
        PoundageSettlement poundageSettlement = iPoundageSettlementService.getOneByCustomerTransactionId(customerTransactionId);
        if ( poundageSettlement != null ){
            return ResultBean.getSucceed().setD(mappingService.map(poundageSettlement,PoundageSettlementBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<PoundageSettlementBean> actSavePoundageSettlement(PoundageSettlementBean poundageSettlementBean) {
        PoundageSettlement poundageSettlement = mappingService.map(poundageSettlementBean, PoundageSettlement.class);
        poundageSettlement = iPoundageSettlementService.save(poundageSettlement);
        return ResultBean.getSucceed().setD(mappingService.map(poundageSettlement,PoundageSettlementBean.class));
    }

    /**
     *          获取  营销代码
     * @param customerTransactionId
     * @return
     */
    @Override
    public ResultBean<PoundageSettlementBean> actGetMarketingCodeByCustomerTransactionId(String customerTransactionId) {

        CustomerTransactionBean customerTransactionBean = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(customerTransactionId).getD();
        if ( customerTransactionBean == null ){
            return ResultBean.getFailed().setM("客户交易不存在！");
        }
        PoundageSettlement poundageSettlement = iPoundageSettlementService.getOneByCustomerTransactionId(customerTransactionId, DataStatus.SAVE);
        // 判断  该笔交易的手续费分成是否存在
        if ( poundageSettlement == null ){
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(customerTransactionId);
            if (purchaseCarOrder != null){
                if (customerTransactionBean.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED || customerTransactionBean.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING ){
                    return ResultBean.getFailed().setM("该交易已经取消或正在取消中！");
                }
                //针对历史订单未分成的数据进行计算
                ResultBean<PoundageSettlementBean>  resp = actCalculateFeeSharing(purchaseCarOrder.getId());
                if(resp.failed()){
                    return ResultBean.getFailed().setM(resp.getM());
                }
                poundageSettlement = mappingService.map(resp.getD(),PoundageSettlement.class);
            }else{
                return ResultBean.getFailed().setM(String.format("该客户签约单不存在！"));
            }
        }

        return ResultBean.getSucceed().setD(mappingService.map(poundageSettlement,PoundageSettlementBean.class));
    }

    /**
     * 计算分润数据
     * @param orderId
     * @return
     */
    @Override
    public ResultBean<PoundageSettlementBean> actCalculateFeeSharing(String orderId) {

        String msg = null;

        PurchaseCarOrder purchaseCarOrder = iOrderService.getOne(orderId);
        String customerTransactionId = purchaseCarOrder.getCustomerTransactionId();
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(customerTransactionId).getD();
        if (customerTransaction == null){
            logger.error(messageService.getMessage("MSG_POUNDAGESETTLEMENT_CUSTOMERTRANSACTIONNULL"));
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_POUNDAGESETTLEMENT_CUSTOMERTRANSACTIONNULL"));
        }
        if (customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING){
            logger.error(messageService.getMessage("MSG_POUNDAGESETTLEMENT_TRANSACTION_IS_CANCELLED"));
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_POUNDAGESETTLEMENT_TRANSACTION_IS_CANCELLED"));
        }
        //  客户ID
        String customerId = purchaseCarOrder.getCustomerId();

        //  获取该客户的借款信息
        CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(purchaseCarOrder.getCustomerLoanId()).getD();
        if (customerLoanBean == null){
            logger.error(messageService.getMessage("MSG_POUNDAGESETTLEMENT_CUSROMERLONNULL"));
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_POUNDAGESETTLEMENT_CUSROMERLONNULL"));
        }

        //      贷款金额
        Double limitAmount = customerLoanBean.getCreditAmount();
        //      贷款期数
        Integer creditMonths = customerLoanBean.getRateType().getMonths();
        //  订单的手续费
        Double charge = customerLoanBean.getBankFeeAmount();
        //   订单的手续费缴纳方式  :  SSS         默认为：趸交
        String chargePaymentWay = customerLoanBean.getChargePaymentWay() == null ? "WHOLE":customerLoanBean.getChargePaymentWay();
        //      该订单的经销商       目前  单据的  经销商ID 已经不保存，需要从其他地方获取
        String carDealerId = customerTransaction.getCarDealerId();
        CarDealerBean carDealer = iCarDealerBizService.actGetOneCarDealer(carDealerId).getD();
        if (carDealer == null){
            logger.error(messageService.getMessage("MSG_ERROR_NOCARDEALER"));
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_ERROR_NOCARDEALER"));
        }
        //  订单的渠道行      A
        String cooperationCashSourceId = carDealer.getCooperationCashSourceId();
        //  订单的报单行      B
        String cashSourceId = carDealer.getCashSourceId();

        //  判断  该订单的手续费分成 是否 已经存在，存在就修改，不存在就新增
        PoundageSettlement poundageSettlement = iPoundageSettlementService.getOneByCustomerTransactionId(customerTransactionId,DataStatus.SAVE);
        if (poundageSettlement == null){
            poundageSettlement = new PoundageSettlement();
            poundageSettlement.setCustomerTransactionId(customerTransactionId);    //客户交易ID
            poundageSettlement.setCustomerId(customerId);//客户ID
            poundageSettlement.setLimitAmount(limitAmount);//贷款金额
            poundageSettlement.setCreditMonths(creditMonths);//贷款期数
            poundageSettlement.setPoundage(charge);//手续费
            poundageSettlement.setChargePaymentWayCode(chargePaymentWay);//手续费缴纳方式
            poundageSettlement.setChannelCashSourceId(cooperationCashSourceId);//  保存  渠道行
            poundageSettlement.setDeclarationCashSourceId(cashSourceId);//  保存 报单行
            poundageSettlement.setOrderTime(purchaseCarOrder.getTs());//订单生成的时间
        }else{
            //修改的情况下尽量不要修改已经有的数据
            poundageSettlement.setChargePaymentWayCode(chargePaymentWay);//手续费缴纳方式
            if(poundageSettlement.getChannelCashSourceId() == null){
                poundageSettlement.setChannelCashSourceId(cooperationCashSourceId);//  保存  渠道行
            }
            if(poundageSettlement.getDeclarationCashSourceId() == null){
                poundageSettlement.setDeclarationCashSourceId(cashSourceId);
            }
            if(poundageSettlement.getCreditMonths() == null){
                poundageSettlement.setLimitAmount(limitAmount);//贷款金额
                poundageSettlement.setCreditMonths(creditMonths);//贷款期数
                poundageSettlement.setPoundage(charge);//手续费
            }
        }

        //      判断 该订单的  渠道行  计算行  是否为  同一个支行
        String assignCashSourceId = ""; //  该订单 的 代码行（结算行）
        if ( cooperationCashSourceId == null ){
            msg = String.format("该订单的渠道【%s】中渠道行为空！",carDealer.getName());
            logger.error(String.format(messageService.getMessage("MSG_POUNDAGESETTLEMENT_COOPERATIONCASHSOURCEIDNUll"),carDealer.getName()));
            return saveFailed(poundageSettlement,msg);
        }
        if ( cashSourceId== null){
            msg = String.format("该订单的渠道【%s】中保单行为空！",carDealer.getName());
            return  saveFailed(poundageSettlement,msg);
        }
        if (cooperationCashSourceId.equals(cashSourceId)){
            assignCashSourceId = cooperationCashSourceId;
        } else {
            //      获取手续费分成标准表中，渠道行：A   报单行：B   的手续费分成标准数据
            BankSettlementStandardBean bankSettlementStandard = iBankSettlementStandardBizService.actFindBankSettlementStandardByChannelIdAndDeclarationId(cooperationCashSourceId, cashSourceId).getD();
            if (bankSettlementStandard == null){
                msg = String.format(messageService.getMessage("MSG_POUNDAGESETTLEMENT_BANKSETTLEMENTSTANDARD"),carDealer.getName());
                return saveFailed(poundageSettlement,msg);
            }

            //  查询分成表中, 某手续费缴纳方式下的代码行（结算行）为 A 和 B 所有的手续费总额
            Double totalA = getAllBySettlementCashSourceIdAndChargePaymentWayCode(chargePaymentWay, cashSourceId, cooperationCashSourceId, cooperationCashSourceId).getD();
            Double totalB = getAllBySettlementCashSourceIdAndChargePaymentWayCode(chargePaymentWay, cashSourceId, cooperationCashSourceId, cashSourceId).getD();

            //      调用  分成计算工具类     计算出     该订单分配给那个支行
            assignCashSourceId = OrderAssignUtil.assign(charge, totalA, totalB, bankSettlementStandard.getChannelProportion(), bankSettlementStandard.getDeclarationProportion()) == "A" ? cooperationCashSourceId : cashSourceId;
        }

        if(poundageSettlement.getSettlementCashSourceId() == null){
            //修改的情况下不要调整代码行数据
            poundageSettlement.setSettlementCashSourceId(assignCashSourceId);
        }
        if (poundageSettlement.getMarketingCode() == null){
            //      获取  代码行的  营销代码
            CashSourceBean cashSourceBean = iCashSourceBizService.actGetCashSource(assignCashSourceId).getD();
            if (cashSourceBean != null && cashSourceBean.getMarketingCode() != null && cashSourceBean.getMarketingCode().size() > 0){
                poundageSettlement.setMarketingCode(cashSourceBean.getMarketingCode().get(0));
            } else {
                logger.error(String.format(messageService.getMessage("MSG_CASHSOURCE_MARKETINGCODE_NULL"),cashSourceBean.getName()));
            }
        }

        poundageSettlement.setTs(DateTimeUtils.getCreateTime());//订单生成的时间
        poundageSettlement.setDataStatus(DataStatus.SAVE);
        poundageSettlement.setEffectStatus(PoundageSettlementBean.EFFECT_NONE);//  保存  生效状态
        poundageSettlement.setStatus(PoundageSettlementBean.STATUS_NORMAL);//保存业务状态
        poundageSettlement.setComment(msg);

        //  保存  手续费结算记录
        return ResultBean.getSucceed().setD(mappingService.map(iPoundageSettlementService.save(poundageSettlement),PoundageSettlementBean.class));
    }

    public ResultBean<PoundageSettlementBean> saveFailed(PoundageSettlement ps,String errorInfo){
        ps.setStatus(PoundageSettlementBean.STATUS_ERROR);
        ps.setComment(errorInfo);
        ps.setEffectStatus(PoundageSettlementBean.EFFECT_NONE);
        ps = iPoundageSettlementService.save(ps);
        return ResultBean.getSucceed().setD(mappingService.map(ps,PoundageSettlementBean.class));
    }

    /**
     *      计算     代码行（结算行） 在    该手续费缴纳方式下的  手续费总额       数据状态 只能为 保存 状态
     * @param settlementCashSourceId    代码行（结算行）ID
     * @param chargePaymentWayCode      手续费缴纳方式
     * @return
     */
    public ResultBean<Double> getAllBySettlementCashSourceIdAndChargePaymentWayCode(String chargePaymentWayCode, String declarationCashSourceId, String channelCashSourceId, String settlementCashSourceId) {
        Aggregation a =
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("chargePaymentWayCode").is(chargePaymentWayCode)),
                        Aggregation.match(Criteria.where("declarationCashSourceId").is(declarationCashSourceId)),
                        Aggregation.match(Criteria.where("channelCashSourceId").is(channelCashSourceId)),
                        Aggregation.match(Criteria.where("settlementCashSourceId").is(settlementCashSourceId)),
                        Aggregation.match(Criteria.where("dataStatus").is(DataStatus.SAVE)),
                        Aggregation.group("chargePaymentWayCode", "declarationCashSourceId","channelCashSourceId","settlementCashSourceId")
                                .sum("poundage").as("poundage")
                );
        //TODO 方法重构，不直接调用mongoTemplate
        AggregationResults<PoundageSettlement> results = mongoTemplate.aggregate(a, "so_poundagesettlement", PoundageSettlement.class);
        List<PoundageSettlement> tagCount = results.getMappedResults();
        return ResultBean.getSucceed().setD(tagCount.size() > 0 ? tagCount.get(0).getPoundage() : 0.0);
    }

    //  计算报单行的每日手续费分成
    public ResultBean<List<PoundageSettlementBean>>  actCalculateDailyDeclaration(PoundageSettlementBean ps){
        //左匹配
        Pattern pattern = Pattern.compile("^"+ps.getOrderTime()+".*$", Pattern.CASE_INSENSITIVE);
        Aggregation a = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("declarationCashSourceId").is(ps.getDeclarationCashSourceId())),
                Aggregation.match(Criteria.where("channelCashSourceId").is(ps.getChannelCashSourceId())),
                Aggregation.match(Criteria.where("settlementCashSourceId").is(ps.getDeclarationCashSourceId())),
                Aggregation.match(Criteria.where("dataStatus").is(DataStatus.SAVE)),
                Aggregation.match(Criteria.where("orderTime").regex(pattern)),
                Aggregation.sort(Sort.Direction.DESC, "orderTime"),
                Aggregation.sort(Sort.Direction.DESC, "chargePaymentWayCode")
        );
        AggregationResults<PoundageSettlement> results = mongoTemplate.aggregate(a, "so_poundagesettlement", PoundageSettlement.class);
        List<PoundageSettlement> result = results.getMappedResults();
        return ResultBean.getSucceed().setD(result);
    }

    //  计算渠道行的每日手续费分成
    public ResultBean<List<PoundageSettlementBean>>  actCalculateDailyChannel(PoundageSettlementBean ps){
        //左匹配
        Pattern pattern = Pattern.compile("^"+ps.getOrderTime()+".*$", Pattern.CASE_INSENSITIVE);
        Aggregation a = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("declarationCashSourceId").is(ps.getDeclarationCashSourceId())),
                Aggregation.match(Criteria.where("channelCashSourceId").is(ps.getChannelCashSourceId())),
                Aggregation.match(Criteria.where("settlementCashSourceId").is(ps.getChannelCashSourceId())),
                Aggregation.match(Criteria.where("dataStatus").is(DataStatus.SAVE)),
                Aggregation.match(Criteria.where("orderTime").regex(pattern)),
                Aggregation.sort(Sort.Direction.DESC, "orderTime"),
                Aggregation.sort(Sort.Direction.DESC, "chargePaymentWayCode")
        );
        AggregationResults<PoundageSettlement> results = mongoTemplate.aggregate(a, "so_poundagesettlement", PoundageSettlement.class);
        List<PoundageSettlement> result = results.getMappedResults();
        return ResultBean.getSucceed().setD(result);
    }

    //  查询  截止某个日期累计发生的数据总和
    public ResultBean<List<Map<String,Object>>>  actGetSummation(PoundageSettlementBean ps){
        String fdate = SimpleUtils.getOffsetDaysTodayStrOf("yyyy-MM-dd",ps.getOrderTime(),1);
        //      根据 条件查询  出来 某天的手续费分成情况  并对数据进行分组合计          分期
        Aggregation a = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("declarationCashSourceId").is(ps.getDeclarationCashSourceId())),
                Aggregation.match(Criteria.where("channelCashSourceId").is(ps.getChannelCashSourceId())),
                Aggregation.match(Criteria.where("chargePaymentWayCode").is(ps.getChargePaymentWayCode())),
                Aggregation.match(Criteria.where("dataStatus").is(DataStatus.SAVE)),
                Aggregation.match(Criteria.where("orderTime").lte(fdate)),
                Aggregation.group("settlementCashSourceId")
                        .count().as("totalNum")
                        .sum("limitAmount").as("limitAmountTotal")
                        .sum("poundage").as("poundageTotal"),
                Aggregation.sort(PoundageSettlement.getSortASC("_id"))
        );

        AggregationResults<BasicDBObject> wholeResults = mongoTemplate.aggregate(a, "so_poundagesettlement", BasicDBObject.class);
        List<BasicDBObject> result = wholeResults.getMappedResults();
        String[] fields ={"poundageTotal"};
        String[] ratioFields = {"ratio"};
        return ResultBean.getSucceed().setD(calRatioByTwoFields(fillResult(mappingService.map(ps,PoundageSettlement.class),result),fields,ratioFields));
    }
    /**
     * 计算指定列的行值占据的比率
     * @param result
     * @param fields
     * @param ratioFieldNames
     * @return
     */
    private List<Map<String,Object>> calRatioByTwoFields(List<Map<String,Object>> result, String[] fields, String[] ratioFieldNames){
        Map<String,Object> totalMap = new HashMap<String, Object>();
        for(int i=0;i<result.size(); i++){
            for(int j=0; j<fields.length; j++){
                Double total = (Double) totalMap.get(fields[j]+"_total");
                if(total == null){
                    total = 0.0;
                }
                total = total +  (result.get(i).get(fields[j])==null?0.0:(Double)result.get(i).get(fields[j]));
                totalMap.put(fields[j]+"_total",total);
            }
        }
        for(int i=0;i<result.size(); i++){
            for(int j=0; j<fields.length; j++){
                Double total = (Double) totalMap.get(fields[j]+"_total");
                if(total == null){
                    total = 0.0;
                }
                Double ratio = 0.0;
                Double val = (result.get(i).get(fields[j])==null?0.0:(Double)result.get(i).get(fields[j]));
                if(total>0.0 && val>0.0){
                    ratio = val/total;
                }
                result.get(i).put(ratioFieldNames[j],ratio);
            }
        }
        return result;
    }

    /**
     * 根据缴纳方式计算每个支行的分润比例
     * @param ps
     * @param results
     * @return
     */
    private List<Map<String,Object>>  fillResult(PoundageSettlement ps,List<BasicDBObject> results ){

        List<Map<String,Object>>  listResult = new ArrayList<Map<String, Object>>();
        for(int i=0; i<results.size(); i++){
            Iterator<String> keys = results.get(i).keySet().iterator();
            Map<String,Object> hm = new HashMap<String, Object>();
            while (keys.hasNext()){
                String key = keys.next();
                hm.put(key,results.get(i).get(key));
            }
            listResult.add(hm);
        }
        if(ps.getChannelCashSourceId().equals(ps.getDeclarationCashSourceId())){
            //渠道行和报单行一致，只需要存在一条数据
            if(listResult.size() ==0){
                listResult.add(createZeroBasicDBObject(ps.getChannelCashSourceId()));
            }
            //补充两条数据
        }else{
            //渠道行和结算行不一致的情况下，需要确定存在两条记录
            if(listResult.size()==1){
                //补充一条数据
                String settleBankId =  (String) listResult.get(0).get("_id");
                if(ps.getChannelCashSourceId().equals(settleBankId)){
                    listResult.add(createZeroBasicDBObject(ps.getDeclarationCashSourceId()));
                }else{
                    listResult.add(createZeroBasicDBObject(ps.getChannelCashSourceId()));
                }
            }else if(listResult.size()==0){
                listResult.add(createZeroBasicDBObject(ps.getDeclarationCashSourceId()));
                listResult.add(createZeroBasicDBObject(ps.getChannelCashSourceId()));
            }
        }
        return listResult;
    }

    /**
     * 创建一个为空的对象
     * @param settleBankId
     * @return
     */
    private BasicDBObject createZeroBasicDBObject(String settleBankId){
        BasicDBObject bd = new BasicDBObject();
        bd.put("_id",settleBankId);
        bd.put("totalNum",0.0);
        bd.put("limitAmountTotal",0.0);
        bd.put("poundageTotal",0.0);
        bd.put("ratio",0.0);
        return bd;
    }

    /**
     * 根据交易ID删除流水数据，流水数据作废
     *
     * @param customerTransactionId
     * @return
     */
    public ResultBean<PoundageSettlementBean> actDeleteOneByCustomerTransactionId(String customerTransactionId) {
        PoundageSettlement ps =  iPoundageSettlementService.getOneByCustomerTransactionId(customerTransactionId);
        if(ps != null){
            ps.setDataStatus(DataStatus.DISCARD);
            ps.setEffectStatus(PoundageSettlementBean.EFFECT_NONE);
            String msg = String.format(messageService.getMessage("MSG_POUNDAGESETTLEMENT_DELETE"),ps.getId());
            return ResultBean.getSucceed().setD(iPoundageSettlementService.save(ps)).setM(msg);
        }
        return ResultBean.getSucceed().setM(String.format(messageService.getMessage("MSG_POUNDAGESETTLEMENT_NOTFOUND_CUSTOMERTRANSACTIONID"),customerTransactionId));
    }

    /**
     * 根据订单ID，更新某个银行日流水表
     * @param orderId
     * @return
     */
    public ResultBean<PoundageSettlementBean>  syncPoundageSettlementByOrderId(String orderId){
        String msg = null;
        PurchaseCarOrder order = iOrderService.getOne(orderId);
        if(order == null){
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_ORDER_NULL_ID"),orderId));
        }
        String customerTransactionId = order.getCustomerTransactionId();
        CustomerTransactionBean customerTransaction = iCustomerTransactionBizService.actFindAvailableCustomerTransactionById(customerTransactionId).getD();
        if (customerTransaction == null){
            logger.error(messageService.getMessage("MSG_POUNDAGESETTLEMENT_CUSTOMERTRANSACTIONNULL"));
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_POUNDAGESETTLEMENT_CUSTOMERTRANSACTIONNULL"));
        }
        if(customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLED
                || customerTransaction.getStatus() == CustomerTransactionBean.TRANSACTION_CANCELLING ){
            //交易取消或取消中，无效的订单，删除
            actDeleteOneByCustomerTransactionId(customerTransactionId);
            return ResultBean.getSucceed().setM(String.format(messageService.getMessage("MSG_POUNDAGESETTLEMENT_ORDER_CANCEL"),customerTransactionId));
        }
        if(order.getApproveStatus() == ApproveStatus.APPROVE_REJECT){
            //客户签约被拒绝，无效的订单，删除
            actDeleteOneByCustomerTransactionId(customerTransactionId);
            return ResultBean.getSucceed().setM(String.format(messageService.getMessage("MSG_POUNDAGESETTLEMENT_ORDER_REFUSE"),customerTransactionId));
        }
        PoundageSettlement ps =  iPoundageSettlementService.getOneByCustomerTransactionId(customerTransactionId);
        if(ps == null) {
            //为空，需要重新计算
            return this.actCalculateFeeSharing(order.getId());
        }else{
            String cashsourceId = ps.getSettlementCashSourceId();
            if(cashsourceId != null){
                //代码行实际为空
                CashSourceBean cashSource = iCashSourceBizService.actGetCashSource(cashsourceId).getD();
                if(cashSource == null){
                    //代码行不存在的情况下说明需要重新生成代码行。
                    return this.actCalculateFeeSharing(order.getId());
                }
            }else{
                //为空的情况下重新生成代码行
                return this.actCalculateFeeSharing(order.getId());
            }
            CustomerLoanBean customerLoanBean = iCustomerBizService.actGetCustomerLoanById(order.getCustomerLoanId()).getD();
            if (customerLoanBean == null){
                logger.error(messageService.getMessage("MSG_POUNDAGESETTLEMENT_CUSROMERLONNULL"));
                return ResultBean.getFailed().setM(messageService.getMessage("MSG_POUNDAGESETTLEMENT_CUSROMERLONNULL"));
            }
            ps.setChargePaymentWayCode(customerLoanBean.getChargePaymentWay());
            ps.setCreditMonths(customerLoanBean.getRateType().getMonths());
            ps.setCustomerId(order.getCustomerId());
            ps.setOrderTime(order.getTs());
            ps.setCustomerTransactionId(customerTransactionId);
            ps.setPoundage(customerLoanBean.getBankFeeAmount());
            ps.setDataStatus(DataStatus.SAVE);
            ps.setStatus(PoundageSettlementBean.STATUS_NORMAL);
            ps.setComment(null);
            ps.setOrderTime(order.getTs());
            //根据银行卡业务处理数据，如果是已经刷卡的状态，则进行状态更新
            BankCardApply bca = iBankCardApplyService.findByCustomerTransactionId(customerTransactionId);
            if(bca == null){
                ps.setEffectStatus(PoundageSettlementBean.EFFECT_NONE);
            }else{
                if(bca.getStatus() == BankCardApplyBean.BKSTATUS_SWIPING || bca.getStatus() == BankCardApplyBean.BKSTATUS_GET){
                    ps.setEffectStatus(PoundageSettlementBean.EFFECT_DOING);
                }else{
                    ps.setEffectStatus(PoundageSettlementBean.EFFECT_NONE);
                }
            }
            ps = iPoundageSettlementService.save(ps);
            msg = String.format("根据订单同步了支行分润表【%s】！",ps.getId());
        }
        return ResultBean.getSucceed().setD(mappingService.map(ps,PoundageSettlementBean.class)).setM(msg);
    }


    /**
     * 同步全部的订单数据到支行利润分配表
     * @return
     */
    public ResultBean<List<PoundageSettlement>>  syncAllOrder(){
        List<PoundageSettlement> results = new ArrayList<PoundageSettlement>();
        int pageIndex = 0;
        int pageCount = 20;
        Pageable page = new PageRequest(pageIndex,pageCount,PurchaseCarOrder.getTsSortASC());
        List<PurchaseCarOrder> orders = iOrderService.getAll(page).getContent();
        while(orders != null && orders.size()>0) {
            for (PurchaseCarOrder order : orders) {
                ResultBean<PoundageSettlementBean>  res = this.syncPoundageSettlementByOrderId(order.getId());
                if(res.failed()){
                    logger.error(res.getM());
                }else{
                    logger.info(res.getM());
                }
            }
            pageIndex++;
            page =  new PageRequest(pageIndex,pageCount,PurchaseCarOrder.getTsSortASC());
            orders = iOrderService.getAll(page).getContent();
        }
        return ResultBean.getSucceed().setD(results);
    }

    //  银行制卡业务，刷卡完成，使分润数据生效
    @Override
    public void actUpdateEffectStatusByBankCardApplyId(String bankCardApplyId) {
        BankCardApply bankCardApply = iBankCardApplyService.getOne(bankCardApplyId);
        if (bankCardApply == null){
            logger.error(String.format(messageService.getMessage("MSG_BANKCARDAPPLY_NOTEXIST"),bankCardApplyId));
        }
        this.actUpdateEffectStatusByCustomerTransactionId(bankCardApply.getCustomerTransactionId(), PoundageSettlementBean.EFFECT_DOING, DataStatus.SAVE);
    }

    //  取消业务通过之后，发送业务通知，作废分润数据
    @Override
    public void actDiscardOneByCancelOrderId(String cancelOrderId) {
        CancelOrder cancelOrder = iCancelOrderService.getOne(cancelOrderId);
        if (cancelOrder != null && cancelOrder.getApproveStatus() == ApproveStatus.APPROVE_PASSED){
            this.actUpdateEffectStatusByCustomerTransactionId(cancelOrder.getCustomerTransactionId(), PoundageSettlementBean.EFFECT_NONE, DataStatus.DISCARD);
        }
        logger.error(String.format(messageService.getMessage("MSG_CANCELORDER_NOTFOUND_ID"),cancelOrderId));
    }

    /**
     *      修改  手续费分成数据的状态(生效状态,数据状态)
     * @param customerTransactionId    交易ID
     * @param effectStatus  生效状态
     * @return
     */
    public ResultBean<PoundageSettlementBean> actUpdateEffectStatusByCustomerTransactionId(String customerTransactionId, Integer effectStatus, Integer dataStatus){
        PoundageSettlement poundageSettlement = iPoundageSettlementService.getOneByCustomerTransactionId(customerTransactionId);
        if (poundageSettlement != null){
            return ResultBean.getSucceed().setD(updateEffectStatus(poundageSettlement,effectStatus,dataStatus));
        } else {
            PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(customerTransactionId);
            if (purchaseCarOrder != null){
                PoundageSettlementBean poundageSettlementBean = this.actCalculateFeeSharing(purchaseCarOrder.getId()).getD();
                return ResultBean.getSucceed().setD(updateEffectStatus(mappingService.map(poundageSettlementBean,PoundageSettlement.class),effectStatus,dataStatus));
            } else {
                logger.info(String.format(messageService.getMessage("MSG_ORDER_NULL_CUSTOMERTRANSACTIONID"),customerTransactionId));
            }
        }
        logger.error(messageService.getMessage("MSG_POUNDAGESETTLEMENT_NULL_CUSTOMERTRANSACTIONID"),customerTransactionId);
        return ResultBean.getFailed();
    }

    public PoundageSettlement updateEffectStatus(PoundageSettlement poundageSettlement, Integer effectStatus, Integer ds){
        poundageSettlement.setEffectStatus(effectStatus);
        poundageSettlement.setDataStatus(ds);
        return iPoundageSettlementService.save(poundageSettlement);
    }

    /**
     *      通过交易ID更新支行结算数据信息
     * @param transactionId
     * @return
     */
    public ResultBean<PoundageSettlementBean> actCalculateFeeSharingByTransactionId(String transactionId){
        PurchaseCarOrder purchaseCarOrder = iOrderService.findByCustomerTransactionId(transactionId);
        if (purchaseCarOrder != null){
            return this.actCalculateFeeSharing(purchaseCarOrder.getId());
        }
        return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_ORDER_NULL_CUSTOMERTRANSACTIONID"),transactionId));
    }
}