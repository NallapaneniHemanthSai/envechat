package com.envechat.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RoomSummaryDto {

    private Long id;
    private String name;
    private String description;
    private boolean isPrivate;
    private String createdBy;
    private LocalDateTime createdAt;
    private int memberCount;
}
