package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Token;
import com.clubtaekwondo.club.repository.TokenRepository;
import com.clubtaekwondo.club.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TokenServiceImpl implements TokenService {


    @Autowired
    private TokenRepository tokenRepository;


    @Override
    public Token save(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public void delete(Token token) {
        tokenRepository.delete(token);
    }
}
