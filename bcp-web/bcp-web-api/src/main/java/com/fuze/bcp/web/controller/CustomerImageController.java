package com.fuze.bcp.web.controller;

import com.fuze.bcp.api.creditcar.bean.CustomerImageFileBean;
import com.fuze.bcp.api.creditcar.service.ICustomerImageFileBizService;
import com.fuze.bcp.bean.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Lily on 2017/7/28.
 */
@RestController
@RequestMapping(value = "/json")
public class CustomerImageController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerImageController.class);

    @Autowired
    private ICustomerImageFileBizService iCustomerImageFileBizService;

    /**
     * 保存档案图片
     * @param customerImageFileBean
     * @return
     */
    @RequestMapping(value="/customerimage",method = RequestMethod.POST)
    @ResponseBody
    private ResultBean saveCustomerImage(@RequestBody CustomerImageFileBean customerImageFileBean){
        return iCustomerImageFileBizService.actSaveCustomerImage(customerImageFileBean);
    }

    /**
     * 删除档案图片
     * @param id
     * @return
     */
    @RequestMapping(value="/customerimage/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    private ResultBean deleteCustomerImage(@PathVariable("id")String id){
        return iCustomerImageFileBizService.actDeleteCustomerImage(id);
    }

    /**
     * 根据ids获取CustomerImages
     * @param ids
     * @return
     */
    @RequestMapping(value="/customerimages/",method = RequestMethod.POST)
    @ResponseBody
    private ResultBean getCustomerImagesByIds(@RequestBody List<String> ids){
        return iCustomerImageFileBizService.actFindCustomerImagesByIds(ids);
    }

    /**
     *          获取某交易的某几种客户档案
     * @param customerId                        客户ID
     * @param customerTransactionId            客户交易ID
     * @param imageTypeCodeList                资料类型编码集合
     * @return
     */
    @RequestMapping(value="/customerimages/{customerId}/{customerTransactionId}",method = RequestMethod.POST)
    @ResponseBody
    private ResultBean<List<CustomerImageFileBean>> actGetTransactionImages(@PathVariable("customerId") String customerId,
                                                                            @PathVariable("customerTransactionId") String customerTransactionId,
                                                                            @RequestBody  List<String> imageTypeCodeList){

        return iCustomerImageFileBizService.actGetTransactionImages(customerId,customerTransactionId,imageTypeCodeList);
    }

    /**
     * 合成身份证
     * @param customerImageFileBean
     * @return
     */
    @RequestMapping(value="/customerimages/merge",method = RequestMethod.POST)
    @ResponseBody
    private ResultBean mergeCustomerImages(@RequestParam("force") Boolean force,
                                           @RequestBody CustomerImageFileBean customerImageFileBean,
                                           @RequestParam("loginUserId") String loginUserId) throws Exception {

        return iCustomerImageFileBizService.actMergeImages(force,customerImageFileBean,loginUserId);
    }

    //根据id获取图片文件
    @RequestMapping(value = "/customerimage/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getCustomerImage(@PathVariable("id") String id){
        ResultBean R = iCustomerImageFileBizService.actFindCustomerImageById(id);
        return R;
    }

    @RequestMapping(value = "/images/{id}",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getImages(@PathVariable("id") String id,@RequestBody List<String> imageTypeCodeList){
        return iCustomerImageFileBizService.actGetImageByIdAndByImageTypeCodeList(id,imageTypeCodeList);
    }

    /**
     * 打包
     * @param cp
     * @return
     *//*
    @RequestMapping(value="/customerimages/package",method = RequestMethod.POST)
    @ResponseBody
    private ResultBean packageCustomerImages(@RequestBody CustomerPackageBean cp){

        return iCustomerPackageService.saveCustomerPackage(cp);
    }
    */

    /*
    //打包记录
    @RequestMapping(value="/customerpackages",method= RequestMethod.POST)
    @ResponseBody
    public ResultBean getAllCustomerPackages(@RequestBody PurchaseCarOrder purchaseCarOrder
    ){
        String purchaseCarOrderId = purchaseCarOrder.getId();
        String customerId = purchaseCarOrder.getCustomerId();
        ResultMsg<List<CustomerPackage>>  customerPackages= iCustomerPackageService.getAllCustomerPackagesByPurchaseCarOrderIdAndCustomerId(purchaseCarOrderId,customerId);
        return customerPackages;
    }

    //下载资料包
    @RequestMapping(value="/customerpackages/{id}/download",method = RequestMethod.GET)
    @ResponseBody
    private void download(@PathVariable("id") String id,HttpServletResponse response) throws Exception {
        // 清空response
        response.reset();
        InputStream fis = null;
        OutputStream toClient = null;
        try{
            //通过id查询customerPackage对象
            CustomerPackageBean customerPackage = iCustomerPackageService.getCustomerPackageById(id);
            // urlpath是下载的文件的路径。
            String urlpath = customerPackage.getUrlPath();
            File file = new File(urlpath);
            //取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件。
            fis = new BufferedInputStream(new FileInputStream(urlpath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();

            response.flushBuffer();
        }catch (IOException ex){
            ex.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (Exception ex) {
            }
            try {
                if (toClient != null)
                    toClient.close();
            } catch (Exception ex) {
            }
        }

    }*/



}
