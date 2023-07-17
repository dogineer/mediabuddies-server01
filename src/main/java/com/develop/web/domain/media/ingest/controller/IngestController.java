package com.develop.web.domain.media.ingest.controller;

import com.develop.web.domain.media.ingest.dto.ResultRequestData;
import com.develop.web.domain.media.ingest.service.CreateClipPost;
import com.develop.web.domain.media.ingest.service.CreateIngestPost;
import com.develop.web.domain.media.ingest.service.IngestListFetcher;
import com.develop.web.domain.media.ingest.dto.IngestRequestData;
import com.develop.web.domain.media.upload.service.CreateFileFromMultipartFile;
import com.develop.web.domain.page.dto.CriteriaDto;
import com.develop.web.domain.media.upload.dto.Metadata;
import com.develop.web.domain.media.upload.service.FileChecker;
import com.develop.web.domain.media.upload.service.UploadFileToServer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "영상 > 인제스트", description = "")
@Slf4j
@RequestMapping(value = "/media")
public class IngestController {
    private final IngestListFetcher ingestListFetcher;
    private final CreateIngestPost createIngestPost;
    private final UploadFileToServer uploadFileToServer;
    private final FileChecker fileChecker;
    private final CreateClipPost createClipPost;
    private final CreateFileFromMultipartFile createFileFromMultipartFile;

    @Value("${app.upload.dir:${user.home}/movies/mam/temp/}")
    private String TempDir;

    @PostMapping(value = "/ingest/add")
    @Operation(summary = "인제스트",
        description = "업로드 -> temp 임시 파일 생성 -> 미디어센터 서버로 이동 -> 영상 아카이브 저장 및 변환 -> 컨버팅 저장 -> 완료 )")

    public void ingestRequset(IngestRequestData ingestRequestData, HttpSession session) throws IOException {
        Resource mediaFiles = createFileFromMultipartFile.run(ingestRequestData.getFiles(), TempDir);;

        Integer memberId = session.getAttribute("empId").hashCode();
        Integer ingestId = ingestRequestData.getId();
        ingestRequestData.setMemberId(memberId);
        createIngestPost.addIngestRequest(ingestRequestData);

        try {
            fileChecker.fileNull(mediaFiles);
            ResultRequestData requestData = new ResultRequestData();

            uploadFileToServer
                .upload(mediaFiles, ingestId)
                .subscribe(resultMetadata -> {
                    requestData.ingest_id = ingestRequestData.getId();
                    requestData.team_id = (Integer) session.getAttribute("teamId");
                    requestData.folder_id = ingestRequestData.getFolder();
                    requestData.e_metadata_id = resultMetadata.id;
                    requestData.a_metadata_id = resultMetadata.id;

                    createClipPost.addClipPost(requestData);
                });

        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    @GetMapping(value = "/ingest/list")
    @Operation(summary = "인제스트 목록", description = "인제스트 목록 현황을 가져옵니다.")
    public List<IngestRequestData> ingestList() {
        CriteriaDto criteriaDto = new CriteriaDto();
        return ingestListFetcher.getIngestRequestList(criteriaDto);
    }
}
