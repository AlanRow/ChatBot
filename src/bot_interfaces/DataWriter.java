package bot_interfaces;

import java.io.IOException;

//интерфейс, способный писать данные куда-либо
public interface DataWriter {
	//запись данных в виде ключ-значение
	public void writeData(String key, String data) throws IOException;
	//удаление данных
	public void clearData() throws IOException;
}
