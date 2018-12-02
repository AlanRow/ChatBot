package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import algs.MemberAlg;
import bot.interfaces.DataManager;
import data.managers.VirtualDataManager;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

class MemberAlgTests {

	@Test
	void memberTypeTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("member_test.txt");
		MemberAlg member = initMember(file, "$<0>:<Alan>:<1>:<member>\r\n", "", "Alan");
		
		assertEquals("member", member.getMemberType());
		
		file.delete();
	}
	
	@Test
	void getUserTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("member_test.txt");
		MemberAlg member = initMember(file, "$<0>:<Alan>:<1>:<member>\r\n", "", "Alan");
		
		assertEquals(new UserInfo(0, "Alan"), member.getUser());
		
		file.delete();
	}
	
	@Test
	void helpTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("member_test.txt");
		MemberAlg member = initMember(file, "$<0>:<Alan>:<1>:<member>\r\n", "", "Alan");
		
		member.readMessage("help");
		
		assertEquals(true, member.isReadyToGenerate());
		assertEquals("Commands list:\n" +
				"help - show this help-list\n" +
				"info - show the information about meeting (name, time, place, etc.)\n" +
				"wont - struck off you from list\n", member.generateMessage());
		assertEquals(false, member.isReadyToGenerate());
		
		file.delete();
	}
	
	@Test
	void willTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("member_test.txt");
		MemberAlg member = initMember(file, "$<0>:<Alan>:<1>:<member>\r\n", "", "Alan");
		
		member.readMessage("will");
		
		assertEquals(true, member.isReadyToGenerate());
		assertEquals("You've just been a member.", member.generateMessage());
		assertEquals(false, member.isReadyToGenerate());
		
		file.delete();
	}
	
	@Test
	void wontTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("member_test.txt");
		MemberAlg member = initMember(file, "$<0>:<Alan>:<1>:<member>\r\n", "", "Alan");
		
		member.readMessage("wont");
		
		assertEquals(true, member.isReadyToGenerate());
		assertEquals("Ok, I've struck off you.", member.generateMessage());
		assertEquals(false, member.isReadyToGenerate());
		
		file.delete();
	}
	
	@Test
	void infoTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("member_test.txt");
		MemberAlg member = initMember(file, "$<0>:<Alan>:<1>:<member>\r\n", "", "Alan");
		
		member.readMessage("info");
		assertEquals(true, member.isReadyToGenerate());
		assertEquals("Here isn't any information about this meeting....", member.generateMessage());
		assertEquals(false, member.isReadyToGenerate());
		
		member.getMeeting().setInfo("OOP-practice at 16:10 in 514 classroom");
		
		member.readMessage("info");
		assertEquals(true, member.isReadyToGenerate());
		assertEquals("OOP-practice at 16:10 in 514 classroom", member.generateMessage());
		assertEquals(false, member.isReadyToGenerate());
		
		file.delete();
	}

	private MemberAlg initMember(File file, String data, String startMes, String userName) throws IOException, UncorrectDataException, UnfoundedDataException {
		file.delete();
		file.createNewFile();
		
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write(data);
			writer.flush();
		}
		DataManager manager = new VirtualDataManager(file.getName());
		Meeting meet = new Meeting(1, manager);
		
		return meet.getMemberByName(userName);//memberAlg(meet, new UserInfo(0, "Alan"), startMes);
	}
}
