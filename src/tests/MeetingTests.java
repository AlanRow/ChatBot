package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import algs.HostAlg;
import algs.ListWriteAlg;
import algs.MemberAlg;
import algs.OwnerAlg;
import bot.interfaces.DataManager;
import data.managers.VirtualDataManager;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

class MeetingTests {

	@Test
	void constructTest() throws IOException, UncorrectDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "", null);
		
		assertEquals(1, meet.getId());
		assertEquals(0, meet.getUsers().size());
		
		file.delete();
	}

	@Test
	void getMemberTest() throws IOException, UncorrectDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<2>:<member>\r\n", null);
		
		if (meet.getMember(new UserInfo(0, "Alan")) == null)
			System.out.println("Oh no!");
		if (meet.getMember(new UserInfo(1, "Alex")) == null)
			System.out.println("Oh no!!!");
		assertEquals(0, meet.getMember(new UserInfo(0, "Alan")).getUser().getId());
		assertEquals(1, meet.getMember(new UserInfo(1, "Alex")).getUser().getId());
		assertEquals(null, meet.getMember(new UserInfo(2, "Nick")));
		
		file.delete();
	}

	@Test
	void addMemberTest() throws IOException, UncorrectDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "", null);
		Map<Integer, Meeting> meets = new HashMap<Integer, Meeting>();
		meets.put(1, meet);
		ListWriteAlg alg = new ListWriteAlg(meets, new UserInfo(0, "Alan"), "");
		meet.subscribe(alg);
		MemberAlg member = new MemberAlg(meet, new UserInfo(0, "Alan"), "");
		
		meet.addMember(member, alg);
		assertEquals(0, meet.getMember(new UserInfo(0, "Alan")).getUser().getId());
		meet.addMember(member, null);
		
		file.delete();
	}

	@Test
	void removeMemberTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<2>:<member>\r\n", null);
		
		meet.removeMember(new UserInfo(0, "Alan"), "");
		
		assertEquals(null, meet.getMember(new UserInfo(0, "Alan")));
		
		file.delete();
	}

	@Test
	void appointAsHostTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<2>:<member>\r\n", null);
		
		meet.appointAsHost(new HostAlg(meet, new UserInfo(0, "Alan"), ""));
		
		assertEquals(true, meet.getMember(new UserInfo(0, "Alan")) instanceof HostAlg);
		
		file.delete();
	}
	
	@Test
	void relegateHostTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<2>:<member>\r\n", null);
		
		meet.relegateHost(new UserInfo(1, "Alex"), "");
		
		assertEquals(false, meet.getMember( new UserInfo(1, "Alex")) instanceof HostAlg);
		
		file.delete();
	}
	
	@Test
	void passwordTest() throws IOException, UncorrectDataException {
		File file = new File("testMeeting.txt");
		Meeting meet1 = initMeeting(1, file, "", null);
		Meeting meet2 = initMeeting(2, file, "", "12345");
		
		assertEquals(true, meet1.isPublic());
		assertEquals(false, meet2.isPublic());
		assertEquals(true, meet1.checkPassword("wertfg"));
		assertEquals(true, meet1.checkPassword(null));
		assertEquals(false, meet2.checkPassword("wertfg"));
		assertEquals(true, meet2.checkPassword("12345"));
		
		file.delete();
	}
	
	@Test
	public void getMemberByNameTest() throws IOException, UncorrectDataException {
		File file = new File("testMeeting.txt");
		Meeting meet1 = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<1>:<member>\r\n", null);
		
		assertEquals(new UserInfo(0, "Alan"), meet1.getMemberByName("Alan").getUser());
		assertEquals(new UserInfo(1, "Alex"), meet1.getMemberByName("Alex").getUser());
		assertNotEquals(new UserInfo(1, "Alex"), meet1.getMemberByName("Nick").getUser());
		assertEquals(null, meet1.getMemberByName("Tim"));

		file.delete();
	}
	
	@Test
	public void getHostByNameTest() throws IOException, UncorrectDataException {
		File file = new File("testMeeting.txt");
		Meeting meet1 = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<1>:<member>\r\n", null);
		
		assertEquals( null, meet1.getHostByName("Alan"));
		assertEquals(new UserInfo(1, "Alex"), meet1.getHostByName("Alex").getUser());

		file.delete();
	}
	
	@Test
	public void setPasswordTest() throws IOException, UncorrectDataException {
		File file = new File("testMeeting.txt");
		Meeting meet1 = initMeeting(1, file, "", null);
		
		meet1.setPassword("12345");
		
		assertEquals(false, meet1.isPublic());
		assertEquals(true, meet1.checkPassword("12345"));
		
		meet1.setPassword("none");

		assertEquals(true, meet1.isPublic());
		
		file.delete();
	}
	
	@Test
	public void setOwnerTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("testMeeting.txt");
		Meeting meet1 = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<1>:<member>\r\n", null);
		
		assertEquals(null, meet1.getOwner());
		
		meet1.setOwner(new UserInfo(2, "Nick"));
		
		assertEquals(new UserInfo(2, "Nick"), meet1.getOwner().getUser());
		assertEquals(true, meet1.getMember(new UserInfo(2, "Nick")) instanceof OwnerAlg);
		
		meet1.setOwner(new UserInfo(3, "Tim"));
		
		assertEquals(new UserInfo(3, "Tim"), meet1.getOwner().getUser());
		assertEquals(false, meet1.getMember(new UserInfo(2, "Nick")) instanceof OwnerAlg);
		assertEquals(true, meet1.getMember(new UserInfo(3, "Tim")) instanceof OwnerAlg);
		
		file.delete();
	}
	
	private Meeting initMeeting(int meetingId, File file, String data, String password) throws IOException, UncorrectDataException {
		file.delete();
		file.createNewFile();
		
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write(data);
			writer.flush();
		}
		DataManager manager = new VirtualDataManager(file.getName());
		if (password == null)
			return new Meeting(meetingId, manager);
		return new Meeting(meetingId, manager, password);
	}

}
