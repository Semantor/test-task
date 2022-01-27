package com.haulmont.testtask.backend.util;

import com.haulmont.testtask.model.dto.ClientDTO;
import com.haulmont.testtask.model.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientExchanger {

    public ClientDTO exchangeToDTO(Client client) {
        return new ClientDTO()
                .firstname(client.getFirstName())
                .lastname(client.getLastName())
                .patronymic(client.getPatronymic())
                .phone(client.getPhoneNumber())
                .email(client.getEmail())
                .passport(client.getPassport());
    }

    public Client exchangeToDO(ClientDTO clientDTO) {
        return Client.builder()
                .firstName(clientDTO.getFirstname())
                .lastName(clientDTO.getLastname())
                .patronymic(clientDTO.getPatronymic())
                .phoneNumber(clientDTO.getPhone())
                .email(clientDTO.getEmail())
                .passport(clientDTO.getPassport())
                .build();
    }
}
