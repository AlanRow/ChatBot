package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import algs.ListWriteAlg;
import dataManagers.FileDataReader;
import dataManagers.FileDataWriter;
import dataManagers.VirtualDataManager;
import exceptions.UncorrectDataException;
import structures.UserInfo;

class ListWriteAlgTests {

	@Test
	void willComeTest() throws IOException, UncorrectDataException {
		File file = new File("lwaTest.txt");
		ListWriteAlg alg = initializeLWA(file, new UserInfo(0, "user"), "");
		
		alg.readMessage("wIlL");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Ok, I've recorded you.", alg.generateMessage());
		
		file.delete();
	}
	
	@Test
	void wontComeTest() throws IOException, UncorrectDataException {
		File file = new File("lwaTest.txt");
		ListWriteAlg alg = initializeLWA(file, new UserInfo(0, "user"), "<0>:<user>\r\n");
		
		alg.readMessage("WoNt");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Ok, I've struck off you", alg.generateMessage());
		
		file.delete();
	}
	
	@Test
	void tautologyTest() throws IOException, UncorrectDataException {
		File file = new File("lwaTest.txt");
		ListWriteAlg alg = initializeLWA(file, new UserInfo(0, "user"), "<0>:<user>\r\n");
		
		alg.readMessage("will");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Yes, I've just understand.", alg.generateMessage());
		
		alg.readMessage("WONT");
		alg.generateMessage();
		alg.readMessage("WonT");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("You haven't recorded, yet.", alg.generateMessage());
		
		file.delete();
	}
	
	@Test
	public void showTest() throws IOException, UncorrectDataException {
		File file = new File("lwaTest.txt");
		ListWriteAlg alg = initializeLWA(file, new UserInfo(0, "user"), "<0>:<user1>\r\n<1>:<user2>\r\n");
		
		alg.readMessage("ShOw");
		assertEquals("user1\nuser2\n", alg.generateMessage());
		
		file.delete();
	}
	
	private ListWriteAlg initializeLWA(File file, UserInfo user, String data) throws IOException, UncorrectDataException
	{
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write(data);
			writer.flush();
		}
		
		VirtualDataManager manager = new VirtualDataManager(file.getName());
		
		return new ListWriteAlg(manager, manager, user);
	}
}
