package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {

    public static final String[] types = {"TABLE", "FOREIGN TABLE", "SYSTEM TABLE", "SYSTEM VIEW", "VIEW"};

    private DataSource dataSource;

    public SQLQueryBuilderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String queryForTable(String tableName) throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Connection connection = dataSource.getConnection()) {
            try (ResultSet resultSet = connection.getMetaData().getColumns(
                    null, null, tableName, null)) {
                if (resultSet.next()) {
                    sb.append("SELECT ");
                    sb.append(resultSet.getString(4));
                    while (resultSet.next()) {
                        sb.append(", ");
                        sb.append(resultSet.getString(4));
                    }
                    sb.append(" FROM ");
                    sb.append(tableName);
                } else {
                    return null;
                }
            }
        }
        return sb.toString();
    }

    @Override
    public List<String> getTables() throws SQLException {
        List<String> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.getMetaData().getTables(
                null, null, null, types)) {
            while (resultSet.next()) {
                result.add(resultSet.getString(3)); // Получаем имя таблицы
            }
        }
        return result;
    }
}
