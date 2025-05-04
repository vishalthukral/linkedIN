package com.linkedin_clone_application.controller;


import com.linkedin_clone_application.model.ConnectionRequest;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.service.ConnectionService;
import com.linkedin_clone_application.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/connections")
public class ConnectionController {

    private final ConnectionService connectionService;
    private final UserService userService;

    public ConnectionController(ConnectionService connectionService, UserService userService) {
        this.connectionService = connectionService;
        this.userService = userService;
    }

//    @GetMapping
//    public String connections(@AuthenticationPrincipal User currentUser, Model model) {
//        model.addAttribute("connections", currentUser.getC());
//        return "connections/list";
//    }

    @GetMapping("/requests")
    public String connectionRequests(@AuthenticationPrincipal User currentUser, Model model) {
        List<ConnectionRequest> pendingRequests = connectionService.getPendingRequests(currentUser);
        model.addAttribute("requests", pendingRequests);
        return "connections/requests";
    }

    @PostMapping("/send/{userId}")
    public String sendConnectionRequest(@PathVariable int userId, @AuthenticationPrincipal User currentUser) {
        User receiver = userService.findById(userId);
                if(receiver==null) {
                    throw new RuntimeException("User not found");
                }
        
        connectionService.sendConnectionRequest(currentUser, receiver);
        return "redirect:/profile/" + userId;
    }

    @PostMapping("/accept/{requestId}")
    public String acceptConnectionRequest(@PathVariable int requestId, @AuthenticationPrincipal User currentUser) {
        ConnectionRequest request = connectionService.getConnectionRequest(requestId)
                .orElseThrow(() -> new RuntimeException("Connection request not found"));
        
        if (request.getReceiver().getId()!=(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to accept this request");
        }
        
        connectionService.acceptConnectionRequest(request);
        return "redirect:/connections/requests";
    }

    @PostMapping("/reject/{requestId}")
    public String rejectConnectionRequest(@PathVariable int requestId, @AuthenticationPrincipal User currentUser) {
        ConnectionRequest request = connectionService.getConnectionRequest(requestId)
                .orElseThrow(() -> new RuntimeException("Connection request not found"));
        
        if (request.getReceiver().getId()!=(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to reject this request");
        }
        
        connectionService.rejectConnectionRequest(request);
        return "redirect:/connections/requests";
    }
}