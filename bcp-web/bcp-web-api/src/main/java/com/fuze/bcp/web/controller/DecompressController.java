package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.auth.jwt.JwtUser;
import com.fuze.bcp.api.creditcar.bean.businessexchange.BusinessExchangeBean;
import com.fuze.bcp.api.creditcar.bean.decompressBill.DecompressBillBean;
import com.fuze.bcp.api.creditcar.service.IDecompressBizService;
import com.fuze.bcp.api.workflow.bean.SignInfo;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.bean.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ${Liu} on 2018/3/10.
 * 解押管理
 */
@RestController
@RequestMapping(value = "/json")
public class DecompressController {

    @Autowired
    IDecompressBizService iDecompressBizService;

    /**
     *发起解押管理
     */
    @RequestMapping(value = "/create/decompress/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean createDecompress(@PathVariable("id")String transactionId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iDecompressBizService.actCreateDecompressOnPc(transactionId,userId);
    }

    /**
     *获取解押列表(含查询)
     */
    @RequestMapping(value = "/decompresss/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<DecompressBillBean> searchDecompress(@RequestBody SearchBean searchBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        ResultBean<DecompressBillBean> decompressBillBeanResultBean = iDecompressBizService.actSearchDecomresss(userId, searchBean);
        return decompressBillBeanResultBean;
    }

    /**
     *获取解押详情
     */
    @RequestMapping(value = "/decompress/{id}/{dataStatus}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDecompress(@PathVariable("id")String decompressId,@PathVariable("dataStatus")Integer dataStatus) {
        return iDecompressBizService.actGetDecompress(decompressId,dataStatus);
    }

    /**
     * 审核解押单
     */
    @RequestMapping(value = "/decompress/{id}/sign", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean signDecompress(@PathVariable("id")String id, @RequestBody SignInfo signInfo) {
        return iDecompressBizService.actSignDecompress(id, signInfo);
    }

    /**
     * 通过交易获取解压单
     * @param customerTransactionId
     * @return
     */
    @RequestMapping(value="/decompress/transaction/{customerTransactionId}", method=RequestMethod.GET)
    @ResponseBody
    public ResultBean getDecompressByTransactionId(@PathVariable("customerTransactionId")String customerTransactionId){
        return iDecompressBizService.actGetDecompressByTransactionId(customerTransactionId);
    }

    /**
     * 保存当前的解押单
     * @param decompressBillBean
     * @return
     */
    @RequestMapping(value = "/decompress",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveDecompress(@RequestBody DecompressBillBean decompressBillBean){
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwtUser.getId();
        return iDecompressBizService.actSaveDecompress(decompressBillBean,userId);
    }

}
