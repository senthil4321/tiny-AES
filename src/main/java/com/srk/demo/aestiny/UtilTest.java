package com.srk.demo.aestiny;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UtilTest {
	private static Logger logger = LogManager.getLogger(UtilTest.class);
	public static void main(String[] args) {
		byte[] data = new byte[] { (byte) 0xFF };
		String data1 = bin2hex(data);
		logger.info("logger {}", data1);
		
		
		data1 = bin2hex(hex2bin("FFEEAA"));
		logger.info("logger {}", data1);

	}

	public static String bin2hex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] hex2bin(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
