package com.example.dtos;

import com.example.models.Branch;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class BankDto {

    private Long id;

    private String name;

    private String telephoneNumber;

    private String email;

    private String website;

    private List<Branch> branches;

}
