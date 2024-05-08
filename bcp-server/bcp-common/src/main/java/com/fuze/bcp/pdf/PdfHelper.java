package com.fuze.bcp.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Goofy <a href="http://www.xdemo.org">http://www.xdemo.org</a>
 */
@SuppressWarnings("deprecation")
public class PdfHelper {

    private static final Logger logger = LoggerFactory.getLogger(PdfHelper.class);

    public static ITextRenderer getRender(String filePath) throws DocumentException, IOException {

        ITextRenderer render = new ITextRenderer();
        //添加字体，以支持中文
        render.getFontResolver().addFont(filePath + File.separator + "ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        render.getFontResolver().addFont(filePath + File.separator + "SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        return render;
    }

//    获取要写入PDF的内容
//    public static String getPdfContent(String ftlName, Object o) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
//        return useTemplate(ftlName, o, getFreemarkerConfig());
//    }

    //使用freemarker得到模板内容
    public static String getTemplate(String ftlName, Map o, Configuration c) throws IOException, TemplateException {
        String source = null;
        Template tpl = c.getTemplate(ftlName);
        tpl.setEncoding("UTF-8");
        StringWriter writer = new StringWriter();
        tpl.process(o, writer);
        writer.flush();
        source = writer.toString();
        return source;
    }

    /**
     * 获取Freemarker配置
     *
     * @return
     * @throws IOException
     */
    public static Configuration getFreemarkerFileConfig() throws IOException {
        Configuration config = new Configuration();
        String path = PdfHelper.getPath();
        config.setDirectoryForTemplateLoading(new File(path));
        config.setEncoding(Locale.CHINA, "utf-8");
        return config;
    }

    public static void main(String[] args) throws IOException, TemplateException {
        Configuration c = getFreemarkerFileConfig();
        Template tpl = c.getTemplate("fzpidaihan.ftl");
        StringWriter writer = new StringWriter();
        Map map = new HashMap<>();
        map.put("testData", "测试数据");
        tpl.process(map, writer);
        writer.flush();
        String source = writer.toString();
        System.out.println(source);
    }

    /**
     * 获取类路径
     *
     * @return
     */
    public static String getPath() {
        return PdfHelper.class.getResource("/").getPath() + "tpl";
    }

    public static String getImagePath() {
        return PdfHelper.getPath() + "/images";
    }

}