package com.be.inssagram.domain.notification.repository;

import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByIdAndReceiverId (Long id, Long receiverId);

    List<Notification> findAllByReceiverId (Long receiverId);

    List<Notification> findAllByReceiverIdAndSenderInfo (Long receiverId, Member senderInfo);

    List<Notification> findAllByReceiverIdAndReadStatus(Long receiverId, boolean readStatus);
}
