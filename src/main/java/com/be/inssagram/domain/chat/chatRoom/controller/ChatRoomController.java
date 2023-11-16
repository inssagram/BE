package com.be.inssagram.domain.chat.chatRoom.controller;

import com.be.inssagram.domain.chat.chatRoom.dto.request.ChatRoomRequest;
import com.be.inssagram.domain.chat.chatRoom.dto.response.ChatRoomResponse;
import com.be.inssagram.domain.chat.chatRoom.dto.response.ChatRoomResponse2;
import com.be.inssagram.domain.chat.chatRoom.entity.ChatRoom;
import com.be.inssagram.domain.chat.chatRoom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @Transactional
    @PostMapping("/create")
    @ResponseBody
    public ChatRoomResponse2 createRoom(
//            @RequestHeader("Authorization") String token,
            @RequestBody ChatRoomRequest request) {
        return chatRoomService.createChatRoom(request);
    }

    // 특정 채팅방 조회
    @GetMapping("/{roomId}")
    @ResponseBody
    public ChatRoomResponse2 searchRoom(
//            @RequestHeader("Authorization") String token,
            @PathVariable Long roomId) {
        return chatRoomService.findRoomById(roomId);
    }

    @Transactional
    @GetMapping("/list")
    @ResponseBody
    public List<ChatRoomResponse2> searchRoomsWithMemberId(
            @RequestParam(value = "member-id") Long memberId
    ) {
        return chatRoomService.searchRoomsWithMemberId(memberId);
    }

    @Transactional
    @GetMapping("/one-to-one")
    @ResponseBody
    public ChatRoomResponse2 searchRoomOneToOne(
            @RequestParam(value = "first-member-id") Long firstMemberId,
            @RequestParam(value = "second-member-id") Long secondMemberId
    ) {
        return chatRoomService.findByFirstParticipantIdAndSecondParticipantId(
                firstMemberId, secondMemberId);
    }

    // 채팅 리스트 화면
    @GetMapping("")
    public String rooms(Model model) {
        return "/chat/room";
    }

    // 모든 채팅방 목록 반환
    @GetMapping("/all")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomService.searchRooms();
    }

    // 채팅방 입장 화면
    @GetMapping("/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }
}
