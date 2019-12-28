package rshu.springboot.mq.mqreceiver.service;

import org.springframework.amqp.support.converter.DefaultClassMapper;

public class RabbitMqFastJsonClassMapper extends DefaultClassMapper {
    public RabbitMqFastJsonClassMapper() {
        super();
        setTrustedPackages("*");
    }
}
