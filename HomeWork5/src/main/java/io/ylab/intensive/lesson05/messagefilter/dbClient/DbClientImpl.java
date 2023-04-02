package io.ylab.intensive.lesson05.messagefilter.dbClient;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

@Component
public class DbClientImpl implements DbClient {

    public static final int BATCH_SIZE = 100;

    public static final String CENSOR_FILE = "censor.txt";

    public static final String CENSOR_TABLE = "censor_table";

    public static final String CREATE_CENSOR_TABLE_SQL = "CREATE TABLE " + CENSOR_TABLE + " (id SERIAL, val VARCHAR)";

    public static final String INSERT_SQL = "INSERT INTO " + CENSOR_TABLE + " (val) VALUES (?)";

    DataSource dataSource;

    public DbClientImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void init() {
        try (Connection connection = dataSource.getConnection()) {
            // Проверям создана ли уже таблица
            try (ResultSet resultSet = connection.getMetaData().getTables(
                    null, null, CENSOR_TABLE, null)) {
                try (Statement statement = connection.createStatement()) {
                    if (!resultSet.next()) {
                        statement.execute(CREATE_CENSOR_TABLE_SQL); // Если нет создаем
                    } else {
                        statement.execute("TRUNCATE " + CENSOR_TABLE); // Если да очищаем от устаревших данных
                    }
                }
            }
            censorFileToDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String ModifyString(String string) {
        String[] strings = getStrings(string);
        String[] separators = getSeparators(string);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (checkString(strings[i])) {
                sb.append(censorString(strings[i]));
            } else {
                sb.append(strings[i]);
            }
            if (i < separators.length) {
                sb.append(separators[i]);
            }
        }
        return sb.toString();
    }

    private String[] getStrings(String string) {
        ArrayList<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (char ch : string.toCharArray()) {
            if ((ch == '.') || (ch == ',') || (ch == '!') || (ch == '?') || (ch == '\n') || (ch == ' ') || (ch == ';')) {
                if (sb.length() != 0) {
                    list.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else {
                sb.append(ch);
            }
        }
        list.add(sb.toString());
        return list.toArray(new String[0]);
    }

    private String[] getSeparators(String string) {
        ArrayList<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (char ch : string.toCharArray()) {
            if ((ch == '.') || (ch == ',') || (ch == '!') || (ch == '?') || (ch == '\n') || (ch == ' ') || (ch == ';')) {
                sb.append(ch);
            } else {
                if (sb.length() != 0) {
                    list.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            }
        }
        list.add(sb.toString());
        return list.toArray(new String[0]);
    }

    private String censorString(String string) {
        if (string.length() > 2) {
            StringBuilder sb = new StringBuilder();
            sb.append(string.charAt(0));
            for (int i = 1; i < string.length() - 1; i++) {
                sb.append('*');
            }
            sb.append(string.charAt(string.length() - 1));
            return sb.toString();
        }
        return string;
    }

    private boolean checkString(String string) {
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM " + CENSOR_TABLE + " WHERE val = ?;")) {
            statement.setString(1, string.toLowerCase(Locale.ROOT));
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private void censorFileToDb() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
                try (FileReader fileReader = new FileReader(CENSOR_FILE); BufferedReader reader = new BufferedReader(fileReader)) {
                    int count = 0;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        statement.setString(1, line);
                        statement.addBatch();
                        if (count == BATCH_SIZE - 1) {
                            executeBatch(statement, connection);
                            count = 0;
                        }
                        count++;
                    }
                    if (count > 0) {
                        executeBatch(statement, connection);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void executeBatch(PreparedStatement statement, Connection connection) throws SQLException {
        try {
            statement.executeBatch();
            connection.commit();
        } catch (BatchUpdateException e) {
            e.printStackTrace();
            connection.rollback();
        }
    }
}
