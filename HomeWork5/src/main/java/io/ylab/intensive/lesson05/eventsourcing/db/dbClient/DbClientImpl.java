package io.ylab.intensive.lesson05.eventsourcing.db.dbClient;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DbClientImpl implements DbClient {

    private DataSource dataSource;

    public DbClientImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void deleteId(Long id) {
        try (java.sql.Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     "DELETE FROM person WHERE person_id = ?;")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) { }
    }

    @Override
    public void savePerson(Long id, String firstName, String lastName, String middleName) {
        try (Connection con = dataSource.getConnection()) {
            if (containsId(id)) {
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

    public boolean containsId(Long id) {
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

}
