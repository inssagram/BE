package com.be.inssagram.domain.chat.chatRoom.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomRequest {
    private Long firstParticipantId;
    private Long secondParticipantId;
}
