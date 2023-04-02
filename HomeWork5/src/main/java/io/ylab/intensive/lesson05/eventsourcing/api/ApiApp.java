package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    PersonApi personApi = applicationContext.getBean(PersonApiImpl.class);

    // пишем взаимодействие с PersonApi
    personApi.savePerson(1L, "Алексей", "Краснов", "Владимирович");
    personApi.savePerson(2L, "Максим", "Арбатов", "Андреевич");
    personApi.savePerson(3L, "Олег", "Петров", "Николаевич");
    Thread.sleep(500); // Сделаем задержку что бы обработчик успел обработать сообщения
    Person person = personApi.findPerson(1L);
    System.out.println(person.getId() + " " + person.getName() +
            " " + person.getMiddleName() + " " + person.getLastName());
    personApi.deletePerson(2L);
    personApi.savePerson(1L, "Михаил", "Токарев", "Анатольевич");
    Thread.sleep(500); // Сделаем задержку что бы обработчик успел обработать сообщения
    System.out.println("Find all:");
    for (Person p : personApi.findAll()) {
      System.out.println(p.getId() + " " + p.getName() +
              " " + p.getMiddleName() + " " + p.getLastName());
    }
  }
}
