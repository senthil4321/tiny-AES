package com.srk.demo.aestiny;

import static com.srk.utils.Utils.bin2hex;
import static com.srk.utils.Utils.byteArray2Int;
import static com.srk.utils.Utils.hex2bin;
import static com.srk.utils.Utils.int2ByteArray;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SrkAES {
	private static Logger logger = LogManager.getLogger(SrkAES.class);

	private static int[] sBox = new int[] {
			// 0 1 2 3 4 5 6 7 8 9 A B C D E F
			0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76, 0xca, 0x82,
			0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0, 0xb7, 0xfd, 0x93, 0x26,
			0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15, 0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96,
			0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75, 0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0,
			0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84, 0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb,
			0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf, 0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f,
			0x50, 0x3c, 0x9f, 0xa8, 0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff,
			0xf3, 0xd2, 0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
			0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb, 0xe0, 0x32,
			0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79, 0xe7, 0xc8, 0x37, 0x6d,
			0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08, 0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6,
			0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a, 0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e,
			0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e, 0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e,
			0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf, 0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f,
			0xb0, 0x54, 0xbb, 0x16 };

	private static int[] rCon = new int[] { 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36 };
	public static void main(String[] args) {

		byte[] key = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

		String key32 = "000102030405060708090A0B0C0D0E0F000102030405060708090A0B0C0D0E0F";
		String key16 = "000102030405060708090A0B0C0D0E0F";
//		byte[] inputText = new byte[] { 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16 };
//		AES cipher = new AES(hex2bin(key16));
//		byte[] ecb_encrypt = cipher.ECB_encrypt(inputText);
//		byte[] ecb_decrypt = cipher.ECB_decrypt(ecb_encrypt);
//
//		logger.info("Key         {}", bin2hex(key));
//		logger.info("ecb_encrypt {}", bin2hex(ecb_encrypt));
//		logger.info("ecb_decrypt {}", bin2hex(ecb_decrypt));

		expandKey(hex2bin(key16));

	}

	private static void expandKey(byte[] key) {
		logger.info("Key         {}", bin2hex(key));
		int[] w = preExpandKey(key);

		int temp = specialOperationExpandKey(w[3], 1);
		w[4] = temp ^ w[0];
		w[5] = w[4] ^ w[1];
		w[6] = w[5] ^ w[2];
		w[7] = w[6] ^ w[3];
	}

	public static int specialOperationExpandKey(int word, int index) {
		int rotWord = rotateWord(word);
		int subWord = substituteWord(rotWord);
		int rConWord = rCon(index);
		int temp = subWord ^ rConWord;
		return temp;
	}

	static int[] preExpandKey(byte[] key) {

		int w[] = new int[48];

		// each round needs 4(ex. w0 w1 w2 w3) * 32 = 128 bits
		int index;
		// w0 to w3
		for (int i = 0; i < 4; i++) {
			w[i] = 0x00000000;
			index = i * 4;

			w[i] = ((key[index + 0] & 0xFF) << 24) | ((key[index + 1] & 0xFF) << 16) | ((key[index + 2] & 0xFF) << 8)
					| ((key[index + 3] & 0xFF) << 0);

			logger.info("expandKey         {}", bin2hex(int2ByteArray(w[i])));
		}

		return w;
	}

	static int rCon(int index) {
		int data = rCon[index] << 24;
		logger.info("rCon output    {}", bin2hex(int2ByteArray(data)));
		return data;
	}

	public static int substituteWord(int data) {

		logger.info("substituteWord input    {}", bin2hex(data));
		byte[] dataArray = int2ByteArray(data);

		for (int i = 0; i < dataArray.length; i++) {
			int b = (int) dataArray[i] & 0xFF;
			dataArray[i] = (byte) substituteByte(b);

		}
		logger.info("substituteWord input    {}", bin2hex(byteArray2Int(dataArray)));
		return byteArray2Int(dataArray);
	}

	public static int substituteByte(int data) {

		logger.info("substituteByte input    {}", bin2hex(data));
		data = sBox[data];
		logger.info("substituteByte output    {}", bin2hex(data));
		return data;
	}

	public static int rotateWord(int data) {

		logger.info("rotateWord Input     {}", bin2hex(int2ByteArray(data)));
		data = (data << 8) | ((data & 0xFF000000) >>> 24);
		logger.info("rotateWord Output    {}", bin2hex(int2ByteArray(data)));
		return data;
	}
}

