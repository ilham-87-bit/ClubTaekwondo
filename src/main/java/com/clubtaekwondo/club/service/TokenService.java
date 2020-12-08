package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Token;

import java.util.List;

public interface TokenService {

    Token save(Token token);

    void delete(Token token);

    void deleteAllToken(List<Token> tokenList);
}
