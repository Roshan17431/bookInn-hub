package com.roshan.bookInn_hub.service.implementation;

import com.roshan.bookInn_hub.dto.Response;
import com.roshan.bookInn_hub.dto.RoomDTO;
import com.roshan.bookInn_hub.entity.Room;
import com.roshan.bookInn_hub.repository.RoomRepository;
import com.roshan.bookInn_hub.security.Utils;
import com.roshan.bookInn_hub.service.interfac.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice,String description) {

        Response response = new Response();
        try{
            Room room = new Room();
            String imageUrl = awsS3Service.saveImageToS3(photo);
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomDescription(description);
            room.setRoomPrice(roomPrice);

            Room savedRoom = roomRepository.save(room);
            RoomDTO dto = Utils.mapRoomEntityToRoomDTO(savedRoom);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(dto);
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred while adding room " + e.getMessage());
        }
        return response;

    }
}
