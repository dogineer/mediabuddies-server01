package com.develop.web.domain.member.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@RequiredArgsConstructor
@ToString
public class JoinUser {
    private final String account;
    private final String name;
    private final Integer gender;
    private final String phone;
    private final String email;
    private final Integer deptId;
    private final Integer posId;
    private final String des;
    private String password;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
