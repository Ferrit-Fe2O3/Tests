package io.ylab.intensive.lesson05.messagefilter;


import io.ylab.intensive.lesson05.messagefilter.dbClient.DbClient;
import io.ylab.intensive.lesson05.messagefilter.rabbitClient.RabbitClient;

import org.springframework.stereotype.Component;

@Component
public class MessageFilterApiImpl implements MessageFilterApi {

    RabbitClient rabbitClient;

    DbClient dbClient;

    MessageFilterApiImpl(RabbitClient rabbitClient, DbClient dbClient) {
        this.rabbitClient = rabbitClient;
        this.dbClient = dbClient;
    }

    @Override
    public void start() {
        dbClient.init();
        while (true) {
            String message = rabbitClient.receiveMessage();
            if (message != null) {
                message = dbClient.ModifyString(message);
                rabbitClient.publishMessage(message);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
