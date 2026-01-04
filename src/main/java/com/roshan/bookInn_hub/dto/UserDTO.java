package com.roshan.bookInn_hub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
}
