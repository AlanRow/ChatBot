package bot_interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//интерфейс, способный писать данные куда-либо
public interface DataWriter {
	//запись данных в виде ключ-значение
	public void writeAllData(Map<String, List<String>> data) throws IOException;
	//удаление данных
	public void clearData() throws IOException;
}
