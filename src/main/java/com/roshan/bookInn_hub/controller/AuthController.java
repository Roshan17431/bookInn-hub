package com.roshan.bookInn_hub.controller;


import com.roshan.bookInn_hub.dto.Response;
import com.roshan.bookInn_hub.service.interfac.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.roshan.bookInn_hub.entity.User;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user){
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
