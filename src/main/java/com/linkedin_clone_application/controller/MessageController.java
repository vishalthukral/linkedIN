package com.linkedin_clone_application.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


import com.linkedin_clone_application.dto.MessageDTO;
import com.linkedin_clone_application.enums.MessageStatus;
import com.linkedin_clone_application.model.Message;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.service.CloudinaryService;
import com.linkedin_clone_application.service.MessageService;
import com.linkedin_clone_application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final MessageService messageService;
    private final CloudinaryService cloudinaryService;

    public MessageController(SimpMessagingTemplate messagingTemplate, UserService userService,
                             MessageService messageService, CloudinaryService cloudinaryService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.messageService = messageService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/messages/{senderId}/{recieverId}")
    public ResponseEntity<List<MessageDTO>> getChatHistory(@PathVariable int senderId, @PathVariable int recieverId) {
        List<Message> messages = messageService.getChatHistory(senderId, recieverId);
        List<MessageDTO> messageDTOs = messages.stream()
                .map(MessageDTO::new)
                .toList();
        return ResponseEntity.ok(messageDTOs);
    }

    @GetMapping("/message/{userId}")
    public String showMessagesPage(@PathVariable int userId, Model model) {
        System.out
                .println("public String showMessagesPage \n (@PathVariable int userId, Model model)" + "\n" + userId);
        // Fetch user and all other users for chat
        User currentUser = userService.findById(userId);
        List<User> users = userService.findAllExcept(currentUser); // Fetch all users except the current one

        model.addAttribute("user", currentUser);
        model.addAttribute("users", users);

        return "message";
    }

    @GetMapping("/messages/{userId}")
    public ResponseEntity<List<MessageDTO>> getUserMessages(@PathVariable int userId) {
        List<Message> messages = messageService.getMessagesForUser(userId);
        List<MessageDTO> messageDTOs = messages.stream()
                .map(MessageDTO::new)
                .toList();
        return ResponseEntity.ok(messageDTOs);
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload Map<String, Object> messageData) {
        System.out.println("Received WebSocket message: " + messageData);

        try {
            int senderId = Integer.parseInt(messageData.get("senderId").toString());
            int receiverId = Integer.parseInt(messageData.get("receiverId").toString());
            String messageText = messageData.getOrDefault("messageText", "").toString();
            String imageUrl = messageData.getOrDefault("imageUrl", "").toString();

            // Fetch sender and receiver

            User sender = userService.findById(senderId);
            User receiver = userService.findById(receiverId);

            if (sender == null || receiver == null) {
                System.err.println("Error: Sender or receiver not found.");
                return;
            }


            // Create and save message
            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMessageText(messageText);
            message.setStatus(MessageStatus.SENT);
            message.setCreatedAt(LocalDateTime.now());
            message.setAttachmentUrl(imageUrl);// Ensure timestamp is set

            Message savedMessage = messageService.saveMessage(message);

            // Convert to DTO before sending
            MessageDTO messageDTO = new MessageDTO(savedMessage);

            messagingTemplate.convertAndSendToUser(Integer.toString(senderId), "/queue/messages", messageDTO);
            messagingTemplate.convertAndSendToUser(Integer.toString(receiverId), "/queue/messages", messageDTO);

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessageHttp(@RequestBody Message message) {
        Message savedMessage = messageService.saveMessage(message); // Persist the message
        return ResponseEntity.ok("Message sent successfully with ID: " + savedMessage.getId());
    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername(); // this returns email
        }
        return null;
    }
}