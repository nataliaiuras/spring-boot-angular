package com.example.services;

import com.example.dtos.ClientDto;
import com.example.models.Client;
import com.example.exceptions.AppException;
import com.example.mapers.ClientMapper;
import com.example.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public List<ClientDto> allClients() {
        return clientMapper.toClientDtos(clientRepository.findAll());
    }

    public ClientDto createClient(ClientDto clientDto) {
        Client client = clientMapper.toClient(clientDto);

        Client savedClient = clientRepository.save(client);

        return clientMapper.toClientDto(savedClient);
    }

    public ClientDto updateClient(Long id, ClientDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new AppException("Client not found", HttpStatus.NOT_FOUND));
        clientMapper.updateClient(client, clientMapper
                .toClient(clientDto));
        Client savedClient = clientRepository.save(client);
        return clientMapper.toClientDto(savedClient);
    }

    public ClientDto patchClient(Long id, ClientDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new AppException("Client not found", HttpStatus.NOT_FOUND));

        if (clientDto.getFirstName() != null) {
            client.setFirstName(clientDto.getFirstName());
        }
        if (clientDto.getLastName() != null) {
            client.setLastName(clientDto.getLastName());
        }
        if (clientDto.getBirthDate() != null) {
            client.setBirthDate(clientDto.getBirthDate());
        }
        if (clientDto.getCnp() != null) {
            client.setCnp(clientDto.getCnp());
        }
        if (clientDto.getTelephoneNumber() != null) {
            client.setTelephoneNumber(clientDto.getTelephoneNumber());
        }
        if (clientDto.getEmail() != null) {
            client.setEmail(clientDto.getEmail());
        }
        if (clientDto.getCreatedDate() != null) {
            client.setCreatedDate(clientDto.getCreatedDate());
        }

        Client savedClient = clientRepository.save(client);

        return clientMapper.toClientDto(savedClient);
    }

    public ClientDto deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new AppException("Client not found", HttpStatus.NOT_FOUND));
        ClientDto clientDto = clientMapper.toClientDto(client);

        clientRepository.deleteById(id);

        return clientDto;
    }

    @Transactional
    public ClientDto getClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new AppException("Client not found", HttpStatus.NOT_FOUND));
       /* client.getBranch().getBank().getName();*/
        return clientMapper.toClientDto(client);
    }


   /* public ClientDto getClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow();
        // Force loading of relationships
        client.getBranch().getBank().getName(); // This loads all relationships
        return convertToDto(client);
    }*/

}
