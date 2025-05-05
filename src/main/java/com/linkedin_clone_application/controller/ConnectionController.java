package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.model.ConnectionRequest;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.service.ConnectionService;
import com.linkedin_clone_application.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/connections")
public class ConnectionController {
    private final ConnectionService connectionService;
    private final UserService userService;

    public ConnectionController(ConnectionService connectionService, UserService userService1) {
        this.connectionService = connectionService;
        this.userService = userService1;
    }

    @PostMapping("/send")
    public String sendRequest(@RequestParam int senderId, @RequestParam int receiverId) {
        connectionService.sendConnectionRequest(senderId, receiverId);
        return "redirect:/connections/users"; // Redirect to users list
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        String email = getLoggedInUserEmail();
        if (email == null) {
            return "redirect:/login";  // If not logged in, redirect to login
        }
        int userId = userService.findByEmail(email).getId();
        User user = userService.findByEmail(email);
        List<User> users = userService.findAllExcept(user);
        int senderId = user.getId();
        Map<Integer, Boolean> connectionStatusMap = new HashMap<>();
        for (User u : users) {
            boolean alreadyRequested = connectionService.isRequestSentOrConnected(user, u);
            connectionStatusMap.put(u.getId(), alreadyRequested);
        }
        model.addAttribute("connectionStatusMap", connectionStatusMap);
        model.addAttribute("senderId", senderId);
        model.addAttribute("users", users);
        List<ConnectionRequest> requests = connectionService.getPendingRequests(userId);
        model.addAttribute("requests", requests);
        return "network";
    }

    @GetMapping("/requests")
    public String getPendingRequests(@RequestParam int userId, Model model) {
        List<ConnectionRequest> requests = connectionService.getPendingRequests(userId);
        model.addAttribute("requests", requests);
        return "connectionRequests";
    }

    @PostMapping("/accept")
    public String acceptRequest(@RequestParam int requestId) {
        connectionService.acceptRequest(requestId);

        String email = getLoggedInUserEmail();
        if (email == null) {
            return "redirect:/login";  // If not logged in, redirect to login
        }
        int userId = userService.findByEmail(email).getId();
        return "redirect:/connections/requests?userId=" + userId;
    }

    @PostMapping("/reject")
    public String rejectRequest(@RequestParam int requestId) {
        connectionService.rejectRequest(requestId);

        String email = getLoggedInUserEmail();
        if (email == null) {
            return "redirect:/login";  // If not logged in, redirect to login
        }
        int userId = userService.findByEmail(email).getId();
        return "redirect:/connections/requests?userId=" + userId;
    }

    @GetMapping("/list")
    public String getConnectedUsers(Model model) {

        String email = getLoggedInUserEmail();
        if (email == null) {
            return "redirect:/login";  // If not logged in, redirect to login
        }
        int userId = userService.findByEmail(email).getId();

        User user = userService.findByEmail(email);
        List<ConnectionRequest> connections = connectionService.getConnectedUsers(userId);
        model.addAttribute("connections", connections);
        return "connectionsList";  // Make sure this matches your Thymeleaf file name
    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername(); // this returns email
        }
        return null;
    }
}