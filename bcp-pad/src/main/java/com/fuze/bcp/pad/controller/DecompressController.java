package com.fuze.bcp.pad.controller;


import com.fuze.bcp.api.creditcar.bean.decompressBill.DecompressBillSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IDecompressBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ${Liu} on 2018/3/8.
 * 解押管理
 */
@RestController
@RequestMapping(value = "/json")
public class DecompressController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(DecompressController.class);

    @Autowired
    IDecompressBizService iDecompressBizService;

    /**
     *创建解押
     */
    @RequestMapping(value = "/decompress/{transactionId}/init",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean createDecompressBill(@PathVariable("transactionId") String transactionId){
        return iDecompressBizService.actCreateDecompress(transactionId);
    }

    /**
     *提交解押
     */
    @RequestMapping(value = "/decompress",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean submitDecompressBill(@RequestBody DecompressBillSubmissionBean decompressBillSubmissionBean){
        return iDecompressBizService.actSubmitDecompress(decompressBillSubmissionBean);
    }

    /**
     *获取解押单列表
     */
    @RequestMapping(value = "/decompresss",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDecompressBillList(@RequestParam(value = "pageindex", defaultValue = "0", required = false) Integer pageIndex, @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pageSize, @RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed){
        return iDecompressBizService.actGetDecomresss(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

    /**
     *获取解押单详情
     */
    @RequestMapping(value = "/decompress/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDecompressBillInfo(@PathVariable("id") String decompressId){
        return iDecompressBizService.actGetDecompressInfo(decompressId);
    }
}
