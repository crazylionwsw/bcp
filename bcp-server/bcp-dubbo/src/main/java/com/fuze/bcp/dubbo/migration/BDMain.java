package com.fuze.bcp.dubbo.migration;

import com.fuze.bcp.domain.MongoBaseEntity;
import com.fuze.bcp.dubbo.migration.mongo.MongoConnect;
import com.fuze.bcp.dubbo.migration.mongo.ParamObject;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/10/18.
 */
public class BDMain {

    static Map<String, ParamObject> transactionMap = new HashMap<>();

    static {
        transactionMap.put("bd_carbrand",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.CarBrandImpl", "bd_carbrand", "com.fuze.bcp.bd.domain.CarBrand"));

        transactionMap.put("bd_carmodel",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.CarModelImpl", "bd_carmodel", "com.fuze.bcp.bd.domain.CarModel"));

        transactionMap.put("bd_cartype",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.CarTypeImpl", "bd_cartype", "com.fuze.bcp.bd.domain.CarType"));

        transactionMap.put("so_customer",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.CustomerImpl", "so_customer", "com.fuze.bcp.customer.domain.Customer"));

        transactionMap.put("bd_cashsource",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.CashSourceImpl", "bd_cashsource", "com.fuze.bcp.bd.domain.CashSource"));

        transactionMap.put("bd_dealeremployee",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.DealeremployeeImpl", "bd_dealeremployee", "com.fuze.bcp.bd.domain.DealerEmployee"));

        transactionMap.put("bd_employee",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.EmployeeImpl", "bd_employee", "com.fuze.bcp.bd.domain.Employee"));

        transactionMap.put("bd_guaranteeway",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.GuaranteeWayImpl", "bd_guaranteeway", "com.fuze.bcp.bd.domain.GuaranteeWay"));

        transactionMap.put("bd_province",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.ProvinceImpl", "bd_province", "com.fuze.bcp.bd.domain.Province"));

        transactionMap.put("bd_repaymentway",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.RepaymentwayImpl", "bd_repaymentway", "com.fuze.bcp.bd.domain.RepaymentWay"));

        transactionMap.put("sys_terminaldevice",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.TerminalDeviceImpl", "sys_terminaldevice", "com.fuze.bcp.sys.domain.TerminalDevice"));

        transactionMap.put("sys_terminaldevice_usage",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.DeviceUsageImpl", "sys_terminaldevice_usage", "com.fuze.bcp.sys.domain.DeviceUsage"));

        transactionMap.put("au_loginuser",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.LoginuserImpl", "au_loginuser", "com.fuze.bcp.auth.domain.LoginUser"));
        // lgj
        transactionMap.put("bd_orginfo",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.OrginfoImpl", "bd_orginfo", "com.fuze.bcp.bd.domain.Orginfo"));

        transactionMap.put("bd_fileexpress",
               new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.FileExpressImpl", "bd_fileexpress", "com.fuze.bcp.creditcar.domain.FileExpress"));

        transactionMap.put("bd_creditproduct",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.CreditProductImpl", "bd_creditproduct", "com.fuze.bcp.bd.domain.CreditProduct"));

        transactionMap.put("bd_cashsource_employee",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.CashsourcEemployeeImpl", "bd_cashsource_employee", "com.fuze.bcp.bd.domain.CashSourceEmployee"));
         // hxy

        transactionMap.put("bd_sourcerate",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.SourceRateImpl", "bd_sourcerate", "com.fuze.bcp.bd.domain.SourceRate"));

        transactionMap.put("bd_businesstype",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.BusinesstypeImpl", "bd_businesstype", "com.fuze.bcp.bd.domain.BusinessType"));

        transactionMap.put("bd_cardealer",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.CardealerImpl", "bd_cardealer", "com.fuze.bcp.bd.domain.CarDealer"));

        transactionMap.put("bd_billtype",
                new ParamObject("com.fuze.bcp.dubbo.migration.bdservice.impl.BillTypeImpl", "bd_billtype", "com.fuze.bcp.bd.domain.BillType"));

    }

    public static void main(String[] args) {
//         正式库 慎用
        MongoTemplate source = MongoConnect.getMongoTemplate("139.198.11.30", 38289, "root", "admin", "2017Fuzefenqi998", "fzfq-prod");
        MongoTemplate target = MongoConnect.getMongoTemplate("172.16.2.5", 27017, "mongolive", "admin", "FuzefenqiPa88word", "bcp_v11");
        for (String key : transactionMap.keySet()) {
            ParamObject p = transactionMap.get(key);
            Thread thread = new Thread(() -> {
                Query query = new Query();
                List<BasicDBObject> basicDBObjects = source.find(query, BasicDBObject.class, p.getOldCollectionName());
                for (int i = 0; i < basicDBObjects.size(); i = i + 100) {
                    int index = i;
                    Thread thread1 = new Thread(() -> {
                        for (int j = 0; j < 100 && index + j < basicDBObjects.size(); j++) {
                            BasicDBObject sourceMap = basicDBObjects.get(index + j);
                            MongoBaseEntity entity = null;
                            BDDataMigration d = null;
                            try {
                                Class c1 = Class.forName(p.getNewName());
                                Class c2 = Class.forName(p.getServerName());
                                entity = (MongoBaseEntity) c1.newInstance();
                                d = (BDDataMigration) c2.newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            d.bDDataMigration(sourceMap, entity, source, target);
                            target.save(entity);
                        }
                    });
                    thread1.start();
                }
            });
            thread.start();
        }
    }
}
