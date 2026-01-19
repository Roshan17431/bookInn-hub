package com.roshan.bookInn_hub.service.implementation;

import com.roshan.bookInn_hub.dto.BookingDTO;
import com.roshan.bookInn_hub.dto.Response;
import com.roshan.bookInn_hub.entity.Booking;
import com.roshan.bookInn_hub.entity.Room;
import com.roshan.bookInn_hub.entity.User;
import com.roshan.bookInn_hub.exception.OurException;
import com.roshan.bookInn_hub.repository.BookingRepository;
import com.roshan.bookInn_hub.repository.RoomRepository;
import com.roshan.bookInn_hub.repository.UserRepository;
import com.roshan.bookInn_hub.security.Utils;
import com.roshan.bookInn_hub.service.interfac.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();
        try{
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new IllegalArgumentException("Check out date cannot be before check in date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room not found"));
            User user = userRepository.findById(userId).orElseThrow(()-> new OurException("User not found"));

            List<Booking> existingBookings = room.getBookings();

            if(!roomIsAvailable(bookingRequest, existingBookings)){
                throw new OurException("Room not Available for selected date range");
            }
            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Booking Successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);;
        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Saving a booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode){

        Response response = new Response();

        try{
            Booking booking = bookingRepository
                    .findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(()-> new OurException("Booking not found"));

            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setStatusCode(200);
            response.setMessage("Booking Found");
            response.setBooking(bookingDTO);
        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error finding booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {

        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingList(bookingDTOList);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Getting all bookings: " + e.getMessage());

        }
        return response;
    }
}
