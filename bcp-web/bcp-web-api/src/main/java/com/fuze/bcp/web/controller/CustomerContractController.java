package com.fuze.bcp.web.controller;


import com.fuze.bcp.api.creditcar.bean.CustomerContractBean;
import com.fuze.bcp.api.creditcar.service.ICustomerContractBizService;
import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zqw on 2017/8/10.
 */
@RestController
@RequestMapping(value = "/json")
public class CustomerContractController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerContractController.class);

    @Autowired
    private ICustomerContractBizService iCustomerContractBizService;

    /**
     *          获取某交易的某几种客户档案
     * @param customerId                        客户ID
     * @param customerTransactionId            客户交易ID
     * @param documentIdsList                   资料类型ID集合
     * @return
     */
    @RequestMapping(value="/customercontract/{customerId}/{customerTransactionId}",method = RequestMethod.POST)
    @ResponseBody
    private ResultBean<List<CustomerContractBean>> actGetTransactionContracts(@PathVariable("customerId") String customerId,
                                                                              @PathVariable("customerTransactionId") String customerTransactionId,
                                                                              @RequestBody List<String> documentIdsList){

        return iCustomerContractBizService.actGetTransactionContracts(customerId,customerTransactionId,documentIdsList);
    }

    /**
     *      生成  用户的 合同
     * @param force                             是否强制生成
     * @param customerContractBean            用户 合同
     * @return
     */
    @RequestMapping(value="/customercontract", method=RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCustomerContract(@RequestParam("force")Boolean force,@RequestBody CustomerContractBean customerContractBean){

        return iCustomerContractBizService.actCreateCustomerContract(force,customerContractBean);
    }

    /**
     * 查找合成的文档
     * @param customerImageFileBean
     * @return
     */
    @RequestMapping(value = "/customercontract/merged",method = RequestMethod.POST)
    @ResponseBody
    private ResultBean getMergedCustomerContract(@RequestBody CustomerImageFileBean customerImageFileBean){
        return iCustomerContractBizService.actGetMergedCustomerContract(customerImageFileBean);
    }
}
