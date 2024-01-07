package com.srk.demo.aestiny;

import static com.srk.utils.Utils.bin2hex;
import static com.srk.utils.Utils.hex2bin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.srk.demo.aestiny.core.AES;

public class MainAESCrypt {
	private static Logger logger = LogManager.getLogger(MainAESCrypt.class);
	public static void main(String[] args) {

		byte[] key = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

		String key32 = "000102030405060708090A0B0C0D0E0F000102030405060708090A0B0C0D0E0F";
		String key16 = "000102030405060708090A0B0C0D0E0F";
		byte[] inputText = new byte[] { 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16 };
		AES cipher = new AES(hex2bin(key16));
		byte[] ecb_encrypt = cipher.ECB_encrypt(inputText);
		byte[] ecb_decrypt = cipher.ECB_decrypt(ecb_encrypt);

		logger.info("Key         {}", bin2hex(key));
		logger.info("ecb_encrypt {}", bin2hex(ecb_encrypt));
		logger.info("ecb_decrypt {}", bin2hex(ecb_decrypt));

	}
}
