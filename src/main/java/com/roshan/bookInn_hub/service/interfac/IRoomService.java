package com.roshan.bookInn_hub.service.interfac;

import com.roshan.bookInn_hub.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface IRoomService {

    Response addNewRoom(MultipartFile photo, String roomType, BigDecimal price, String description);


}
