package com.linkedin_clone_application.service;

import com.linkedin_clone_application.enums.ConnectionStatus;
import com.linkedin_clone_application.model.ConnectionRequest;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.ConnectionRequestRepository;
import com.linkedin_clone_application.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionService {
    private final ConnectionRequestRepository connectionRequestRepository;
    private final UserRepository userRepository;

    public ConnectionService(ConnectionRequestRepository connectionRequestRepository, UserRepository userRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
        this.userRepository = userRepository;
    }

    public void sendConnectionRequest(int senderId, int receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() ->
                new RuntimeException("Receiver not found"));

        ConnectionRequest request = new ConnectionRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(ConnectionStatus.PENDING);
        connectionRequestRepository.save(request);
    }

    public List<ConnectionRequest> getPendingRequests(int receiverId) {
        User receiver = userRepository.findById(receiverId).orElseThrow(() ->
                new RuntimeException("Receiver not found"));
        return connectionRequestRepository.findByReceiverAndStatus(receiver, ConnectionStatus.PENDING);
    }

    public List<ConnectionRequest> getAcceptedRequest(int senderId) {
        User sender = userRepository.findById(senderId).orElseThrow(() ->
                new RuntimeException("Receiver not found"));
        return connectionRequestRepository.findBySenderAndStatus(sender,ConnectionStatus.ACCEPTED);
    }

    public void acceptRequest(int requestId) {
        ConnectionRequest request =
                connectionRequestRepository.findById(requestId).orElseThrow(() ->
                        new RuntimeException("Request not found"));
        request.setStatus(ConnectionStatus.ACCEPTED);
        connectionRequestRepository.save(request);
    }

    public void rejectRequest(int requestId) {
        ConnectionRequest request =
                connectionRequestRepository.findById(requestId).orElseThrow(() ->
                        new RuntimeException("Request not found"));
        request.setStatus(ConnectionStatus.REJECTED);
        connectionRequestRepository.save(request);
    }

    public List<ConnectionRequest> getConnectedUsers(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return connectionRequestRepository.findBySenderOrReceiverAndStatus(user, user, ConnectionStatus.ACCEPTED);
    }

    public boolean isRequestSentOrConnected(User user1, User user2) {
        List<ConnectionStatus> statuses = List.of(ConnectionStatus.PENDING, ConnectionStatus.ACCEPTED);
        return connectionRequestRepository.existsBetweenUsersWithStatuses(user1, user2, statuses);
    }
}