package com.fuze.bcp.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lily on 2017/5/3.
 */
public class ExcelUtil {
    /**
     * 功能: 导出为Excel工作簿
     * 参数: sheetName[工作簿中的一张工作表的名称]
     * 参数: titleName[表格的标题名称]
     * 参数: headers[表格每一列的列名]
     * 参数: dataSet[要导出的数据源]
     * 参数: resultUrl[导出的excel文件地址]
     * 参数: pattern[时间类型数据的格式]
     */
    public static String exportExcel(String sheetName, String titleName, String[] headers, Collection<?> dataSet, String pattern,OutputStream outputStream) {

        String msg = doExportExcel(sheetName, titleName, headers, dataSet, pattern,outputStream);
        return msg;

    }

    /**
     * 导出excel
     * @param sheetName
     * @param titleName
     * @param headers
     * @param dataSet
     * @param outputStream
     * @return
     */
    public static String doExportExcel(String sheetName, String titleName, String[] headers,int[] widths, List<List<Object>> dataSet, OutputStream outputStream) {

        int linenum = 0;
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个工作表
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置工作表默认列宽度为20个字节
        sheet.setDefaultColumnWidth(20);
        //在工作表中合并首行并居中
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,headers.length-1));

        // 创建[标题]样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        // 设置[标题]样式
        //titleStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        //titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //创建[标题]字体
        HSSFFont titleFont = workbook.createFont();
        //设置[标题]字体
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setFontHeightInPoints((short) 24);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把[标题字体]应用到[标题样式]
        titleStyle.setFont(titleFont);

        // 创建[列首]样式
        HSSFCellStyle headersStyle = workbook.createCellStyle();
        // 设置[列首]样式
        headersStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headersStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headersStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headersStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headersStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //创建[列首]字体
        HSSFFont headersFont = workbook.createFont();
        //设置[列首]字体
        headersFont.setFontName("仿宋");
        headersFont.setColor(HSSFColor.BLACK.index);
        headersFont.setFontHeightInPoints((short) 12);
        headersFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把[列首字体]应用到[列首样式]
        headersStyle.setFont(headersFont);

        // 创建[表中数据]样式
        HSSFCellStyle dataSetStyle = workbook.createCellStyle();
        // 设置[表中数据]样式
        dataSetStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataSetStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataSetStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataSetStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataSetStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        dataSetStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 创建[表中数据]字体
        HSSFFont dataSetFont = workbook.createFont();
        // 设置[表中数据]字体
        dataSetFont.setFontName("仿宋");
        dataSetFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        dataSetFont.setColor(HSSFColor.AUTOMATIC.index);
        // 把[表中数据字体]应用到[表中数据样式]
        dataSetStyle.setFont(dataSetFont);

        //创建标题行-增加样式-赋值
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(titleStyle);
        titleCell.setCellValue(titleName);

        // 创建列首-增加样式-赋值
        HSSFRow row = sheet.createRow(1);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("序号");
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i+1);
            cell.setCellStyle(headersStyle);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        sheet.setColumnWidth(0,5*256);
        for(int i=0; i<widths.length; i++){
            sheet.setColumnWidth(i+1,widths[i]*256);
        }

        // 创建表中数据行-增加样式-赋值
        Iterator<List<Object>> it = dataSet.iterator();
        int index = 1;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            List t = it.next();
            cell = row.createCell(0);
            cell.setCellStyle(dataSetStyle);
            cell.setCellValue(String.valueOf(index-1));

            for (int i = 0; i < t.size(); i++) {
                int colIndex = i+1;
                cell = row.createCell(colIndex);
                cell.setCellStyle(dataSetStyle);
                    setCellValue(cell,t.get(i));
            }
        }
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return linenum+"条数据下载已完成!";
    }

    public static void setCellValue(HSSFCell cell, Object objVal){
        if(objVal instanceof String){
            cell.setCellValue((String)objVal);
        }else if(objVal instanceof Double){
            cell.setCellValue((Double)objVal);
        }else if(objVal instanceof Date){
            cell.setCellValue((Date)objVal);
        }else if(objVal instanceof Boolean){
            cell.setCellValue((Boolean)objVal);
        }else {
            if(objVal == null){
                objVal = "";
            }
            cell.setCellValue(objVal.toString());
        }
    }

    /**
     * 功能:真正实现导出
     */
    private static String doExportExcel(String sheetName,String titleName,String[] headers,Collection<?> dataSet,String pattern,OutputStream outputStream) {

        int linenum = 0;
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个工作表
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置工作表默认列宽度为20个字节
        sheet.setDefaultColumnWidth(20);
        //在工作表中合并首行并居中
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,headers.length-1));

        // 创建[标题]样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        // 设置[标题]样式
        //titleStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        //titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //创建[标题]字体
        HSSFFont titleFont = workbook.createFont();
        //设置[标题]字体
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setFontHeightInPoints((short) 24);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把[标题字体]应用到[标题样式]
        titleStyle.setFont(titleFont);

        // 创建[列首]样式
        HSSFCellStyle headersStyle = workbook.createCellStyle();
        // 设置[列首]样式
        headersStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headersStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headersStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headersStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headersStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //创建[列首]字体
        HSSFFont headersFont = workbook.createFont();
        //设置[列首]字体
        headersFont.setColor(HSSFColor.BLACK.index);
        headersFont.setFontHeightInPoints((short) 12);
        headersFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把[列首字体]应用到[列首样式]
        headersStyle.setFont(headersFont);

        // 创建[表中数据]样式
        HSSFCellStyle dataSetStyle = workbook.createCellStyle();
        // 设置[表中数据]样式
        dataSetStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataSetStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataSetStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataSetStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataSetStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        dataSetStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 创建[表中数据]字体
        HSSFFont dataSetFont = workbook.createFont();
        // 设置[表中数据]字体
        dataSetFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        dataSetFont.setColor(HSSFColor.BLACK.index);
        // 把[表中数据字体]应用到[表中数据样式]
        dataSetStyle.setFont(dataSetFont);

        //创建标题行-增加样式-赋值
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(titleStyle);
        titleCell.setCellValue(titleName);

        // 创建列首-增加样式-赋值
        HSSFRow row = sheet.createRow(1);
        for (short i = 0; i < headers.length; i++) {

            @SuppressWarnings("deprecation")
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(headersStyle);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);

        }

        // 创建表中数据行-增加样式-赋值
        Iterator<?> it = dataSet.iterator();
        int index = 1;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            Object t = it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            linenum = index - 1;
            for (short i = 0; i < fields.length; i++) {
                @SuppressWarnings("deprecation")
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(dataSetStyle);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get"+ fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
                try {
                    @SuppressWarnings("rawtypes")
                    Class tCls = t.getClass();
                    @SuppressWarnings("unchecked")
                    Method getMethod = tCls.getMethod(getMethodName,new Class[] {});
                    Object value = getMethod.invoke(t, new Object[] {});
                    if(value == null){
                        value = "";
                    }

                    // 如果是时间类型,按照格式转换
                    String textValue = null;
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    }

                    // 利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        cell.setCellValue(textValue);
                        //判断超过15位的做文本处理
                        /*if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            // 不是数字做普通处理

                        }*/
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        //OutputStream out=null;
        try {
            //out = new FileOutputStream(resultUrl);
            //BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return linenum+"条数据下载已完成!";
    }

}
