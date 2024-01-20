package com.srk.demo.aestiny;

import static com.srk.utils.Utils.bin2hex;
import static com.srk.utils.Utils.int2ByteArray;
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
		logger.info("expandKey         {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rotateWord2() {
		int input = 0xFF1122FF;
		int expected = 0xFFFF1122;
		int data = MainAESCrypt.rotateWord(input);
		logger.info("expandKey         {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rotateWord3() {
		int input = 0xFFFF2233;
		int expected = 0x33FFFF22;
		int data = MainAESCrypt.rotateWord(input);
		logger.info("expandKey         {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_substituteByte1() {
		byte input = 0x01;
		byte expected = 0x7C;
		int data = MainAESCrypt.substituteByte(input);
		logger.info("substituteWord     {}", bin2hex(data));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_substituteByte2() {
		byte input = 0x00;
		byte expected = 0x63;
		int data = MainAESCrypt.substituteByte(input);
		logger.info("substituteWord     {}", bin2hex(data));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_substituteByte3() {
		int input = 0xFF;
		int expected = 0x16;
		int data = MainAESCrypt.substituteByte(input);
		logger.info("substituteWord     {}", bin2hex(data));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_substituteWord1() {
		int input = 0xFFFFFFFF;
		int expected = 0x16161616;
		int data = MainAESCrypt.substituteWord(input);
		logger.info("substituteWord     {}", bin2hex(data));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_substituteWord2() {
		int input = 0xFFFFFF00;
		int expected = 0x16161663;
		int data = MainAESCrypt.substituteWord(input);
		logger.info("substituteWord     {}", bin2hex(data));
		assertEquals(expected, data, "Expected data not found");
	}

}
