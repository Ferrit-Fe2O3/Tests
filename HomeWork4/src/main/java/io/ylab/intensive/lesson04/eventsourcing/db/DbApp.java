package io.ylab.intensive.lesson04.eventsourcing.db;


import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.rabbitmq.client.*;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;

public class DbApp {

  public static final String QUEUE_NAME = "queue";

  public static void main(String[] args) throws Exception {
    DataSource dataSource = initDb();
    ConnectionFactory connectionFactory = initMQ();

    Connection connection = connectionFactory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      System.out.println(" [x] Received '" + message + "'");

      String[] fields = message.split(";");
      if (fields[0].equals("delete")) {
        System.out.println(" [x] - Deleting person with id - '" + fields[1] + "'");
        if (containsId(Long.parseLong(fields[1]), dataSource)) {
          deleteId(Long.parseLong(fields[1]), dataSource);
          System.out.println(" [x] - Removal successful");
        } else {
          System.out.println(" [x] - Removal failed: not found person with id - '" + fields[1] + "'");
        }
      } else if (fields[0].equals("save")) {
        System.out.println(" [x] - Saving person with id - '" + fields[1] + "'");
        savePerson(Long.parseLong(fields[1]), fields[2], fields[3], fields[4], dataSource);
      }
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
  }
  
  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
  
  private static DataSource initDb() throws SQLException {
    String ddl = ""
                     + "drop table if exists person;"
                     + "create table if not exists person (\n"
                     + "person_id bigint primary key,\n"
                     + "first_name varchar,\n"
                     + "last_name varchar,\n"
                     + "middle_name varchar\n"
                     + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(ddl, dataSource);
    return dataSource;
  }

  private static boolean containsId(Long id, DataSource dataSource) {
    try (java.sql.Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 "SELECT * FROM person " +
                         "WHERE person_id = ?;")) {
      statement.setLong(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    } catch (SQLException e) {
      return false;
    }
  }

  private static void deleteId(Long id, DataSource dataSource) {
    try (java.sql.Connection con = dataSource.getConnection();
         PreparedStatement statement = con.prepareStatement(
                 "DELETE FROM person WHERE person_id = ?;")) {
      statement.setLong(1, id);
      statement.executeUpdate();
    } catch (SQLException e) { }
  }

  private static void savePerson(Long id, String firstName, String lastName, String middleName, DataSource dataSource) {
    try (java.sql.Connection con = dataSource.getConnection()) {
      if (containsId(id, dataSource)) {
        try (PreparedStatement statement = con.prepareStatement(
                "UPDATE person SET first_name = ?, last_name = ?, middle_name = ? WHERE person_id = ?;")) {
          statement.setString(1, firstName);
          statement.setString(2, lastName);
          statement.setString(3, middleName);
          statement.setLong(4, id);
          statement.executeUpdate();
        }
      } else {
        try (PreparedStatement statement = con.prepareStatement(
                "INSERT INTO person (person_id, first_name, last_name, middle_name)" +
                        " VALUES (?, ?, ?, ?);")) {
          statement.setLong(1, id);
          statement.setString(2, firstName);
          statement.setString(3, lastName);
          statement.setString(4, middleName);

          statement.executeUpdate();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
