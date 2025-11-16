package com.buapp.notificationservice.notification.kafka;

import com.buapp.notificationservice.notification.kafka.abonnement.AbonnementEvent;
import com.buapp.notificationservice.notification.kafka.achat.AchatEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
    private String bootstrapServers;

    private Map<String, Object> baseConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, AchatEvent> achatEventConsumerFactory() {
        Map<String, Object> props = baseConsumerConfigs();
        
        JsonDeserializer<AchatEvent> deserializer = new JsonDeserializer<>(AchatEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);
        
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AchatEvent> achatKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AchatEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(achatEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, AbonnementEvent> abonnementEventConsumerFactory() {
        Map<String, Object> props = baseConsumerConfigs();
        
        JsonDeserializer<AbonnementEvent> deserializer = new JsonDeserializer<>(AbonnementEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);
        
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AbonnementEvent> abonnementKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AbonnementEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(abonnementEventConsumerFactory());
        return factory;
    }
}
