package com.fuze.bcp.pad.controller;

import com.fuze.bcp.api.creditcar.bean.appointpayment.AppointPaymentSubmissionBean;
import com.fuze.bcp.api.creditcar.bean.cartransfer.CarTransferSubmissionBean;
import com.fuze.bcp.api.creditcar.service.IAppointPaymentBizService;
import com.fuze.bcp.api.creditcar.service.ICarTransferBizService;
import com.fuze.bcp.bean.ResultBean;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 转移过户
 */
@RestController
@RequestMapping(value = "/json")
public class CarTransferController extends BaseController {

    @Autowired
    ICarTransferBizService iCarTransferBizService;

    /**
     * 【PAD-API】 分页获取loginUserId 的转移过户
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/cartransfers", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarTransfersPage(@RequestParam(value = "pageindex", required = false, defaultValue = "0") Integer pageIndex, @RequestParam(value = "pagesize", required = false, defaultValue = "5") Integer pageSize,@RequestParam(value = "ispassed", defaultValue = "0", required = false) boolean isPassed) {
        return iCarTransferBizService.actGetCarTransfers(isPassed,super.getOperatorId(), pageIndex, pageSize);
    }

    /**
     * 【PAD-API】 根据ID获取我的垫资
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/transaction/{transactionid}/cartransfer", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCarTransfer(@PathVariable("transactionid") String transactionId) {
        return iCarTransferBizService.actInitCarTransferByTransactionId(transactionId);
    }

    /**
     * 【PAD-API】临时保存转移过户(整体提交)
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/cartransfer", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCarTransfer(@RequestBody CarTransferSubmissionBean carTransferSubmissionBean) {
        carTransferSubmissionBean.setLoginUserId(super.getOperatorId());
        return iCarTransferBizService.actSaveCarTransfer(carTransferSubmissionBean);
    }

    /**
     * 【PAD-API】 提交保存转移过户(进工作流)
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/cartransfer/{cartransferid}", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean submiCarTransfer(@PathVariable("cartransferid") String carTransferId, @RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        String comment = (String) obj.get("comment");
        return iCarTransferBizService.actSubmitCarTransfer(carTransferId, comment);
    }


    /***************************************************临时接口，创建***********************************************8/
     /**
     * 【PAD-API】 临时接口，创建
     *
     * @return ResultBean
     */
    @RequestMapping(value = "/cartransfer/{orderid}/init", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean creatrCarTransferByOrderId(@PathVariable("orderid") String orderId) {
        return iCarTransferBizService.actCreateCarTransfer(orderId);
    }


}
