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
import bot_interfaces.DataManager;
import dataManagers.VirtualDataManager;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

class MeetingTests {

	@Test
	void constructTest() throws IOException, UncorrectDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "");
		
		assertEquals(1, meet.getId());
		assertEquals(0, meet.getUsers().size());
		
		file.delete();
	}

	@Test
	void getMemberTest() throws IOException, UncorrectDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<2>:<member>\r\n");
		
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
		Meeting meet = initMeeting(1, file, "");
		Map<Integer, Meeting> meets = new HashMap<Integer, Meeting>();
		meets.put(1, meet);
		ListWriteAlg alg = new ListWriteAlg(meets, new UserInfo(0, "Alan"), "");
		meet.subscribe(alg);
		MemberAlg member = new MemberAlg(meet, new UserInfo(0, "Alan"), "");
		
		meet.addMember(member, alg);
		assertEquals(0, meet.getMember(new UserInfo(0, "Alan")).getUser().getId());
		
		file.delete();
	}

	@Test
	void removeMemberTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<2>:<member>\r\n");
		
		meet.removeMember(new UserInfo(0, "Alan"), "");
		
		assertEquals(null, meet.getMember(new UserInfo(0, "Alan")));
		
		file.delete();
	}

	@Test
	void appointAsHostTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<2>:<member>\r\n");
		
		meet.appointAsHost(new HostAlg(meet, new UserInfo(0, "Alan"), ""));
		
		assertEquals(true, meet.getMember( new UserInfo(0, "Alan")) instanceof HostAlg);
		
		file.delete();
	}
	
	@Test
	void relegateHostTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("testMeeting.txt");
		Meeting meet = initMeeting(1, file, "$<0>:<Alan>:<1>:<member>\r\n$<1>:<Alex>:<1>:<host>\r\n$<2>:<Nick>:<2>:<member>\r\n");
		
		meet.relegateHost(new UserInfo(1, "Alex"), "");
		
		assertEquals(false, meet.getMember( new UserInfo(1, "Alex")) instanceof HostAlg);
		
		file.delete();
	}
	
	private Meeting initMeeting(int meetingId, File file, String data) throws IOException, UncorrectDataException {
		file.delete();
		file.createNewFile();
		
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write(data);
			writer.flush();
		}
		DataManager manager = new VirtualDataManager(file.getName());
		
		return new Meeting(meetingId, manager);
	}

}
