package com.roshan.bookInn_hub.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDTO {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private int numOfAdults;

    private int numOfChildren;

    private int totalNumOfGuest;

    private String bookingConfirmationCode;


}
