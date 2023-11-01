package com.be.inssagram.domain.notification.repository;

import com.be.inssagram.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByIdAndReceiverId (Long id, Long receiverId);

    List<Notification> findByReceiverId (Long receiverId);

    List<Notification> findAllByReceiverIdAndSenderId (Long receiverId, Long senderId);

    List<Notification> findAllByReceiverIdAndReadStatus(Long receiverId, boolean readStatus);
}
