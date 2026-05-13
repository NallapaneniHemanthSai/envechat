package com.envechat.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RoomDetailDto {

    private Long id;
    private String name;
    private String description;
    private boolean isPrivate;
    private String createdBy;
    private LocalDateTime createdAt;
    private int memberCount;
    private List<RoomMemberSummaryDto> members;
}
