package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import algs.ListWriteAlg;
import algs.UserAlg;
import bot.interfaces.DataManager;
import data.managers.VirtualDataManager;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

class UserAlgTests {

	@Test
	void helpTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("user_tests.txt");
		Map<Integer, Meeting> meets = new HashMap<Integer, Meeting>();
		UserAlg userAlg = initUser(file, "", null, meets);
		
		userAlg.readMessage("help");
		assertEquals(true, userAlg.isReadyToGenerate());
		assertEquals("Commands list:\n" +
				"help - show this help-list\n" +
				"will - join to public meeting\n" +
				"will [password] - join to not-public meeting\n", userAlg.generateMessage());
		assertEquals(false, userAlg.isReadyToGenerate());
		
		file.delete();
	}
	
	@Test
	void willTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("user_tests.txt");
		Map<Integer, Meeting> meets = new HashMap<Integer, Meeting>();
		UserAlg userAlg = initUser(file, "", null, meets);
		
		userAlg.readMessage("will");
		assertEquals("Ok, I've recorded you.", userAlg.generateMessage());
		assertEquals(userAlg.getUser(), meets.get(1).getMemberByName("Alan").getUser());
		
		file.delete();
	}
	
	@Test
	void willPasTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("user_tests.txt");
		Map<Integer, Meeting> meets = new HashMap<Integer, Meeting>();
		UserAlg userAlg = initUser(file, "", "12345", meets);
		
		userAlg.readMessage("will");
		assertEquals("This meet isn't public. Please, type the password after <will>.", userAlg.generateMessage());
		assertEquals(null, meets.get(1).getMemberByName("Alan"));
		
		userAlg.readMessage("will qwerty");
		assertEquals("Sorry, the password is incorrect.", userAlg.generateMessage());
		assertEquals(null, meets.get(1).getMemberByName("Alan"));
		
		userAlg.readMessage("will 12345");
		assertEquals("Ok, I've recorded you.", userAlg.generateMessage());
		assertEquals(userAlg.getUser(), meets.get(1).getMemberByName("Alan").getUser());
		
		file.delete();
	}

	
	private UserAlg initUser(File file, String data, String password, Map<Integer, Meeting> meets) throws IOException, UncorrectDataException, UnfoundedDataException {
		file.delete();
		file.createNewFile();
	
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write(data);
			writer.flush();
		}
		DataManager manager = new VirtualDataManager(file.getName());
		
		Meeting meet = new Meeting(1, manager, password);
		meets.put(1, meet);
		
		ListWriteAlg listAlg = new ListWriteAlg(meets, new UserInfo(0, "Alan"));
		
		return new UserAlg(listAlg, meets, new UserInfo(0, "Alan"), "");
	}
}
