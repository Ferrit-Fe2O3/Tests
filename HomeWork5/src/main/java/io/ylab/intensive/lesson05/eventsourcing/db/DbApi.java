package io.ylab.intensive.lesson05.eventsourcing.db;

public interface DbApi {

    void start();

    void deleteId(Long id);

    void savePerson(Long id, String firstName, String lastName, String middleName);

}
