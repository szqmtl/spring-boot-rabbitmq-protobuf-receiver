package rshu.springboot.mq.mqreceiver;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import rshu.springboot.mq.proto.ExchangeData;

@EnableRabbit
@SpringBootApplication
public class MqReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqReceiverApplication.class, args);
	}

	static final public String ExchangeName = "exchange-test";
	static final public String QueueName = "queue-test";
	static final public String RoutingName = "routing-test";

	@Bean
	DirectExchange exchange(){
		return new DirectExchange(ExchangeName);
	}

	@Bean
	Queue queue(){
		return new Queue(QueueName, false);
	}

	@Bean
	Binding binding(Queue queue, DirectExchange exchange){
		return BindingBuilder.bind(queue).to(exchange).with(RoutingName);
	}

	@Bean
	public MappingJackson2MessageConverter consumerJackson2MessageConverter(){
		return new MappingJackson2MessageConverter();
	}

	@Bean
	public MappingProtobuf2MessageConverter protobufMessageConverter(){
		return new MappingProtobuf2MessageConverter();
	}

	@Bean
	public SimpleRabbitListenerContainerFactory messageHandlerMethodFactory(ConnectionFactory connectionFactory){
		var factory = new SimpleRabbitListenerContainerFactory();
//		factory.setMessageConverter(consumerJackson2MessageConverter());
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(protobufMessageConverter());
		return factory;
	}

//	@Override
//	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
//		registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
//	}
}
