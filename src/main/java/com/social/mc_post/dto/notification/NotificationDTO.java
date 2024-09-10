package com.social.mc_post.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {
    private UUID id;
    private UUID authorId;
    private String content;
    private NotificationType notificationType;
    private LocalDateTime sentTime;
    private UUID receiverId;
    private MicroServiceName serviceName;
}