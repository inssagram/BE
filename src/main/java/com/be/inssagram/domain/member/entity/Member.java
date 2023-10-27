package com.be.inssagram.domain.member.entity;


import com.be.inssagram.domain.member.dto.request.UpdateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

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
    private String description;
    private String profilePic;
    private String password;
    private String role;
    private String companyName;

    public void updateFields(UpdateRequest updateRequest) {
        if (updateRequest.getEmail() != null) {
            this.email = updateRequest.getEmail();
        }
        if (updateRequest.getNickname() != null) {
            this.nickname = updateRequest.getNickname();
        }
        if (updateRequest.getProfilePic() != null) {
            this.profilePic = updateRequest.getProfilePic();
        }
        if (updateRequest.getDescription() != null) {
            this.description = updateRequest.getDescription();
        }
        if (updateRequest.getPassword() != null) {
            this.password = updateRequest.getPassword();
        }
        if (updateRequest.getCompanyName() != null) {
            this.companyName = updateRequest.getCompanyName();
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = this.getRole();
        SimpleGrantedAuthority simpleAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleAuthority);

        return authorities;
    }
}
