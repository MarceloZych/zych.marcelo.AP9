package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getClientService(){
        return clientRepository.findAll()
                                .stream()
                                .map(client -> new ClientDTO(client))
                                .collect(Collectors.toList());
    }
    @Override
    public ClientDTO getClientIDService(Long id){
        Client client = clientRepository.findById(id).orElse(null);
        return new ClientDTO(client);
    }
    @Override
    public Client findClientByEmailService(String email){
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClientService(Client client){
        clientRepository.save(client);
    }

    @Override
    public ClientDTO getCurrentClientService(String email){
        return new ClientDTO(clientRepository.findByEmail(email));
    }
}
