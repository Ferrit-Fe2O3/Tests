package io.ylab.intensive.lesson04.persistentmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Класс, методы которого надо реализовать 
 */
public class PersistentMapImpl implements PersistentMap {

  private String name;

  private DataSource dataSource;

  public PersistentMapImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void init(String name) {
    this.name = name;
  }

  @Override
  public boolean containsKey(String key) throws SQLException {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 "SELECT * FROM persistent_map " +
                         "WHERE map_name = ? AND KEY = ?;")) {
        statement.setString(1, name);
        statement.setString(2, key);
        try (ResultSet resultSet = statement.executeQuery()) {
          return resultSet.next();
        }
      }
  }

  @Override
  public List<String> getKeys() throws SQLException {
    List<String> keyList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 "SELECT * FROM persistent_map " +
                         "WHERE map_name = ?;")) {
      statement.setString(1, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          keyList.add(resultSet.getString("KEY"));
        }
      }
    }
    return keyList;
  }

  @Override
  public String get(String key) throws SQLException {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 "SELECT * FROM persistent_map " +
                         "WHERE map_name = ? AND KEY = ?;")) {
      statement.setString(1, name);
      statement.setString(2, key);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return  resultSet.getString("value");
        }
        return null;
      }
    }
  }

  @Override
  public void remove(String key) throws SQLException {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 "DELETE FROM persistent_map WHERE map_name = ? AND KEY = ?;")) {
        statement.setString(1, name);
        statement.setString(2, key);
        statement.executeUpdate();
    }
  }

  @Override
  public void put(String key, String value) throws SQLException {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 "INSERT INTO persistent_map (map_name, KEY, value) VALUES (?, ?, ?)")) {
        if (containsKey(key)) {
          remove(key);
        }
        statement.setString(1, name);
        statement.setString(2, key);
        statement.setString(3, value);
        statement.executeUpdate();
      }
  }

  @Override
  public void clear() throws SQLException {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 "DELETE FROM persistent_map WHERE map_name = ?;")) {
      statement.setString(1, name);
      statement.executeUpdate();
    }
  }
}
