package we.LiteBoard.domain.notification.application;

import we.LiteBoard.domain.notification.enumerate.NotificationType;

public record NotificationMessage(String title, String content, NotificationType type) {
}

