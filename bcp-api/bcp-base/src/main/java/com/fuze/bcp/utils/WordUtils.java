package com.fuze.bcp.utils;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;

/**
 * Created by jinglu on 2017/12/27.
 */
public class WordUtils {

    /**
     *
     * @param content       要转换的HTML文档
     * @param wordCode      文档编码
     * @param saveFileName  保存的文件名称，包含路径
     * @throws Exception
     */
    public void htmlToWord2(String content,String wordCode,String saveFileName) throws Exception {

        //拼一个标准的HTML格式文档
        InputStream is = new ByteArrayInputStream(content.getBytes(wordCode));
        OutputStream os = new FileOutputStream(saveFileName);
        this.inputStreamToWord(is, os);
    }

    /**
     * 把is写入到对应的word输出流os中
     * 不考虑异常的捕获，直接抛出
     * @param is
     * @param os
     * @throws IOException
     */
    private void inputStreamToWord(InputStream is, OutputStream os) throws IOException {
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
}
