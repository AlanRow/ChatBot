package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import algs.HostAlg;
import algs.MemberAlg;
import bot.interfaces.DataManager;
import data.managers.VirtualDataManager;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

class HostAlgTests {

	@Test
	void memberTypeTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("host_test.txt");
		HostAlg host = initHost(file, "$<0>:<Alan>:<1>:<host>\r\n", "", "Alan");
		
		assertEquals("host", host.getMemberType());
		
		file.delete();
	}
	
	@Test
	void getUserTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("host_test.txt");
		HostAlg host = initHost(file, "$<0>:<Alan>:<1>:<host>\r\n", "", "Alan");
		
		assertEquals(new UserInfo(0, "Alan"), host.getUser());
		
		file.delete();
	}
	
	@Test
	void helpTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("host_test.txt");
		HostAlg host = initHost(file, "$<0>:<Alan>:<1>:<host>\r\n", "", "Alan");
		
		host.readMessage("help");
		
		assertEquals(true, host.isReadyToGenerate());
		assertEquals("Commands list:\n" +
				"help - show this help-list\n" +
				"info - show the information about meeting (name, time, place, etc.)\n" +
				"wont - struck off you from list\n" +
				"show - show the list of meeting members\n" +
				"dehost - if you want to refuse from hosting of meeting\n" +
				"exclude [username] - exclude some user from meeting\n", host.generateMessage());
		assertEquals(false, host.isReadyToGenerate());
		
		file.delete();
	}
	
	@Test
	void showTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("host_test.txt");
		HostAlg host = initHost(file, "$<0>:<Alan>:<1>:<host>\r\n$<1>:<Nick>:<1>:<member>\r\n", "", "Alan");
		
		host.readMessage("show");
		
		assertEquals(true, host.isReadyToGenerate());
		assertEquals("Alan\nNick\n", host.generateMessage());
		assertEquals(false, host.isReadyToGenerate());
		
		file.delete();
	}
	
	@Test
	void dehostTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("host_test.txt");
		HostAlg host = initHost(file, "$<0>:<Alan>:<1>:<host>\r\n$<1>:<Nick>:<1>:<member>\r\n", "", "Alan");
		
		host.readMessage("dehost");
		
		assertEquals(true, host.isReadyToGenerate());
		assertEquals("Now, you're not a host.", host.generateMessage());
		assertEquals(false, host.isReadyToGenerate());
		assertEquals("member", host.getMeeting().getMemberByName("Alan").getMemberType());
		
		file.delete();
	}
	
	@Test
	void excludeTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("host_test.txt");
		HostAlg host = initHost(file, "$<0>:<Alan>:<1>:<host>\r\n$<1>:<Nick>:<1>:<member>\r\n$<2>:<Alex>:<1>:<host>\r\n", "", "Alan");
		
		host.readMessage("exclude Nick");
		assertEquals("Nick has excluded.", host.generateMessage());
		assertEquals(null, host.getMeeting().getMemberByName("Nick"));
		
		host.readMessage("exclude");
		assertEquals("Please, type the name of user, who you want to exclude...", host.generateMessage());
		
		host.readMessage("exclude Tim");
		assertEquals("Here isn't the person with name <Tim>.", host.generateMessage());
		
		host.readMessage("exclude Alan");
		assertEquals("You try to shot in yourself leg. If you want to exit, then using <wont>", host.generateMessage());
		
		host.readMessage("exclude Alex");
		assertEquals("You try to exclude other host. You cannot do this", host.generateMessage());
		
		file.delete();
	}

	private HostAlg initHost(File file, String data, String startMes, String userName) throws IOException, UncorrectDataException, UnfoundedDataException {
		file.delete();
		file.createNewFile();
		
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write(data);
			writer.flush();
		}
		DataManager manager = new VirtualDataManager(file.getName());
		Meeting meet = new Meeting(1, manager);
		
		return meet.getHostByName(userName);//memberAlg(meet, new UserInfo(0, "Alan"), startMes);
	}

}
