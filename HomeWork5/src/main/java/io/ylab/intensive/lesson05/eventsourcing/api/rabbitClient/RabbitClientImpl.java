package io.ylab.intensive.lesson05.eventsourcing.api.rabbitClient;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitClientImpl implements RabbitClient {

    public static final String QUEUE_NAME = "queue";

    ConnectionFactory connectionFactory;


    public RabbitClientImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void publishMessage(String message) {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
