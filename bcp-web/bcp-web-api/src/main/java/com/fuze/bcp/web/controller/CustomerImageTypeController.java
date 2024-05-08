package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICustomerImageTypeBizService;
import com.fuze.bcp.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/json")
public class CustomerImageTypeController {

    @Autowired
    private IBaseDataBizService iBaseDataBizService;

    @Autowired
    private ICustomerImageTypeBizService iCustomerImageTypeBizService;

    /**
     * 获取所有档案类型列表(带分页)
     *
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/imagetype/pages", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerImageTypes(@RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage) {
        return iBaseDataBizService.actGetCustomerImageTypes(currentPage);
    }

    @RequestMapping(value = "/imagetypes", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerImageTypes() {
        return iBaseDataBizService.actGetCustomerImageTypes();
    }

    /**
     * 获取列表数据
     *
     * @return
     */
    @RequestMapping(value = "/imagetypes/lookups", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerImageType() {
        return iBaseDataBizService.actLookupCustomerImageTypes();
    }

    /**
     * 保存档案类型
     *
     * @param customerImageType
     * @return
     */
    @RequestMapping(value = "/imagetype", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean saveCustomerImageType(@RequestBody CustomerImageTypeBean customerImageType) {
        return iBaseDataBizService.actSaveCustomerImageType(customerImageType);
    }

    /**
     * 删除档案类型
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/imagetype/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultBean deleteCustomerImageType(@PathVariable("id") String id) {
        return iBaseDataBizService.actDeleteCustomerImageType(id);
    }

    /**
     * 根据ids获取资料类型
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/imagetypes/", method = RequestMethod.POST)
    @ResponseBody
    private ResultBean<List<CustomerImageTypeBean>> getCustomerImageTypesByIds(@RequestBody List<String> ids) {
        ResultBean<List<CustomerImageTypeBean>> customerImageTypes = iCustomerImageTypeBizService.actFindCustomerImageTypesByIds(ids);
        return customerImageTypes;
    }

    /**
     * 根据  codes  获取CustomerImages
     * @param codes
     * @return
     */
    @RequestMapping(value="/imagetypes/bycodes",method = RequestMethod.POST)
    @ResponseBody
    private ResultBean getCustomerImagesByCodes(@RequestBody List<String> codes){
        return iCustomerImageTypeBizService.actFindCustomerImageTypesByCodes(codes);
    }

    /**
     * 根据code 获取单个CustomerImages
     * @param code
     * @return
     */
    @RequestMapping(value = "/imagetype/bycode/{code}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerImageByCode(@PathVariable("code") String code){
        return iCustomerImageTypeBizService.actFindCustomerImageTypeByCode(code);
    }

    /**
     * 删除单个样例图片
     * @return
     */
    @RequestMapping(value = "/image/{id}/{fileId}",method = RequestMethod.DELETE)
    @ResponseBody
    private ResultBean deleteSingleImage(@PathVariable("id") String id,@PathVariable("fileId")String fileId){
        return iCustomerImageTypeBizService.actDeleteSingleImage(id,fileId);
    }

    @RequestMapping(value = "/imageTypes/param/{bizCode}/{billTypeCode}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerImageCodesByParam(@PathVariable("bizCode") String businessTypeCode,@PathVariable("billTypeCode") String billTypeCode){
        return iCustomerImageTypeBizService.actFindCustomerImageTypeCodesByParam(businessTypeCode,billTypeCode);
    }

    @RequestMapping(value = "/imageTypes/param/declaration",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getDeclarationImageTypes(){
        return iCustomerImageTypeBizService.actFindCustomerImageTypeCodesByDeclarationParam();
    }
}
