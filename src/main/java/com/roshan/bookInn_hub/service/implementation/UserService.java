package com.roshan.bookInn_hub.service.implementation;

import com.roshan.bookInn_hub.repository.UserRepository;
import com.roshan.bookInn_hub.security.JWTUtils;
import com.roshan.bookInn_hub.service.interfac.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;


}
