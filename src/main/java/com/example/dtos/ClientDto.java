package com.example.dtos;

import com.example.models.Account;
import com.example.models.Branch;
import com.example.models.Credentials;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class ClientDto {

    private long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String cnp;
    private String telephoneNumber;
    private String email;
    private Date createdDate;
    private Credentials credentials;
    private List<Account> accounts;

}
