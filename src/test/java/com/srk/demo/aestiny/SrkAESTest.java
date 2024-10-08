package com.srk.demo.aestiny;

import static com.srk.utils.Utils.bin2hex;
import static com.srk.utils.Utils.hex2bin;
import static com.srk.utils.Utils.int2ByteArray;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SrkAESTest {
	private static Logger logger = LogManager.getLogger(SrkAESTest.class);

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
		int data = SrkAES.rotateWord(input);
		logger.info("expandKey         {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rotateWord2() {
		int input = 0xFF1122FF;
		int expected = 0x1122FFFF;
		int data = SrkAES.rotateWord(input);
		logger.info("expandKey         {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rotateWord3() {
		int input = 0xFFFF2233;
		int expected = 0xFF2233FF;
		int data = SrkAES.rotateWord(input);
		logger.info("expandKey         {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_substituteByte1() {
		byte input = 0x01;
		byte expected = 0x7C;
		int data = SrkAES.substituteByte(input);
		logger.info("substituteWord     {}", bin2hex(data));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_substituteByte2() {
		byte input = 0x00;
		byte expected = 0x63;
		int data = SrkAES.substituteByte(input);
		logger.info("substituteWord     {}", bin2hex(data));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_substituteByte3() {
		int input = 0xFF;
		int expected = 0x16;
		int data = SrkAES.substituteByte(input);
		logger.info("substituteWord     {}", bin2hex(data));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_substituteWord1() {
		int input = 0xFFFFFFFF;
		int expected = 0x16161616;
		int data = SrkAES.substituteWord(input);
		logger.info("substituteWord     {}", bin2hex(data));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_substituteWord2() {
		int input = 0xFFFFFF00;
		int expected = 0x16161663;
		int data = SrkAES.substituteWord(input);
		logger.info("substituteWord     {}", bin2hex(data));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rCon1() {
		int index = 1;
		int expected = 0x01000000;
		int data = SrkAES.rCon(index);
		logger.info("test_rCon1     {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rCon2() {
		int index = 2;
		int expected = 0x02000000;
		int data = SrkAES.rCon(index);
		logger.info("test_rCon1     {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rCon3() {
		int index = 3;
		int expected = 0x04000000;
		int data = SrkAES.rCon(index);
		logger.info("test_rCon1     {}", bin2hex(int2ByteArray(data)));
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_rCon4() {
		int index = 4;
		int expected = 0x08000000;
		int data = SrkAES.rCon(index);
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
		int rotWord = SrkAES.rotateWord(input);

		logger.info("rotWord     {}", bin2hex(int2ByteArray(rotWord)));
		assertEquals(0x0D0E0F0C, rotWord, "Rotate Word Error");

		int subWord = SrkAES.substituteWord(rotWord);
		assertEquals(0xD7AB76FE, subWord, "subWord Word Error");

		int index = 1;
		int rConWord = SrkAES.rCon(index);
		assertEquals(0x01000000, rConWord, "rConWord Word Error");

		int data = subWord ^ rConWord;
		assertEquals(expected, data, "Expected data not found");
	}

	@Test
	void test_specialOperationExpandKey() {
		int input = 0x0C0D0E0F;
		int expected = 0xD6AB76FE;
		int roundIndex = 1;
		int outputWord = SrkAES.specialOperationExpandKey(input, roundIndex);

		logger.info("specialOperationExpandKey     {}", bin2hex(int2ByteArray(outputWord)));
		assertEquals(expected, outputWord, "Error Special Operation Expand Key ");

	}

	@Test
	@Disabled("Failed test to fix")
	void test_ExpandKeyRount1() {

		String key16 = "000102030405060708090A0B0C0D0E0F";
		int[] w = SrkAES.preExpandKey(hex2bin(key16));

		int input = 0x0C0D0E0F;
		int expected = 0xD6AB76FE;
		int roundIndex = 1;

		assertEquals(input, w[3], "Error Special Operation Expand Key ");

		int temp = SrkAES.specialOperationExpandKey(w[3], roundIndex);
		assertEquals(expected, temp, "Error Special Operation Expand Key ");

		w[4] = w[0] ^ temp;

		
		assertEquals(0xD2AF72FA, w[4], "Error Special Operation Expand Key ");

		w[5] = w[1] ^ w[4];
		w[6] = w[2] ^ w[5];
		w[7] = w[3] ^ w[6];


	}
}
