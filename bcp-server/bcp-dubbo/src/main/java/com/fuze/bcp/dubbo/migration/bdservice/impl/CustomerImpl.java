package com.fuze.bcp.dubbo.migration.bdservice.impl;

import com.fuze.bcp.api.customer.bean.CustomerJobBean;
import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.bdservice.Customer;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by CJ on 2017/10/24.
 */
public class CustomerImpl implements Customer {


    @Override
    public String bDDataMigration(BasicDBObject sourceMap, MongoBaseEntity entity, MongoTemplate source, MongoTemplate target) {
        com.fuze.bcp.customer.domain.Customer obj = (com.fuze.bcp.customer.domain.Customer) entity;
        obj.setId(sourceMap.getString("_id"));
        obj.setName(sourceMap.getString("name"));
        obj.setDataStatus(sourceMap.getInt("dataStatus"));
        obj.setTs(sourceMap.getString("ts"));
        obj.setAddress(sourceMap.getString("address"));
        obj.setAge(sourceMap.getInt("age"));
        obj.setAuthorizeBy(sourceMap.getString("authorizeBy"));
        obj.setBirthday(sourceMap.getString("birthday"));
        obj.setCells((List<String>) sourceMap.get("cells"));
        obj.setContactAddress(sourceMap.getString("contactAddress"));
        BasicDBObject bas = (BasicDBObject) sourceMap.get("companyInfo");
        CustomerJobBean c = new CustomerJobBean();
        if (bas != null) {
            c.setCompanyName(bas.getString("name"));
            c.setCompanyAddress(bas.getString("address"));
            c.setCompanyNature(bas.getString("nature"));
            c.setEducation(bas.getString("education"));
            c.setEmail(bas.getString("email"));
            c.setEntryDate(bas.getString("entryDate"));
            c.setHrCell(bas.getString("hrCell"));
            c.setHrName(bas.getString("hrName"));
            c.setJob(bas.getString("job"));
            c.setSalary(bas.getDouble("salary"));
            c.setWorkDate(bas.containsField("workDate") ? bas.getInt("workDate") : null);
        }
        obj.setCustomerJob(c);
        if (sourceMap.getBoolean("directGuest")) {
            obj.setDirectGuest(1);
        } else {
            obj.setDirectGuest(0);
        }
        obj.setGender(sourceMap.getInt("gender"));
        obj.setIdentifyNo(sourceMap.getString("identifyNo"));
        if (sourceMap.containsField("identifyValid") && !"".equals(sourceMap.getString("identifyValid"))) {
            StringBuffer date = new StringBuffer();

            String identifyValid = sourceMap.getString("identifyValid"); // 2022-2-1至2020-2-1 长期  长期有效
            String[] str = identifyValid.split("至");
            if (str.length == 2) {
                String str1 = identifyValid.split("至")[0];
                if (str1 != null && !"".equals(str1)) {
                    String[] str11 = str1.split("-");
                    if (str11.length == 3) {
                        Calendar ccc = Calendar.getInstance();
                        ccc.set(Integer.valueOf(str11[0].trim()), Integer.valueOf(str11[1].trim()), Integer.valueOf(str11[2].trim()));
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
                        date.append(sf.format(ccc.getTime()));
                    }
                }
                date.append("-");
                String str2 = identifyValid.split("至")[1];
                if (str2 != null && !"".equals(str2)) {
                    String[] str11 = str2.split("-");
                    if (str11.length == 3) {
                        Calendar ccc = Calendar.getInstance();
                        ccc.set(Integer.valueOf(str11[0].trim()), Integer.valueOf(str11[1].trim()), Integer.valueOf(str11[2].trim()));
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
                        date.append(sf.format(ccc.getTime()));
                    }else if (str2.indexOf("长期") != -1){
                        date.append("长期有效");
                    }
                }
                obj.setIdentifyValid(date.toString());
            }


        }
        //[{"code":"unmarried","name":"未婚"},{"code":"married","name":"已婚"},{"code":"divorced","name":"离异"},{"code":"widowed","name":"丧偶"},{"code":"other","name":"其他"}]
        if (sourceMap.getString("maritalStatus") != null){
            if ("0".equals(sourceMap.getString("maritalStatus"))){
                obj.setMaritalStatus("unmarried");
            } else if ("1".equals(sourceMap.getString("maritalStatus"))){
                obj.setMaritalStatus("married");
            } else if ("2".equals(sourceMap.getString("maritalStatus"))){
                obj.setMaritalStatus("divorced");
            } else if ("3".equals(sourceMap.getString("maritalStatus"))){
                obj.setMaritalStatus("widowed");
            }
        } else {
            obj.setMaritalStatus("other");
        }


        obj.setNationality(sourceMap.getString("nationality"));

//        obj.setIsBJCensusRegister();
//        obj.setIsCarBrand();
//        obj.setIsHouseBrand();
//        obj.setCensusRegisterCity();
//        obj.setIsSocialInsurance();
//        obj.setSocialInsuranceDate();
//        obj.setWx_openid();

        System.out.println("-----------Customer-------------迁移完成-----------------------------");

        return null;
    }
}
