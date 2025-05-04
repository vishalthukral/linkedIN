package com.linkedin_clone_application.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "connection_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;
    
    @Column(nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    
    public enum Status {
        PENDING, ACCEPTED, REJECTED
    }
}