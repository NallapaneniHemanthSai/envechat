package com.envechat.backend.dto;

import com.envechat.backend.model.Room;
import com.envechat.backend.model.RoomMember;
import com.envechat.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public RoomSummaryDto toSummary(Room room) {
        int memberCount = countMembers(room);
        return RoomSummaryDto.builder()
                .id(room.getId())
                .name(room.getName())
                .description(room.getDescription())
                .isPrivate(room.isPrivate())
                .createdBy(room.getCreatedBy())
                .createdAt(room.getCreatedAt())
                .memberCount(memberCount)
                .build();
    }

    public RoomDetailDto toDetail(Room room) {
        List<RoomMemberSummaryDto> memberDtos = toMemberSummaries(room);
        return RoomDetailDto.builder()
                .id(room.getId())
                .name(room.getName())
                .description(room.getDescription())
                .isPrivate(room.isPrivate())
                .createdBy(room.getCreatedBy())
                .createdAt(room.getCreatedAt())
                .memberCount(memberDtos.size())
                .members(memberDtos)
                .build();
    }

    private List<RoomMemberSummaryDto> toMemberSummaries(Room room) {
        if (room.getMembers() == null || room.getMembers().isEmpty()) {
            return Collections.emptyList();
        }
        return room.getMembers().stream()
                .map(this::toMemberSummary)
                .collect(Collectors.toList());
    }

    private RoomMemberSummaryDto toMemberSummary(RoomMember member) {
        User user = member.getUser();
        return RoomMemberSummaryDto.builder()
                .userId(user != null ? user.getId() : null)
                .username(user != null ? user.getUsername() : null)
                .avatarUrl(user != null ? user.getAvatarUrl() : null)
                .role(member.getRole() != null ? member.getRole().name() : null)
                .build();
    }

    private static int countMembers(Room room) {
        if (room.getMembers() == null) {
            return 0;
        }
        return room.getMembers().size();
    }
}
