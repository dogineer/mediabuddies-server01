package com.develop.web.common.view.controller;

import com.develop.web.common.view.dto.CriteriaDto;
import com.develop.web.domain.admin.dept.dto.DeptDto;
import com.develop.web.domain.admin.dept.service.FindDeptList;
import com.develop.web.domain.admin.rank.dto.RankDto;
import com.develop.web.domain.admin.rank.service.RankListFetcher;
import com.develop.web.domain.service.clip.dto.ClipDto;
import com.develop.web.domain.service.clip.service.ClipDataListFetcher;
import com.develop.web.domain.service.folder.dto.FolderDto;
import com.develop.web.domain.service.folder.service.RootFolderListFetcher;
import com.develop.web.domain.service.ingest.dto.IngestListDto;
import com.develop.web.domain.service.ingest.service.IngestListFetcher;
import com.develop.web.domain.users.team.dto.TeamDto;
import com.develop.web.domain.users.team.service.TeamListFetcher;
import com.develop.web.domain.users.user.dto.MemberInfo;
import com.develop.web.domain.users.user.service.DetailMemberFetcher;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/common")
public class CommonApiController {
    private final RootFolderListFetcher rootFolderListFetcher;
    private final ClipDataListFetcher clipDataListFetcher;
    private final DetailMemberFetcher detailMemberFetcher;
    private final TeamListFetcher teamListFetcher;
    private final FindDeptList findDeptList;
    private final IngestListFetcher ingestListFetcher;
    private final RankListFetcher rankListFetcher;

    @GetMapping("/folder-list")
    @Operation(summary = "클립 페이지 > 폴더 데이터", description = "폴더 기초 데이터를 가져옵니다.")
    public List<FolderDto> pageClipFolderDataFetcher(){
        return rootFolderListFetcher.getFolder();
    }

    @GetMapping("/clip-list")
    @Operation(summary = "클립 페이지 > 미디어 데이터", description = "미디어 기초 데이터를 가져옵니다.")
    public List<ClipDto> pageClipMediaDataFetcher(CriteriaDto criteriaDto){
        return clipDataListFetcher.getClipList(criteriaDto);
    }

     @GetMapping("/ingest-list")
    @Operation(summary = "인제스트 페이지", description = "인제스트 데이터를 가져옵니다.")
    public List<IngestListDto> pageCommonIngestData(CriteriaDto criteriaDto) {
        return ingestListFetcher.getIngestRequestList(criteriaDto);
    }

    @GetMapping("/user-info")
    @Operation(summary = "모든 페이지", description = "입력한 유저의 정보를 가져옵니다.")
    public MemberInfo pageCommonMemberInfoData(String account) {
        return detailMemberFetcher.getMember(account);
    }

    @GetMapping("/team-list")
    @Operation(summary = "모든 페이지", description = "전체 팀 리스트를 가져옵니다.")
    public List<TeamDto> pageCommonTeamInfoData() {
        return teamListFetcher.getTeam();
    }

    @GetMapping("/dept-list")
    @Operation(summary = "모든 페이지", description = "전체 부서 리스트를 가져옵니다.")
    public List<DeptDto> pageCommonDeptInfoData() {
        return findDeptList.getDeptList();
    }

    @GetMapping("/rank-list")
    @Operation(summary = "모든 페이지", description = "랭크 리스트를 가져옵니다.")
    public List<RankDto> pageCommonRankData() {
        return rankListFetcher.getRankList();
    }
}