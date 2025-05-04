package com.linkedin_clone_application.repository;


import com.linkedin_clone_application.model.ConnectionRequest;
import com.linkedin_clone_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRequestRepo extends JpaRepository<ConnectionRequest, Integer> {
    
    List<ConnectionRequest> findByReceiverAndStatus(User receiver, ConnectionRequest.Status status);
    
    Optional<ConnectionRequest> findBySenderAndReceiver(User sender, User receiver);
    
    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, ConnectionRequest.Status status);
}