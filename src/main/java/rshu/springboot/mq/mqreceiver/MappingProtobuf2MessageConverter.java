package rshu.springboot.mq.mqreceiver;

import com.google.common.base.Preconditions;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import rshu.springboot.mq.proto.ExchangeData;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class MappingProtobuf2MessageConverter implements MessageConverter {
    private final static String MESSAGE_TYPE_NAME = "_msg_type_name_";
    private final static String CONTENT_TYPE_PROTOBUF = "application/x-backend-command";

    private static Map<String, MessageLite.Builder> builderMap;

    static {
        builderMap = new HashMap<>();
        builderMap.put(ExchangeData.ExchangeDataElement.class.getSimpleName(), ExchangeData.ExchangeDataElement.newBuilder());
    }
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        String messageType = o.getClass().getSimpleName();
        if(!builderMap.containsKey(messageType)){
            throw new MessageConversionException("not support message type:" + messageType);
        }

        MessageLite lit = (MessageLite) o;
        messageProperties.setHeader(MESSAGE_TYPE_NAME, messageType);
        messageProperties.setContentType(CONTENT_TYPE_PROTOBUF);
        return new Message(lit.toByteArray(), messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String messageType = message.getMessageProperties().getHeaders().get(MESSAGE_TYPE_NAME).toString();
        if(!builderMap.containsKey(messageType)){
            throw new MessageConversionException("not support message type:" + messageType);
        }
        try{
            MessageLite.Builder builder = builderMap.get(messageType).clear();
            builder = builder.mergeFrom(message.getBody());
            return builder.build();
        }catch (InvalidProtocolBufferException e){
            throw new MessageConversionException("deserialize message error", e);
        }
    }

}
