package com.social.mc_post.kafka;

import com.social.mc_post.dto.notification.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.kafkaMessageTopicForNotification}")
    private String kafkaMessageTopicForNotification;

    public void sendMessageForNotification(NotificationDTO data) {
        kafkaTemplate.send(kafkaMessageTopicForNotification, data);
    }
}