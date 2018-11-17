package dataManagers;

import bot_interfaces.DataReader;
import exceptions.UncorrectDataException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//класс для чтения информации типа ключ-значение из файла
public class FileDataReader implements DataReader {

	//файл из которого будем читать
	private File file;
	
	public FileDataReader (String fileName) throws IOException {
		file = new File(fileName);
		
		if (!file.exists())
			throw new IOException("There isn't file " + fileName + " for reading.");
	}
	
	//метод вычитки всей информации из файла
	public Map<String, List<String>> getAllData() throws IOException, UncorrectDataException {
		
		Map<String, List<String>> keyMap = new HashMap<String, List<String>>();
		
		//*НУЖНЫ ПРАВКИ*
		//необходимо переписать метод, чтобы он считывал произвольное число значений после ключа
		//и выбрасывал exception, когда значений более, чем 2 и когда ключ встречается в файле дважды
		//используй UncorrectDataException
		try(FileReader reader = new FileReader(file))
		{
			String key = "";
			//читает пока не дойдет до конца файла
			while ((key = readWord(reader)) != null) //readWord читает целое слово внутри <...>
			{
				if ((char)reader.read() != ':')//если после слова не стоит ':', то кидаем исключение
					throw new UncorrectDataException("The file <" + file.getName() + "> hasn't the ':' after key.");
				String value = readWord(reader);
				
				//если после ключа не идет значение кидаем исключение
				if (value == null)
					throw new UncorrectDataException("The file <" + file.getName() + "> hasn't value for key.");
				
				//добавляем новый элемент в отображение для вывода
				if (!keyMap.containsKey(key))
					keyMap.put(key, new ArrayList<String>());
				
				keyMap.get(key).add(value);
				
				//проверяем перенос строки в конце набора
				if ((char)reader.read() != '\r')
					throw new UncorrectDataException("The file <" + file.getName() + "> hasn't \\r.");
			
				if ((char)reader.read() != '\n')
					throw new UncorrectDataException("The file <" + file.getName() + "> hasn't \\n.");
			}
			
			return keyMap;
		}
	}

	private static String readWord(FileReader reader) throws IOException, UncorrectDataException
	{
		int code;
		String word = "";
		
		if ((code = reader.read()) <= 0)
			return null;
		
		if ((char)code != '<')
			throw new UncorrectDataException("The word hasn't '<' at the start, but nas a '" + (char)code + "'.");
		
		
		while ((code = reader.read()) > 0 && (char)code != '>') {
			word += (char) code;
		}
		
		return word;
	}
}
