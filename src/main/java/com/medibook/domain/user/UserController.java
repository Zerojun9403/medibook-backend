package com.medibook.domain.user;

import com.medibook.domain.user.dto.UserProfileDto;
import com.medibook.domain.user.dto.DashboardStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMyProfile() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @GetMapping("/me/stats")
    public ResponseEntity<DashboardStatsDto> getMyStats() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(userService.getDashboardStats(userId));
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}