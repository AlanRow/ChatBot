package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import algs.HostAlg;
import algs.OwnerAlg;
import bot.interfaces.DataManager;
import data.managers.VirtualDataManager;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

class OwnerAlgTests {

	@Test
	void memberTypeTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("owner_test.txt");
		OwnerAlg owner = initOwner(file, "$<0>:<Alan>:<1>:<owner>\r\n");
		
		assertEquals("owner", owner.getMemberType());
		
		file.delete();
	}
	
	@Test
	void getUserTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("owner_test.txt");
		OwnerAlg owner = initOwner(file, "$<0>:<Alan>:<1>:<owner>\r\n");
		
		assertEquals(new UserInfo(0, "Alan"), owner.getUser());
		
		file.delete();
	}
	
	@Test
	void helpTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("owner_test.txt");
		OwnerAlg owner = initOwner(file, "$<0>:<Alan>:<1>:<owner>\r\n");
		
		owner.readMessage("help");
		
		assertEquals(true, owner.isReadyToGenerate());
		assertEquals("Commands list:\n" +
				"help - show this help-list\n" +
				"info - show the information about meeting (name, time, place, etc.)\n" +
				"show - show the list of meeting members\n" +
				"exclude [username] - exclude some user from meeting\n"+
				"dehost [username] - if you want to refuse somebody from hosting meeting\n" +
				"setpswd [password]- set the password of meeting. Type <set none> if you want to make the meeting public.\n" +
				"setinfo [info] - set information about meeting (name, time, place, etc.)\n" +
				"sethost [username] - for adding some user to hosts of meeting\n"+
				"delegate [username] - delegate yor ownership to some member or host\n", owner.generateMessage());
		assertEquals(false, owner.isReadyToGenerate());
		
		file.delete();
	}
	
	@Test
	void dehostTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("owner_test.txt");
		OwnerAlg owner = initOwner(file, "$<0>:<Alan>:<1>:<owner>\r\n$<1>:<Nick>:<1>:<host>\r\n");
		
		owner.readMessage("dehost Nick");
		assertEquals("Ok, host has excluded", owner.generateMessage());
		assertEquals("member", owner.getMeeting().getMemberByName("Nick").getMemberType());
		
		owner.readMessage("dehost");
		assertEquals("Please, type the name of user, which you want to relegate from hosts.", owner.generateMessage());
		
		owner.readMessage("dehost Nick");
		assertEquals("Sorry, the member with name <Nick> has unfound in hosts-list.", owner.generateMessage());
		
		owner.readMessage("dehost Alan");
		assertEquals("You're the owner and you cannot to exclude yourself from hosts. If you want to delegate your authority, then try to type \"delegate [user_name]\".", owner.generateMessage());
		
		file.delete();
	}
	
	@Test
	void setpswdTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("owner_test.txt");
		OwnerAlg owner = initOwner(file, "$<0>:<Alan>:<1>:<owner>\r\n$<1>:<Nick>:<1>:<host>\r\n");
		
		owner.readMessage("setpswd 12345");
		assertEquals("Ok, password is saved.", owner.generateMessage());
		assertEquals(false, owner.getMeeting().isPublic());
		assertEquals(true, owner.getMeeting().checkPassword("12345"));
		
		owner.readMessage("setpswd");
		assertEquals("If you want to meke the meeting public then type <set none>.", owner.generateMessage());
		
		owner.readMessage("setpswd none");
		assertEquals("Ok, meeting is public.", owner.generateMessage());
		assertEquals(true, owner.getMeeting().isPublic());
		
		file.delete();
	}
	
	@Test
	void setinfoTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("owner_test.txt");
		OwnerAlg owner = initOwner(file, "$<0>:<Alan>:<1>:<owner>\r\n");
		
		owner.readMessage("setinfo OOP-practice at 16:10 in 514 classroom");
		assertEquals("Description has saved", owner.generateMessage());
		owner.readMessage("info");
		assertEquals("OOP-practice at 16:10 in 514 classroom", owner.generateMessage());
		

		owner.readMessage("setinfo");
		assertEquals("Type after <info> the description of meeting or <none> (if you want to clear the info)", owner.generateMessage());
		
		owner.readMessage("setinfo none");
		assertEquals("Description has cleared", owner.generateMessage());
		
		owner.readMessage("info");
		assertEquals("Here isn't any information about this meeting....", owner.generateMessage());
		
		file.delete();
	}
	
	@Test
	void sethostTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("owner_test.txt");
		OwnerAlg owner = initOwner(file, "$<0>:<Alan>:<1>:<owner>\r\n$<1>:<Nick>:<1>:<member>\r\n");
		
		owner.readMessage("sethost Nick");
		assertEquals("User <Nick> has appointed as a host.", owner.generateMessage());
		assertEquals("host", owner.getMeeting().getMemberByName("Nick").getMemberType());

		owner.readMessage("sethost");
		assertEquals("Please, type the name of user, which you want to appoint as a host.", owner.generateMessage());

		owner.readMessage("sethost Tim");
		assertEquals("Sorry, the member with name <Tim> has unfound in meeting-list.", owner.generateMessage());
		
		owner.readMessage("sethost Nick");
		assertEquals("This member is host already.", owner.generateMessage());
		
		owner.readMessage("sethost Alan");
		assertEquals("You're the owner. It's cooler. than host.", owner.generateMessage());
		
		file.delete();
	}
	
	@Test
	void delegateTests() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("owner_test.txt");
		OwnerAlg owner = initOwner(file, "$<0>:<Alan>:<1>:<owner>\r\n$<1>:<Nick>:<1>:<member>\r\n");
		
		owner.readMessage("delegate");
		assertEquals("Please, type the name of user, which you want to relegate from hosts.", owner.generateMessage());

		owner.readMessage("delegate Tim");
		assertEquals("Sorry, the member with name <Tim> has unfound in meeting-list.", owner.generateMessage());

		owner.readMessage("delegate Alan");
		assertEquals("Winnie, it's your home!.", owner.generateMessage());
		
		owner.readMessage("delegate Nick");
		assertEquals("Ok, the user <Nick> has became owner.", owner.generateMessage());
		assertEquals("host", owner.getMeeting().getMemberByName("Alan").getMemberType());
		assertEquals("owner", owner.getMeeting().getMemberByName("Nick").getMemberType());
		
		file.delete();
	}

	private OwnerAlg initOwner(File file, String data) throws IOException, UncorrectDataException, UnfoundedDataException {
		file.delete();
		file.createNewFile();
		
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write(data);
			writer.flush();
		}
		DataManager manager = new VirtualDataManager(file.getName());
		Meeting meet = new Meeting(1, manager);
		
		return meet.getOwner();
	}

}
