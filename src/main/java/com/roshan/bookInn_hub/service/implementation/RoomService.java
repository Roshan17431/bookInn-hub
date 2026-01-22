package com.roshan.bookInn_hub.service.implementation;

import com.roshan.bookInn_hub.dto.Response;
import com.roshan.bookInn_hub.dto.RoomDTO;
import com.roshan.bookInn_hub.entity.Room;
import com.roshan.bookInn_hub.exception.OurException;
import com.roshan.bookInn_hub.repository.RoomRepository;
import com.roshan.bookInn_hub.security.Utils;
import com.roshan.bookInn_hub.service.interfac.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

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

    @Override
    public List<String> getAllRoomTypes(){
        return roomRepository.findDistinctRoomType();
    }

    @Override
    public Response getAllRooms(){
        Response response = new Response();
        try{
            List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<RoomDTO> dto = Utils.mapRoomListEntityToRoomListDTO(rooms);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(dto);
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error fetching all rooms: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId){
        Response response = new Response();
        try{
            Room room = roomRepository.findById(roomId).orElseThrow(()-> new OurException("User not found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Successful");
        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error deleting room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if (photo != null && !photo.isEmpty()) {
                imageUrl = awsS3Service.saveImageToS3(photo);
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomDescription(description);
            if (imageUrl != null) room.setRoomPhotoUrl(imageUrl);

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }
}
