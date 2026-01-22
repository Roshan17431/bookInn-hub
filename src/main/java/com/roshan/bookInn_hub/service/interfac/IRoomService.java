package com.roshan.bookInn_hub.service.interfac;

import com.roshan.bookInn_hub.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface IRoomService {

    Response addNewRoom(MultipartFile photo, String roomType, BigDecimal price, String description);

    List<String> getAllRoomTypes();

    Response getAllRooms();

    Response deleteRoom(Long roomId);

    Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo);

}
