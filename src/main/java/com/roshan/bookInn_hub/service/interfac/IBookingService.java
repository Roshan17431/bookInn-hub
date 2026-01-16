package com.roshan.bookInn_hub.service.interfac;

import com.roshan.bookInn_hub.dto.Response;
import com.roshan.bookInn_hub.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
}
