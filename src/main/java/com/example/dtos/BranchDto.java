package com.example.dtos;

import com.example.models.Address;
import com.example.models.Bank;
import com.example.models.Client;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class BranchDto {

    private Long id;

    private String name;

    private String bicCode;

    private String swiftCode;

    private String telephoneNumber;

    private String email;

    private Address address;

    private List<Client> clients;
}
