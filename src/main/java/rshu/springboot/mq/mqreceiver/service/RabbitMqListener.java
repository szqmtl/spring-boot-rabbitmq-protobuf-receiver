package rshu.springboot.mq.mqreceiver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import rshu.springboot.mq.mqreceiver.MqReceiverApplication;
import rshu.springboot.mq.proto.ExchangeData;

import java.io.IOException;

@Service
public class RabbitMqListener {
    static int s = 0;

    @RabbitListener(queues = MqReceiverApplication.QueueName)
    public void listen(final ExchangeData.ExchangeDataElement message) {
        System.out.printf("%d: Received a new notification: %s\n", s++, message.toString());
    }
}
