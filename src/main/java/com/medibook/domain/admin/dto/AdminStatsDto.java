package com.medibook.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminStatsDto {
    private long totalUsers;
    private long totalReservations;
}
