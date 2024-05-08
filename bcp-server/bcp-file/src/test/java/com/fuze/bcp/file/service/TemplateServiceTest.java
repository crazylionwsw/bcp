package com.fuze.bcp.file.service;

import com.fuze.bcp.api.file.bean.EmailObjectBean;
import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.bean.PushObjectBean;
import com.fuze.bcp.api.file.bean.TemplateObjectBean;
import com.fuze.bcp.api.file.service.ITemplateBizService;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.pdf.PdfHelper;
import com.lowagie.text.DocumentException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/8/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath*:/dubbo-consumer.xml")
public class TemplateServiceTest {

    @Autowired
    ITemplateBizService iTemplateBizService;

    @Test
    public void testSaveTemplate() {
        TemplateObjectBean templateObjectBean = new TemplateObjectBean();
        templateObjectBean.setCode("fzpidaihan.ftl");
        FileBean fileBean = new FileBean();
        fileBean.setId("59968dbd1c58cf12bc34a6e2");
        templateObjectBean.setFileBean(fileBean);
        templateObjectBean.setName("测试模板");
        List<String> list = new ArrayList<>();
        list.add("au_loginuser__username");
        templateObjectBean.setMetaDatas(list);
        iTemplateBizService.actSaveTemplate(templateObjectBean);

    }

    @Test
    public void testGetOne() {
        ResultBean data = iTemplateBizService.actGetTemplate("59968e4c1c58cf12bc34a6e3");
        System.out.println(data);
    }

    @Test
    public void testSaveEmailObject() {
        EmailObjectBean emailObjectBean = new EmailObjectBean();
        emailObjectBean.setName("电子报批邮件");
        emailObjectBean.setSubject("title");
        List list = new ArrayList();
        list.add("001");
//        emailObjectBean.setContractCodes(list);
        emailObjectBean.setContentTemplateId("59b8c4747bb238bc38fe11a4");
        iTemplateBizService.actSaveEmailObject(emailObjectBean);

    }

    @Test
    public void testCreateFileByTemplate() {
        String a = iTemplateBizService.actCreateFileByTemplate("59968e4c1c58cf12bc34a6e3", "59882fcd6d83f105c8134532", "pdf").getD();
        System.out.println(a);
    }

    @Test
    public void testTs() {
        TemplateObjectBean t1 = new TemplateObjectBean();
        t1.setName("测试数据1");
        TemplateObjectBean t2 = new TemplateObjectBean();
        t2.setName("测试数据2");
        TemplateObjectBean t3 = new TemplateObjectBean();
        t3.setName("测试数据3");
        List<TemplateObjectBean> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        list.forEach(o -> o.setName("ccccccccccccc"));
        System.out.println(list);
    }

    @Test
    public void testSavePushTemplate(){
        PushObjectBean pushObjectBean = new PushObjectBean();
        pushObjectBean.setSubject("测试数据啦");
        pushObjectBean.setContentTemplateId("59bb43a6ba2a5d0da5193db9");
        iTemplateBizService.actSavePushObject(pushObjectBean);
    }

    public static void main(String[] args) throws IOException, TemplateException, DocumentException {
        StringTemplateLoader stl = new StringTemplateLoader();
        String template = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <style type=\"text/css\">\n" +
                "        body {\n" +
                "            margin-left: 45px;\n" +
                "            margin-right: 45px;\n" +
                "            font-family: SimSun;\n" +
                "            font-size: 10px;\n" +
                "        }\n" +
                "\n" +
                "        table {\n" +
                "            margin: auto;\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            border: 2px solid #444444;\n" +
                "        }\n" +
                "\n" +
                "        th,td,tr {\n" +
                "            border: 1px solid #444444;\n" +
                "            font-size: 16px;\n" +
                "            margin-left: 3px;\n" +
                "            width: 25%;\n" +
                "            height: 40px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .tab{\n" +
                "            page-break-after: always;\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        .mcContent {\n" +
                "            line-height: 180%;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .logo {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .title {\n" +
                "            text-align: center;\n" +
                "            font-weight: bold;\n" +
                "            font-size: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .notes {\n" +
                "            font-weight: normal;\n" +
                "            margin-left: 5px;\n" +
                "            margin-right: 5px;\n" +
                "            line-height: 18px;\n" +
                "        }\n" +
                "\n" +
                "        .text_content {\n" +
                "            margin-left: 5px;\n" +
                "            margin-right: 5px;\n" +
                "            line-height: 18px;\n" +
                "        }\n" +
                "\n" +
                "        .sum_insured_first_row {\n" +
                "            width: 20%;\n" +
                "        }\n" +
                "\n" +
                "        .sum_insured_span {\n" +
                "            font-size: 10px;\n" +
                "        }\n" +
                "\n" +
                "        .special_agreements_div {\n" +
                "            page-break-before: always;\n" +
                "            font-size: 14px;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .special_agreements_div .special_agreements {\n" +
                "            font-size: 18px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "\n" +
                "        .title_right {\n" +
                "            width: 100%;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "\n" +
                "        .title_right p {\n" +
                "            text-align: left;\n" +
                "            margin: 0;\n" +
                "            margin-left: 50%;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        @page {\n" +
                "            size:5.5in 10in;\n" +
                "        @\n" +
                "        bottom-center\n" +
                "        {\n" +
                "            content\n" +
                "            :\n" +
                "                    \"page \"\n" +
                "                    counter(\n" +
                "                            page\n" +
                "                    )\n" +
                "                    \" of  \"\n" +
                "                    counter(\n" +
                "                            pages\n" +
                "                    );\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "\n" +
                "        .signature {\n" +
                "            margin: 0 auto;\n" +
                "            clear: both;\n" +
                "            font-size: 16px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "\n" +
                "        .signature_table {\n" +
                "            /* \tfont-size: 16px; */\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body align=\"center\" >\n" +
                "测试数据<img src=\"${imagePath}\" />测试数据\n" +
                "</body>\n" +
                "</html>";

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
        configuration.setTemplateLoader(stl);
        Template temp = configuration.getTemplate("hello");
        StringWriter writer = new StringWriter();
        Map map = new HashMap();
        map.put("imagePath", "http://localhost:8083/json/file/download/59ae11ec5832de107ca9356c");
        temp.process(map, writer);
        String content = writer.toString();
        System.out.println(content);
        File file = new File("test.pdf");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ITextRenderer render = PdfHelper.getRender("");
        render.setDocumentFromString(content);
        render.layout();
        render.createPDF(fileOutputStream);
        render.finishPDF();
        fileOutputStream.close();

    }




}
