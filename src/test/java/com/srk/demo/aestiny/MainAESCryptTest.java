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
		int expected = 0x112233FF;
		int data = MainAESCrypt.rotateWord(input);
		logger.info("expandKey         {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rotateWord2() {
		int input = 0xFF1122FF;
		int expected = 0x1122FFFF;
		int data = MainAESCrypt.rotateWord(input);
		logger.info("expandKey         {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rotateWord3() {
		int input = 0xFFFF2233;
		int expected = 0xFF2233FF;
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

	@Test
	void test_rCon1() {
		int index = 1;
		int expected = 0x01000000;
		int data = MainAESCrypt.rCon(index);
		logger.info("test_rCon1     {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rCon2() {
		int index = 2;
		int expected = 0x02000000;
		int data = MainAESCrypt.rCon(index);
		logger.info("test_rCon1     {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rCon3() {
		int index = 3;
		int expected = 0x04000000;
		int data = MainAESCrypt.rCon(index);
		logger.info("test_rCon1     {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rCon4() {
		int index = 4;
		int expected = 0x08000000;
		int data = MainAESCrypt.rCon(index);
		logger.info("test_rCon1     {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_expandKey() {

//		W4 Special Operation 1
//	    W4 0. input w3
//	    W4 0C0D0E0F
//	    W4 1. rotWord
//	    W4 0D0E0F0C
//	    W4 2. subWord
//	    W4 D7AB76FE
//	        W4 3. rConWord
//	        W4 01000000
//	    W4 3. subWord xor rConWord
//	    W4 D6AB76FE
//	    W4 XOR
//	    W4 = W0 xor g(W3)
//	w4 D6AA74FD

		int input = 0x0C0D0E0F;
		int expected = 0xD6AB76FE;
		int rotWord = MainAESCrypt.rotateWord(input);

		logger.info("rotWord     {}", bin2hex(int2ByteArray(rotWord)));
		assertEquals(0x0D0E0F0C, rotWord, "Rotate Word Error");

		int subWord = MainAESCrypt.substituteWord(rotWord);
		assertEquals(0xD7AB76FE, subWord, "subWord Word Error");

		int index = 1;
		int rConWord = MainAESCrypt.rCon(index);
		assertEquals(0x01000000, rConWord, "rConWord Word Error");

		int data = subWord ^ rConWord;
		assertEquals(expected, data, "Expected data not found");
	}

}
