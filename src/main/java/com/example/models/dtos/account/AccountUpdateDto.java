package com.example.models.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateDto {

    private Long id;
    private boolean active;

}
