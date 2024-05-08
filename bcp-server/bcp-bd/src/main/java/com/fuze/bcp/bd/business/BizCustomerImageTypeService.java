package com.fuze.bcp.bd.business;

import com.fuze.bcp.api.bd.bean.CustomerImageTypeBean;
import com.fuze.bcp.api.bd.service.IBaseDataBizService;
import com.fuze.bcp.api.bd.service.ICustomerImageTypeBizService;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.sys.service.IParamBizService;
import com.fuze.bcp.bd.domain.CustomerImageType;
import com.fuze.bcp.bd.service.ICustomerImageTypeService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.service.MessageService;
import com.fuze.bcp.utils.Collections3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2017/7/20.
 */
@Service
public class BizCustomerImageTypeService implements ICustomerImageTypeBizService {

    private static final Logger logger = LoggerFactory.getLogger(BizCustomerImageTypeService.class);

    @Autowired
    ICustomerImageTypeService iCustomerImageTypeService;

    @Autowired
    MappingService mappingService;

    @Autowired
    MessageService messageService;

    @Autowired
    IFileBizService iFileBizService;

    @Autowired
    private IParamBizService iParamBizService;

    @Autowired
    private IBaseDataBizService iBaseDataBizService;


    @Override
    public ResultBean<CustomerImageTypeBean> actFindCustomerImageTypeByCode(String code) {
        CustomerImageType customerImageType = iCustomerImageTypeService.getCustomerImageTypeByCode(code);
        if (customerImageType == null) {
            return ResultBean.getFailed();
        } else {
            return ResultBean.getSucceed().setD(mappingService.map(customerImageType, CustomerImageTypeBean.class));
        }
    }

    @Override
    public ResultBean<CustomerImageTypeBean> actFindCustomerImageTypeById(String id) {
        CustomerImageType customerImageType = iCustomerImageTypeService.FindCustomerImageTypeById(id);
        if (customerImageType == null) {
            return ResultBean.getFailed();
        } else {
            return ResultBean.getSucceed().setD(mappingService.map(customerImageType, CustomerImageTypeBean.class));
        }
    }

    @Override
    public ResultBean<List<CustomerImageTypeBean>> actFindCustomerImageTypesByIds(List<String> ids) {
        List<CustomerImageType> customerImageTypes = iCustomerImageTypeService.findCustomerImageTypesByIds(ids);
        if (customerImageTypes == null)
            customerImageTypes = new ArrayList<CustomerImageType>();
        return ResultBean.getSucceed().setD(mappingService.map(customerImageTypes,CustomerImageTypeBean.class));
    }

    @Override
    public ResultBean<List<CustomerImageTypeBean>> actFindCustomerImageTypesByCodes(List<String> codes) {
        List<CustomerImageType> customerImageTypes = iCustomerImageTypeService.findCustomerImageTypesByCodes(codes);
        if (customerImageTypes == null)
            customerImageTypes = new ArrayList<CustomerImageType>();
        return ResultBean.getSucceed().setD(mappingService.map(customerImageTypes,CustomerImageTypeBean.class));
    }

    @Override
    public ResultBean<CustomerImageTypeBean> actDeleteSingleImage(String id,String imageId) {
        CustomerImageType customerImageType = iCustomerImageTypeService.getOne(id);
        if(customerImageType!=null){
            List imageIds = customerImageType.getExampleFileIds();
            imageIds.remove(imageId);
            iFileBizService.actDeleteFileById(imageId);
            customerImageType.setExampleFileIds(imageIds);
            iCustomerImageTypeService.save(customerImageType);
            return ResultBean.getSucceed().setD(mappingService.map(customerImageType,CustomerImageTypeBean.class));
        }
        return ResultBean.getFailed();
    }


    public ResultBean<List<CustomerImageTypeBean>> actGetCustomerImageTypesByBillType(String bizCode, String billTypeCode) {
        //读取系统参数
        Map<?, ?> padImages = iParamBizService.actGetMap("PAD_IMAGES_ORDER").getD();
        Map<?, ?> padBizImages = (Map<?, ?>) padImages.get(bizCode);
        List<String> imageTypeCodeList = (List<String>)padBizImages.get(billTypeCode);

        List<CustomerImageTypeBean> cits = new ArrayList<CustomerImageTypeBean>();
        for (String imageTypeCode : imageTypeCodeList) {
            CustomerImageTypeBean imageType = iBaseDataBizService.actGetCustomerImageType(imageTypeCode).getD();
            cits.add(imageType);
        }
        return ResultBean.getSucceed().setD(cits);
    }

    /**
     *  从系统参配项中获取，WEB端档案资料的配置顺序
     * @param businessTypeCode
     * @param billTypeCode
     * @return
     */
    @Override
    public ResultBean<String> actFindCustomerImageTypeCodesByParam(String businessTypeCode, String billTypeCode){
        //读取系统参数
        Map<?, ?> webImages = iParamBizService.actGetMap("WEB_IMAGES_ORDER").getD();
        if ( webImages == null){
            logger.error(String.format(messageService.getMessage("MSG_SYSPARAM_NOTFIND_NULL"),"WEB_IMAGES_ORDER"));
            return ResultBean.getFailed();
        }
        Map<?, ?> webBizImages = (Map<?, ?>) webImages.get(businessTypeCode);
        List<String> imageTypeCodeList = (List<String>)webBizImages.get(billTypeCode);
        return ResultBean.getSucceed().setD(imageTypeCodeList);
    }

    /**
     *  从系统参配项中获取，银行报批需要选择的材料清单
     * @return
     */
    public ResultBean<CustomerImageTypeBean> actFindCustomerImageTypeCodesByDeclarationParam(){
        ResultBean<List<?>> resultBean = iParamBizService.actGetList("DECLARATION_CUSTOMERIMAGETIES");
        if (resultBean.isSucceed()){
            List<String> customerImageTypeCodes = (List<String>) resultBean.getD();
            List<CustomerImageType> customerImageTypes = new ArrayList<CustomerImageType>();
            for (String customerImageTypeCode : customerImageTypeCodes){
                CustomerImageType customerImageTypeByCode = iCustomerImageTypeService.getCustomerImageTypeByCode(customerImageTypeCode);
                if (customerImageTypeByCode != null){
                    customerImageTypes.add(customerImageTypeByCode);
                } else {
                    logger.error(String.format(messageService.getMessage("MSG_CUSTOMERIMAGETYPE_NULL_CODE"),customerImageTypeCode));
                }
            }
            return ResultBean.getSucceed().setD(mappingService.map(customerImageTypes,CustomerImageTypeBean.class));
        }
        return ResultBean.getFailed().setM(String.format(messageService.getMessage("MSG_SYSPARAM_NOTFIND_CODE"),"DECLARATION_CUSTOMERIMAGETIES"));
    }

    /**
     *  【pad】 获取pad端某业务的所有档案资料
     * @param businessTypeCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultBean<DataPageBean<CustomerImageTypeBean>> actGetCustomerImageTypes(String businessTypeCode, Integer pageIndex, Integer pageSize) {
        //读取系统参数
        Map<?, ?> padImages = iParamBizService.actGetMap("PAD_IMAGES_ORDER").getD();
        Map<?, ?> padBizImages = (Map<?, ?>) padImages.get(businessTypeCode);
        List<String> businessTypeImageTypeCodes = new ArrayList<String>();
        for (Object billTypeCode : padBizImages.keySet()){
            businessTypeImageTypeCodes.addAll((List<String>) padBizImages.get(billTypeCode));
        }
        businessTypeImageTypeCodes = Collections3.stringListDistinct(businessTypeImageTypeCodes);
        Page<CustomerImageType> customerImageTypePage = iCustomerImageTypeService.getCustomerImageTypesByCodes(businessTypeImageTypeCodes,pageIndex,pageSize);
        DataPageBean<CustomerImageTypeBean> destination = new DataPageBean<CustomerImageTypeBean>();
        destination.setPageSize(customerImageTypePage.getSize());
        destination.setTotalCount(customerImageTypePage.getTotalElements());
        destination.setTotalPages(customerImageTypePage.getTotalPages());
        destination.setCurrentPage(customerImageTypePage.getNumber());
        destination.setResult(mappingService.map(customerImageTypePage.getContent(),CustomerImageTypeBean.class));
        return ResultBean.getSucceed().setD(destination);
    }
}
