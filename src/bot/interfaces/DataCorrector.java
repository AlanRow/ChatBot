package bot.interfaces;

import java.io.IOException;

import exceptions.UnfoundedDataException;

//улучшение интерфейса DataWriter
public interface DataCorrector extends DataWriter
{
	//удаление данных по ключу
	public void removeData(String key) throws IOException;
	
	//удаление данных по ключу и номеру значения: 1 - id, 2 - username или имя, 3 - номер встречи (0 или 1)
	//4 - статус (user, member, host)
	//public void removeData(String key, int dataNumber) throws UnfoundedDataException, IOException;
}
