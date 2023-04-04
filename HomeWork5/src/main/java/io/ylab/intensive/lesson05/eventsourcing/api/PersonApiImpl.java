package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.api.dbClient.DbClient;
import io.ylab.intensive.lesson05.eventsourcing.api.rabbitClient.RabbitClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonApiImpl implements PersonApi {

    private RabbitClient rabbitClient;

    private DbClient dbClient;

    public PersonApiImpl(DbClient dbClient, RabbitClient rabbitClient) {
        this.dbClient = dbClient;
        this.rabbitClient = rabbitClient;
    }

    @Override
    public void deletePerson(Long personId) {
        String message = "delete;" + Long.toString(personId);
        rabbitClient.publishMessage(message);
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        String message = "save;" + Long.toString(personId) + ";" + firstName + ";" + lastName + ";" + middleName;
        rabbitClient.publishMessage(message);
    }

    @Override
    public Person findPerson(Long personId) {
        return dbClient.findPerson(personId);
    }

    @Override
    public List<Person> findAll() {
        return dbClient.findAll();
    }

}
