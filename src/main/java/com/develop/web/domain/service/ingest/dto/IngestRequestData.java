package com.develop.web.domain.service.ingest.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class IngestRequestData {
    private Integer id;                 // 인제스트 ID
    private LocalDateTime createAt;     // 생성 날짜
    private Integer memberId;           // 멤버 ID
    private String title;               // 제목
    private Integer programId;          // 프로그램 아이디
    private String program;             // 프로그램명 (팀명)
    private Integer folder;             // 폴더
    private String name;                // 요청자
    private String phone;               // 전화번호
    private Codec codec;                // 요청코덱
    private LocalDateTime successAt;      // 성공 유무

    private MultipartFile files;        // 영상 데이타

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
