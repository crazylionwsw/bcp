package com.fuze.bcp.dubbo.migration.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;

import java.util.*;

/**
 * Created by CJ on 2017/8/22.
 */
public class MongoConnect {

    private static Map<String, MongoClient> mongoClients = new HashMap<>();

    public static MongoClient getClient(String userName) {
        if (mongoClients == null) {
            mongoClients = new HashMap<>();
        }
        if (mongoClients.containsKey(userName)) {
            return mongoClients.get(userName);
        }
        return null;
    }

    public static synchronized Map<String, MongoClient> addtClient(String ip, Integer port, String userName, String source, String password) {
        if (mongoClients == null) {
            mongoClients = new HashMap<>();
        }
        try {
            if(!mongoClients.containsKey(userName)){
                MongoCredential credential = MongoCredential.createScramSha1Credential(userName, source, password.toCharArray());
                List<MongoCredential> credentialList = new ArrayList<MongoCredential>();
                credentialList.add(credential);
                MongoClient mongoClient = new MongoClient(new ServerAddress(ip, port), Arrays.asList(credential));
                mongoClients.put(userName, mongoClient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mongoClients;
    }

    public static MongoTemplate getMongoTemplate(String ip, Integer port, String userName, String source, String password, String dataBaseName) {
        addtClient(ip, port, userName, source, password);
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(getClient(userName), dataBaseName);
//        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
        return mongoTemplate;
    }


    public static void main(String[] args) {
        bathUpdate();
    }

    public static void bathUpdate() {
        MongoTemplate mongoTemplateSource = getMongoTemplate("139.198.3.138", 27901, "fuzedev", "admin", "FuzefenqiDev11", "bcp-dev");
        MongoTemplate mongoTemplateTarget = getMongoTemplate("test.fuzefenqi.com", 28018, "fuzetest", "admin", "FuzefenqiV11", "bcp-test");
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Integer n = d.migration(mongoTemplate);
//                System.out.println("受影响的行数：" + n);
//            }
//        });
//        thread.start();
    }


}
