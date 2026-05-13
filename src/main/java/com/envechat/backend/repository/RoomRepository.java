package com.envechat.backend.repository;

import com.envechat.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByName(String name);
    Optional<Room> findByName(String name);
    Optional<Room> findByInviteCode(String inviteCode);

    /**
     * Eager-loads members and users so JSON serialization does not touch uninitialized
     * lazy associations after the persistence context closes (avoids LazyInitializationException
     * when open-in-view is false or the session ends before the response is written).
     */
    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.members m LEFT JOIN FETCH m.user")
    List<Room> findAllWithMembersAndUsers();

    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.members m LEFT JOIN FETCH m.user WHERE r.id = :id")
    Optional<Room> findByIdWithMembersAndUsers(@Param("id") Long id);
}