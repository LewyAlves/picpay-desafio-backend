package com.picpay.api.services;

import com.picpay.api.dto.TransactionDto;
import com.picpay.api.model.transaction.Transaction;
import com.picpay.api.model.user.User;
import com.picpay.api.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RestTemplate restTemplate;

    public void createTransaction(TransactionDto dto) throws Exception {
        User sander = this.userService.findUserById(dto.sanderId());
        User receiver = this.userService.findUserById(dto.receiverId());

        userService.validateTransaction(sander,dto.value());

        if (this.authorizeTransaction(sander,dto.value())){
            throw new Exception("Transação não autorizada");
        }

        Transaction transaction = new Transaction();
        transaction.setAnount(dto.value());
        transaction.setSander(sander);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sander.setBalance(sander.getBalance().subtract(dto.value()));
        receiver.setBalance(receiver.getBalance().add(dto.value()));

        transactionRepository.save(transaction);
        userService.saveUser(sander);
        userService.saveUser(receiver);
    }

    public boolean authorizeTransaction(User sander, BigDecimal value){
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        } else return false;
    }
}
