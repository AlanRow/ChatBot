package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import algs.ListWriteAlg;
import data.managers.FileDataReader;
import data.managers.FileDataWriter;
import data.managers.VirtualDataManager;
import exceptions.UncorrectDataException;
import structures.Meeting;
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
		ListWriteAlg alg = initializeLWA(file, new UserInfo(0, "Alan"), "$<0>:<Alan>:<1>:<member>\r\n");
		
		alg.readMessage("WoNt");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Ok, I've struck off you.", alg.generateMessage());
		
		file.delete();
	}
	
	@Test
	void tautologyTest() throws IOException, UncorrectDataException {
		File file = new File("lwaTest.txt");
		ListWriteAlg alg = initializeLWA(file, new UserInfo(0, "Alan"), "$<0>:<Alan>:<1>:<member>\r\n");
		
		alg.readMessage("will");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("You've just been a member.", alg.generateMessage());
		
		file.delete();
	}
	
	@Test
	public void showTest() throws IOException, UncorrectDataException {
		File file = new File("lwaTest.txt");
		ListWriteAlg alg = initializeLWA(file, new UserInfo(1, "Nick"), "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Nick>:<1>:<host>\r\n");
		
		alg.readMessage("ShOw");
		assertEquals("Alan\nNick\n", alg.generateMessage());
		
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
		Map<Integer, Meeting> meetings = new HashMap<Integer, Meeting>();
		meetings.put(1, new Meeting(1, manager));
		
		return new ListWriteAlg(meetings, user);
	}
}
