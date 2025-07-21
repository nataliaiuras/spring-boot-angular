package com.example.mapers;

import com.example.dtos.CredentialsDto;
import com.example.models.Credentials;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

    Credentials toCredentials(CredentialsDto credentialsDto);

    CredentialsDto toCredentialsDto(Credentials credentials);

    List<CredentialsDto> toCredentialsDtos(List<Credentials> credentials);

    void updateCredentials(@MappingTarget Credentials target, Credentials source);
}