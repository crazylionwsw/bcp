package com.fuze.bcp.bd.service;

import com.fuze.bcp.api.bd.bean.CarBrandBean;
import com.fuze.bcp.api.bd.bean.CarModelBean;
import com.fuze.bcp.api.bd.bean.CarTypeBean;
import com.fuze.bcp.api.bd.service.ICarTypeBizService;
import com.fuze.bcp.bean.APILookupBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by CJ on 2017/6/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class CarTypeMrgServiceTest {

    @Autowired
    ICarTypeBizService iCarTypeMrgService;


    @Test
    public void testactGetCarBrands() {
        ResultBean<DataPageBean<CarBrandBean>> data = iCarTypeMrgService.actGetCarBrands(0);
        System.out.println(data);
    }

    @Test
    public void testactLookupCarBrands() {
        ResultBean<List<APILookupBean>> data = iCarTypeMrgService.actLookupCarBrands();
        System.out.println(data);
    }

    @Test
    public void testDeleteCarBrand() {
        CarBrandBean carBrandBean = new CarBrandBean();
        carBrandBean.setId("593f3bb963a6e80fe4b12882");
        ResultBean<CarBrandBean> data = iCarTypeMrgService.actDeleteCarBrand("593f3bb963a6e80fe4b12882");
        System.out.println(data);
    }

    @Test
    public void testSaveCarBrand() {
        CarBrandBean carBrand = new CarBrandBean();
        carBrand.setGroupName("testG1");
        ResultBean<CarBrandBean> data = iCarTypeMrgService.actSaveCarBrand(carBrand);
        System.out.println(data);

    }


    @Test
    public void testactGetCarModels1() {
        CarBrandBean carBrand = new CarBrandBean();
        carBrand.setId("58fa051c77c8b44a492957c6");
        ResultBean<DataPageBean<CarModelBean>> datas = iCarTypeMrgService.actGetCarModels(0, "593f3bb963a6e80fe4b12882");

        System.out.println(datas);

    }

    @Test
    public void testActGetCarModels() {
        ResultBean<DataPageBean<CarModelBean>> data = iCarTypeMrgService.actGetCarModels(0);
        System.out.println(data);
    }

    @Test
    public void testActGetCarModels2() { // 数据过多，超时，请使用分页方法或者有参数方法
        ResultBean<List<APILookupBean>> data = iCarTypeMrgService.actLookupCarModels();
        System.out.println(data);
    }

    @Test
    public void testActGetCarModels3() {
        CarBrandBean carBrandBean = new CarBrandBean();
        carBrandBean.setId("58fa051c77c8b44a492957e2");
        ResultBean<List<APILookupBean>> data = iCarTypeMrgService.actLookupCarModels("58fa051c77c8b44a492957e2");
        System.out.println(data);
    }

    @Test
    public void testActGetCarTypes() { // 超时,请使用有参数方法
        ResultBean<DataPageBean<CarTypeBean>> data = iCarTypeMrgService.actGetCarTypes(0);
        System.out.println(data);
    }

    @Test
    public void testActGetCarTypes2() {
        CarModelBean carModelBean = new CarModelBean();
        carModelBean.setId("593f4d8a63a6e832749785a3");
        ResultBean<DataPageBean<CarTypeBean>> data = iCarTypeMrgService.actGetCarTypes(0, "593f4d8a63a6e832749785a3");
        System.out.println(data);
    }

    @Test
    public void testActGetCarTypes3() {
        CarModelBean carModelBean = new CarModelBean();
        carModelBean.setId("593f4d8a63a6e832749785a3");
        ResultBean<List<APILookupBean>> data = iCarTypeMrgService.actLookupCarTypes("593f4d8a63a6e832749785a3");
        System.out.println(data);

    }

    @Test
    public void testActGetCarTypes4() { // 超时
        CarModelBean carModelBean = new CarModelBean();
        carModelBean.setId("593f4d8a63a6e832749785a3");
        ResultBean<List<APILookupBean>> data = iCarTypeMrgService.actLookupCarTypes();
        System.out.println(data);
    }

    @Test
    public void testSaveCarModel() {
        CarModelBean carModelBean = new CarModelBean();
        carModelBean.setGroupName("testM2");
        ResultBean<CarBrandBean> carBrandResultBean = iCarTypeMrgService.actGetCarBrandById("593f4d6c63a6e832749785a2");
        carModelBean.setCarBrandId(carBrandResultBean.getD().getId());
        ResultBean<CarModelBean> carModelResultBean = iCarTypeMrgService.actSaveCarModel(carModelBean);
        System.out.println(carModelResultBean);
    }

    @Test
    public void testSaveCarType() {
        CarTypeBean carTypeBean = new CarTypeBean();
        carTypeBean.setModel("testT1");
        carTypeBean.setCarBrandId("593f4d6c63a6e832749785a2");
        carTypeBean.setCarModelId("593f4d8a63a6e832749785a3");
        ResultBean<CarTypeBean> data = iCarTypeMrgService.actSaveCarType(carTypeBean);
        System.out.println(data);

    }


}
