package io.ylab.intensive.lesson04.filesort;

import java.io.*;
import java.sql.*;
import javax.sql.DataSource;

public class FileSortImpl implements FileSorter {

  public static final int BATCH_SIZE = 100;

  public static final String OUTPUT_FILE = "output.txt";

  private DataSource dataSource;

  public FileSortImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public File sort(File data) {
    fileToDb(data);
    //fileToDbSlow(data);
    return sortAndSaveToFile();
  }

  // Функция для добавления данных в БД пакетами
  private void fileToDb(File file) {
    try (FileReader fileReader = new FileReader(file);
         BufferedReader reader = new BufferedReader(fileReader);
         Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement statement = connection.prepareStatement(
              "INSERT INTO numbers (val) VALUES (?)")) {
        int count = 0;
        String line;
        while ((line = reader.readLine()) != null) {
          statement.setLong(1, Long.parseLong(line));
          statement.addBatch();
          if ((count % BATCH_SIZE) == 0) {
            try {
              statement.executeBatch();
              connection.commit();
            } catch (BatchUpdateException e) {
              e.printStackTrace();
              connection.rollback();
            }
          }
          count++;
        }
        // Дописываем остаток
        try {
          statement.executeBatch();
          connection.commit();
        } catch (BatchUpdateException e) {
          e.printStackTrace();
          connection.rollback();
        }
      }
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  // Функция для вывода отсортированых данных из БД в файл
  private File sortAndSaveToFile() {
    File file = new File(OUTPUT_FILE);
    try (FileWriter fileWriter = new FileWriter(file);
         BufferedWriter writer = new BufferedWriter(fileWriter);
         Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 "SELECT * FROM numbers ORDER BY val DESC");
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        writer.write(Long.toString(resultSet.getLong("val")));
        writer.write("\n");
        writer.flush();
      }
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
    return file;
  }

  // Функция для добавления данных в БД по одному числу
  private void fileToDbSlow(File file) {
    try (FileReader fileReader = new FileReader(file);
         BufferedReader reader = new BufferedReader(fileReader);
         Connection connection = dataSource.getConnection()) {
      try (PreparedStatement statement = connection.prepareStatement(
              "INSERT INTO numbers (val) VALUES (?)")) {
        String line;
        while ((line = reader.readLine()) != null) {
          statement.setLong(1, Long.parseLong(line));
          statement.executeUpdate();
        }
      }
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }

}
