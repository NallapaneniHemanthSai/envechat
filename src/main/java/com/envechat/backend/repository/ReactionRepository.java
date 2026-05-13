package com.envechat.backend.repository;

import com.envechat.backend.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByMessageId(Long messageId);
    Optional<Reaction> findByMessageIdAndUserIdAndEmoji(Long messageId, Long userId, String emoji);
    boolean existsByMessageIdAndUserIdAndEmoji(Long messageId, Long userId, String emoji);
    void deleteByMessageIdAndUserIdAndEmoji(Long messageId, Long userId, String emoji);
    long countByMessageIdAndEmoji(Long messageId, String emoji);
}
