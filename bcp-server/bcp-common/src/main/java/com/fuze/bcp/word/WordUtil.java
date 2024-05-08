package com.fuze.bcp.word;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 2017/7/13.
 * Word 生成工具
 */
public class WordUtil {

    public static CustomXWPFDocument generateWord(Map<String, Object> param, InputStream inputStream,Map<String, Integer> wordFontSize){
        CustomXWPFDocument doc=null;
        try {
            OPCPackage pack= OPCPackage.open(inputStream);
            doc=new CustomXWPFDocument(pack);
            if(param!=null&&param.size()>0){
                //处理段落
                List<XWPFParagraph> paragraphList = doc.getParagraphs();
                processParagraphs(paragraphList, param, doc, wordFontSize);
                //处理表格
                Iterator<XWPFTable> it = doc.getTablesIterator();
                while(it.hasNext()){
                    XWPFTable table = it.next();
                    List<XWPFTableRow> rows = table.getRows();
                    for (XWPFTableRow row : rows) {
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {
                            List<XWPFParagraph> paragraphListTable =  cell.getParagraphs();
                            processParagraphs(paragraphListTable, param, doc,wordFontSize);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 处理段落
     * @param paragraphList
     * @param param
     * @param doc
     */
    public static void processParagraphs(List<XWPFParagraph> paragraphList,Map<String, Object> param,CustomXWPFDocument doc, Map<String, Integer> wordFontSize){
        if(paragraphList!=null&&paragraphList.size()>0){
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs=paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text=run.getText(0);
                    if(text!=null){
                        boolean isSetText=false;
                        for (Map.Entry<String, Object> entry : param.entrySet()) {
                            String key=entry.getKey();
                            if(text.indexOf(key)!=-1){
                                isSetText=true;
                                Object value=entry.getValue();

                                if(value instanceof Map){ //图片替换
                                    /*text=text.replace(key, "");
                                    Map pic=(Map) value;
                                    int width=Integer.parseInt(pic.get("width").toString());
                                    int height=Integer.parseInt(pic.get("height").toString());
                                    int picType=getPictureType(pic.get("type").toString());
                                    byte[] byteArray = (byte[]) pic.get("content");
                                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
                                    try {
                                        String relationId = doc.addPictureData(byteInputStream,picType);
                                        doc.createPicture(relationId, width , height,paragraph);
                                    } catch (InvalidFormatException e) {
                                        e.printStackTrace();
                                    }*/
                                } else{
                                    //此处暂时不用
                                    int maxLen = getMaxLength(wordFontSize,key);
                                    if (maxLen != 0){
                                        int len = value.toString().length();
                                        if(len> maxLen){
                                            int fSize  = Double.valueOf(run.getFontSize()*maxLen/len).intValue();
                                            run.setFontSize(fSize);
                                        }
                                    }
                                    text=text.replace(key,value == null ? "" : value.toString());
                                }
                            }
                        }
                        if(isSetText){
                            run.setText(text, 0);
                        }
                    }
                }
            }
        }
    }

    public static int getMaxLength(Map<String, Integer> wordFontSize, String key){
        for (String word : wordFontSize.keySet()){
            if (word.equals(key)){
                return wordFontSize.get(key);
            }
        }
        return 0;
    }

    /**
     * 根据图片类型获取对应的图片类型代码
     * @param picType
     * @return
     */
    public static int getPictureType(String picType){
        int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
        if(picType!=null){
            if(picType.equalsIgnoreCase("png")){
                res=CustomXWPFDocument.PICTURE_TYPE_PNG;
            }else if(picType.equalsIgnoreCase("dib")){
                res = CustomXWPFDocument.PICTURE_TYPE_DIB;
            }else if(picType.equalsIgnoreCase("emf")){
                res = CustomXWPFDocument.PICTURE_TYPE_EMF;
            }else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){
                res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
            }else if(picType.equalsIgnoreCase("wmf")){
                res = CustomXWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    /**
     *
     * @param content       要转换的HTML文档
     * @param wordCode      文档编码
     * @param os             输出流
     * @throws Exception
     */
    public static void htmlStringToWord2(String content, String wordCode, OutputStream os) throws Exception {

        //HTML格式文档 转换为字节数组
        InputStream is = new ByteArrayInputStream(content.getBytes(wordCode));
        inputStreamToWord(is, os);
    }

    /**
     * 把is写入到对应的word输出流os中
     * 不考虑异常的捕获，直接抛出
     * @param is
     * @param os
     * @throws IOException
     */
    private static void inputStreamToWord(InputStream is, OutputStream os) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem();
        //对应于org.apache.poi.hdf.extractor.WordDocument
        fs.createDocument(is, "WordDocument");
        fs.writeFilesystem(os);
        os.close();
        is.close();
    }

    /**
     * 把输入流里面的内容以 wordCode 编码当文本取出。
     * 不考虑异常，直接抛出
     * @param wordCode    UTF-8
     * @param ises         输入流
     * @return
     * @throws IOException
     */
    private String getContent(String wordCode, InputStream... ises) throws IOException {
        if (ises != null) {
            StringBuilder result = new StringBuilder();
            BufferedReader br;
            String line;
            for (InputStream is : ises) {
                br = new BufferedReader(new InputStreamReader(is, wordCode));
                while ((line=br.readLine()) != null) {
                    result.append(line);
                }
            }
            return result.toString();
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
//        Map<String, Object> param=new HashMap<String, Object>();
//        param.put("creditAmount", "1");
//        param.put("CREDITAMOUNT", "2");
//        param.put("REALPRICE", "3");
//        param.put("realPirce", "4");
//        CustomXWPFDocument doc=WordUtil.generateWord(param, "C:\\Users\\jinglu\\Desktop\\思路.docx");
//        FileOutputStream fopts = new FileOutputStream("C:\\Users\\jinglu\\Desktop\\思路1.docx");
//        doc.write(fopts);
//        fopts.close();
        String content = "<!DOCTYPE html PUBLIC \\\"-//W3C//DTD XHTML 1.0 Strict//EN\\\"  \\\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\\\"><html xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><head>    <title></title>    <meta http-equiv=\\\"Content-Type\\\" content=\\\"text/html; charset=utf-8\\\" />    <style type=\\\"text/css\\\">    </style></head>    <body align=\\\"center\\\">        <p style=\\\"font-size: 1em;\\\">关于客户直子的情况说明</p>        <p style=\\\"font-size: 1em;\\\">            <ol>                <li>                    购车分期情况<br/>                    客户拟购买陆风 陆风X8 2014 款 2.5T 手动 两驱探索版豪华型，该车成交价格为人民币12.98万元（新车指导价12.98万元），客户首付2.6万元，申请分期贷款10.38万元，占比分别是20.03%和79.97%，申请期数是12期，手续费0.415万元，申请分期合计12.98万元。                </li>                <li>                    基本情况<br/>                    客户直子，性别男，32岁（1985-12-01年生人）。目前在北京超级帮有限公司工作，担任开发。企业经营情况，如需要请补充说明。                </li>                <li>                    资产情况<br/>                    如需要请补充说明。                </li>                <li>                    负债情况<br/>                    如需要请补充说明。                </li>                <li>                    指标情况<br/>                     购车指标是用申请人获得。                </li>                <li>                    还款能力分析<br/>                    如需要请补充说明。                </li>                <li>                    社保情况<br/>                    直子有北京社保卡，缴存单位与申请单位一致，缴存基数2,000元;                </li>            </ol>        </p>    </body></html>";
        htmlStringToWord2(content,"GBK",null);
    }
}
