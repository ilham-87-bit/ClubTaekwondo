package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Token;

public interface TokenService {

    Token save(Token token);

    void delete(Token token);
}
