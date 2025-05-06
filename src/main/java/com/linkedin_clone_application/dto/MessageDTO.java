package com.linkedin_clone_application.dto;

import com.linkedin_clone_application.model.Message;

import java.time.LocalDateTime;


public class MessageDTO {
    private int id;
    private int senderId;
    private String senderFirstName;
    private String senderProfileImageUrl;
    private int receiverId;
    private String messageText;
    private String attachmentUrl;
    private String status;
    private LocalDateTime createdAt;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.senderId = message.getSender().getId();
        this.senderFirstName = message.getSender().getFirstName();
        this.senderProfileImageUrl = message.getSender().getProfilePictureUrl();
        this.receiverId = message.getReceiver().getId();
        this.messageText = message.getMessageText();
        this.attachmentUrl = message.getAttachmentUrl();
        this.status = message.getStatus().name();
        this.createdAt = message.getCreatedAt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderProfileImageUrl() {
        return senderProfileImageUrl;
    }

    public void setSenderProfileImageUrl(String senderProfileImageUrl) {
        this.senderProfileImageUrl = senderProfileImageUrl;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}