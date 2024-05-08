package com.fuze.bcp.file.service;

import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;

/**
 * Created by CJ on 2017/6/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class FileServiceTest {

    @Autowired
    IFileBizService iFileMrgService;

    @Test
    public void testSaveFile() throws FileNotFoundException {
        FileBean fileBean = new FileBean();
        fileBean.setFileName("text.txt");
        fileBean.setContentLength(12L);
        fileBean.setBucketName("abc");
        fileBean.setZoneName("zone");
        fileBean.setObjectname("object");
        iFileMrgService.actSaveFile(fileBean);
    }

    @Test
    public void testGets() {
        ResultBean<FileBean> data = iFileMrgService.actGetFile("5948969a825b4f1ddc544cd5");
        System.out.println(data);
    }

    @Test
    public void testdelete() {
       iFileMrgService.actDeleteFileById("5948969a825b4f1ddc544cd5");
    }

    @Test
    public void testGetFiles(){
        ResultBean<DataPageBean<FileBean>> data = iFileMrgService.actGetFiles(0);
        System.out.println(data);
    }
}
