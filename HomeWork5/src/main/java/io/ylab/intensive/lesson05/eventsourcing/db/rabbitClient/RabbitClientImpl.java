package io.ylab.intensive.lesson05.eventsourcing.db.rabbitClient;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitClientImpl implements RabbitClient {

    public static final String QUEUE_NAME = "queue";

    private ConnectionFactory connectionFactory;

    public RabbitClientImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public String receiveMessage() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            GetResponse response = channel.basicGet(QUEUE_NAME, true);
            if (response == null) {
                return  null;
            } else {
                byte[] body = response.getBody();
                return new String(body);
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
