package com.fuze.bcp.customer.business;

import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.api.customer.bean.*;
import com.fuze.bcp.api.customer.service.ICustomerBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.customer.domain.*;
import com.fuze.bcp.customer.service.*;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.DateTimeUtils;
import com.fuze.bcp.utils.IdcardUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by admin on 2017/5/24.
 */
@Service
public class BizCustomerService implements ICustomerBizService {

    @Autowired
    ICustomerService iCustomerService;

    @Autowired
    ICustomerCarService iCustomerCarService;

    @Autowired
    ICustomerCardService iCustomerCardService;

    @Autowired
    ICustomerLoanService iCustomerLoanService;

    @Autowired
    ICustomerExchangeCarService iCustomerExchangeCarService;

    @Autowired
    ICustomerExchangeLoanService iCustomerExchangeLoanService;

    @Autowired
    IBaseDataBizService iBaseDataBizService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    ICarTypeBizService iCarTypeBizService;

    @Autowired
    ICustomerRepaymentService iCustomerRepaymentService;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 分页列表
     *
     * @param currentPage
     * @return
     */
    @Override
    public ResultBean<CustomerBean> actGetCustomers(Integer currentPage) {
        Page<Customer> customers = iCustomerService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(customers, CustomerBean.class));
    }

    @Override
    public ResultBean<List<CustomerBean>> actGetAvaliableCustomer() {
        List<Customer> customers = iCustomerService.getAvaliableAll();
        return ResultBean.getSucceed().setD(mappingService.map(customers, CustomerBean.class));
    }


    public ResultBean<CustomerBean> actGetCustomersOrderByTs(Integer currentPage) {
        Page<Customer> customers = iCustomerService.getAllOrderByTs(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(customers, CustomerBean.class));
    }

    //获取客户信息
    @Override
    public ResultBean<CustomerBean> actGetCustomerById(String id) {
        Customer customer = iCustomerService.getOne(id);
        if (customer == null) {
            return ResultBean.getFailed();
        }
        return ResultBean.getSucceed().setD(mappingService.map(customer, CustomerBean.class));
    }

    @Override
    public ResultBean<CustomerBean> actSaveCustomer(CustomerBean customerBean) {
        Customer customer = mappingService.map(customerBean, Customer.class);
        customer = iCustomerService.save(customer);
        return ResultBean.getSucceed().setD(mappingService.map(customer, CustomerBean.class));
    }

    @Override
    public ResultBean<CustomerBean> actSaveCustomerSalary(CustomerBean customerBean) {
        Customer customer = iCustomerService.getOne(customerBean.getId());
        if (customer != null) {
            customer.getCustomerJob().setSalary(customerBean.getCustomerJob().getSalary());
            customer = iCustomerService.save(customer);
            return ResultBean.getSucceed().setD(mappingService.map(customer, CustomerBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerBean> actSubmitCustomer(CustomerBean customerBean) {
        //    根据客户身份证号 解析信息
        customerBean = actParseCustomerIdentifyNo(customerBean).getD();

        //通过身份证号查询
        Customer customer = iCustomerService.getByIdentifyNo(customerBean.getIdentifyNo());
        if (customer != null) { //客户已存在，只更新客户信息
            //更新客户信息
            copyFields(customerBean, customer);
        }
        customer = iCustomerService.save(mappingService.map(customerBean, Customer.class));
        if (customer != null && customer.getId() != null) {
            //保存客户信息并返回CustomerBean
            return ResultBean.getSucceed().setD(mappingService.map(customer, CustomerBean.class));
        } else {
            return ResultBean.getFailed().setM("保存失败！");
        }
    }

    private void copyFields(CustomerBean customerBean, Customer customer) {
        Field fields[] = customerBean.getClass().getDeclaredFields();//Bean的fields
        List<String> ignoreProperties = new ArrayList<String>();
        try {
            Field.setAccessible(fields, true);
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                Object value = fields[i].get(customerBean);
                if (value != null) {
                    ignoreProperties.add(name);
                }
            }

            String[] strings = new String[ignoreProperties.size()];
            ignoreProperties.toArray(strings);

            BeanUtils.copyProperties(customer, customerBean, strings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getValueFromFields(Field[] fields, Customer customer, String name) {
        Object value = null;
        try {
            Field.setAccessible(fields, true);
            for (Field field : fields) {
                if (name.equals(field.getName())) {
                    value = field.get(customer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    /**
     * 解析 客户信息中的 客户身份证号  获取客户年龄、出生日期、户籍所在地
     *
     * @param customerBean
     * @return
     */
    public ResultBean<CustomerBean> actParseCustomerIdentifyNo(CustomerBean customerBean) {
        String identifyNo = customerBean.getIdentifyNo();
        if (IdcardUtils.validateCard(identifyNo)) {
            customerBean.setAge(IdcardUtils.getAgeByIdCard(identifyNo));
            //      将 yyyyMMdd 格式 String类型的时间字符串 转换为    yyyy-MM-dd 格式的字符串
            customerBean.setBirthday(DateTimeUtils.StringPattern(IdcardUtils.getBirthByIdCard(identifyNo), "yyyyMMdd", "yyyy-MM-dd"));
            customerBean.setCensusRegisterCity(IdcardUtils.getProvinceByIdCard(identifyNo));
            //  解析 性别
            //customerBean.setGender("N".equals(IdcardUtils.getGenderByIdCard(identifyNo)) ? -1 : ("M".equals(IdcardUtils.getGenderByIdCard(identifyNo)) ? 0 : 1) );
        }
        return ResultBean.getSucceed().setD(customerBean);
    }


    @Override
    public ResultBean<CustomerBean> actDeleteCustomer(String id) {
        Customer customer = iCustomerService.getOne(id);
        if (customer != null) {
            customer = iCustomerService.delete(id);
            return ResultBean.getSucceed().setD(mappingService.map(customer, CustomerBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<CustomerBean>> actSearchCustomer(CustomerBean customerBean) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if (!StringUtils.isEmpty(customerBean.getName()))
            query.addCriteria(Criteria.where("name").regex(Pattern.compile("^.*"+ customerBean.getName() +".*$", Pattern.CASE_INSENSITIVE)));
        if (!StringUtils.isEmpty(customerBean.getIdentifyNo()))
            query.addCriteria(Criteria.where("identifyNo").regex(Pattern.compile("^.*"+ customerBean.getIdentifyNo() +".*$", Pattern.CASE_INSENSITIVE)));
        if (!StringUtils.isEmpty(customerBean.getCells()) && customerBean.getCells().size() > 0)
            query.addCriteria(Criteria.where("cells").in(customerBean.getCells().get(0)));
        List<Customer> customers = mongoTemplate.find(query,Customer.class);
        return ResultBean.getSucceed().setD(mappingService.map(customers, CustomerBean.class));
    }

    @Override
    public ResultBean<DataPageBean<CustomerBean>> searchCustomer(Integer currentPage, CustomerBean customerBean) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE));
        if (!StringUtils.isEmpty(customerBean.getName()))
            query.addCriteria(Criteria.where("name").regex(Pattern.compile("^.*"+ customerBean.getName() +".*$", Pattern.CASE_INSENSITIVE)));
        if (!StringUtils.isEmpty(customerBean.getIdentifyNo()))
            query.addCriteria(Criteria.where("identifyNo").regex(Pattern.compile("^.*"+ customerBean.getIdentifyNo() +".*$", Pattern.CASE_INSENSITIVE)));
        if (!StringUtils.isEmpty(customerBean.getCells()) && customerBean.getCells().size() > 0)
            query.addCriteria(Criteria.where("cells").in(customerBean.getCells().get(0)));
        Pageable pageable = new PageRequest(currentPage,20);
        query.with(pageable);
        List<Customer> customers = mongoTemplate.find(query,Customer.class);
        Page<Customer> pageAll = new PageImpl(customers,pageable, mongoTemplate.count(query,Customer.class));
        return ResultBean.getSucceed().setD(mappingService.map(pageAll, CustomerBean.class));
    }

    @Override
    public ResultBean<List<CustomerBean>> actGetCustomerQuery(String loginUserId, String inputStr) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("name").regex(".*?\\" + inputStr + ".*"), Criteria.where("identifyNo").regex(".*?\\" + inputStr + ".*"));
        criteria.and("loginUserId").is(loginUserId);
        List<Customer> customers = mongoTemplate.find(new Query(criteria), Customer.class);
        return ResultBean.getSucceed().setD(mappingService.map(customers, CustomerBean.class));
    }

    @Override
    public ResultBean<CustomerBean> actGetCustomerByIdentifyNo(String identifyNo) {
        Customer customer = iCustomerService.getByIdentifyNo(identifyNo);
        if (customer != null) {
            return ResultBean.getSucceed().setD(mappingService.map(customer, CustomerBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerCarBean> actSaveCustomerCar(CustomerCarBean customerCarBean) {
        CustomerCar car = mappingService.map(customerCarBean, CustomerCar.class);
        car = iCustomerCarService.save(car);

        return ResultBean.getSucceed().setD(mappingService.map(car, CustomerCarBean.class));
    }


    /**
     * 根据 transactionId 获取 CustomerCar
     *
     * @param transactionId
     * @return
     */
    @Override
    public ResultBean<CustomerCarBean> actGetCustomerCarByTransactionId(String transactionId) {
        CustomerCar car = iCustomerCarService.findByCustomerTransactionId(transactionId);
        if (car != null) {
            return ResultBean.getSucceed().setD(mappingService.map(car, CustomerCarBean.class));
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }

    }

    @Override
    public ResultBean<List<CustomerCarBean>> actGetCustomerCarsByTransactionId(String transactionId) {
        List<CustomerCar> cars = iCustomerCarService.findAllByCustomerTransactionId(transactionId);
        if (cars != null) {
            return ResultBean.getSucceed().setD(mappingService.map(cars, CustomerCarBean.class));
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
    }

    @Override
    public ResultBean<CustomerLoanBean> actSaveCustomerLoan(CustomerLoanBean customerLoanBean) {
        CustomerLoan loan = mappingService.map(customerLoanBean, CustomerLoan.class);
        loan = iCustomerLoanService.save(loan);
        if(loan.getApprovedCreditAmount() != null){
            List<CustomerExchangeLoan> exchangeLoanList = iCustomerExchangeLoanService.getByCustomerTransactionId(loan.getCustomerTransactionId());
            for (CustomerExchangeLoan exLoan:exchangeLoanList) {
                exLoan.setApprovedCreditAmount(loan.getApprovedCreditAmount());
                iCustomerExchangeLoanService.save(exLoan);
            }
        }

        return ResultBean.getSucceed().setD(mappingService.map(loan, CustomerLoanBean.class));
    }


    /**
     * 根据 transactionId 获取 CustomerLoan
     *
     * @param transactionId
     * @return
     */
    @Override
    public ResultBean<CustomerLoanBean> actGetCustomerLoanByTransactionId(String transactionId) {
        CustomerLoan loan = iCustomerLoanService.findByCustomerTransactionId(transactionId);
        if (loan != null) {
            return ResultBean.getSucceed().setD(mappingService.map(loan, CustomerLoanBean.class));
        } else {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
    }

    @Override
    public ResultBean<List<CustomerLoanBean>> actGetCustomerLoansByTransactionId(String transactionId) {
        return null;
    }

    /**
     * 根据ID查询车辆资质信息中的借款信息
     *
     * @param id
     * @return
     */
    public ResultBean<CustomerLoanBean> actGetCustomerLoanById(String id) {
        CustomerLoan customerLoan = iCustomerLoanService.getOne(id);
        if (customerLoan == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_CUSTOMERLOAN_NOTFIND_BY_ID"), id));
        }
        return ResultBean.getSucceed().setD(mappingService.map(customerLoan, CustomerLoanBean.class));
    }

    /**
     * TODO:    计算  贴息方案
     *
     * @param bankFeeAmount      银行手续费
     * @param compensatoryAmount 贴息金额
     * @param months             贷款期限
     * @return
     */
    public ResultBean<Map<String, Object>> actCalculateCompensatoryWay1(Double bankFeeAmount, Double compensatoryAmount, Integer months) {
        Map<String, Object> map = new HashMap<String, Object>();
        String compensatoryWay = "无贴息";
        Integer compensatoryMonth = 0;

        int divisor = months / 12;//除数
        int remainder = months % 12;//余数
        if (compensatoryAmount == 0) {

        } else if (compensatoryAmount.doubleValue() == bankFeeAmount.doubleValue()) {
            compensatoryWay = "全贴息";
            compensatoryMonth = months;
        } else {
            if (remainder > 0) {
                compensatoryWay = "其它";
                compensatoryMonth = Integer.parseInt(String.valueOf(Math.round((compensatoryAmount.doubleValue() / bankFeeAmount) * divisor))) * 12;
            } else if (remainder == 0) {
                int compensatoryProportion = Integer.parseInt(String.valueOf(Math.round((compensatoryAmount.doubleValue() / bankFeeAmount) * divisor)));
                int selfProportion = divisor - compensatoryProportion;

                compensatoryWay = "贴息+自付：" + compensatoryProportion + "+" + selfProportion;
                if (selfProportion == 0) {
                    compensatoryWay = "其他";
                }
                compensatoryMonth = Integer.parseInt(String.valueOf(Math.round((compensatoryAmount.doubleValue() / bankFeeAmount) * divisor))) * 12;
            }
        }

        map.put("compensatoryWay", compensatoryWay);
        map.put("compensatoryMonth", compensatoryMonth);
        return ResultBean.getSucceed().setD(map);
    }

    //  计算贴息方案以及贴息期限
    public ResultBean<Map<String, Object>> actCalculateCompensatoryWay4(Double bankFeeAmount, Double compensatoryAmount, Integer months) {
        Map<String, Object> map = new HashMap<String, Object>();
        String compensatoryWay = "无贴息";
        Integer compensatoryMonth = 0;

        int monthsDivisor = months / 12;//除数
        int monthsRemainder = months % 12;//余数

        compensatoryMonth = Integer.parseInt(String.valueOf(Math.round((compensatoryAmount.doubleValue() / bankFeeAmount) * months)));
        int compensatoryMonthDivisor = compensatoryMonth / 12;//除数
        int compensatoryMonthRemainder = compensatoryMonth % 12;//余数
        if (compensatoryMonthDivisor == 0 && compensatoryMonthRemainder == 0) {
            compensatoryWay = "无贴息";
        } else if (monthsDivisor - compensatoryMonthDivisor == 0) {
            compensatoryWay = "全贴息";
        } else if (compensatoryMonthDivisor == 0 && compensatoryMonthRemainder != 0) {
            compensatoryWay = "贴息+自付：其他";
        } else {
            compensatoryWay = "贴息+自付：" + compensatoryMonthDivisor + "+" + (monthsDivisor - compensatoryMonthDivisor);
        }

        map.put("compensatoryWay", compensatoryWay);
        map.put("compensatoryMonth", compensatoryMonth);
        return ResultBean.getSucceed().setD(map);
    }

    public ResultBean<Map<String, Object>> actCalculateCompensatoryWay(Double bankFeeAmount, Double compensatoryAmount, Integer months) {
        Map<String, Object> map = new HashMap<String, Object>();
        String compensatoryWay = "无贴息";

        int monthsDivisor = months / 12;//年限
        int compensatoryMonth = Integer.parseInt(String.valueOf(Math.round((compensatoryAmount.doubleValue() / bankFeeAmount) * months)));

        //  贴息期限
        double compensatoryYear = monthsDivisor * compensatoryAmount.doubleValue() / bankFeeAmount;
        double compensatoryYearRemainder = monthsDivisor * compensatoryAmount.doubleValue() % bankFeeAmount;

        if (compensatoryYear == 0) {
            compensatoryWay = "无贴息";
        } else if (monthsDivisor - compensatoryYear == 0) {
            compensatoryWay = "全贴息";
        } else {
            //  贴息期限非整
            if (compensatoryYearRemainder > 0) {
                compensatoryWay = "贴息+自付：其他";
            } else {
                compensatoryWay = "贴息+自付：" + (int) compensatoryYear + "+" + ((int) monthsDivisor - (int) compensatoryYear);
            }
        }
        map.put("compensatoryWay", compensatoryWay);
        map.put("compensatoryMonth", compensatoryMonth);
        return ResultBean.getSucceed().setD(map);
    }

    @Override
    public ResultBean<CustomerRepaymentBean> actGetCustomerRepaymentByCustomerTransactionId(String customerTransactionId) {
        CustomerRepayment customerRepayment = iCustomerRepaymentService.findByCustomerTransactionId(customerTransactionId);
        if (customerRepayment != null) {
            return ResultBean.getSucceed().setD(mappingService.map(customerRepayment, CustomerRepaymentBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerRepaymentBean> actSaveCustomerRepayment(CustomerRepaymentBean customerRepaymentBean) {
        CustomerRepayment customerRepayment = mappingService.map(customerRepaymentBean, CustomerRepayment.class);
        customerRepayment = iCustomerRepaymentService.save(customerRepayment);
        return ResultBean.getSucceed().setD(mappingService.map(customerRepayment, CustomerRepaymentBean.class));
    }

    /**
     * 根据ID查询车辆资质信息中的购车信息
     *
     * @param id
     * @return
     */
    public ResultBean<CustomerCarBean> actGetCustomerCarById(String id) {
        CustomerCar customerCar = iCustomerCarService.getOne(id);
        if (customerCar == null) {
            return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_TYPE_NULL_BY_ID"), id));
        }
        return ResultBean.getSucceed().setD(mappingService.map(customerCar, CustomerCarBean.class));
    }

    /**
     * 根据Id查询客户卡信息
     *
     * @param id
     * @return
     */
    @Override
    public ResultBean<CustomerCardBean> actGetCustomerCardById(String id) {
        CustomerCard customerCard = iCustomerCardService.getOne(id);
        if (customerCard == null) {
            return ResultBean.getFailed();
        }
        return ResultBean.getSucceed().setD(mappingService.map(customerCard, CustomerCardBean.class));
    }

    /**
     * 通过客户交易Id查询卡信息
     *
     * @param customerTransactionId
     * @return
     */
    @Override
    public ResultBean<CustomerCardBean> actGetCustomerCardByCustomerTransactionId(String customerTransactionId) {
        CustomerCard customerCard = iCustomerCardService.findByCustomerTransactionId(customerTransactionId);
        if (customerCard == null) {
            return ResultBean.getFailed().setM(messageService.getMessage("MSG_FAIL_NUll"));
        }
        return ResultBean.getSucceed().setD(mappingService.map(customerCard, CustomerCardBean.class));
    }

    /**
     * 保存客户卡信息
     *
     * @param customerCardBean
     * @return
     */
    public ResultBean<CustomerCardBean> actSaveCustomerCard(CustomerCardBean customerCardBean) {
        CustomerCard customerCard = iCustomerCardService.save(mappingService.map(customerCardBean, CustomerCard.class));
        return ResultBean.getSucceed().setD(mappingService.map(customerCard, CustomerCardBean.class));
    }

    /**
     * 每笔业务对应两条借款信息，返回不可控制
     * @param customerTransactionId
     * @return
     */
    @Override
    @Deprecated
    public ResultBean<CustomerLoanBean> actGetCustomerLoan(String customerTransactionId) {
        CustomerLoan customerLoan = iCustomerLoanService.findByCustomerTransactionId(customerTransactionId);
        if (customerLoan != null) {
            return ResultBean.getSucceed().setD(mappingService.map(customerLoan, CustomerLoanBean.class));
        }
        return ResultBean.getFailed();
    }



    @Override
    public ResultBean<CustomerCarBean> actGetCustomerCar(String customerId) {
        CustomerCar customerCar = iCustomerCarService.findByCustomerId(customerId);
        if (customerCar != null) {
            return ResultBean.getSucceed().setD(mappingService.map(customerCar, CustomerCarBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<CustomerLoanBean>> actFindAllCustomerLoan() {
        List<CustomerLoan> all = iCustomerLoanService.findAll();
        if (all != null) {
            return ResultBean.getSucceed().setD(all);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerBean> actUpdateCustomerJob(String id, CustomerJobBean customerJobBean) {
        Customer customer = iCustomerService.getOne(id);
        if (customer != null) {
            customer.setCustomerJob(customerJobBean);
            customer = iCustomerService.save(customer);
            return ResultBean.getSucceed().setD(mappingService.map(customer, CustomerBean.class));
        }
        return ResultBean.getFailed().setM(messageService.getMessage("MSG_CUSTOMER_NULL_ID"));
    }

    /**
     *      保存客户的自雇信息
     * @param id
     * @param isSelfEmployed
     * @return
     */
    @Override
    public  ResultBean<CustomerBean> actSaveCustomerType(String id, Integer isSelfEmployed){
        Customer customer = iCustomerService.getOne(id);
        if (customer != null ){
            customer.setIsSelfEmployed(isSelfEmployed);
            customer = iCustomerService.save(customer);
            return ResultBean.getSucceed().setD(mappingService.map(customer, CustomerBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerCarExchangeBean> actSaveCustomerExchangeCar(CustomerCarExchangeBean customerCarExchangeBean) {
        CustomerExchangeCar customerExchangeCar = mappingService.map(customerCarExchangeBean, CustomerExchangeCar.class);
        CustomerExchangeCar exchangeCar = iCustomerExchangeCarService.save(customerExchangeCar);
        return ResultBean.getSucceed().setD(mappingService.map(exchangeCar, CustomerCarExchangeBean.class));
    }

    @Override
    public ResultBean<CustomerLoanExchangeBean> actSaveCustomerExchangeLoan(CustomerLoanExchangeBean customerLoanExchangeBean) {
        CustomerExchangeLoan customerExchangeLoan = mappingService.map(customerLoanExchangeBean, CustomerExchangeLoan.class);
        CustomerExchangeLoan exchangeLoan = iCustomerExchangeLoanService.save(customerExchangeLoan);
        return ResultBean.getSucceed().setD(mappingService.map(exchangeLoan, CustomerLoanExchangeBean.class));
    }

    @Override
    public ResultBean<CustomerLoanExchangeBean> actGetCustomerExchangeLoan(String exchangeLoanId) {
        CustomerExchangeLoan exchangeLoan = iCustomerExchangeLoanService.getAvailableOne(exchangeLoanId);
        if(exchangeLoan != null){
            return ResultBean.getSucceed().setD(mappingService.map(exchangeLoan,CustomerLoanExchangeBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerCarExchangeBean> actGetCustomerExchangeCar(String exchangeCarId) {
        CustomerExchangeCar exchangeCar = iCustomerExchangeCarService.getAvailableOne(exchangeCarId);
        if(exchangeCar != null){
            return ResultBean.getSucceed().setD(mappingService.map(exchangeCar,CustomerCarExchangeBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<CustomerLoanExchangeBean> actGetCustomerExchangeLoanByCustomerTransactionId(String customerTransactionId) {

        CustomerExchangeLoan customerExchangeLoan = iCustomerExchangeLoanService.findByCustomerTransactionId(customerTransactionId);
        if (customerExchangeLoan != null) {
            return ResultBean.getSucceed().setD(mappingService.map(customerExchangeLoan, CustomerLoanExchangeBean.class));
        }
        return ResultBean.getFailed();
    }


    public ResultBean<CustomerCarExchangeBean> actGetCustomerExchangeCarByCustomerTransactionId(String customerTransactionId) {
        CustomerExchangeCar customerExchangeCar = iCustomerExchangeCarService.findByCustomerTransactionId(customerTransactionId);
        if (customerExchangeCar != null) {
            return ResultBean.getSucceed().setD(mappingService.map(customerExchangeCar, CustomerCarExchangeBean.class));
        }
        return ResultBean.getFailed();
    }

}