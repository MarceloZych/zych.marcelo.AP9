package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplementation implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<LoanDTO> getLoansService(){
        return loanRepository.findAll()
                            .stream()
                            .map(loans -> new LoanDTO(loans))
                            .collect(Collectors.toList());
    }

    @Override
    public Loan findIdService(Long id){
        return loanRepository.findLoanById(id);
    }

    @Override
    public boolean existLoanIdService(Long id){
        return loanRepository.existsById(id);
    }
}
