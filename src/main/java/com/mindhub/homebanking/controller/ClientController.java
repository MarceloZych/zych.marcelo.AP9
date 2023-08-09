package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientRepository clientRepository;
    @GetMapping("/clients")
    public List<ClientDTO> getClient(){
        List<Client> allClients = clientRepository.findAll();

        List<ClientDTO> clientDTOConvertedList = allClients
                                                        .stream()
                                                        .map(client -> new ClientDTO(client))
                                                        .collect(Collectors.toList());

        return clientDTOConvertedList;
        // return clientRepository.findAll();
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClientDTO(@PathVariable Long id){
        Client client = clientRepository.findById(id).orElse(null);
        return new ClientDTO(client);

    }
}
