//package com.linkedin_clone_application.service;
//
//
//import com.linkedin_clone_application.model.ConnectionRequest;
//import com.linkedin_clone_application.model.User;
//import com.linkedin_clone_application.repository.ConnectionRequestRepo;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class ConnectionService {
//
//    private final ConnectionRequestRepo connectionRequestRepository;
//    private final UserService userService;
//
//    public ConnectionService(ConnectionRequestRepo connectionRequestRepository, UserService userService) {
//        this.connectionRequestRepository = connectionRequestRepository;
//        this.userService = userService;
//    }
//
//    public ConnectionRequest sendConnectionRequest(User sender, User receiver) {
//        // Check if a request already exists
//        Optional<ConnectionRequest> existingRequest = connectionRequestRepository.findBySenderAndReceiver(sender, receiver);
//
//        if (existingRequest.isPresent()) {
//            return existingRequest.get();
//        }
//
//        ConnectionRequest request = new ConnectionRequest();
//        request.setSender(sender);
//        request.setReceiver(receiver);
//
//        return connectionRequestRepository.save(request);
//    }
//
//    public List<ConnectionRequest> getPendingRequests(User user) {
//        return connectionRequestRepository.findByReceiverAndStatus(user, ConnectionRequest.Status.PENDING);
//    }
//
//    @Transactional
//    public void acceptConnectionRequest(ConnectionRequest request) {
//        request.setStatus(ConnectionRequest.Status.ACCEPTED);
//        connectionRequestRepository.save(request);
//
////        userService.addConnection(request.getSender(), request.getReceiver());
//    }
//
//    @Transactional
//    public void rejectConnectionRequest(ConnectionRequest request) {
//        request.setStatus(ConnectionRequest.Status.REJECTED);
//        connectionRequestRepository.save(request);
//    }
//
//    public boolean hasConnectionRequest(User sender, User receiver) {
//        return connectionRequestRepository.existsBySenderAndReceiverAndStatus(
//            sender, receiver, ConnectionRequest.Status.PENDING);
//    }
//
//    public Optional<ConnectionRequest> getConnectionRequest(int id) {
//        return connectionRequestRepository.findById(id);
//    }
//}