package com.be.inssagram.domain.member.entity;


import com.be.inssagram.domain.member.dto.request.UpdateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String password;
    private String jobField;
    private String gender;

    public void updateFields(UpdateRequest updateRequest) {
        if (updateRequest.getEmail() != null) {
            this.email = updateRequest.getEmail();
        }
        if (updateRequest.getNickname() != null) {
            this.nickname = updateRequest.getNickname();
        }
        if (updateRequest.getPassword() != null) {
            this.password = updateRequest.getPassword();
        }
        if (updateRequest.getJobField() != null) {
            this.jobField = updateRequest.getJobField();
        }
        if (updateRequest.getGender() != null) {
            this.gender = updateRequest.getGender();
        }
    }
}