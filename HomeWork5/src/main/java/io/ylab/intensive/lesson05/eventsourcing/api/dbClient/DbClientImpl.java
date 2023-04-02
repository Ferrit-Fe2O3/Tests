package io.ylab.intensive.lesson05.eventsourcing.api.dbClient;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DbClientImpl implements DbClient {

    private DataSource dataSource;

    public DbClientImpl(DataSource dataSource) {
        this.dataSource = dataSource;
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
