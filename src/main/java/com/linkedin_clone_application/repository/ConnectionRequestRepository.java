package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.enums.ConnectionStatus;
import com.linkedin_clone_application.model.ConnectionRequest;
import com.linkedin_clone_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer> {

    List<ConnectionRequest> findByReceiverAndStatus(User receiver, ConnectionStatus status);
    List<ConnectionRequest> findBySenderAndStatus(User sender, ConnectionStatus status);
    List<ConnectionRequest> findBySenderOrReceiverAndStatus(User sender, User receiver, ConnectionStatus status);

}