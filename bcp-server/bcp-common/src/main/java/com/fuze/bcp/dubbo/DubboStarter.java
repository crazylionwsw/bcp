package com.fuze.bcp.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by CJ on 2017/8/24.
 */
public class DubboStarter {

    static ClassPathXmlApplicationContext context;

    static Logger logger = LoggerFactory.getLogger(DubboStarter.class);

    public static void start(String str, ApplicationContext applicationContext, String str2) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    context = new ClassPathXmlApplicationContext(new String[]{str}, applicationContext);
                    context.start();
                    synchronized (DubboStarter.class) {
                        logger.info("Application ["+ str2 +"] Started Success!");
                        while (true) {
                            try {
                                DubboStarter.class.wait();
                            } catch (Exception t) {
                                logger.error("致命异常", t);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("服务器致命异常", e);
                }
            }
        });
        try {
            thread.start();
        }catch (Exception e) {
            logger.error("服务器致命异常", e);
        }
    }

}
