package com.develop.web.common.view.service;

import com.develop.web.common.view.dto.AccountDto;
import com.develop.web.domain.service.folder.service.RootFolderListFetcher;
import com.develop.web.domain.service.folder.service.ProgramFolderGroupFetcher;
import com.develop.web.domain.service.ingest.service.IngestListFetcher;
import com.develop.web.common.view.dto.CriteriaDto;
import com.develop.web.common.view.dto.PageDto;
import com.develop.web.domain.service.ingest.mapper.UploadMapper;
import com.develop.web.domain.admin.notice.service.PostListFetcher;
import com.develop.web.domain.users.user.service.DetailMemberFetcher;
import com.develop.web.domain.users.program.service.ProgramListFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@RequiredArgsConstructor
@Component("ingestPageFetcher")
public class IngestPageFetcher implements PageingService{
    private final PostListFetcher postListFetcher;
    private final DetailMemberFetcher detailMemberFetcher;
    private final ProgramListFetcher programListFetcher;
    private final ProgramFolderGroupFetcher programFolderGroupFetcher;
    private final IngestListFetcher ingestListFetcher;
    private final RootFolderListFetcher rootFolderListFetcher;

    private final UploadMapper uploadMapper;

    @Override
    public void fetchPageing(CriteriaDto criteriaDto, AccountDto accountDto, Model model) {
        String account = accountDto.getAccount();
        Integer programId = accountDto.getProgramId();
        Integer rankId = accountDto.getRankId();

        int countTotal = uploadMapper.selectIngestCount();
        PageDto pageDto = new PageDto(countTotal, 10, criteriaDto);

        model.addAttribute("NoticeList", postListFetcher.getPost());
        model.addAttribute("ProgramList", programListFetcher.getBelongProgram(programId, rankId));
        model.addAttribute("MemberInfo", detailMemberFetcher.getMember(account));
        model.addAttribute("folderRootList", rootFolderListFetcher.getFolder());
        model.addAttribute("ProgramFolderList", programFolderGroupFetcher.getProgramFolder(programId, rankId));
        model.addAttribute("IngestRequestList", ingestListFetcher.getIngestRequestList(criteriaDto));

        model.addAttribute("pageMaker", pageDto);

    }
}
