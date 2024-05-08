package com.fuze.bcp.pdf;

import com.lowagie.text.DocumentException;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * PDF生成工具类
 *
 * @author Goofy <a href="http://www.xdemo.org">http://www.xdemo.org</a>
 */
public class PdfUtils {
    private static final Logger logger = LoggerFactory.getLogger(PdfUtils.class);

    /**
     * 生成PDF到流
     *
     * @param ftlName      模板文件名（不含路径）
     * @param data         数据
     * @param outputStream 目标流
     * @throws Exception
     */
    public static void generateToFile(String ftlName, Map<String, Object> data, OutputStream outputStream) throws Exception {
        data.put("imagePath", PdfHelper.getImagePath());
        String html = PdfHelper.getTemplate(ftlName, data, PdfHelper.getFreemarkerFileConfig());
        //logger.info("生成的ＨＴＭＬ:"+html);
        ITextRenderer render = PdfHelper.getRender("");
        render.setDocumentFromString(html);
        //html中如果有图片，图片的路径则使用这里设置的路径的相对路径，这个是作为根路径
        render.layout();
        render.createPDF(outputStream);
        render.finishPDF();
    }

    /**
     * 将 pdf 转换为jpg 、 png
     *
     * @param io   pdf文件数据的输入流
     * @param baos 图片文件数据的输出流
     * @throws IOException
     */
    public static void pdfToJpg(InputStream io, ByteArrayOutputStream baos) throws IOException {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        PDFFile pdffile = null;
        BufferedImage img = null;

        try {
            bis = new BufferedInputStream(io);

            byte[] buff = new byte[2048];
            ByteBuffer buf = ByteBuffer.allocate(1024000);
            int bytesRead = 0;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                buf.put(buff);
            }

            //      将二进制文件流  转换为 pdffile
            pdffile = new PDFFile(buf);
            for (int i = 1; i <= pdffile.getNumPages(); i++) {
                // draw the first page to an image
                PDFPage page = pdffile.getPage(i);

                int width = (int) page.getBBox().getWidth();
                int height = (int) page.getBBox().getHeight();

                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = img.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // do the actual drawing
                PDFRenderer renderer = new PDFRenderer(page, g2, new Rectangle(0, 0, width, 550), null, Color.WHITE);

                try {
                    page.waitForFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                renderer.run();
                g2.dispose();
                //      将 图片缓冲区的数据 写到输出流中去
                boolean png = ImageIO.write(img, "png", baos);
                logger.info("ImageIO写入", png);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
            } catch (Exception ex) {
            }
            try {
                if (bos != null)
                    bos.close();
            } catch (Exception ex) {
            }
        }
    }

//    /**
//     * 生成PDF到输出流中（ServletOutputStream用于下载PDF）
//     * @param ftlPath ftl模板文件的路径（不含文件名）
//     * @param ftlName ftl模板文件的名称（不含路径）
//     * @param imageDiskPath 如果PDF中要求图片，那么需要传入图片所在位置的磁盘路径
//     * @param data 输入到FTL中的数据
//     * @param response HttpServletResponse
//     * @return
//     * @throws TemplateNotFoundException
//     * @throws MalformedTemplateNameException
//     * @throws ParseException
//     * @throws IOException
//     * @throws TemplateException
//     * @throws DocumentException
//     */
//	public static OutputStream generateToServletOutputStream(String ftlPath,String ftlName,String imageDiskPath,Object data,HttpServletResponse response) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException, DocumentException{
//		String html= PdfHelper.getPdfContent(ftlPath, ftlName, data);
//		OutputStream out = null;
//		ITextRenderer render = null;
//		out = response.getOutputStream();
//		render = PdfHelper.getRender(ftlPath);
//		render.setDocumentFromString(html);
//		if(imageDiskPath!=null&&!imageDiskPath.equals("")){
//			//html中如果有图片，图片的路径则使用这里设置的路径的相对路径，这个是作为根路径
//			render.getSharedContext().setBaseURL("file:/"+imageDiskPath);
//		}
//		render.layout();
//		render.createPDF(out);
//		render.finishPDF();
//		render = null;
//		return out;
//	}

}
