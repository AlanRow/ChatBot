package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import algs.ListWriteAlg;
import dataManagers.FileDataReader;
import dataManagers.FileDataWriter;
import dataManagers.VirtualDataManager;
import exceptions.UnCorrectDataException;
import structures.UserInfo;

class ListWriteAlgTests {

	@Test
	void willComeTest() throws IOException, UnCorrectDataException {
		ListWriteAlg alg = initializeLWA("lwaTest1.txt", new UserInfo(0, "user"), "");
		
		alg.readMessage("wIlL");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Хорошо, я вас записал.", alg.generateMessage());
	}
	
	void wontComeTest() throws IOException, UnCorrectDataException {
		ListWriteAlg alg = initializeLWA("lwaTest2.txt",new UserInfo(0, "user"), "<user><will>\r\n");
		
		alg.readMessage("WoNt");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Хорошо, я вас вычеркнул.", alg.generateMessage());
	}
	
	void tautologyTest() throws IOException, UnCorrectDataException {
		ListWriteAlg alg = initializeLWA("lwaTest2.txt", new UserInfo(0, "user"), "<user><will>\r\n");
		
		alg.readMessage("will");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Да, я так и понял.", alg.generateMessage());
		
		alg.readMessage("WONT");
		alg.generateMessage();
		alg.readMessage("WonT");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Да, я так и понял.", alg.generateMessage());
	}
	
	private ListWriteAlg initializeLWA(String fileName, UserInfo user, String data) throws IOException, UnCorrectDataException
	{
		try (FileWriter writer = new FileWriter(fileName))
		{
			writer.write(data);
			writer.flush();
		}
		
		VirtualDataManager manager = new VirtualDataManager(new FileDataReader(fileName), new FileDataWriter(fileName));
		
		return new ListWriteAlg(manager, manager, user);
	}
}
