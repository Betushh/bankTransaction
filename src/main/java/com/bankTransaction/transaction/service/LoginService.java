package com.bankTransaction.transaction.service;

import com.bankTransaction.transaction.model.dto.login.LoginRequestDto;
import com.bankTransaction.transaction.model.dto.login.LoginResponseDto;

public interface LoginService {

    LoginResponseDto login(LoginRequestDto loginRequest);

    void logout(String username);

}
