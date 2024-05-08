package com.fuze.bcp.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式化工具类
 */
public class FormatUtils {

    private static String[] num = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
    // 10进制单位
    private static String[] dw = {"","拾","佰","仟"};
    // 1000进制单位
    private static String[] dw2 = {"","万","亿","兆","京","垓","杼","穰","溝","澗","正","載","極","恆河沙","阿僧祇","那由他","不可思議","无量","大数"};
    private static String[] dw1 = {"角","分"};


    /**
     * 格式化小数位
     *
     * @param dl
     * @param digit
     * @return
     */
    public static double formatDigit(double dl, int digit) {
        DecimalFormat df1 = new DecimalFormat("#0.0");
        return Double.parseDouble(df1.format(dl));
    }

    /**
     * 保留两位小数位
     * @param dl
     * @return
     */
    public static double formatDigitTwo(double dl) {
        DecimalFormat df1 = new DecimalFormat("#.00");
        return Double.parseDouble(df1.format(dl));
    }

    /**
     * 格式化整数位
     * @param dl
     * @return
     */
    public static int formatInteger(double dl){
        return Double.valueOf(dl).intValue();
    }



    /**
     * 判断字符串是否是email格式
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 将float类型的数据大小，转换为常见的大小，如：MB（即兆）。
     *
     * @param size float类型
     * @return 返回的字符串是以B、KB、MB、GB、TB为单位的大小表示
     */
    public static String formatSize(float size) {
        long kb = 1024L;
        long mb = kb * 1024L;
        long gb = mb * 1024L;
        long tb = gb * 1024L;

        String result = "0 B";
        //不去转换负数，遇到时将提示出错
        if (size < 0) {
            return "size error";
        }
        //对于超出float表示范围，则提示数据太大
        if (size > Float.MAX_VALUE) {
            return "size too large";
        }
        //对数据大小从B到TB分区间处理，优先以最小的单位表示
        if (size < kb) {
            result = String.format("%d B", (int) size);
        } else if (size < mb) {
            result = String.format("%.2f KB", size / kb);
        } else if (size < gb) {
            result = String.format("%.2f MB", size / mb);
        } else if (size < tb) {
            result = String.format("%.2f GB", size / gb);
        } else {
            //对TB级的数据大小，精确到小数点后第三位（即GB）
            result = String.format("%.3f TB", size / tb);
        }
        return result;
    }

    /**
     * 当使用ajax get方式提交后，再把文本回显为原字符串
     *
     * @param str 由数字编码组成的字符串，且以空格分隔
     * @return 以unicode编码的字符串
     */
    public static String unicodeToString(String str) {
        if (str == null || "".equals(str)) return "";

        StringBuffer buf = new StringBuffer("");
        String[] ss = str.split(" ");
        for (int i = 0; i < ss.length; i++) {
            char c = (char) Integer.parseInt(ss[i].trim());
            buf.append(c);
        }
        String newStr = buf.toString();
        return newStr;
    }


    /**
     * 根据文件名获取文件的后缀
     *
     * @param fileName
     * @return fileName's suffix
     */
    public static String getFileSuffix(String fileName) {
        String suffix = "";
        if (fileName == null) {
            return suffix;
        }
        //以"." 作为识别后缀的标识
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex == -1) {
            return suffix;
        }
        //取"." 后面的字符串作为后缀
        suffix = fileName.substring(suffixIndex + 1);
        return suffix;
    }


    /**
     * 随机生成6位含有字母和数字的随机字符串
     *
     * @param length
     * @return
     */
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "num" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 随机生成手机验证码
     *
     * @return
     */
    public static char[] getIdentifyingCode(int length) {
        String chars = "0123456789";
        char[] rands = new char[length];
        for (int i = 0; i < length; i++) {
            int rand = (int) (Math.random() * 10);
            rands[i] = chars.charAt(rand);
        }
        return rands;

    }

    public static Boolean getBooleanByInt(Integer integer) {
        if (integer != null && integer.intValue() == 1) {
            return new Boolean(true);
        }
        return new Boolean(false);
    }

    public static Integer getIntByBoolean(Boolean boo) {
        if (boo != null && boo) {
            return 1;
        }
        return 0;
    }


    public static  String parse(String str) {
        // 判断是否为合格数字
        if(str.matches("^\\d+(\\.\\d+)?\\d*$")) {
            String integer = "";
            String decimal = "";
            // 拆分成整数和小数部分
            int pos = str.indexOf(".");
            if(pos >=0 ) {
                integer = str.substring(0,pos);
                decimal = str.substring(pos+1);
                decimal = decimal.replaceAll("(.*?)(0+)$", "$1");

            } else {
                integer = str;
            }
            integer = integer.replaceAll("^(0+)(.*)", "$2");
            if(integer.isEmpty() && !decimal.isEmpty()) {
                integer = "0";
            }
            // 解析整数字符串
            String intStr =  parseIntger(integer);
            // 解析小数字符串
            String decimalStr = parseDecimal(decimal,!intStr.isEmpty());
            if(!intStr.isEmpty()) {
                if(integer.endsWith("0") && !decimal.isEmpty() && !decimal.startsWith("0")){
                    intStr+="零";
                }
            }
            return intStr;
        }
        throw new IllegalArgumentException(str);
    }

    private static String parseIntger(String str) {
        if(null == str || str.isEmpty()) {
            return "";
        }
        // 分割字符串为四个字符一组的字符串二维数组
        char[][] nums = spit(str.toCharArray());
        StringBuilder s = new StringBuilder();
        // 上一组数字中最后一个字符是否为0 (方便拼接 “零” )
        boolean lastZero = false;
        for(int j=0;j<nums.length;j++) {
            char[] chs = nums[j];
            // 整组数字中是不是全为 0 (方便拼接 千进制单位 )
            boolean allZero = true;
            // 以下循环将四位字符解析为 10进制单位中的数
            for(int i=0,len=chs.length;i<len;i++) {
                int value = chs[i] - '0';
                // 当前数字大于0或者下一个数字不是0
                if(value > 0 || (i < (len-1) && (chs[i+1] - '0') > 0)) {
                    if(lastZero) {
                        // 如果上一次解析的时候最有一个数字为0，并且当前数字不为0的情况则在前面先拼接 “零”
                        if(value > 0) {
                            s.append(num[0]);
                        }
                        lastZero = false;
                    }
                    s.append(num[value]);
                    allZero = false;
                }
                if(value > 0) { // 不是零的情况拼接单位
                    s.append(dw[chs.length - i -1]);
                }
            }
            lastZero = chs[chs.length-1] == '0';
            if(!allZero) { // 如果当前解析的四个字符不是全为0 则拼接千进制单位
                s.append(dw2[nums.length - j - 1]);
            }
        }

        return s.toString();
    }

    private static char[][] spit(char[] chs) {
        int tmpSize = chs.length / 4;
        int size = chs.length %  4 == 0?tmpSize:tmpSize+1;
        char[][] nums = new char[size][];
        if(1 == size) {
            nums[0] = chs;
        } else {
            for(int i=chs.length;i>0;i-=4) {
                if(i < 4) {
                    nums[--size] = Arrays.copyOfRange(chs, 0, i);
                } else {
                    nums[--size] = Arrays.copyOfRange(chs, i-4, i);
                }
            }
        }
        return nums;
    }

    private static String parseDecimal(String str,boolean existsInt) {
        if( null == str || str.isEmpty()) {
            return "";
        }
        if(str.length() > dw1.length) {
            str = str.substring(0,dw1.length);
        }
        char[] chs = str.toCharArray();
        StringBuilder s = new StringBuilder();

        for(int i=0,len=chs.length;i<len;i++) {
            int value = chs[i] - '0';
            if(value != 0 || (i < (len-1) && (chs[i+1] - '0') > 0)) {
                if(existsInt || value > 0) {
                    s.append(num[value]);
                }
            }
            if(value > 0) {
                s.append(dw1[i]);
            }
        }
        return s.toString();
    }
}
