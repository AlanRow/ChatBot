package console.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bot.interfaces.UserControl;
import structures.UserInfo;

public class CUserController implements UserControl
{
	private boolean isUserAuth;
	private String userName;
	
	public CUserController()
	{
		isUserAuth = false;
		userName = "";
	}
	
	public boolean areNewUsers() 
	{
		if (!isUserAuth)
		{
			isUserAuth = true;
			return true;
		}
		
		return false;
	}

	public List<UserInfo> getNewUsers() {
		
		Scanner input = new Scanner(System.in);

		System.out.println("Здравствуйте, представтьесь, пожалуйста.");
		List<UserInfo> novice = new ArrayList<UserInfo>();
		novice.add(new UserInfo(0, input.nextLine()));
		return novice;
	}
	
}
