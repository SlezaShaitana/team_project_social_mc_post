package com.social.mc_post.dto.notification;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationDTO {
    private UUID id;
    private UUID authorId;
    private String content;
    private NotificationType notificationType;
    private LocalDateTime sentTime;
    private UUID receiverId;
}