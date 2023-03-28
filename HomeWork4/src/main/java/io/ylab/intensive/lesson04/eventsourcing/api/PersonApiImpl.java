package io.ylab.intensive.lesson04.eventsourcing.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;

/**
 * Тут пишем реализацию
 */
  public class PersonApiImpl implements PersonApi {

    private DataSource dataSource;

    private ConnectionFactory connectionFactory;

    public static final String QUEUE_NAME = "queue";

    public PersonApiImpl(DataSource dataSource, ConnectionFactory connectionFactory) {
      this.dataSource = dataSource;
      this.connectionFactory = connectionFactory;
    }

    @Override
    public void deletePerson(Long personId) {
      try (Connection connection = connectionFactory.newConnection();
           Channel channel = connection.createChannel()) {
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String string = "delete;" + Long.toString(personId);
        channel.basicPublish("", QUEUE_NAME, null, string.getBytes(StandardCharsets.UTF_8));
      } catch (IOException | TimeoutException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
      try (Connection connection = connectionFactory.newConnection();
           Channel channel = connection.createChannel()) {
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String string = "save;" + Long.toString(personId) + ";" + firstName + ";" + lastName + ";" + middleName;
        channel.basicPublish("", QUEUE_NAME, null, string.getBytes(StandardCharsets.UTF_8));
      } catch (IOException | TimeoutException e) {
        e.printStackTrace();
      }
    }

    @Override
    public Person findPerson(Long personId) {
      Person person = new Person();
      try (java.sql.Connection connection = dataSource.getConnection();
           PreparedStatement statement = connection.prepareStatement("SELECT * FROM person WHERE person_id = ?")) {
        statement.setLong(1, personId);
        try (ResultSet resultSet = statement.executeQuery()) {
          if (resultSet.next()) {
            person.setId(resultSet.getLong("person_id"));
            person.setName(resultSet.getString("first_name"));
            person.setLastName(resultSet.getString("last_name"));
            person.setMiddleName(resultSet.getString("middle_name"));
          } else {
            return null;
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return person;
    }

    @Override
    public List<Person> findAll() {
      List<Person> persons = new ArrayList<>();
      try (java.sql.Connection connection = dataSource.getConnection();
           PreparedStatement statement = connection.prepareStatement("SELECT * FROM person");
           ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          Person person = new Person();
          person.setId(resultSet.getLong("person_id"));
          person.setName(resultSet.getString("first_name"));
          person.setLastName(resultSet.getString("last_name"));
          person.setMiddleName(resultSet.getString("middle_name"));
          persons.add(person);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return persons;
    }
}
