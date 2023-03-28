package io.ylab.intensive.lesson04.movie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class MovieLoaderImpl implements MovieLoader {

  public static final int BATCH_SIZE = 50;

  private DataSource dataSource;

  public MovieLoaderImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void loadData(File file) {
    List<Movie> movies = fileToMovies(file);
    try {
      addMoviesToDb(movies);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Функция для парсинга данных из файла в List
  private List<Movie> fileToMovies(File file) {
    List<Movie> movies = new ArrayList<>();
    try (FileReader fileReader = new FileReader(file); BufferedReader reader = new BufferedReader(fileReader)) {
      reader.readLine();
      reader.readLine();
      String line;
      while ((line = reader.readLine()) != null) {
        Movie movie = new Movie();
        String[] fields = line.split(";");
        movie.setYear(fields[0].equals("") ? null : Integer.parseInt(fields[0]));
        movie.setLength(fields[1].equals("") ? null : Integer.parseInt(fields[1]));
        movie.setTitle(fields[2]);
        movie.setSubject(fields[3]);
        movie.setActors(fields[4]);
        movie.setActress(fields[5]);
        movie.setDirector(fields[6]);
        movie.setPopularity(fields[7].equals("") ? null : Integer.parseInt(fields[7]));
        movie.setAwards(fields[8].equalsIgnoreCase("yes"));
        movies.add(movie);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return movies;
  }

  // Функция для добавления фильмов в БД пакетами
  public void addMoviesToDb(List<Movie> movies) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement statement = connection.prepareStatement("INSERT INTO movie " +
                      "(year, length, title, subject, actors, actress, director, popularity, awards) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
        int count = 0;
        for (Movie movie : movies) {
          if (movie.getYear() != null) {
            statement.setInt(1, movie.getYear());
          } else {
            statement.setNull(1, Types.INTEGER);
          }
          if (movie.getLength() != null) {
            statement.setInt(2, movie.getLength());
          } else {
            statement.setNull(2, Types.INTEGER);
          }
          statement.setString(3, movie.getTitle());
          statement.setString(4, movie.getSubject());
          statement.setString(5, movie.getActors());
          statement.setString(6, movie.getActress());
          statement.setString(7, movie.getDirector());
          if (movie.getPopularity() != null) {
            statement.setInt(8, movie.getPopularity());
          } else {
            statement.setNull(8, Types.INTEGER);
          }
          statement.setBoolean(9, movie.getAwards());
          statement.addBatch();
          if ((count % BATCH_SIZE) == 0 || count == movies.size() - 1) {
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
      }
      connection.setAutoCommit(true);
    }
  }

}
