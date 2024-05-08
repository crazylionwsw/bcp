package com.fuze.bcp.utils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class MD5Util {

	/**
	 * 默认的密码字符串组合，apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			System.err.println(MD5Util.class.getName()
					+ "初始化失败，MessageDigest不支持MD5Util。");
			nsaex.printStackTrace();
		}
	}


	public static String getFileMD5String(String filename) throws IOException {
		return getFileMD5(filename);
	}

	private static String getFileMD5String(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
				file.length());
		messagedigest.update(byteBuffer);
		return bufferToHex(messagedigest.digest());
	}

	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}

	/**
	 * 获取文件流的MD5计算结果
	 * @param inputStream
	 * @return
	 */
	public static String getFileMD5(InputStream inputStream) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[1024*1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			return new String(Hex.encodeHex(md.digest()));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取文件的MD5计算结果
	 * @param fileName
	 * @return
	 */
	public static String getFileMD5(String fileName) {
	        FileInputStream fis = null;
	        try {
	            fis = new FileInputStream(new File(fileName));
	            return getFileMD5(fis);
	        } catch (FileNotFoundException e) {
	            return null;
	        } catch (Exception e) {
	            return null;
	        } finally {
	            try {
	                if (fis != null) fis.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	}

	public static void main(String[] strs){
		try{
			long l1 = System.currentTimeMillis();
			String str1 = MD5Util.getFileMD5("/Volumes/OTHER/temp/test0.jpg");
			long l2 = System.currentTimeMillis();
			System.out.println("MD5:"+str1);
			String str2 = MD5Util.getFileMD5String("/Volumes/OTHER/temp/test1.jpg");
			long l3 = System.currentTimeMillis();
			System.out.println("MD5:"+str2);
			System.out.println(l3-l1);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	} 

}
