package com.develop.web.domain.admin.program.controller;

import com.develop.web.domain.admin.program.dto.ProgramRecursionDto;
import com.develop.web.domain.admin.program.dto.ProgramPathDto;
import com.develop.web.domain.admin.program.service.*;
import com.develop.web.domain.member.program.dto.ProgramDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "어드민 > 프로그램&팀 관리", description = "유형 > 프로그램 > 팀")
@RequiredArgsConstructor
@RequestMapping(value = "/s1/api/admin/program")
public class AdminProgramController {
    private final ProgramService programService;

    @PostMapping("/add")
    @Operation(summary = "프로그램, 유형, 팀 추가하기",
        description = "부모ID가 1이면 유형, 부모ID가 유형ID일 경우 프로그램")
    public void programAdd(@RequestBody ProgramDto programDto) {
        programService.addProgram(programDto);
    }

    @DeleteMapping("/delete/{programId}")
    @Operation(summary = "프로그램 삭제하기", description = "프로그램을 삭제합니다.")
    public void deleteProgram(@PathVariable("programId") Integer programId) {
        programService.removeProgram(programId);
    }

    @GetMapping("/list/all")
    @Operation(summary = "전체 팀 리스트", description = "전체 팀 목록을 조회합니다.")
    public List<ProgramRecursionDto> programList() {
        return programService.findProgramList();
    }

    @GetMapping("/type/list")
    @Operation(summary = "프로그램 유형 리스트", description = "프로그램 유형을 조회합니다.")
    public List<ProgramDto> programTypeList(){
        return programService.findProgramTypeList();
    }

    @GetMapping("/find/program/{programId}")
    @Operation(summary = "프로그램 찾기", description = "프로그램을 찾습니다.")
    public List<ProgramDto> programDetails(@PathVariable Integer programId){
        return programService.findProgram(programId);
    }

    @GetMapping("/find/tree/{programId}")
    @Operation(summary = "프로그램 계층 리스트", description = "프로그램 계층을 조회합니다.")
    public List<ProgramPathDto> programTreeDetails(@PathVariable Integer programId){
        return programService.findProgramPathList(programId);
    }
}
