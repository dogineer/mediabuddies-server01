package com.develop.web.common.view.service;

import com.develop.web.common.view.dto.AccountDto;
import com.develop.web.domain.service.folder.service.ProgramFolderFetcherService;
import com.develop.web.domain.service.clip.mapper.ClipMapper;
import com.develop.web.domain.service.clip.service.ClipDataListFetcher;
import com.develop.web.domain.admin.notice.service.PostListFetcher;
import com.develop.web.common.view.dto.CriteriaDto;
import com.develop.web.common.view.dto.PageDto;
import com.develop.web.domain.users.user.service.DetailMemberFetcher;
import com.develop.web.domain.users.program.service.ProgramListFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@RequiredArgsConstructor
@Component("clipPageFetcher")
public class ClipPageFetcher implements PageingService {
    private final PostListFetcher postListFetcher;
    private final DetailMemberFetcher detailMemberFetcher;
    private final ProgramListFetcher programListFetcher;
    private final ProgramFolderFetcherService programFolderFetcherService;
    private final ClipDataListFetcher clipDataListFetcher;

    private final ClipMapper clipMapper;

    @Override
    public void fetchPageing(CriteriaDto criteriaDto, AccountDto accountDto, Model model) {

        String account = accountDto.getAccount();
        Integer programId = accountDto.getProgramId();
        Boolean isAdmin = accountDto.getIsAdmin();

        int countTotal = clipMapper.selectClipCount();
        PageDto pageDto = new PageDto(countTotal, 10, criteriaDto);

        model.addAttribute("NoticeList", postListFetcher.getPost());
        model.addAttribute("ProgramList", programListFetcher.findProgram());
        model.addAttribute("MemberInfo", detailMemberFetcher.getMember(account));
        model.addAttribute("ProgramRootFolderList", programFolderFetcherService.findProgramFolderRoot(programId, isAdmin));
        model.addAttribute("ClipList", clipDataListFetcher.getClipList(criteriaDto));

        model.addAttribute("pageMaker", pageDto);
    }
}
