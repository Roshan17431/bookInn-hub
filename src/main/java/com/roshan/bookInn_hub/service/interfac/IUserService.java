package com.roshan.bookInn_hub.service.interfac;

import com.roshan.bookInn_hub.dto.LoginRequest;
import com.roshan.bookInn_hub.dto.Response;
import com.roshan.bookInn_hub.entity.User;

public interface IUserService {

    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();
}
