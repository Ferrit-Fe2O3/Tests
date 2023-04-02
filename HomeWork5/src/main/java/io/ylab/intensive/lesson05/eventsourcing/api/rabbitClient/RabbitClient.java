package io.ylab.intensive.lesson05.eventsourcing.api.rabbitClient;

public interface RabbitClient {

    void publishMessage(String message);

}
