package com.picpay.api.services;

import com.picpay.api.model.user.User;
import com.picpay.api.model.user.UserType;
import com.picpay.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sander, BigDecimal amount) throws Exception {
        if (sander.getUserType() == UserType.MERCHANT){
            throw new Exception("Usuario do tipo lojista não está autorizado a realizar transação");
        }

        if (sander.getBalance().compareTo(amount) < 0 ){
            throw new Exception("saldo insuficiente");
        }
    }

    public User findUserById(Long id) throws Exception {
        return this.userRepository.findUserById(id).orElseThrow(() -> new Exception("Usuario não encontrado"));
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }
}
