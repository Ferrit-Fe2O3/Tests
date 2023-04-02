package io.ylab.intensive.lesson05.eventsourcing.db.dbClient;

public interface DbClient {

    void deleteId(Long id);

    void savePerson(Long id, String firstName, String lastName, String middleName);

    boolean containsId(Long id);

}
