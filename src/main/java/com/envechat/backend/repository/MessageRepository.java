package com.envechat.backend.repository;

import com.envechat.backend.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomIdOrderBySentAtAsc(String roomId);
    Page<Message> findByRoomIdAndIsDeletedFalseOrderBySentAtDesc(String roomId, Pageable pageable);
    long countByRoomId(String roomId);
}
