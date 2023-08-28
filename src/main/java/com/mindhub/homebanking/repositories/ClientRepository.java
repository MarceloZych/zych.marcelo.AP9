package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
    Set<Client> findByLastName(String lastName);
    Client findByEmail(String email);

    /*boolean existsByRandomNumber(Long number);*/

}
