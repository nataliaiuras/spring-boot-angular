package com.example.mapers;

import com.example.dtos.ClientDto;
import com.example.models.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toClient(ClientDto clientDto);

    ClientDto toClientDto(Client client);

    List<ClientDto> toClientDtos(List<Client> clients);

    void updateClient(@MappingTarget Client target, Client source);
}
