package com.be.inssagram.domain.notification.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final TokenProvider tokenProvider;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader("Authorization") String token) {
        InfoResponse member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        return notificationService.subscribe(member.member_id());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<InfoResponse> deleteNotification(@RequestHeader("Authorization") String token,
                                                        @PathVariable Long id){
        InfoResponse member = InfoResponse.fromEntity(tokenProvider.getMemberFromToken(token));
        notificationService.deleteNotification(member.member_id(), id);
        return ApiResponse.createMessage("삭제가 되었습니다");
    }
}
