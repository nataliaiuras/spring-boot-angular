package com.example.models.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetadataDto {
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Long version;
}

