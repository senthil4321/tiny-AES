package com.srk.demo.aestiny;

import static com.srk.utils.Utils.bin2hex;
import static com.srk.utils.Utils.intToByteArray;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainAESCryptTest {
	private static Logger logger = LogManager.getLogger(MainAESCryptTest.class);
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test_rotateWord() {

		int input = 0xFF112233;
		int expected = 0x33FF1122;
		int data = MainAESCrypt.rotateWord(input);
		logger.info("expandKey         {}", bin2hex(intToByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rotateWord2() {

		int input = 0xFF1122FF;
		int expected = 0xFFFF1122;
		int data = MainAESCrypt.rotateWord(input);
		logger.info("expandKey         {}", bin2hex(intToByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rotateWord3() {

		int input = 0xFFFF2233;
		int expected = 0x33FFFF22;
		int data = MainAESCrypt.rotateWord(input);
		logger.info("expandKey         {}", bin2hex(intToByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

}
