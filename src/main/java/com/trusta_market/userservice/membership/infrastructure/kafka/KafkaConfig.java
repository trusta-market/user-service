package com.trusta_market.userservice.membership.infrastructure.kafka;

import com.trusta_market.userservice.membership.infrastructure.kafka.dto.OrderConfirmedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:user-service}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, OrderConfirmedEvent> orderConfirmedConsumerFactory() {
        JsonDeserializer<OrderConfirmedEvent> deserializer =
                new JsonDeserializer<>(OrderConfirmedEvent.class, false);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // earliest: 서비스 재시작 시 놓친 메시지도 처음부터 처리
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 수동 ack — Consumer에서 처리 완료 후 명시적으로 ack
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderConfirmedEvent> orderConfirmedListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderConfirmedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConfirmedConsumerFactory());
        // MANUAL: acknowledge() 호출 시점에 오프셋 커밋
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}
