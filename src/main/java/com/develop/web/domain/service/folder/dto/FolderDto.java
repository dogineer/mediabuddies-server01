package com.develop.web.domain.service.folder.dto;

import lombok.Data;

@Data
public class FolderDto {
    private String folderName;
    private Integer folderParentId;
    private Integer programId;
}
