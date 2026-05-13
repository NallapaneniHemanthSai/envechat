package com.envechat.backend.repository;

import com.envechat.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByName(String name);
    Optional<Room> findByName(String name);
    Optional<Room> findByInviteCode(String inviteCode);
}