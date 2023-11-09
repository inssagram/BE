package com.be.inssagram.domain.notification.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.notification.dto.response.NotificationResponse;
import com.be.inssagram.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final TokenProvider tokenProvider;

    @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long id) {
        return notificationService.subscribe(id);
    }


    @DeleteMapping("/{id}")
    public ApiResponse<InfoResponse> deleteNotification(@RequestHeader("Authorization") String token,
                                                        @PathVariable Long id){
        InfoResponse member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        notificationService.deleteNotification(member.member_id(), id);
        return ApiResponse.createMessage("삭제가 되었습니다");
    }

    @GetMapping("/all")
    @Transactional
    public ApiResponse<List<NotificationResponse>> getMyNotifications(@RequestHeader("Authorization") String token) {
        InfoResponse member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        return ApiResponse.createSuccessWithMessage(
                notificationService.getMyNotifications(member.member_id()), "모든 알림을 조회하셧습니다");
    }
}
