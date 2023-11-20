package com.be.inssagram.domain.notification.repository;

import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByIdAndReceiverId (Long id, Long receiverId);

    Notification findByChatroomIdAndReceiverId(Long chatroomId, Long receiverId);

    @Query("SELECT n FROM Notification n WHERE n.receiverId = :receiverId AND n.chatroomId IS NULL")
    List<Notification> findAllByReceiverId (@Param("receiverId") Long receiverId);

    List<Notification> findAllByReceiverIdAndSenderInfo (Long receiverId, Member senderInfo);

    @Query("SELECT n FROM Notification n WHERE n.receiverId = :receiverId AND n.readStatus = false AND n.chatroomId IS NULL")
    List<Notification> findAllByReceiverIdAndReadStatus(@Param("receiverId") Long receiverId);

    @Query("SELECT n FROM Notification n WHERE n.receiverId = :receiverId AND n.readStatus = false AND n.chatroomId IS NOT NULL")
    List<Notification> findUnreadChatMessages(
            @Param("receiverId") Long receiverId);

    @Query("SELECT n FROM Notification n WHERE n.receiverId = :receiverId AND n.chatroomId IS NOT NULL")
    List<Notification> findAllChatMessages(
            @Param("receiverId") Long receiverId);
}
