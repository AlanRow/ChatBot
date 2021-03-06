package tests;

import static org.junit.jupiter.api.Assertions.*;


import algs.HelloAlg;
import bot.interfaces.*;

import org.junit.jupiter.api.Test;

class HelloAlgTest {

	@Test
	void readTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Algorithm alg = new HelloAlg();
		
		assertEquals(false, alg.isReadyToGenerate());
		
		alg.readMessage("blablabla");

		assertEquals(false, alg.isReadyToGenerate());
		
		alg.readMessage("hello");

		assertEquals(true, alg.isReadyToGenerate());
	}
	
	@Test
	void writeTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Algorithm alg = new HelloAlg();
		
		alg.readMessage("hello");

		assertEquals("Hello!", alg.generateMessage());
		assertEquals("", alg.generateMessage());
		
		alg.readMessage("HI");
		
		assertEquals("We have greeted, yet.", alg.generateMessage());
		assertEquals("", alg.generateMessage());
	}

}
