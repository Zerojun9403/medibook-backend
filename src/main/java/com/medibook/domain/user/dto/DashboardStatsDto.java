package com.medibook.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardStatsDto {
    private long totalReservations;
    private long completedReservations;
    private long upcomingReservations;
    private long cancelledReservations;
}
