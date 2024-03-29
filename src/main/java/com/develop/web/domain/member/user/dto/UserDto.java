package com.develop.web.domain.member.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Integer id;                 // 유저 PK
    private Integer deptId;             // 부서 ID
    private Integer posId;              // 직위 ID
    private Integer programId;          // 팀 ID
    private String account;             // 유저 아이디
    private String name;                // 이름
    private String birth;               // 생년월일
    private Integer gender;             // 성별 (1 남 / 0 여)
    private String phone;               // 전화번호
    private String email;               // 이메일
    private String des;                 // 설명(사유)
    private LocalDateTime joined_at;    // 가입 날짜
    private LocalDateTime approved_at;  // 승인 날짜

    private String role;                // 권한
    private Integer del_fl;             // 유저 삭제 플래그

    private String password;            // 패스워드
    private String verifyPassword;      // 패스워드 확인
    private String changePassword;      // 패스워드 변경
}
