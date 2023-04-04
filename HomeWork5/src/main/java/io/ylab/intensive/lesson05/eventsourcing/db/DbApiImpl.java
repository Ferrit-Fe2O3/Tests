package io.ylab.intensive.lesson05.eventsourcing.db;

import io.ylab.intensive.lesson05.eventsourcing.db.dbClient.DbClient;
import io.ylab.intensive.lesson05.eventsourcing.db.rabbitClient.RabbitClient;
import org.springframework.stereotype.Component;

@Component
public class DbApiImpl implements DbApi {

    private DbClient dbClient;

    private RabbitClient rabbitClient;

    public DbApiImpl(DbClient dbClient, RabbitClient rabbitClient) {
        this.dbClient = dbClient;
        this.rabbitClient = rabbitClient;
    }

    public void start() {
        while (true) {
            String message = rabbitClient.receiveMessage();
            if (message != null) {
                String[] fields = message.split(";");
                if (fields[0].equals("delete")) {
                    System.out.println(" [x] - Deleting person with id - '" + fields[1] + "'");
                    if (dbClient.containsId(Long.parseLong(fields[1]))) {
                        deleteId(Long.parseLong(fields[1]));
                        System.out.println(" [x] - Removal successful");
                    } else {
                        System.out.println(" [x] - Removal failed: not found person with id - '" + fields[1] + "'");
                    }
                } else if (fields[0].equals("save")) {
                    System.out.println(" [x] - Saving person with id - '" + fields[1] + "'");
                    savePerson(Long.parseLong(fields[1]), fields[2], fields[3], fields[4]);
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteId(Long id) {
        dbClient.deleteId(id);
    }

    @Override
    public void savePerson(Long id, String firstName, String lastName, String middleName) {
        dbClient.savePerson(id, firstName, lastName, middleName);
    }
}
