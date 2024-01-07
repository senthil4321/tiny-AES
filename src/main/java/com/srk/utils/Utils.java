package com.srk.utils;

public class Utils {

	// Private constructor to prevent instantiation of the class
	private Utils() {
		throw new AssertionError("ConstantsUtil class should not be instantiated.");
	}

	public static String bin2hex(int[] intArray) {

		byte[] byteArray = new byte[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
			byteArray[i] = (byte) intArray[i];
		}
		return bin2hex(byteArray);

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

	public static String bin2hex(int intData) {
		byte byteData = (byte) intData;
		return bin2hex(byteData);
	}


	public static String bin2hex(byte bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[2];
		int v = bytes & 0xFF;
		hexChars[0] = hexArray[v >>> 4];
		hexChars[1] = hexArray[v & 0x0F];
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

	public static void printMatrix(int[][] matrix) {
		int rows = matrix.length;
		int columns = matrix[0].length;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				// System.out.printf("%4d", matrix[i][j]);
				System.out.printf("%4s", bin2hex(matrix[i][j]));

			}
			System.out.println();
		}
	}

	public static String int2hex(int i) {
		Integer intObject = Integer.valueOf(i);
		String s = String.format("%08X", intObject);
		return s;
	}
}