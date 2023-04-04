package io.ylab.intensive.lesson05.messagefilter.rabbitClient;

public interface RabbitClient {

    void publishMessage(String message);

    String receiveMessage();

}
