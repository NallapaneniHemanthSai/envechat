
package com.envechat.backend.model;
 
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
 
@Data
@Entity
@Table(name = "rooms")
public class Room {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(unique = true, nullable = false)
    private String name;
 
    @Column(name = "created_by")
    private String createdBy;
 
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}