package com.roshan.bookInn_hub.security;

import com.roshan.bookInn_hub.dto.UserDTO;
import com.roshan.bookInn_hub.entity.User;

public class Utils {

    public static UserDTO mapUserEntityToUserDTO(User user){
        UserDTO userDTO = new UserDTO();

         userDTO.setId(user.getId());
         userDTO.setEmail(user.getEmail());
         userDTO.setFirstName(user.getFirstName());
         userDTO.setLastName(user.getLastName());
         userDTO.setAuthProvider(user.getAuthProvider() != null ? user.getAuthProvider().toString() : null);
         userDTO.setImageUrl(user.getImageUrl());

         return userDTO;
    }
}
