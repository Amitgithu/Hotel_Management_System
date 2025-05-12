package com.hotel.auth.service;

import com.hotel.auth.dto.LoginRequestDTO;
import com.hotel.auth.dto.LoginResponseDTO;
import com.hotel.auth.dto.RegisterRequestDTO;

public interface AuthService {
    String register(RegisterRequestDTO registerReques) ;
    LoginResponseDTO login(LoginRequestDTO loginRequest);
}
