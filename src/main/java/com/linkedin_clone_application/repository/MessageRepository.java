package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) OR (m.sender.id = :receiverId AND m.receiver.id = :senderId) ORDER BY m.createdAt")
    List<Message> findChatHistory(@Param("senderId") int senderId, @Param("receiverId") int receiverId);

    @Query("SELECT m FROM Message m WHERE m.receiver.id = :userId OR m.sender.id = :userId ORDER BY m.createdAt DESC")
    List<Message> findMessagesForUser(@Param("userId") int userId);

}