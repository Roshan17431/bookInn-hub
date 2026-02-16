package com.roshan.bookInn_hub.controller;

import com.roshan.bookInn_hub.dto.Response;
import com.roshan.bookInn_hub.service.interfac.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewRoom(
            @RequestParam(value = "photo",required = false) MultipartFile photo,
            @RequestParam(value = "roomType",required = false)String  roomType,
            @RequestParam(value = "roomPrice",required = false) BigDecimal roomPrice,
            @RequestParam(value = "roomDescription",required = false)String roomDescription
    ){
        if (photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null || roomType.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields(photo, roomType,roomPrice)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = roomService.addNewRoom(photo, roomType, roomPrice, roomDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllUsers(){
        Response response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
