package bot_interfaces;

import java.util.List;

import structures.UserInfo;

/*интерфес, класса, отвечающего за добавление новых пользователей. Содержит работу со 
 * средой передачи сообщений, а также может содержать стандартное приветствие*/

public interface UserControl {
	//метод проверки, появились ли новые пользователи
	public boolean areNewUsers();
	//метод возвращает список новых пользователей
	public List<UserInfo> getNewUsers();
}
