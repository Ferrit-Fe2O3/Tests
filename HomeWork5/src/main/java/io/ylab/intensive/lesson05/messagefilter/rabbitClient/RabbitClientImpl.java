package io.ylab.intensive.lesson05.messagefilter.rabbitClient;

import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitClientImpl implements RabbitClient {

    public static final String OUTPUT_QUEUE = "output";

    public static final String INPUT_QUEUE = "input";

    ConnectionFactory connectionFactory;

    public RabbitClientImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void publishMessage(String message) {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(OUTPUT_QUEUE, false, false, false, null);
            channel.basicPublish("", OUTPUT_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String receiveMessage() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(INPUT_QUEUE, false, false, false, null);
            GetResponse response = channel.basicGet(INPUT_QUEUE, true);
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
