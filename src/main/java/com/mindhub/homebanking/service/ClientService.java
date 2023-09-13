package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getClientService();

    ClientDTO getClientIDService(Long id);

    Client findClientByEmailService(String email);

    void saveClientService(Client client);

    ClientDTO getCurrentClientService(String email);
}
