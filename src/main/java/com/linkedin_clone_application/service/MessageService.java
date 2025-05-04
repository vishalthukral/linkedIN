package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Message;
import com.linkedin_clone_application.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    public MessageRepo messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getChatHistory(int senderId, int recieverId) {
        return messageRepository.findChatHistory(senderId, recieverId);
    }

    public List<Message> getMessagesForUser(int userId) {
        return messageRepository.findMessagesForUser(userId);
    }

}