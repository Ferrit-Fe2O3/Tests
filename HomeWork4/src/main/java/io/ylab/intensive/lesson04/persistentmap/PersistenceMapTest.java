package io.ylab.intensive.lesson04.persistentmap;

import java.sql.SQLException;
import javax.sql.DataSource;

import io.ylab.intensive.lesson04.DbUtil;

public class PersistenceMapTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMap persistentMap = new PersistentMapImpl(dataSource);
    // Написать код демонстрации работы
    persistentMap.init("first_map");
    persistentMap.put("first_key", "first_value"); // Вносим первые данные
    persistentMap.put("first_key", "new_first_value"); // Перезаписываем первые данные
    persistentMap.put("second_key", "second_value"); // Вносим вторые данные
    System.out.println(persistentMap.containsKey("first_key")); // true
    System.out.println(persistentMap.containsKey("third_key")); // false
    System.out.println(persistentMap.getKeys()); // Выводим все ключи
    System.out.println(persistentMap.get("first_key")); // Выводим значение первого ключа
    persistentMap.clear(); // Очищаем мапу
    System.out.println(persistentMap.getKeys()); // Смотрим результат
  }
  
  public static DataSource initDb() throws SQLException {
    String createMapTable = "" 
                                + "drop table if exists persistent_map; " 
                                + "CREATE TABLE if not exists persistent_map (\n"
                                + "   map_name varchar,\n"
                                + "   KEY varchar,\n"
                                + "   value varchar\n"
                                + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);
    return dataSource;
  }
}
