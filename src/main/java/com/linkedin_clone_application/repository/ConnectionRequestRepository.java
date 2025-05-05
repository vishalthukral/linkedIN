package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.enums.ConnectionStatus;
import com.linkedin_clone_application.model.ConnectionRequest;
import com.linkedin_clone_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer> {


    List<ConnectionRequest> findBySenderAndStatus(User sender, ConnectionStatus status);


    @Query("SELECT cr FROM ConnectionRequest cr " +
            "WHERE cr.receiver = :receiver AND cr.status = :status")
    List<ConnectionRequest> findByReceiverAndStatus(@Param("receiver") User receiver,
                                                    @Param("status") ConnectionStatus status);

    @Query("SELECT cr FROM ConnectionRequest cr " +
            "WHERE (cr.sender = :sender OR cr.receiver = :receiver) " +
            "AND cr.status = :status")
    List<ConnectionRequest> findBySenderOrReceiverAndStatus(@Param("sender") User sender,
                                                            @Param("receiver") User receiver,
                                                            @Param("status") ConnectionStatus status);

    @Query("SELECT COUNT(cr) > 0 FROM ConnectionRequest cr " +
            "WHERE ((cr.sender = :user1 AND cr.receiver = :user2) " +
            "   OR (cr.sender = :user2 AND cr.receiver = :user1)) " +
            "AND cr.status IN :statuses")
    boolean existsBetweenUsersWithStatuses(@Param("user1") User user1,
                                           @Param("user2") User user2,
                                           @Param("statuses") List<ConnectionStatus> statuses);
}