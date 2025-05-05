package com.linkedin_clone_application.model;

import com.linkedin_clone_application.enums.MessageStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;



@Entity
@Table(name = "messages") // Make sure to specify the table name
public class Message{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id",nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id",nullable = false)
    private User receiver;

    @Column(nullable = false)
    private String messageText;

    private String attachmentUrl;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Message() {
        this.createdAt=LocalDateTime.now();
        this.status=MessageStatus.SENT;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}