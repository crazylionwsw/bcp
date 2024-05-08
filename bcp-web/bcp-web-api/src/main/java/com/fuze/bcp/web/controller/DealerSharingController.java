package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.cardealer.service.ICarDealerBizService;
import com.fuze.bcp.api.creditcar.bean.dealersharing.DealerSharingBean;
import com.fuze.bcp.api.creditcar.bean.dealersharing.GroupSharingBean;
import com.fuze.bcp.api.creditcar.bean.dealersharing.SharingDetailsBean;
import com.fuze.bcp.api.creditcar.service.IDealerSharingBizService;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lily on 2017/5/5.
 */
@RestController
@RequestMapping(value = "/json")
public class DealerSharingController {

    private static final Logger logger = LoggerFactory.getLogger(DealerSharingController.class);

    @Autowired
    private IDealerSharingBizService iDealerSharingBizService;

    @Autowired
    ICarDealerBizService iCarDealerBizService;

    //集团分成列表(带分页)
    @RequestMapping(value = "/group/sharings", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDelerGroup(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage, String month, String groupId) {
        return iDealerSharingBizService.actGetGroupSharings(currentPage, month, groupId);
    }

    /**
     * 集团分成详细
     */
    @RequestMapping(value = "/group/sharing/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookupGroupSharing(@PathVariable("id") String id) {
        Map map = new HashMap();
        map.put("data", iDealerSharingBizService.actLookupGroupDetail(id).getD());
        return ResultBean.getSucceed().setD(map);
    }

    /**
     * 重新生成特定月份的分成详细
     *
     * @param saleMonth
     * @return
     */
    @RequestMapping(value = "/sharing/details", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean resetSharingDetails(@RequestParam String groupId, @RequestParam String saleMonth) {
        if ("".equals(groupId)) {
            List<APILookupBean> list = iCarDealerBizService.actLookupDealerGroup().getD();
            if (list != null) {
                for (APILookupBean b : list) {
                    iDealerSharingBizService.actResetSharingDetails(saleMonth, 2, b.getId(), null);
                    iDealerSharingBizService.actCreateGroupSharing(b.getId(), saleMonth);
                }
            }
            return ResultBean.getSucceed();
        } else {
            iDealerSharingBizService.actResetSharingDetails(saleMonth, 2, groupId, null);
            iDealerSharingBizService.actCreateGroupSharing(groupId, saleMonth);
            return ResultBean.getSucceed();
        }

    }

    /**
     * 创建当月特定集团的集团分成
     */
    @RequestMapping(value = "/creat/group/sharing", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean creatGroupSharing(@RequestParam String groupId, @RequestParam String saleMonth) {
        return iDealerSharingBizService.actCreateGroupSharing(groupId, saleMonth);
    }

    /**
     * 创建特定渠道指定月份的渠道分成
     */
    @RequestMapping(value = "/creat/dealer/sharing", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean creatDealerSharing(@RequestParam String dealerId, @RequestParam String saleMonth) {
        iDealerSharingBizService.actResetDealerSharingDetails(saleMonth, dealerId);
        return iDealerSharingBizService.actCreateDealerSharing(dealerId, saleMonth);
    }

    /**
     * 更改渠道详细的状态
     */
    @RequestMapping(value = "/confirm/status", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<SharingDetailsBean> confirmStatus(@RequestParam String id, @RequestParam String carDealerId, @RequestParam String sharingDetailId, @RequestParam Integer status, @RequestParam Double sharingRatio, @RequestParam Double sharingAmount, @RequestParam Integer mainPartType) throws Exception {
        return iDealerSharingBizService.actConfirmStatus(id, carDealerId, sharingDetailId, status, sharingRatio, sharingAmount, mainPartType);
    }

    /**
     * 分页获取分成详细
     */
    @RequestMapping(value = "/sharings/details", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean sharingDetail(Integer currentPage) {
        return iDealerSharingBizService.actGetSharingDetails(currentPage);
    }

    /**
     * 分页获取渠道分成(分页列表)
     */
    @RequestMapping(value = "/dealersharings", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean dealerSharings(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage, String month, String orgId) {
        return iDealerSharingBizService.actGetDealerSharings(currentPage, month, orgId);
    }

    /**
     * 获取全部渠道分成信息
     */
    @RequestMapping(value = "/lookup/dealersharingdetail", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean<List<SharingDetailsBean>> lookUpDealerSharingDetail(@RequestParam("id") String carDealerSharingId) {
        return iDealerSharingBizService.actLookupDealerDetail(carDealerSharingId);
    }

    // 获取特定集团的集团 分成详细
    @RequestMapping(value = "/lookup/dealerdetail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean lookUpGroupDetail(@PathVariable("id") String groupSharingId) {
        return iDealerSharingBizService.actLookupGroupDetail(groupSharingId);
    }

    /**
     * 获取
     *
     * @param groupId
     * @param saleMonth
     * @return
     */
    @RequestMapping(value = "/groupsharing/info", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getGroupSharingInfo(@RequestParam String groupId, @RequestParam String saleMonth) {
        return iDealerSharingBizService.actGetGroupDetail(groupId, saleMonth);
    }

    /**
     * 根据月份和部门创建渠道分成
     *
     * @param saleMonth
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/dealerdetail/create", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean createDealerSharings(@RequestParam String saleMonth, @RequestParam String orgId) {
        iDealerSharingBizService.actResetSharingDetails(saleMonth, 1, null, orgId);
        return iDealerSharingBizService.actCreateDealerSharings(saleMonth, orgId);
    }

    /**
     * 获取单条渠道分成信息
     *
     * @param dealerSharingId
     * @return
     */
    @RequestMapping(value = "/dealer/sharing/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDealerSharing(@PathVariable("id") String dealerSharingId) {
        return iDealerSharingBizService.actGetDealerSharing(dealerSharingId);
    }

    /**
     * 分成处理结算状态(渠道)
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sharing/balancecardealersharing", method = RequestMethod.GET)
    public ResultBean<DealerSharingBean> balanceDealerSharing(@RequestParam("id") String id) {
        try {
            iDealerSharingBizService.actDoBalanceSharing(SharingDetailsBean.DEALERDETAIL, id);
            ResultBean<DealerSharingBean> dealerSharingBeanResultBean = iDealerSharingBizService.actGetDealerSharing(id);
            return dealerSharingBeanResultBean;
        } catch (Exception e) {
            return ResultBean.getFailed();
        }
    }

    /**
     * 分成处理结算状态(集团)
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sharing/balancegroupsharing", method = RequestMethod.GET)
    public ResultBean<GroupSharingBean> balanceGroupSharing(@RequestParam("id") String id) {
        try {
            iDealerSharingBizService.actDoBalanceSharing(SharingDetailsBean.GROUPDETAIL, id);
            return iDealerSharingBizService.actGetGroupSharing(id);
        } catch (Exception e) {
            return ResultBean.getFailed();
        }
    }

    @RequestMapping(value = "/sharing/getGroupSharing", method = RequestMethod.GET)
    public ResultBean<GroupSharingBean> getGroupSharing(@RequestParam("id") String id) {
        return iDealerSharingBizService.actGetGroupSharing(id);
    }

}
