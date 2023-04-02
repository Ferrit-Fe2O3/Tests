package io.ylab.intensive.lesson05.eventsourcing.api.dbClient;

import io.ylab.intensive.lesson05.eventsourcing.Person;

import java.util.List;

public interface DbClient {

    Person findPerson(Long personId);

    List<Person> findAll();

}
