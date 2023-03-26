package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    ConnectionFactory connectionFactory = initMQ();
    DataSource dataSource = DbUtil.buildDataSource();

    PersonApi api = new PersonApiImpl(dataSource, connectionFactory);
    api.savePerson(1L, "Алексей", "Краснов", "Владимирович");
    api.savePerson(2L, "Максим", "Арбатов", "Андреевич");
    api.savePerson(3L, "Олег", "Петров", "Николаевич");
    Thread.sleep(500); // Сделаем задержку что бы обработчик успел обработать сообщения
    Person person = api.findPerson(1L);
    System.out.println(person.getId() + " " + person.getName() +
            " " + person.getMiddleName() + " " + person.getLastName());
    api.deletePerson(2L);
    api.savePerson(1L, "Михаил", "Токарев", "Анатольевич");
    Thread.sleep(500); // Сделаем задержку что бы обработчик успел обработать сообщения
    System.out.println("Find all:");
    for (Person p : api.findAll()) {
      System.out.println(p.getId() + " " + p.getName() +
              " " + p.getMiddleName() + " " + p.getLastName());
    }
  }

  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
}
