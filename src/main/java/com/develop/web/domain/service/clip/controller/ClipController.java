package com.develop.web.domain.service.clip.controller;

import com.develop.web.domain.service.clip.dto.ClipDto;
import com.develop.web.domain.service.clip.service.ClipDataFetcherService;
import com.develop.web.domain.service.clip.service.ClipContentTypeService;
import com.develop.web.domain.service.clip.service.ClipXmlCreateService;
import com.develop.web.common.view.dto.CriteriaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Tag(name = "미디어 > 클립", description = "클립을 위한 api")
@RequestMapping(value = "/s1/api/clip")
@RestController
public class ClipController {
    private final ClipDataFetcherService clipDataFetcherService;
    private final ClipXmlCreateService clipXmlCreateService;
    private final ClipContentTypeService clipContentTypeService;

    @Value("${CNPS.MC.URL}")
    private String mc;

    @Value("${app.thumbnail.dir:${user.home}/media-buddies/thumbnail/}")
    private String thumbnailDir;

    @GetMapping("/get-server02-url")
    @Operation(summary = "서버2", description = "서버 URL 가져오기")
    public String getServer02URL() {
        return mc;
    }

    @GetMapping("/list")
    @Operation(summary = "클립 리스트 확인", description = "서버에 저장된 클립 데이터 목록 확인")
    public List<ClipDto> clipDataList() {
        CriteriaDto criteriaDto = new CriteriaDto();

        return clipDataFetcherService.findClipList(criteriaDto);
    }

    @GetMapping("/check/file")
    @Operation(summary = "클립 존재 확인", description = "서버에 저장된 클립 메타데이터로 존재 유무 확인합니다.")
    public boolean checkFileExistenceCheck(@RequestParam String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    @GetMapping("/xml")
    @Operation(summary = "클립 xml 데이터 확인", description = "테스트중")
    public String showXml(@RequestParam("file") String file) throws ParserConfigurationException, TransformerException {
        return clipXmlCreateService.create("test", file);
    }

    @GetMapping("/mediaType")
    @Operation(summary = "미디어 타입 찾기", description = "영상의 미디어 타입을 구합니다.")
    public ResponseEntity<String> mediaTypeDetails(@RequestParam("filename") String filename) {
        String contentType = clipContentTypeService.findMediaFileContentType(filename);

        return ResponseEntity.ok()
            .body(contentType);
    }

    @GetMapping("/thumbnail")
    @Operation(summary = "썸네일", description = "로컬에 저장된 썸네일을 찾습니다.")
    public ResponseEntity<Resource> findThumbnail(@RequestParam("filename") String filename){
        try {
            String thumbnailPath = thumbnailDir + "/" + filename;
            File thumbnail = new File(thumbnailPath);
            Resource resource = new FileSystemResource(thumbnail);

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/streaming")
    @Operation(summary = "스트리밍", description = "로컬에 저장된 클립을 재생합니다.")
    public ResponseEntity<Resource> streamingClip(@RequestParam("clipPath") String clipPath) {
        try {
            File videoFile = new File(clipPath);

            if (videoFile.exists()) {
                String contentType = clipContentTypeService.findMediaFileContentType(clipPath);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(contentType));

                PreloadedResource resource = new PreloadedResource(videoFile);

                return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private static class PreloadedResource extends InputStreamResource {
        private final File file;

        PreloadedResource(File file) {
            super(openInputStream(file));
            this.file = file;
        }

        private static InputStream openInputStream(File file) {
            try {
                return new FileInputStream(file);
            } catch (IOException e) {
                throw new RuntimeException("미리 로드하기 위해 입력 스트림을 열지 못했습니다. file: " + file.getName(), e);
            }
        }

        @Override
        public long contentLength() {
            return file.length();
        }
    }
}
